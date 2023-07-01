package com.sudarshan.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sudarshan.productservice.model.Product;
public interface ProductRepository extends JpaRepository<Product, String> {

}
