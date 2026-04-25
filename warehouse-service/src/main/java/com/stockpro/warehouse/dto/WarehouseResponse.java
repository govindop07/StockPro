package com.stockpro.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponse {
    private Long warehouseId;
    private String name;
    private String location;
    private String address;
    private Long managerId;
    private Integer capacity;
    private Integer usedCapacity;
    private String phone;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
