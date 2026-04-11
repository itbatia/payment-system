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
 * CountryResponse
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class CountryResponse {

  @NotNull 
  @Schema(name = "id", description = "Internal unique country ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  private Long id;

  @NotNull 
  @Schema(name = "name", description = "Full country name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  private String name;

  @NotNull 
  @Schema(name = "alpha2", description = "ISO 3166-1 alpha-2 code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alpha2")
  private String alpha2;

  @NotNull 
  @Schema(name = "alpha3", description = "ISO 3166-1 alpha-3 code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alpha3")
  private String alpha3;

  public CountryResponse() {
    super();
  }

}

