package com.banking.accountmanagementapis.config;

import com.banking.accountmanagementapis.entity.Account;
import com.banking.accountmanagementapis.dto.*;
import com.banking.accountmanagementapis.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample Data Loader - Creates sample accounts when the application starts
 */
@Component
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            System.out.println("🏦 Creating sample banking data...");
            createSampleAccounts();
            System.out.println("✅ Sample data created successfully!");
            System.out.println("📊 Total accounts created: " + accountRepository.count());
        } else {
            System.out.println("📋 Database already contains data. Skipping sample data creation.");
        }
    }

    private void createSampleAccounts() {
        // Account 1 - John Smith - Checking Account
        Map<String, String> metadata1 = new HashMap<>();
        metadata1.put("preferredBranch", "NYC-001");
        metadata1.put("accountPurpose", "primary");
        
        Account account1 = new Account(
            generateAccountNumber(),
            AccountType.CHECKING,
            "USD",
            new BigDecimal("1500.00"),
            "customer-001",
            "John",
            "Smith",
            "john.smith@email.com",
            "+1234567890",
            "123 Main Street, New York, NY 10001"
        );
        account1.setAccountNickname("Primary Checking");
        account1.setBranchId("NYC-001");
        account1.setMetadata(metadata1);
        accountRepository.save(account1);

        // Account 2 - Jane Doe - Savings Account
        Map<String, String> metadata2 = new HashMap<>();
        metadata2.put("preferredBranch", "LA-002");
        metadata2.put("accountPurpose", "savings");
        metadata2.put("interestRate", "2.5");
        
        Account account2 = new Account(
            generateAccountNumber(),
            AccountType.SAVINGS,
            "USD",
            new BigDecimal("5000.00"),
            "customer-002",
            "Jane",
            "Doe",
            "jane.doe@email.com",
            "+1987654321",
            "456 Oak Avenue, Los Angeles, CA 90210"
        );
        account2.setAccountNickname("Emergency Fund");
        account2.setBranchId("LA-002");
        account2.setMetadata(metadata2);
        accountRepository.save(account2);

        // Account 3 - Robert Johnson - Money Market Account
        Map<String, String> metadata3 = new HashMap<>();
        metadata3.put("preferredBranch", "CHI-003");
        metadata3.put("accountPurpose", "investment");
        metadata3.put("riskLevel", "moderate");
        
        Account account3 = new Account(
            generateAccountNumber(),
            AccountType.MONEY_MARKET,
            "USD",
            new BigDecimal("10000.00"),
            "customer-003",
            "Robert",
            "Johnson",
            "bob.johnson@email.com",
            "+1555666777",
            "789 Pine Street, Chicago, IL 60601"
        );
        account3.setAccountNickname("Investment Fund");
        account3.setBranchId("CHI-003");
        account3.setMetadata(metadata3);
        accountRepository.save(account3);

        // Account 4 - John Smith's Second Account (Savings)
        Map<String, String> metadata4 = new HashMap<>();
        metadata4.put("preferredBranch", "NYC-001");
        metadata4.put("accountPurpose", "vacation");
        metadata4.put("targetAmount", "5000");
        
        Account account4 = new Account(
            generateAccountNumber(),
            AccountType.SAVINGS,
            "USD",
            new BigDecimal("2500.00"),
            "customer-001", // Same customer as account 1
            "John",
            "Smith",
            "john.smith@email.com",
            "+1234567890",
            "123 Main Street, New York, NY 10001"
        );
        account4.setAccountNickname("Vacation Fund");
        account4.setBranchId("NYC-001");
        account4.setMetadata(metadata4);
        accountRepository.save(account4);

        // Account 5 - Maria Garcia - Certificate of Deposit
        Map<String, String> metadata5 = new HashMap<>();
        metadata5.put("preferredBranch", "MIA-004");
        metadata5.put("accountPurpose", "long-term-savings");
        metadata5.put("maturityDate", "2030-12-12");
        metadata5.put("interestRate", "4.2");
        
        Account account5 = new Account(
            generateAccountNumber(),
            AccountType.CERTIFICATE_DEPOSIT,
            "USD",
            new BigDecimal("15000.00"),
            "customer-004",
            "Maria",
            "Garcia",
            "maria.garcia@email.com",
            "+1444555666",
            "321 Elm Street, Miami, FL 33101"
        );
        account5.setAccountNickname("5-Year CD");
        account5.setBranchId("MIA-004");
        account5.setMetadata(metadata5);
        accountRepository.save(account5);

        // Account 6 - David Wilson - Investment Account
        Map<String, String> metadata6 = new HashMap<>();
        metadata6.put("preferredBranch", "SF-005");
        metadata6.put("accountPurpose", "retirement");
        metadata6.put("portfolioType", "aggressive");
        
        Account account6 = new Account(
            generateAccountNumber(),
            AccountType.INVESTMENT,
            "USD",
            new BigDecimal("25000.00"),
            "customer-005",
            "David",
            "Wilson",
            "david.wilson@email.com",
            "+1777888999",
            "555 Market Street, San Francisco, CA 94105"
        );
        account6.setAccountNickname("Retirement Portfolio");
        account6.setBranchId("SF-005");
        account6.setMetadata(metadata6);
        accountRepository.save(account6);

        System.out.println("📝 Sample accounts created:");
        System.out.println("   • John Smith: Checking ($1,500) + Savings ($2,500)");
        System.out.println("   • Jane Doe: Savings ($5,000)");
        System.out.println("   • Robert Johnson: Money Market ($10,000)");
        System.out.println("   • Maria Garcia: Certificate Deposit ($15,000)");
        System.out.println("   • David Wilson: Investment ($25,000)");
        System.out.println("   💰 Total sample funds: $59,000");
    }

    private String generateAccountNumber() {
        // Generate a simple 10-digit account number
        return String.format("%010d", (int) (Math.random() * 1000000000));
    }
}