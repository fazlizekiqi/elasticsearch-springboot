package com.fazlizekiqi.elasticsearchspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class ElasticsearchSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchSpringbootApplication.class, args);
	}

}
