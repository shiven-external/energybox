package com.energybox.backendcodingchallenge.dto.gateway;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
public class GetGatewaysRequestDto {
  @Min(value = 0, message = "The value must greater than or equal to 0")
  int pageNumber = 0;

  @Positive(message = "The value must be positive")
  int itemsPerPage = 10;
}
