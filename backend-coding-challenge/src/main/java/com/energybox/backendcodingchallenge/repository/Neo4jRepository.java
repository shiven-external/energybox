package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.service.Neo4jDatabaseService;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Neo4jRepository {
  private Session neo4jSession;

  @Autowired
  public Neo4jRepository(Neo4jDatabaseService neo4jDatabaseService) {
    this.neo4jSession = neo4jDatabaseService.getSession();
  }

  public Session getSession(){
    return this.neo4jSession;
  }
}