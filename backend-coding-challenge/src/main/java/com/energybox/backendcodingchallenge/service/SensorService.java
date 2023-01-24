package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.entity.ConnectionRelationship;
import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import org.neo4j.ogm.cypher.query.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;

  private final int maxItemsPerPage = 100;

  @Autowired
  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }
  public Collection<SensorEntity> getSensors(int pageNumber, int inputtedItemsPerPage) {
    int itemsPerPage = Math.min(inputtedItemsPerPage, this.maxItemsPerPage);

    return this.sensorRepository.getSession().loadAll(SensorEntity.class,
        new Pagination(pageNumber, itemsPerPage));
  }

  public SensorEntity createSensor(String[] types) {
    SensorEntity sensor = new SensorEntity(types);
    this.sensorRepository.getSession().save(sensor);

    return sensor;
  }

  public ConnectionRelationship connectSensorToGateway(Long sensorId, Long gatewayId) {
    SensorEntity sensor = this.sensorRepository.getSession().load(SensorEntity.class, sensorId);
    GatewayEntity gateway = this.sensorRepository.getSession().load(GatewayEntity.class,
        gatewayId);

    ConnectionRelationship relationship = new ConnectionRelationship(sensor, gateway);
    this.sensorRepository.getSession().save(relationship);

    return relationship;
  }

  public List<SensorEntity> getSensorsWithType(String type) {
    return this.sensorRepository.getSensorsWithType(type);
  }
}
