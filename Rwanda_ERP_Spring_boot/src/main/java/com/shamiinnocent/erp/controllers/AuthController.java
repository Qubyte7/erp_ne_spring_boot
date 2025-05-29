package com.shamiinnocent.erp.controllers;


import com.shamiinnocent.erp.Dtos.requests.EmployeeRequest;
import com.shamiinnocent.erp.Dtos.requests.LoginRequest;
import com.shamiinnocent.erp.Dtos.responses.EmployeeResponse;
import com.shamiinnocent.erp.Dtos.responses.JwtTokenResponse;
import com.shamiinnocent.erp.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing employee Login (Auth) in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "none")
public class AuthController {

    private final EmployeeService employeeService;

    /**
     * Authenticates an employee and returns a JWT token.
     *
     * @param request the login request
     * @return a JWT token response
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates an employee and returns a JWT token")
    public ResponseEntity<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Authenticating employee with email: {}", request.email());
        JwtTokenResponse response = employeeService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new Manager
     *
     * @param request the employee details
     * @return the created manager
     */
    @PostMapping("/manager")
    @Operation(summary = "Create a new Manager", description = "Creates a new manager in the system")
    public ResponseEntity<EmployeeResponse> createManager(@Valid @RequestBody EmployeeRequest request) {
        log.info("Creating Manager with code: {}", request.code());
        EmployeeResponse response = employeeService.createManager(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Activates a manager's account.
     *
     * @param id the ID of the manager to activate
     * @return the activated manager
     */
    @PatchMapping("/manager/{id}/activate")
    @Operation(summary = "Activate a manager", description = "Activates a manager's account")
    public ResponseEntity<EmployeeResponse> activateManager(@PathVariable Long id) {
        log.info("Activating manager with ID: {}", id);
        EmployeeResponse response = employeeService.activateEmployee(id);
        return ResponseEntity.ok(response);
    }
}
