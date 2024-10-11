package com.cetys.josea.challenge.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    private final RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Obtener todos los productos
    public String getAllProducts() {
        String url = "https://dummyjson.com/products";
        return restTemplate.getForObject(url, String.class);  // Puedes mapear a una clase en lugar de String
    }

    // Obtener un solo producto por ID
    public String getProductById(int id) {
        String url = "https://dummyjson.com/products/" + id;
        return restTemplate.getForObject(url, String.class);  // Puedes mapear a una clase en lugar de String
    }
}