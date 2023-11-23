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
import com.romys.payloads.hit.ElasticHit;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.List;
import java.util.stream.Collectors;

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

        public List<ElasticHit<ProductModel>> getAll()
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {

                return this.convert2Models(
                                this.client.resource(String.format("%s/%s/_search", host, index))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class)
                                                .getEntity(String.class));
        }

        public JsonNode create(ProductModel product)
                        throws JsonProcessingException, ClientHandlerException, UniformInterfaceException {
                return this.objectMapper.readValue(
                                this.client.resource(String.format("%s/%s/_doc", host, index))
                                                .type(MediaType.APPLICATION_JSON)
                                                .post(ClientResponse.class, objectMapper.writeValueAsString(product))
                                                .getEntity(String.class),
                                JsonNode.class);
        }

        public ElasticHit<ProductModel> getById(String id)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {
                return this.convert2Model(
                                this.client.resource(String.format("%s/%s/_doc/%s", host, index, id))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class)
                                                .getEntity(String.class));
        }

        public void deleteById(String id)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {
                String data = this.client.resource(String.format("%s/%s/_doc/%s", host, index, id))
                                .type(MediaType.APPLICATION_JSON)
                                .delete(ClientResponse.class).getEntity(String.class);
                System.out.println(data);
        }

        public List<ElasticHit<ProductModel>> getByField(String field, String value)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {
                return this.convert2Models(
                                this.client.resource(String.format("%s/%s/_search", host, index))
                                                .type(MediaType.APPLICATION_JSON)
                                                .post(ClientResponse.class,
                                                                String.format(
                                                                                "{\"query\":{\"bool\":{\"must\":[{\"match_phrase_prefix\":{\"%s\":\"%s\"}}]}}}",
                                                                                field, value))
                                                .getEntity(String.class));
        }

        private ElasticHit<ProductModel> convert2Model(String rawJson)
                        throws JsonMappingException, JsonProcessingException, IllegalArgumentException {
                JsonNode product = this.objectMapper.readValue(
                                rawJson,
                                JsonNode.class);

                if (!product.get("found").asBoolean()) {
                        System.out.println("ashdkjhadskjahdkjhasjdhakjmemek");
                }

                return new ElasticHit<>(
                                this.objectMapper.treeToValue(
                                                product.get("_id"),
                                                String.class),
                                this.objectMapper.treeToValue(
                                                product.get("_index"),
                                                String.class),
                                this.objectMapper.treeToValue(
                                                product.get("_source"),
                                                ProductModel.class));
        }

        private List<ElasticHit<ProductModel>> convert2Models(String rawJson)
                        throws JsonMappingException, JsonProcessingException, UniformInterfaceException,
                        ClientHandlerException {

                return this.objectMapper.readValue(this.objectMapper.readValue(rawJson,
                                JsonNode.class).get("hits").get("hits").toString(),
                                new TypeReference<List<JsonNode>>() {
                                }).stream().map(
                                                product -> {
                                                        try {
                                                                return new ElasticHit<>(
                                                                                this.objectMapper.treeToValue(
                                                                                                product.get("_id"),
                                                                                                String.class),
                                                                                this.objectMapper.treeToValue(
                                                                                                product.get("_index"),
                                                                                                String.class),
                                                                                this.objectMapper.treeToValue(
                                                                                                product.get("_source"),
                                                                                                ProductModel.class));
                                                        } catch (JsonProcessingException e) {
                                                                return null;
                                                        }
                                                })
                                .collect(Collectors.toList());
        }
}