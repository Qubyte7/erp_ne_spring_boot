package com.shamiinnocent.erp.services;

import com.shamiinnocent.erp.Dtos.requests.PayrollProcessRequest;
import com.shamiinnocent.erp.Dtos.responses.PaySlipResponse;
import com.shamiinnocent.erp.Models.Deduction;
import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.Employment;
import com.shamiinnocent.erp.Models.PaySlip;
import com.shamiinnocent.erp.Repositories.DeductionRepo;
import com.shamiinnocent.erp.Repositories.EmployeeRepo;
import com.shamiinnocent.erp.Repositories.EmploymentRepo;
import com.shamiinnocent.erp.Repositories.PaySlipRepo;
import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.SlipStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for processing payrolls in the ERP system.
 * Handles salary calculations, pay slip generation, and payroll approval.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollService {

    private final PaySlipRepo paySlipRepo;
    private final EmployeeRepo employeeRepo;
    private final EmploymentRepo employmentRepo;
    private final DeductionRepo deductionRepo;

    /**
     * Processes payroll for all active employees for a given month and year.
     *
     * @param request the payroll process request containing month and year
     * @return a list of generated pay slips
     */
    @Transactional
    public List<PaySlipResponse> processPayroll(PayrollProcessRequest request) {
        log.info("Processing payroll for month: {}, year: {}", request.month(), request.year());

        YearMonth yearMonth = YearMonth.of(request.year(), request.month());

        // Get all active employees
        List<Employee> activeEmployees = employeeRepo.findByStatus(Account.ACTIVE);
        log.info("Found {} active employees", activeEmployees.size());

        // Get all deductions
        List<Deduction> deductions = deductionRepo.findAll();
        Map<String, Double> deductionRates = new HashMap<>();
        for (Deduction deduction : deductions) {
            deductionRates.put(deduction.getName(), deduction.getPercentage());
        }

        // Check if required deductions exist
        if (!deductionRates.containsKey("Employee Tax") || 
            !deductionRates.containsKey("Pension") || 
            !deductionRates.containsKey("Medical Insurance") || 
            !deductionRates.containsKey("Other") || 
            !deductionRates.containsKey("Housing") || 
            !deductionRates.containsKey("Transport")) {
            throw new IllegalStateException("Required deductions are not configured in the system");
        }

        List<PaySlip> generatedPaySlips = new ArrayList<>();

        // Process payroll for each employee
        for (Employee employee : activeEmployees) {
            // Check if employee already has a pay slip for this month/year
            if (paySlipRepo.existsByEmployeeAndYearMonth(employee, yearMonth)) {
                log.warn("Employee {} already has a pay slip for {}", employee.getCode(), yearMonth);
                continue;
            }

            // Get active employment for employee
            List<Employment> activeEmployments = employmentRepo.findByEmployeeAndStatus(employee, Account.ACTIVE);
            if (activeEmployments.isEmpty()) {
                log.warn("Employee {} has no active employment", employee.getCode());
                continue;
            }

            // Use the first active employment (assuming an employee has only one active employment at a time)
            Employment employment = activeEmployments.get(0);
            Double baseSalary = employment.getBaseSalary();

            // Calculate deductions and allowances
            Double employeeTaxRate = deductionRates.get("Employee Tax") / 100.0;
            Double pensionRate = deductionRates.get("Pension") / 100.0;
            Double medicalInsuranceRate = deductionRates.get("Medical Insurance") / 100.0;
            Double otherTaxRate = deductionRates.get("Other") / 100.0;
            Double housingRate = deductionRates.get("Housing") / 100.0;
            Double transportRate = deductionRates.get("Transport") / 100.0;

            Double houseAmount = baseSalary * housingRate;
            Double transportAmount = baseSalary * transportRate;
            Double employeeTaxAmount = baseSalary * employeeTaxRate;
            Double pensionAmount = baseSalary * pensionRate;
            Double medicalInsuranceAmount = baseSalary * medicalInsuranceRate;
            Double otherTaxAmount = baseSalary * otherTaxRate;

            // Calculate gross and net salary
            Double grossSalary = baseSalary + houseAmount + transportAmount;
            Double totalDeductions = employeeTaxAmount + pensionAmount + medicalInsuranceAmount + otherTaxAmount;
            Double netSalary = grossSalary - totalDeductions;

            // Ensure net salary is not negative
            if (netSalary < 0) {
                log.warn("Net salary for employee {} is negative, skipping", employee.getCode());
                continue;
            }

            // Create pay slip
            PaySlip paySlip = PaySlip.builder()
                    .employee(employee)
                    .yearMonth(yearMonth)
                    .houseAmount(houseAmount)
                    .transportAmount(transportAmount)
                    .employeeTaxAmount(employeeTaxAmount)
                    .pensionAmount(pensionAmount)
                    .medicalInsuranceAmount(medicalInsuranceAmount)
                    .otherTaxAmount(otherTaxAmount)
                    .grossSalary(grossSalary)
                    .netSalary(netSalary)
                    .status(SlipStatus.PENDING)
                    .build();

            PaySlip savedPaySlip = paySlipRepo.save(paySlip);
            generatedPaySlips.add(savedPaySlip);
            log.info("Generated pay slip for employee {} for {}", employee.getCode(), yearMonth);
        }

        log.info("Payroll processing completed. Generated {} pay slips", generatedPaySlips.size());

        return generatedPaySlips.stream()
                .map(PaySlipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Approves all pending pay slips for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of approved pay slips
     */
    @Transactional
    public List<PaySlipResponse> approvePayroll(int month, int year) {
        log.info("Approving payroll for month: {}, year: {}", month, year);

        YearMonth yearMonth = YearMonth.of(year, month);

        // Get all pending pay slips for the given month/year
        List<PaySlip> pendingPaySlips = paySlipRepo.findByYearMonthAndStatus(yearMonth, SlipStatus.PENDING);
        log.info("Found {} pending pay slips", pendingPaySlips.size());

        List<PaySlip> approvedPaySlips = new ArrayList<>();

        // Approve each pay slip
        for (PaySlip paySlip : pendingPaySlips) {
            paySlip.setStatus(SlipStatus.PAID);
            PaySlip approvedPaySlip = paySlipRepo.save(paySlip);
            approvedPaySlips.add(approvedPaySlip);
            log.info("Approved pay slip for employee {} for {}", paySlip.getEmployee().getCode(), yearMonth);
        }

        log.info("Payroll approval completed. Approved {} pay slips", approvedPaySlips.size());

        return approvedPaySlips.stream()
                .map(PaySlipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets all pay slips for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of pay slips
     */
    public List<PaySlipResponse> getPaySlipsByMonthYear(int month, int year) {
        log.info("Getting pay slips for month: {}, year: {}", month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        List<PaySlip> paySlips = paySlipRepo.findByYearMonth(yearMonth);

        return paySlips.stream()
                .map(PaySlipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets all pay slips for the current employee.
     *
     * @return a list of pay slips for the current employee
     */
    public List<PaySlipResponse> getCurrentEmployeePaySlips() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        List<PaySlip> paySlips = paySlipRepo.findByEmployee(employee);

        return paySlips.stream()
                .map(PaySlipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Gets a pay slip for the current employee for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return the pay slip
     */
    public PaySlipResponse getCurrentEmployeePaySlipByMonthYear(int month, int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        YearMonth yearMonth = YearMonth.of(year, month);
        PaySlip paySlip = paySlipRepo.findByEmployeeAndYearMonth(employee, yearMonth)
                .orElseThrow(() -> new IllegalArgumentException("Pay slip not found for month: " + month + ", year: " + year));

        return PaySlipResponse.fromEntity(paySlip);
    }

    /**
     * Gets a pay slip by its ID.
     *
     * @param id the ID of the pay slip
     * @return the pay slip
     */
    public PaySlipResponse getPaySlipById(Long id) {
        log.info("Getting pay slip with ID: {}", id);

        PaySlip paySlip = paySlipRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pay slip with ID " + id + " not found"));

        return PaySlipResponse.fromEntity(paySlip);
    }
}