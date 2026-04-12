package by.itbatia.psp.personservice.util;

import by.itbatia.psp.personservice.entity.AddressEntity;
import by.itbatia.psp.personservice.entity.UserEntity;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class UserUtils {

    public static UserEntity buildUserEntity(String email, AddressEntity address) {
        return build(email, address);
    }

    public static UserEntity buildUserEntity(AddressEntity address) {
        return build("john.doe@example.com", address);
    }

    private static UserEntity build(String email, AddressEntity address) {
        return UserEntity.builder()
            .email(email)
            .firstName("John")
            .lastName("Doe")
            .address(address)
            .build();
    }
}
