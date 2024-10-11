package com.cetys.josea.challenge.repositories;

import com.cetys.josea.challenge.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}