package com.shamiinnocent.erp.Repositories;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

/**
 * Repository for managing Message entities.
 */
@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    /**
     * Finds all messages for a given employee.
     *
     * @param employee the employee to search for
     * @return a list of messages for the employee
     */
    List<Message> findByEmployee(Employee employee);

    /**
     * Finds all messages for a given month and year.
     *
     * @param yearMonth the month and year to search for
     * @return a list of messages for the given month and year
     */
    List<Message> findByYearMonth(YearMonth yearMonth);

    /**
     * Finds all messages for a given employee and month/year.
     *
     * @param employee the employee to search for
     * @param yearMonth the month and year to search for
     * @return a list of messages for the given employee and month/year
     */
    List<Message> findByEmployeeAndYearMonth(Employee employee, YearMonth yearMonth);

    /**
     * Finds all messages with a given status.
     *
     * @param status the status to search for (e.g., "PENDING", "SENT", or "FAILED")
     * @return a list of messages with the given status
     */
    List<Message> findByStatus(String status);

    /**
     * this is the list of messages
     * Finds all messages for a given employee with a given status.
     *
     * @param employee the employee to search for
     * @param status the status to search for
     * @return a list of messages for the given employee with the given status
     */
    List<Message> findByEmployeeAndStatus(Employee employee, String status);
}