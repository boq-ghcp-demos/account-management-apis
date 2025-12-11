package com.banking.accountmanagementapis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration for Banking Account Management API.
 * Provides Swagger UI documentation and API specifications.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                    new Server().url("http://localhost:8081").description("Development Server"),
                    new Server().url("https://api.banking.com").description("Production Server")
                ))
                .info(new Info()
                    .title("Banking Account Management API")
                    .description("Comprehensive Banking Account Management System with enterprise-grade security and compliance features")
                    .version("1.0.0")
                    .contact(new Contact()
                        .name("Banking API Team")
                        .email("api-support@banking.com")
                        .url("https://banking.com/support"))
                    .license(new License()
                        .name("Banking License")
                        .url("https://banking.com/license")));
    }
}