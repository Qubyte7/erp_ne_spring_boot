package com.shamiinnocent.erp.controllers;

import com.shamiinnocent.erp.Dtos.requests.DeductionRequest;
import com.shamiinnocent.erp.Dtos.responses.DeductionResponse;
import com.shamiinnocent.erp.services.DeductionService;
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
 * Controller for managing deductions in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Deduction Management", description = "APIs for managing deductions and tax rates")
public class DeductionController {

    private final DeductionService deductionService;

    /**
     * Creates a new deduction.
     *
     * @param request the deduction details
     * @return the created deduction
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Create a new deduction", description = "Creates a new deduction in the system")
    public ResponseEntity<DeductionResponse> createDeduction(@Valid @RequestBody DeductionRequest request) {
        log.info("Creating deduction with code: {}", request.code());
        DeductionResponse response = deductionService.createDeduction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing deduction.
     *
     * @param id the ID of the deduction to update
     * @param request the updated deduction details
     * @return the updated deduction
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Update a deduction", description = "Updates an existing deduction in the system")
    public ResponseEntity<DeductionResponse> updateDeduction(
            @PathVariable Long id,
            @Valid @RequestBody DeductionRequest request) {
        log.info("Updating deduction with ID: {}", id);
        DeductionResponse response = deductionService.updateDeduction(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a deduction by ID.
     *
     * @param id the ID of the deduction to get
     * @return the deduction
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get a deduction by ID", description = "Retrieves a deduction by its ID")
    public ResponseEntity<DeductionResponse> getDeductionById(@PathVariable Long id) {
        log.info("Getting deduction with ID: {}", id);
        DeductionResponse response = deductionService.getDeductionById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a deduction by code.
     *
     * @param code the code of the deduction to get
     * @return the deduction
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get a deduction by code", description = "Retrieves a deduction by its code")
    public ResponseEntity<DeductionResponse> getDeductionByCode(@PathVariable String code) {
        log.info("Getting deduction with code: {}", code);
        DeductionResponse response = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a deduction by name.
     *
     * @param name the name of the deduction to get
     * @return the deduction
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get a deduction by name", description = "Retrieves a deduction by its name")
    public ResponseEntity<DeductionResponse> getDeductionByName(@PathVariable String name) {
        log.info("Getting deduction with name: {}", name);
        DeductionResponse response = deductionService.getDeductionByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets all deductions.
     *
     * @return a list of all deductions
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Get all deductions", description = "Retrieves all deductions in the system")
    public ResponseEntity<List<DeductionResponse>> getAllDeductions() {
        log.info("Getting all deductions");
        List<DeductionResponse> responses = deductionService.getAllDeductions();
        return ResponseEntity.ok(responses);
    }

    /**
     * Deletes a deduction.
     *
     * @param id the ID of the deduction to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Delete a deduction", description = "Deletes a deduction from the system")
    public ResponseEntity<String> deleteDeduction(@PathVariable Long id) {
        log.info("Deleting deduction with ID: {}", id);
        deductionService.deleteDeduction(id);
        return ResponseEntity.ok("Deduction deleted successfully");
    }

    /**
     * Initializes default deductions.
     * This endpoint is used to create the default deductions if they don't exist.
     *
     * @return a success message
     */
    @PostMapping("/initialize")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Initialize default deductions", description = "Creates the default deductions if they don't exist")
    public ResponseEntity<String> initializeDefaultDeductions() {
        log.info("Initializing default deductions");
        deductionService.initializeDefaultDeductions();
        return ResponseEntity.ok("Default deductions initialized successfully");
    }
}