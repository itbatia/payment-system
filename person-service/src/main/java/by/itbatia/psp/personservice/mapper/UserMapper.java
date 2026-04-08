package by.itbatia.psp.personservice.mapper;

import by.itbatia.psp.common.dto.internal.UserCreateRequest;
import by.itbatia.psp.common.dto.internal.UserResponse;
import by.itbatia.psp.common.dto.internal.UserUpdateRequest;
import by.itbatia.psp.personservice.entity.UserEntity;
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
    uses = AddressMapper.class
)
public interface UserMapper {

    @Named("toUserEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "address", qualifiedByName = "toAddressEntity")
    @Mapping(target = "filled", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toUserEntity(UserCreateRequest request);

    @Named("updateUserFromRequest")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "filled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget UserEntity existingUser);

    @Named("toUserResponse")
    @Mapping(target = "address", source = "address", qualifiedByName = "toAddressResponse")
    UserResponse toUserResponse(UserEntity entity);
}
