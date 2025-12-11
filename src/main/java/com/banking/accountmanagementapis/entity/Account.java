package com.banking.accountmanagementapis.entity;

import com.banking.accountmanagementapis.dto.AccountStatus;
import com.banking.accountmanagementapis.dto.AccountType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Account JPA Entity for SQLite database
 */
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_customer_id", columnList = "customerId"),
    @Index(name = "idx_account_type", columnList = "accountType"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_account_number", columnList = "accountNumber", unique = true)
})
public class Account {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 36)
    private String accountId;
    
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;
    
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "available_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance;
    
    @Column(name = "account_nickname", length = 50)
    private String accountNickname;
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;
    
    @Column(name = "branch_id", length = 10)
    private String branchId;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    @ElementCollection
    @CollectionTable(name = "account_metadata", 
                    joinColumns = @JoinColumn(name = "account_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    private Map<String, String> metadata = new HashMap<>();
    
    public Account() {
        this.accountId = UUID.randomUUID().toString();
        this.status = AccountStatus.PENDING_APPROVAL;
        this.balance = BigDecimal.ZERO;
        this.availableBalance = BigDecimal.ZERO;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public Account(String accountNumber, AccountType accountType, String currency, 
                   BigDecimal initialDeposit, String customerId, String firstName, 
                   String lastName, String email, String phoneNumber, String address) {
        this();
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = initialDeposit;
        this.availableBalance = initialDeposit;
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = AccountStatus.ACTIVE;
    }
    
    @PrePersist
    public void prePersist() {
        if (accountId == null) {
            accountId = UUID.randomUUID().toString();
        }
        if (lastActivityAt == null) {
            lastActivityAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        lastActivityAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        this.availableBalance = balance; // For simplicity, available = balance
    }
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }
    
    public String getAccountNickname() {
        return accountNickname;
    }
    
    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getBranchId() {
        return branchId;
    }
    
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }
    
    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", status=" + status +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}