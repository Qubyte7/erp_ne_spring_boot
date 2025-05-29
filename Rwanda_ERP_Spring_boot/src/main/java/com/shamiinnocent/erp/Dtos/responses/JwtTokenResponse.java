package com.shamiinnocent.erp.Dtos.responses;

import jakarta.validation.constraints.NotBlank;

/**
 * A DTO for responding with a JWT token in the ERP system.
 * Encapsulates the token issued after successful authentication,
 * along with the user's email and role.
 *
 * @param token the JWT token
 * @param email the user's email
 * @param role the user's role
 */
public record JwtTokenResponse(

        @NotBlank(message = "JWT token is required")
        String token,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Role is required")
        String role
) {
}
