# Banking API Test Script - Demonstrating Sample Data

Write-Host "🏦 Banking Account Management API Testing" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow

# Wait for application to be ready
Start-Sleep -Seconds 2

Write-Host "`n📋 Testing API Endpoints with Sample Data..." -ForegroundColor Cyan

# Test 1: Get all accounts for customer-001 (John Smith)
Write-Host "`n1️⃣  Testing: Get all accounts for customer-001 (John Smith)" -ForegroundColor Green
try {
    $headers = @{"X-Customer-ID" = "customer-001"}
    $johnAccounts = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method GET -Headers $headers
    
    Write-Host "✅ Found $($johnAccounts.content.Count) accounts for customer-001:" -ForegroundColor Green
    foreach($account in $johnAccounts.content) {
        Write-Host "   📊 $($account.accountNickname) ($($account.accountType))" -ForegroundColor White
        Write-Host "      💰 Balance: $($account.balance.amount) $($account.balance.currency)" -ForegroundColor Cyan
        Write-Host "      🏠 Branch: $($account.branchId)" -ForegroundColor Gray
        Write-Host "      🔗 Account: $($account.accountNumber)" -ForegroundColor Gray
        Write-Host ""
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Get all accounts for customer-002 (Jane Doe)
Write-Host "`n2️⃣  Testing: Get all accounts for customer-002 (Jane Doe)" -ForegroundColor Green
try {
    $headers = @{"X-Customer-ID" = "customer-002"}
    $janeAccounts = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method GET -Headers $headers
    
    Write-Host "✅ Found $($janeAccounts.content.Count) accounts for customer-002:" -ForegroundColor Green
    foreach($account in $janeAccounts.content) {
        Write-Host "   📊 $($account.accountNickname) ($($account.accountType))" -ForegroundColor White
        Write-Host "      💰 Balance: $($account.balance.amount) $($account.balance.currency)" -ForegroundColor Cyan
        Write-Host "      📧 Email: $($account.customerDetails.email)" -ForegroundColor Gray
        Write-Host ""
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get a specific account by ID (first account from John)
if ($johnAccounts.content.Count -gt 0) {
    Write-Host "`n3️⃣  Testing: Get specific account by ID" -ForegroundColor Green
    $accountId = $johnAccounts.content[0].accountId
    try {
        $headers = @{"X-Customer-ID" = "customer-001"}
        $specificAccount = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts/$accountId" -Method GET -Headers $headers
        
        Write-Host "✅ Retrieved specific account:" -ForegroundColor Green
        Write-Host "   📋 Account ID: $($specificAccount.accountId)" -ForegroundColor White
        Write-Host "   📊 Nickname: $($specificAccount.accountNickname)" -ForegroundColor White
        Write-Host "   🏦 Type: $($specificAccount.accountType)" -ForegroundColor White
        Write-Host "   📊 Status: $($specificAccount.status)" -ForegroundColor White
        Write-Host "   📅 Created: $($specificAccount.createdAt)" -ForegroundColor Gray
    } catch {
        Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Test filtering by account type
Write-Host "`n4️⃣  Testing: Filter accounts by type (SAVINGS)" -ForegroundColor Green
try {
    $headers = @{"X-Customer-ID" = "customer-001"}
    $savingsAccounts = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts?accountType=SAVINGS" -Method GET -Headers $headers
    
    Write-Host "✅ Found $($savingsAccounts.content.Count) SAVINGS accounts for customer-001:" -ForegroundColor Green
    foreach($account in $savingsAccounts.content) {
        Write-Host "   💰 $($account.accountNickname): $($account.balance.amount) $($account.balance.currency)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test creating a new account
Write-Host "`n5️⃣  Testing: Create a new account" -ForegroundColor Green
$newAccount = @{
    accountType = "CHECKING"
    currency = "USD"
    initialDeposit = 750.00
    customerDetails = @{
        firstName = "Alice"
        lastName = "Johnson"
        email = "alice.johnson@email.com"
        phoneNumber = "+1999888777"
        address = "999 Broadway, Seattle, WA 98101"
    }
    accountNickname = "Daily Spending"
    metadata = @{
        preferredBranch = "SEA-006"
        accountPurpose = "daily"
    }
}

try {
    $headers = @{
        "Content-Type" = "application/json"
        "X-Customer-ID" = "customer-006"
    }
    $createdAccount = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts" -Method POST -Body ($newAccount | ConvertTo-Json -Depth 3) -Headers $headers
    
    Write-Host "✅ Successfully created new account:" -ForegroundColor Green
    Write-Host "   📋 Account ID: $($createdAccount.accountId)" -ForegroundColor White
    Write-Host "   🏦 Account Number: $($createdAccount.accountNumber)" -ForegroundColor White
    Write-Host "   💰 Initial Balance: $($createdAccount.balance.amount) $($createdAccount.balance.currency)" -ForegroundColor Cyan
    Write-Host "   👤 Customer: $($newAccount.customerDetails.firstName) $($newAccount.customerDetails.lastName)" -ForegroundColor White
} catch {
    Write-Host "❌ Error creating account: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Test updating an account
if ($johnAccounts.content.Count -gt 0) {
    Write-Host "`n6️⃣  Testing: Update account nickname" -ForegroundColor Green
    $accountId = $johnAccounts.content[0].accountId
    $updateRequest = @{
        accountNickname = "Updated Primary Account"
        metadata = @{
            preferredBranch = "NYC-001"
            accountPurpose = "primary"
            lastUpdate = "2025-12-12"
        }
    }
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
            "X-Customer-ID" = "customer-001"
        }
        $updatedAccount = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts/$accountId" -Method PUT -Body ($updateRequest | ConvertTo-Json -Depth 3) -Headers $headers
        
        Write-Host "✅ Successfully updated account:" -ForegroundColor Green
        Write-Host "   📋 New Nickname: $($updatedAccount.accountNickname)" -ForegroundColor White
        Write-Host "   📅 Updated At: $($updatedAccount.updatedAt)" -ForegroundColor Gray
    } catch {
        Write-Host "❌ Error updating account: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 7: Test pagination
Write-Host "`n7️⃣  Testing: Pagination (page size = 2)" -ForegroundColor Green
try {
    $headers = @{"X-Customer-ID" = "customer-001"}
    $pagedAccounts = Invoke-RestMethod -Uri "http://localhost:8081/api/accounts?page=0&size=2" -Method GET -Headers $headers
    
    Write-Host "✅ Pagination test results:" -ForegroundColor Green
    Write-Host "   📄 Page: $($pagedAccounts.number + 1) of $($pagedAccounts.totalPages)" -ForegroundColor White
    Write-Host "   📊 Items on this page: $($pagedAccounts.numberOfElements)" -ForegroundColor White
    Write-Host "   📈 Total items: $($pagedAccounts.totalElements)" -ForegroundColor White
} catch {
    Write-Host "❌ Error with pagination: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 API Testing Complete!" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host "🌐 Swagger UI available at: http://localhost:8081/swagger-ui/index.html" -ForegroundColor Green
Write-Host "🔗 Application running at: http://localhost:8081" -ForegroundColor Green
Write-Host "📊 Database: SQLite with sample data loaded" -ForegroundColor Green

Write-Host "`n📋 Available Sample Customers:" -ForegroundColor Cyan
Write-Host "   • customer-001: John Smith (2 accounts)" -ForegroundColor White
Write-Host "   • customer-002: Jane Doe (1 account)" -ForegroundColor White
Write-Host "   • customer-003: Robert Johnson (1 account)" -ForegroundColor White
Write-Host "   • customer-004: Maria Garcia (1 account)" -ForegroundColor White
Write-Host "   • customer-005: David Wilson (1 account)" -ForegroundColor White
Write-Host "   • customer-006: Alice Johnson (1 account - newly created)" -ForegroundColor White

Write-Host "`n🧪 Try these API endpoints:" -ForegroundColor Cyan
Write-Host "GET    /api/accounts                     - List accounts" -ForegroundColor Gray
Write-Host "POST   /api/accounts                     - Create account" -ForegroundColor Gray
Write-Host "GET    /api/accounts/{id}                - Get specific account" -ForegroundColor Gray
Write-Host "PUT    /api/accounts/{id}                - Update account" -ForegroundColor Gray
Write-Host "DELETE /api/accounts/{id}                - Close account" -ForegroundColor Gray