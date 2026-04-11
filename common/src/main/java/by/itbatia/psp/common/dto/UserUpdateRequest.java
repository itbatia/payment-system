package by.itbatia.psp.common.dto;

import java.net.URI;
import java.util.Objects;
import by.itbatia.psp.common.dto.AddressUpdateRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserUpdateRequest
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class UserUpdateRequest {

  @NotNull @Valid 
  @Schema(name = "id", description = "Unique user identifier (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  private UUID id;

  @Size(min = 1, max = 32) 
  @Schema(name = "secret_key", description = "Secret key for internal authentication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secret_key")
  private @Nullable String secretKey = null;

  @NotNull @Size(min = 8, max = 1024) @jakarta.validation.constraints.Email 
  @Schema(name = "email", description = "Unique email address", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  private String email;

  @Size(min = 2, max = 32) 
  @Schema(name = "first_name", description = "First name of the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first_name")
  private @Nullable String firstName;

  @Size(min = 2, max = 32) 
  @Schema(name = "last_name", description = "Last name of the user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_name")
  private @Nullable String lastName;

  @Valid 
  @Schema(name = "address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  private @Nullable AddressUpdateRequest address;

  public UserUpdateRequest() {
    super();
  }

}

