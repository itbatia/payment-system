package by.itbatia.psp.common.dto;

import java.net.URI;
import java.util.Objects;
import by.itbatia.psp.common.dto.CountryResponse;
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
 * AddressResponse
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class AddressResponse {

  @NotNull @Valid 
  @Schema(name = "id", description = "Unique address identifier (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  private UUID id;

  
  @Schema(name = "address", description = "Street address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  private @Nullable String address;

  
  @Schema(name = "zip_code", description = "Postal code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zip_code")
  private @Nullable String zipCode;

  
  @Schema(name = "city", description = "City name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  private @Nullable String city;

  
  @Schema(name = "state", description = "State or region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  private @Nullable String state;

  @NotNull @Valid 
  @Schema(name = "country", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("country")
  private CountryResponse country;

  public AddressResponse() {
    super();
  }

}

