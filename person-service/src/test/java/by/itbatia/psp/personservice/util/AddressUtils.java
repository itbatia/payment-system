package by.itbatia.psp.personservice.util;

import by.itbatia.psp.personservice.entity.AddressEntity;
import by.itbatia.psp.personservice.entity.CountryEntity;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class AddressUtils {

    public static AddressEntity buildAddressEntity(CountryEntity country) {
        return AddressEntity.builder()
            .address("Nezavisimosti Ave, 100")
            .zipCode("220030")
            .city("Minsk")
            .state("Minsk Region")
            .country(country)
            .build();
    }
}
