package by.itbatia.psp.personservice.it;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.personservice.Application;
import by.itbatia.psp.personservice.entity.CountryEntity;
import by.itbatia.psp.personservice.entity.IndividualEntity;
import by.itbatia.psp.personservice.enums.Status;
import by.itbatia.psp.personservice.repository.CountryRepository;
import by.itbatia.psp.personservice.repository.IndividualRepository;
import by.itbatia.psp.personservice.util.EmailUtil;
import by.itbatia.psp.personservice.util.IndividualUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Batsian_SV
 */
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты IndividualControllerV1")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndividualControllerV1IntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");
    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IndividualRepository individualRepository;

    // ================================================================================================================================== //
    //                                                    CREATE — ПОЗИТИВНЫЙ СЦЕНАРИЙ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("POST /api/v1/individuals: успешное создание → 201 + корректный ответ")
    void givenValidRequest_whenCreate_thenReturns201AndIndividual() throws Exception {
        // given
        String passportNumber = "AB1234567";
        String email = "john.doe@example.com";
        long countryId = 1;

        CountryEntity country = countryRepository.findById(countryId).orElseThrow();
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest(passportNumber, email, countryId);

        // when
        ResultActions result = performCreating(request);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.passport_number").value(passportNumber))
            .andExpect(jsonPath("$.user.email").value(email))
            .andExpect(jsonPath("$.user.address.city").value(request.getUser().getAddress().getCity()))
            .andExpect(jsonPath("$.user.address.country.name").value(country.getName()));
    }

    // ================================================================================================================================== //
    //                                                    CREATE — НЕГАТИВНЫЕ СЦЕНАРИИ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("POST /api/v1/individuals: дублирующий email → 409 Conflict")
    void givenDuplicateEmail_whenCreate_thenReturns409() throws Exception {
        // given
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest();

        ResultActions result1 = performCreating(request);

        // when (повторная регистрация с тем же email)
        ResultActions result2 = performCreating(request);

        // then
        result1
            .andExpect(status().isCreated());

        result2
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("User with this email already exists"));
    }

    @Test
    @DisplayName("POST /api/v1/individuals: невалидный email → 400 Bad Request")
    void givenInvalidEmail_whenCreate_thenReturns400() throws Exception {
        // given
        String invalidEmail = "invalid-email";
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest(invalidEmail);

        // when
        ResultActions result = performCreating(request);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("user.email: must be a well-formed email address"));
    }

    @Test
    @DisplayName("POST /api/v1/individuals: несуществующий countryId → 404 Not Found")
    void givenNonExistingCountryId_whenCreate_thenReturns404() throws Exception {
        // given
        long notExistingCountryId = 99999L;
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest(notExistingCountryId);

        // when
        ResultActions result = performCreating(request);

        // then
        String errorMsg = String.format("Country by [id=%d] not found", notExistingCountryId);

        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value(errorMsg));
    }

    // ================================================================================================================================== //
    //                                                     READ — ПОЗИТИВНЫЕ СЦЕНАРИИ                                                     //
    // ================================================================================================================================== //

    @Test
    @DisplayName("GET /api/v1/individuals/{id}: существующий ID → 200")
    void givenExistingId_whenGetById_thenReturns200() throws Exception {
        // given
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest();
        ResultActions result1 = performCreating(request);

        String content = result1
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(content, "$.id");

        // when
        ResultActions result2 = performGetById(id);

        // then
        result2
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.user.email").value(request.getUser().getEmail()));
    }

    @Test
    @DisplayName("GET /api/v1/individuals/email/{email}: существующий email → 200")
    void givenExistingEmail_whenGetByEmail_thenReturns200() throws Exception {
        // given
        String email = "findme@example.com";
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest(email);

        performCreating(request)
            .andExpect(status().isCreated());

        // when
        ResultActions result = performGetByEmail(email);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.email").value(email));
    }

    // ================================================================================================================================== //
    //                                                     READ — НЕГАТИВНЫЕ СЦЕНАРИИ                                                     //
    // ================================================================================================================================== //

    @Test
    @DisplayName("GET /api/v1/individuals/{id}: несуществующий ID → 404")
    void givenNonExistingId_whenGetById_thenReturns404() throws Exception {
        // given
        UUID notExistingId = UUID.randomUUID();

        // when
        ResultActions result = performGetById(notExistingId.toString());

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value(containsString("not found")));
    }

    @Test
    @DisplayName("GET /api/v1/individuals/email/{email}: несуществующий email → 404")
    void givenNonExistingEmail_whenGetByEmail_thenReturns404() throws Exception {
        // given
        String nonExistingEmail = EmailUtil.generateUniqueEmail();

        // when
        ResultActions result = performGetByEmail(nonExistingEmail);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value(containsString("not found")));
    }

    // ================================================================================================================================== //
    //                                                    UPDATE — ПОЗИТИВНЫЕ СЦЕНАРИИ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("PUT /api/v1/individuals: успешное обновление → 200 + обновлённые данные")
    void givenExistingIndividual_whenUpdate_thenReturns200AndUpdatedData() throws Exception {
        // given
        IndividualCreateRequest createRequest = IndividualUtils.buildValidIndividualCreateRequest();

        String createdContent = performCreating(createRequest)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(createdContent, "$.id");
        String userId = JsonPath.read(createdContent, "$.user.id");
        String originalEmail = JsonPath.read(createdContent, "$.user.email");

        // Подготавливаем запрос на обновление
        String passportNumber = "CD9876543";
        String firstName = "Jane";
        String lastName = "Smith";

        IndividualUpdateRequest updateRequest = IndividualUtils.buildValidIndividualUpdateRequest(
            id, userId, passportNumber, firstName, lastName, originalEmail
        );

        // when
        ResultActions result = performUpdating(updateRequest);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.passport_number").value(passportNumber))
            .andExpect(jsonPath("$.user.first_name").value(firstName))
            .andExpect(jsonPath("$.user.last_name").value(lastName));
    }

    // ================================================================================================================================== //
    //                                                    UPDATE — НЕГАТИВНЫЕ СЦЕНАРИИ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("PUT /api/v1/individuals: обновление несуществующего ID → 404 Not Found")
    void givenNonExistingId_whenUpdate_thenReturns404() throws Exception {
        // given
        UUID notExistingId = UUID.randomUUID();
        IndividualUpdateRequest updateRequest = IndividualUtils.buildValidIndividualUpdateRequest(notExistingId);

        // when
        ResultActions result = performUpdating(updateRequest);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value(containsString("not found")));
    }

    @Test
    @DisplayName("PUT /api/v1/individuals: обновление с дублирующим email → 409 Conflict")
    void givenUpdateWithDuplicateEmail_whenUpdate_thenReturns409() throws Exception {
        // given
        String sharedEmail = "shared@example.com";
        String otherEmail = "other@example.com";

        // Создаём первого пользователя
        IndividualCreateRequest request1 = IndividualUtils.buildValidIndividualCreateRequest(sharedEmail);
        performCreating(request1)
            .andExpect(status().isCreated());

        // Создаём второго пользователя
        IndividualCreateRequest request2 = IndividualUtils.buildValidIndividualCreateRequest(otherEmail);
        String individual2 = performCreating(request2)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String id2 = JsonPath.read(individual2, "$.id");
        String userId2 = JsonPath.read(individual2, "$.user.id");


        // when (пытаемся обновить второго пользователя, установив email первого)
        IndividualUpdateRequest updateRequest = IndividualUtils.buildValidIndividualUpdateRequest(id2, userId2, sharedEmail);
        ResultActions result = performUpdating(updateRequest);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("User with this email already exists"));
    }

    @Test
    @DisplayName("PUT /api/v1/individuals: отсутствует обязательное поле ID → 400 Bad Request")
    void givenUpdateWithoutId_whenUpdate_thenReturns400() throws Exception {
        // given
        UUID individualId = null;
        IndividualUpdateRequest updateRequest = IndividualUtils.buildValidIndividualUpdateRequest(individualId);

        // when
        ResultActions result = performUpdating(updateRequest);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value(containsString("must not be null"))); // @NotNull на id
    }

    // ================================================================================================================================== //
    //                                                    DELETE — ПОЗИТИВНЫЙ СЦЕНАРИЙ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("DELETE /api/v1/individuals/{id}: успешное удаление → 204 No Content")
    void givenExistingIndividual_whenDelete_thenReturns204() throws Exception {
        // given
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest();
        String individual = performCreating(request)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(individual, "$.id");

        // when
        ResultActions result = performDeleting(id);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent());

        IndividualEntity deletedIndividual = individualRepository.findById(UUID.fromString(id)).orElseThrow();
        assertEquals(Status.DELETED, deletedIndividual.getStatus());
        assertNotNull(deletedIndividual.getUser().getAddress().getArchivedAt());
    }

    // ================================================================================================================================== //
    //                                                    DELETE — НЕГАТИВНЫЕ СЦЕНАРИИ                                                    //
    // ================================================================================================================================== //

    @Test
    @DisplayName("DELETE /api/v1/individuals/{id}: удаление несуществующего ID → 404 Not Found")
    void givenNonExistingId_whenDelete_thenReturns404() throws Exception {
        String notExistingId = UUID.randomUUID().toString();

        // when
        ResultActions result = performDeleting(notExistingId);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value(containsString("not found")));
    }

    @Test
    @DisplayName("DELETE /api/v1/individuals/{id}: повторное удаление уже удалённого → 400 Bad Request")
    void givenAlreadyDeletedIndividual_whenDeleteAgain_thenReturns400() throws Exception {
        // given
        IndividualCreateRequest request = IndividualUtils.buildValidIndividualCreateRequest();

        String individual = performCreating(request)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(individual, "$.id");

        // Первое удаление
        performDeleting(id);

        // when (Повторное удаление)
        ResultActions result = performDeleting(id);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value(String.format("Individual [id=%s] has already been deleted", id)));
    }

    // ================================================================================================================================== //
    //                                                           COMMON METHODS                                                           //
    // ================================================================================================================================== //

    private ResultActions performCreating(IndividualCreateRequest request) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    private ResultActions performGetById(String id) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/individuals/{id}", id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
        );
    }

    private ResultActions performGetByEmail(String email) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/individuals/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
        );
    }

    private ResultActions performUpdating(IndividualUpdateRequest request) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    private ResultActions performDeleting(String id) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/individuals/{id}", id)
        );
    }
}
