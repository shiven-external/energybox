package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.dto.gateway.CreateGatewayResponseDto;
import com.energybox.backendcodingchallenge.dto.gateway.GetGatewaysRequestDto;
import com.energybox.backendcodingchallenge.dto.gateway.GetGatewaysResponseDto;
import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/gateway")
public class GatewayController {
  private final GatewayService gatewayService;

  @Autowired
  public GatewayController(GatewayService gatewayService, Neo4jDatabaseService neo4jDatabaseService) {
    this.gatewayService = gatewayService;
  }

  @ApiOperation(value = "get all gateways", response = GatewayEntity.class)
  @RequestMapping(method = RequestMethod.GET)
  public List<GetGatewaysResponseDto> get(@RequestBody() @Validated GetGatewaysRequestDto body) {
    Collection<GatewayEntity> gateways = this.gatewayService.getGateways(body.getPageNumber(),
        body.getItemsPerPage());
    List<GetGatewaysResponseDto> responseDtos = gateways.stream().map(
        gatewayEntity -> new ModelMapper().map(gatewayEntity, GetGatewaysResponseDto.class)
    ).collect(Collectors.toList());

    return responseDtos;
  }

  @ApiOperation(value = "create a gateway", response = GatewayEntity.class)
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity create() {
    GatewayEntity sensor = this.gatewayService.createGateway();

    CreateGatewayResponseDto responseDto = new ModelMapper()
        .map(sensor, CreateGatewayResponseDto.class);

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @ApiOperation(value = "get sensors connected to gateway", response = GatewayEntity.class)
  @RequestMapping(value = "/{id}/sensor", method = RequestMethod.GET)
  public ResponseEntity<Object> getSensors(
      @PathVariable(name="id") Long id
  ) {
    Collection<SensorEntity> sensors = this.gatewayService.getSensorsConnectedToGateway(id);

    Gson gson = new Gson();

    return new ResponseEntity<>(gson.toJson(sensors), HttpStatus.OK);
  }

  @ApiOperation(value = "get gateways with sensors of a specific type connected to it", response =
      GatewayEntity.class)
  @RequestMapping(value = "/queryBySensorType", method = RequestMethod.GET)
  public ResponseEntity<Object> getGatewaysWithConnectedSensorsOfType(
      @RequestParam(name="sensorType") String sensorType
  ) {
    Collection<GatewayEntity> gateways =
        this.gatewayService.getGatewaysConnectedToSensorType(sensorType);

    Gson gson = new Gson();

    return new ResponseEntity<>(gson.toJson(gateways), HttpStatus.OK);
  }
}

