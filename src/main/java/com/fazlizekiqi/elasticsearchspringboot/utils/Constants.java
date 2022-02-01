package com.fazlizekiqi.elasticsearchspringboot.utils;

public class Constants {

  public final static String ELASTIC_QUERIES = "/elasticserach-queries";
  public final static String QUERY_PATH = ELASTIC_QUERIES + "/query.json";
  public final static String UPDATE_PATH = ELASTIC_QUERIES + "/update.json";

  public final static String APPLICATION_ROOT_NAME = "fazlizekiqi.elasticTutorial";
  public final static String ELASTIC_PROPERTIES = APPLICATION_ROOT_NAME + ".elasticProperties";

  public final static String ELASTIC_HOSTNAME = ELASTIC_PROPERTIES + ".host";
  public final static String ELASTIC_PORTS = ELASTIC_PROPERTIES + ".ports";
  public final static String ELASTIC_USERNAME = ELASTIC_PROPERTIES + ".username";
  public final static String ELASTIC_PASSWORD = ELASTIC_PROPERTIES + ".password";

  public final static String GET = "GET";
  public final static String POST = "POST";
  public final static String DELETE = "DELETE";

  public final static String NEWS_HEADLINES_INDEX = "news_headlines/";
  public final static String NEWS_HEADLINES_DOC = NEWS_HEADLINES_INDEX + "_doc/";
  public final static String NEWS_HEADLINES_DOC_ID = NEWS_HEADLINES_INDEX + "_doc/%s";
  public final static String NEWS_HEADLINES_UPDATE_ID = NEWS_HEADLINES_INDEX + "_update/%s";
  public final static String NEWS_HEADLINES_SEARCH = NEWS_HEADLINES_DOC + "_search";

}
