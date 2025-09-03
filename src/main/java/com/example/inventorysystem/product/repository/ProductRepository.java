package com.example.inventorysystem.product.repository;

import com.example.inventorysystem.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    Optional<Product> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    List<Product> findByIsActive(Boolean isActive);
    
    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minimumStock AND p.isActive = true")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.sku LIKE %:keyword%) AND p.isActive = true")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = true")
    Long countActiveProducts();
    
    @Query("SELECT SUM(p.currentStock * p.price) FROM Product p WHERE p.isActive = true")
    Double getTotalInventoryValue();
}
