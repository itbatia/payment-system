package by.itbatia.psp.individualsapi.it;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.itbatia.individualsapi.dto.ErrorResponse;
import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserInfoResponse;
import by.itbatia.individualsapi.dto.UserLoginRequest;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.config.KeycloakTestcontainerConfig;
import by.itbatia.psp.individualsapi.util.EmailUtil;
import by.itbatia.psp.individualsapi.util.TokenRefreshRequestUtil;
import by.itbatia.psp.individualsapi.util.UserLoginRequestUtil;
import by.itbatia.psp.individualsapi.util.UserRegistrationRequestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Batsian_SV
 */
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthFlowIntegrationTest extends KeycloakTestcontainerConfig {

    private static final String PASSWORD = "SecurePass1!";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("IT: Test successful auth flow: /registration -> 201, /me -> 200")
    void whenRegistrationAndGetMeWithValidToken_then200() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD);

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
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD);

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
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD);

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
    @DisplayName("IT: Refresh token returns new access token: /registration -> 201, refresh -> 200")
    void whenRegistrationAndCorrectRefreshToken_then200() {
        // given
        String email = EmailUtil.generateUniqueEmail();
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD);

        // 1. Registration -> 201
        TokenResponse tokens = successfulRegistration(request);

        // 2. Refresh -> 200
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
    @DisplayName("IT: Invalid refresh token: refresh -> 400")
    void whenInvalidRefreshToken_then400() {
        // given
        TokenRefreshRequest refreshReq = TokenRefreshRequestUtil.build();

        // 1. Refresh -> 400
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
    @DisplayName("IT: Access /me with invalid JWT: 401 with JSON error")
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
    void whenRegisterWithMalformedEmail_Then400() {
        // given
        String email = "malformed.email.here";
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD);

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
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(email, PASSWORD, incorrectConfirmPassword);

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

    private TokenResponse successfulRegistration(UserRegistrationRequest request) {
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
}
