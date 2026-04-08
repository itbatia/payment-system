package by.itbatia.psp.personservice.mapper;

import by.itbatia.psp.common.dto.internal.AddressCreateRequest;
import by.itbatia.psp.common.dto.internal.AddressResponse;
import by.itbatia.psp.common.dto.internal.AddressUpdateRequest;
import by.itbatia.psp.personservice.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
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
    suppressTimestampInGenerated = true,
    uses = CountryMapper.class
)
public interface AddressMapper {

    @Named("toAddressEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", source = "countryId", qualifiedByName = "mapCountryIdToCountry")
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    AddressEntity toAddressEntity(AddressCreateRequest request);

    @Named("toAddressEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", source = "countryId", qualifiedByName = "mapCountryIdToCountry")
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    AddressEntity toAddressEntity(AddressUpdateRequest request);

    @Named("updateAddressFromRequest")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromRequest(AddressUpdateRequest request, @MappingTarget AddressEntity existingAddress);

    @Named("toAddressResponse")
    @Mapping(target = "country", source = "country", qualifiedByName = "toCountryResponse")
    AddressResponse toAddressResponse(AddressEntity entity);
}
