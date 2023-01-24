package com.energybox.backendcodingchallenge.dto.gateway;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetGatewaysResponseDto {
  @NotNull
  Long id;
}
