package com.stockpro.warehouse.repository;

import com.stockpro.warehouse.entity.StockLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockLevelRepository extends JpaRepository<StockLevel, Long> {

    Optional<StockLevel> findByWarehouseIdAndProductId(Long warehouseId, Long productId);

    List<StockLevel> findByWarehouseId(Long warehouseId);

    List<StockLevel> findByProductId(Long productId);

    @Query("SELECT SUM(s.quantity) FROM StockLevel s WHERE s.productId = :productId")
    Long getTotalQuantityByProductId(Long productId);

    @Query("SELECT SUM(s.quantity - s.reservedQuantity) FROM StockLevel s WHERE s.productId = :productId")
    Long getAvailableQuantityByProductId(Long productId);
}
