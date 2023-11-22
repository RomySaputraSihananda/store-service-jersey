package com.romys.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import com.romys.services.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public JsonNode getAll()
            throws JsonMappingException, JsonProcessingException, ClientHandlerException, UniformInterfaceException {
        return this.service.getAll();
    }
}

// throws JsonMappingException, JsonProcessingException, ClientHandlerException,
// UniformInterfaceException