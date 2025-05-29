//package com.shamiinnocent.erp.Models;
//
//
//import com.shamiinnocent.erp.enums.Account;
//import com.shamiinnocent.erp.enums.Role;
//import com.shamiinnocent.erp.utils.VerificationUtil;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///**
// * Entity class representing a user in the system.
// * <p>
// * This class encapsulates all user information including authentication details,
// * personal information, and account status. Each user can be associated with an owner
// * profile for vehicle management operations.
// * </p>
// * <p>
// * Includes verification mechanisms for email/phone verification through OTP.
// * </p>
// *
// * @author Fortress Backend
// * @version 1.0
// * @since 1.0
// */
//@Data
//@Entity
//@Table(name = "users")
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class User {
//    /**
//     * The unique identifier for the user.
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    /**
//     * The user's first name.
//     */
//    @Column(name = "user_name", nullable = false)
//    private String userName;
//
//    /**
//     * The user's email address. Must be unique and is used for authentication.
//     */
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;
//
//    /**
//     * The user's hashed password.
//     */
//    @Column(name = "password", nullable = false)
//    private String password;
//
//
//    /**
//     * The current status of the user account.
//     * Default status is PENDING until verification is complete.
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private Account status = Account.INACTIVE;
//
//    /**
//     * The role assigned to the user for authorization purposes.
//     * Default role is USER.
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role", nullable = false)
//    private Role role ;
//
//    /**
//     * The verification code sent to the user for account verification.
//     */
//    @Column(name = "verification_code")
//    private String verificationCode;
//
//    /**
//     * The expiration time for the verification code.
//     */
//    @Column(name = "code_expiration_time")
//    private LocalDateTime codeExpirationTime;
//
//    /**
//     * Returns the full name of the user by concatenating first and last name.
//     *
//     * @return the full name of the user
//     */
//    public String getFullName() {
//        return this.userName;
//    }
//
//    /**
//     * Checks if the verification code has expired.
//     *
//     * @return true if the code has expired, false otherwise
//     */
//    public boolean isCodeExpired() {
//        return VerificationUtil.isCodeExpired(codeExpirationTime);
//    }
//}