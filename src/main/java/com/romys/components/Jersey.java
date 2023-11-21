package com.romys.components;

import com.sun.jersey.api.client.WebResource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sun.jersey.api.client.Client;

@Configuration
public class Jersey {

    @Value("${service.elastic.host}")
    private String host;

    @Bean
    public Client client() {
        return Client.create();
    }

    @Bean
    public WebResource webResource(Client client) {
        return client.resource(host);
    }
}
