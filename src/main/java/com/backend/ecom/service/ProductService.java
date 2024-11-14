package com.backend.ecom.service;

import com.backend.ecom.entity.Product;
import com.backend.ecom.exception.ProductCreationException;
import com.backend.ecom.exception.ProductDeletionException;
import com.backend.ecom.exception.ProductNotFoundException;
import com.backend.ecom.exception.ProductUpdateException;

import java.util.List;
import java.util.Optional;

public interface ProductService {


  Product createProduct(Product product) throws ProductCreationException;

  List<Product> getAllProducts();

  Optional<Product> getProductById(Long id);

  Product updateProduct(Long id, Product productDetails) throws ProductUpdateException, ProductNotFoundException;

  void deleteProduct(Long id) throws ProductNotFoundException, ProductDeletionException;
}
