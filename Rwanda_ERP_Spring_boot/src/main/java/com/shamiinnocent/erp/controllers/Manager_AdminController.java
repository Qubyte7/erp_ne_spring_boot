package com.shamiinnocent.erp.controllers;


import com.shamiinnocent.erp.Dtos.requests.EmployeeRequest;
import com.shamiinnocent.erp.Dtos.responses.EmployeeResponse;
import com.shamiinnocent.erp.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing employees in the ERP system.
 */
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin & MANAGER Management", description = "APIs for managing managers and admins")
@SecurityRequirement(name = "none")
public class Manager_AdminController {

    private final EmployeeService employeeService;

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

}
