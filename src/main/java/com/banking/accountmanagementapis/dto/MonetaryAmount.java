package com.banking.accountmanagementapis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Monetary Amount DTO with currency support
 */
public class MonetaryAmount {
    
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative")
    @JsonProperty("amount")
    private BigDecimal amount;
    
    @NotNull(message = "Currency cannot be null")
    @JsonProperty("currency")
    private String currency;
    
    public MonetaryAmount() {}
    
    public MonetaryAmount(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonetaryAmount that = (MonetaryAmount) o;
        return Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    @Override
    public String toString() {
        return "MonetaryAmount{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}