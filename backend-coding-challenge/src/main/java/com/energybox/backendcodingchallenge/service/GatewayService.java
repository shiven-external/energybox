package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.entity.GatewayEntity;
import com.energybox.backendcodingchallenge.entity.SensorEntity;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import org.neo4j.ogm.cypher.query.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GatewayService {

  private final GatewayRepository gatewayRepository;
  private final int maxItemsPerPage = 100;

  @Autowired
  public GatewayService(GatewayRepository gatewayRepository) {
    this.gatewayRepository = gatewayRepository;
  }
  public Collection<GatewayEntity> getGateways(int pageNumber, int inputtedItemsPerPage) {
    int itemsPerPage = Math.min(inputtedItemsPerPage, this.maxItemsPerPage);

    return this.gatewayRepository.getSession().loadAll(GatewayEntity.class,
        new Pagination(pageNumber, itemsPerPage));
  }

  public GatewayEntity createGateway() {
    GatewayEntity gateway = new GatewayEntity();
    this.gatewayRepository.getSession().save(gateway);

    return gateway;
  }

  public Collection<SensorEntity> getSensorsConnectedToGateway(Long gatewayId) {
    return this.gatewayRepository.getSensorsConnectedToGateway(gatewayId);
  }

  public Collection<GatewayEntity> getGatewaysConnectedToSensorType(String type) {
    return this.gatewayRepository.getGatewaysConnectedToSensorType(type);
  }
}
