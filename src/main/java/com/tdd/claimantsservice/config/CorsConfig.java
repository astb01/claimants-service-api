package com.tdd.claimantsservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class CorsConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                // Allowed server access list
                .allowedOrigins("http://localhost:8085", "http://localhost:8087")
                // Allowed HTTP methods
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                // List of allowed headers returned by spring
                .allowedHeaders("X-Auth-Token", "Content-Type")
                // whether or not the response to the request can be exposed
                .allowCredentials(false)
                // The time that the preflight response is cached
                .maxAge(4800);
    }
}
