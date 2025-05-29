package com.shamiinnocent.erp.controllers;

import com.shamiinnocent.erp.Dtos.requests.EmployeeRequest;
import com.shamiinnocent.erp.Dtos.requests.LoginRequest;
import com.shamiinnocent.erp.Dtos.responses.EmployeeResponse;
import com.shamiinnocent.erp.Dtos.responses.JwtTokenResponse;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.services.EmployeeService;
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
 * Controller for managing employees in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Creates a new employee.
     *
     * @param request the employee details
     * @return the created employee
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Create a new employee", description = "Creates a new employee in the system")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        log.info("Creating employee with code: {}", request.code());
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Creates a new ADMIN
     *
     * @param request the employee details
     * @return the created employee
     */
    @PostMapping("/manager")
    @Operation(summary = "Create a new employee", description = "Creates a new employee in the system")
    public ResponseEntity<EmployeeResponse> createManager(@Valid @RequestBody EmployeeRequest request) {
        log.info("Creating employee with code: {}", request.code());
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Creates a new ADMIN
     *
     * @param request the employee details
     * @return the created employee
     */
    @PostMapping("/admin")
    @Operation(summary = "Create a new employee", description = "Creates a new employee in the system")
    public ResponseEntity<EmployeeResponse> createAdmin(@Valid @RequestBody EmployeeRequest request) {
        log.info("Creating employee with code: {}", request.code());
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing employee.
     *
     * @param id the ID of the employee to update
     * @param request the updated employee details
     * @return the updated employee
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Update an employee", description = "Updates an existing employee in the system")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        log.info("Updating employee with ID: {}", id);
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets an employee by ID.
     *
     * @param id the ID of the employee to get
     * @return the employee
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get an employee by ID", description = "Retrieves an employee by their ID")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        log.info("Getting employee with ID: {}", id);
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets an employee by code.
     *
     * @param code the code of the employee to get
     * @return the employee
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get an employee by code", description = "Retrieves an employee by their code")
    public ResponseEntity<EmployeeResponse> getEmployeeByCode(@PathVariable String code) {
        log.info("Getting employee with code: {}", code);
        EmployeeResponse response = employeeService.getEmployeeByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets all employees.
     *
     * @return a list of all employees
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get all employees", description = "Retrieves all employees in the system")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        log.info("Getting all employees");
        List<EmployeeResponse> responses = employeeService.getAllEmployees();
        return ResponseEntity.ok(responses);
    }

    /**
     * Gets all employees with a given status.
     *
     * @param status the status to filter by
     * @return a list of employees with the given status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get employees by status", description = "Retrieves all employees with a given status")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByStatus(@PathVariable Account status) {
        log.info("Getting employees with status: {}", status);
        List<EmployeeResponse> responses = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(responses);
    }

    /**
     * Deletes an employee.
     *
     * @param id the ID of the employee to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Delete an employee", description = "Deletes an employee from the system")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        log.info("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    /**
     * Activates an employee's account.
     *
     * @param id the ID of the employee to activate
     * @return the activated employee
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Activate an employee", description = "Activates an employee's account")
    public ResponseEntity<EmployeeResponse> activateEmployee(@PathVariable Long id) {
        log.info("Activating employee with ID: {}", id);
        EmployeeResponse response = employeeService.activateEmployee(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivates an employee's account.
     *
     * @param id the ID of the employee to deactivate
     * @return the deactivated employee
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Deactivate an employee", description = "Deactivates an employee's account")
    public ResponseEntity<EmployeeResponse> deactivateEmployee(@PathVariable Long id) {
        log.info("Deactivating employee with ID: {}", id);
        EmployeeResponse response = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the current employee's profile.
     *
     * @return the current employee
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get current employee", description = "Retrieves the profile of the currently authenticated employee")
    public ResponseEntity<EmployeeResponse> getCurrentEmployee() {
        log.info("Getting current employee");
        EmployeeResponse response = employeeService.getCurrentEmployee();
        return ResponseEntity.ok(response);
    }

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
}