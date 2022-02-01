package com.fazlizekiqi.elasticsearchspringboot.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fazlizekiqi.elasticsearchspringboot.domain.deserializers.NewsDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = NewsDeserializer.class)
public class News {

  @JsonInclude(Include.NON_NULL)
  private String id;
  private String headline;
  private String category;
  private String authors;
  private String link;
  @JsonProperty("short_description")
  private String shortDescription;

}
