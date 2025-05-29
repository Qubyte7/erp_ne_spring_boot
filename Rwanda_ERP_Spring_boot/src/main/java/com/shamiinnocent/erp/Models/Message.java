package com.shamiinnocent.erp.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Entity representing a Message in the ERP system.
 * 
 * This entity stores messages that need to be sent to employees
 * when their payroll is approved.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Message {
    /**
     * Unique identifier for the message
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The employee to whom the message is addressed
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * The content of the message
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * The month and year for which the payroll message is related
     */
    @Column(name = "year_month", nullable = false)
    private YearMonth yearMonth;

    /**
     * The date and time when the message was created
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time when the message was sent
     */
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    /**
     * Status of the message (PENDING, SENT, FAILED)
     */
    @Column(name = "status", nullable = false)
    private String status = "PENDING";
}