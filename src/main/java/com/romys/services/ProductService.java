package com.romys.services;

import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romys.exceptions.ProductException;
import com.romys.models.ProductModel;
import com.romys.payloads.hit.ElasticHit;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.List;
import java.util.UUID;
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

        /*
         * get all data from elasticsearch
         */
        public List<ElasticHit<ProductModel>> getAll()
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {

                return this.convert2Models(
                                this.client.resource(String.format("%s/%s/_search", host, index))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class)
                                                .getEntity(String.class));
        }

        /*
         * create data and send to elasticsearch
         */
        public ElasticHit<ProductModel> create(ProductModel product)
                        throws JsonProcessingException, ClientHandlerException, UniformInterfaceException {
                String id = UUID.randomUUID().toString();

                this.client.resource(String.format("%s/%s/_doc/%s", host, index, id))
                                .type(MediaType.APPLICATION_JSON)
                                .post(ClientResponse.class, objectMapper.writeValueAsString(product));

                return this.getById(id);
        }

        /*
         * get data by id from elasticsearch
         */
        public ElasticHit<ProductModel> getById(String id)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {
                return this.convert2Model(
                                this.client.resource(String.format("%s/%s/_doc/%s", host, index, id))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .get(ClientResponse.class)
                                                .getEntity(String.class));
        }

        /*
         * get all data from elasticsearch
         */
        public ElasticHit<ProductModel> deleteById(String id)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {

                JsonNode product = this.objectMapper
                                .readValue(this.client.resource(String.format("%s/%s/_doc/%s", host, index, id))
                                                .type(MediaType.APPLICATION_JSON)
                                                .delete(ClientResponse.class).getEntity(String.class), JsonNode.class);

                if (objectMapper.readValue(product.get("result").toString(), String.class).equals("not_found")) {
                        throw new ProductException("Product Not Found");
                }

                return null;
        }

        /*
         * get data by field and value from elasticsearch
         */
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

        /*
         * update data from elasticsearch
         */
        public ElasticHit<ProductModel> update(ProductModel product, String id)
                        throws JsonMappingException, JsonProcessingException, ClientHandlerException,
                        UniformInterfaceException {
                ClientResponse res = this.client.resource(String.format("%s/%s/_update/%s", host, index, id))
                                .type(MediaType.APPLICATION_JSON)
                                .post(ClientResponse.class, objectMapper.writeValueAsString(product));
                System.out.println(String.format("{\"doc\": %s}", objectMapper.writeValueAsString(product)));
                System.out.println(res.toString());

                return this.getById(id);
        }

        /*
         * method to convert String Json to Model
         */
        private ElasticHit<ProductModel> convert2Model(String rawJson)
                        throws JsonMappingException, JsonProcessingException, IllegalArgumentException {
                JsonNode product = this.objectMapper.readValue(
                                rawJson,
                                JsonNode.class);

                if (!product.get("found").asBoolean()) {
                        throw new ProductException("Product Not Found");
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

        /*
         * method to convert String Json to List of Model
         */
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