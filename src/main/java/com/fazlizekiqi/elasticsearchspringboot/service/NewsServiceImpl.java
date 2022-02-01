package com.fazlizekiqi.elasticsearchspringboot.service;

import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.DELETE;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.GET;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.NEWS_HEADLINES_DOC;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.NEWS_HEADLINES_DOC_ID;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.NEWS_HEADLINES_SEARCH;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.NEWS_HEADLINES_UPDATE_ID;
import static com.fazlizekiqi.elasticsearchspringboot.utils.Constants.POST;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import com.fazlizekiqi.elasticsearchspringboot.utils.Constants;
import com.fazlizekiqi.elasticsearchspringboot.utils.QueryUtils;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

  private static final String HITS = "hits";
  private static final String _ID = "_id";

  private final RestClient restClient;
  private final ObjectMapper mapper;

  @Override
  @SneakyThrows
  public List<News> getNews() {
    Request request = new Request(GET, NEWS_HEADLINES_SEARCH);
    String query = QueryUtils.getQueryFromPath(ResourceUtils.CLASSPATH_URL_PREFIX + Constants.QUERY_PATH);
    NStringEntity nStringEntity = new NStringEntity(query, ContentType.APPLICATION_JSON);
    request.setEntity(nStringEntity);

    Response response = restClient.performRequest(request);
    String responseStr = EntityUtils.toString(response.getEntity());

    String responseAsString = mapper.readTree(responseStr)
      .path(HITS)
      .get(HITS)
      .toString();

    return mapper.readValue(responseAsString, new TypeReference<>() {});

  }

  @Override
  @SneakyThrows
  public News getNewsById(String id) {
    Request getRequest = new Request(GET, String.format(NEWS_HEADLINES_DOC_ID, id));

    try {
      Response getResponse = restClient.performRequest(getRequest);
      String responseStr = EntityUtils.toString(getResponse.getEntity());
      String getResponseAsString = mapper.readTree(responseStr).toString();

      return mapper.readValue(getResponseAsString, new TypeReference<>() {});
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("News with id:%s does not exists.", id));
    }

  }

  @Override
  @SneakyThrows
  public News createNews(News news) {
    Request request = new Request(POST, NEWS_HEADLINES_DOC);

    String newsAsString = mapper.writeValueAsString(news);
    NStringEntity nStringEntity = new NStringEntity(newsAsString, ContentType.APPLICATION_JSON);
    request.setEntity(nStringEntity);

    Response postResponse = restClient.performRequest(request);
    int responseStatus = postResponse.getStatusLine().getStatusCode();
    boolean newsCreated = HttpStatus.CREATED.value() == responseStatus;

    if (newsCreated) {

      String responseStr = EntityUtils.toString(postResponse.getEntity());
      String id = mapper.readTree(responseStr)
        .path(_ID)
        .textValue();

      return getNewsById(id);
    }

    return null;
  }

  @Override
  @SneakyThrows
  public void deleteNews(String id) {
    News newsById = getNewsById(id);
    Request deleteRequest = new Request(DELETE, String.format(NEWS_HEADLINES_DOC_ID, newsById.getId()));
    restClient.performRequest(deleteRequest);
  }

  @SneakyThrows
  @Override
  public News updateNews(String id, News newsToBeUpdated) {
    News currentNews = getNewsById(id);
    currentNews.setAuthors(newsToBeUpdated.getAuthors());
    currentNews.setLink(newsToBeUpdated.getLink());
    currentNews.setHeadline(newsToBeUpdated.getHeadline());
    currentNews.setCategory(newsToBeUpdated.getCategory());
    currentNews.setShortDescription(newsToBeUpdated.getShortDescription());

    Request request = new Request(POST, String.format(NEWS_HEADLINES_UPDATE_ID, id));

    String newsAsString = mapper.writeValueAsString(currentNews);
    String query = QueryUtils.getQueryFromPath(ResourceUtils.CLASSPATH_URL_PREFIX + Constants.UPDATE_PATH);
    String formattedUpdatedString = String.format(query, newsAsString);

    NStringEntity nStringEntity = new NStringEntity(formattedUpdatedString, ContentType.APPLICATION_JSON);
    request.setEntity(nStringEntity);

    Response postResponse = restClient.performRequest(request);
    int responseStatus = postResponse.getStatusLine().getStatusCode();
    boolean newsUpdated = HttpStatus.OK.value() == responseStatus;

    if (newsUpdated) {

      String responseStr = EntityUtils.toString(postResponse.getEntity());
      String createdId = mapper.readTree(responseStr)
        .path(_ID)
        .textValue();

      return getNewsById(createdId);
    }

    return null;

  }

}
