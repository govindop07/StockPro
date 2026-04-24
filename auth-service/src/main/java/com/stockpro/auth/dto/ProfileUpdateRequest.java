package com.stockpro.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    private String phone;
    private String department;
}
