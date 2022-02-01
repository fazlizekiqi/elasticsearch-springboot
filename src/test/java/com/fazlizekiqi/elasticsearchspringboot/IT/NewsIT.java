package com.fazlizekiqi.elasticsearchspringboot.IT;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import com.fazlizekiqi.elasticsearchspringboot.ElasticsearchTestContainerConfig;
import com.fazlizekiqi.elasticsearchspringboot.controller.payloads.NewsPayload;
import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import com.fazlizekiqi.elasticsearchspringboot.service.NewsService;
import java.net.URI;
import java.net.URISyntaxException;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NewsIT {

  public static final String NEWS_ENDPOINT = "/api/v1/news/";

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  NewsService newsService;

  @Autowired
  RestClient restClient;


  @Autowired
  TestRestTemplate restTemplate;

  @Container
  public static ElasticsearchContainer container = new ElasticsearchTestContainerConfig();

  @BeforeEach
  void setUp() {
    // TODO Delete index on each test
  }

  @BeforeAll
  static void beforeAll() {
    container.start();
    assertTrue(container.isRunning());
  }

  @Test()
  @DisplayName("It should get news by id.")
  void itShouldGetNewsById() throws URISyntaxException {

    News news = newsService.createNews(News.builder().authors("Albana").headline("Fazli Zekiqi").build());

    assertThat(news).isNotNull();
    assertThat(news.getId()).isNotNull();

    URI uri = new URI(NEWS_ENDPOINT + news.getId());
    ResponseEntity<NewsPayload> response = testRestTemplate.getForEntity(uri, NewsPayload.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test()
  @DisplayName("It should create news.")
  void itShouldCreateNews() throws URISyntaxException {

    NewsPayload newsPayload = new NewsPayload(
      "Elasticsearch tutorial",
      "EDUCATION",
      "Fazli Zekiqi",
      "https://fazlizekiqi.io",
      "Elasticsearch + Spring boot"
    );

    URI uri = new URI(NEWS_ENDPOINT);
    ResponseEntity<NewsPayload> response = testRestTemplate.postForEntity(uri,newsPayload, NewsPayload.class);

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

  }

  @Test()
  @DisplayName("It should create news.")
  void itShouldDeleteNews() {

    News news = newsService.createNews(News.builder().authors("Albana").headline("Fazli Zekiqi").build());

    ResponseEntity<Object> exchange = testRestTemplate.exchange(
      NEWS_ENDPOINT + "{id}",
      HttpMethod.DELETE,
      null,
      Object.class,
      news.getId()
    );

    assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test()
  @DisplayName("It should update news.")
  void itShouldUpdateNews() {
    NewsPayload newsPayload = new NewsPayload(
      "Elasticsearch tutorial",
      "EDUCATION",
      "Fazli Zekiqi",
      "https://fazlizekiqi.io",
      "Elasticsearch + Spring boot"
    );

    News news = newsService.createNews(newsPayload.toNews());

    HttpEntity<NewsPayload> newsPayloadHttpEntity = new HttpEntity<>(newsPayload);
    ResponseEntity<?> exchange = testRestTemplate.exchange(
      NEWS_ENDPOINT + "{id}",
      HttpMethod.PUT,
      newsPayloadHttpEntity,
      Object.class,
      news.getId()
    );

    URI location = exchange.getHeaders().getLocation();

    assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(location.toString()).isEqualTo(news.getId());

  }

  @AfterAll
  static void afterAll() {
    if (container.isRunning()) {
      container.stop();
    }
  }
}
