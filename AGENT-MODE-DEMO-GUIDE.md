
## Exercise 1: Generate documentation from OpenAPI Specification

```
Create a comprehensive OpenAPI 3.0+ specification contract ONLY for enterprise banking loan management APIs

## Contract Generation Requirements:
- **Target Output**: [specs/banking-loan-management.yaml] - Complete OpenAPI 3.0+ specification
- **Compliance Standards**: Basel III, PCI DSS, GDPR, PSD2, Open Banking API Standards
- **Contract Focus**: API interface definition, schemas, security, and error specifications ONLY

## OpenAPI Specification Structure:

### 1. **API Metadata & Information**
- API title, version (semantic versioning), description
- Contact information, license details, terms of service URL
- Server definitions with environment-specific URLs
- External documentation links
- Tags for endpoint categorization

### 2. **Path Definitions & Operations**
- **Core Loan Operations**: [HTTP_METHOD] [ENDPOINT_PATH]
  - `/loans` - Loan origination and management
  - `/loans/{loanId}/payments` - Payment processing
  - `/loans/{loanId}/collateral` - Collateral management
  - `/credit-assessments` - Credit evaluation endpoints
  - `/loan-products` - Product catalog endpoints
- **HTTP Methods**: GET, POST, PUT, PATCH, DELETE with appropriate usage
- **Operation IDs**: Unique identifiers for each operation
- **Request/Response Specifications**: Complete parameter and body definitions

### 3. **Schema Definitions (Components)**
- **Core Banking Entities**:
  - `LoanApplication`: Application data structure
  - `Loan`: Complete loan object with all properties
  - `Customer`: Customer profile and KYC data
  - `Collateral`: Asset and valuation information
  - `Payment`: Payment transaction structure
  - `CreditAssessment`: Credit scoring and risk data
- **Data Types**: Precise decimal for monetary amounts, ISO date formats, currency codes
- **Validation Rules**: Banking-specific constraints (account numbers, routing codes, regulatory limits)
- **Enumerations**: Loan status, product types, risk categories, payment frequencies

### 4. **Security Scheme Definitions**
- **OAuth 2.0 Specification**: Authorization Code + PKCE flow definition
- **API Key Schema**: Header-based API key authentication
- **Required Security Headers**: 
  - `X-Request-ID`: Request correlation identifier
  - `X-Customer-ID`: Customer context identifier
  - `X-Channel-ID`: Channel identification
  - `Authorization`: Bearer token specification
- **Scopes Definition**: Fine-grained permission scopes for banking operations

### 5. **Request/Response Examples**
- **Complete Example Payloads**: Representative data for all operations
- **Multiple Scenarios**: Success cases, validation errors, business rule violations
- **Currency Handling**: Multi-currency examples with proper formatting
- **Date/Time Examples**: Timezone-aware datetime representations
- **Pagination Examples**: Cursor-based and offset-based pagination samples

### 6. **Error Response Specifications**
- **Banking Error Schema**: RFC 7807 Problem Details format
- **Error Code Taxonomy**:
  - `LOAN_VALIDATION_ERROR`: Input validation failures
  - `CREDIT_INSUFFICIENT`: Credit worthiness issues
  - `REGULATORY_VIOLATION`: Compliance rule breaches
  - `BUSINESS_RULE_VIOLATION`: Banking policy violations
- **HTTP Status Code Mapping**: 400, 401, 403, 404, 409, 422, 429, 500 with banking context
- **Localized Error Messages**: Multi-language error response support

### 7. **Parameter Specifications**
- **Path Parameters**: Resource identifiers with format validation
- **Query Parameters**: Filtering, sorting, pagination with banking-specific options
- **Header Parameters**: Required and optional headers with validation rules
- **Request Body Parameters**: Complete schema references with validation
- **Parameter Examples**: Representative values for all parameter types

### 8. **Response Schema Definitions**
- **Success Response Structures**: Complete data models for all operations
- **Pagination Metadata**: Links, cursors, total counts, page information
- **HATEOAS Links**: Hypermedia controls for state transitions
- **Response Headers**: ETag, cache-control, rate-limit headers
- **Content Types**: JSON, HAL+JSON media type specifications

### 9. **Banking-Specific Constraints**
- **Monetary Precision**: Decimal constraints for currency amounts
- **Regulatory Limits**: Maximum loan amounts, interest rate bounds
- **Account Format Validation**: IBAN, SWIFT, routing number patterns
- **Customer Data Validation**: SSN, tax ID, phone number formats
- **Geographic Restrictions**: Country codes, regulatory jurisdiction constraints
- **Temporal Constraints**: Business hours, processing windows, settlement dates

### 10. **Contract Validation Rules**
- **Schema Consistency**: Cross-reference validation between schemas
- **Endpoint Completeness**: CRUD operations for all resources
- **Security Coverage**: Authentication/authorization on all endpoints
- **Error Completeness**: Error responses for all failure scenarios
- **Example Validity**: All examples validate against their schemas
- **Backward Compatibility**: Version compatibility considerations

```

## Exercise 2: Enterprise .NET API Workspace Scaffolding for Banking Loan Management

```
Create a complete .NET 8.0 Web API workspace scaffolding for enterprise banking loan management system with all required packages and project structure - NO BUSINESS LOGIC IMPLEMENTATION

## Project Structure Requirements:
- **Framework**: .NET 8.0 Web API (LTS)
- **Project Type**: ASP.NET Core Web API with Clean Architecture
- **Database**: Entity Framework Core with SQL Server provider
- **Target Output**: Complete runnable project skeleton with empty method implementations

## Required NuGet Packages & Dependencies:

### 1. **Core Framework Packages**
```xml
<PackageReference Include="Microsoft.AspNetCore.OpenApi" Version="8.0.0" />
<PackageReference Include="Swashbuckle.AspNetCore" Version="6.5.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="8.0.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="8.0.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="8.0.0" />


### 2. **Authentication & Security**
```xml
<PackageReference Include="Microsoft.AspNetCore.Authentication.JwtBearer" Version="8.0.0" />
<PackageReference Include="Microsoft.AspNetCore.Authentication.OpenIdConnect" Version="8.0.0" />
<PackageReference Include="Microsoft.Identity.Web" Version="2.15.2" />
<PackageReference Include="System.IdentityModel.Tokens.Jwt" Version="7.0.3" />


### 3. **Validation & Serialization**
```xml
<PackageReference Include="FluentValidation.AspNetCore" Version="11.3.0" />
<PackageReference Include="AutoMapper.Extensions.Microsoft.DependencyInjection" Version="12.0.1" />
<PackageReference Include="Newtonsoft.Json" Version="13.0.3" />


### 4. **Logging & Monitoring**
```xml
<PackageReference Include="Serilog.AspNetCore" Version="8.0.0" />
<PackageReference Include="Serilog.Sinks.Console" Version="5.0.1" />
<PackageReference Include="Serilog.Sinks.File" Version="5.0.0" />
<PackageReference Include="Microsoft.ApplicationInsights.AspNetCore" Version="2.21.0" />


### 5. **Health Checks & Resilience**
```xml
<PackageReference Include="Microsoft.Extensions.Diagnostics.HealthChecks.EntityFrameworkCore" Version="8.0.0" />
<PackageReference Include="AspNetCore.HealthChecks.SqlServer" Version="7.0.0" />
<PackageReference Include="Polly.Extensions.Http" Version="3.0.0" />


### 6. **API Versioning & Documentation**
```xml
<PackageReference Include="Microsoft.AspNetCore.Mvc.Versioning" Version="5.1.0" />
<PackageReference Include="Microsoft.AspNetCore.Mvc.Versioning.ApiExplorer" Version="5.1.0" />
<PackageReference Include="NSwag.AspNetCore" Version="14.0.0" />


### 7. **Testing Packages (Test Projects)**
```xml
<PackageReference Include="Microsoft.NET.Test.Sdk" Version="17.8.0" />
<PackageReference Include="xunit" Version="2.6.2" />
<PackageReference Include="xunit.runner.visualstudio" Version="2.4.5" />
<PackageReference Include="Microsoft.AspNetCore.Mvc.Testing" Version="8.0.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
<PackageReference Include="Moq" Version="4.20.69" />
<PackageReference Include="FluentAssertions" Version="6.12.0" />


## Project Structure Scaffold:

### 1. **Solution Structure**

BankingLoanManagement.sln
├── src/
│   ├── BankingLoanManagement.API/           # Web API Layer
│   ├── BankingLoanManagement.Core/          # Domain Layer
│   ├── BankingLoanManagement.Infrastructure/ # Data Access Layer
│   └── BankingLoanManagement.Application/   # Application Services Layer
├── tests/
│   ├── BankingLoanManagement.UnitTests/
│   ├── BankingLoanManagement.IntegrationTests/
│   └── BankingLoanManagement.API.Tests/
└── docs/
    └── api/


### 2. **API Layer (Controllers & Configuration)**
- **Empty Controller Classes**:
  - `LoansController.cs` - Loan management endpoints (empty methods)
  - `LoanProductsController.cs` - Product catalog endpoints (empty methods)
  - `CreditAssessmentsController.cs` - Credit evaluation endpoints (empty methods)
  - `PaymentsController.cs` - Payment processing endpoints (empty methods)
  - `CollateralController.cs` - Collateral management endpoints (empty methods)
- **Configuration Files**:
  - `Program.cs` - Application bootstrap with DI container setup
  - `appsettings.json` - Base configuration
  - `appsettings.Development.json` - Development overrides
  - `appsettings.Production.json` - Production overrides

### 3. **Domain Layer (Core Entities)**
- **Empty Entity Classes**:
  - `Loan.cs` - Core loan entity (properties only, no methods)
  - `Customer.cs` - Customer entity (properties only)
  - `LoanProduct.cs` - Loan product entity (properties only)
  - `Payment.cs` - Payment entity (properties only)
  - `Collateral.cs` - Collateral entity (properties only)
  - `CreditAssessment.cs` - Credit assessment entity (properties only)
- **Enum Definitions**:
  - `LoanStatus.cs`, `PaymentStatus.cs`, `RiskCategory.cs`
- **Empty Interface Definitions** (no implementations)

### 4. **Infrastructure Layer (Data Access)**
- **Empty Repository Classes**:
  - `LoanRepository.cs` - Empty CRUD methods with TODO comments
  - `CustomerRepository.cs` - Empty CRUD methods with TODO comments
  - `PaymentRepository.cs` - Empty CRUD methods with TODO comments
- **DbContext Configuration**:
  - `LoanManagementDbContext.cs` - Entity configurations (empty DbSets)
  - **Migration Setup**: Initial migration with empty tables

### 5. **Application Layer (Services)**
- **Empty Service Classes**:
  - `LoanService.cs` - Business logic methods (empty with TODO comments)
  - `CreditAssessmentService.cs` - Credit evaluation methods (empty)
  - `PaymentService.cs` - Payment processing methods (empty)
- **DTOs and Mapping Profiles**:
  - Request/Response DTOs (properties only)
  - AutoMapper profiles (empty mapping configurations)

## Middleware & Configuration Setup:

### 1. **Security Middleware**
- JWT Bearer authentication setup (configuration only)
- CORS policy configuration
- API key validation middleware (empty implementation)
- Rate limiting middleware setup

### 2. **Logging Configuration**
- Serilog configuration with console and file sinks
- Application Insights integration setup
- Structured logging format configuration

### 3. **Health Checks**
- Database connectivity health check
- External service health checks (empty implementations)
- Custom business logic health checks (empty)

### 4. **API Documentation**
- Swagger/OpenAPI configuration
- API versioning setup
- XML documentation generation setup

## Essential Configuration Files:

### 1. **.gitignore File**
```gitignore
## .NET Core
bin/
obj/
out/
*.user
*.suo
*.cache
*.docstates
[Dd]ebug/
[Rr]elease/
x64/
x86/
[Aa][Rr][Mm]/
[Aa][Rr][Mm]64/
bld/
[Bb]in/
[Oo]bj/
[Ll]og/

## Visual Studio
.vs/
*.userprefs
*.pidb
*.booproj
*~
*.swp
*.DS_Store

## Database
*.mdf
*.ldf
*.ndf
*.db
*.sqlite
*.sqlite3

## Logs
logs/
*.log

## Secrets
appsettings.*.json
!appsettings.json
!appsettings.Development.json
secrets.json
.env

## Build results
[Dd]ebug/
[Dd]ebugPublic/
[Rr]elease/
[Rr]eleases/
x64/
x86/
build/
bld/
[Bb]in/
[Oo]bj/

## NuGet
*.nupkg
packages/


### 2. **Development Tools Configuration**
- `launchSettings.json` - Development server configuration
- `global.json` - SDK version pinning
- `.editorconfig` - Code formatting rules

## Startup Validation Requirements:

### 1. **Application Startup Success**
- All services register correctly in DI container
- Database connection string validates (but no actual DB required)
- All middleware pipeline configures without errors
- Swagger UI loads successfully at `/swagger`

### 2. **Health Check Endpoints**
- `/health` - Returns 200 OK with basic system status

### 3. **API Endpoint Accessibility**
- All controller endpoints return HTTP 200 with placeholder responses
- Authentication endpoints return appropriate HTTP 401/403 responses
- API versioning works correctly (v1, v2 endpoint routing)

## Placeholder Implementation Guidelines:

### 1. **Controller Methods**
```csharp
// Example structure - all methods should return NotImplementedException or placeholder data
[HttpGet]
public async Task<IActionResult> GetLoans()
{
    // TODO: Implement loan retrieval logic
    throw new NotImplementedException("Loan retrieval not yet implemented");
}


### 2. **Service Methods**
```csharp
// All service methods should have meaningful signatures but empty implementations
public async Task<LoanDto> CreateLoanAsync(CreateLoanRequest request)
{
    // TODO: Implement loan creation business logic
    // TODO: Validate input parameters
    // TODO: Apply business rules
    // TODO: Save to database
    throw new NotImplementedException("Loan creation logic pending implementation");
}


### 3. **Repository Methods**
```csharp
// Repository methods should have proper async signatures
public async Task<Loan> GetByIdAsync(Guid loanId)
{
    // TODO: Implement database query
    // TODO: Add logging
    // TODO: Handle not found scenarios
    return await Task.FromResult<Loan>(null);
}


## Success Criteria:
- ✅ Project compiles without errors
- ✅ Application starts and runs on first launch
- ✅ Swagger UI accessible and displays all endpoints
- ✅ All NuGet packages compatible and restore successfully
- ✅ Health checks respond correctly
- ✅ No business logic implemented (placeholder methods only)
- ✅ Complete project structure with all layers
- ✅ .gitignore configured for .NET projects
- ✅ All configuration files present and valid
```

## Exercise 3: Enterprise Banking Loan Management API - Business Logic Implementation

```
Implement comprehensive business logic for the enterprise banking loan management API based on the scaffolded workspace from Exercise 2

## Implementation Scope:
- **Build Upon**: Scaffolded .NET 8.0 Web API project structure
- **Focus**: Complete business logic implementation with banking domain expertise
- **Compliance**: Regulatory requirements (Basel III, PCI DSS, GDPR compliance)
- **Quality**: Enterprise-grade code with error handling, logging, and validation

## Core Business Logic Implementation:

### 1. **Loan Origination Workflow**
- **Loan Application Processing**:
  - Customer eligibility verification
  - Income and employment verification
  - Credit bureau integration and scoring
  - Debt-to-income ratio calculations
  - Automated decision engine (approve/decline/manual review)
- **Document Management**:
  - Required document checklist validation
  - Document upload and verification workflows
  - Digital signature integration
- **Regulatory Compliance Checks**:
  - Anti-money laundering (AML) screening
  - Know Your Customer (KYC) verification
  - OFAC sanctions list checking

### 2. **Credit Assessment Engine**
- **Credit Scoring Implementation**:
  - Multiple credit bureau API integration (Experian, Equifax, TransUnion)
  - Custom risk scoring models
  - Credit history analysis and trending
  - Bankruptcy and foreclosure impact assessment
- **Risk-Based Pricing**:
  - Interest rate calculation based on risk profile
  - Dynamic pricing models with market rate integration
  - Loan term optimization based on customer profile
- **Underwriting Rules Engine**:
  - Configurable business rules for different loan products
  - Automated underwriting decisions
  - Exception handling for manual underwriter review

### 3. **Collateral Management System**
- **Asset Valuation**:
  - Automated Valuation Model (AVM) integration
  - Third-party appraisal management
  - Market value monitoring and alerts
- **Collateral Types Support**:
  - Real estate (residential/commercial)
  - Vehicles (auto loans)
  - Securities and investments
  - Business assets and inventory
- **Insurance Requirements**:
  - Insurance verification and tracking
  - Premium payment monitoring
  - Claim processing integration

### 4. **Payment Processing & Servicing**
- **Payment Calculation Engine**:
  - Amortization schedule generation
  - Interest calculation (simple, compound, variable rates)
  - Payment allocation logic (principal, interest, fees, escrow)
- **Payment Processing**:
  - ACH payment processing with multiple retry logic
  - Credit card payment integration
  - Wire transfer handling
  - Payment reversal and adjustment processing
- **Delinquency Management**:
  - Automated late fee calculation
  - Grace period handling
  - Collection workflow automation
  - Loss mitigation programs

### 5. **Loan Product Catalog Management**
- **Product Configuration**:
  - Loan product setup with terms and conditions
  - Interest rate management (fixed, variable, hybrid)
  - Fee structure configuration
  - Eligibility criteria definition
- **Rate Management**:
  - Prime rate integration and tracking
  - Market-based rate adjustments
  - Promotional rate campaigns
- **Compliance Validation**:
  - Truth in Lending Act (TILA) disclosure generation
  - Annual Percentage Rate (APR) calculations
  - Fair lending compliance monitoring

## Technical Implementation Requirements:

### 1. **Data Validation & Business Rules**
- **Input Validation**:
  ```csharp
  // Comprehensive validation using FluentValidation
  public class CreateLoanRequestValidator : AbstractValidator<CreateLoanRequest>
  {
      public CreateLoanRequestValidator()
      {
          RuleFor(x => x.LoanAmount)
              .GreaterThan(0).WithMessage("Loan amount must be positive")
              .LessThanOrEqualTo(10000000).WithMessage("Loan amount exceeds maximum limit");
          
          RuleFor(x => x.CustomerSSN)
              .Matches(@"^\d{3}-\d{2}-\d{4}$").WithMessage("Invalid SSN format");
              
          // TODO: Implement remaining validation rules
      }
  }
  

- **Business Rule Engine**:
  - Configurable rules for loan approval criteria
  - Dynamic rule evaluation with audit trails
  - Rule versioning and rollback capabilities

### 2. **External API Integrations**
- **Credit Bureau APIs**:
  ```csharp
  public interface ICreditBureauService
  {
      Task<CreditReport> GetCreditReportAsync(string ssn, string firstName, string lastName);
      Task<CreditScore> GetCreditScoreAsync(string ssn);
      Task<bool> VerifyIdentityAsync(IdentityVerificationRequest request);
  }
 

- **Core Banking System Integration**:
  - Customer account verification
  - Deposit account integration
  - Real-time balance inquiries
  - Transaction posting and monitoring

- **Payment Gateway Integration**:
  - Multi-provider payment processing
  - Tokenization for PCI compliance
  - Recurring payment setup
  - Fraud detection integration

### 3. **Audit Logging & Compliance**
- **Comprehensive Audit Trail**:
  ```csharp
  public class AuditService : IAuditService
  {
      public async Task LogAsync(AuditEntry entry)
      {
          // TODO: Implement structured audit logging
          // - User identification and authentication context
          // - Action performed with before/after values
          // - Timestamp with timezone
          // - IP address and session information
          // - Regulatory compliance markers
      }
  }
  

- **Data Privacy **:
  - Personal data encryption at rest and in transit
  - Right to be forgotten implementation
  - Consent management and tracking
  - Data retention policy enforcement

### 4. **Error Handling & Resilience**
- **Banking-Specific Error Handling**:
  ```csharp
  public class BankingExceptionMiddleware
  {
      public async Task InvokeAsync(HttpContext context, RequestDelegate next)
      {
          try
          {
              await next(context);
          }
          catch (InsufficientCreditScoreException ex)
          {
              // TODO: Handle credit-related errors with proper HTTP status
              // TODO: Log for regulatory reporting
              // TODO: Generate customer-friendly error message
          }
          catch (RegulatoryComplianceException ex)
          {
              // TODO: Handle compliance violations
              // TODO: Trigger compliance team notifications
          }
      }
  }
  

- **Circuit Breaker Pattern**:
  - External service failure handling
  - Fallback mechanisms for critical operations
  - Service degradation strategies

### 5. **Security Implementation**
- **Customer Authentication & Authorization**:
  ```csharp
  [Authorize]
  [ApiController]
  public class LoansController : ControllerBase
  {
      [HttpPost]
      [RequiredScope("loans:create")]
      public async Task<IActionResult> CreateLoan([FromBody] CreateLoanRequest request)
      {
          // TODO: Validate customer context from JWT token
          // TODO: Ensure customer can only access their own data
          // TODO: Log all sensitive operations for audit
      }
  }


- **Data Protection**:
  - Field-level encryption for PII
  - Tokenization for sensitive financial data
  - Key management and rotation
  - Secure communication protocols

## Database Implementation:

### 1. **Entity Framework Configuration**
- **Complex Relationships**:
  ```csharp
  public class LoanManagementDbContext : DbContext
  {
      protected override void OnModelCreating(ModelBuilder modelBuilder)
      {
          // TODO: Configure entity relationships
          // TODO: Set up indexes for performance
          // TODO: Configure decimal precision for monetary fields
          // TODO: Add soft delete functionality
          // TODO: Configure audit trail tables
      }
  }
 



### 2. **Repository Pattern Implementation**
- **Generic Repository with Banking Extensions**:
  ```csharp
  public class LoanRepository : Repository<Loan>, ILoanRepository
  {
      public async Task<IEnumerable<Loan>> GetLoansByCustomerIdAsync(Guid customerId)
      {
          // TODO: Implement efficient customer loan queries
          // TODO: Include necessary navigation properties
          // TODO: Apply business-specific filters
          // TODO: Add logging and performance monitoring
      }
  }

## Testing Implementation Requirements:

### 1. **Unit Testing**
- **Business Logic Testing**:
  ```csharp
  public class LoanServiceTests
  {
      [Fact]
      public async Task CreateLoanAsync_ValidRequest_ShouldCreateLoan()
      {
          // TODO: Test loan creation with valid inputs
          // TODO: Verify business rule application
          // TODO: Assert audit logging occurs
      }
      
      [Fact]
      public async Task CreateLoanAsync_InsufficientCredit_ShouldThrowException()
      {
          // TODO: Test credit rejection scenarios
          // TODO: Verify proper error handling
      }
  }
  

### 2. **Integration Testing**
- **Unit Tests**
- **Test Data Generation**



## Success Criteria:
- ✅ Complete business logic implemented for all loan management workflows
- ✅ All regulatory compliance requirements addressed
- ✅ Comprehensive error handling and audit logging
- ✅ External API integrations functional with fallback mechanisms
- ✅ Database operations optimized with proper indexing
- ✅ Security measures implemented (authentication, authorization, encryption)
- ✅ Unit and integration tests with 80%+ code coverage
- ✅ Performance benchmarks met (API response times <500ms)
- ✅ All banking business rules correctly implemented
- ✅ Production-ready logging and monitoring in place
```



