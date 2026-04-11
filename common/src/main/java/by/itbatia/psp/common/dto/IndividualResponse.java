package by.itbatia.psp.common.dto;

import java.net.URI;
import java.util.Objects;
import by.itbatia.psp.common.dto.UserResponse;
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
 * IndividualResponse
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class IndividualResponse {

  @NotNull @Valid 
  @Schema(name = "id", description = "Unique individual identifier (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  private UUID id;

  
  @Schema(name = "passport_number", description = "Passport number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passport_number")
  private @Nullable String passportNumber;

  
  @Schema(name = "phone_number", description = "Phone number in international format", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone_number")
  private @Nullable String phoneNumber;

  @NotNull @Valid 
  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("user")
  private UserResponse user;

  public IndividualResponse() {
    super();
  }

}

