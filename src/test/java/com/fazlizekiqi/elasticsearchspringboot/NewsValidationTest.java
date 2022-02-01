package com.fazlizekiqi.elasticsearchspringboot;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazlizekiqi.elasticsearchspringboot.controller.NewsController;
import com.fazlizekiqi.elasticsearchspringboot.controller.payloads.NewsPayload;
import com.fazlizekiqi.elasticsearchspringboot.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NewsController.class)
class NewsValidationTest {

  public static final String NEWS_ENDPOINT = "/api/v1/news/";

  @MockBean
  NewsService newsService;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("It will throw an error when trying to create an invalid news.")
  void itWillThrowError() throws Exception {
    String newsJson = new ObjectMapper().writeValueAsString(new NewsPayload("Fazli",null,null,null,null));
    mockMvc.perform(post(NEWS_ENDPOINT)
        .contentType(APPLICATION_JSON)
        .content(newsJson))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("It will accept valid news when trying to create news.")
  void itWillAcceptThePayload() throws Exception {
    NewsPayload payload = new NewsPayload(
      "Fazli",
      "ENTERTAINMENT",
      "Fazli Zekiqi",
      "https://fazlizekiqi.io",
      "Elasticsearch + Spring boot"
    );
    String newsJson = new ObjectMapper().writeValueAsString(payload);
    given(newsService.createNews(payload.toNews())).willReturn(payload.toNews());

    mockMvc.perform(post(NEWS_ENDPOINT)
        .contentType(APPLICATION_JSON)
        .content(newsJson))
      .andDo(print())
      .andExpect(status().isCreated());
  }


}
