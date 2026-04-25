package com.stockpro.warehouse.controller;

import com.stockpro.warehouse.dto.StockAdjustmentRequest;
import com.stockpro.warehouse.dto.StockLevelResponse;
import com.stockpro.warehouse.dto.WarehouseRequest;
import com.stockpro.warehouse.dto.WarehouseResponse;
import com.stockpro.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseResource {

    private final WarehouseService warehouseService;

    public WarehouseResource(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        return new ResponseEntity<>(warehouseService.createWarehouse(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, request));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateWarehouse(@PathVariable Long id) {
        warehouseService.deactivateWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stock/adjust")
    public ResponseEntity<StockLevelResponse> adjustStock(@PathVariable Long id, @Valid @RequestBody StockAdjustmentRequest request) {
        return ResponseEntity.ok(warehouseService.adjustStock(id, request));
    }

    @GetMapping("/{id}/stock/{productId}")
    public ResponseEntity<StockLevelResponse> getStockLevel(@PathVariable Long id, @PathVariable Long productId) {
        return ResponseEntity.ok(warehouseService.getStockLevel(id, productId));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<StockLevelResponse>> getStockByWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getStockByWarehouse(id));
    }

    @GetMapping("/stock/product/{productId}")
    public ResponseEntity<List<StockLevelResponse>> getStockByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(warehouseService.getStockByProduct(productId));
    }
    
    @GetMapping("/stock/product/{productId}/total")
    public ResponseEntity<Long> getTotalQuantityByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(warehouseService.getTotalQuantityByProduct(productId));
    }

    @GetMapping("/stock/product/{productId}/available")
    public ResponseEntity<Long> getAvailableQuantityByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(warehouseService.getAvailableQuantityByProduct(productId));
    }
}
