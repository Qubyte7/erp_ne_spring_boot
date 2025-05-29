package com.shamiinnocent.erp.Models;

import com.shamiinnocent.erp.enums.Account;
import com.shamiinnocent.erp.enums.Role;
import com.shamiinnocent.erp.utils.VerificationUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing an Employee in the ERP system.
 * 
 * This entity stores personal information about employees including their
 * identification, contact details, authentication credentials, and status.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    /**
     * Unique identifier for the employee
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employee code - a unique identifier for the employee
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Employee's first name
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Employee's last name
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Employee's email address - used for login and notifications
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Employee's password - used for authentication
     */
    @Column(nullable = false)
    private String password;

    /**
     * Employee's mobile phone number
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * Employee's date of birth
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Employee's account status (active, inactive, pending)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Account status = Account.DISABLED;

    /**
     * Employee's role in the system (ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ROLE_EMPLOYEE;

    /**
     * Verification code for account activation or password reset
     */
    @Column(name = "verification_code")
    private String verificationCode;

    /**
     * Expiration time for the verification code
     */
    @Column(name = "code_expiration_time")
    private LocalDateTime codeExpirationTime;

    /**
     * Employment details for this employee
     */
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employment> employments;

    /**
     * Pay slips for this employee
     */
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaySlip> paySlips;

    /**
     * Returns the full name of the employee (first name + last name)
     * 
     * @return The employee's full name
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Checks if the verification code has expired
     *
     * @return true if the code has expired, false otherwise
     */
    public boolean isCodeExpired() {
        return VerificationUtil.isCodeExpired(codeExpirationTime);
    }
}
