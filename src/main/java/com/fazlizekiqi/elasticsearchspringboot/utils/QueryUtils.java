package com.fazlizekiqi.elasticsearchspringboot.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueryUtils {

  private final static String ERROR_MESSAGE = "Error while getting the query from path";

  private static ResourceLoader resourceLoader;

  @Autowired
  public QueryUtils(ResourceLoader resourceLoader) {
    QueryUtils.resourceLoader = resourceLoader;
  }

  public static String getQueryFromPath(String path)  {
    Resource resource = resourceLoader.getResource(path);

    try (InputStream inputStream = resource.getInputStream()) {
      return new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));
    } catch (IOException e) {
      log.info(ERROR_MESSAGE, e);
    }

    return null;

  }

}
