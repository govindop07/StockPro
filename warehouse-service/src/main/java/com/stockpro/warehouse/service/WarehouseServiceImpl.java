package com.stockpro.warehouse.service;

import com.stockpro.warehouse.dto.StockAdjustmentRequest;
import com.stockpro.warehouse.dto.StockLevelResponse;
import com.stockpro.warehouse.dto.WarehouseRequest;
import com.stockpro.warehouse.dto.WarehouseResponse;
import com.stockpro.warehouse.entity.StockLevel;
import com.stockpro.warehouse.entity.Warehouse;
import com.stockpro.warehouse.repository.StockLevelRepository;
import com.stockpro.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final StockLevelRepository stockLevelRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, StockLevelRepository stockLevelRepository) {
        this.warehouseRepository = warehouseRepository;
        this.stockLevelRepository = stockLevelRepository;
    }

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .location(request.getLocation())
                .address(request.getAddress())
                .managerId(request.getManagerId())
                .capacity(request.getCapacity())
                .phone(request.getPhone())
                .isActive(true)
                .build();

        return mapToResponse(warehouseRepository.save(warehouse));
    }

    @Override
    public WarehouseResponse getWarehouseById(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findByWarehouseId(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));
        return mapToResponse(warehouse);
    }

    @Override
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long warehouseId, WarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findByWarehouseId(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));

        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setAddress(request.getAddress());
        warehouse.setManagerId(request.getManagerId());
        warehouse.setCapacity(request.getCapacity());
        warehouse.setPhone(request.getPhone());

        return mapToResponse(warehouseRepository.save(warehouse));
    }

    @Override
    @Transactional
    public void deactivateWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findByWarehouseId(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));
        warehouse.setIsActive(false);
        warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public StockLevelResponse adjustStock(Long warehouseId, StockAdjustmentRequest request) {
        Warehouse warehouse = warehouseRepository.findByWarehouseId(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (!warehouse.getIsActive()) {
            throw new IllegalArgumentException("Warehouse is not active");
        }

        StockLevel stockLevel = stockLevelRepository.findByWarehouseIdAndProductId(warehouseId, request.getProductId())
                .orElse(StockLevel.builder()
                        .warehouseId(warehouseId)
                        .productId(request.getProductId())
                        .quantity(0)
                        .reservedQuantity(0)
                        .locationRef(request.getLocationRef())
                        .build());

        switch (request.getAdjustmentType().toUpperCase()) {
            case "ADD":
                stockLevel.setQuantity(stockLevel.getQuantity() + request.getQuantity());
                break;
            case "REMOVE":
                if (stockLevel.getAvailableQuantity() < request.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient available stock");
                }
                stockLevel.setQuantity(stockLevel.getQuantity() - request.getQuantity());
                break;
            case "RESERVE":
                if (stockLevel.getAvailableQuantity() < request.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient available stock to reserve");
                }
                stockLevel.setReservedQuantity(stockLevel.getReservedQuantity() + request.getQuantity());
                break;
            case "RELEASE":
                if (stockLevel.getReservedQuantity() < request.getQuantity()) {
                    throw new IllegalArgumentException("Cannot release more than reserved");
                }
                stockLevel.setReservedQuantity(stockLevel.getReservedQuantity() - request.getQuantity());
                break;
            default:
                throw new IllegalArgumentException("Unknown adjustment type");
        }

        if (request.getLocationRef() != null) {
            stockLevel.setLocationRef(request.getLocationRef());
        }

        return mapToResponse(stockLevelRepository.save(stockLevel));
    }

    @Override
    public StockLevelResponse getStockLevel(Long warehouseId, Long productId) {
        StockLevel stockLevel = stockLevelRepository.findByWarehouseIdAndProductId(warehouseId, productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return mapToResponse(stockLevel);
    }

    @Override
    public List<StockLevelResponse> getStockByWarehouse(Long warehouseId) {
        return stockLevelRepository.findByWarehouseId(warehouseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockLevelResponse> getStockByProduct(Long productId) {
        return stockLevelRepository.findByProductId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalQuantityByProduct(Long productId) {
        Long total = stockLevelRepository.getTotalQuantityByProductId(productId);
        return total != null ? total : 0L;
    }

    @Override
    public Long getAvailableQuantityByProduct(Long productId) {
        Long available = stockLevelRepository.getAvailableQuantityByProductId(productId);
        return available != null ? available : 0L;
    }

    private WarehouseResponse mapToResponse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .warehouseId(warehouse.getWarehouseId())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .address(warehouse.getAddress())
                .managerId(warehouse.getManagerId())
                .capacity(warehouse.getCapacity())
                .usedCapacity(warehouse.getUsedCapacity())
                .phone(warehouse.getPhone())
                .isActive(warehouse.getIsActive())
                .createdAt(warehouse.getCreatedAt())
                .build();
    }

    private StockLevelResponse mapToResponse(StockLevel stockLevel) {
        return StockLevelResponse.builder()
                .stockId(stockLevel.getStockId())
                .warehouseId(stockLevel.getWarehouseId())
                .productId(stockLevel.getProductId())
                .quantity(stockLevel.getQuantity())
                .reservedQuantity(stockLevel.getReservedQuantity())
                .availableQuantity(stockLevel.getAvailableQuantity())
                .locationRef(stockLevel.getLocationRef())
                .lastUpdated(stockLevel.getLastUpdated())
                .build();
    }
}
