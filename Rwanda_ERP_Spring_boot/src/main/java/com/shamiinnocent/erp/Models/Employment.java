package com.shamiinnocent.erp.Models;

import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.EmployementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing an Employment record in the ERP system.
 * 
 * This entity stores professional employment details for employees including
 * department, position, base salary, and employment status.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employments")
public class Employment {
    /**
     * Unique identifier for the employment record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employment code - a unique identifier for the employment record
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * The employee associated with this employment record
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * Department where the employee works
     */
    @Column(name = "department", nullable = false)
    private String department;

    /**
     * Position or job title of the employee
     */
    @Column(name = "position", nullable = false)
    private String position;

    /**
     * Base salary for the employee
     * This is used to calculate deductions and gross salary
     */
    @Column(name = "base_salary", nullable = false)
    private Double baseSalary;

    /**
     * Date when the employee joined the company
     */
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    /**
     * Employment status (active or inactive)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployementStatus status = EmployementStatus.ACTIVE;
}