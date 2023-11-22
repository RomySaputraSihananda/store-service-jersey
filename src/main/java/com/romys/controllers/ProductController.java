package com.romys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.List;

import com.romys.models.ProductModel;
import com.romys.payloads.responses.BodyResponses;
import com.romys.services.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<BodyResponses<ProductModel>> getAllProducts()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponses<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                "all data products", this.service.getAll()), HttpStatus.OK);
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
    public List<ProductModel> searchByField(@RequestParam String field, @RequestParam String value)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.getByField(field, value);
    }
}