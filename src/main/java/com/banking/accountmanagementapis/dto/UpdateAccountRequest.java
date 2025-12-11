package com.banking.accountmanagementapis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Map;
import java.util.Objects;

/**
 * Update Account Request DTO
 */
public class UpdateAccountRequest {
    
    @Size(max = 50, message = "Account nickname must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_\\.]+$", message = "Account nickname contains invalid characters")
    @JsonProperty("accountNickname")
    private String accountNickname;
    
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    
    public UpdateAccountRequest() {}
    
    public UpdateAccountRequest(String accountNickname, Map<String, String> metadata) {
        this.accountNickname = accountNickname;
        this.metadata = metadata;
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
        UpdateAccountRequest that = (UpdateAccountRequest) o;
        return Objects.equals(accountNickname, that.accountNickname) &&
               Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountNickname, metadata);
    }
    
    @Override
    public String toString() {
        return "UpdateAccountRequest{" +
                "accountNickname='" + accountNickname + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}