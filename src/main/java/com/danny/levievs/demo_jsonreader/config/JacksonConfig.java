package com.danny.levievs.demo_jsonreader.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper jacksonMapper() {
        return new ObjectMapper();
    }
}
