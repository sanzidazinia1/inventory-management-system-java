package com.example.inventorysystem.transaction.controller;

import com.example.inventorysystem.transaction.model.StockTransaction;
import com.example.inventorysystem.transaction.service.StockTransactionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class StockTransactionController {
    
    private final StockTransactionService transactionService;

    @Autowired
    public StockTransactionController(StockTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/stock-in")
    public ResponseEntity<Map<String, Object>> stockIn(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Please login to manage stock");
                return ResponseEntity.badRequest().body(response);
            }

            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");
            String notes = (String) request.get("notes");

            String result = transactionService.stockIn(productId, quantity, notes, session);
            if (result.equals("Stock added successfully")) {
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
            response.put("message", "Stock in failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/stock-out")
    public ResponseEntity<Map<String, Object>> stockOut(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "Please login to manage stock");
                return ResponseEntity.badRequest().body(response);
            }

            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");
            String notes = (String) request.get("notes");

            String result = transactionService.stockOut(productId, quantity, notes, session);
            if (result.equals("Stock removed successfully")) {
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
            response.put("message", "Stock out failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTransactions() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StockTransaction> transactions = transactionService.getAllTransactions();
            response.put("success", true);
            response.put("data", transactions);
            response.put("message", "Transactions retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get transactions: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getTransactionsByProduct(@PathVariable Integer productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StockTransaction> transactions = transactionService.getTransactionsByProductId(productId);
            response.put("success", true);
            response.put("data", transactions);
            response.put("message", "Product transactions retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get product transactions: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Map<String, Object>> getTransactionsByType(@PathVariable String type) {
        Map<String, Object> response = new HashMap<>();
        try {
            StockTransaction.TransactionType transactionType = StockTransaction.TransactionType.valueOf(type.toUpperCase());
            List<StockTransaction> transactions = transactionService.getTransactionsByType(transactionType);
            response.put("success", true);
            response.put("data", transactions);
            response.put("message", "Transactions by type retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get transactions by type: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getTransactionAnalytics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> analytics = transactionService.getTransactionAnalytics();
            response.put("success", true);
            response.put("data", analytics);
            response.put("message", "Transaction analytics retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get transaction analytics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
