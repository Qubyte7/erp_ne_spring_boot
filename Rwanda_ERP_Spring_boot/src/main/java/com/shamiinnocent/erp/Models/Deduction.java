package com.shamiinnocent.erp.Models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a Deduction type in the ERP system.
 * 
 * This entity stores information about different types of deductions
 * that can be applied to an employee's salary, such as taxes, pension,
 * medical insurance, etc.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deductions")
public class Deduction {
    /**
     * Unique identifier for the deduction
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Deduction code - a unique identifier for the deduction
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Name of the deduction (e.g., "Employee Tax", "Pension", "Medical Insurance")
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Percentage rate of the deduction (e.g., 30.0 for 30%)
     */
    @Column(name = "percentage", nullable = false)
    private Double percentage;
//
//    /**
//     * Description of the deduction
//     */
//    @Column(name = "description")
//    private String description;
}