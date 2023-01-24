
package com.energybox.backendcodingchallenge.e2e;

import com.energybox.backendcodingchallenge.dto.sensor.*;
import com.energybox.backendcodingchallenge.entity.ConnectionRelationship;
import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.TestOnly;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SensorE2eTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Neo4jDatabaseService neo4jDatabaseService;

  private Session session;

  @BeforeAll
  public void init(){
    this.session = this.neo4jDatabaseService.getSession();
  }

  @BeforeEach
  public void beforeEach() {
    this.session.purgeDatabase();
  }

  @Test
  public void itShouldGetSensors() throws Exception {
    this.session.deleteAll(SensorEntity.class);
    SensorEntity sensor1 = new SensorEntity(new String[] { "temperature" });
    SensorEntity sensor2 = new SensorEntity(new String[] { "temperature" });
    this.session.save(sensor1);
    this.session.save(sensor2);

    MvcResult result  = this.mockMvc
        .perform(
            get("/sensor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    String expectedResponse = new Gson().toJson(new SensorEntity[]{ sensor1, sensor2 });
    Assert.assertEquals(expectedResponse, responseBody);
  }
  @Test
  public void itShouldCreateSensor() throws Exception {
    CreateSensorRequestDto body = new CreateSensorRequestDto();
    body.setTypes(new String[] { "temp" });
    Gson gson = new Gson();

    MvcResult result  = this.mockMvc
        .perform(
            post("/sensor")
              .contentType(MediaType.APPLICATION_JSON)
              .content(gson.toJson(body)
            )
        )
        .andDo(print())
        .andExpect(status().is(201))
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    CreateSensorResponseDto responseDto
        = new Gson().fromJson(responseBody, CreateSensorResponseDto.class);

    Assert.assertArrayEquals(body.getTypes(), responseDto.getTypes());
    SensorEntity sensor = this.session.load(SensorEntity.class, responseDto.getId());
    Assert.assertEquals(sensor.getId(), responseDto.getId());
  }

  @Test
  @TestOnly
  public void itShouldConnectSensorToGateway() throws Exception {
    SensorEntity sensor = new SensorEntity(new String[] { "temperature" });
    GatewayEntity gateway = new GatewayEntity();
    this.session.save(sensor);
    this.session.save(gateway);

    String url = String.format("/sensor/%s/connect", sensor.getId());

    ConnectSensorToGatewayRequestDto requestDto = new ConnectSensorToGatewayRequestDto();
    requestDto.setGatewayId(gateway.getId());

    MvcResult result  = this.mockMvc.perform(
        post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new Gson().toJson(requestDto))
        )
        .andDo(print())
        .andExpect(status().is(201))
        .andReturn();

    ConnectSensorToGatewayResponseDto responseDto = new Gson().fromJson(
        result.getResponse().getContentAsString(), ConnectSensorToGatewayResponseDto.class);
    Assert.assertNotNull(responseDto.getRelationshipId());

    ConnectionRelationship connection = this.session.load(ConnectionRelationship.class,
        responseDto.getRelationshipId());

    Assert.assertEquals(gateway.getId(), connection.getGateway().getId());
    Assert.assertEquals(sensor.getId(), connection.getSensor().getId());
  }

  @Test
  public void itShouldGetSensorsWithASpecificType() throws Exception {
    this.session.deleteAll(SensorEntity.class);
    SensorEntity sensor1 = new SensorEntity(new String[] { "temperature" });
    SensorEntity sensor2 = new SensorEntity(new String[] { "humidity" });
    SensorEntity sensor3 = new SensorEntity(new String[] { "humidity" });
    this.session.save(sensor1);
    this.session.save(sensor2);
    this.session.save(sensor3);

    GetSensorsWithTypeRequestDto requestDto = new GetSensorsWithTypeRequestDto();
    requestDto.setType("humidity");

    MvcResult result  = this.mockMvc
        .perform(
            get("/sensor/queryByType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(requestDto))
        )
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn();

    String responseBody = result.getResponse().getContentAsString();

    Type actualResponseDtoType = new TypeToken<ArrayList<GetSensorsWIthTypeResponseDto>>(){}.getType();
    List<GetSensorsWIthTypeResponseDto> actual = new Gson().fromJson(responseBody,
        actualResponseDtoType);
    Collections.sort(actual, (d1, d2) -> {
      if(d1.getId() > d2.getId()) return -1;
      else if (d1.getId() < d2.getId()) return 1;
      return 0;
    });

    Collection<SensorEntity> sensors = new HashSet<SensorEntity>();
    sensors.add(sensor3);
    sensors.add(sensor2);

    List<GetSensorsWIthTypeResponseDto> expectedResponseDto =
        Utils.<GetSensorsWIthTypeResponseDto>mapCollectionOntoDto(sensors,
            GetSensorsWIthTypeResponseDto.class);

    Collections.sort(expectedResponseDto, (d1, d2) -> {
      if(d1.getId() > d2.getId()) return -1;
      else if (d1.getId() < d2.getId()) return 1;
      return 0;
    });

    Assert.assertEquals(expectedResponseDto, actual);
  }
}
