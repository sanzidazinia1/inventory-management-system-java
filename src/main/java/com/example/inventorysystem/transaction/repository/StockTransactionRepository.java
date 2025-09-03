package com.example.inventorysystem.transaction.repository;

import com.example.inventorysystem.transaction.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Integer> {
    
    List<StockTransaction> findByProductId(Integer productId);
    
    List<StockTransaction> findByTransactionType(StockTransaction.TransactionType transactionType);
    
    List<StockTransaction> findByCreatedBy(Integer createdBy);
    
    @Query("SELECT st FROM StockTransaction st WHERE st.transactionDate BETWEEN :startDate AND :endDate")
    List<StockTransaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT st FROM StockTransaction st WHERE st.productId = :productId AND st.transactionDate BETWEEN :startDate AND :endDate")
    List<StockTransaction> findByProductIdAndDateRange(@Param("productId") Integer productId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(st) FROM StockTransaction st WHERE st.transactionType = :transactionType")
    Long countByTransactionType(@Param("transactionType") StockTransaction.TransactionType transactionType);
    
    @Query("SELECT SUM(st.quantity) FROM StockTransaction st WHERE st.transactionType = :transactionType")
    Long getTotalQuantityByType(@Param("transactionType") StockTransaction.TransactionType transactionType);
}
