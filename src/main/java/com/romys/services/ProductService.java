package com.romys.services;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romys.models.ProductModel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.List;

@Service
public class ProductService {
    @Value("${service.elastic.host}")
    private String host;

    @Value("${service.elastic.index.product}")
    private String index;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Client client;

    public List<?> getAll()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {

        return this.convert2Models(
                this.client.resource(String.format("%s/%s/_search", host, index)).accept(MediaType.APPLICATION_JSON)
                        .get(ClientResponse.class)
                        .getEntity(String.class));
    }

    public ProductModel getById(String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.convert2Model(
                this.client.resource(String.format("%s/%s/_doc/%s", host, index, id)).accept(MediaType.APPLICATION_JSON)
                        .get(ClientResponse.class)
                        .getEntity(String.class));
    }

    public void deleteById(String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        this.client.resource(String.format("%s/%s/_doc/%s", host, index, id)).accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class)
                .getEntity(String.class);
    }

    private ProductModel convert2Model(String rawJson)
            throws JsonMappingException, JsonProcessingException, IllegalArgumentException {
        return this.objectMapper.treeToValue(this.objectMapper.readValue(
                rawJson,
                JsonNode.class).get("_source"), ProductModel.class);
    }

    private List<?> convert2Models(String rawJson)
            throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException {
        return this.objectMapper.readValue(this.objectMapper.readValue(rawJson,
                JsonNode.class).get("hits").get("hits").toString(),
                new TypeReference<List<?>>() {
                });
    }
}