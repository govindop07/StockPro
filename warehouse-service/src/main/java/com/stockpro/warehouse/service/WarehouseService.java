package com.stockpro.warehouse.service;

import com.stockpro.warehouse.dto.StockAdjustmentRequest;
import com.stockpro.warehouse.dto.StockLevelResponse;
import com.stockpro.warehouse.dto.WarehouseRequest;
import com.stockpro.warehouse.dto.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseRequest request);
    WarehouseResponse getWarehouseById(Long warehouseId);
    List<WarehouseResponse> getAllWarehouses();
    WarehouseResponse updateWarehouse(Long warehouseId, WarehouseRequest request);
    void deactivateWarehouse(Long warehouseId);

    StockLevelResponse adjustStock(Long warehouseId, StockAdjustmentRequest request);
    StockLevelResponse getStockLevel(Long warehouseId, Long productId);
    List<StockLevelResponse> getStockByWarehouse(Long warehouseId);
    List<StockLevelResponse> getStockByProduct(Long productId);
    
    Long getTotalQuantityByProduct(Long productId);
    Long getAvailableQuantityByProduct(Long productId);
}
