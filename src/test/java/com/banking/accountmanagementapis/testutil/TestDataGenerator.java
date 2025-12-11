package com.banking.accountmanagementapis.testutil;

import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.entity.Account;
import net.datafaker.Faker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Test Data Generator utility for creating test data, mocks, and stubs
 */
public class TestDataGenerator {
    
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    // Valid currencies
    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "CAD", "AUD"};
    
    // Valid branch IDs
    private static final String[] BRANCH_IDS = {"BR001", "BR002", "BR003", "BR004", "BR005"};
    
    /**
     * Generate a random Customer Details DTO
     */
    public static CustomerDetails generateCustomerDetails() {
        return new CustomerDetails(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.internet().emailAddress(),
            faker.phoneNumber().cellPhone(),
            faker.address().fullAddress()
        );
    }
    
    /**
     * Generate a random Create Account Request DTO
     */
    public static CreateAccountRequest generateCreateAccountRequest() {
        return generateCreateAccountRequest(null, null, null);
    }
    
    /**
     * Generate a Create Account Request with specific parameters
     */
    public static CreateAccountRequest generateCreateAccountRequest(AccountType accountType, String currency, BigDecimal initialDeposit) {
        CustomerDetails customerDetails = generateCustomerDetails();
        
        return new CreateAccountRequest(
            accountType != null ? accountType : getRandomAccountType(),
            currency != null ? currency : getRandomCurrency(),
            initialDeposit != null ? initialDeposit : generateRandomAmount(),
            customerDetails,
            faker.lorem().words(3).toString().replace("[", "").replace("]", "").replace(",", ""),
            generateMetadata()
        );
    }
    
    /**
     * Generate an Update Account Request DTO
     */
    public static UpdateAccountRequest generateUpdateAccountRequest() {
        return new UpdateAccountRequest(
            faker.lorem().words(2).toString().replace("[", "").replace("]", "").replace(",", ""),
            generateMetadata()
        );
    }
    
    /**
     * Generate an Account Response DTO
     */
    public static AccountResponse generateAccountResponse() {
        return generateAccountResponse(null, null);
    }
    
    /**
     * Generate an Account Response with specific customer ID
     */
    public static AccountResponse generateAccountResponse(String customerId, String accountId) {
        String currency = getRandomCurrency();
        BigDecimal balance = generateRandomAmount();
        
        AccountResponse response = new AccountResponse();
        response.setAccountId(accountId != null ? accountId : UUID.randomUUID().toString());
        response.setAccountNumber(generateMaskedAccountNumber());
        response.setAccountType(getRandomAccountType());
        response.setStatus(getRandomAccountStatus());
        response.setCurrency(currency);
        response.setBalance(new MonetaryAmount(balance, currency));
        response.setAvailableBalance(new MonetaryAmount(balance, currency));
        response.setAccountNickname(faker.lorem().words(3).toString().replace("[", "").replace("]", "").replace(",", ""));
        response.setCustomerId(customerId != null ? customerId : UUID.randomUUID().toString());
        response.setBranchId(getRandomBranchId());
        response.setCreatedAt(generatePastDateTime());
        response.setUpdatedAt(generateRecentDateTime());
        response.setLastActivityAt(generateRecentDateTime());
        response.setMetadata(generateMetadata());
        
        return response;
    }
    
    /**
     * Generate an Account Entity
     */
    public static Account generateAccountEntity() {
        return generateAccountEntity(null, null);
    }
    
    /**
     * Generate an Account Entity with specific customer ID
     */
    public static Account generateAccountEntity(String customerId, String accountId) {
        CustomerDetails customerDetails = generateCustomerDetails();
        
        Account account = new Account(
            generateAccountNumber(),
            getRandomAccountType(),
            getRandomCurrency(),
            generateRandomAmount(),
            customerId != null ? customerId : UUID.randomUUID().toString(),
            customerDetails.getFirstName(),
            customerDetails.getLastName(),
            customerDetails.getEmail(),
            customerDetails.getPhoneNumber(),
            customerDetails.getAddress()
        );
        
        if (accountId != null) {
            account.setAccountId(accountId);
        }
        
        account.setAccountNickname(faker.lorem().words(3).toString().replace("[", "").replace("]", "").replace(",", ""));
        account.setBranchId(getRandomBranchId());
        account.setStatus(getRandomAccountStatus());
        account.setMetadata(generateMetadata());
        
        return account;
    }
    
    /**
     * Generate a list of Account entities
     */
    public static List<Account> generateAccountEntities(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> generateAccountEntity())
            .collect(Collectors.toList());
    }
    
    /**
     * Generate a list of Account entities for a specific customer
     */
    public static List<Account> generateAccountEntitiesForCustomer(String customerId, int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> generateAccountEntity(customerId, null))
            .collect(Collectors.toList());
    }
    
    /**
     * Generate a Page of Account entities
     */
    public static Page<Account> generateAccountPage(List<Account> accounts, Pageable pageable) {
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), accounts.size());
        
        List<Account> pageContent = accounts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, accounts.size());
    }
    
    /**
     * Generate a Page of AccountResponse DTOs
     */
    public static Page<AccountResponse> generateAccountResponsePage(int totalElements, Pageable pageable) {
        List<AccountResponse> responses = IntStream.range(0, Math.min(pageable.getPageSize(), totalElements))
            .mapToObj(i -> generateAccountResponse())
            .collect(Collectors.toList());
            
        return new PageImpl<>(responses, pageable, totalElements);
    }
    
    /**
     * Generate Monetary Amount
     */
    public static MonetaryAmount generateMonetaryAmount() {
        return generateMonetaryAmount(null);
    }
    
    /**
     * Generate Monetary Amount with specific currency
     */
    public static MonetaryAmount generateMonetaryAmount(String currency) {
        return new MonetaryAmount(
            generateRandomAmount(),
            currency != null ? currency : getRandomCurrency()
        );
    }
    
    /**
     * Generate random BigDecimal amount between 0 and 10000
     */
    public static BigDecimal generateRandomAmount() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 0, 10000));
    }
    
    /**
     * Generate random positive BigDecimal amount between 1 and 10000
     */
    public static BigDecimal generateRandomPositiveAmount() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10000));
    }
    
    /**
     * Generate random account number (10 digits)
     */
    public static String generateAccountNumber() {
        return faker.number().digits(10);
    }
    
    /**
     * Generate masked account number
     */
    public static String generateMaskedAccountNumber() {
        return "****" + faker.number().digits(4);
    }
    
    /**
     * Generate random UUID string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate metadata map
     */
    public static Map<String, String> generateMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("source", "test");
        metadata.put("createdBy", faker.name().username());
        metadata.put("priority", faker.options().option("HIGH", "MEDIUM", "LOW"));
        return metadata;
    }
    
    /**
     * Generate past date time (1-30 days ago)
     */
    public static LocalDateTime generatePastDateTime() {
        return LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30));
    }
    
    /**
     * Generate recent date time (1-5 days ago)
     */
    public static LocalDateTime generateRecentDateTime() {
        return LocalDateTime.now().minusDays(faker.number().numberBetween(1, 5));
    }
    
    /**
     * Get random Account Type
     */
    public static AccountType getRandomAccountType() {
        AccountType[] types = AccountType.values();
        return types[random.nextInt(types.length)];
    }
    
    /**
     * Get random Account Status
     */
    public static AccountStatus getRandomAccountStatus() {
        AccountStatus[] statuses = AccountStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    /**
     * Get random currency
     */
    public static String getRandomCurrency() {
        return CURRENCIES[random.nextInt(CURRENCIES.length)];
    }
    
    /**
     * Get random branch ID
     */
    public static String getRandomBranchId() {
        return BRANCH_IDS[random.nextInt(BRANCH_IDS.length)];
    }
    
    /**
     * Generate test pageable
     */
    public static Pageable generatePageable() {
        return PageRequest.of(0, 10);
    }
    
    /**
     * Generate test pageable with specific parameters
     */
    public static Pageable generatePageable(int page, int size) {
        return PageRequest.of(page, size);
    }
    
    /**
     * Generate valid email
     */
    public static String generateValidEmail() {
        return faker.internet().emailAddress();
    }
    
    /**
     * Generate valid phone number
     */
    public static String generateValidPhoneNumber() {
        return "+1" + faker.number().digits(10);
    }
    
    /**
     * Generate invalid email (for negative testing)
     */
    public static String generateInvalidEmail() {
        return faker.lorem().word() + "@invalid";
    }
    
    /**
     * Generate invalid phone number (for negative testing)
     */
    public static String generateInvalidPhoneNumber() {
        return "invalid-phone";
    }
}