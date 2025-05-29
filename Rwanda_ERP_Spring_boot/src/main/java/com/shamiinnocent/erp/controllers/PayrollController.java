package com.shamiinnocent.erp.controllers;

import com.shamiinnocent.erp.Dtos.requests.PayrollProcessRequest;
import com.shamiinnocent.erp.Dtos.responses.PaySlipResponse;
import com.shamiinnocent.erp.services.NotificationService;
import com.shamiinnocent.erp.services.PayrollService;
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
 * Controller for managing payrolls in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payroll Management", description = "APIs for processing payrolls and managing pay slips")
public class PayrollController {

    private final PayrollService payrollService;
    private final NotificationService notificationService;

    /**
     * Processes payroll for all active employees for a given month and year.
     *
     * @param request the payroll process request containing month and year
     * @return a list of generated pay slips
     */
    @PostMapping("/process")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "Process payroll", description = "Processes payroll for all active employees for a given month and year")
    public ResponseEntity<List<PaySlipResponse>> processPayroll(@Valid @RequestBody PayrollProcessRequest request) {
        log.info("Processing payroll for month: {}, year: {}", request.month(), request.year());
        List<PaySlipResponse> responses = payrollService.processPayroll(request);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    /**
     * Approves all pending pay slips for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of approved pay slips
     */
    @PatchMapping("/approve/{year}/{month}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Approve payroll", description = "Approves all pending pay slips for a given month and year")
    public ResponseEntity<List<PaySlipResponse>> approvePayroll(
            @PathVariable int year,
            @PathVariable int month) {
        log.info("Approving payroll for month: {}, year: {}", month, year);
        List<PaySlipResponse> responses = payrollService.approvePayroll(month, year);
        
        // Send notifications to employees
        notificationService.notifyEmployeesForApprovedPayrolls(month, year);
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets all pay slips for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of pay slips
     */
    @GetMapping("/slips/{year}/{month}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get pay slips by month/year", description = "Retrieves all pay slips for a given month and year")
    public ResponseEntity<List<PaySlipResponse>> getPaySlipsByMonthYear(
            @PathVariable int year,
            @PathVariable int month) {
        log.info("Getting pay slips for month: {}, year: {}", month, year);
        List<PaySlipResponse> responses = payrollService.getPaySlipsByMonthYear(month, year);
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets all pay slips for the current employee.
     *
     * @return a list of pay slips for the current employee
     */
    @GetMapping("/slips/me")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @Operation(summary = "Get current employee pay slips", description = "Retrieves all pay slips for the current employee")
    public ResponseEntity<List<PaySlipResponse>> getCurrentEmployeePaySlips() {
        log.info("Getting pay slips for current employee");
        List<PaySlipResponse> responses = payrollService.getCurrentEmployeePaySlips();
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets a pay slip for the current employee for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return the pay slip
     */
    @GetMapping("/slips/me/{year}/{month}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @Operation(summary = "Get current employee pay slip by month/year", description = "Retrieves a pay slip for the current employee for a given month and year")
    public ResponseEntity<PaySlipResponse> getCurrentEmployeePaySlipByMonthYear(
            @PathVariable int year,
            @PathVariable int month) {
        log.info("Getting pay slip for current employee for month: {}, year: {}", month, year);
        PaySlipResponse response = payrollService.getCurrentEmployeePaySlipByMonthYear(month, year);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a pay slip by its ID.
     *
     * @param id the ID of the pay slip
     * @return the pay slip
     */
    @GetMapping("/slips/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get pay slip by ID", description = "Retrieves a pay slip by its ID")
    public ResponseEntity<PaySlipResponse> getPaySlipById(@PathVariable Long id) {
        log.info("Getting pay slip with ID: {}", id);
        PaySlipResponse response = payrollService.getPaySlipById(id);
        return ResponseEntity.ok(response);
    }
}