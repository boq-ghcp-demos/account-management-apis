
# Developer Runbook: Account Management API

This runbook provides detailed information for developers and support engineers working with the Account Management API. It covers everything from high-level architecture to operational procedures.

## 1. API Overview & Architecture

### 1.1. API Purpose & Business Context

The Account Management API provides core banking functionalities related to customer accounts.

-   **Business Functionality**:
    -   Create and manage customer accounts.
    -   Retrieve account details and transaction history.
    -   Perform balance inquiries.
    -   Manage beneficiaries.
-   **Integration Points**:
    -   Integrates with the core banking system for account ledger management.
    -   Connects to the customer relationship management (CRM) system for customer data.
    -   Uses a message queue (RabbitMQ) for asynchronous notifications.
-   **Customer Impact & Criticality**: This is a **Tier 1** service. Downtime directly impacts customers' ability to view their account information and perform basic banking operations. High availability (99.99%+) is critical.

### 1.2. Technical Architecture

-   **High-Level System Architecture**:

    ```mermaid
    graph TD
        A[Clients] --> B{API Gateway};
        B --> C[Account Management API];
        C --> D[Core Banking System];
        C --> E[CRM System];
        C --> F[RabbitMQ];
    ```

-   **Data Flow**:
    1.  Client sends a request to the API Gateway.
    2.  The Gateway authenticates the request and forwards it to the Account Management API.
    3.  The API processes the request, interacting with downstream systems (Core Banking, CRM).
    4.  For asynchronous operations, a message is published to RabbitMQ.
    5.  The API returns a response to the client.

-   **Dependencies**:
    -   **Database**: Oracle Database for storing transactional data.
    -   **Message Queue**: RabbitMQ for event-driven notifications.
    -   **Third-Party Services**: Integration with a third-party for KYC verification.

-   **Security Architecture**:
    -   Authentication is handled via OAuth 2.0 with JWT tokens.
    -   All communication is over TLS 1.3.
    -   Sensitive data is encrypted at rest using AES-256.

## 2. Development Environment Setup

### 2.1. Local Development Environment

-   **Required Tools**:
    -   Java JDK 17
    -   Apache Maven 3.8+
    -   Docker for running database and message queue.
    -   IDE: IntelliJ IDEA or VS Code with Java extensions.
-   **Database Setup**:
    -   Run `docker-compose up -d` to start an Oracle DB instance.
    -   Load sample data using the `create_sample_data.ps1` script.
-   **Configuration**:
    -   Copy `application.properties.template` to `application.properties`.
    -   Update database credentials and other local settings.

### 2.2. Testing Environment Configuration

-   **Testing Frameworks**:
    -   **Unit Tests**: JUnit 5, Mockito
    -   **Integration Tests**: Spring Boot Test, Testcontainers
    -   **E2E Tests**: Postman/Newman
-   **Mock Services**:
    -   Use WireMock to mock external dependencies like the core banking system.
-   **Test Data**:
    -   Use `src/test/resources/test-data.sql` for a consistent test dataset.

## 3. API Documentation & Specifications

### 3.1. OpenAPI/Swagger Specifications

The complete API contract is defined in the OpenAPI 3.0 specification.

-   **Location**: `specs/banking-comprehensive-api.yaml`
-   **Content**: Includes all endpoints, request/response schemas, and examples.

### 3.2. Authentication & Authorization

-   **OAuth 2.0/JWT**:
    -   The API uses the Client Credentials grant type for M2M communication.
    -   JWTs are signed with RS256 and have a 15-minute expiry.
-   **API Key Management**: API keys are managed through the developer portal.
-   **RBAC**: Roles (`CUSTOMER`, `ADMIN`) are used to control access to different endpoints.
-   **Rate Limiting**:
    -   **Standard**: 100 requests/minute.
    -   **Elevated**: 500 requests/minute for premium partners.

## 4. Compliance & Security Guidelines

### 4.1. Regulatory Compliance

-   **PCI DSS**: No full cardholder data is stored or processed.
-   **Data Retention**: Customer data is retained for 7 years as per regulatory requirements.
-   **Audit Logging**: All sensitive operations are logged for audit purposes.
-   **GDPR**: Adherence to data privacy and "right to be forgotten" principles.

### 4.2. Security Best Practices

-   **Input Validation**: All incoming data is validated against the OpenAPI schema.
-   **Encryption**:
    -   **In Transit**: TLS 1.3
    -   **At Rest**: AES-256 for sensitive data in the database.
-   **Secrets Management**: Use HashiCorp Vault for managing secrets.
-   **Vulnerability Scanning**: Automated scanning with Snyk is part of the CI/CD pipeline.

## 5. Build & Deployment Procedures

### 5.1. Build Pipeline Configuration

-   **Build Tool**: Maven (`pom.xml`)
-   **CI/CD**: Jenkins
-   **Quality Gates**:
    -   SonarQube for static code analysis.
    -   Snyk for vulnerability scanning.
    -   Code coverage must be >80%.

### 5.2. Deployment Strategies

-   **Environments**: DEV -> UAT -> PROD
-   **Deployment**: Blue-green deployment strategy to ensure zero downtime.
-   **Rollback**: Automated rollback procedures are in place if health checks fail.
-   **Database Migrations**: Handled by Flyway.

## 6. Monitoring & Observability

### 6.1. Application Performance Monitoring (APM)

-   **Tool**: Dynatrace
-   **KPIs**:
    -   Response Time: <200ms (p95)
    -   Error Rate: <0.1%
    -   Throughput: Track requests per minute.

### 6.2. Business Metrics Monitoring

-   **Tool**: Grafana dashboards
-   **Metrics**:
    -   Transaction volume and success rates.
    -   New account creation trends.

### 6.3. Infrastructure Monitoring

-   **Tool**: Prometheus
-   **Metrics**: CPU, memory, disk usage, and network performance.

## 7. Troubleshooting & Incident Response

### 7.1. Common Issues & Solutions

| Error Code | Root Cause                               | Solution                                           |
|------------|------------------------------------------|----------------------------------------------------|
| 401        | Invalid or expired JWT token.            | Refresh the token.                                 |
| 503        | Downstream service (core banking) is down.| Check the status of the core banking system.       |

### 7.2. Incident Response Procedures

-   **Severity**:
    -   **SEV-1**: Major customer impact. On-call engineer paged immediately.
    -   **SEV-2**: Minor impact. Ticket created and addressed within business hours.
-   **Communication**: Use Statuspage for public communication.
-   **Post-Incident Review**: A blameless post-mortem is conducted for every SEV-1.

## 8. Change Management & Versioning

### 8.1. API Versioning Strategy

-   **Strategy**: Semantic Versioning (v1, v2, etc., in the URL).
-   **Backward Compatibility**: Non-breaking changes are allowed in minor versions.
-   **Deprecation**: Deprecated APIs are supported for 6 months with prior notification.

### 8.2. Change Approval Process

-   **Code Review**: At least two approvals are required for every pull request.
-   **ARB/CAB**: Major architectural changes require approval from the Architectural Review Board.

## 9. Business Continuity & Disaster Recovery

### 9.1. Backup & Recovery Procedures

-   **RTO**: 1 hour
-   **RPO**: 5 minutes
-   **Failover**: Automated cross-region failover for the production environment.

### 9.2. Load Testing & Capacity Planning

-   **Load Testing**: Conducted quarterly to ensure the system can handle peak loads.
-   **Capacity Scaling**: The application is designed to scale horizontally.

## 10. Integration Guidelines

### 10.1. External System Integration

-   **Core Banking System**: Use the provided SDK for interactions.
-   **Payment Gateway**: Follow the RESTful API documentation from the payment provider.
-   **Third-Party Services**: All integrations must be fault-tolerant, with proper timeouts and circuit breakers.

