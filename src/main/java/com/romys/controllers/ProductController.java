package com.romys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.romys.models.ProductModel;
import com.romys.services.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService service;

    @Value("${service.elastic.host}")
    private String host;

    @Value("${service.elastic.index.product}")
    private String index;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Client client;

    @GetMapping
    public List<?> getAllProducts()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.getAll();
    }

    @GetMapping("/{id}")
    public ProductModel getProductById(@PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.getById(id);
    }

    @PostMapping
    public JsonNode createProduct(@RequestBody ProductModel product)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.create(product);
    }

    @PutMapping("/{id}")
    public String updateProductById(@RequestBody ProductModel product, @PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        this.service.deleteById(id);
        return "deleted";
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        this.service.deleteById(id);
        return "deleted";
    }

    @GetMapping("/search")
    public ProductModel searchByField(@RequestParam String field, @RequestParam String value)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.getByField(field, value);
    }

    @GetMapping("/test")
    public List<?> getMethodName()
            throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException {
        return this.objectMapper.readValue(this.objectMapper.readValue(
                this.client.resource(String.format("%s/%s/_search", host, index)).accept(MediaType.APPLICATION_JSON)
                        .get(String.class),
                JsonNode.class).get("hits").get("hits").toString(),
                new TypeReference<List<?>>() {
                });
    }
}