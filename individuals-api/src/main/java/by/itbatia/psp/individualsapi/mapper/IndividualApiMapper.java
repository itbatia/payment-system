package by.itbatia.psp.individualsapi.mapper;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
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
public interface IndividualApiMapper {

    @Named("toUserRegistrationRequest")
    default UserRegistrationRequest toUserRegistrationRequest(IndividualCreateRequest request, IndividualResponse response) {
        return UserRegistrationRequest.builder()
            .userId(response.getUser().getId())
            .email(request.getUser().getEmail())
            .password(request.getUser().getPassword())
            .confirmPassword(request.getUser().getConfirmPassword())
            .build();
    }
}
