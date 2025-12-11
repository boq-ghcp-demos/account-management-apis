package com.banking.accountmanagementapis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Create Account Request DTO
 */
public class CreateAccountRequest {
    
    @NotNull(message = "Account type is required")
    @JsonProperty("accountType")
    private AccountType accountType;
    
    @NotNull(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 code")
    @JsonProperty("currency")
    private String currency;
    
    @NotNull(message = "Initial deposit is required")
    @JsonProperty("initialDeposit")
    private BigDecimal initialDeposit;
    
    @Valid
    @NotNull(message = "Customer details are required")
    @JsonProperty("customerDetails")
    private CustomerDetails customerDetails;
    
    @Size(max = 50, message = "Account nickname must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_\\.]+$", message = "Account nickname contains invalid characters")
    @JsonProperty("accountNickname")
    private String accountNickname;
    
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    
    public CreateAccountRequest() {}
    
    public CreateAccountRequest(AccountType accountType, String currency, BigDecimal initialDeposit, 
                               CustomerDetails customerDetails, String accountNickname, Map<String, String> metadata) {
        this.accountType = accountType;
        this.currency = currency;
        this.initialDeposit = initialDeposit;
        this.customerDetails = customerDetails;
        this.accountNickname = accountNickname;
        this.metadata = metadata;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }
    
    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
    
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }
    
    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
    
    public String getAccountNickname() {
        return accountNickname;
    }
    
    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
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
        CreateAccountRequest that = (CreateAccountRequest) o;
        return accountType == that.accountType &&
               Objects.equals(currency, that.currency) &&
               Objects.equals(initialDeposit, that.initialDeposit) &&
               Objects.equals(customerDetails, that.customerDetails) &&
               Objects.equals(accountNickname, that.accountNickname) &&
               Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountType, currency, initialDeposit, customerDetails, accountNickname, metadata);
    }
    
    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "accountType=" + accountType +
                ", currency='" + currency + '\'' +
                ", initialDeposit=" + initialDeposit +
                ", customerDetails=" + customerDetails +
                ", accountNickname='" + accountNickname + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}