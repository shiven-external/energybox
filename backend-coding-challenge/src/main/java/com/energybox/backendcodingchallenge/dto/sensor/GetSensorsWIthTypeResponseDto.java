package com.energybox.backendcodingchallenge.dto.sensor;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSensorsWIthTypeResponseDto {
  @NotNull
  Long id;

  String[] types;
}
