package by.itbatia.psp.personservice.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import by.itbatia.psp.personservice.entity.AddressEntity;
import by.itbatia.psp.personservice.entity.CountryEntity;
import by.itbatia.psp.personservice.entity.IndividualEntity;
import by.itbatia.psp.personservice.entity.UserEntity;
import by.itbatia.psp.personservice.enums.Status;
import by.itbatia.psp.personservice.util.AddressUtils;
import by.itbatia.psp.personservice.util.IndividualUtils;
import by.itbatia.psp.personservice.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Batsian_SV
 */
@DataJpaTest
@Testcontainers
@DisplayName("Репозиторий IndividualRepository: интеграционные тесты")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IndividualRepositoryTest {

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

    private static final String UNIQUE_IDX_NAME_IN_DB = "users_email_key";

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestEntityManager entityManager;

    // ================================================================================================================================== //
    //                                                               CREATE                                                               //
    // ================================================================================================================================== //

    @Test
    @DisplayName("Позитивный кейс: сохранение нового Individual с User и Address → успешно")
    void givenValidIndividual_whenSaved_thenPersistedWithAllAssociations() {
        // given
        CountryEntity country = countryRepository.findById(1L).orElseThrow();
        AddressEntity address = AddressUtils.buildAddressEntity(country);
        UserEntity user = UserUtils.buildUserEntity(address);
        IndividualEntity individual = IndividualUtils.buildIndividualEntity(user);

        // when
        IndividualEntity saved = individualRepository.saveAndFlush(individual);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser()).isNotNull();
        assertThat(saved.getUser().getAddress()).isNotNull();
        assertThat(saved.getUser().getAddress().getCountry()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("Негативный кейс: попытка сохранить Individual с дублирующим email → DataIntegrityViolationException")
    void givenIndividualWithDuplicateEmail_whenSaved_thenThrowsDataIntegrityViolation() {
        // given
        String email = "duplicate@example.com";
        CountryEntity country = countryRepository.findById(1L).orElseThrow();

        AddressEntity address1 = AddressUtils.buildAddressEntity(country);
        UserEntity user1 = UserUtils.buildUserEntity(email, address1);
        IndividualEntity individual1 = IndividualUtils.buildIndividualEntity(user1);

        individualRepository.saveAndFlush(individual1);

        AddressEntity address2 = AddressUtils.buildAddressEntity(country);
        UserEntity user2 = UserUtils.buildUserEntity(email, address2);
        IndividualEntity individual2 = IndividualUtils.buildIndividualEntity(user2);

        // when + then
        assertThatThrownBy(() -> individualRepository.saveAndFlush(individual2))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining(UNIQUE_IDX_NAME_IN_DB);
    }

    // ================================================================================================================================== //
    //                                                                READ                                                                //
    // ================================================================================================================================== //

    @Test
    @DisplayName("Позитивный кейс: поиск Individual по ID → возвращает с User, Address, Country")
    void givenExistingIndividual_whenFindById_thenReturnWithUserAndAddressAndCountry() {
        // given
        CountryEntity country = countryRepository.findById(1L).orElseThrow();
        AddressEntity address = AddressUtils.buildAddressEntity(country);
        UserEntity user = UserUtils.buildUserEntity(address);
        IndividualEntity individual = IndividualUtils.buildIndividualEntity(user);

        UUID id = individualRepository.saveAndFlush(individual).getId();

        // when
        Optional<IndividualEntity> result = individualRepository.findById(id);

        // then
        assertThat(result).isPresent();
        IndividualEntity found = result.get();
        assertThat(found.getUser()).isNotNull();
        assertThat(found.getUser().getAddress()).isNotNull();
        assertThat(found.getUser().getAddress().getCountry()).isNotNull();
        assertThat(found.getUser().getAddress().getCountry().getName()).isEqualTo(country.getName());
    }

    @Test
    @DisplayName("Позитивный кейс: поиск Individual по email → возвращает запись")
    void givenExistingIndividual_whenFindByUserEmail_thenReturnIndividual() {
        // given
        String email = "test@test.com";
        CountryEntity country = countryRepository.findById(1L).orElse(null);
        AddressEntity address = AddressUtils.buildAddressEntity(country);
        UserEntity user = UserUtils.buildUserEntity(email, address);
        IndividualEntity individual = IndividualUtils.buildIndividualEntity(user);

        entityManager.persistAndFlush(individual);

        // when
        Optional<IndividualEntity> result = individualRepository.findByUserEmail(email);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Негативный кейс: поиск Individual по несуществующему email → возвращает пустой Optional")
    void givenNonExistingEmail_whenFindByUserEmail_thenReturnEmpty() {
        // given
        String nonexistentEmail = "nonexistent@example.com";

        // when
        Optional<IndividualEntity> result = individualRepository.findByUserEmail(nonexistentEmail);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Негативный кейс: поиск Individual по несуществующему ID → возвращает пустой Optional")
    void givenNonExistingId_whenFindById_thenReturnNotFound() {
        // given
        UUID nonexistentId = UUID.randomUUID();

        // when
        Optional<IndividualEntity> result = individualRepository.findById(nonexistentId);

        // then
        assertThat(result).isEmpty();
    }

    // ================================================================================================================================== //
    //                                                               UPDATE                                                               //
    // ================================================================================================================================== //

    @Test
    @DisplayName("Позитивный кейс: обновление Individual (изменение имени, фамилии, телефона) → успешно")
    void givenExistingIndividual_whenUpdate_thenFieldsAreUpdated() {
        // given
        CountryEntity country = countryRepository.findById(1L).orElseThrow();
        AddressEntity address = AddressUtils.buildAddressEntity(country);
        UserEntity user = UserUtils.buildUserEntity("old@example.com", address);
        IndividualEntity individual = IndividualUtils.buildIndividualEntity(user);
        UUID id = individualRepository.saveAndFlush(individual).getId();

        // when
        assert id != null;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newPhoneNumber = "+375299999999";
        String newEmail = "new@example.com";

        IndividualEntity updated = individualRepository.findById(id).orElseThrow();
        updated.getUser().setFirstName(newFirstName);
        updated.getUser().setLastName(newLastName);
        updated.getUser().setEmail(newEmail);
        updated.setPhoneNumber(newPhoneNumber);
        individualRepository.saveAndFlush(updated);

        // then
        IndividualEntity reloaded = individualRepository.findById(id).orElseThrow();
        assertThat(reloaded.getUser().getFirstName()).isEqualTo(newFirstName);
        assertThat(reloaded.getUser().getLastName()).isEqualTo(newLastName);
        assertThat(reloaded.getUser().getEmail()).isEqualTo(newEmail);
        assertThat(reloaded.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("Негативный кейс: обновление Individual с дублирующим email → DataIntegrityViolationException")
    void givenUpdateWithDuplicateEmail_thenThrowsUniqueConstraintViolation() {
        // given
        String email1 = "user1@example.com";
        String email2 = "user2@example.com";

        CountryEntity country = countryRepository.findById(1L).orElseThrow();
        AddressEntity address = AddressUtils.buildAddressEntity(country);

        UserEntity user1 = UserUtils.buildUserEntity(email1, address);
        UserEntity user2 = UserUtils.buildUserEntity(email2, address);

        IndividualEntity ind1 = IndividualUtils.buildIndividualEntity(user1);
        IndividualEntity ind2 = IndividualUtils.buildIndividualEntity(user2);

        individualRepository.saveAndFlush(ind1);
        UUID id2 = individualRepository.saveAndFlush(ind2).getId();
        assert id2 != null;

        // when + then
        assertThatThrownBy(() -> {
            IndividualEntity updated = individualRepository.findById(id2).orElseThrow();
            updated.getUser().setEmail(email1);
            individualRepository.saveAndFlush(updated);
        })
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining(UNIQUE_IDX_NAME_IN_DB);
    }
}
