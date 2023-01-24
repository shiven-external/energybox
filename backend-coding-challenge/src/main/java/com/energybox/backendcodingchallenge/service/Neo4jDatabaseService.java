package com.energybox.backendcodingchallenge.service;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Neo4jDatabaseService {
  private final Session session;

  @Autowired
  public Neo4jDatabaseService(@Value("${spring.neo4j.uri}") String databaseUrl,
                              @Value("${spring.neo4j.authentication.username}") String username,
                              @Value("${spring.neo4j.authentication.password}") String password) {
    Configuration configuration = new Configuration.Builder()
        .uri(databaseUrl)
        .credentials(username, password)
        .build();

    SessionFactory sessionFactory = new SessionFactory(configuration, "com.energybox.backendcodingchallenge");
    this.session = sessionFactory.openSession();
  }

  public Session getSession() {
    return session;
  }
}
