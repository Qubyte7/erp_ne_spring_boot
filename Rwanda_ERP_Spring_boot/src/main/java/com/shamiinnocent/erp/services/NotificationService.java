package com.shamiinnocent.erp.services;

import com.shamiinnocent.erp.Models.Employee;
import com.shamiinnocent.erp.Models.Message;
import com.shamiinnocent.erp.Models.PaySlip;
import com.shamiinnocent.erp.Repositories.MessageRepo;
import com.shamiinnocent.erp.Repositories.PaySlipRepo;
import com.shamiinnocent.erp.enums.SlipStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for managing notifications in the ERP system.
 * Handles creating and sending notifications to employees when their payroll is approved.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MessageRepo messageRepo;
    private final PaySlipRepo paySlipRepo;
    private final EmailService emailService;

    /**
     * Creates and sends notifications for approved payrolls.
     * This method is called when payrolls are approved to notify employees.
     *
     * @param month the month of the approved payrolls
     * @param year the year of the approved payrolls
     */
    @Transactional
    public void notifyEmployeesForApprovedPayrolls(int month, int year) {
        log.info("Creating notifications for approved payrolls for month: {}, year: {}", month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        List<PaySlip> approvedPaySlips = paySlipRepo.findByYearMonthAndStatus(yearMonth, SlipStatus.PAID);
        log.info("Found {} approved pay slips", approvedPaySlips.size());

        for (PaySlip paySlip : approvedPaySlips) {
            Employee employee = paySlip.getEmployee();
            String messageContent = createPayrollNotificationMessage(employee, paySlip, yearMonth);

            // Create message in database
            Message message = Message.builder()
                    .employee(employee)
                    .content(messageContent)
                    .yearMonth(yearMonth)
                    .createdAt(LocalDateTime.now())
                    .status("PENDING")
                    .build();

            Message savedMessage = messageRepo.save(message);
            log.info("Created notification message for employee {} for {}", employee.getCode(), yearMonth);

            // Send email notification
            try {
                emailService.sendEmail(
                        employee.getEmail(),
                        "Payroll Notification - " + yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        messageContent
                );
                savedMessage.setStatus("SENT");
                savedMessage.setSentAt(LocalDateTime.now());
                messageRepo.save(savedMessage);
                log.info("Sent email notification to employee {} for {}", employee.getCode(), yearMonth);
            } catch (Exception e) {
                savedMessage.setStatus("FAILED");
                messageRepo.save(savedMessage);
                log.error("Failed to send email notification to employee {} for {}: {}", 
                        employee.getCode(), yearMonth, e.getMessage(), e);
            }
        }

        log.info("Notification process completed for month: {}, year: {}", month, year);
    }

    /**
     * Creates a notification message for an approved payroll.
     *
     * @param employee the employee to notify
     * @param paySlip the approved pay slip
     * @param yearMonth the month and year of the payroll
     * @return the notification message
     */
    private String createPayrollNotificationMessage(Employee employee, PaySlip paySlip, YearMonth yearMonth) {
        String monthYear = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        String institution = "ERP System"; // This could be configurable
        String amount = String.format("%.2f", paySlip.getNetSalary());

        return String.format(
                "Dear %s, your Salary of %s from %s (Amount: %s) has been credited to your %s account successfully.",
                employee.getFirstName(),
                monthYear,
                institution,
                amount,
                employee.getCode()
        );
    }

    /**
     * Resends failed notifications.
     * This method can be scheduled to run periodically to retry sending failed notifications.
     */
    @Transactional
    public void resendFailedNotifications() {
        log.info("Resending failed notifications");

        List<Message> failedMessages = messageRepo.findByStatus("FAILED");
        log.info("Found {} failed messages", failedMessages.size());

        for (Message message : failedMessages) {
            Employee employee = message.getEmployee();
            YearMonth yearMonth = message.getYearMonth();

            try {
                emailService.sendEmail(
                        employee.getEmail(),
                        "Payroll Notification - " + yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        message.getContent()
                );
                message.setStatus("SENT");
                message.setSentAt(LocalDateTime.now());
                messageRepo.save(message);
                log.info("Successfully resent notification to employee {} for {}", employee.getCode(), yearMonth);
            } catch (Exception e) {
                log.error("Failed to resend notification to employee {} for {}: {}", 
                        employee.getCode(), yearMonth, e.getMessage(), e);
            }
        }

        log.info("Resending failed notifications completed");
    }

    /**
     * Gets all messages for the given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of messages for the employee
     */
    public List<Message> getMessagesByEmployee(Long employeeId) {
        log.info("Getting messages for employee with ID: {}", employeeId);
        
        Employee employee = new Employee();
        employee.setId(employeeId);
        
        return messageRepo.findByEmployee(employee);
    }

    /**
     * Gets all messages for a given month and year.
     *
     * @param month the month
     * @param year the year
     * @return a list of messages for the given month and year
     */
    public List<Message> getMessagesByMonthYear(int month, int year) {
        log.info("Getting messages for month: {}, year: {}", month, year);
        
        YearMonth yearMonth = YearMonth.of(year, month);
        return messageRepo.findByYearMonth(yearMonth);
    }
}