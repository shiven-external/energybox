package com.energybox.backendcodingchallenge.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@NodeEntity("Sensor")
public class SensorEntity {
  public static String name = "Sensor";

  @Id
  @GeneratedValue
  @Index(unique = true)
  private Long id;

  private String[] types;

  @Relationship(type="CONNECTED_TO")
  private GatewayEntity gateway;

  public SensorEntity() {}
  public SensorEntity(String[] types) {
    this.types = types;
  }
}
