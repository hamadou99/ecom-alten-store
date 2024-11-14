package com.backend.ecom.entity;

import com.backend.ecom.entity.ennumeration.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String code;
  private String name;
  private String description;
  private String image;
  private Double price;
  private Double quantity;
  private String internalReference;
  private Long shellId;
  @Enumerated(EnumType.STRING)
  private InventoryStatus inventoryStatus;
  private Double rating;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}
