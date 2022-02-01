package com.fazlizekiqi.elasticsearchspringboot.domain.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import com.fazlizekiqi.elasticsearchspringboot.domain.News.NewsBuilder;
import java.io.IOException;

public class NewsDeserializer extends JsonDeserializer {

  @Override
  public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

    JsonNode jsonNode = jp.getCodec().<JsonNode>readTree(jp);
    JsonNode newsNode = jsonNode.path("_source");

    NewsBuilder builder = News.builder();

    if(jsonNode.has("_id")){
      builder.id(jsonNode.get("_id").textValue());
    }

    if (newsNode.has("headline")) {
      builder.headline(newsNode.get("headline").textValue());
    }

    if (newsNode.has("category")) {
      builder.category(newsNode.get("category").textValue());
    }

    if (newsNode.has("link")) {
      builder.link(newsNode.get("link").textValue());
    }

    if (newsNode.has("authors")) {
      builder.authors(newsNode.get("authors").textValue());
    }

    if (newsNode.has("short_description")) {
      builder.shortDescription(newsNode.get("short_description").textValue());
    }

    return builder.build();
  }


}
