package com.energybox.backendcodingchallenge.dto.gateway;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSensorsConnectedToGatewaysResponseDto {
  @NotNull
  Long id;

  String[] types;
}
