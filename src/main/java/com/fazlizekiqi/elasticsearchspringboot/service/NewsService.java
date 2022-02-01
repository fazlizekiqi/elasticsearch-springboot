package com.fazlizekiqi.elasticsearchspringboot.service;

import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import java.util.List;

public interface NewsService {

  List<News> getNews();

  News getNewsById(String id);

  News createNews(News news);

  void deleteNews(String id);

  News updateNews(String id, News toNews);

}
