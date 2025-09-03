package com.example.inventorysystem.product.service;

import com.example.inventorysystem.product.model.Product;
import com.example.inventorysystem.product.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String createProduct(Product product, HttpSession session) {
        try {
            // Check if SKU already exists
            if (productRepository.existsBySku(product.getSku())) {
                return "Product with this SKU already exists";
            }

            // Set created by from session
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                product.setCreatedBy(userId);
            }
            
            product.setCreatedDate(LocalDateTime.now());
            product.setIsActive(true);

            productRepository.save(product);
            return "Product created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create product: " + e.getMessage();
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findByIsActive(true);
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public String updateProduct(Integer productId, Product updatedProduct, HttpSession session) {
        try {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                
                if (updatedProduct.getName() != null) {
                    product.setName(updatedProduct.getName());
                }
                if (updatedProduct.getDescription() != null) {
                    product.setDescription(updatedProduct.getDescription());
                }
                if (updatedProduct.getPrice() != null) {
                    product.setPrice(updatedProduct.getPrice());
                }
                if (updatedProduct.getCurrentStock() != null) {
                    product.setCurrentStock(updatedProduct.getCurrentStock());
                }
                if (updatedProduct.getMinimumStock() != null) {
                    product.setMinimumStock(updatedProduct.getMinimumStock());
                }
                
                // Set modified by from session
                Integer userId = (Integer) session.getAttribute("userId");
                if (userId != null) {
                    product.setModifiedBy(userId);
                }
                product.setModifiedDate(LocalDateTime.now());

                productRepository.save(product);
                return "Product updated successfully";
            } else {
                return "Product not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Update failed: " + e.getMessage();
        }
    }

    public String deleteProduct(Integer productId) {
        try {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setIsActive(false); // Soft delete
                productRepository.save(product);
                return "Product deleted successfully";
            } else {
                return "Product not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Delete failed: " + e.getMessage();
        }
    }

    public Map<String, Object> getProductAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        try {
            Long totalProducts = productRepository.countActiveProducts();
            Double totalValue = productRepository.getTotalInventoryValue();
            List<Product> lowStockProducts = getLowStockProducts();
            
            analytics.put("totalProducts", totalProducts);
            analytics.put("totalInventoryValue", totalValue);
            analytics.put("lowStockCount", lowStockProducts.size());
            analytics.put("lowStockProducts", lowStockProducts);
            
            return analytics;
        } catch (Exception e) {
            e.printStackTrace();
            analytics.put("error", "Failed to get analytics: " + e.getMessage());
            return analytics;
        }
    }
}
