package com.banking.accountmanagementapis.dto;

/**
 * Account Status enumeration based on API specification
 */
public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    FROZEN,
    CLOSED,
    PENDING_APPROVAL,
    SUSPENDED
}