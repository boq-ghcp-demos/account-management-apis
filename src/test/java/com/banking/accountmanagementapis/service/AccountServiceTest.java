package com.banking.accountmanagementapis.service;

import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.entity.Account;
import com.banking.accountmanagementapis.repository.AccountRepository;
import com.banking.accountmanagementapis.testutil.TestDataGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive Unit Tests for AccountService
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Account Service Unit Tests")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private CreateAccountRequest validCreateRequest;
    private UpdateAccountRequest validUpdateRequest;
    private Account testAccount;
    private String testCustomerId;
    private String testAccountId;

    @BeforeEach
    void setUp() {
        testCustomerId = TestDataGenerator.generateUUID();
        testAccountId = TestDataGenerator.generateUUID();
        
        validCreateRequest = TestDataGenerator.generateCreateAccountRequest(
            AccountType.CHECKING, "USD", BigDecimal.valueOf(1000.00));
        
        validUpdateRequest = TestDataGenerator.generateUpdateAccountRequest();
        
        testAccount = TestDataGenerator.generateAccountEntity(testCustomerId, testAccountId);
    }

    @Test
    @DisplayName("Should create account successfully with valid request")
    void createAccount_WithValidRequest_ShouldReturnAccountResponse() {
        // Given
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.createAccount(validCreateRequest, testCustomerId);

        // Then
        assertNotNull(result);
        assertEquals(testAccountId, result.getAccountId());
        assertEquals(testCustomerId, result.getCustomerId());
        assertEquals(AccountType.CHECKING, result.getAccountType());
        assertEquals("USD", result.getCurrency());
        
        verify(accountRepository).existsByAccountNumber(anyString());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should handle account number collision during creation")
    void createAccount_WithAccountNumberCollision_ShouldGenerateUniqueNumber() {
        // Given
        when(accountRepository.existsByAccountNumber(anyString()))
            .thenReturn(true)   // First check returns true (collision)
            .thenReturn(false); // Second check returns false (unique)
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.createAccount(validCreateRequest, testCustomerId);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(2)).existsByAccountNumber(anyString());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should get account by ID successfully")
    void getAccountById_WithValidAccountId_ShouldReturnAccount() {
        // Given
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));

        // When
        AccountResponse result = accountService.getAccountById(testAccountId, testCustomerId);

        // Then
        assertNotNull(result);
        assertEquals(testAccountId, result.getAccountId());
        assertEquals(testCustomerId, result.getCustomerId());
        verify(accountRepository).findById(testAccountId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when account not found")
    void getAccountById_WithInvalidAccountId_ShouldThrowException() {
        // Given
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, 
            () -> accountService.getAccountById(testAccountId, testCustomerId));
        
        verify(accountRepository).findById(testAccountId);
    }

    @Test
    @DisplayName("Should throw SecurityException when customer doesn't own account")
    void getAccountById_WithWrongCustomerId_ShouldThrowSecurityException() {
        // Given
        String wrongCustomerId = TestDataGenerator.generateUUID();
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(SecurityException.class, 
            () -> accountService.getAccountById(testAccountId, wrongCustomerId));
        
        verify(accountRepository).findById(testAccountId);
    }

    @Test
    @DisplayName("Should get accounts for customer with pagination")
    void getAccountsForCustomer_WithValidCustomerId_ShouldReturnPagedAccounts() {
        // Given
        Pageable pageable = TestDataGenerator.generatePageable();
        List<Account> accounts = TestDataGenerator.generateAccountEntitiesForCustomer(testCustomerId, 5);
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());
        
        when(accountRepository.findByCustomerId(testCustomerId, pageable)).thenReturn(accountPage);

        // When
        Page<AccountResponse> result = accountService.getAccountsForCustomer(testCustomerId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        
        verify(accountRepository).findByCustomerId(testCustomerId, pageable);
    }

    @Test
    @DisplayName("Should get accounts with filters")
    void getAccountsWithFilters_WithFilters_ShouldReturnFilteredAccounts() {
        // Given
        Pageable pageable = TestDataGenerator.generatePageable();
        AccountType accountType = AccountType.SAVINGS;
        AccountStatus status = AccountStatus.ACTIVE;
        String currency = "USD";
        
        List<Account> accounts = Arrays.asList(testAccount);
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());
        
        when(accountRepository.findAccountsWithFilters(testCustomerId, accountType, status, currency, pageable))
            .thenReturn(accountPage);

        // When
        Page<AccountResponse> result = accountService.getAccountsWithFilters(
            testCustomerId, accountType, status, currency, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        
        verify(accountRepository).findAccountsWithFilters(testCustomerId, accountType, status, currency, pageable);
    }

    @Test
    @DisplayName("Should update account successfully")
    void updateAccount_WithValidRequest_ShouldReturnUpdatedAccount() {
        // Given
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.updateAccount(testAccountId, validUpdateRequest, testCustomerId);

        // Then
        assertNotNull(result);
        assertEquals(testAccountId, result.getAccountId());
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent account")
    void updateAccount_WithInvalidAccountId_ShouldThrowException() {
        // Given
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, 
            () -> accountService.updateAccount(testAccountId, validUpdateRequest, testCustomerId));
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw SecurityException when updating account not owned by customer")
    void updateAccount_WithWrongCustomerId_ShouldThrowSecurityException() {
        // Given
        String wrongCustomerId = TestDataGenerator.generateUUID();
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(SecurityException.class, 
            () -> accountService.updateAccount(testAccountId, validUpdateRequest, wrongCustomerId));
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should close account successfully when balance is zero")
    void closeAccount_WithZeroBalance_ShouldCloseAccount() {
        // Given
        testAccount.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        assertDoesNotThrow(() -> accountService.closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST"));

        // Then
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when closing account with non-zero balance")
    void closeAccount_WithNonZeroBalance_ShouldThrowException() {
        // Given
        testAccount.setBalance(BigDecimal.valueOf(100.00));
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(IllegalStateException.class, 
            () -> accountService.closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST"));
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when closing non-existent account")
    void closeAccount_WithInvalidAccountId_ShouldThrowException() {
        // Given
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, 
            () -> accountService.closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST"));
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw SecurityException when closing account not owned by customer")
    void closeAccount_WithWrongCustomerId_ShouldThrowSecurityException() {
        // Given
        String wrongCustomerId = TestDataGenerator.generateUUID();
        testAccount.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(SecurityException.class, 
            () -> accountService.closeAccount(testAccountId, wrongCustomerId, "CUSTOMER_REQUEST"));
        
        verify(accountRepository).findById(testAccountId);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get active accounts count for customer")
    void getActiveAccountsCount_WithValidCustomerId_ShouldReturnCount() {
        // Given
        List<Account> activeAccounts = TestDataGenerator.generateAccountEntitiesForCustomer(testCustomerId, 3);
        when(accountRepository.findActiveAccountsByCustomerId(testCustomerId)).thenReturn(activeAccounts);

        // When
        long count = accountService.getActiveAccountsCount(testCustomerId);

        // Then
        assertEquals(3, count);
        verify(accountRepository).findActiveAccountsByCustomerId(testCustomerId);
    }

    @Test
    @DisplayName("Should check if account exists")
    void accountExists_WithValidAccountId_ShouldReturnTrue() {
        // Given
        when(accountRepository.existsById(testAccountId)).thenReturn(true);

        // When
        boolean exists = accountService.accountExists(testAccountId);

        // Then
        assertTrue(exists);
        verify(accountRepository).existsById(testAccountId);
    }

    @Test
    @DisplayName("Should return false when account does not exist")
    void accountExists_WithInvalidAccountId_ShouldReturnFalse() {
        // Given
        when(accountRepository.existsById(testAccountId)).thenReturn(false);

        // When
        boolean exists = accountService.accountExists(testAccountId);

        // Then
        assertFalse(exists);
        verify(accountRepository).existsById(testAccountId);
    }

    @Test
    @DisplayName("Should handle null values gracefully in update request")
    void updateAccount_WithNullValues_ShouldHandleGracefully() {
        // Given
        UpdateAccountRequest requestWithNulls = new UpdateAccountRequest(null, null);
        when(accountRepository.findById(testAccountId)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse result = accountService.updateAccount(testAccountId, requestWithNulls, testCustomerId);

        // Then
        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }
}