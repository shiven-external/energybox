package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GatewayRepository extends Neo4jRepository {
  @Autowired
  public GatewayRepository(Neo4jDatabaseService neo4jDatabaseService) {
    super(neo4jDatabaseService);
  }

  public List<SensorEntity> getSensorsConnectedToGateway(Long gatewayId) {
    String query = """
        MATCH (sensor: Sensor)-[CONNECTED_TO]->(gateway: Gateway)
        WHERE ID(gateway) = $gatewayId RETURN ID(sensor) as id, sensor.types as types;
    """;

    return this.getSession().queryDto(query, Map.of("gatewayId", gatewayId),
        SensorEntity.class);
  }

  public List<GatewayEntity> getGatewaysConnectedToSensorType(String type) {
    String query = """
        MATCH (sensor: Sensor)-[CONNECTED_TO]->(gateway: Gateway)
        WHERE $sensorType IN sensor.types RETURN distinct ID(gateway) as id;
    """;

    return this.getSession().queryDto(query, Map.of("sensorType", type),
        GatewayEntity.class);
  }
}
