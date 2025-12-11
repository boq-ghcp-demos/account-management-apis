package com.banking.accountmanagementapis;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Banking Account Management APIs
 * Runs all tests in the project
 */
@Suite
@SuiteDisplayName("Banking Account Management API Test Suite")
@SelectPackages({
    "com.banking.accountmanagementapis.controller",
    "com.banking.accountmanagementapis.service", 
    "com.banking.accountmanagementapis.repository",
    "com.banking.accountmanagementapis.dto",
    "com.banking.accountmanagementapis.integration",
    "com.banking.accountmanagementapis.testutil"
})
public class TestSuite {
}