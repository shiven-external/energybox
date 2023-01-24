package com.energybox.backendcodingchallenge.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity("Gateway")
public class GatewayEntity {
  public static String name = "Gateway";
  @Id
  @GeneratedValue
  @Index(unique = true)
  @Getter()
  @Setter()
  private Long id;

  @Getter()
  @Setter()
  @Relationship(type="CONNECTED_TO")
  private List<SensorEntity> sensors;

  public List<SensorEntity> getSafeSensors() {
    if(this.sensors == null) this.sensors = new ArrayList();

    return this.sensors;
  }
}

