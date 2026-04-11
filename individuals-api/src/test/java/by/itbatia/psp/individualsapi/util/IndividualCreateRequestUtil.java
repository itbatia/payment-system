package by.itbatia.psp.individualsapi.util;

import by.itbatia.psp.common.dto.AddressCreateRequest;
import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.UserCreateRequest;

/**
 * @author Batsian_SV
 */
public class IndividualCreateRequestUtil {

    public static IndividualCreateRequest build(String email, String password) {
        return build(email, password, password);
    }

    public static IndividualCreateRequest build(String email, String password, String confirmPassword) {
        IndividualCreateRequest individual = new IndividualCreateRequest();
        individual.setPassportNumber("AB1234567");
        individual.setPhoneNumber("+375291234567");

        UserCreateRequest user = new UserCreateRequest();
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setFirstName("Test");
        user.setLastName("User");

        AddressCreateRequest address = new AddressCreateRequest();
        address.setCountryId(1L);
        address.setAddress("Test St, 1");
        address.setZipCode("12345");
        address.setCity("Test City");
        address.setState("Test State");

        user.setAddress(address);
        individual.setUser(user);

        return individual;
    }
}
