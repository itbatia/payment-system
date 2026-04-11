package by.itbatia.psp.personservice.mapper;

import by.itbatia.psp.common.dto.CountryResponse;
import by.itbatia.psp.personservice.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Batsian_SV
 */
@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN,
    suppressTimestampInGenerated = true
)
public interface CountryMapper {

    @Named("mapCountryIdToCountry")
    default CountryEntity mapCountryIdToCountry(Long countryId) {
        if (countryId == null) {
            return null;
        }
        return CountryEntity.builder()
            .id(countryId)
            .build();
    }

    @Named("toCountryResponse")
    CountryResponse toCountryResponse(CountryEntity entity);
}
