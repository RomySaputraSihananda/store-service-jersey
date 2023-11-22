package com.romys.services;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

@Service
public class ProductService {
    @Value("${service.elastic.host}")
    private String host;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Client client;

    public JsonNode getAll()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.objectMapper.readValue(
                this.client.resource(host).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class)
                        .getEntity(String.class),
                JsonNode.class).get("_source");
    }
}