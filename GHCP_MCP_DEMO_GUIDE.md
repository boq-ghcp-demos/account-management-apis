# GitHub Copilot for Planning using MCP servers Demo Guide

## 🎯 **Overview**

This demo guide showcases how to leverage GitHub Copilot with Azure DevOps MCP server integration for streamlined project planning and enhancement creation. We'll demonstrate using our BOQ Banking API planning templates to create comprehensive user stories, epics, and tasks in Azure DevOps.


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



MarkItDown MCP Server # https://developer.microsoft.com/blog/10-microsoft-mcp-servers-to-accelerate-your-development-workflow
