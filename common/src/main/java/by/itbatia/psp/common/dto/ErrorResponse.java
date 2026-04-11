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
 * ErrorResponse
 */
@lombok.Data

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-11T22:30:55.188080300+03:00[Europe/Minsk]", comments = "Generator version: 7.20.0")
public class ErrorResponse {

  @NotNull 
  @Schema(name = "error", description = "Human-readable error message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("error")
  private String error;

  @NotNull 
  @Schema(name = "status", description = "HTTP status code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  private Integer status;

  public ErrorResponse() {
    super();
  }

}

