package by.itbatia.psp.common.dto;

import java.net.URI;
import java.util.Objects;
import by.itbatia.psp.common.dto.UserUpdateRequest;
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
 * IndividualUpdateRequest
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class IndividualUpdateRequest {

  @NotNull @Valid 
  @Schema(name = "id", description = "Unique individual identifier (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  private UUID id;

  @NotNull @Size(max = 32) 
  @Schema(name = "passport_number", description = "Passport number (optional)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("passport_number")
  private String passportNumber;

  @NotNull @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") @Size(max = 32) 
  @Schema(name = "phone_number", description = "Phone number in international format (optional)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("phone_number")
  private String phoneNumber;

  @NotNull @Valid 
  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("user")
  private UserUpdateRequest user;

  public IndividualUpdateRequest() {
    super();
  }

}

