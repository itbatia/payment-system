package by.itbatia.psp.personservice.util;

import java.util.UUID;

import by.itbatia.psp.common.dto.AddressCreateRequest;
import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.common.dto.UserCreateRequest;
import by.itbatia.psp.common.dto.UserUpdateRequest;
import by.itbatia.psp.personservice.entity.IndividualEntity;
import by.itbatia.psp.personservice.entity.UserEntity;
import by.itbatia.psp.personservice.enums.Status;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class IndividualUtils {

    //-//-//-//-// -----------------------------------------------   Entity   ----------------------------------------------- //-//-//-//-//

    public static IndividualEntity buildIndividualEntity(UserEntity user) {
        return IndividualEntity.builder()
            .passportNumber("AB1234567")
            .phoneNumber("+375291234567")
            .user(user)
            .status(Status.ACTIVE)
            .build();
    }

    //-//-//-//-// ------------------------------------------   Create requests   ------------------------------------------- //-//-//-//-//

    public static IndividualCreateRequest buildIndividualCreateRequest() {
        return buildRequest("AB1234567", EmailUtil.generateUniqueEmail(), 1);
    }

    public static IndividualCreateRequest buildIndividualCreateRequest(long countryId) {
        return buildRequest("AB1234567", EmailUtil.generateUniqueEmail(), countryId);
    }

    public static IndividualCreateRequest buildIndividualCreateRequest(String email) {
        return buildRequest("AB1234567", email, 1);
    }

    public static IndividualCreateRequest buildIndividualCreateRequest(String passportNumber,
                                                                       String email,
                                                                       long countryId) {
        return buildRequest(passportNumber, email, countryId);
    }

    private static IndividualCreateRequest buildRequest(String passportNumber, String email, long countryId) {
        AddressCreateRequest address = new AddressCreateRequest();
        address.setCountryId(countryId);
        address.setAddress("Nezavisimosti Ave, 100");
        address.setZipCode("220030");
        address.setCity("Minsk");
        address.setState("Minsk Region");

        UserCreateRequest user = new UserCreateRequest();
        user.setEmail(email);
        user.setPassword("SecurePass1!");
        user.setConfirmPassword("SecurePass1!");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress(address);

        IndividualCreateRequest request = new IndividualCreateRequest();
        request.setPassportNumber(passportNumber);
        request.setPhoneNumber("+375291234567");
        request.setUser(user);

        return request;
    }

    //-//-//-//-// ------------------------------------------   Update requests   ------------------------------------------- //-//-//-//-//

    public static IndividualUpdateRequest buildIndividualUpdateRequest(String id, String userId, String email) {
        return buildRequest(UUID.fromString(id), UUID.fromString(userId), "AB1234567", "John", "Doe", email);
    }

    public static IndividualUpdateRequest buildIndividualUpdateRequest(UUID id) {
        return buildRequest(id, UUID.randomUUID(), "AB1234567", "John", "Doe", EmailUtil.generateUniqueEmail());
    }

    public static IndividualUpdateRequest buildIndividualUpdateRequest(String id,
                                                                       String userId,
                                                                       String passportNumber,
                                                                       String firstName,
                                                                       String lastName,
                                                                       String email) {
        return buildRequest(UUID.fromString(id), UUID.fromString(userId), passportNumber, firstName, lastName, email);
    }

    private static IndividualUpdateRequest buildRequest(UUID id,
                                                        UUID userId,
                                                        String passportNumber,
                                                        String firstName,
                                                        String lastName,
                                                        String email) {
        IndividualUpdateRequest request = new IndividualUpdateRequest();
        request.setId(id);
        request.setPassportNumber(passportNumber);

        UserUpdateRequest userUpdate = new UserUpdateRequest();
        userUpdate.setId(userId);
        userUpdate.setFirstName(firstName);
        userUpdate.setLastName(lastName);
        userUpdate.setEmail(email);
        request.setUser(userUpdate);

        return request;
    }
}
