package com.backend.ecom.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.backend.ecom.entity.Product;
import com.backend.ecom.exception.ProductCreationException;
import com.backend.ecom.exception.ProductDeletionException;
import com.backend.ecom.exception.ProductNotFoundException;
import com.backend.ecom.exception.ProductUpdateException;
import com.backend.ecom.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Test
  public void testCreateProduct() throws Exception, ProductCreationException {
    Product product = new Product();
    product.setId(1L);
    product.setName("Test Product");

    Mockito.when(productService.createProduct(Mockito.any(Product.class))).thenReturn(product);

    mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Test Product\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Test Product")));
  }

  @Test
  public void testGetAllProducts() throws Exception {
    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");

    Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

    mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Product 1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Product 2")));
  }

  @Test
  public void testGetProductById_found() throws Exception {
    Product product = new Product();
    product.setId(1L);
    product.setName("Test Product");

    Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));

    mockMvc.perform(get("/api/v1/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Test Product")));
  }

  @Test
  public void testGetProductById_notFound() throws Exception {
    Mockito.when(productService.getProductById(1L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/v1/products/1"))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateProduct_success() throws Exception, ProductUpdateException, ProductNotFoundException {
    Product existingProduct = new Product();
    existingProduct.setId(1L);
    existingProduct.setName("Old Product");

    Product updatedProduct = new Product();
    updatedProduct.setId(1L);
    updatedProduct.setName("Updated Product");

    Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(existingProduct));
    Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any(Product.class))).thenReturn(updatedProduct);

    mockMvc.perform(patch("/api/v1/products/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Updated Product\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Updated Product")));
  }

  @Test
  public void testDeleteProduct_success() throws Exception, ProductDeletionException, ProductNotFoundException {
    Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(new Product()));
    Mockito.doNothing().when(productService).deleteProduct(1L);

    mockMvc.perform(delete("/api/v1/products/1"))
            .andExpect(status().isNoContent());
  }


}
