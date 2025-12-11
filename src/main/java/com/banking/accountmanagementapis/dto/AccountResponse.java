package com.banking.accountmanagementapis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Account Response DTO
 */
public class AccountResponse {
    
    @JsonProperty("accountId")
    private String accountId;
    
    @JsonProperty("accountNumber")
    private String accountNumber;
    
    @JsonProperty("accountType")
    private AccountType accountType;
    
    @JsonProperty("status")
    private AccountStatus status;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("balance")
    private MonetaryAmount balance;
    
    @JsonProperty("availableBalance")
    private MonetaryAmount availableBalance;
    
    @JsonProperty("accountNickname")
    private String accountNickname;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("branchId")
    private String branchId;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @JsonProperty("lastActivityAt")
    private LocalDateTime lastActivityAt;
    
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    
    public AccountResponse() {}
    
    public AccountResponse(String accountId, String accountNumber, AccountType accountType, 
                          AccountStatus status, String currency, MonetaryAmount balance, 
                          MonetaryAmount availableBalance, String accountNickname, String customerId, 
                          String branchId, LocalDateTime createdAt, LocalDateTime updatedAt, 
                          LocalDateTime lastActivityAt, Map<String, String> metadata) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
        this.currency = currency;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.accountNickname = accountNickname;
        this.customerId = customerId;
        this.branchId = branchId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastActivityAt = lastActivityAt;
        this.metadata = metadata;
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
    
    public MonetaryAmount getBalance() {
        return balance;
    }
    
    public void setBalance(MonetaryAmount balance) {
        this.balance = balance;
    }
    
    public MonetaryAmount getAvailableBalance() {
        return availableBalance;
    }
    
    public void setAvailableBalance(MonetaryAmount availableBalance) {
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
        this.metadata = metadata;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountResponse that = (AccountResponse) o;
        return Objects.equals(accountId, that.accountId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
    
    @Override
    public String toString() {
        return "AccountResponse{" +
                "accountId='" + accountId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", status=" + status +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", accountNickname='" + accountNickname + '\'' +
                ", customerId='" + customerId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastActivityAt=" + lastActivityAt +
                '}';
    }
}