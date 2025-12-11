package com.banking.accountmanagementapis.integration;

import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.entity.Account;
import com.banking.accountmanagementapis.repository.AccountRepository;
import com.banking.accountmanagementapis.testutil.TestDataGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive Integration Tests for Account Management APIs
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Account Management Integration Tests")
class AccountManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String testCustomerId;
    private String testRequestId;

    @BeforeEach
    void setUp() {
        testCustomerId = TestDataGenerator.generateUUID();
        testRequestId = TestDataGenerator.generateUUID();
        
        // Clean up database before each test
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Should perform complete account lifecycle - create, read, update, delete")
    void accountLifecycle_CompleteFlow_ShouldWorkEndToEnd() throws Exception {
        // Step 1: Create Account
        CreateAccountRequest createRequest = TestDataGenerator.generateCreateAccountRequest(
            AccountType.SAVINGS, "USD", BigDecimal.valueOf(1000.00));

        String createResponse = mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.currency", is("USD")))
                .andExpect(jsonPath("$.customerId", is(testCustomerId)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponse createdAccount = objectMapper.readValue(createResponse, AccountResponse.class);
        String accountId = createdAccount.getAccountId();
        
        // Verify account was persisted in database
        assertTrue(accountRepository.existsById(accountId));
        
        // Step 2: Read Account
        mockMvc.perform(get("/v2/accounts/{accountId}", accountId)
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", is(accountId)))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.customerId", is(testCustomerId)));

        // Step 3: Update Account
        UpdateAccountRequest updateRequest = new UpdateAccountRequest(
            "My Updated Savings Account", 
            TestDataGenerator.generateMetadata());

        mockMvc.perform(put("/v2/accounts/{accountId}", accountId)
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", is(accountId)))
                .andExpect(jsonPath("$.accountNickname", is("My Updated Savings Account")));

        // Verify update was persisted
        Account updatedAccount = accountRepository.findById(accountId).orElseThrow();
        assertEquals("My Updated Savings Account", updatedAccount.getAccountNickname());

        // Step 4: Set balance to zero (prerequisite for closing)
        updatedAccount.setBalance(BigDecimal.ZERO);
        accountRepository.save(updatedAccount);

        // Step 5: Close Account
        mockMvc.perform(delete("/v2/accounts/{accountId}", accountId)
                    .header("X-Customer-ID", testCustomerId)
                    .param("reason", "CUSTOMER_REQUEST"))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify account status was updated to CLOSED
        Account closedAccount = accountRepository.findById(accountId).orElseThrow();
        assertEquals(AccountStatus.CLOSED, closedAccount.getStatus());
    }

    @Test
    @DisplayName("Should list accounts with filtering and pagination")
    void listAccounts_WithMultipleAccountsAndFilters_ShouldReturnCorrectResults() throws Exception {
        // Create multiple accounts with different types and statuses
        Account account1 = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        account1.setAccountType(AccountType.CHECKING);
        account1.setStatus(AccountStatus.ACTIVE);
        account1.setCurrency("USD");
        
        Account account2 = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        account2.setAccountType(AccountType.SAVINGS);
        account2.setStatus(AccountStatus.ACTIVE);
        account2.setCurrency("USD");
        
        Account account3 = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        account3.setAccountType(AccountType.CHECKING);
        account3.setStatus(AccountStatus.INACTIVE);
        account3.setCurrency("EUR");

        accountRepository.saveAll(List.of(account1, account2, account3));

        // Test 1: List all accounts
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(3)))
                .andExpect(jsonPath("$.totalElements", is(3)));

        // Test 2: Filter by account type (CHECKING)
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .param("accountType", "CHECKING"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(2)));

        // Test 3: Filter by status (ACTIVE)
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .param("status", "ACTIVE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(2)));

        // Test 4: Filter by currency (EUR)
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .param("currency", "EUR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(1)));

        // Test 5: Multiple filters (CHECKING + ACTIVE)
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .param("accountType", "CHECKING")
                    .param("status", "ACTIVE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(1)));

        // Test 6: Pagination
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .param("page", "0")
                    .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.hasNext", is(true)));
    }

    @Test
    @DisplayName("Should handle security correctly - customer isolation")
    void accountSecurity_CustomerIsolation_ShouldPreventUnauthorizedAccess() throws Exception {
        String customer1Id = TestDataGenerator.generateUUID();
        String customer2Id = TestDataGenerator.generateUUID();

        // Create account for customer1
        Account customer1Account = TestDataGenerator.generateAccountEntity(customer1Id, null);
        Account savedAccount = accountRepository.save(customer1Account);

        // Customer1 should be able to access their account
        mockMvc.perform(get("/v2/accounts/{accountId}", savedAccount.getAccountId())
                    .header("X-Customer-ID", customer1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(customer1Id)));

        // Customer2 should NOT be able to access customer1's account
        mockMvc.perform(get("/v2/accounts/{accountId}", savedAccount.getAccountId())
                    .header("X-Customer-ID", customer2Id))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Access denied")));

        // Customer2 should not see customer1's accounts in list
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", customer2Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @DisplayName("Should validate request data correctly")
    void requestValidation_WithInvalidData_ShouldReturnValidationErrors() throws Exception {
        // Test 1: Missing required fields
        CreateAccountRequest invalidRequest = new CreateAccountRequest();
        
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test 2: Invalid currency format
        CreateAccountRequest invalidCurrencyRequest = TestDataGenerator.generateCreateAccountRequest();
        invalidCurrencyRequest.setCurrency("INVALID");
        
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidCurrencyRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test 3: Invalid email format in customer details
        CreateAccountRequest invalidEmailRequest = TestDataGenerator.generateCreateAccountRequest();
        invalidEmailRequest.getCustomerDetails().setEmail("invalid-email");
        
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidEmailRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle business rules correctly")
    void businessRules_AccountClosure_ShouldEnforceBalanceRule() throws Exception {
        // Create account with non-zero balance
        Account accountWithBalance = TestDataGenerator.generateAccountEntity(testCustomerId, null);
        accountWithBalance.setBalance(BigDecimal.valueOf(100.00));
        Account savedAccount = accountRepository.save(accountWithBalance);

        // Should not be able to close account with non-zero balance
        mockMvc.perform(delete("/v2/accounts/{accountId}", savedAccount.getAccountId())
                    .header("X-Customer-ID", testCustomerId)
                    .param("reason", "CUSTOMER_REQUEST"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Account cannot be closed")))
                .andExpect(jsonPath("$.message", containsString("non-zero balance")));

        // Set balance to zero and try again
        savedAccount.setBalance(BigDecimal.ZERO);
        accountRepository.save(savedAccount);

        // Should now be able to close the account
        mockMvc.perform(delete("/v2/accounts/{accountId}", savedAccount.getAccountId())
                    .header("X-Customer-ID", testCustomerId)
                    .param("reason", "CUSTOMER_REQUEST"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should handle database constraints and unique account numbers")
    void databaseConstraints_UniqueAccountNumber_ShouldBeEnforced() throws Exception {
        // Create first account
        CreateAccountRequest request1 = TestDataGenerator.generateCreateAccountRequest();
        
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request1)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Create second account - should get different account number
        CreateAccountRequest request2 = TestDataGenerator.generateCreateAccountRequest();
        
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request2)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Verify both accounts exist with unique account numbers
        List<Account> accounts = accountRepository.findByCustomerId(testCustomerId);
        assertEquals(2, accounts.size());
        assertNotEquals(accounts.get(0).getAccountNumber(), accounts.get(1).getAccountNumber());
    }

    @Test
    @DisplayName("Should handle concurrent requests correctly")
    void concurrentRequests_MultipleAccountCreation_ShouldHandleGracefully() throws Exception {
        // This test simulates concurrent account creation
        // In a real scenario, you would use multiple threads
        
        CreateAccountRequest request1 = TestDataGenerator.generateCreateAccountRequest();
        CreateAccountRequest request2 = TestDataGenerator.generateCreateAccountRequest();
        CreateAccountRequest request3 = TestDataGenerator.generateCreateAccountRequest();

        // Create multiple accounts rapidly
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isCreated());

        // Verify all accounts were created successfully
        List<Account> accounts = accountRepository.findByCustomerId(testCustomerId);
        assertEquals(3, accounts.size());
    }
}
