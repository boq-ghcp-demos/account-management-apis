# Banking Account Management API

Enterprise-grade Java Spring Boot application for comprehensive banking account management with PCI DSS compliance and SOX audit trails.

## 🏗️ Architecture Overview

The application follows a three-tier architecture with comprehensive security, audit logging, and compliance features:

### Key Features

- **Comprehensive Account Management**: Full CRUD operations for banking accounts
- **OAuth2 + JWT Authentication**: Secure authentication with scope-based authorization
- **API Key Authentication**: Additional security layer for enhanced protection
- **Audit Logging**: Complete audit trails for SOX and regulatory compliance
- **PCI DSS Compliance**: Data masking and secure handling of sensitive information
- **Rate Limiting**: Protection against abuse and DoS attacks
- **Response Caching**: 1-minute TTL for balance inquiries for performance optimization
- **Input Validation**: Comprehensive validation and sanitization
- **Error Handling**: Banking-specific error codes following RFC 7807
- **Idempotency**: Safe retry operations with idempotency keys

## 🚀 API Endpoints

### Account Management Endpoints

| Method | Endpoint | Description | Required Scopes |
|--------|----------|-------------|-----------------|
| `GET` | `/v2/accounts` | List customer accounts with filtering and pagination | `accounts:read` |
| `GET` | `/v2/accounts/{accountId}` | Get detailed account information | `accounts:read` |
| `POST` | `/v2/accounts` | Create new account with validation | `accounts:write` |

### Security Requirements

All endpoints require:
- **OAuth2 Authentication**: Valid JWT token with appropriate scopes
- **API Key**: `X-API-Key` header for additional security
- **Customer ID**: `X-Customer-ID` header (must match authenticated user)
- **Request ID**: `X-Request-ID` header for audit tracing

## 🔧 Technology Stack

- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.2** - Enterprise application framework
- **Spring Security 6** - OAuth2 Resource Server with JWT
- **Spring Cache** - Caffeine-based caching for performance
- **Validation API** - Bean validation with custom validators
- **OpenAPI 3** - Comprehensive API documentation
- **SLF4J + Logback** - Structured audit logging
- **Jackson** - JSON processing with security configurations

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.8 or higher
- IDE with Spring Boot support (IntelliJ IDEA recommended)

## 🛠️ Setup and Configuration

### 1. Clone and Build

```bash
git clone <repository-url>
cd account-management-apis
mvn clean install
```

### 2. Environment Configuration

Update `application.properties` with your environment-specific values:

```properties
# OAuth2 Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-auth-server.com/oauth2
spring.security.oauth2.resourceserver.jwt.audiences=banking-api

# Database Configuration (when integrated)
# spring.datasource.url=jdbc:postgresql://localhost:5432/banking_db
# spring.datasource.username=${DB_USERNAME}
# spring.datasource.password=${DB_PASSWORD}
```

### 3. Security Configuration

Configure OAuth2 scopes and API keys according to your security requirements:

- `accounts:read` - Read access to account information
- `accounts:write` - Create and update accounts
- `accounts:delete` - Close/delete accounts
- `accounts:freeze` - Freeze/unfreeze operations
- `audit:read` - Access to audit logs

## 🚦 Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

### Production Mode

```bash
java -jar target/account-management-apis-1.0.0.jar --spring.profiles.active=prod
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Interactive Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### OpenAPI Specification

Raw OpenAPI spec available at: `http://localhost:8080/v3/api-docs`

## 🔒 Security Features

### Authentication & Authorization

- **OAuth2 JWT Tokens**: All endpoints require valid JWT tokens
- **Scope-based Authorization**: Fine-grained permissions using OAuth2 scopes
- **Customer ID Validation**: Ensures customers can only access their own data
- **API Key Authentication**: Additional security layer

### Security Headers

The application automatically adds security headers:
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `X-XSS-Protection: 1; mode=block`
- `Strict-Transport-Security: max-age=31536000; includeSubDomains`
- `Content-Security-Policy: default-src 'self'`

### Data Protection

- **Account Number Masking**: Sensitive data masked in responses (`****1234`)
- **Input Sanitization**: Protection against injection attacks
- **PCI DSS Compliance**: Secure handling of financial data

## 📊 Audit & Compliance

### Audit Logging

All operations are logged with:
- **Activity Type**: What operation was performed
- **Customer ID**: Who performed the operation
- **Request ID**: Unique identifier for request tracing
- **Timestamp**: When the operation occurred
- **Result**: Success/Failure status
- **Details**: Additional context information

### Log Categories

- **AUDIT**: Business operations and account activities
- **SECURITY**: Security events and authorization failures
- **ACCESS**: API access patterns and performance metrics

### Compliance Features

- **SOX Compliance**: 7-year audit trail retention
- **PCI DSS**: Data masking and secure processing
- **Regulatory Reporting**: Structured logs for compliance reporting

## ⚡ Performance Features

### Caching Strategy

- **Balance Inquiries**: 1-minute TTL using Caffeine cache
- **Cache Keys**: Combination of account ID and request parameters
- **Cache Statistics**: Monitoring and metrics available

### Async Processing

- **Non-blocking Operations**: CompletableFuture for improved throughput
- **Thread Pool Configuration**: Optimized for concurrent requests

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn verify
```

### Test Coverage

```bash
mvn jacoco:report
```

## 📈 Monitoring & Observability

### Health Checks

- **Application Health**: `GET /actuator/health`
- **Detailed Health**: Requires authentication
- **Kubernetes Probes**: Liveness and readiness endpoints

### Metrics

- **Prometheus Metrics**: Available at `/actuator/prometheus`
- **JVM Metrics**: Memory, threads, garbage collection
- **Custom Metrics**: API calls, cache hit rates, audit events

### Logging

Structured logging with correlation IDs:
```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "level": "INFO",
  "requestId": "550e8400-e29b-41d4-a716-446655440000",
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "message": "Account created successfully",
  "activityType": "ACCOUNT_CREATED"
}
```

## 🐛 Error Handling

### Banking Error Codes

| Code | Description | HTTP Status |
|------|-------------|-------------|
| `BANK_A001` | Authentication Required | 401 |
| `BANK_A002` | Customer Authorization Failed | 403 |
| `BANK_E001` | Invalid Account Type | 400 |
| `BANK_R001` | Account Not Found | 404 |
| `BANK_C001` | Account Already Exists | 409 |
| `BANK_B001` | Insufficient Funds | 422 |
| `BANK_L001` | Rate Limit Exceeded | 429 |
| `BANK_S001` | Internal System Error | 500 |

### Error Response Format

```json
{
  \"type\": \"https://api.banking.com/problems/account-not-found\",
  \"title\": \"Account Not Found\",
  \"status\": 404,
  \"detail\": \"The account with ID '550e8400-e29b-41d4-a716-446655440000' was not found\",
  \"code\": \"BANK_R001\",
  \"timestamp\": \"2024-01-15T10:30:00Z\",
  \"traceId\": \"987fcdeb-51a2-43d1-9f12-345678901234\"
}
```

## 🔄 Rate Limiting

### Default Limits

- **Per Hour**: 1,000 requests
- **Per Minute**: 100 requests
- **Per Customer**: Individual rate limits applied

### Rate Limit Headers

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1640995200
```

## 🚀 Deployment

### Docker Support

```dockerfile
FROM openjdk:21-jre-slim
COPY target/account-management-apis-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT [\"java\", \"-jar\", \"/app.jar\"]
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: banking-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: banking-api
  template:
    spec:
      containers:
      - name: banking-api
        image: banking-api:latest
        ports:
        - containerPort: 8080
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Follow coding standards and add tests
4. Ensure all tests pass and coverage is maintained
5. Submit a pull request with detailed description

## 📄 License

This project is licensed under the Banking API License. See the LICENSE file for details.

## 📞 Support

For technical support and API questions:
- **Email**: api-support@banking.com
- **Documentation**: [API Documentation Portal]
- **Status Page**: [System Status Page]

---

**⚠️ Important Security Notes:**

1. Never commit sensitive configuration values
2. Use environment variables for secrets
3. Enable HTTPS in production environments
4. Regularly rotate API keys and certificates
5. Monitor audit logs for suspicious activities
6. Follow PCI DSS guidelines for production deployment