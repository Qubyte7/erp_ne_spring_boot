package com.shamiinnocent.erp.controllers;

import com.shamiinnocent.erp.Dtos.requests.EmploymentRequest;
import com.shamiinnocent.erp.Dtos.responses.EmploymentResponse;
import com.shamiinnocent.erp.services.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing employment records in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/employments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employment Management", description = "APIs for managing employment records")
public class EmploymentController {

    private final EmploymentService employmentService;

    /**
     * Creates a new employment record.
     *
     * @param request the employment details
     * @return the created employment record
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Create a new employment record", description = "Creates a new employment record in the system")
    public ResponseEntity<EmploymentResponse> createEmployment(@Valid @RequestBody EmploymentRequest request) {
        log.info("Creating employment with code: {}", request.code());
        EmploymentResponse response = employmentService.createEmployment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing employment record.
     *
     * @param id the ID of the employment record to update
     * @param request the updated employment details
     * @return the updated employment record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Update an employment record", description = "Updates an existing employment record in the system")
    public ResponseEntity<EmploymentResponse> updateEmployment(
            @PathVariable Long id,
            @Valid @RequestBody EmploymentRequest request) {
        log.info("Updating employment with ID: {}", id);
        EmploymentResponse response = employmentService.updateEmployment(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets an employment record by ID.
     *
     * @param id the ID of the employment record to get
     * @return the employment record
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get an employment record by ID", description = "Retrieves an employment record by its ID")
    public ResponseEntity<EmploymentResponse> getEmploymentById(@PathVariable Long id) {
        log.info("Getting employment with ID: {}", id);
        EmploymentResponse response = employmentService.getEmploymentById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets an employment record by code.
     *
     * @param code the code of the employment record to get
     * @return the employment record
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get an employment record by code", description = "Retrieves an employment record by its code")
    public ResponseEntity<EmploymentResponse> getEmploymentByCode(@PathVariable String code) {
        log.info("Getting employment with code: {}", code);
        EmploymentResponse response = employmentService.getEmploymentByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets all employment records.
     *
     * @return a list of all employment records
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get all employment records", description = "Retrieves all employment records in the system")
    public ResponseEntity<List<EmploymentResponse>> getAllEmployments() {
        log.info("Getting all employments");
        List<EmploymentResponse> responses = employmentService.getAllEmployments();
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets all employment records for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of employment records for the employee
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get employment records by employee", description = "Retrieves all employment records for a given employee")
    public ResponseEntity<List<EmploymentResponse>> getEmploymentsByEmployee(@PathVariable Long employeeId) {
        log.info("Getting employments for employee with ID: {}", employeeId);
        List<EmploymentResponse> responses = employmentService.getEmploymentsByEmployee(employeeId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets all active employment records for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of active employment records for the employee
     */
    @GetMapping("/employee/{employeeId}/active")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get active employment records by employee", description = "Retrieves all active employment records for a given employee")
    public ResponseEntity<List<EmploymentResponse>> getActiveEmploymentsByEmployee(@PathVariable Long employeeId) {
        log.info("Getting active employments for employee with ID: {}", employeeId);
        List<EmploymentResponse> responses = employmentService.getActiveEmploymentsByEmployee(employeeId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Deletes an employment record.
     *
     * @param id the ID of the employment record to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Delete an employment record", description = "Deletes an employment record from the system")
    public ResponseEntity<String> deleteEmployment(@PathVariable Long id) {
        log.info("Deleting employment with ID: {}", id);
        employmentService.deleteEmployment(id);
        return ResponseEntity.ok("Employment record deleted successfully");
    }

    /**
     * Activates an employment record.
     *
     * @param id the ID of the employment record to activate
     * @return the activated employment record
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Activate an employment record", description = "Activates an employment record")
    public ResponseEntity<EmploymentResponse> activateEmployment(@PathVariable Long id) {
        log.info("Activating employment with ID: {}", id);
        EmploymentResponse response = employmentService.activateEmployment(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivates an employment record.
     *
     * @param id the ID of the employment record to deactivate
     * @return the deactivated employment record
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Deactivate an employment record", description = "Deactivates an employment record")
    public ResponseEntity<EmploymentResponse> deactivateEmployment(@PathVariable Long id) {
        log.info("Deactivating employment with ID: {}", id);
        EmploymentResponse response = employmentService.deactivateEmployment(id);
        return ResponseEntity.ok(response);
    }
}