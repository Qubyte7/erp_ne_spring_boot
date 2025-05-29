package com.shamiinnocent.erp.Models;

import com.shamiinnocent.erp.enums.SlipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

/**
 * Entity representing a Pay Slip in the ERP system.
 * 
 * This entity stores information about an employee's salary for a specific month/year,
 * including gross salary, net salary, and various deductions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pay_slips", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "year_month"})
})
public class PaySlip {
    /**
     * Unique identifier for the pay slip
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee associated with this pay slip
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * The month and year for which this pay slip is generated
     */
    @Column(name = "year_month", nullable = false)
    private YearMonth yearMonth;

    /**
     * Housing allowance amount
     */
    @Column(name = "house_amount", nullable = false)
    private Double houseAmount;

    /**
     * Transport allowance amount
     */
    @Column(name = "transport_amount", nullable = false)
    private Double transportAmount;

    /**
     * Employee tax amount
     */
    @Column(name = "employee_tax_amount", nullable = false)
    private Double employeeTaxAmount;

    /**
     * Pension amount
     */
    @Column(name = "pension_amount", nullable = false)
    private Double pensionAmount;

    /**
     * Medical insurance amount
     */
    @Column(name = "medical_insurance_amount", nullable = false)
    private Double medicalInsuranceAmount;

    /**
     * Other tax amount
     */
    @Column(name = "other_tax_amount", nullable = false)
    private Double otherTaxAmount;

    /**
     * Gross salary (base salary + allowances)
     */
    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    /**
     * Net salary (gross salary - deductions)
     */
    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    /**
     * Status of the pay slip (pending or paid)
     */
    @Column(name = "status", nullable = false)
    private SlipStatus status = SlipStatus.PENDING;
}