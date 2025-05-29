package com.shamiinnocent.erp.enums;

/**
 * Enum representing the different roles in the ERP system.
 * 
 * ROLE_ADMIN: Can approve salary
 * ROLE_MANAGER: Can process salary and add Employee details
 * ROLE_EMPLOYEE: Can view their details, download slip, and view pending salary payment
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_MANAGER,
    ROLE_EMPLOYEE
}
