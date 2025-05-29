package com.shamiinnocent.erp.Dtos.requests;

import jakarta.validation.constraints.*;

/**
 * A DTO for creating or updating a deduction in the ERP system.
 * Includes deduction details with validation.
 *
 * @param code        the deduction's unique code
 * @param name        the name of the deduction (e.g., "Employee Tax", "Pension")
 * @param percentage  the percentage rate of the deduction (e.g., 30.0 for 30%)
 */
public record DeductionRequest(
        @NotBlank(message = "Deduction code is required")
        @Size(max = 20, message = "Deduction code must be 20 characters or less")
        @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Deduction code must contain only letters, numbers, and hyphens")
        String code,

        @NotBlank(message = "Deduction name is required")
        @Size(max = 50, message = "Deduction name must be 50 characters or less")
        String name,

        @NotNull(message = "Percentage is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Percentage must be greater than or equal to 0")
        @DecimalMax(value = "100.0", inclusive = true, message = "Percentage must be less than or equal to 100")
        Double percentage

) {
}