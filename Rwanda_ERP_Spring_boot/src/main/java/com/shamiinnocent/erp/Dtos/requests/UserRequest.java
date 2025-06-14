package com.shamiinnocent.erp.Dtos.requests;

import com.shamiinnocent.erp.enums.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * A DTO for creating or updating a user in the vehicle tracking system.
 * Includes user details with strict validation for secure registration.
 *
 * @param userName  the user's user name
 * @param email the user's email address
 * @param password   the user's password
// * @param mobile the user's mobile number
// * @param nationalId the user's national ID
 * @author Fortress Backend
 * @since 1.0
 */
public record UserRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 15, message = "First name must be 15 characters or less")
        @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
        String userName,


        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 50, message = "Email must be 50 characters or less")
        String email,


        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must contain at least one letter and one number")
        String password,

        Role role
//        @NotBlank(message = "Mobile number is required")
//        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid mobile number format")
//        String mobile,
//
//        @NotNull(message = "Date of birth is required")
//        @PastOrPresent(message = "Date of birth must be in the past or present")
//        LocalDate dob,



//        @NotBlank(message = "National ID is required")
//        @Size(min = 16, max = 16, message = "National ID must be exactly 16 characters")
//        @Pattern(regexp = "^[A-Za-z0-9\\-]{16}$", message = "National ID must be 16 alphanumeric characters, optionally including hyphens")
//        String nationalId


) {
}