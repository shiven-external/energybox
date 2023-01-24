package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SensorRepository extends Neo4jRepository {
  @Autowired
  public SensorRepository(Neo4jDatabaseService neo4jDatabaseService) {
    super(neo4jDatabaseService);
  }

  public List<SensorEntity> getSensorsWithType(String type) {
    String query = """
        Match (sensor: Sensor) WHERE $sensorType IN sensor.types
        RETURN ID(sensor) as id, sensor.types as types;
    """;

    return this.getSession().queryDto(query, Map.of("sensorType", type),
        SensorEntity.class);
  }
}

