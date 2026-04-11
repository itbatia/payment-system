package by.itbatia.psp.individualsapi.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

/**
 * @author Batsian_SV
 */
@Data
@Builder
public class UserRegistrationRequest {

    private UUID userId;
    private String email;
    private String password;
    private String confirmPassword;
}
