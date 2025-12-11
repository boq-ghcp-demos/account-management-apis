package com.banking.accountmanagementapis.dto;

import com.banking.accountmanagementapis.testutil.TestDataGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validation Tests for DTO classes
 */
@DisplayName("DTO Validation Tests")
class DtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid CreateAccountRequest should pass validation")
    void createAccountRequest_WithValidData_ShouldPassValidation() {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest(
            AccountType.CHECKING, "USD", BigDecimal.valueOf(1000.00));

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Valid request should not have validation violations");
    }

    @Test
    @DisplayName("CreateAccountRequest with null account type should fail validation")
    void createAccountRequest_WithNullAccountType_ShouldFailValidation() {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setAccountType(null);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Account type is required")));
    }

    @Test
    @DisplayName("CreateAccountRequest with null currency should fail validation")
    void createAccountRequest_WithNullCurrency_ShouldFailValidation() {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setCurrency(null);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"US", "USDD", "123", "us", "usd"})
    @DisplayName("CreateAccountRequest with invalid currency format should fail validation")
    void createAccountRequest_WithInvalidCurrency_ShouldFailValidation(String invalidCurrency) {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setCurrency(invalidCurrency);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be a valid ISO 4217 code")));
    }

    @Test
    @DisplayName("CreateAccountRequest with null initial deposit should fail validation")
    void createAccountRequest_WithNullInitialDeposit_ShouldFailValidation() {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setInitialDeposit(null);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Initial deposit is required")));
    }

    @Test
    @DisplayName("CreateAccountRequest with null customer details should fail validation")
    void createAccountRequest_WithNullCustomerDetails_ShouldFailValidation() {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setCustomerDetails(null);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Customer details are required")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "This is a very long nickname that exceeds the maximum allowed length of 50 characters"})
    @DisplayName("CreateAccountRequest with invalid account nickname should fail validation")
    void createAccountRequest_WithInvalidNickname_ShouldFailValidation(String invalidNickname) {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setAccountNickname(invalidNickname);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test@Account", "Test#Account", "Test&Account"})
    @DisplayName("CreateAccountRequest with invalid characters in nickname should fail validation")
    void createAccountRequest_WithInvalidNicknameCharacters_ShouldFailValidation(String invalidNickname) {
        // Given
        CreateAccountRequest request = TestDataGenerator.generateCreateAccountRequest();
        request.setAccountNickname(invalidNickname);

        // When
        Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Account nickname contains invalid characters")));
    }

    @Test
    @DisplayName("Valid CustomerDetails should pass validation")
    void customerDetails_WithValidData_ShouldPassValidation() {
        // Given
        CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();

        // When
        Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

        // Then
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CustomerDetails with blank first name should fail validation")
    void customerDetails_WithBlankFirstName_ShouldFailValidation(String blankFirstName) {
        // Given
        CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();
        customerDetails.setFirstName(blankFirstName);

        // When
        Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("First name is required")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("CustomerDetails with blank last name should fail validation")
    void customerDetails_WithBlankLastName_ShouldFailValidation(String blankLastName) {
        // Given
        CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();
        customerDetails.setLastName(blankLastName);

        // When
        Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Last name is required")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "@invalid.com", "invalid@", "invalid.com", "invalid@.com"})
    @DisplayName("CustomerDetails with invalid email should fail validation")
    void customerDetails_WithInvalidEmail_ShouldFailValidation(String invalidEmail) {
        // Given
        CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();
        customerDetails.setEmail(invalidEmail);

        // When
        Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid email format")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "abc", "12345678901234567", "+", "123-456-7890"})
    @DisplayName("CustomerDetails with invalid phone number should fail validation")
    void customerDetails_WithInvalidPhoneNumber_ShouldFailValidation(String invalidPhone) {
        // Given
        CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();
        customerDetails.setPhoneNumber(invalidPhone);

        // When
        Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid phone number format")));
    }

    @Test
    @DisplayName("CustomerDetails with valid phone numbers should pass validation")
    void customerDetails_WithValidPhoneNumbers_ShouldPassValidation() {
        String[] validPhones = {"+12345678901", "+1234567890123456", "12345678901"};
        
        for (String validPhone : validPhones) {
            // Given
            CustomerDetails customerDetails = TestDataGenerator.generateCustomerDetails();
            customerDetails.setPhoneNumber(validPhone);

            // When
            Set<ConstraintViolation<CustomerDetails>> violations = validator.validate(customerDetails);

            // Then
            assertTrue(violations.isEmpty(), "Valid phone number should pass validation: " + validPhone);
        }
    }

    @Test
    @DisplayName("CustomerDetails with names exceeding max length should fail validation")
    void customerDetails_WithLongNames_ShouldFailValidation() {
        String longName = "a".repeat(51); // Exceeds 50 character limit
        
        // Test first name
        CustomerDetails customerDetails1 = TestDataGenerator.generateCustomerDetails();
        customerDetails1.setFirstName(longName);
        Set<ConstraintViolation<CustomerDetails>> violations1 = validator.validate(customerDetails1);
        assertFalse(violations1.isEmpty());
        assertTrue(violations1.stream().anyMatch(v -> v.getMessage().contains("First name must not exceed 50 characters")));

        // Test last name
        CustomerDetails customerDetails2 = TestDataGenerator.generateCustomerDetails();
        customerDetails2.setLastName(longName);
        Set<ConstraintViolation<CustomerDetails>> violations2 = validator.validate(customerDetails2);
        assertFalse(violations2.isEmpty());
        assertTrue(violations2.stream().anyMatch(v -> v.getMessage().contains("Last name must not exceed 50 characters")));
    }

    @Test
    @DisplayName("Valid MonetaryAmount should pass validation")
    void monetaryAmount_WithValidData_ShouldPassValidation() {
        // Given
        MonetaryAmount amount = new MonetaryAmount(BigDecimal.valueOf(100.00), "USD");

        // When
        Set<ConstraintViolation<MonetaryAmount>> violations = validator.validate(amount);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("MonetaryAmount with null amount should fail validation")
    void monetaryAmount_WithNullAmount_ShouldFailValidation() {
        // Given
        MonetaryAmount amount = new MonetaryAmount(null, "USD");

        // When
        Set<ConstraintViolation<MonetaryAmount>> violations = validator.validate(amount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount cannot be null")));
    }

    @Test
    @DisplayName("MonetaryAmount with negative amount should fail validation")
    void monetaryAmount_WithNegativeAmount_ShouldFailValidation() {
        // Given
        MonetaryAmount amount = new MonetaryAmount(BigDecimal.valueOf(-100.00), "USD");

        // When
        Set<ConstraintViolation<MonetaryAmount>> violations = validator.validate(amount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount must be non-negative")));
    }

    @Test
    @DisplayName("MonetaryAmount with null currency should fail validation")
    void monetaryAmount_WithNullCurrency_ShouldFailValidation() {
        // Given
        MonetaryAmount amount = new MonetaryAmount(BigDecimal.valueOf(100.00), null);

        // When
        Set<ConstraintViolation<MonetaryAmount>> violations = validator.validate(amount);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency cannot be null")));
    }

    @Test
    @DisplayName("Valid UpdateAccountRequest should pass validation")
    void updateAccountRequest_WithValidData_ShouldPassValidation() {
        // Given
        UpdateAccountRequest request = TestDataGenerator.generateUpdateAccountRequest();

        // When
        Set<ConstraintViolation<UpdateAccountRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("UpdateAccountRequest with null values should pass validation (optional fields)")
    void updateAccountRequest_WithNullValues_ShouldPassValidation() {
        // Given
        UpdateAccountRequest request = new UpdateAccountRequest(null, null);

        // When
        Set<ConstraintViolation<UpdateAccountRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Update request with null optional fields should be valid");
    }

    @Test
    @DisplayName("UpdateAccountRequest with long nickname should fail validation")
    void updateAccountRequest_WithLongNickname_ShouldFailValidation() {
        // Given
        String longNickname = "a".repeat(51); // Exceeds 50 character limit
        UpdateAccountRequest request = new UpdateAccountRequest(longNickname, null);

        // When
        Set<ConstraintViolation<UpdateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Account nickname must not exceed 50 characters")));
    }

    @Test
    @DisplayName("UpdateAccountRequest with invalid nickname characters should fail validation")
    void updateAccountRequest_WithInvalidNicknameCharacters_ShouldFailValidation() {
        // Given
        UpdateAccountRequest request = new UpdateAccountRequest("Invalid@Nickname#", null);

        // When
        Set<ConstraintViolation<UpdateAccountRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Account nickname contains invalid characters")));
    }
}