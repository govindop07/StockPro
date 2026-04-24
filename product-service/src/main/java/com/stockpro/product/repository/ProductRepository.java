package com.stockpro.product.repository;

import com.stockpro.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    Optional<Product> findByProductId(Long productId);

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByIsActive(Boolean isActive);

    Optional<Product> findByBarcode(String barcode);

    Long countByCategory(String category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> searchByName(String name);
}
