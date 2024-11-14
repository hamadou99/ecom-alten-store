package com.backend.ecom.repository;

import com.backend.ecom.entity.Category;
import com.backend.ecom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long > {
}
