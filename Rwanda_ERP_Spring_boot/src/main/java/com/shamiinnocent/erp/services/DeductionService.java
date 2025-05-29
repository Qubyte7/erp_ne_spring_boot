package com.shamiinnocent.erp.services;

import com.shamiinnocent.erp.Dtos.requests.DeductionRequest;
import com.shamiinnocent.erp.Dtos.responses.DeductionResponse;
import com.shamiinnocent.erp.Models.Deduction;
import com.shamiinnocent.erp.Repositories.DeductionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing deductions in the ERP system.
 * Handles CRUD operations for deductions and ensures that tax rates are properly maintained.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeductionService {

    private final DeductionRepo deductionRepo;

    /**
     * Creates a new deduction in the system.
     *
     * @param request the deduction details
     * @return the created deduction
     */
    @Transactional
    public DeductionResponse createDeduction(DeductionRequest request) {
        log.info("Creating deduction with code: {}", request.code());

        // Check if deduction with the same code or name already exists
        if (deductionRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Deduction with code " + request.code() + " already exists");
        }
        if (deductionRepo.existsByName(request.name())) {
            throw new IllegalArgumentException("Deduction with name " + request.name() + " already exists");
        }

        // Create new deduction
        Deduction deduction = Deduction.builder()
                .code(request.code())
                .name(request.name())
                .percentage(request.percentage())
                .build();

        // Save deduction
        Deduction savedDeduction = deductionRepo.save(deduction);
        log.info("Deduction created with ID: {}", savedDeduction.getId());

        return DeductionResponse.fromEntity(savedDeduction);
    }

    /**
     * Updates an existing deduction in the system.
     *
     * @param id      the ID of the deduction to update
     * @param request the updated deduction details
     * @return the updated deduction
     */
    @Transactional
    public DeductionResponse updateDeduction(Long id, DeductionRequest request) {
        log.info("Updating deduction with ID: {}", id);

        // Find deduction
        Deduction deduction = deductionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with ID " + id + " not found"));

        // Check if code or name is being changed and if it conflicts with existing deductions
        if (!deduction.getCode().equals(request.code()) && deductionRepo.existsByCode(request.code())) {
            throw new IllegalArgumentException("Deduction with code " + request.code() + " already exists");
        }
        if (!deduction.getName().equals(request.name()) && deductionRepo.existsByName(request.name())) {
            throw new IllegalArgumentException("Deduction with name " + request.name() + " already exists");
        }

        // Update deduction
        deduction.setCode(request.code());
        deduction.setName(request.name());
        deduction.setPercentage(request.percentage());


        // Save deduction
        Deduction updatedDeduction = deductionRepo.save(deduction);
        log.info("Deduction updated with ID: {}", updatedDeduction.getId());

        return DeductionResponse.fromEntity(updatedDeduction);
    }

    /**
     * Gets a deduction by its ID.
     *
     * @param id the ID of the deduction to get
     * @return the deduction
     */
    public DeductionResponse getDeductionById(Long id) {
        log.info("Getting deduction with ID: {}", id);

        Deduction deduction = deductionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with ID " + id + " not found"));

        return DeductionResponse.fromEntity(deduction);
    }

    /**
     * Gets a deduction by its code.
     *
     * @param code the code of the deduction to get
     * @return the deduction
     */
    public DeductionResponse getDeductionByCode(String code) {
        log.info("Getting deduction with code: {}", code);

        Deduction deduction = deductionRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with code " + code + " not found"));

        return DeductionResponse.fromEntity(deduction);
    }

    /**
     * Gets a deduction by its name.
     *
     * @param name the name of the deduction to get
     * @return the deduction
     */
    public DeductionResponse getDeductionByName(String name) {
        log.info("Getting deduction with name: {}", name);

        Deduction deduction = deductionRepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with name " + name + " not found"));

        return DeductionResponse.fromEntity(deduction);
    }

    /**
     * Gets all deductions in the system.
     *
     * @return a list of all deductions
     */
    public List<DeductionResponse> getAllDeductions() {
        log.info("Getting all deductions");

        return deductionRepo.findAll().stream()
                .map(DeductionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a deduction from the system.
     *
     * @param id the ID of the deduction to delete
     */
    @Transactional
    public void deleteDeduction(Long id) {
        log.info("Deleting deduction with ID: {}", id);

        if (!deductionRepo.existsById(id)) {
            throw new IllegalArgumentException("Deduction with ID " + id + " not found");
        }

        deductionRepo.deleteById(id);
        log.info("Deduction deleted with ID: {}", id);
    }

    /**
     * Initializes default deductions if they don't exist.
     * This method is called during application startup to ensure that
     * the required deductions are available in the system.
     */
    @Transactional
    public void initializeDefaultDeductions() {
        log.info("Initializing default deductions");

        // Employee Tax (30%)
        if (!deductionRepo.existsByName("Employee Tax")) {
            Deduction employeeTax = Deduction.builder()
                    .code("EMP_TAX")
                    .name("Employee Tax")
                    .percentage(30.0)
                    .build();
            deductionRepo.save(employeeTax);
            log.info("Created default Employee Tax deduction");
        }

        // Pension (6%)
        if (!deductionRepo.existsByName("Pension")) {
            Deduction pension = Deduction.builder()
                    .code("PENSION")
                    .name("Pension")
                    .percentage(6.0)
                    .build();
            deductionRepo.save(pension);
            log.info("Created default Pension deduction");
        }

        // Medical Insurance (5%)
        if (!deductionRepo.existsByName("Medical Insurance")) {
            Deduction medicalInsurance = Deduction.builder()
                    .code("MED_INS")
                    .name("Medical Insurance")
                    .percentage(5.0)
                    .build();
            deductionRepo.save(medicalInsurance);
            log.info("Created default Medical Insurance deduction");
        }

        // Other (5%)
        if (!deductionRepo.existsByName("Other")) {
            Deduction other = Deduction.builder()
                    .code("OTHER")
                    .name("Other")
                    .percentage(5.0)
                    .build();
            deductionRepo.save(other);
            log.info("Created default Other deduction");
        }

        // Housing (14%)
        if (!deductionRepo.existsByName("Housing")) {
            Deduction housing = Deduction.builder()
                    .code("HOUSING")
                    .name("Housing")
                    .percentage(14.0)
                    .build();
            deductionRepo.save(housing);
            log.info("Created default Housing allowance");
        }

        // Transport (14%)
        if (!deductionRepo.existsByName("Transport")) {
            Deduction transport = Deduction.builder()
                    .code("TRANSPORT")
                    .name("Transport")
                    .percentage(14.0)
                    .build();
            deductionRepo.save(transport);
            log.info("Created default Transport allowance");
        }

        log.info("Default deductions initialization completed");
    }
}