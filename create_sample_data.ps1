# Sample Data Creation Script for Banking API

Write-Host "Creating sample accounts..." -ForegroundColor Yellow

# Wait for application to start
Start-Sleep -Seconds 3

# Account 1 - John Smith - Checking Account
$account1 = @{
    accountType = "CHECKING"
    currency = "USD"
    initialDeposit = 1500.00
    customerDetails = @{
        firstName = "John"
        lastName = "Smith"
        email = "john.smith@email.com"
        phoneNumber = "+1234567890"
        address = "123 Main Street, New York, NY 10001"
    }
    accountNickname = "Primary Checking"
    metadata = @{
        preferredBranch = "NYC-001"
        accountPurpose = "primary"
    }
}

$headers1 = @{
    "Content-Type" = "application/json"
    "X-Customer-ID" = "customer-001"
}

try {
    $response1 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($account1 | ConvertTo-Json -Depth 3) -Headers $headers1
    Write-Host "✓ Created Account 1 - John Smith Checking Account" -ForegroundColor Green
    Write-Host "  Account ID: $($response1.accountId)" -ForegroundColor Cyan
    Write-Host "  Account Number: $($response1.accountNumber)" -ForegroundColor Cyan
    Write-Host "  Balance: $($response1.balance.amount) $($response1.balance.currency)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Failed to create Account 1: $($_.Exception.Message)" -ForegroundColor Red
}

# Account 2 - Jane Doe - Savings Account
$account2 = @{
    accountType = "SAVINGS"
    currency = "USD"
    initialDeposit = 5000.00
    customerDetails = @{
        firstName = "Jane"
        lastName = "Doe"
        email = "jane.doe@email.com"
        phoneNumber = "+1987654321"
        address = "456 Oak Avenue, Los Angeles, CA 90210"
    }
    accountNickname = "Emergency Fund"
    metadata = @{
        preferredBranch = "LA-002"
        accountPurpose = "savings"
        interestRate = "2.5"
    }
}

$headers2 = @{
    "Content-Type" = "application/json"
    "X-Customer-ID" = "customer-002"
}

try {
    $response2 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($account2 | ConvertTo-Json -Depth 3) -Headers $headers2
    Write-Host "✓ Created Account 2 - Jane Doe Savings Account" -ForegroundColor Green
    Write-Host "  Account ID: $($response2.accountId)" -ForegroundColor Cyan
    Write-Host "  Account Number: $($response2.accountNumber)" -ForegroundColor Cyan
    Write-Host "  Balance: $($response2.balance.amount) $($response2.balance.currency)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Failed to create Account 2: $($_.Exception.Message)" -ForegroundColor Red
}

# Account 3 - Bob Johnson - Money Market Account
$account3 = @{
    accountType = "MONEY_MARKET"
    currency = "USD"
    initialDeposit = 10000.00
    customerDetails = @{
        firstName = "Robert"
        lastName = "Johnson"
        email = "bob.johnson@email.com"
        phoneNumber = "+1555666777"
        address = "789 Pine Street, Chicago, IL 60601"
    }
    accountNickname = "Investment Fund"
    metadata = @{
        preferredBranch = "CHI-003"
        accountPurpose = "investment"
        riskLevel = "moderate"
    }
}

$headers3 = @{
    "Content-Type" = "application/json"
    "X-Customer-ID" = "customer-003"
}

try {
    $response3 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($account3 | ConvertTo-Json -Depth 3) -Headers $headers3
    Write-Host "✓ Created Account 3 - Bob Johnson Money Market Account" -ForegroundColor Green
    Write-Host "  Account ID: $($response3.accountId)" -ForegroundColor Cyan
    Write-Host "  Account Number: $($response3.accountNumber)" -ForegroundColor Cyan
    Write-Host "  Balance: $($response3.balance.amount) $($response3.balance.currency)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Failed to create Account 3: $($_.Exception.Message)" -ForegroundColor Red
}

# Account 4 - Alice Brown - Multiple accounts for same customer as John Smith
$account4 = @{
    accountType = "SAVINGS"
    currency = "USD"
    initialDeposit = 2500.00
    customerDetails = @{
        firstName = "John"
        lastName = "Smith"
        email = "john.smith@email.com"
        phoneNumber = "+1234567890"
        address = "123 Main Street, New York, NY 10001"
    }
    accountNickname = "Vacation Fund"
    metadata = @{
        preferredBranch = "NYC-001"
        accountPurpose = "vacation"
        targetAmount = "5000"
    }
}

$headers4 = @{
    "Content-Type" = "application/json"
    "X-Customer-ID" = "customer-001"  # Same customer as account 1
}

try {
    $response4 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($account4 | ConvertTo-Json -Depth 3) -Headers $headers4
    Write-Host "✓ Created Account 4 - John Smith Savings Account" -ForegroundColor Green
    Write-Host "  Account ID: $($response4.accountId)" -ForegroundColor Cyan
    Write-Host "  Account Number: $($response4.accountNumber)" -ForegroundColor Cyan
    Write-Host "  Balance: $($response4.balance.amount) $($response4.balance.currency)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Failed to create Account 4: $($_.Exception.Message)" -ForegroundColor Red
}

# Account 5 - Maria Garcia - Certificate of Deposit
$account5 = @{
    accountType = "CERTIFICATE_DEPOSIT"
    currency = "USD"
    initialDeposit = 15000.00
    customerDetails = @{
        firstName = "Maria"
        lastName = "Garcia"
        email = "maria.garcia@email.com"
        phoneNumber = "+1444555666"
        address = "321 Elm Street, Miami, FL 33101"
    }
    accountNickname = "5-Year CD"
    metadata = @{
        preferredBranch = "MIA-004"
        accountPurpose = "long-term-savings"
        maturityDate = "2030-12-12"
        interestRate = "4.2"
    }
}

$headers5 = @{
    "Content-Type" = "application/json"
    "X-Customer-ID" = "customer-004"
}

try {
    $response5 = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($account5 | ConvertTo-Json -Depth 3) -Headers $headers5
    Write-Host "✓ Created Account 5 - Maria Garcia Certificate Deposit" -ForegroundColor Green
    Write-Host "  Account ID: $($response5.accountId)" -ForegroundColor Cyan
    Write-Host "  Account Number: $($response5.accountNumber)" -ForegroundColor Cyan
    Write-Host "  Balance: $($response5.balance.amount) $($response5.balance.currency)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Failed to create Account 5: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Sample Data Creation Complete ===" -ForegroundColor Yellow

# Test getting all accounts for customer-001
Write-Host "`nTesting API - Getting accounts for customer-001:" -ForegroundColor Yellow
try {
    $getHeaders = @{
        "X-Customer-ID" = "customer-001"
    }
    $customerAccounts = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method GET -Headers $getHeaders
    Write-Host "✓ Customer 001 has $($customerAccounts.content.Count) accounts" -ForegroundColor Green
    foreach($account in $customerAccounts.content) {
        Write-Host "  - $($account.accountNickname): $($account.balance.amount) $($account.balance.currency)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Failed to retrieve accounts: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nYou can view the API documentation at: http://localhost:8081/swagger-ui/index.html" -ForegroundColor Green
Write-Host "Application is running on: http://localhost:8081" -ForegroundColor Green