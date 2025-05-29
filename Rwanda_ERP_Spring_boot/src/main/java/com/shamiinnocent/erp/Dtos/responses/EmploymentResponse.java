package com.shamiinnocent.erp.Dtos.responses;

import com.shamiinnocent.erp.Models.Employment;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.EmployementStatus;

import java.time.LocalDate;

/**
 * A DTO for returning employment details in the ERP system.
 */
public record EmploymentResponse(
        Long id,
        String code,
        Long employeeId,
        String employeeName,
        String department,
        String position,
        Double baseSalary,
        LocalDate joiningDate,
        EmployementStatus status
) {
    /**
     * Creates an EmploymentResponse from an Employment entity.
     *
     * @param employment the employment entity
     * @return an EmploymentResponse
     */
    public static EmploymentResponse fromEntity(Employment employment) {
        return new EmploymentResponse(
                employment.getId(),
                employment.getCode(),
                employment.getEmployee().getId(),
                employment.getEmployee().getFullName(),
                employment.getDepartment(),
                employment.getPosition(),
                employment.getBaseSalary(),
                employment.getJoiningDate(),
                employment.getStatus()
        );
    }
}