package com.fazlizekiqi.elasticsearchspringboot.configurations;


import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.ELASTIC_HOSTNAME;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.ELASTIC_PASSWORD;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.ELASTIC_PORTS;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.ELASTIC_USERNAME;

import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

  private final String username;
  private final String password;
  private final String hostname;
  private final Integer[] ports;

  @Autowired
  public ElasticSearchConfig(
    @Value("${" + ELASTIC_USERNAME + "}")  String username,
    @Value("${" + ELASTIC_PASSWORD + "}")  String password,
    @Value("${" + ELASTIC_HOSTNAME + "}")String hostname,
    @Value("${" + ELASTIC_PORTS + "}")  Integer... ports
  ) {
    this.username= username;
    this.password= password;
    this.hostname = hostname;
    this.ports = ports;
  }

  @Bean(destroyMethod = "close")
  public RestClient getRestClientBean() {

    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
      AuthScope.ANY,
      new
        UsernamePasswordCredentials(username, password)
    );

    HttpHost[] httpHosts = Arrays.stream(ports)
      .map(port -> new HttpHost(hostname, port))
      .toArray(HttpHost[]::new);

    return RestClient.builder(httpHosts)
      .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
      .build();

  }

}
