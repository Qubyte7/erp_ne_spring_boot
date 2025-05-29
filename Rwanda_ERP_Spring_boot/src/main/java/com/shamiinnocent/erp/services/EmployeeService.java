package com.shamiinnocent.erp.services;

import com.shamiinnocent.erp.Dtos.requests.EmployeeRequest;
import com.shamiinnocent.erp.Dtos.requests.LoginRequest;
import com.shamiinnocent.erp.Dtos.responses.EmployeeResponse;
import com.shamiinnocent.erp.Dtos.responses.JwtTokenResponse;
import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Repositories.EmployeeRepo;
import com.shamiinnocent.erp.config.JwtConfig;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.Role;
import com.shamiinnocent.erp.interfaces.EmployeeInterface;
import com.shamiinnocent.erp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing employees in the ERP system.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements EmployeeInterface {

    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;



    /**
     * Creates a new employee in the system.
     *
     * @param request the employee details
     * @return the created employee
     */
    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        log.info("Creating employee with code: {}", request.code());

        // Check if employee with the same code or email already exists
        if (employeeRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Employee with code " + request.code() + " already exists");
        }
        if (employeeRepo.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Employee with email " + request.email() + " already exists");
        }

        // Create new employee
        Employee employee = Employee.builder()
                .code(request.code())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .mobile(request.mobile())
                .dateOfBirth(request.dateOfBirth())
                .role(request.role())
                .status(Account.DISABLED) // New employees are inactive by default
                .build();

        // Save employee
        Employee savedEmployee = employeeRepo.save(employee);
        log.info("Employee created with ID: {}", savedEmployee.getId());

        return EmployeeResponse.fromEntity(savedEmployee);
    }




    /**
     * Updates an existing employee in the system.
     *
     * @param id      the ID of the employee to update
     * @param request the updated employee details
     * @return the updated employee
     */
    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        log.info("Updating employee with ID: {}", id);

        // Find employee
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));

        // Check if code or email is being changed and if it conflicts with existing employees
        if (!employee.getCode().equals(request.code()) && employeeRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Employee with code " + request.code() + " already exists");
        }
        if (!employee.getEmail().equals(request.email()) && employeeRepo.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Employee with email " + request.email() + " already exists");
        }

        // Update employee
        employee.setCode(request.code());
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setMobile(request.mobile());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setRole(request.role());

        // Update password if provided
        if (request.password() != null && !request.password().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(request.password()));
        }

        // Save employee
        Employee updatedEmployee = employeeRepo.save(employee);
        log.info("Employee updated with ID: {}", updatedEmployee.getId());

        return EmployeeResponse.fromEntity(updatedEmployee);
    }

    /**
     * Gets an employee by their ID.
     *
     * @param id the ID of the employee to get
     * @return the employee
     */
    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        log.info("Getting employee with ID: {}", id);

        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));

        return EmployeeResponse.fromEntity(employee);
    }

    /**
     * Gets an employee by their code.
     *
     * @param code the code of the employee to get
     * @return the employee
     */
    @Override
    public EmployeeResponse getEmployeeByCode(String code) {
        log.info("Getting employee with code: {}", code);

        Employee employee = employeeRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Employee with code " + code + " not found"));

        return EmployeeResponse.fromEntity(employee);
    }

    /**
     * Gets all employees in the system.
     *
     * @return a list of all employees
     */
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        log.info("Getting all employees");

        return employeeRepo.findAll().stream()
                .map(EmployeeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets all employees with a given status.
     *
     * @param status the status to filter by
     * @return a list of employees with the given status
     */
    @Override
    public List<EmployeeResponse> getEmployeesByStatus(Account status) {
        log.info("Getting employees with status: {}", status);

        return employeeRepo.findByStatus(status).stream()
                .map(EmployeeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an employee from the system.
     *
     * @param id the ID of the employee to delete
     */
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        if (!employeeRepo.existsById(id)) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found");
        }

        employeeRepo.deleteById(id);
        log.info("Employee deleted with ID: {}", id);
    }

    /**
     * Activates an employee's account.
     *
     * @param id the ID of the employee to activate
     * @return the activated employee
     */
    @Override
    @Transactional
    public EmployeeResponse activateEmployee(Long id) {
        log.info("Activating employee with ID: {}", id);

        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));

        employee.setStatus(Account.ACTIVE);
        Employee activatedEmployee = employeeRepo.save(employee);
        log.info("Employee activated with ID: {}", activatedEmployee.getId());

        return EmployeeResponse.fromEntity(activatedEmployee);
    }

    /**
     * Deactivates an employee's account.
     *
     * @param id the ID of the employee to deactivate
     * @return the deactivated employee
     */
    @Override
    @Transactional
    public EmployeeResponse deactivateEmployee(Long id) {
        log.info("Deactivating employee with ID: {}", id);

        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));

        employee.setStatus(Account.DISABLED);
        Employee deactivatedEmployee = employeeRepo.save(employee);
        log.info("Employee deactivated with ID: {}", deactivatedEmployee.getId());

        return EmployeeResponse.fromEntity(deactivatedEmployee);
    }

    /**
     * Authenticates an employee and returns a JWT token.
     *
     * @param request the login request
     * @return a JWT token response
     */
    @Override
    public JwtTokenResponse login(LoginRequest request) {
        log.info("Authenticating employee with email: {}", request.email());

        // Authenticate employee
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Find employee
        Employee employee = findByEmail(request.email());

        // Generate JWT token
        String token = JwtUtil.createToken(
                jwtConfig.getSecretKey(),
                employee.getEmail(),
                jwtConfig.getExpiryInSeconds()
        );

        log.info("Employee authenticated with email: {}", request.email());

        return new JwtTokenResponse(token, employee.getEmail(), employee.getRole().toString());
    }

    /**
     * Gets the currently authenticated employee.
     *
     * @return the authenticated employee
     */
    @Override
    public EmployeeResponse getCurrentEmployee() {
        log.info("Getting current employee");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Employee employee = findByEmail(email);
        return EmployeeResponse.fromEntity(employee);
    }

    /**
     * Finds an employee by their email.
     *
     * @param email the email to search for
     * @return the employee entity
     */
    @Override
    public Employee findByEmail(String email) {
        log.info("Finding employee with email: {}", email);

        return employeeRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee with email " + email + " not found"));
    }
}