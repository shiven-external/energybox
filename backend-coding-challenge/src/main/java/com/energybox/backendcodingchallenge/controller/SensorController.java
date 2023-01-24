package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.dto.sensor.*;
import com.energybox.backendcodingchallenge.entity.ConnectionRelationship;
import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/sensor")
public class SensorController {

  private final SensorService sensorService;

  @Autowired
  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @ApiOperation(value = "get all sensors", response = GatewayEntity.class)
  @RequestMapping(method = RequestMethod.GET)
  public List<GetSensorsResponseDto> get(@RequestBody() @Validated GetSensorsRequestDto body) {

    Collection<SensorEntity> sensors = this.sensorService.getSensors(body.getPageNumber(),
        body.getItemsPerPage());
    List<GetSensorsResponseDto> responseDtos = sensors.stream().map(
        sensorEntity -> new ModelMapper().map(sensorEntity, GetSensorsResponseDto.class)
    ).collect(Collectors.toList());

    return responseDtos;
  }

  @ApiOperation(value = "create a sensor", response = GatewayEntity.class)
  @RequestMapping(method = RequestMethod.POST, consumes =
      MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity create(
      @RequestBody @Validated CreateSensorRequestDto body
  ) {
    SensorEntity sensor = this.sensorService.createSensor(body.getTypes());

    CreateSensorResponseDto responseDto = new ModelMapper()
        .map(sensor, CreateSensorResponseDto.class);

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @ApiOperation(value = "connect a sensor to a gateway", response = GatewayEntity.class)
  @RequestMapping(value = "/{sensorId}/connect", method = RequestMethod.POST, consumes =
      MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity connectSensorToGateway(
      @PathVariable(name="sensorId") Long sensorId,
      @RequestBody @Validated ConnectSensorToGatewayRequestDto requestDto
  ) {
    ConnectionRelationship connection = this.sensorService.connectSensorToGateway(sensorId,
        requestDto.getGatewayId());

    ModelMapper mm = new ModelMapper();
    ConnectSensorToGatewayResponseDto responseDto = mm.map(connection,
        ConnectSensorToGatewayResponseDto.class);

    return new ResponseEntity<>(new Gson().toJson(responseDto), HttpStatus.CREATED);
  }

  @ApiOperation(value = "get sensors with type", response = GatewayEntity.class)
  @RequestMapping(value="/queryByType", method = RequestMethod.GET)
  public List<GetSensorsWIthTypeResponseDto> getSensorsWithType(@RequestBody() @Validated GetSensorsWithTypeRequestDto body) {
    Collection<SensorEntity> sensors = this.sensorService.getSensorsWithType(body.getType());

    List<GetSensorsWIthTypeResponseDto> responseDtos = sensors.stream().map(
        sensorEntity -> new ModelMapper().map(sensorEntity, GetSensorsWIthTypeResponseDto.class)
    ).collect(Collectors.toList());

    return responseDtos;
  }
}
