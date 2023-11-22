package com.romys.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public Client client() {
        return Client.create();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
