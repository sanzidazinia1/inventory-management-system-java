package com.example.inventorysystem.transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockTransaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "product_id", nullable = false)
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Column(name = "previous_stock")
    private Integer previousStock;

    @Column(name = "new_stock")
    private Integer newStock;

    @Size(max = 500, message = "Notes must be less than 500 characters")
    private String notes;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
    }

    public enum TransactionType {
        IN, OUT
    }

    public StockTransaction(Integer productId, TransactionType transactionType, Integer quantity, String notes) {
        this.productId = productId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.notes = notes;
        this.transactionDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "transactionId=" + transactionId +
                ", productId=" + productId +
                ", transactionType=" + transactionType +
                ", quantity=" + quantity +
                ", previousStock=" + previousStock +
                ", newStock=" + newStock +
                ", notes='" + notes + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
