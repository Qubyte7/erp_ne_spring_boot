package com.shamiinnocent.erp.Dtos.responses;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.Role;

import java.time.LocalDate;

/**
 * A DTO for returning employee details in the ERP system.
 */
public record EmployeeResponse(
        Long id,
        String code,
        String firstName,
        String lastName,
        String email,
        String mobile,
        LocalDate dateOfBirth,
        Account status,
        Role role
) {
    /**
     * Creates an EmployeeResponse from an Employee entity.
     *
     * @param employee the employee entity
     * @return an EmployeeResponse
     */
    public static EmployeeResponse fromEntity(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getMobile(),
                employee.getDateOfBirth(),
                employee.getStatus(),
                employee.getRole()
        );
    }
}