package com.example.inventorysystem.transaction.service;

import com.example.inventorysystem.product.model.Product;
import com.example.inventorysystem.product.repository.ProductRepository;
import com.example.inventorysystem.transaction.model.StockTransaction;
import com.example.inventorysystem.transaction.repository.StockTransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockTransactionService {
    
    private final StockTransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public StockTransactionService(StockTransactionRepository transactionRepository, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public String stockIn(Integer productId, Integer quantity, String notes, HttpSession session) {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                return "Product not found";
            }

            Product product = productOpt.get();
            Integer previousStock = product.getCurrentStock();
            
            // Create transaction record
            StockTransaction transaction = new StockTransaction();
            transaction.setProductId(productId);
            transaction.setTransactionType(StockTransaction.TransactionType.IN);
            transaction.setQuantity(quantity);
            transaction.setPreviousStock(previousStock);
            transaction.setNewStock(previousStock + quantity);
            transaction.setNotes(notes);
            transaction.setTransactionDate(LocalDateTime.now());
            
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                transaction.setCreatedBy(userId);
            }

            // Update product stock
            product.addStock(quantity);
            
            // Save both
            transactionRepository.save(transaction);
            productRepository.save(product);

            return "Stock added successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Stock in failed: " + e.getMessage();
        }
    }

    @Transactional
    public String stockOut(Integer productId, Integer quantity, String notes, HttpSession session) {
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                return "Product not found";
            }

            Product product = productOpt.get();
            Integer previousStock = product.getCurrentStock();
            
            if (previousStock < quantity) {
                return "Insufficient stock available";
            }
            
            // Create transaction record
            StockTransaction transaction = new StockTransaction();
            transaction.setProductId(productId);
            transaction.setTransactionType(StockTransaction.TransactionType.OUT);
            transaction.setQuantity(quantity);
            transaction.setPreviousStock(previousStock);
            transaction.setNewStock(previousStock - quantity);
            transaction.setNotes(notes);
            transaction.setTransactionDate(LocalDateTime.now());
            
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                transaction.setCreatedBy(userId);
            }

            // Update product stock
            product.removeStock(quantity);
            
            // Save both
            transactionRepository.save(transaction);
            productRepository.save(product);

            return "Stock removed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Stock out failed: " + e.getMessage();
        }
    }

    public List<StockTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<StockTransaction> getTransactionsByProductId(Integer productId) {
        return transactionRepository.findByProductId(productId);
    }

    public List<StockTransaction> getTransactionsByType(StockTransaction.TransactionType type) {
        return transactionRepository.findByTransactionType(type);
    }

    public List<StockTransaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateRange(startDate, endDate);
    }

    public Map<String, Object> getTransactionAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        try {
            Long stockInCount = transactionRepository.countByTransactionType(StockTransaction.TransactionType.IN);
            Long stockOutCount = transactionRepository.countByTransactionType(StockTransaction.TransactionType.OUT);
            Long totalStockIn = transactionRepository.getTotalQuantityByType(StockTransaction.TransactionType.IN);
            Long totalStockOut = transactionRepository.getTotalQuantityByType(StockTransaction.TransactionType.OUT);
            
            analytics.put("stockInTransactions", stockInCount);
            analytics.put("stockOutTransactions", stockOutCount);
            analytics.put("totalQuantityIn", totalStockIn);
            analytics.put("totalQuantityOut", totalStockOut);
            analytics.put("netStockMovement", (totalStockIn != null ? totalStockIn : 0) - (totalStockOut != null ? totalStockOut : 0));
            
            return analytics;
        } catch (Exception e) {
            e.printStackTrace();
            analytics.put("error", "Failed to get analytics: " + e.getMessage());
            return analytics;
        }
    }
}
