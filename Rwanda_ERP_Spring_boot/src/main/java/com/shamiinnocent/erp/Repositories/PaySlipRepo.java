package com.shamiinnocent.erp.Repositories;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.PaySlip;
import com.shamiinnocent.erp.enums.SlipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing PaySlip entities.
 */
@Repository
public interface PaySlipRepo extends JpaRepository<PaySlip, Long> {
    /**
     * Finds all pay slips for a given employee.
     *
     * @param employee the employee to search for
     * @return a list of pay slips for the employee
     */
    List<PaySlip> findByEmployee(Employee employee);

    /**
     * Finds all pay slips for a given month and year.
     *
     * @param yearMonth the month and year to search for
     * @return a list of pay slips for the given month and year
     */
    List<PaySlip> findByYearMonth(YearMonth yearMonth);

    /**
     * Finds all pay slips for a given employee and month/year.
     *
     * @param employee the employee to search for
     * @param yearMonth the month and year to search for
     * @return a list of pay slips for the given employee and month/year
     */
    Optional<PaySlip> findByEmployeeAndYearMonth(Employee employee, YearMonth yearMonth);

    /**
     * Finds all pay slips with a given status.
     *
     * @param status the status to search for (e.g., "PENDING" or "PAID")
     * @return a list of pay slips with the given status
     */
    List<PaySlip> findByStatus(SlipStatus status);

    /**
     * Finds all pay slips for a given month/year with a given status.
     *
     * @param yearMonth the month and year to search for
     * @param status the status to search for
     * @return a list of pay slips for the given month/year with the given status
     */
    List<PaySlip> findByYearMonthAndStatus(YearMonth yearMonth, SlipStatus status);

    /**
     * Checks if a pay slip exists for a given employee and month/year.
     *
     * @param employee the employee to check
     * @param yearMonth the month and year to check
     * @return true if a pay slip exists, false otherwise
     */
    boolean existsByEmployeeAndYearMonth(Employee employee, YearMonth yearMonth);
}