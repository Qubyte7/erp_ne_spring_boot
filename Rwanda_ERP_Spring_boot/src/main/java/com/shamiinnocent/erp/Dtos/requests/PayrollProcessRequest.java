package com.shamiinnocent.erp.Dtos.requests;

import jakarta.validation.constraints.*;

/**
 * A DTO for processing payrolls in the ERP system.
 * Includes details about the month and year for which to process payrolls.
 *
 * @param month the month for which to process payrolls (1-12)
 * @param year  the year for which to process payrolls
 */
public record PayrollProcessRequest(
        @NotNull(message = "Month is required")
        @Min(value = 1, message = "Month must be between 1 and 12")
        @Max(value = 12, message = "Month must be between 1 and 12")
        Integer month,

        @NotNull(message = "Year is required")
        @Min(value = 2000, message = "Year must be 2000 or later")
        Integer year
) {
}