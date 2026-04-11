package by.itbatia.psp.common.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AddressCreateRequest
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class AddressCreateRequest {

  @NotNull 
  @Schema(name = "country_id", description = "Country ID from the reference table", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("country_id")
  private Long countryId;

  @Size(min = 10, max = 128) 
  @Schema(name = "address", description = "Street address (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  private @Nullable String address = null;

  @Size(min = 4, max = 32) 
  @Schema(name = "zip_code", description = "Postal code (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zip_code")
  private @Nullable String zipCode = null;

  @Size(min = 4, max = 32) 
  @Schema(name = "city", description = "City name (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  private @Nullable String city = null;

  @Size(min = 4, max = 32) 
  @Schema(name = "state", description = "State or region (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  private @Nullable String state = null;

  public AddressCreateRequest() {
    super();
  }

}

