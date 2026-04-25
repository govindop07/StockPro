package com.stockpro.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String locationRef;
    
    // Type of adjustment: "ADD", "REMOVE", "RESERVE", "RELEASE"
    @NotNull(message = "Adjustment type is required")
    private String adjustmentType;
}
