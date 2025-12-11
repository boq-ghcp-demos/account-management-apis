package com.banking.accountmanagementapis.repository;

import com.banking.accountmanagementapis.dto.AccountStatus;
import com.banking.accountmanagementapis.dto.AccountType;
import com.banking.accountmanagementapis.entity.Account;
import com.banking.accountmanagementapis.testutil.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Repository Tests for AccountRepository
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Account Repository Tests")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private String testCustomerId;
    private String anotherCustomerId;
    private Account testAccount1;
    private Account testAccount2;
    private Account testAccount3;

    @BeforeEach
    void setUp() {
        testCustomerId = TestDataGenerator.generateUUID();
        anotherCustomerId = TestDataGenerator.generateUUID();
        
        // Clean database
        accountRepository.deleteAll();
        
        // Create test data
        testAccount1 = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        testAccount1.setAccountType(AccountType.CHECKING);
        testAccount1.setStatus(AccountStatus.ACTIVE);
        testAccount1.setCurrency("USD");
        
        testAccount2 = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        testAccount2.setAccountType(AccountType.SAVINGS);
        testAccount2.setStatus(AccountStatus.ACTIVE);
        testAccount2.setCurrency("EUR");
        
        testAccount3 = TestDataGenerator.generateAccountEntity(anotherCustomerId, null);
        testAccount3.setAccountType(AccountType.CHECKING);
        testAccount3.setStatus(AccountStatus.INACTIVE);
        testAccount3.setCurrency("USD");
        
        accountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
    }

    @Test
    @DisplayName("Should find accounts by customer ID")
    void findByCustomerId_WithValidCustomerId_ShouldReturnCustomerAccounts() {
        // When
        List<Account> accounts = accountRepository.findByCustomerId(testCustomerId);

        // Then
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().allMatch(account -> account.getCustomerId().equals(testCustomerId)));
    }

    @Test
    @DisplayName("Should find accounts by customer ID with pagination")
    void findByCustomerId_WithPagination_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Account> accountPage = accountRepository.findByCustomerId(testCustomerId, pageable);

        // Then
        assertEquals(1, accountPage.getContent().size());
        assertEquals(2, accountPage.getTotalElements());
        assertEquals(2, accountPage.getTotalPages());
        assertTrue(accountPage.hasNext());
    }

    @Test
    @DisplayName("Should find account by account number")
    void findByAccountNumber_WithValidAccountNumber_ShouldReturnAccount() {
        // Given
        String accountNumber = testAccount1.getAccountNumber();

        // When
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(accountNumber);

        // Then
        assertTrue(foundAccount.isPresent());
        assertEquals(testAccount1.getAccountId(), foundAccount.get().getAccountId());
        assertEquals(accountNumber, foundAccount.get().getAccountNumber());
    }

    @Test
    @DisplayName("Should return empty when account number not found")
    void findByAccountNumber_WithInvalidAccountNumber_ShouldReturnEmpty() {
        // Given
        String nonExistentAccountNumber = "9999999999";

        // When
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(nonExistentAccountNumber);

        // Then
        assertTrue(foundAccount.isEmpty());
    }

    @Test
    @DisplayName("Should find accounts by account type")
    void findByAccountType_WithValidType_ShouldReturnMatchingAccounts() {
        // When
        List<Account> checkingAccounts = accountRepository.findByAccountType(AccountType.CHECKING);

        // Then
        assertEquals(2, checkingAccounts.size());
        assertTrue(checkingAccounts.stream().allMatch(account -> account.getAccountType() == AccountType.CHECKING));
    }

    @Test
    @DisplayName("Should find accounts by status")
    void findByStatus_WithValidStatus_ShouldReturnMatchingAccounts() {
        // When
        List<Account> activeAccounts = accountRepository.findByStatus(AccountStatus.ACTIVE);

        // Then
        assertEquals(2, activeAccounts.size());
        assertTrue(activeAccounts.stream().allMatch(account -> account.getStatus() == AccountStatus.ACTIVE));
    }

    @Test
    @DisplayName("Should find accounts by currency")
    void findByCurrency_WithValidCurrency_ShouldReturnMatchingAccounts() {
        // When
        List<Account> usdAccounts = accountRepository.findByCurrency("USD");

        // Then
        assertEquals(2, usdAccounts.size());
        assertTrue(usdAccounts.stream().allMatch(account -> "USD".equals(account.getCurrency())));
    }

    @Test
    @DisplayName("Should find accounts by customer ID and account type")
    void findByCustomerIdAndAccountType_WithValidFilters_ShouldReturnMatchingAccounts() {
        // When
        List<Account> accounts = accountRepository.findByCustomerIdAndAccountType(testCustomerId, AccountType.CHECKING);

        // Then
        assertEquals(1, accounts.size());
        assertEquals(testAccount1.getAccountId(), accounts.get(0).getAccountId());
        assertEquals(AccountType.CHECKING, accounts.get(0).getAccountType());
        assertEquals(testCustomerId, accounts.get(0).getCustomerId());
    }

    @Test
    @DisplayName("Should find accounts by customer ID and status")
    void findByCustomerIdAndStatus_WithValidFilters_ShouldReturnMatchingAccounts() {
        // When
        List<Account> accounts = accountRepository.findByCustomerIdAndStatus(testCustomerId, AccountStatus.ACTIVE);

        // Then
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().allMatch(account -> 
            account.getCustomerId().equals(testCustomerId) && account.getStatus() == AccountStatus.ACTIVE));
    }

    @Test
    @DisplayName("Should find accounts created after specified date")
    void findByCreatedAtAfter_WithValidDate_ShouldReturnRecentAccounts() {
        // Given
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(1);

        // When
        List<Account> recentAccounts = accountRepository.findByCreatedAtAfter(cutoffDate);

        // Then
        assertEquals(3, recentAccounts.size());
        assertTrue(recentAccounts.stream().allMatch(account -> account.getCreatedAt().isAfter(cutoffDate)));
    }

    @Test
    @DisplayName("Should find accounts with last activity after specified date")
    void findByLastActivityAtAfter_WithValidDate_ShouldReturnActiveAccounts() {
        // Given
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(1);

        // When
        List<Account> activeAccounts = accountRepository.findByLastActivityAtAfter(cutoffDate);

        // Then
        assertEquals(3, activeAccounts.size());
        assertTrue(activeAccounts.stream().allMatch(account -> account.getLastActivityAt().isAfter(cutoffDate)));
    }

    @Test
    @DisplayName("Should find accounts with filters using custom query")
    void findAccountsWithFilters_WithMultipleFilters_ShouldReturnMatchingAccounts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // Test 1: Filter by customer ID only
        Page<Account> customerAccounts = accountRepository.findAccountsWithFilters(
            testCustomerId, null, null, null, pageable);
        assertEquals(2, customerAccounts.getTotalElements());

        // Test 2: Filter by customer ID and account type
        Page<Account> customerCheckingAccounts = accountRepository.findAccountsWithFilters(
            testCustomerId, AccountType.CHECKING, null, null, pageable);
        assertEquals(1, customerCheckingAccounts.getTotalElements());

        // Test 3: Filter by customer ID, account type, and status
        Page<Account> customerActiveCheckingAccounts = accountRepository.findAccountsWithFilters(
            testCustomerId, AccountType.CHECKING, AccountStatus.ACTIVE, null, pageable);
        assertEquals(1, customerActiveCheckingAccounts.getTotalElements());

        // Test 4: Filter by all parameters
        Page<Account> fullFilterAccounts = accountRepository.findAccountsWithFilters(
            testCustomerId, AccountType.CHECKING, AccountStatus.ACTIVE, "USD", pageable);
        assertEquals(1, fullFilterAccounts.getTotalElements());

        // Test 5: No filters (null customer ID)
        Page<Account> allAccounts = accountRepository.findAccountsWithFilters(
            null, null, null, null, pageable);
        assertEquals(3, allAccounts.getTotalElements());
    }

    @Test
    @DisplayName("Should find active accounts for customer using custom query")
    void findActiveAccountsByCustomerId_WithValidCustomerId_ShouldReturnActiveAccounts() {
        // When
        List<Account> activeAccounts = accountRepository.findActiveAccountsByCustomerId(testCustomerId);

        // Then
        assertEquals(2, activeAccounts.size());
        assertTrue(activeAccounts.stream().allMatch(account -> 
            account.getCustomerId().equals(testCustomerId) && account.getStatus() == AccountStatus.ACTIVE));
    }

    @Test
    @DisplayName("Should count accounts by customer ID")
    void countByCustomerId_WithValidCustomerId_ShouldReturnCorrectCount() {
        // When
        long count = accountRepository.countByCustomerId(testCustomerId);

        // Then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should count accounts by status")
    void countByStatus_WithValidStatus_ShouldReturnCorrectCount() {
        // When
        long activeCount = accountRepository.countByStatus(AccountStatus.ACTIVE);
        long inactiveCount = accountRepository.countByStatus(AccountStatus.INACTIVE);

        // Then
        assertEquals(2, activeCount);
        assertEquals(1, inactiveCount);
    }

    @Test
    @DisplayName("Should check if account number exists")
    void existsByAccountNumber_WithValidAccountNumber_ShouldReturnTrue() {
        // Given
        String existingAccountNumber = testAccount1.getAccountNumber();
        String nonExistingAccountNumber = "9999999999";

        // When & Then
        assertTrue(accountRepository.existsByAccountNumber(existingAccountNumber));
        assertFalse(accountRepository.existsByAccountNumber(nonExistingAccountNumber));
    }

    @Test
    @DisplayName("Should delete accounts by customer ID")
    void deleteByCustomerId_WithValidCustomerId_ShouldDeleteAllCustomerAccounts() {
        // Given
        assertEquals(2, accountRepository.countByCustomerId(testCustomerId));
        assertEquals(1, accountRepository.countByCustomerId(anotherCustomerId));

        // When
        accountRepository.deleteByCustomerId(testCustomerId);

        // Then
        assertEquals(0, accountRepository.countByCustomerId(testCustomerId));
        assertEquals(1, accountRepository.countByCustomerId(anotherCustomerId));
        assertEquals(1, accountRepository.count()); // Only one account should remain
    }

    @Test
    @DisplayName("Should handle empty results gracefully")
    void repositoryMethods_WithNoMatchingData_ShouldReturnEmptyResults() {
        // Given
        String nonExistentCustomerId = TestDataGenerator.generateUUID();

        // When & Then
        assertTrue(accountRepository.findByCustomerId(nonExistentCustomerId).isEmpty());
        assertEquals(0, accountRepository.countByCustomerId(nonExistentCustomerId));
        assertTrue(accountRepository.findActiveAccountsByCustomerId(nonExistentCustomerId).isEmpty());
        
        Page<Account> emptyPage = accountRepository.findByCustomerId(nonExistentCustomerId, PageRequest.of(0, 10));
        assertTrue(emptyPage.getContent().isEmpty());
        assertEquals(0, emptyPage.getTotalElements());
    }

    @Test
    @DisplayName("Should handle pagination edge cases")
    void pagination_EdgeCases_ShouldHandleCorrectly() {
        // Test empty page beyond available data
        Pageable beyondDataPageable = PageRequest.of(10, 10);
        Page<Account> emptyPage = accountRepository.findByCustomerId(testCustomerId, beyondDataPageable);
        
        assertTrue(emptyPage.getContent().isEmpty());
        assertEquals(2, emptyPage.getTotalElements()); // Total elements should still be correct
        assertFalse(emptyPage.hasNext());
        assertTrue(emptyPage.hasPrevious());

        // Test large page size
        Pageable largePageable = PageRequest.of(0, 100);
        Page<Account> largePage = accountRepository.findByCustomerId(testCustomerId, largePageable);
        
        assertEquals(2, largePage.getContent().size());
        assertEquals(2, largePage.getTotalElements());
        assertFalse(largePage.hasNext());
    }

    @Test
    @DisplayName("Should maintain referential integrity with metadata")
    void metadata_Operations_ShouldMaintainIntegrity() {
        // Given
        Account accountWithMetadata = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        accountWithMetadata.getMetadata().put("testKey", "testValue");
        accountWithMetadata.getMetadata().put("anotherKey", "anotherValue");
        
        Account saved = accountRepository.save(accountWithMetadata);

        // When
        Optional<Account> retrieved = accountRepository.findById(saved.getAccountId());

        // Then
        assertTrue(retrieved.isPresent());
        assertEquals(2, retrieved.get().getMetadata().size());
        assertEquals("testValue", retrieved.get().getMetadata().get("testKey"));
        assertEquals("anotherValue", retrieved.get().getMetadata().get("anotherKey"));
    }
}