package com.stockpro.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String location;
    private String address;
    private Long managerId;
    private Integer capacity;
    private String phone;
}
