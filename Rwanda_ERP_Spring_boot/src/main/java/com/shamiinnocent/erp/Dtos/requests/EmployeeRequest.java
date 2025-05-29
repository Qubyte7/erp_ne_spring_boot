package com.shamiinnocent.erp.Dtos.requests;

import com.shamiinnocent.erp.enums.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * A DTO for creating or updating an employee in the ERP system.
 * Includes employee details with validation for secure registration.
 *
 * @param code       the employee's unique code
 * @param firstName  the employee's first name
 * @param lastName   the employee's last name
 * @param email      the employee's email address
 * @param password   the employee's password (required for new employees)
 * @param mobile     the employee's mobile number
 * @param dateOfBirth the employee's date of birth
 * @param role       the employee's role in the system
 */
public record EmployeeRequest(
        @NotBlank(message = "Employee code is required")
        @Size(max = 20, message = "Employee code must be 20 characters or less")
        @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Employee code must contain only letters, numbers, and hyphens")
        String code,

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be 50 characters or less")
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "First name must contain only letters and spaces")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be 50 characters or less")
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Last name must contain only letters and spaces")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must be 100 characters or less")
        String email,

        String password,

        @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Invalid mobile number format")
        String mobile,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        Role role
) {
}