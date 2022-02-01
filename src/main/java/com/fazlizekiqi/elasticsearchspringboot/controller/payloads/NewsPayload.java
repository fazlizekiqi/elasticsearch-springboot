package com.fazlizekiqi.elasticsearchspringboot.controller.payloads;

import com.fazlizekiqi.elasticsearchspringboot.domain.News;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class NewsPayload {

  @NotBlank
  @Size(min = 1, max = 300)
  private String headline;
  @NotBlank
  @Size(min = 1, max = 50)
  private String category;
  @NotBlank
  @Size(min = 1, max = 250)
  private String authors;
  @NotBlank
  @Size(min = 1, max = 500)
  private String link;
  @NotBlank
  @Size(min = 1, max = 1200)
  private String shortDescription;

  public News toNews() {
    return News
      .builder()
      .authors(getAuthors())
      .link(getLink())
      .shortDescription(getShortDescription())
      .category(getCategory())
      .headline(getHeadline())
      .build();
  }
}
