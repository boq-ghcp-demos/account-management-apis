package com.banking.accountmanagementapis.service;

import com.banking.accountmanagementapis.entity.Account;
import com.banking.accountmanagementapis.repository.AccountRepository;
import com.banking.accountmanagementapis.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final Random random = new Random();
    
    @Autowired
    private AccountRepository accountRepository;
    
    /**
     * Create a new account
     */
    public AccountResponse createAccount(CreateAccountRequest request, String customerId) {
        logger.info("Creating new account for customer: {}", customerId);
        
        // Generate unique account number
        String accountNumber = generateAccountNumber();
        
        // Ensure account number is unique
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }
        
        // Create account entity
        Account account = new Account(
            accountNumber,
            request.getAccountType(),
            request.getCurrency(),
            request.getInitialDeposit(),
            customerId,
            request.getCustomerDetails().getFirstName(),
            request.getCustomerDetails().getLastName(),
            request.getCustomerDetails().getEmail(),
            request.getCustomerDetails().getPhoneNumber(),
            request.getCustomerDetails().getAddress()
        );
        
        account.setAccountNickname(request.getAccountNickname());
        account.setBranchId("BR001"); // Default branch
        
        if (request.getMetadata() != null) {
            account.setMetadata(request.getMetadata());
        }
        
        // Save account
        Account savedAccount = accountRepository.save(account);
        
        logger.info("Account created successfully: {}", savedAccount.getAccountId());
        return mapToAccountResponse(savedAccount);
    }
    
    /**
     * Get account by ID
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(String accountId, String customerId) {
        logger.info("Getting account by ID: {} for customer: {}", accountId, customerId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));
            
        // Verify customer ownership
        if (!account.getCustomerId().equals(customerId)) {
            throw new SecurityException("Access denied: Account does not belong to customer");
        }
        
        return mapToAccountResponse(account);
    }
    
    /**
     * Get accounts for a customer
     */
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsForCustomer(String customerId, Pageable pageable) {
        logger.info("Getting accounts for customer: {} with pagination", customerId);
        
        Page<Account> accountsPage = accountRepository.findByCustomerId(customerId, pageable);
        
        return accountsPage.map(this::mapToAccountResponse);
    }
    
    /**
     * Get accounts with filters
     */
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsWithFilters(String customerId, AccountType accountType, 
                                                       AccountStatus status, String currency, 
                                                       Pageable pageable) {
        logger.info("Getting filtered accounts for customer: {}", customerId);
        
        Page<Account> accountsPage = accountRepository.findAccountsWithFilters(
            customerId, accountType, status, currency, pageable);
            
        return accountsPage.map(this::mapToAccountResponse);
    }
    
    /**
     * Update account
     */
    public AccountResponse updateAccount(String accountId, UpdateAccountRequest request, String customerId) {
        logger.info("Updating account: {} for customer: {}", accountId, customerId);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));
            
        // Verify customer ownership
        if (!account.getCustomerId().equals(customerId)) {
            throw new SecurityException("Access denied: Account does not belong to customer");
        }
        
        // Update fields
        if (request.getAccountNickname() != null) {
            account.setAccountNickname(request.getAccountNickname());
        }
        
        if (request.getMetadata() != null) {
            account.setMetadata(request.getMetadata());
        }
        
        Account updatedAccount = accountRepository.save(account);
        
        logger.info("Account updated successfully: {}", accountId);
        return mapToAccountResponse(updatedAccount);
    }
    
    /**
     * Close/Delete account
     */
    public void closeAccount(String accountId, String customerId, String reason) {
        logger.info("Closing account: {} for customer: {} with reason: {}", accountId, customerId, reason);
        
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));
            
        // Verify customer ownership
        if (!account.getCustomerId().equals(customerId)) {
            throw new SecurityException("Access denied: Account does not belong to customer");
        }
        
        // Check if account can be closed (no outstanding balance)
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance");
        }
        
        // Mark as closed instead of deleting
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        
        logger.info("Account closed successfully: {}", accountId);
    }
    
    /**
     * Generate masked account number
     */
    private String generateAccountNumber() {
        // Generate a 10-digit account number
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * Mask account number for security
     */
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
    
    /**
     * Map Account entity to AccountResponse DTO
     */
    private AccountResponse mapToAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();
        
        response.setAccountId(account.getAccountId());
        response.setAccountNumber(maskAccountNumber(account.getAccountNumber()));
        response.setAccountType(account.getAccountType());
        response.setStatus(account.getStatus());
        response.setCurrency(account.getCurrency());
        
        response.setBalance(new MonetaryAmount(account.getBalance(), account.getCurrency()));
        response.setAvailableBalance(new MonetaryAmount(account.getAvailableBalance(), account.getCurrency()));
        
        response.setAccountNickname(account.getAccountNickname());
        response.setCustomerId(account.getCustomerId());
        response.setBranchId(account.getBranchId());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        response.setLastActivityAt(account.getLastActivityAt());
        response.setMetadata(account.getMetadata());
        
        return response;
    }
    
    /**
     * Get active accounts count for customer
     */
    @Transactional(readOnly = true)
    public long getActiveAccountsCount(String customerId) {
        return accountRepository.findActiveAccountsByCustomerId(customerId).size();
    }
    
    /**
     * Check if account exists
     */
    @Transactional(readOnly = true)
    public boolean accountExists(String accountId) {
        return accountRepository.existsById(accountId);
    }
}