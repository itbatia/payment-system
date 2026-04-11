package by.itbatia.psp.individualsapi.it;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import by.itbatia.psp.common.dto.ErrorResponse;
import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.individualsapi.client.PersonServiceClient;
import by.itbatia.psp.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.dto.UserLoginRequest;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.util.*;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthFlowIntegrationTest {

    @Container
    static final KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer("quay.io/keycloak/keycloak:26.5")
        .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("individuals-api.keycloak.base-url", KEYCLOAK_CONTAINER::getAuthServerUrl);
    }

    private static final String PASSWORD = "SecurePass1!";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PersonServiceClient personServiceClient;

    @Test
    @DisplayName("IT: Test successful auth flow: /registration -> 201, /me -> 200")
    void whenRegistrationAndGetMeWithValidToken_then200() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD);
        mockPersonServiceClient(email);

        // 1. Registration -> 201
        TokenResponse tokens = successfulRegistration(request);

        // 2. Me -> 200
        webTestClient.get()
            .uri("/api/v1/auth/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserInfoResponse.class)
            .value(user -> {
                assert user != null;
                assertThat(user.getEmail()).isEqualTo(email);
                assertThat(user.getId()).isNotBlank();
            });
    }

    @Test
    @DisplayName("IT: Login with invalid credential: /login -> 401")
    void whenLoginWithInvalidCredentials_then401() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        UserLoginRequest loginReq = UserLoginRequestUtil.build(email, PASSWORD);

        // 1. Login -> 401
        webTestClient.post()
            .uri("/api/v1/auth/login")
            .bodyValue(loginReq)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponse.class)
            .value(err -> {
                assert err != null;
                assertThat(err.getError()).isEqualTo("Invalid user credentials");
                assertThat(err.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            });
    }

    @Test
    @DisplayName("IT: Registration with existing email: /registration -> 409")
    void whenRegisterWithExistingEmail_then409() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD);
        mockPersonServiceClient(email);

        // 1. Registration -> 201
        successfulRegistration(request);

        // 2. Registration -> 409
        webTestClient.post()
            .uri("/api/v1/auth/registration")
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectBody(ErrorResponse.class)
            .value(err -> {
                assert err != null;
                assertThat(err.getError()).isEqualTo("User exists with same email");
                assertThat(err.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
            });
    }

    @Test
    @DisplayName("IT: Login after registration returns tokens: /registration -> 201, /login -> 200")
    void whenRegistrationAndLoginWithValidEmail_then200() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD);
        mockPersonServiceClient(email);

        // 1. Registration -> 201
        successfulRegistration(request);

        // 2. Login -> 200
        UserLoginRequest loginReq = UserLoginRequestUtil.build(email, PASSWORD);

        TokenResponse tokens = webTestClient.post()
            .uri("/api/v1/auth/login")
            .bodyValue(loginReq)
            .exchange()
            .expectStatus().isOk()
            .returnResult(TokenResponse.class)
            .getResponseBody()
            .blockFirst();

        assert tokens != null;
        assertThat(tokens).isNotNull();
        assertThat(tokens.getAccessToken()).isNotBlank();
    }

    @Test
    @DisplayName("IT: Refresh token returns new access token: /registration -> 201, /refresh-token -> 200")
    void whenRegistrationAndCorrectRefreshToken_then200() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD);
        mockPersonServiceClient(email);

        // 1. Registration -> 201
        TokenResponse tokens = successfulRegistration(request);

        // 2. Refresh-token -> 200
        TokenRefreshRequest refreshReq = TokenRefreshRequestUtil.build(tokens.getRefreshToken());

        TokenResponse newTokens = webTestClient.post()
            .uri("/api/v1/auth/refresh-token")
            .bodyValue(refreshReq)
            .exchange()
            .expectStatus().isOk()
            .returnResult(TokenResponse.class)
            .getResponseBody()
            .blockFirst();

        assert newTokens != null;
        assertThat(newTokens).isNotNull();
        assertThat(newTokens.getAccessToken()).isNotBlank();
        assertThat(newTokens.getAccessToken()).isNotEqualTo(tokens.getAccessToken());
    }

    @Test
    @DisplayName("IT: Invalid refresh token: /refresh-token -> 400")
    void whenInvalidRefreshToken_then400() {
        // given
        TokenRefreshRequest refreshReq = TokenRefreshRequestUtil.build();

        // 1. Refresh-token -> 400
        webTestClient.post()
            .uri("/api/v1/auth/refresh-token")
            .bodyValue(refreshReq)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class)
            .value(err -> {
                assert err != null;
                assertThat(err.getError()).isEqualTo("Invalid refresh token");
                assertThat(err.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            });
    }

    @Test
    @DisplayName("IT: Access /me without token: /me -> 401")
    void whenAccessMeWithoutToken_then401() {
        // 1. Me -> 401
        webTestClient.get()
            .uri("/api/v1/auth/me")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("IT: Access /me with invalid JWT: /me -> 401")
    void whenAccessMeWithInvalidToken_then401() {
        // 1. Me -> 401
        webTestClient.get()
            .uri("/api/v1/auth/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token.here")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("IT: Registration with malformed email: /registration -> 400")
    void whenRegisterWithMalformedEmail_then400() {
        // given
        String email = "malformed.email.here";
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD);

        // 1. Registration -> 400
        webTestClient.post()
            .uri("/api/v1/auth/registration")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class)
            .value(err -> {
                assert err != null;
                assertThat(err.getError()).isEqualTo("Email must be a valid email address");
                assertThat(err.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            });
    }

    @Test
    @DisplayName("IT: Registration with incorrect password confirmation: /registration -> 400")
    void whenRegisterWithIncorrectPasswordConfirmation_then400() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        String incorrectConfirmPassword = "incorrect.confirmation.here";
        IndividualCreateRequest request = IndividualCreateRequestUtil.build(email, PASSWORD, incorrectConfirmPassword);

        // 1. Registration -> 400
        webTestClient.post()
            .uri("/api/v1/auth/registration")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class)
            .value(err -> {
                assert err != null;
                assertThat(err.getError()).isEqualTo("Password and confirmation do not match");
                assertThat(err.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            });
    }

    private TokenResponse successfulRegistration(IndividualCreateRequest request) {
        TokenResponse tokens = webTestClient.post()
            .uri("/api/v1/auth/registration")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated()
            .returnResult(TokenResponse.class)
            .getResponseBody()
            .blockFirst();

        assert tokens != null;
        assertThat(tokens).isNotNull();
        assertThat(tokens.getAccessToken()).isNotBlank();

        return tokens;
    }

    private void mockPersonServiceClient(String email) {
        UserRegistrationRequest userRegistrationRequest = UserRegistrationRequestUtil.build(email, PASSWORD);

        when(personServiceClient.createIndividual(any(IndividualCreateRequest.class)))
            .thenReturn(Mono.just(userRegistrationRequest));
    }
}
