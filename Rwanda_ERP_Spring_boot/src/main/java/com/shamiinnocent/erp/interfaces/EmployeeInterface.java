package com.shamiinnocent.erp.interfaces;

import com.shamiinnocent.erp.Dtos.requests.EmployeeRequest;
import com.shamiinnocent.erp.Dtos.requests.LoginRequest;
import com.shamiinnocent.erp.Dtos.responses.EmployeeResponse;
import com.shamiinnocent.erp.Dtos.responses.JwtTokenResponse;
import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.enums.Account;

import java.util.List;

/**
 * Interface defining operations for managing employees in the ERP system.
 */
public interface EmployeeInterface {
    /**
     * Creates a new employee in the system.
     *
     * @param request the employee details
     * @return the created employee
     */
    EmployeeResponse createEmployee(EmployeeRequest request);

    /**
     * Updates an existing employee in the system.
     *
     * @param id the ID of the employee to update
     * @param request the updated employee details
     * @return the updated employee
     */
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    /**
     * Gets an employee by their ID.
     *
     * @param id the ID of the employee to get
     * @return the employee
     */
    EmployeeResponse getEmployeeById(Long id);

    /**
     * Gets an employee by their code.
     *
     * @param code the code of the employee to get
     * @return the employee
     */
    EmployeeResponse getEmployeeByCode(String code);

    /**
     * Gets all employees in the system.
     *
     * @return a list of all employees
     */
    List<EmployeeResponse> getAllEmployees();

    /**
     * Gets all employees with a given status.
     *
     * @param status the status to filter by
     * @return a list of employees with the given status
     */
    List<EmployeeResponse> getEmployeesByStatus(Account status);

    /**
     * Deletes an employee from the system.
     *
     * @param id the ID of the employee to delete
     */
    void deleteEmployee(Long id);

    /**
     * Activates an employee's account.
     *
     * @param id the ID of the employee to activate
     * @return the activated employee
     */
    EmployeeResponse activateEmployee(Long id);

    /**
     * Deactivates an employee's account.
     *
     * @param id the ID of the employee to deactivate
     * @return the deactivated employee
     */
    EmployeeResponse deactivateEmployee(Long id);

    /**
     * Authenticates an employee and returns a JWT token.
     *
     * @param request the login request
     * @return a JWT token response
     */
    JwtTokenResponse login(LoginRequest request);

    /**
     * Gets the currently authenticated employee.
     *
     * @return the authenticated employee
     */
    EmployeeResponse getCurrentEmployee();

    /**
     * Finds an employee by their email.
     *
     * @param email the email to search for
     * @return the employee entity
     */
    Employee findByEmail(String email);

    /**
     * Creates a new employee in the system.
     *
     * @param request the employee details
     * @return the created employee
     */
    EmployeeResponse createManager(EmployeeRequest request);

}