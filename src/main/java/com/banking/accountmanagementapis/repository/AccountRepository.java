package com.banking.accountmanagementapis.repository;

import com.banking.accountmanagementapis.dto.AccountStatus;
import com.banking.accountmanagementapis.dto.AccountType;
import com.banking.accountmanagementapis.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Account Repository for database operations
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    /**
     * Find accounts by customer ID
     */
    List<Account> findByCustomerId(String customerId);
    
    /**
     * Find accounts by customer ID with pagination
     */
    Page<Account> findByCustomerId(String customerId, Pageable pageable);
    
    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);
    
    /**
     * Find accounts by account type
     */
    List<Account> findByAccountType(AccountType accountType);
    
    /**
     * Find accounts by status
     */
    List<Account> findByStatus(AccountStatus status);
    
    /**
     * Find accounts by currency
     */
    List<Account> findByCurrency(String currency);
    
    /**
     * Find accounts by customer ID and account type
     */
    List<Account> findByCustomerIdAndAccountType(String customerId, AccountType accountType);
    
    /**
     * Find accounts by customer ID and status
     */
    List<Account> findByCustomerIdAndStatus(String customerId, AccountStatus status);
    
    /**
     * Find accounts created after a specific date
     */
    List<Account> findByCreatedAtAfter(LocalDateTime dateTime);
    
    /**
     * Find accounts with last activity after a specific date
     */
    List<Account> findByLastActivityAtAfter(LocalDateTime dateTime);
    
    /**
     * Custom query to find accounts with filters
     */
    @Query("SELECT a FROM Account a WHERE " +
           "(:customerId IS NULL OR a.customerId = :customerId) AND " +
           "(:accountType IS NULL OR a.accountType = :accountType) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:currency IS NULL OR a.currency = :currency)")
    Page<Account> findAccountsWithFilters(
            @Param("customerId") String customerId,
            @Param("accountType") AccountType accountType,
            @Param("status") AccountStatus status,
            @Param("currency") String currency,
            Pageable pageable);
    
    /**
     * Find active accounts for a customer
     */
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") String customerId);
    
    /**
     * Count accounts by customer ID
     */
    long countByCustomerId(String customerId);
    
    /**
     * Count accounts by status
     */
    long countByStatus(AccountStatus status);
    
    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Delete accounts by customer ID (for cleanup)
     */
    void deleteByCustomerId(String customerId);
}