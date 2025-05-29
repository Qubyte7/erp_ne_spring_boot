package com.shamiinnocent.erp.Dtos.requests;

import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.EmployementStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * A DTO for creating or updating an employment record in the ERP system.
 * Includes employment details with validation.
 *
 * @param code        the employment record's unique code
 * @param employeeId  the ID of the employee associated with this employment record
 * @param department  the department where the employee works
 * @param position    the position or job title of the employee
 * @param baseSalary  the base salary for the employee
 * @param joiningDate the date when the employee joined the company
 * @param status      the employment status (active or inactive)
 */
public record EmploymentRequest(
        @NotBlank(message = "Employment code is required")
        @Size(max = 20, message = "Employment code must be 20 characters or less")
        @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Employment code must contain only letters, numbers, and hyphens")
        String code,

        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotBlank(message = "Department is required")
        @Size(max = 100, message = "Department must be 100 characters or less")
        String department,

        @NotBlank(message = "Position is required")
        @Size(max = 100, message = "Position must be 100 characters or less")
        String position,

        @NotNull(message = "Base salary is required")
        @Positive(message = "Base salary must be positive")
        Double baseSalary,

        @NotNull(message = "Joining date is required")
        @PastOrPresent(message = "Joining date must be in the past or present")
        LocalDate joiningDate,

        EmployementStatus status
) {
}