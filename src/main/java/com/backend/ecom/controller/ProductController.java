package com.backend.ecom.controller;

import com.backend.ecom.entity.Product;
import com.backend.ecom.exception.ProductCreationException;
import com.backend.ecom.exception.ProductDeletionException;
import com.backend.ecom.exception.ProductNotFoundException;
import com.backend.ecom.exception.ProductUpdateException;
import com.backend.ecom.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "products",description = "API de gestion des produits")
public class ProductController {

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "Créer un nouveau produit",
          description = "Permet de créer un produit dans le système.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
          @ApiResponse(responseCode = "400", description = "Requête invalide")
  })
  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) throws ProductCreationException {
    logger.info("Création d'un nouveau produit avec les détails : {}", product);
    Product newProduct = productService.createProduct(product);
    logger.info("Produit créé avec succès : {}", newProduct);
    return ResponseEntity.status(201).body(newProduct);
  }

  @Operation(summary = "Récupérer tous les produits",
          description = "Retourne la liste de tous les produits disponibles.")
  @ApiResponse(responseCode = "200", description = "Liste des produits récupérée avec succès")
  @GetMapping
  public List<Product> getAllProducts() {
    logger.info("Récupération de tous les produits");
    List<Product> products = productService.getAllProducts();
    logger.info("Nombre de produits récupérés : {}", products.size());
    return products;
  }

  @Operation(summary = "Récupérer un produit par ID",
          description = "Retourne un produit spécifique en fonction de son ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Produit trouvé"),
          @ApiResponse(responseCode = "404", description = "Produit non trouvé pour l'ID fourni")
  })
  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(
          @Parameter(description = "ID du produit à récupérer", required = true)
          @PathVariable Long id) {
    logger.info("Recherche du produit avec ID : {}", id);
    return productService.getProductById(id)
            .map(product -> {
              logger.info("Produit trouvé : {}", product);
              return ResponseEntity.ok(product);
            })
            .orElseGet(() -> {
              logger.warn("Produit non trouvé pour l'ID : {}", id);
              return ResponseEntity.notFound().build();
            });
  }

  @Operation(summary = "Mettre à jour un produit",
          description = "Modifie un produit existant avec les nouvelles informations fournies.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
          @ApiResponse(responseCode = "404", description = "Produit non trouvé pour l'ID fourni"),
          @ApiResponse(responseCode = "400", description = "Requête invalide")
  })
  @PatchMapping("/{id}")
  public ResponseEntity<Product> updateProduct(
          @Parameter(description = "ID du produit à mettre à jour", required = true)
          @PathVariable Long id,
          @RequestBody Product productDetails) throws ProductUpdateException, ProductNotFoundException {
    logger.info("Mise à jour du produit avec ID : {} avec les nouveaux détails : {}", id, productDetails);
    Product updatedProduct = productService.updateProduct(id, productDetails);
    logger.info("Produit mis à jour avec succès : {}", updatedProduct);
    return ResponseEntity.ok(updatedProduct);
  }

  @Operation(summary = "Supprimer un produit",
          description = "Supprime un produit spécifique en fonction de son ID.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Produit supprimé avec succès"),
          @ApiResponse(responseCode = "404", description = "Produit non trouvé pour l'ID fourni")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(
          @Parameter(description = "ID du produit à supprimer", required = true)
          @PathVariable Long id) throws ProductDeletionException, ProductNotFoundException {
    logger.info("Suppression du produit avec ID : {}", id);
    productService.deleteProduct(id);
    logger.info("Produit supprimé avec succès pour l'ID : {}", id);
    return ResponseEntity.noContent().build();
  }
}
