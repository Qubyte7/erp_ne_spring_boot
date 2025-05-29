//package com.shamiinnocent.erp.services;
//
//import com.shamiinnocent.erp.Dtos.requests.AccountActivationRequest;
//import com.shamiinnocent.erp.Dtos.requests.LoginRequest;
//import com.shamiinnocent.erp.Dtos.requests.UserRequest;
//import com.shamiinnocent.erp.Dtos.requests.VerificationRequest;
//import com.shamiinnocent.erp.Dtos.responses.JwtTokenResponse;
//import com.shamiinnocent.erp.Dtos.responses.VerificationResponse;
//import com.shamiinnocent.erp.Models.User;
//import com.shamiinnocent.erp.Repositories.UserRepo;
//import com.shamiinnocent.erp.config.JwtConfig;
//import com.shamiinnocent.erp.enums.Account;
//import com.shamiinnocent.erp.enums.Role;
//import com.shamiinnocent.erp.exceptions.VerificationException;
//import com.shamiinnocent.erp.interfaces.UserInterface;
//import com.shamiinnocent.erp.utils.JwtUtil;
//import com.shamiinnocent.erp.utils.VerificationUtil;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
///**
// * Service for managing user registration, activation, verification, and login.
// * Handles user lifecycle with secure authentication and email notifications.
// *
// * @author Fortress Backend
// * @since 1.0
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserService implements UserInterface {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtConfig jwtConfig;
//    private final UserRepo userRepository;
//    private final EmailService emailService;
//    private final PasswordEncoder passwordEncoder;
//
//    /**
//     * Registers a new user with the provided details.
//     * Checks for duplicate email and saves the user with an encoded password.
//     *
//     * @param request the user registration request
//     * @return a success message indicating next steps
//     * @throws IllegalStateException if the email is already registered
//     */
//    @Override
//    @Transactional
//    public String register(@Valid UserRequest request) {
//        log.info("Registering user with email: {}", request.email   ());
//
//        // Check for duplicate email
//        if (userRepository.existsByEmail(request.email())) {
//            log.warn("Email {} already registered", request.email());
//            throw new IllegalStateException("Email already registered. Please use a different email or login");
//        }
//
//        // Create and save user entity
//        User user = User.builder()
//                .userName(request.userName())
//                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
//                .role(request.role())
//                .status(Account.INACTIVE)
//                .build();
//
//        userRepository.save(user);
//        log.info("Successfully registered user with email: {}", request.email());
//
//        return "Registration successful. Please activate your account using the activation API";
//    }
//
//    /**
//     * Initiates account activation by sending a verification code to the user's email.
//     *
//     * @param email the email address of the user
//     * @throws VerificationException if the user is not found
//     */
//    @Transactional
//    public void activateAccount(String email) {
//        log.info("Initiating account activation for email: {}", email);
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    return new VerificationException("No account found with email: " + email + ". Please sign up first");
//                });
//
//        // Generate verification code and expiration
//        String verificationCode = VerificationUtil.generateVerificationCode();
//        int otpExpirationMinutes = 10;
//        LocalDateTime expirationTime = VerificationUtil.getExpirationTime(otpExpirationMinutes);
//
//        // Update user with verification details and pending status
//        user.setStatus(Account.PENDING);
//        user.setVerificationCode(verificationCode);
//        user.setCodeExpirationTime(expirationTime);
//        userRepository.save(user);
//
//        // Send activation email
//        AccountActivationRequest activationRequest = new AccountActivationRequest(
//                user.getEmail(),
//                user.getFullName(),
//                verificationCode,
//                VerificationUtil.formatDateTime(expirationTime)
//        );
//
//        emailService.sendActivateAccountEmail(activationRequest);
//        log.info("Activation email sent to: {}", email);
//    }
//
//
//    /**
//     * Verifies a user's account using the provided verification code.
//     * Activates the account if the code is valid and not expired.
//     *
//     * @param request the verification request
//     * @return a success message
//     * @throws VerificationException if verification fails
//     */
//    @Transactional
//    public String verifyAccount(@Valid VerificationRequest request) {
//        log.info("Verifying account for email: {}", request.email());
//
//        User user = userRepository.findByEmail(request.email())
//                .orElseThrow(() -> {
//                    log.error("No account found with email: {}", request.email());
//                    return new VerificationException("No account found with email: " + request.email() + ". Please sign up first");
//                });
//
//        // Validate verification code
//        if (user.getVerificationCode() == null) {
//            log.warn("No verification code found for email: {}", request.email());
//            throw new VerificationException("Please request an activation code first");
//        }
//
//        if (VerificationUtil.isCodeExpired(user.getCodeExpirationTime())) {
//            log.warn("Verification code expired for email: {}", request.email());
//            emailService.sendOtpExpiredNotification(user.getEmail(), user.getFullName());
//            throw new VerificationException("Verification code has expired. Please request a new one");
//        }
//
//        if (!user.getVerificationCode().equals(request.verificationCode())) {
//            log.warn("Invalid verification code for email: {}", request.email());
//            throw new VerificationException("Invalid verification code");
//        }
//
//        // Activate account and clear verification data
//        user.setStatus(Account.ACTIVE);
//        user.setVerificationCode(null);
//        user.setCodeExpirationTime(null);
//        userRepository.save(user);
//
//        // Send confirmation email
//        VerificationResponse response = new VerificationResponse(user.getEmail(), user.getFullName());
//        emailService.sendAccountVerifiedSuccessfullyEmail(response);
//
//        log.info("Successfully verified account for email: {}", request.email());
//        return "Account verified successfully. You can now proceed to login";
//    }
//
//
//
//    /**
//     * Authenticates a user and issues a JWT token upon successful login.
//     *
//     * @param request the login request
//     * @return a JWT token response
//     * @throws IllegalStateException if authentication fails or account is inactive
//     */
//    @Override
//    public JwtTokenResponse login(@Valid LoginRequest request) {
//        log.info("Processing login for email: {}", request.email());
//
//        User user = userRepository.findByEmail(request.email())
//                .orElseThrow(() -> {
//                    log.warn("Invalid email: {}", request.email());
//                    return new IllegalStateException("Invalid email or password");
//                });
//
//        // Verify password
//        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
//            log.warn("Invalid password for email: {}", request.email());
//            throw new IllegalStateException("Invalid email or password");
//        }
//
//        // Check account status
//        if (user.getStatus() != Account.ACTIVE) {
//            log.warn("Account not verified for email: {}", request.email());
//            throw new IllegalStateException("Account not verified. Please activate your account first");
//        }
//
//        // Authenticate and generate JWT
//        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), request.password());
//        authenticationManager.authenticate(auth);
//        String token = JwtUtil.createToken(
//                "rca",
//                user.getEmail(),
//                jwtConfig.getExpiryInSeconds()
//        );
//
//        log.info("Successful login for email: {}", request.email());
//        return new JwtTokenResponse(token,user.getEmail(),user.getRole().name());
//    }
//
//
//    /**
//     * Resets a user's password using a reset code.
//     *
//     * @param email      the email address of the user
//     * @param resetCode  the reset code sent to the user
//     * @param newPassword the new password
//     * @return a success message
//     * @throws IllegalStateException if the reset code is invalid or user is not found
//     */
//    @Override
//    @Transactional
//    public String resetPassword(String email, String resetCode, String newPassword) {
//        log.info("Processing password reset for email: {}", email);
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    log.error("No account found with email: {}", email);
//                    return new IllegalStateException("No account found with email: " + email);
//                });
//
//        // Validate reset code
//        if (user.getVerificationCode() == null) {
//            log.warn("No reset code found for email: {}", email);
//            throw new IllegalStateException("Please request a password reset code first");
//        }
//
//        if (VerificationUtil.isCodeExpired(user.getCodeExpirationTime())) {
//            log.warn("Reset code expired for email: {}", email);
//            throw new IllegalStateException("Reset code has expired. Please request a new one");
//        }
//
//        if (!user.getVerificationCode().equals(resetCode)) {
//            log.warn("Invalid reset code for email: {}", email);
//            throw new IllegalStateException("Invalid reset code");
//        }
//
//        // Update password and clear verification data
//        user.setPassword(passwordEncoder.encode(newPassword));
//        user.setVerificationCode(null);
//        user.setCodeExpirationTime(null);
//        userRepository.save(user);
//
//        log.info("Successfully reset password for email: {}", email);
//        return "Password reset successfully. You can now login with your new password";
//    }
//}
