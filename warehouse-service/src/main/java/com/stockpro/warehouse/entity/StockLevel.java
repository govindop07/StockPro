package com.stockpro.warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_levels", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"warehouse_id", "product_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(length = 50)
    private String locationRef; // bin/aisle ref

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Version
    private Integer version; // For optimistic locking

    @Transient
    public Integer getAvailableQuantity() {
        if (quantity == null || reservedQuantity == null) return 0;
        return quantity - reservedQuantity;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
        if (this.quantity == null) this.quantity = 0;
        if (this.reservedQuantity == null) this.reservedQuantity = 0;
    }
}
