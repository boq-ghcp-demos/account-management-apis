package com.banking.accountmanagementapis.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test Configuration for Banking Account Management APIs
 */
@TestConfiguration
@Profile("test")
public class TestConfig {
    
    /**
     * Mock MVC configuration for integration tests
     */
    @Bean
    @Primary
    public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }
}