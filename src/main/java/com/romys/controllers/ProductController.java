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

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.util.List;

import com.romys.models.ProductModel;
import com.romys.payloads.hit.ElasticHit;
import com.romys.payloads.responses.BodyResponse;
import com.romys.services.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService service;

    /*
     * Get All Product
     */
    @GetMapping
    public ResponseEntity<BodyResponse<List<ElasticHit<ProductModel>>>> getAllProducts()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                "all data products", this.service.getAll()), HttpStatus.OK);
    }

    /*
     * Get Product by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<BodyResponse<ElasticHit<ProductModel>>> getProductById(@PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                String.format("data products with id %s", id), this.service.getById(id)), HttpStatus.OK);
    }

    /*
     * Create Product
     */
    @PostMapping
    public ResponseEntity<BodyResponse<ElasticHit<ProductModel>>> createProduct(@RequestBody ProductModel product)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED.value(),
                String.format("data success created"), this.service.create(product)), HttpStatus.CREATED);
    }

    /*
     * Update Product By id
     */
    @PutMapping("/{id}")
    public ResponseEntity<BodyResponse<ElasticHit<ProductModel>>> updateProductById(@RequestBody ProductModel product,
            @PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED.value(),
                String.format("data success created"), this.service.update(product, id)), HttpStatus.CREATED);
    }

    /*
     * Delete Product By id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BodyResponse<ElasticHit<ProductModel>>> deleteProductById(@PathVariable String id)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {

        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED.value(),
                String.format("data success created"), this.service.deleteById(id)), HttpStatus.CREATED);
    }

    /*
     * Search Product by field
     */
    @GetMapping("/search")
    public ResponseEntity<BodyResponse<List<ElasticHit<ProductModel>>>> searchByField(@RequestParam String field,
            @RequestParam String value)
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return new ResponseEntity<>(new BodyResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                "all data products", this.service.getByField(field, value)), HttpStatus.OK);
    }
}