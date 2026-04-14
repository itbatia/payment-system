package by.itbatia.psp.personservice.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.personservice.Application;
import by.itbatia.psp.personservice.common.MockMvcHelper;
import by.itbatia.psp.personservice.common.TestContainersSupport;
import by.itbatia.psp.personservice.entity.CustomRevisionEntity;
import by.itbatia.psp.personservice.util.IndividualUtils;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Batsian_SV
 */
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Интеграционные тесты аудита изменений Individual")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndividualAuditIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = TestContainersSupport.createPostgresContainer();

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        TestContainersSupport.configurePostgresProperties(registry, POSTGRES_CONTAINER);
    }

    @Autowired
    private MockMvcHelper mockMvc;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("PUT /individuals: заголовок X-Request-Initiator обязателен → 400 при отсутствии")
    void whenUpdateWithoutInitiatorHeader_then400() throws Exception {
        // given
        IndividualUpdateRequest updateRequest = IndividualUtils.buildIndividualUpdateRequest(UUID.randomUUID());

        // when
        ResultActions result = mockMvc.performUpdatingWithoutHeader(updateRequest);

        // then
        result
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /individuals: значение X-Request-Initiator сохраняется в revinfo.updated_by")
    void whenUpdateWithInitiator_thenActorSavedInRevinfo() throws Exception {
        // given (создаём запись)
        IndividualCreateRequest createReq = IndividualUtils.buildIndividualCreateRequest();

        String content = mockMvc.performCreating(createReq)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(content, "$.id");

        // when (обновляем с заголовком)
        String initiatorEmail = "user@example.com";

        IndividualUpdateRequest updateReq = IndividualUtils.buildIndividualUpdateRequest(UUID.fromString(id));
        mockMvc.performUpdating(updateReq, initiatorEmail)
            .andExpect(status().isOk());

        // then (проверяем, что в revinfo есть запись с этим initiator)
        List<CustomRevisionEntity> revisions = entityManager
            .createQuery("SELECT r FROM CustomRevisionEntity r ORDER BY r.id DESC", CustomRevisionEntity.class)
            .setMaxResults(1)
            .getResultList();

        assertThat(revisions).hasSize(1);
        assertThat(revisions.getLast().getModifiedBy()).isEqualTo(initiatorEmail);
    }
}
