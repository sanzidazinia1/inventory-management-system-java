package com.example.inventorysystem.product.controller;

import com.example.inventorysystem.product.model.Product;
import com.example.inventorysystem.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody Product product, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Please login to create products");
                return ResponseEntity.badRequest().body(response);
            }

            String result = productService.createProduct(product, session);
            if (result.equals("Product created successfully")) {
                response.put("success", true);
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getAllProducts();
            response.put("success", true);
            response.put("data", products);
            response.put("message", "Products retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get products: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                response.put("success", true);
                response.put("data", product.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Please login to update products");
                return ResponseEntity.badRequest().body(response);
            }

            String result = productService.updateProduct(id, product, session);
            if (result.equals("Product updated successfully")) {
                response.put("success", true);
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Please login to delete products");
                return ResponseEntity.badRequest().body(response);
            }

            String result = productService.deleteProduct(id);
            if (result.equals("Product deleted successfully")) {
                response.put("success", true);
                response.put("message", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.searchProducts(keyword);
            response.put("success", true);
            response.put("data", products);
            response.put("message", "Search completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getLowStockProducts();
            response.put("success", true);
            response.put("data", products);
            response.put("message", "Low stock products retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get low stock products: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getProductAnalytics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> analytics = productService.getProductAnalytics();
            response.put("success", true);
            response.put("data", analytics);
            response.put("message", "Analytics retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get analytics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
