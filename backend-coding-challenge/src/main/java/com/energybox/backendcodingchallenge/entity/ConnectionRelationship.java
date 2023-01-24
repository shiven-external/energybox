package com.energybox.backendcodingchallenge.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@RelationshipEntity(type = "CONNECTED_TO")
public class ConnectionRelationship {
  @Id
  @GeneratedValue
  private Long relationshipId;
  @StartNode
  private SensorEntity sensor;
  @EndNode
  private GatewayEntity gateway;

  public ConnectionRelationship() {}
  public ConnectionRelationship(SensorEntity sensor, GatewayEntity gateway) {
    this.sensor = sensor;
    this.gateway = gateway;
  }
}
