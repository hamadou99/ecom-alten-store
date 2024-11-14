package com.backend.ecom.service;

import com.backend.ecom.entity.Category;
import com.backend.ecom.entity.Product;
import com.backend.ecom.exception.ProductCreationException;
import com.backend.ecom.exception.ProductDeletionException;
import com.backend.ecom.exception.ProductNotFoundException;
import com.backend.ecom.exception.ProductUpdateException;
import com.backend.ecom.repository.CategoryRepository;
import com.backend.ecom.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  @Transactional
  public Product createProduct(Product product) throws ProductCreationException {
    try {
      logger.info("Début de la création d'un produit avec les détails : {}", product);
      if (product.getCategory() != null) {
        logger.debug("Enregistrement de la catégorie associée au produit");
        Category category = categoryRepository.save(product.getCategory());
        product.setCategory(category);
      }
      product.setCreatedAt(LocalDateTime.now());
      Product savedProduct = productRepository.save(product);
      logger.info("Produit créé avec succès : {}", savedProduct);
      return savedProduct;
    } catch (Exception e) {
      logger.error("Erreur lors de la création du produit : {}", e.getMessage(), e);
      throw new ProductCreationException("Erreur lors de la création du produit", e);
    }
  }

  @Override
  public List<Product> getAllProducts() {
    logger.info("Récupération de tous les produits");
    List<Product> products = productRepository.findAll();
    logger.info("Nombre de produits récupérés : {}", products.size());
    return products;
  }

  @Override
  public Optional<Product> getProductById(Long id) {
    logger.info("Recherche du produit avec ID : {}", id);
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      logger.info("Produit trouvé : {}", product.get());
    } else {
      logger.warn("Produit non trouvé pour l'ID : {}", id);
    }
    return product;
  }

  @Override
  @Transactional
  public Product updateProduct(Long id, Product productDetails) throws ProductUpdateException, ProductNotFoundException {
    logger.info("Mise à jour du produit avec ID : {} avec les détails : {}", id, productDetails);
    Product product = productRepository.findById(id)
            .orElseThrow(() -> {
              logger.error("Produit non trouvé pour l'ID : {}", id);
              return new ProductNotFoundException("Produit non trouvé pour l'ID : " + id);
            });

    try {
      product.setCode(productDetails.getCode());
      product.setName(productDetails.getName());
      product.setDescription(productDetails.getDescription());
      product.setImage(productDetails.getImage());
      product.setPrice(productDetails.getPrice());
      product.setQuantity(productDetails.getQuantity());
      product.setInternalReference(productDetails.getInternalReference());
      product.setShellId(productDetails.getShellId());
      product.setInventoryStatus(productDetails.getInventoryStatus());
      product.setRating(productDetails.getRating());
      product.setUpdatedAt(LocalDateTime.now());
      Product updatedProduct = productRepository.save(product);
      logger.info("Produit mis à jour avec succès : {}", updatedProduct);
      return updatedProduct;
    } catch (Exception e) {
      logger.error("Erreur lors de la mise à jour du produit avec ID : {}", id, e);
      throw new ProductUpdateException("Erreur lors de la mise à jour du produit avec ID : " + id, e);
    }
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) throws ProductNotFoundException, ProductDeletionException {
    logger.info("Suppression du produit avec ID : {}", id);
    if (!productRepository.existsById(id)) {
      logger.warn("Produit non trouvé pour l'ID : {}", id);
      throw new ProductNotFoundException("Produit non trouvé pour l'ID : " + id);
    }
    try {
      productRepository.deleteById(id);
      logger.info("Produit supprimé avec succès pour l'ID : {}", id);
    } catch (Exception e) {
      logger.error("Erreur lors de la suppression du produit avec ID : {}", id, e);
      throw new ProductDeletionException("Erreur lors de la suppression du produit avec ID : " + id, e);
    }
  }

}
