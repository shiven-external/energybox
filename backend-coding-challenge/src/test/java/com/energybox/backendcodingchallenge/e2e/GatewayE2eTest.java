package com.energybox.backendcodingchallenge.e2e;

import com.energybox.backendcodingchallenge.dto.gateway.CreateGatewayResponseDto;
import com.energybox.backendcodingchallenge.dto.gateway.GetSensorsConnectedToGatewaysResponseDto;
import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
class GatewayE2eTest {
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
	public void itShouldGetGateways() throws Exception {
		GatewayEntity gateway1 = new GatewayEntity();
		GatewayEntity gateway2 = new GatewayEntity();
		this.session.save(gateway1);
		this.session.save(gateway2);

		MvcResult result  = this.mockMvc
				.perform(
						get("/gateway")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{}")
				)
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		String expectedResponse = new Gson().toJson(new GatewayEntity[]{ gateway1, gateway2 });
		Assert.assertEquals(expectedResponse, responseBody);
	}

	@Test
	public void itShouldCreateAGateway() throws Exception {
		MvcResult result  = this.mockMvc
				.perform(post("/gateway"))
				.andDo(print())
				.andExpect(status().is(201))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		CreateGatewayResponseDto responseDto
				= new Gson().fromJson(responseBody, CreateGatewayResponseDto.class);

		GatewayEntity gateway = this.session.load(GatewayEntity.class, responseDto.getId());
		Assert.assertEquals(gateway.getId(), responseDto.getId());
	}

	@Test
	public void itShouldGetSensorsConnectedToGateway() throws Exception {
		GatewayEntity gateway = new GatewayEntity();
		SensorEntity sensor1 = new SensorEntity(new String[] { "temperature" });
		SensorEntity sensor2 = new SensorEntity(new String[] { "temperature" });

		sensor1.setGateway(gateway);
		sensor2.setGateway(gateway);

		this.session.save(sensor1);
		this.session.save(sensor2);
		this.session.save(gateway);


		String url = String.format("/gateway/%s/sensor", gateway.getId());

		MvcResult result  = this.mockMvc
				.perform(get(url))
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();


		Collection<SensorEntity> sensors = new HashSet<SensorEntity>();
		sensors.add(sensor2);
		sensors.add(sensor1);

		List<GetSensorsConnectedToGatewaysResponseDto> expectedResponseDto =
			Utils.<GetSensorsConnectedToGatewaysResponseDto>mapCollectionOntoDto(sensors,
					GetSensorsConnectedToGatewaysResponseDto.class);
		Collections.sort(expectedResponseDto, (d1, d2) -> {
			if(d1.getId() > d2.getId()) return -1;
			else if (d1.getId() < d2.getId()) return 1;
			return 0;
		});

		Type actualResponseDtoType = new TypeToken<ArrayList<GetSensorsConnectedToGatewaysResponseDto>>(){}.getType();
		List<GetSensorsConnectedToGatewaysResponseDto> actual = new Gson().fromJson(responseBody,
				actualResponseDtoType);
		Collections.sort(actual, (d1, d2) -> {
			if(d1.getId() > d2.getId()) return -1;
			else if (d1.getId() < d2.getId()) return 1;
			return 0;
		});
		Assert.assertEquals(expectedResponseDto, actual);
	}

	@Test
	public void itShouldGetGatewaysConnectedToASensorType() throws Exception {
		GatewayEntity gateway = new GatewayEntity();
		SensorEntity sensor1 = new SensorEntity(new String[] { "temperature" });
		SensorEntity sensor2 = new SensorEntity(new String[] { "electricity", "humidity" });
		SensorEntity sensor3 = new SensorEntity(new String[] { "electricity" });

		sensor1.setGateway(gateway);
		sensor2.setGateway(gateway);
		sensor3.setGateway(gateway);

		this.session.save(sensor1);
		this.session.save(sensor2);
		this.session.save(sensor3);
		this.session.save(gateway);
		this.session.save(new GatewayEntity());


		String url = String.format("/gateway/queryBySensorType?sensorType=electricity",
				gateway.getId());

		MvcResult result  = this.mockMvc
				.perform(get(url))
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		Assert.assertEquals(new Gson().toJson(new GatewayEntity[] { gateway }), responseBody);
	}
}
