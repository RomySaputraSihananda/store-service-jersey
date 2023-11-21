package com.romys.models;

import java.util.List;

import lombok.Data;

@Data
public class ProductModel {
    public String title;
    public String description;
    public int price;
    public double discountPercentage;
    public double rating;
    public int stock;
    public String brand;
    public String category;
    public String thumbnail;
    public List<String> images;
}