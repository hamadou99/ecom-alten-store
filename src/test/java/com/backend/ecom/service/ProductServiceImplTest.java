package com.backend.ecom.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.backend.ecom.entity.Category;
import com.backend.ecom.entity.Product;
import com.backend.ecom.exception.ProductCreationException;
import com.backend.ecom.exception.ProductNotFoundException;
import com.backend.ecom.exception.ProductUpdateException;
import com.backend.ecom.repository.CategoryRepository;
import com.backend.ecom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateProduct_withCategory() throws ProductCreationException {
    Product product = new Product();
    Category category = new Category();
    product.setCategory(category);

    when(categoryRepository.save(category)).thenReturn(category);
    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.createProduct(product);

    assertNotNull(result);
    verify(categoryRepository, times(1)).save(category);
    verify(productRepository, times(1)).save(product);
  }

  @Test
  public void testCreateProduct_withoutCategory() throws ProductCreationException {
    Product product = new Product();
    product.setCategory(null);

    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.createProduct(product);

    assertNotNull(result);
    verify(categoryRepository, times(0)).save(any(Category.class));
    verify(productRepository, times(1)).save(product);
  }

  @Test
  public void testGetAllProducts() {
    List<Product> products = Arrays.asList(new Product(), new Product());
    when(productRepository.findAll()).thenReturn(products);

    List<Product> result = productService.getAllProducts();

    assertEquals(2, result.size());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  public void testGetProductById_found() {
    Product product = new Product();
    product.setId(1L);
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    Optional<Product> result = productService.getProductById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    verify(productRepository, times(1)).findById(1L);
  }

  @Test
  public void testGetProductById_notFound() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Product> result = productService.getProductById(1L);

    assertFalse(result.isPresent());
    verify(productRepository, times(1)).findById(1L);
  }

  @Test
  public void testUpdateProduct_success() throws ProductUpdateException, ProductNotFoundException {
    Product existingProduct = new Product();
    existingProduct.setId(1L);
    Product updatedProductDetails = new Product();
    updatedProductDetails.setName("Updated Product");

    when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
    when(productRepository.save(existingProduct)).thenReturn(existingProduct);

    Product result = productService.updateProduct(1L, updatedProductDetails);

    assertNotNull(result);
    assertEquals("Updated Product", result.getName());
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(existingProduct);
  }


  @Test
  public void testDeleteProduct_success() {
    when(productRepository.existsById(1L)).thenReturn(true);
    doNothing().when(productRepository).deleteById(1L);

    assertDoesNotThrow(() -> productService.deleteProduct(1L));

    verify(productRepository, times(1)).existsById(1L);
    verify(productRepository, times(1)).deleteById(1L);
  }


}
