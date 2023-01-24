package com.energybox.backendcodingchallenge.dto.sensor;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetSensorsWithTypeRequestDto {
  @NotNull
  String type;
}
