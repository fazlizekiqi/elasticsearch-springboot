package com.fazlizekiqi.elasticsearchspringboot.controller;


import com.fazlizekiqi.elasticsearchspringboot.controller.payloads.NewsPayload;
import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import com.fazlizekiqi.elasticsearchspringboot.service.NewsService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@Slf4j
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  @GetMapping()
  public ResponseEntity<List<News>> getNews(){
    //TODO Impl
    List<News> news = newsService.getNews();
    return ResponseEntity.status(HttpStatus.OK).body(news);
  }

  @GetMapping("/{id}")
  public ResponseEntity<News> getNewsById(@PathVariable String id)  {
    News news = newsService.getNewsById(id);
    return ResponseEntity.status(HttpStatus.OK).body(news);
  }

  @PostMapping
  public ResponseEntity<News> addNewsController(@RequestBody @Valid NewsPayload newsPayload) {
    News news = newsPayload.toNews();
    final News createdNews = newsService.createNews(news);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
  }

  @SneakyThrows
  @PutMapping("/{id}")
  public ResponseEntity<?> updateNews(@PathVariable String id, @RequestBody @Valid NewsPayload news) {
    News updatedNews = newsService.updateNews(id, news.toNews());
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .location(new URI(updatedNews.getId()))
      .build();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable String id) {
    newsService.deleteNews(id);
  }

}
