package com.banking.accountmanagementapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for Banking Account Management APIs.
 * 
 * Simple version for initial testing and development.
 */
@SpringBootApplication
public class AccountManagementApisApplication {

    public static void main(String[] args) {
        System.out.println("Starting Banking Account Management APIs...");
        SpringApplication.run(AccountManagementApisApplication.class, args);
    }
}