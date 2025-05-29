package com.shamiinnocent.erp.Dtos.responses;

import com.shamiinnocent.erp.Models.PaySlip;
import com.shamiinnocent.erp.enums.SlipStatus;

import java.time.YearMonth;

/**
 * A DTO for returning pay slip details in the ERP system.
 */
public record PaySlipResponse(
        Long id,
        Long employeeId,
        String employeeName,
        YearMonth yearMonth,
        Double houseAmount,
        Double transportAmount,
        Double employeeTaxAmount,
        Double pensionAmount,
        Double medicalInsuranceAmount,
        Double otherTaxAmount,
        Double grossSalary,
        Double netSalary,
        SlipStatus status
) {
    /**
     * Creates a PaySlipResponse from a PaySlip entity.
     *
     * @param paySlip the pay slip entity
     * @return a PaySlipResponse
     */
    public static PaySlipResponse fromEntity(PaySlip paySlip) {
        return new PaySlipResponse(
                paySlip.getId(),
                paySlip.getEmployee().getId(),
                paySlip.getEmployee().getFullName(),
                paySlip.getYearMonth(),
                paySlip.getHouseAmount(),
                paySlip.getTransportAmount(),
                paySlip.getEmployeeTaxAmount(),
                paySlip.getPensionAmount(),
                paySlip.getMedicalInsuranceAmount(),
                paySlip.getOtherTaxAmount(),
                paySlip.getGrossSalary(),
                paySlip.getNetSalary(),
                paySlip.getStatus()
        );
    }
}