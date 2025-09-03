package com.example.inventorysystem.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must be less than 50 characters")
    private String sku;

    @Column(name = "current_stock", nullable = false)
    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock must be non-negative")
    private Integer currentStock = 0;

    @Column(name = "minimum_stock", nullable = false)
    @NotNull(message = "Minimum stock is required")
    @Min(value = 0, message = "Minimum stock must be non-negative")
    private Integer minimumStock = 0;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public Product(String name, String description, Double price, String sku, Integer currentStock, Integer minimumStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return currentStock <= minimumStock;
    }

    public void addStock(Integer quantity) {
        if (quantity > 0) {
            this.currentStock += quantity;
        }
    }

    public boolean removeStock(Integer quantity) {
        if (quantity > 0 && this.currentStock >= quantity) {
            this.currentStock -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", currentStock=" + currentStock +
                ", minimumStock=" + minimumStock +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", isActive=" + isActive +
                '}';
    }
}
