package com.tdd.claimantsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Value("${dvla-service.uri:/dvla-verify}")
    String dvlaServiceUri;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.setConnectTimeout(30000)
                .setReadTimeout(30000)
                .errorHandler(new DefaultResponseErrorHandler(){
                    @Override
                    protected boolean hasError(HttpStatus statusCode) {
                        return false;
                    }
                })
                .rootUri(dvlaServiceUri)
                .build();
    }
}
