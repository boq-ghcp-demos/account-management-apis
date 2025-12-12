package com.banking.accountmanagementapis.controller;

import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple Banking Account Controller for initial testing.
 * This is a minimal version to get the application running.
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "Banking Account Management API endpoints")
public class SimpleAccountController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAccountController.class);
    private final AccountService accountService;
    
    @Autowired
    public SimpleAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Health check endpoint
     */
    @Operation(summary = "Health Check", description = "Check if the Banking Account Management API is running")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API is healthy and running")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Banking Account Management API is running");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * List accounts endpoint with filtering and pagination
     */
    @Operation(summary = "List Accounts", description = "Retrieve a paginated list of customer accounts with filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved account list"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> listAccounts(
            @Parameter(description = "Customer ID for filtering accounts") @RequestHeader(value = "X-Customer-ID") String customerId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "Filter by account type") @RequestParam(required = false) AccountType accountType,
            @Parameter(description = "Filter by account status") @RequestParam(required = false) AccountStatus status,
            @Parameter(description = "Filter by currency") @RequestParam(required = false) String currency,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("List accounts request - CustomerId: {}, RequestId: {}", customerId, requestId);
        
        try {
            // Create pageable with sorting
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            // Get accounts with filters
            Page<AccountResponse> accountsPage = accountService.getAccountsWithFilters(
                customerId, accountType, status, currency, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("accounts", accountsPage.getContent());
            response.put("totalElements", accountsPage.getTotalElements());
            response.put("totalPages", accountsPage.getTotalPages());
            response.put("currentPage", accountsPage.getNumber());
            response.put("size", accountsPage.getSize());
            response.put("hasNext", accountsPage.hasNext());
            response.put("hasPrevious", accountsPage.hasPrevious());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error listing accounts for customer: {}", customerId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve accounts");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get account details endpoint
     */
    @Operation(summary = "Get Account Details", description = "Retrieve detailed information for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved account details"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(
            @Parameter(description = "Unique account identifier", required = true) @PathVariable String accountId,
            @Parameter(description = "Customer ID for authorization") @RequestHeader(value = "X-Customer-ID") String customerId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId) {
        
        logger.info("Get account details - AccountId: {}, CustomerId: {}, RequestId: {}", 
                   accountId, customerId, requestId);
        
        try {
            AccountResponse account = accountService.getAccountById(accountId, customerId);
            return ResponseEntity.ok(account);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            logger.warn("Account not found: {}", accountId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account not found");
            errorResponse.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (SecurityException e) {
            logger.warn("Access denied for account: {} by customer: {}", accountId, customerId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Access denied");
            errorResponse.put("message", "Account does not belong to customer");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error retrieving account: {}", accountId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve account");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Create new account endpoint
     */
    @Operation(summary = "Create New Account", description = "Create a new banking account for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    public ResponseEntity<?> createAccount(
            @Parameter(description = "Customer ID for account creation") @RequestHeader(value = "X-Customer-ID") String customerId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "Account creation request data") @Valid @RequestBody CreateAccountRequest request) {
        
        logger.info("Create account request - CustomerId: {}, RequestId: {}", customerId, requestId);
        
        try {
            AccountResponse account = accountService.createAccount(request, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request data: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request data");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error creating account for customer: {}", customerId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create account");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Update account endpoint
     */
    @Operation(summary = "Update Account", description = "Update account information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(
            @Parameter(description = "Account ID to update") @PathVariable String accountId,
            @Parameter(description = "Customer ID for authorization") @RequestHeader(value = "X-Customer-ID") String customerId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "Account update request data") @Valid @RequestBody UpdateAccountRequest request) {
        
        logger.info("Update account request - AccountId: {}, CustomerId: {}, RequestId: {}", 
                   accountId, customerId, requestId);
        
        try {
            AccountResponse account = accountService.updateAccount(accountId, request, customerId);
            return ResponseEntity.ok(account);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            logger.warn("Account not found: {}", accountId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account not found");
            errorResponse.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (SecurityException e) {
            logger.warn("Access denied for account: {} by customer: {}", accountId, customerId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Access denied");
            errorResponse.put("message", "Account does not belong to customer");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error updating account: {}", accountId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update account");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Close account endpoint
     */
    @Operation(summary = "Close Account", description = "Close/Delete an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account closed successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "409", description = "Account cannot be closed")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> closeAccount(
            @Parameter(description = "Account ID to close") @PathVariable String accountId,
            @Parameter(description = "Customer ID for authorization") @RequestHeader(value = "X-Customer-ID") String customerId,
            @Parameter(description = "Request ID for tracing") @RequestHeader(value = "X-Request-ID", required = false) String requestId,
            @Parameter(description = "Reason for closure") @RequestParam(required = false, defaultValue = "CUSTOMER_REQUEST") String reason) {
        
        logger.info("Close account request - AccountId: {}, CustomerId: {}, RequestId: {}, Reason: {}", 
                   accountId, customerId, requestId, reason);
        
        try {
            accountService.closeAccount(accountId, customerId, reason);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            logger.warn("Account not found: {}", accountId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account not found");
            errorResponse.put("accountId", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (SecurityException e) {
            logger.warn("Access denied for account: {} by customer: {}", accountId, customerId);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Access denied");
            errorResponse.put("message", "Account does not belong to customer");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        } catch (IllegalStateException e) {
            logger.warn("Account cannot be closed: {} - {}", accountId, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account cannot be closed");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error closing account: {}", accountId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to close account");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}