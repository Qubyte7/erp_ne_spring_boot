package com.shamiinnocent.erp.Repositories;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.enums.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Employee entities.
 */
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    /**
     * Finds an employee by their email address.
     *
     * @param email the email address to search for
     * @return an optional containing the employee, or empty if not found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Finds an employee by their code.
     *
     * @param code the employee code to search for
     * @return an optional containing the employee, or empty if not found
     */
    Optional<Employee> findByCode(String code);

    /**
     * Checks if an employee exists with the given email address.
     *
     * @param email the email address to check
     * @return true if an employee exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks if an employee exists with the given code.
     *
     * @param code the employee code to check
     * @return true if an employee exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Finds all employees with the given status.
     *
     * @param status the status to search for
     * @return a list of employees with the given status
     */
    List<Employee> findByStatus(Account status);
}