package com.fazlizekiqi.elasticsearchspringboot;

import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchTestContainerConfig extends ElasticsearchContainer {

  private static final String ELASTICSEARCH_DOCKER_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
  private static final String ELASTICSEARCH_DOCKER_VERSION = "7.16.2";
  private static final Integer HOST_PORT_1 = 9201;
  private static final Integer HOST_PORT_2 = 9301;
  private static final Integer CONTAINER_PORT_1 = 9200;
  private static final Integer CONTAINER_PORT_2 = 9300;
  private static final String CLUSTER_NAME = "cluster.name";
  private static final String ELASTIC_SEARCH = "elasticsearch";

  public ElasticsearchTestContainerConfig() {
    super(
      DockerImageName
      .parse(ELASTICSEARCH_DOCKER_IMAGE)
      .withTag(ELASTICSEARCH_DOCKER_VERSION)
    );

    this.addFixedExposedPort(HOST_PORT_1, CONTAINER_PORT_1);
    this.addFixedExposedPort(HOST_PORT_2, CONTAINER_PORT_2);
    this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
  }
}
