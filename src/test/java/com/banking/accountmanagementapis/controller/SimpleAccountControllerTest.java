package com.banking.accountmanagementapis.controller;

import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.service.AccountService;
import com.banking.accountmanagementapis.testutil.TestDataGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive Unit Tests for SimpleAccountController
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(SimpleAccountController.class)
@ActiveProfiles("test")
@DisplayName("Account Controller Unit Tests")
class SimpleAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private String testCustomerId;
    private String testAccountId;
    private String testRequestId;
    private CreateAccountRequest validCreateRequest;
    private UpdateAccountRequest validUpdateRequest;
    private AccountResponse mockAccountResponse;

    @BeforeEach
    void setUp() {
        testCustomerId = TestDataGenerator.generateUUID();
        testAccountId = TestDataGenerator.generateUUID();
        testRequestId = TestDataGenerator.generateUUID();
        
        validCreateRequest = TestDataGenerator.generateCreateAccountRequest(
            AccountType.CHECKING, "USD", BigDecimal.valueOf(1000.00));
        
        validUpdateRequest = TestDataGenerator.generateUpdateAccountRequest();
        
        mockAccountResponse = TestDataGenerator.generateAccountResponse(testCustomerId, testAccountId);
    }

    @Test
    @DisplayName("Should return health status successfully")
    void health_ShouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/v2/accounts/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.message", containsString("Banking Account Management API is running")))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }

    @Test
    @DisplayName("Should list accounts successfully with valid customer ID")
    void listAccounts_WithValidCustomerId_ShouldReturnAccountsList() throws Exception {
        // Given
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AccountResponse> mockPage = TestDataGenerator.generateAccountResponsePage(5, expectedPageable);
        
        when(accountService.getAccountsWithFilters(eq(testCustomerId), isNull(), isNull(), isNull(), any(Pageable.class)))
            .thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accounts", hasSize(mockPage.getContent().size())))
                .andExpect(jsonPath("$.totalElements", is((int)mockPage.getTotalElements())))
                .andExpect(jsonPath("$.totalPages", is(mockPage.getTotalPages())))
                .andExpect(jsonPath("$.currentPage", is(mockPage.getNumber())))
                .andExpect(jsonPath("$.hasNext", is(mockPage.hasNext())));

        verify(accountService).getAccountsWithFilters(eq(testCustomerId), isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should list accounts with filters")
    void listAccounts_WithFilters_ShouldReturnFilteredAccounts() throws Exception {
        // Given
        AccountType accountType = AccountType.SAVINGS;
        AccountStatus status = AccountStatus.ACTIVE;
        String currency = "USD";
        
        Pageable expectedPageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "accountNumber"));
        Page<AccountResponse> mockPage = TestDataGenerator.generateAccountResponsePage(3, expectedPageable);
        
        when(accountService.getAccountsWithFilters(testCustomerId, accountType, status, currency, expectedPageable))
            .thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId)
                    .param("accountType", accountType.name())
                    .param("status", status.name())
                    .param("currency", currency)
                    .param("page", "0")
                    .param("size", "5")
                    .param("sortBy", "accountNumber")
                    .param("sortDir", "asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts", hasSize(mockPage.getContent().size())));

        verify(accountService).getAccountsWithFilters(testCustomerId, accountType, status, currency, expectedPageable);
    }

    @Test
    @DisplayName("Should return 400 when customer ID header is missing")
    void listAccounts_WithoutCustomerIdHeader_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Request-ID", testRequestId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 500 when service throws exception")
    void listAccounts_WhenServiceThrowsException_ShouldReturn500() throws Exception {
        // Given
        when(accountService.getAccountsWithFilters(anyString(), any(), any(), any(), any(Pageable.class)))
            .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(get("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Failed to retrieve accounts")))
                .andExpect(jsonPath("$.message", containsString("Database connection failed")));
    }

    @Test
    @DisplayName("Should get account details successfully")
    void getAccount_WithValidAccountId_ShouldReturnAccountDetails() throws Exception {
        // Given
        when(accountService.getAccountById(testAccountId, testCustomerId)).thenReturn(mockAccountResponse);

        // When & Then
        mockMvc.perform(get("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(testAccountId)))
                .andExpect(jsonPath("$.customerId", is(testCustomerId)));

        verify(accountService).getAccountById(testAccountId, testCustomerId);
    }

    @Test
    @DisplayName("Should return 404 when account not found")
    void getAccount_WithInvalidAccountId_ShouldReturn404() throws Exception {
        // Given
        when(accountService.getAccountById(testAccountId, testCustomerId))
            .thenThrow(new EntityNotFoundException("Account not found"));

        // When & Then
        mockMvc.perform(get("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Account not found")))
                .andExpect(jsonPath("$.accountId", is(testAccountId)));

        verify(accountService).getAccountById(testAccountId, testCustomerId);
    }

    @Test
    @DisplayName("Should return 403 when customer doesn't own account")
    void getAccount_WithWrongCustomerId_ShouldReturn403() throws Exception {
        // Given
        when(accountService.getAccountById(testAccountId, testCustomerId))
            .thenThrow(new SecurityException("Account does not belong to customer"));

        // When & Then
        mockMvc.perform(get("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Access denied")))
                .andExpect(jsonPath("$.message", is("Account does not belong to customer")));
    }

    @Test
    @DisplayName("Should create account successfully with valid request")
    void createAccount_WithValidRequest_ShouldReturnCreatedAccount() throws Exception {
        // Given
        when(accountService.createAccount(any(CreateAccountRequest.class), eq(testCustomerId)))
            .thenReturn(mockAccountResponse);

        // When & Then
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(testAccountId)))
                .andExpect(jsonPath("$.customerId", is(testCustomerId)));

        verify(accountService).createAccount(any(CreateAccountRequest.class), eq(testCustomerId));
    }

    @Test
    @DisplayName("Should return 400 for invalid create account request")
    void createAccount_WithInvalidRequest_ShouldReturn400() throws Exception {
        // Given - Create invalid request with null required fields
        CreateAccountRequest invalidRequest = new CreateAccountRequest();

        // When & Then
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when create account service throws IllegalArgumentException")
    void createAccount_WhenServiceThrowsIllegalArgumentException_ShouldReturn400() throws Exception {
        // Given
        when(accountService.createAccount(any(CreateAccountRequest.class), eq(testCustomerId)))
            .thenThrow(new IllegalArgumentException("Invalid account type"));

        // When & Then
        mockMvc.perform(post("/v2/accounts")
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid request data")))
                .andExpect(jsonPath("$.message", is("Invalid account type")));
    }

    @Test
    @DisplayName("Should update account successfully")
    void updateAccount_WithValidRequest_ShouldReturnUpdatedAccount() throws Exception {
        // Given
        when(accountService.updateAccount(testAccountId, validUpdateRequest, testCustomerId))
            .thenReturn(mockAccountResponse);

        // When & Then
        mockMvc.perform(put("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(testAccountId)));

        verify(accountService).updateAccount(testAccountId, validUpdateRequest, testCustomerId);
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent account")
    void updateAccount_WithInvalidAccountId_ShouldReturn404() throws Exception {
        // Given
        when(accountService.updateAccount(testAccountId, validUpdateRequest, testCustomerId))
            .thenThrow(new EntityNotFoundException("Account not found"));

        // When & Then
        mockMvc.perform(put("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should close account successfully")
    void closeAccount_WithValidRequest_ShouldReturn204() throws Exception {
        // Given
        doNothing().when(accountService).closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST");

        // When & Then
        mockMvc.perform(delete("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId)
                    .header("X-Request-ID", testRequestId)
                    .param("reason", "CUSTOMER_REQUEST"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountService).closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST");
    }

    @Test
    @DisplayName("Should return 409 when account cannot be closed due to non-zero balance")
    void closeAccount_WithNonZeroBalance_ShouldReturn409() throws Exception {
        // Given
        doThrow(new IllegalStateException("Cannot close account with non-zero balance"))
            .when(accountService).closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST");

        // When & Then
        mockMvc.perform(delete("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId)
                    .param("reason", "CUSTOMER_REQUEST"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Account cannot be closed")))
                .andExpect(jsonPath("$.message", is("Cannot close account with non-zero balance")));
    }

    @Test
    @DisplayName("Should use default reason when reason parameter is not provided")
    void closeAccount_WithoutReasonParameter_ShouldUseDefault() throws Exception {
        // Given
        doNothing().when(accountService).closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST");

        // When & Then
        mockMvc.perform(delete("/v2/accounts/{accountId}", testAccountId)
                    .header("X-Customer-ID", testCustomerId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(accountService).closeAccount(testAccountId, testCustomerId, "CUSTOMER_REQUEST");
    }
}
