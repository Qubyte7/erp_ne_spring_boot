package com.shamiinnocent.erp.Dtos.responses;

import com.shamiinnocent.erp.Models.Deduction;

/**
 * A DTO for returning deduction details in the ERP system.
 */
public record DeductionResponse(
        Long id,
        String code,
        String name,
        Double percentage

) {
    /**
     * Creates a DeductionResponse from a Deduction entity.
     *
     * @param deduction the deduction entity
     * @return a DeductionResponse
     */
    public static DeductionResponse fromEntity(Deduction deduction) {
        return new DeductionResponse(
                deduction.getId(),
                deduction.getCode(),
                deduction.getName(),
                deduction.getPercentage()

        );
    }
}