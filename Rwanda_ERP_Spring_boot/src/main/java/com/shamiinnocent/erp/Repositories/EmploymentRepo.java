package com.shamiinnocent.erp.Repositories;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.Employment;
import com.shamiinnocent.erp.enums.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Employment entities.
 */
@Repository
public interface EmploymentRepo extends JpaRepository<Employment, Long> {
    /**
     * Finds an employment record by its code.
     *
     * @param code the employment code to search for
     * @return an optional containing the employment record, or empty if not found
     */
    Optional<Employment> findByCode(String code);

    /**
     * Finds all employment records for a given employee.
     *
     * @param employee the employee to search for
     * @return a list of employment records for the employee
     */
    List<Employment> findByEmployee(Employee employee);

    /**
     * Finds all active employment records for a given employee.
     *
     * @param employee the employee to search for
     * @param status the status to search for (typically ACTIVE)
     * @return a list of active employment records for the employee
     */
    List<Employment> findByEmployeeAndStatus(Employee employee, Account status);

    /**
     * Checks if an employment record exists with the given code.
     *
     * @param code the employment code to check
     * @return true if an employment record exists, false otherwise
     */
    boolean existsByCode(String code);

}