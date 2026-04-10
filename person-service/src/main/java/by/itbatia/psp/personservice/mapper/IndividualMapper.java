package by.itbatia.psp.personservice.mapper;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.personservice.entity.IndividualEntity;
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
    uses = UserMapper.class
)
public interface IndividualMapper {

    @Named("toIndividualEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user", qualifiedByName = "toUserEntity")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    IndividualEntity toIndividualEntity(IndividualCreateRequest request);

    @Named("updateIndividualFromRequest")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user", qualifiedByName = "updateUserFromRequest")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateIndividualFromRequest(IndividualUpdateRequest request, @MappingTarget IndividualEntity existingEntity);

    @Named("toIndividualResponse")
    @Mapping(target = "user", source = "user", qualifiedByName = "toUserResponse")
    IndividualResponse toIndividualResponse(IndividualEntity entity);
}
