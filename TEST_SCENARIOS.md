# Bill Payment System - Test Scenarios

## Overview

This document provides comprehensive test scenarios for the Bill Payment System, covering all business rules and API endpoints.

---

## Test Environment Setup

### Prerequisites
1. Application is running on `http://localhost:8080`
2. Database is initialized with sample data (V4 migration)
3. API testing tool (Postman, curl, or Swagger UI)

### Sample Test Data

The system includes the following sample accounts:

| Account ID   | Current Balance | Card Numbers                              |
|--------------|-----------------|-------------------------------------------|
| ACC00001234  | $1,500.00       | 4111111111111111, 4222222222222222       |
| ACC00005678  | $2,500.50       | 4333333333333333                         |
| ACC00009012  | $750.25         | 4444444444444444                         |
| ACC00003456  | $0.00           | None                                      |
| ACC00007890  | $5,000.00       | 4555555555555555                         |

---

## Test Scenarios

### Category 1: Account Management

#### Test 1.1: Get All Accounts
**Objective:** Verify retrieval of all accounts with pagination

**Request:**
```bash
GET http://localhost:8080/api/accounts?page=0&size=10
```

**Expected Result:**
- Status: 200 OK
- Response contains paginated list of accounts
- Each account has: accountId, currentBalance, hasPositiveBalance, createdAt, updatedAt

**Validation:**
- ✓ Response structure matches AccountResponseDto
- ✓ Pagination metadata is present
- ✓ All accounts from sample data are included

---

#### Test 1.2: Get Account by ID - Success
**Objective:** BR001: Verify successful account retrieval

**Request:**
```bash
GET http://localhost:8080/api/accounts/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
{
  "accountId": "ACC00001234",
  "currentBalance": 1500.00,
  "hasPositiveBalance": true,
  "createdAt": "...",
  "updatedAt": "..."
}
```

**Validation:**
- ✓ Account details are correct
- ✓ hasPositiveBalance is true (balance > 0)

---

#### Test 1.3: Get Account by ID - Not Found
**Objective:** BR001: Verify error when account doesn't exist

**Request:**
```bash
GET http://localhost:8080/api/accounts/ACC99999999
```

**Expected Result:**
- Status: 404 Not Found
- Error message: "Account ID NOT found..."

**Validation:**
- ✓ Proper error status code
- ✓ Error message matches business rule

---

#### Test 1.4: Get Account Balance - Positive Balance
**Objective:** BR002: Verify balance check for account with positive balance

**Request:**
```bash
GET http://localhost:8080/api/accounts/ACC00001234/balance
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
{
  "accountId": "ACC00001234",
  "currentBalance": 1500.00,
  "hasPositiveBalance": true
}
```

**Validation:**
- ✓ Balance is correct
- ✓ hasPositiveBalance is true

---

#### Test 1.5: Get Account Balance - Zero Balance
**Objective:** BR002: Verify balance check for account with zero balance

**Request:**
```bash
GET http://localhost:8080/api/accounts/ACC00003456/balance
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
{
  "accountId": "ACC00003456",
  "currentBalance": 0.00,
  "hasPositiveBalance": false
}
```

**Validation:**
- ✓ Balance is zero
- ✓ hasPositiveBalance is false

---

#### Test 1.6: Create Account - Success
**Objective:** Verify successful account creation

**Request:**
```bash
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountId": "ACC00011111",
  "currentBalance": 3000.00
}
```

**Expected Result:**
- Status: 201 Created
- Response contains created account with timestamps

**Validation:**
- ✓ Account is created in database
- ✓ Balance is set correctly
- ✓ Timestamps are populated

---

#### Test 1.7: Create Account - Empty Account ID
**Objective:** BR001: Verify validation for empty account ID

**Request:**
```bash
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountId": "",
  "currentBalance": 3000.00
}
```

**Expected Result:**
- Status: 400 Bad Request
- Error message: "Acct ID can NOT be empty..."

**Validation:**
- ✓ Validation error is returned
- ✓ Error message matches business rule

---

#### Test 1.8: Create Account - Zero Balance
**Objective:** BR002: Verify validation for zero balance

**Request:**
```bash
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountId": "ACC00022222",
  "currentBalance": 0.00
}
```

**Expected Result:**
- Status: 400 Bad Request
- Error message: "You have nothing to pay..."

**Validation:**
- ✓ Validation error is returned
- ✓ Error message matches business rule

---

#### Test 1.9: Update Account - Success
**Objective:** Verify successful account update

**Request:**
```bash
PUT http://localhost:8080/api/accounts/ACC00001234
Content-Type: application/json

{
  "currentBalance": 2000.00
}
```

**Expected Result:**
- Status: 200 OK
- Response shows updated balance
- updatedAt timestamp is updated

**Validation:**
- ✓ Balance is updated in database
- ✓ updatedAt timestamp is newer than createdAt

---

#### Test 1.10: Delete Account - Success
**Objective:** Verify successful account deletion

**Request:**
```bash
DELETE http://localhost:8080/api/accounts/ACC00011111
```

**Expected Result:**
- Status: 204 No Content

**Validation:**
- ✓ Account is removed from database
- ✓ Subsequent GET returns 404

---

### Category 2: Bill Payment Processing

#### Test 2.1: Process Payment - Display Information Only
**Objective:** BR003: Display payment information without confirmation

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmPayment": ""
}
```

**Expected Result:**
- Status: 400 Bad Request (or custom status for info display)
- Message indicates confirmation is required

**Validation:**
- ✓ No payment is processed
- ✓ Account balance remains unchanged

---

#### Test 2.2: Process Payment - Cancel Payment
**Objective:** BR003: Verify payment cancellation

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmPayment": "N"
}
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
{
  "accountId": "ACC00001234",
  "message": "Payment cancelled by user"
}
```

**Validation:**
- ✓ No payment is processed
- ✓ No transaction is created
- ✓ Account balance remains unchanged

---

#### Test 2.3: Process Payment - Success (Full Workflow)
**Objective:** BR001-BR007: Verify complete bill payment workflow

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmPayment": "Y"
}
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
{
  "transactionId": 5,
  "accountId": "ACC00001234",
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "paymentAmount": 1500.00,
  "timestamp": "...",
  "message": "Payment processed successfully. Transaction ID: 5",
  "transactionTypeCode": "02",
  "transactionCategoryCode": 2,
  "transactionSource": "POS TERM",
  "transactionDescription": "BILL PAYMENT - ONLINE",
  "merchantId": 999999999,
  "merchantName": "BILL PAYMENT",
  "merchantCity": "N/A",
  "merchantZip": "N/A"
}
```

**Validation:**
- ✓ BR001: Account is validated
- ✓ BR002: Positive balance is verified
- ✓ BR003: Confirmation is required and validated
- ✓ BR004: Full balance is paid (previousBalance = paymentAmount)
- ✓ BR005: Unique transaction ID is generated
- ✓ BR006: Transaction is recorded with correct attributes
- ✓ BR007: Account balance is updated to 0.00

**Post-Validation:**
```bash
# Verify account balance is now zero
GET http://localhost:8080/api/accounts/ACC00001234/balance

# Verify transaction was created
GET http://localhost:8080/api/transactions/5
```

---

#### Test 2.4: Process Payment - Account Not Found
**Objective:** BR001: Verify error when account doesn't exist

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC99999999",
  "cardNumber": "4111111111111111",
  "confirmPayment": "Y"
}
```

**Expected Result:**
- Status: 404 Not Found
- Error message: "Account ID NOT found..."

**Validation:**
- ✓ No payment is processed
- ✓ No transaction is created
- ✓ Error message matches business rule

---

#### Test 2.5: Process Payment - Zero Balance
**Objective:** BR002: Verify error when account has zero balance

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC00003456",
  "cardNumber": "4111111111111111",
  "confirmPayment": "Y"
}
```

**Expected Result:**
- Status: 422 Unprocessable Entity
- Error message: "You have nothing to pay..."

**Validation:**
- ✓ No payment is processed
- ✓ No transaction is created
- ✓ Error message matches business rule

---

#### Test 2.6: Process Payment - Invalid Confirmation
**Objective:** BR003: Verify error for invalid confirmation value

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmPayment": "X"
}
```

**Expected Result:**
- Status: 400 Bad Request
- Error message: "Invalid value. Valid values are (Y/N)..."

**Validation:**
- ✓ No payment is processed
- ✓ Error message matches business rule

---

#### Test 2.7: Process Payment - Empty Account ID
**Objective:** BR001: Verify validation for empty account ID

**Request:**
```bash
POST http://localhost:8080/api/accounts/process-payment
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": "4111111111111111",
  "confirmPayment": "Y"
}
```

**Expected Result:**
- Status: 400 Bad Request
- Error message: "Acct ID can NOT be empty..."

**Validation:**
- ✓ Validation error is returned
- ✓ Error message matches business rule

---

### Category 3: Card Cross Reference Management

#### Test 3.1: Get All Card Cross References
**Objective:** Verify retrieval of all cross-references

**Request:**
```bash
GET http://localhost:8080/api/card-cross-references?page=0&size=10
```

**Expected Result:**
- Status: 200 OK
- Response contains paginated list of cross-references

**Validation:**
- ✓ All sample cross-references are included
- ✓ Pagination works correctly

---

#### Test 3.2: Get Card Cross References by Account
**Objective:** Verify retrieval of all cards for an account

**Request:**
```bash
GET http://localhost:8080/api/card-cross-references/account/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response:
```json
[
  {
    "accountId": "ACC00001234",
    "cardNumber": "4111111111111111",
    "createdAt": "...",
    "updatedAt": "..."
  },
  {
    "accountId": "ACC00001234",
    "cardNumber": "4222222222222222",
    "createdAt": "...",
    "updatedAt": "..."
  }
]
```

**Validation:**
- ✓ Both cards for ACC00001234 are returned
- ✓ Response structure is correct

---

#### Test 3.3: Create Card Cross Reference - Success
**Objective:** Verify successful cross-reference creation

**Request:**
```bash
POST http://localhost:8080/api/card-cross-references
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4666666666666666"
}
```

**Expected Result:**
- Status: 201 Created
- Response contains created cross-reference

**Validation:**
- ✓ Cross-reference is created in database
- ✓ Can be retrieved via GET

---

#### Test 3.4: Create Card Cross Reference - Account Not Found
**Objective:** Verify error when account doesn't exist

**Request:**
```bash
POST http://localhost:8080/api/card-cross-references
Content-Type: application/json

{
  "accountId": "ACC99999999",
  "cardNumber": "4666666666666666"
}
```

**Expected Result:**
- Status: 404 Not Found
- Error message indicates account not found

**Validation:**
- ✓ No cross-reference is created
- ✓ Proper error status code

---

#### Test 3.5: Create Card Cross Reference - Already Exists
**Objective:** Verify error when cross-reference already exists

**Request:**
```bash
POST http://localhost:8080/api/card-cross-references
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111"
}
```

**Expected Result:**
- Status: 409 Conflict
- Error message indicates cross-reference already exists

**Validation:**
- ✓ No duplicate is created
- ✓ Proper error status code

---

#### Test 3.6: Delete Card Cross Reference - Success
**Objective:** Verify successful cross-reference deletion

**Request:**
```bash
DELETE http://localhost:8080/api/card-cross-references/ACC00001234/4666666666666666
```

**Expected Result:**
- Status: 204 No Content

**Validation:**
- ✓ Cross-reference is removed from database
- ✓ Subsequent GET returns 404

---

### Category 4: Transaction Management

#### Test 4.1: Get All Transactions
**Objective:** Verify retrieval of all transactions

**Request:**
```bash
GET http://localhost:8080/api/transactions?page=0&size=10
```

**Expected Result:**
- Status: 200 OK
- Response contains paginated list of transactions

**Validation:**
- ✓ Sample transactions are included
- ✓ Pagination works correctly

---

#### Test 4.2: Get Transactions by Account
**Objective:** BR006: Verify retrieval of account transactions

**Request:**
```bash
GET http://localhost:8080/api/transactions/account/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response contains all transactions for ACC00001234

**Validation:**
- ✓ Only transactions for specified account are returned
- ✓ Transaction details are complete

---

#### Test 4.3: Get Bill Payment Transactions
**Objective:** BR006: Verify retrieval of bill payment transactions only

**Request:**
```bash
GET http://localhost:8080/api/transactions/bill-payments
```

**Expected Result:**
- Status: 200 OK
- Response contains only transactions with:
  - transactionTypeCode = "02"
  - transactionCategoryCode = 2

**Validation:**
- ✓ All returned transactions are bill payments
- ✓ No other transaction types are included

---

#### Test 4.4: Get Bill Payment Transactions by Account
**Objective:** BR006: Verify retrieval of bill payments for specific account

**Request:**
```bash
GET http://localhost:8080/api/transactions/bill-payments/account/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response contains bill payment transactions for ACC00001234

**Validation:**
- ✓ Only bill payments for specified account are returned
- ✓ Transaction attributes match BR006 specifications

---

#### Test 4.5: Get Transactions by Date Range
**Objective:** Verify date range filtering

**Request:**
```bash
GET http://localhost:8080/api/transactions/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

**Expected Result:**
- Status: 200 OK
- Response contains transactions within date range

**Validation:**
- ✓ All transactions are within specified range
- ✓ Date filtering works correctly

---

#### Test 4.6: Get Transactions by Amount Range
**Objective:** Verify amount range filtering

**Request:**
```bash
GET http://localhost:8080/api/transactions/amount-range?minAmount=1000.00&maxAmount=2000.00
```

**Expected Result:**
- Status: 200 OK
- Response contains transactions within amount range

**Validation:**
- ✓ All transactions are within specified range
- ✓ Amount filtering works correctly

---

#### Test 4.7: Get Recent Transactions
**Objective:** Verify recent transactions retrieval

**Request:**
```bash
GET http://localhost:8080/api/transactions/recent?days=30
```

**Expected Result:**
- Status: 200 OK
- Response contains transactions from last 30 days

**Validation:**
- ✓ All transactions are within last 30 days
- ✓ Ordered by date (most recent first)

---

#### Test 4.8: Count Transactions by Account
**Objective:** Verify transaction count

**Request:**
```bash
GET http://localhost:8080/api/transactions/count/account/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response: integer count

**Validation:**
- ✓ Count matches actual number of transactions
- ✓ Count is accurate

---

#### Test 4.9: Sum Transaction Amounts by Account
**Objective:** Verify transaction amount sum

**Request:**
```bash
GET http://localhost:8080/api/transactions/sum/account/ACC00001234
```

**Expected Result:**
- Status: 200 OK
- Response: decimal sum

**Validation:**
- ✓ Sum matches total of all transaction amounts
- ✓ Calculation is accurate

---

#### Test 4.10: Get Next Transaction ID
**Objective:** BR005: Verify next transaction ID generation

**Request:**
```bash
GET http://localhost:8080/api/transactions/next-id
```

**Expected Result:**
- Status: 200 OK
- Response: next available transaction ID

**Validation:**
- ✓ ID is greater than current max ID
- ✓ ID is sequential

---

### Category 5: Integration Tests

#### Test 5.1: Complete Bill Payment Workflow
**Objective:** Test end-to-end bill payment process

**Steps:**
1. Create new account with balance
2. Create card cross-reference
3. Get account balance (verify positive)
4. Process payment with confirmation
5. Verify transaction was created
6. Verify account balance is zero
7. Get transaction history

**Expected Result:**
- All steps complete successfully
- Data is consistent across all operations

---

#### Test 5.2: Multiple Payments for Same Account
**Objective:** Verify account can be recharged and paid multiple times

**Steps:**
1. Create account with balance
2. Process payment (balance becomes zero)
3. Update account with new balance
4. Process another payment
5. Verify both transactions exist

**Expected Result:**
- Both payments are processed successfully
- Transaction history shows both payments

---

#### Test 5.3: Concurrent Payment Attempts
**Objective:** Verify transaction isolation

**Steps:**
1. Create account with balance
2. Attempt two simultaneous payment requests
3. Verify only one succeeds

**Expected Result:**
- Only one payment is processed
- No duplicate transactions
- Account balance is correct

---

## Test Execution Checklist

### Pre-Test Setup
- [ ] Application is running
- [ ] Database is initialized
- [ ] Sample data is loaded
- [ ] API testing tool is configured

### Test Execution
- [ ] All Category 1 tests pass (Account Management)
- [ ] All Category 2 tests pass (Bill Payment Processing)
- [ ] All Category 3 tests pass (Card Cross Reference)
- [ ] All Category 4 tests pass (Transaction Management)
- [ ] All Category 5 tests pass (Integration Tests)

### Post-Test Validation
- [ ] Database state is consistent
- [ ] No orphaned records
- [ ] All business rules are enforced
- [ ] Error messages are correct

---

## Test Results Template

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| 1.1 | Get All Accounts | ✓ PASS | |
| 1.2 | Get Account by ID - Success | ✓ PASS | |
| 1.3 | Get Account by ID - Not Found | ✓ PASS | |
| ... | ... | ... | |

---

## Automated Testing

For automated testing, consider using:
- JUnit 5 for unit tests
- Spring Boot Test for integration tests
- MockMvc for controller tests
- TestContainers for database tests

Example test structure:
```java
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testProcessPayment_Success() throws Exception {
        // Test implementation
    }
}
```

---

**Last Updated:** 2024-01-15
