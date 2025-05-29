package com.shamiinnocent.erp.services;

import com.shamiinnocent.erp.Dtos.requests.EmploymentRequest;
import com.shamiinnocent.erp.Dtos.responses.EmploymentResponse;
import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.Employment;
import com.shamiinnocent.erp.Repositories.EmployeeRepo;
import com.shamiinnocent.erp.Repositories.EmploymentRepo;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.EmployementStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing employment records in the ERP system.
 * Handles CRUD operations for employment records and ensures that
 * employees have the correct employment information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmploymentService {

    private final EmploymentRepo employmentRepo;
    private final EmployeeRepo employeeRepo;

    /**
     * Creates a new employment record in the system.
     *
     * @param request the employment details
     * @return the created employment record
     */
    @Transactional
    public EmploymentResponse createEmployment(EmploymentRequest request) {
        log.info("Creating employment with code: {}", request.code());

        // Check if employment with the same code already exists
        if (employmentRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Employment with code " + request.code() + " already exists");
        }

        // Find employee
        Employee employee = employeeRepo.findById(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + request.employeeId() + " not found"));

        // Create new employment
        Employment employment = Employment.builder()
                .code(request.code())
                .employee(employee)
                .department(request.department())
                .position(request.position())
                .baseSalary(request.baseSalary())
                .joiningDate(request.joiningDate())
                .status(EmployementStatus.ACTIVE)
                .build();

        // Save employment
        Employment savedEmployment = employmentRepo.save(employment);
        log.info("Employment created with ID: {}", savedEmployment.getId());

        return EmploymentResponse.fromEntity(savedEmployment);
    }

    /**
     * Updates an existing employment record in the system.
     *
     * @param id      the ID of the employment record to update
     * @param request the updated employment details
     * @return the updated employment record
     */
    @Transactional
    public EmploymentResponse updateEmployment(Long id, EmploymentRequest request) {
        log.info("Updating employment with ID: {}", id);

        // Find employment
        Employment employment = employmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employment with ID " + id + " not found"));

        // Check if code is being changed and if it conflicts with existing employments
        if (!employment.getCode().equals(request.code()) && employmentRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Employment with code " + request.code() + " already exists");
        }

        // Find employee if employee ID is being changed
        Employee employee = employment.getEmployee();
        if (!employment.getEmployee().getId().equals(request.employeeId())) {
            employee = employeeRepo.findById(request.employeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + request.employeeId() + " not found"));
        }

        // Update employment
        employment.setCode(request.code());
        employment.setEmployee(employee);
        employment.setDepartment(request.department());
        employment.setPosition(request.position());
        employment.setBaseSalary(request.baseSalary());
        employment.setJoiningDate(request.joiningDate());
        employment.setStatus(request.status());

        // Save employment
        Employment updatedEmployment = employmentRepo.save(employment);
        log.info("Employment updated with ID: {}", updatedEmployment.getId());

        return EmploymentResponse.fromEntity(updatedEmployment);
    }

    /**
     * Gets an employment record by its ID.
     *
     * @param id the ID of the employment record to get
     * @return the employment record
     */
    public EmploymentResponse getEmploymentById(Long id) {
        log.info("Getting employment with ID: {}", id);

        Employment employment = employmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employment with ID " + id + " not found"));

        return EmploymentResponse.fromEntity(employment);
    }

    /**
     * Gets an employment record by its code.
     *
     * @param code the code of the employment record to get
     * @return the employment record
     */
    public EmploymentResponse getEmploymentByCode(String code) {
        log.info("Getting employment with code: {}", code);

        Employment employment = employmentRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Employment with code " + code + " not found"));

        return EmploymentResponse.fromEntity(employment);
    }

    /**
     * Gets all employment records in the system.
     *
     * @return a list of all employment records
     */
    public List<EmploymentResponse> getAllEmployments() {
        log.info("Getting all employments");

        return employmentRepo.findAll().stream()
                .map(EmploymentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets all employment records for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of employment records for the employee
     */
    public List<EmploymentResponse> getEmploymentsByEmployee(Long employeeId) {
        log.info("Getting employments for employee with ID: {}", employeeId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeId + " not found"));

        return employmentRepo.findByEmployee(employee).stream()
                .map(EmploymentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets all active employment records for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of active employment records for the employee
     */
    public List<EmploymentResponse> getActiveEmploymentsByEmployee(Long employeeId) {
        log.info("Getting active employments for employee with ID: {}", employeeId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeId + " not found"));

        return employmentRepo.findByEmployeeAndStatus(employee, Account.ACTIVE).stream()
                .map(EmploymentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an employment record from the system.
     *
     * @param id the ID of the employment record to delete
     */
    @Transactional
    public void deleteEmployment(Long id) {
        log.info("Deleting employment with ID: {}", id);

        if (!employmentRepo.existsById(id)) {
            throw new IllegalArgumentException("Employment with ID " + id + " not found");
        }

        employmentRepo.deleteById(id);
        log.info("Employment deleted with ID: {}", id);
    }

    /**
     * Activates an employment record.
     *
     * @param id the ID of the employment record to activate
     * @return the activated employment record
     */
    @Transactional
    public EmploymentResponse activateEmployment(Long id) {
        log.info("Activating employment with ID: {}", id);

        Employment employment = employmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employment with ID " + id + " not found"));

        employment.setStatus(EmployementStatus.ACTIVE);
        Employment activatedEmployment = employmentRepo.save(employment);
        log.info("Employment activated with ID: {}", activatedEmployment.getId());

        return EmploymentResponse.fromEntity(activatedEmployment);
    }

    /**
     * Deactivates an employment record.
     *
     * @param id the ID of the employment record to deactivate
     * @return the deactivated employment record
     */
    @Transactional
    public EmploymentResponse deactivateEmployment(Long id) {
        log.info("Deactivating employment with ID: {}", id);

        Employment employment = employmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employment with ID " + id + " not found"));

        employment.setStatus(EmployementStatus.INACTIVE);
        Employment deactivatedEmployment = employmentRepo.save(employment);
        log.info("Employment deactivated with ID: {}", deactivatedEmployment.getId());

        return EmploymentResponse.fromEntity(deactivatedEmployment);
    }
}