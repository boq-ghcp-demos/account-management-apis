# GitHub Copilot for Planning Demo Guide

## 🎯 **Overview**

This demo guide showcases how to leverage GitHub Copilot with Azure DevOps MCP server integration for streamlined project planning and enhancement creation. We'll demonstrate using our BOQ Banking API planning templates to create comprehensive user stories, epics, and tasks in Azure DevOps.

**Demo Duration**: 20-30 minutes  
**Audience**: Product Managers, Scrum Masters, Developers  
**Prerequisites**: Azure DevOps access, GitHub Copilot enabled  
**Repository**: BOQ Account Management APIs  

---

## 📋 **Demo Preparation Checklist**

- [ ] Azure DevOps project setup and accessible
- [ ] GitHub Copilot extension enabled in VS Code
- [ ] MCP Azure DevOps server configured
- [ ] Planning templates available in `/planning-templates/`
- [ ] Demo scenarios prepared
- [ ] Sample data ready

---

## 🎪 **Demo Scenarios**

### **Scenario 1: Creating Enhanced Account Features**

We'll demonstrate creating a comprehensive enhancement for the BOQ banking system using our planning templates and Azure DevOps integration.

#### **Enhancement: Multi-Currency Account Support**

This enhancement will add multi-currency capabilities to our existing account management system.

### **Scenario 2: API Security Enhancement**

Demonstrate planning for advanced security features using our Definition of Done template.

#### **Enhancement: Enhanced Authentication & Authorization**

This will showcase compliance-driven development planning.

---

## 🚀 **Demo Script**

### **Part 1: Setting Up the Enhancement (5 minutes)**

#### **Step 1.1: Initialize Enhancement Planning**

Let's start by creating our enhancement structure using GitHub Copilot and our planning templates.

```markdown
# Enhancement: Multi-Currency Account Support
## Business Context
BOQ customers increasingly need multi-currency account capabilities for international transactions and forex management.

## Using Planning Templates
We'll leverage our existing templates:
- User Story Template: `/planning-templates/user-story-template.md`
- Definition of Done: `/planning-templates/definition-of-done.md`
```

#### **Step 1.2: Generate Epic Using Copilot**

**Prompt for Copilot:**
```
Using the user story template from planning-templates, create an epic for multi-currency account support including:
- Primary epic story
- 5 supporting user stories
- Banking compliance requirements (AUSTRAC, APRA)
- Technical requirements for foreign exchange
- Security considerations for multi-currency transactions
```

**Expected Output Structure:**
```gherkin
Epic: MCAS-001 - Multi-Currency Account Support

As a BOQ customer
I want to maintain multiple currency balances in a single account
So that I can efficiently manage international transactions and reduce forex costs

Features:
- Currency account creation and management
- Real-time exchange rate integration
- Multi-currency transaction processing
- Regulatory compliance for international transfers
- Enhanced reporting for multi-currency accounts
```

### **Part 2: Azure DevOps Integration (10 minutes)**

#### **Step 2.1: Create Epic in Azure DevOps**

**Live Demo Command:**
```bash
# This will be demonstrated using the MCP Azure DevOps integration
# We'll show creating the epic directly from VS Code
```

**Demo Steps:**
1. Use Azure DevOps MCP tools to create the epic
2. Populate with Copilot-generated content
3. Link to planning templates
4. Set up proper categorization and tags

#### **Step 2.2: Generate User Stories with Copilot**

**User Story 1: Currency Account Creation**
```gherkin
User Story: MCAS-002 - Create Multi-Currency Sub-Account

As a BOQ retail customer
I want to create additional currency sub-accounts (USD, EUR, GBP, NZD)
So that I can hold multiple currencies without currency conversion fees

Banking Context:
- customer_type: individual, business
- account_type: multi_currency_savings
- compliance_level: enhanced_dd
- risk_level: medium

Acceptance Criteria:
Given I am a verified BOQ customer with an existing primary account
When I request to add a currency sub-account
Then the system validates my eligibility and creates the sub-account
And applies appropriate regulatory requirements (AUSTRAC SMR, IFTI)
And sets up currency-specific limits and fees

Definition of Done:
- [ ] API endpoints for currency account creation
- [ ] AUSTRAC compliance validation
- [ ] Multi-currency balance display
- [ ] Integration with forex rate provider
- [ ] Unit test coverage >= 90%
- [ ] Security penetration testing complete
```

#### **Step 2.3: Create Work Items in Azure DevOps**

Let's demonstrate creating these items using the MCP integration:

**Live Demo Actions:**
1. Create Epic in Azure DevOps
2. Create User Stories as child items
3. Create technical tasks for implementation
4. Link related items
5. Set up iterations and assignments

### **Part 3: Technical Implementation Planning (8 minutes)**

#### **Step 3.1: Generate Technical Tasks with Copilot**

**Prompt for Copilot:**
```
Based on the multi-currency account user story, generate technical implementation tasks including:
- API design tasks
- Database schema changes
- Integration requirements
- Testing strategies
- Deployment considerations
```

**Expected Technical Tasks:**

1. **API Design Task**
```markdown
Task: MCAS-003 - Design Multi-Currency Account API

Technical Requirements:
- Extend existing account APIs for multi-currency support
- New endpoints: /accounts/{id}/currencies, /accounts/{id}/currencies/{code}
- OpenAPI specification updates
- Backward compatibility maintenance

Acceptance Criteria:
- [ ] OpenAPI spec updated with currency endpoints
- [ ] API versioning strategy implemented
- [ ] Request/response models defined
- [ ] Error handling for invalid currencies
```

2. **Database Schema Task**
```markdown
Task: MCAS-004 - Multi-Currency Database Schema

Technical Requirements:
- New tables: currency_accounts, currency_balances, exchange_rates
- Foreign key relationships to existing account structure
- Audit trail for currency transactions
- Performance optimization for balance calculations

Acceptance Criteria:
- [ ] Database migration scripts created
- [ ] Schema validation tests passing
- [ ] Performance benchmarks meet requirements (<100ms balance retrieval)
- [ ] Backup and recovery procedures updated
```

#### **Step 3.2: Integration with External Services**

**Copilot Prompt:**
```
Generate integration requirements for:
- Real-time forex rate providers
- AUSTRAC reporting for international transfers
- Core banking system updates
- Fraud detection enhancements
```

### **Part 4: Definition of Done Application (5 minutes)**

#### **Step 4.1: Apply DoD Template**

Let's show how our Definition of Done template ensures comprehensive completion:

**Banking-Specific DoD Elements:**
```markdown
✅ **Security & Compliance Checklist for Multi-Currency Feature**

🔒 **Security Requirements**
- [ ] Multi-currency transaction encryption implemented
- [ ] Currency conversion audit trails secure
- [ ] Cross-border transaction monitoring active
- [ ] PCI-DSS compliance for currency data storage

📋 **Regulatory Compliance**
- [ ] AUSTRAC IFTI reporting for international transfers >$10,000
- [ ] SMR (Suspicious Matter Reports) integration for unusual currency activity
- [ ] APRA CPS 234 security controls for multi-currency data
- [ ] Basel III regulatory capital calculations updated

⚡ **Performance & Availability**
- [ ] Real-time forex rate updates (<5 second latency)
- [ ] Multi-currency balance calculations optimized
- [ ] 99.99% availability for currency conversion services
- [ ] Load testing for concurrent multi-currency transactions

🧪 **Testing Excellence**
- [ ] Unit test coverage >= 90% for currency logic
- [ ] Integration tests with forex providers
- [ ] End-to-end testing across all supported currencies
- [ ] Regulatory scenario testing (limits, reporting thresholds)
```

#### **Step 4.2: Link DoD to Azure DevOps Items**

**Demo Action:** Show how to link the DoD checklist items to specific work items in Azure DevOps.

### **Part 5: Advanced Copilot Features (5 minutes)**

#### **Step 5.1: Code Generation from Stories**

**Demonstrate Copilot generating starter code:**

**Prompt:**
```
Generate Spring Boot controller methods for the multi-currency account creation user story, including:
- REST endpoints
- Request validation
- Service layer integration
- Error handling
```

**Expected Generated Code:**
```java
@RestController
@RequestMapping("/api/v2/accounts")
@Validated
public class MultiCurrencyAccountController {
    
    @Autowired
    private MultiCurrencyAccountService accountService;
    
    @PostMapping("/{accountId}/currencies")
    @PreAuthorize("hasPermission(#accountId, 'ACCOUNT', 'MANAGE_CURRENCIES')")
    public ResponseEntity<CurrencyAccountResponse> createCurrencyAccount(
            @PathVariable String accountId,
            @Valid @RequestBody CreateCurrencyAccountRequest request) {
        
        // AUSTRAC compliance validation
        if (requiresEnhancedDueDiligence(request.getCurrencyCode())) {
            auditService.logCurrencyAccountCreation(accountId, request);
        }
        
        CurrencyAccount currencyAccount = accountService.createCurrencyAccount(accountId, request);
        return ResponseEntity.ok(mapToResponse(currencyAccount));
    }
}
```

#### **Step 5.2: Test Generation**

**Show Copilot generating comprehensive tests:**

```java
@Test
@DisplayName("Should create multi-currency account with AUSTRAC compliance")
void shouldCreateMultiCurrencyAccountWithCompliance() {
    // Given
    String accountId = "ACC-123456";
    CreateCurrencyAccountRequest request = CreateCurrencyAccountRequest.builder()
        .currencyCode("USD")
        .initialBalance(BigDecimal.valueOf(50000))
        .build();
    
    // When & Then
    mockMvc.perform(post("/api/v2/accounts/{accountId}/currencies", accountId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currencyCode").value("USD"))
        .andExpect(jsonPath("$.complianceStatus").value("APPROVED"));
    
    // Verify AUSTRAC reporting triggered for high-value USD account
    verify(austracService).submitInitialCurrencyAccountReport(accountId, "USD", BigDecimal.valueOf(50000));
}
```

---

## 🎯 **Demo Talking Points**

### **Key Benefits to Highlight**

1. **Accelerated Planning:**
   - Templates provide consistent structure
   - Copilot generates comprehensive content
   - Reduces planning time by 60-70%

2. **Enhanced Quality:**
   - Banking-specific compliance built-in
   - Comprehensive acceptance criteria
   - Detailed technical requirements

3. **Seamless Integration:**
   - Direct Azure DevOps work item creation
   - Template-driven consistency
   - Automated linking and categorization

4. **Regulatory Compliance:**
   - AUSTRAC requirements embedded
   - APRA standards consideration
   - Security-first approach

### **Advanced Features to Showcase**

1. **AI-Driven Acceptance Criteria:**
   - Context-aware banking scenarios
   - Regulatory requirement inclusion
   - Edge case identification

2. **Technical Task Decomposition:**
   - Architecture considerations
   - Integration requirements
   - Performance criteria

3. **Code Generation Alignment:**
   - Story-to-code traceability
   - Compliance code patterns
   - Test generation

---

## 📈 **Demo Metrics & Outcomes**

### **Before vs After Comparison**

**Traditional Planning (Without Copilot):**
- Epic creation: 2-3 hours
- User story development: 4-6 hours
- Technical task breakdown: 3-4 hours
- Azure DevOps setup: 1-2 hours
- **Total: 10-15 hours**

**With GitHub Copilot + Templates:**
- Epic creation: 15-20 minutes
- User story development: 30-45 minutes
- Technical task breakdown: 20-30 minutes
- Azure DevOps integration: 10-15 minutes
- **Total: 75-110 minutes (85% time reduction)**

### **Quality Improvements**

- **Consistency**: 100% template adherence
- **Compliance**: Automatic regulatory requirement inclusion
- **Completeness**: Comprehensive acceptance criteria
- **Traceability**: Full story-to-code alignment

---

## 🔧 **Technical Setup for Demo**

### **Required Extensions & Tools**

```json
{
  "required_extensions": [
    "GitHub.copilot",
    "GitHub.copilot-chat",
    "ms-vscode.azure-account",
    "ms-azuredevops.azure-pipelines"
  ],
  "mcp_servers": [
    "azure-devops-mcp-server"
  ],
  "demo_files": [
    "planning-templates/user-story-template.md",
    "planning-templates/definition-of-done.md"
  ]
}
```

### **Environment Configuration**

```yaml
# Azure DevOps Configuration
azure_devops:
  organization: "boq-banking"
  project: "account-management-apis"
  work_item_types: ["Epic", "Feature", "User Story", "Task", "Bug"]
  
# GitHub Copilot Settings
copilot:
  suggestions: true
  chat: true
  context_files: ["*.md", "*.yaml", "*.json"]
  
# Demo Data
demo_scenarios:
  - multi_currency_accounts
  - enhanced_authentication
  - regulatory_compliance_updates
```

---

## 🎬 **Demo Script Summary**

### **Opening (2 minutes)**
"Today we'll demonstrate how GitHub Copilot revolutionizes project planning by combining AI assistance with our proven banking templates and seamless Azure DevOps integration."

### **Planning Phase (8 minutes)**
- Show template-driven epic creation
- Demonstrate Copilot-generated user stories
- Highlight banking-specific compliance integration

### **Implementation Phase (8 minutes)**
- Create work items in Azure DevOps
- Generate technical tasks
- Show code generation alignment

### **Quality Assurance (5 minutes)**
- Apply Definition of Done template
- Demonstrate comprehensive testing approach
- Show regulatory compliance validation

### **Closing (2 minutes)**
"We've reduced planning time by 85% while improving quality and compliance. The combination of GitHub Copilot, proven templates, and Azure DevOps integration creates a powerful development acceleration platform."

---

## 🚀 **Next Steps & Follow-Up**

### **Immediate Actions**
1. Set up GitHub Copilot for your team
2. Customize planning templates for your domain
3. Configure Azure DevOps MCP integration
4. Train team members on AI-assisted planning

### **Advanced Implementations**
1. Create custom Copilot prompts for domain-specific requirements
2. Develop automated testing strategies
3. Implement continuous compliance validation
4. Set up metrics and tracking for planning efficiency

### **Measurement & Optimization**
- Track planning time reductions
- Monitor story quality metrics
- Measure defect rates in AI-assisted stories
- Gather team feedback and iterate

---

## 📚 **Additional Resources**

- [BOQ Banking API User Story Template](planning-templates/user-story-template.md)
- [BOQ Definition of Done Checklist](planning-templates/definition-of-done.md)
- [GitHub Copilot Best Practices](https://docs.github.com/en/copilot)
- [Azure DevOps MCP Integration Guide](https://docs.microsoft.com/en-us/azure/devops)

---

**Demo Guide Version**: 1.0  
**Created**: December 2025  
**Last Updated**: December 12, 2025  
**Maintainer**: BOQ Development Team