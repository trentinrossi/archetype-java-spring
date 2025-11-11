# API Documentation Summary - Card Demo Application

## Overview
This document provides a comprehensive summary of all REST API endpoints generated for the Card Demo application, specifically for the Account Management module (CBACT01C - Account Data File Reader and Printer).

## Base URL
```
http://localhost:8080/api
```

## Entity: Account

### Description
Represents a customer account with financial and status information. Each account has an 11-digit unique identifier and tracks balances, credit limits, dates, and transaction cycles.

---

## API Endpoints

### 1. Get All Accounts
**Endpoint:** `GET /api/accounts`

**Description:** Retrieve a paginated list of all accounts

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size
- `sort` (optional) - Sort criteria

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": 12345678901,
      "acctActiveStatus": "A",
      "acctCurrBal": 1500.50,
      "acctCreditLimit": 5000.00,
      "acctCashCreditLimit": 1000.00,
      "acctOpenDate": "2023-01-15",
      "acctExpirationDate": "2026-01-15",
      "acctReissueDate": null,
      "acctCurrCycCredit": 250.00,
      "acctCurrCycDebit": 150.00,
      "acctGroupId": "GRP001",
      "availableCredit": 3500.50,
      "availableCashCredit": -500.50,
      "isActive": true,
      "isExpired": false,
      "currentCycleNetAmount": 100.00,
      "acctActiveStatusDisplay": "Active",
      "createdAt": "2023-01-15T10:30:00",
      "updatedAt": "2024-01-15T14:20:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

**Error Responses:**
- `400 Bad Request` - Invalid request parameters
- `500 Internal Server Error` - Server error

---

### 2. Get Account by Database ID
**Endpoint:** `GET /api/accounts/{id}`

**Description:** Retrieve an account by its database ID

**Path Parameters:**
- `id` (required) - Database ID (Long)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "acctActiveStatus": "A",
  "acctCurrBal": 1500.50,
  ...
}
```

**Error Responses:**
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 3. Get Account by ACCT-ID
**Endpoint:** `GET /api/accounts/acct/{acctId}`

**Description:** Retrieve an account by its unique 11-digit account identifier

**Path Parameters:**
- `acctId` (required) - 11-digit account identifier (Long)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "acctActiveStatus": "A",
  ...
}
```

**Error Responses:**
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 4. Create Account
**Endpoint:** `POST /api/accounts`

**Description:** Create a new account with all required fields

**Request Body:**
```json
{
  "acctId": "12345678901",
  "acctActiveStatus": "A",
  "acctCurrBal": 1500.50,
  "acctCreditLimit": 5000.00,
  "acctCashCreditLimit": 1000.00,
  "acctOpenDate": "2023-01-15",
  "acctExpirationDate": "2026-01-15",
  "acctReissueDate": null,
  "acctCurrCycCredit": 250.00,
  "acctCurrCycDebit": 150.00,
  "acctGroupId": "GRP001"
}
```

**Validation Rules:**
- `acctId`: Required, must be an 11-digit numeric value
- `acctActiveStatus`: Required, must be 'A' (active) or 'I' (inactive)
- `acctCurrBal`: Required, must be >= 0
- `acctCreditLimit`: Required, must be >= 0
- `acctCashCreditLimit`: Required, must be >= 0
- `acctOpenDate`: Required, must be in the past or present
- `acctExpirationDate`: Required, must be in the future
- `acctCurrCycCredit`: Required, must be >= 0
- `acctCurrCycDebit`: Required, must be >= 0
- `acctReissueDate`: Optional
- `acctGroupId`: Optional

**Response:** `201 Created`
```json
{
  "acctId": 12345678901,
  "acctActiveStatus": "A",
  ...
}
```

**Error Responses:**
- `400 Bad Request` - Invalid request data (e.g., "Account ID must be an 11-digit numeric value")
- `500 Internal Server Error` - Server error

---

### 5. Update Account
**Endpoint:** `PUT /api/accounts/{id}`

**Description:** Update account details by database ID

**Path Parameters:**
- `id` (required) - Database ID (Long)

**Request Body:** (All fields optional)
```json
{
  "acctActiveStatus": "I",
  "acctCurrBal": 2000.00,
  "acctCreditLimit": 6000.00,
  "acctCashCreditLimit": 1500.00,
  "acctOpenDate": "2023-01-15",
  "acctExpirationDate": "2027-01-15",
  "acctReissueDate": "2024-01-15",
  "acctCurrCycCredit": 300.00,
  "acctCurrCycDebit": 200.00,
  "acctGroupId": "GRP002"
}
```

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "acctActiveStatus": "I",
  ...
}
```

**Error Responses:**
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 6. Delete Account
**Endpoint:** `DELETE /api/accounts/{id}`

**Description:** Delete an account by its database ID

**Path Parameters:**
- `id` (required) - Database ID (Long)

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 7. Process Accounts Sequentially
**Endpoint:** `GET /api/accounts/process-sequential`

**Description:** Process account records sequentially from the account file until end-of-file is reached. Implements BR-001: Sequential Account Record Processing. This endpoint reads all accounts in order by ACCT-ID and displays their information.

**Response:** `200 OK`
```json
{
  "resultCode": 16,
  "message": "End of file reached - Processing completed successfully",
  "success": true
}
```

**Result Codes:**
- `0` - Success
- `12` - File error - Account file could not be opened
- `16` - End of file reached - Processing completed successfully (expected result)

**Error Responses:**
- `400 Bad Request` - Account file could not be opened
- `500 Internal Server Error` - Error during processing

**Business Rules Implemented:**
- BR-001: Sequential Account Record Processing
- BR-002: Account Data Display Requirements
- BR-003: Account File Access Control
- BR-004: End of File Detection

---

### 8. Apply Credit to Account
**Endpoint:** `POST /api/accounts/{acctId}/credit`

**Description:** Apply a credit amount to the account's current cycle credit. This reduces the current balance and increases the current cycle credit.

**Path Parameters:**
- `acctId` (required) - 11-digit account identifier (Long)

**Query Parameters:**
- `amount` (required) - Credit amount (BigDecimal)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "acctCurrBal": 1250.50,
  "acctCurrCycCredit": 500.00,
  ...
}
```

**Error Responses:**
- `400 Bad Request` - Invalid credit amount (must be > 0)
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 9. Apply Debit to Account
**Endpoint:** `POST /api/accounts/{acctId}/debit`

**Description:** Apply a debit amount to the account's current cycle debit. This increases the current balance and current cycle debit. Validates that the transaction doesn't exceed available credit.

**Path Parameters:**
- `acctId` (required) - 11-digit account identifier (Long)

**Query Parameters:**
- `amount` (required) - Debit amount (BigDecimal)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "acctCurrBal": 1750.50,
  "acctCurrCycDebit": 400.00,
  ...
}
```

**Error Responses:**
- `400 Bad Request` - Invalid debit amount or exceeds credit limit
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 10. Check if Transaction Can Be Processed
**Endpoint:** `GET /api/accounts/{acctId}/can-process-transaction`

**Description:** Verify if a transaction can be processed based on account status (must be active), expiration date (must not be expired), and available credit (must be sufficient).

**Path Parameters:**
- `acctId` (required) - 11-digit account identifier (Long)

**Query Parameters:**
- `amount` (required) - Transaction amount (BigDecimal)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "transactionAmount": 500.00,
  "canProcess": true,
  "message": "Transaction can be processed"
}
```

**Error Responses:**
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 11. Get Available Credit
**Endpoint:** `GET /api/accounts/{acctId}/available-credit`

**Description:** Calculate and retrieve the available credit for an account (credit limit - current balance).

**Path Parameters:**
- `acctId` (required) - 11-digit account identifier (Long)

**Response:** `200 OK`
```json
{
  "acctId": 12345678901,
  "availableCredit": 3500.50
}
```

**Error Responses:**
- `404 Not Found` - Account not found
- `500 Internal Server Error` - Server error

---

### 12. Get Expired Accounts
**Endpoint:** `GET /api/accounts/expired`

**Description:** Retrieve all accounts that have passed their expiration date (acctExpirationDate < current date).

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": 12345678901,
      "acctExpirationDate": "2023-12-31",
      "isExpired": true,
      ...
    }
  ],
  "totalElements": 15
}
```

**Error Responses:**
- `500 Internal Server Error` - Server error

---

### 13. Get Accounts by Status
**Endpoint:** `GET /api/accounts/status/{status}`

**Description:** Retrieve all accounts with a specific active status.

**Path Parameters:**
- `status` (required) - Active status ('A' for active, 'I' for inactive)

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": 12345678901,
      "acctActiveStatus": "A",
      ...
    }
  ],
  "totalElements": 50
}
```

**Error Responses:**
- `400 Bad Request` - Active status must be 'A' or 'I'
- `500 Internal Server Error` - Server error

---

### 14. Get Accounts by Group
**Endpoint:** `GET /api/accounts/group/{groupId}`

**Description:** Retrieve all accounts belonging to a specific account group.

**Path Parameters:**
- `groupId` (required) - Account group identifier (String)

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": 12345678901,
      "acctGroupId": "GRP001",
      ...
    }
  ],
  "totalElements": 25
}
```

**Error Responses:**
- `500 Internal Server Error` - Server error

---

### 15. Count Accounts by Status
**Endpoint:** `GET /api/accounts/count/status/{status}`

**Description:** Get the total count of accounts with a specific active status.

**Path Parameters:**
- `status` (required) - Active status ('A' for active, 'I' for inactive)

**Response:** `200 OK`
```json
{
  "status": "A",
  "count": 150
}
```

**Error Responses:**
- `400 Bad Request` - Active status must be 'A' or 'I'
- `500 Internal Server Error` - Server error

---

### 16. Get Accounts Over Credit Limit
**Endpoint:** `GET /api/accounts/over-credit-limit`

**Description:** Retrieve all accounts where the current balance exceeds the credit limit (acctCurrBal > acctCreditLimit).

**Response:** `200 OK`
```json
[
  {
    "acctId": 12345678901,
    "acctCurrBal": 5500.00,
    "acctCreditLimit": 5000.00,
    ...
  }
]
```

**Error Responses:**
- `500 Internal Server Error` - Server error

---

## Data Model

### Account Entity Fields

| Field Name | Type | Required | Description | Validation |
|------------|------|----------|-------------|------------|
| acctId | Long | Yes | Unique 11-digit account identifier | Must be 11 digits |
| acctActiveStatus | String(1) | Yes | Account status | 'A' (active) or 'I' (inactive) |
| acctCurrBal | BigDecimal(15,2) | Yes | Current balance | >= 0 |
| acctCreditLimit | BigDecimal(15,2) | Yes | Maximum credit limit | >= 0 |
| acctCashCreditLimit | BigDecimal(15,2) | Yes | Maximum cash credit limit | >= 0 |
| acctOpenDate | LocalDate | Yes | Account opening date | Past or present |
| acctExpirationDate | LocalDate | Yes | Account expiration date | Future |
| acctReissueDate | LocalDate | No | Account reissue date | - |
| acctCurrCycCredit | BigDecimal(15,2) | Yes | Current cycle credit amount | >= 0 |
| acctCurrCycDebit | BigDecimal(15,2) | Yes | Current cycle debit amount | >= 0 |
| acctGroupId | String | No | Account group identifier | - |
| createdAt | LocalDateTime | Auto | Record creation timestamp | - |
| updatedAt | LocalDateTime | Auto | Record update timestamp | - |

### Computed Fields (Response Only)

| Field Name | Type | Description | Calculation |
|------------|------|-------------|-------------|
| availableCredit | BigDecimal | Available credit | acctCreditLimit - acctCurrBal |
| availableCashCredit | BigDecimal | Available cash credit | acctCashCreditLimit - acctCurrBal |
| isActive | Boolean | Is account active | acctActiveStatus == 'A' |
| isExpired | Boolean | Is account expired | acctExpirationDate < current date |
| currentCycleNetAmount | BigDecimal | Net cycle amount | acctCurrCycCredit - acctCurrCycDebit |
| acctActiveStatusDisplay | String | Status display name | "Active" or "Inactive" |

---

## Business Rules Implemented

### BR-001: Sequential Account Record Processing
Account records must be processed sequentially from the account file until end-of-file is reached.
- **Endpoint:** `GET /api/accounts/process-sequential`
- **Implementation:** Retrieves all accounts ordered by ACCT-ID ascending and processes each one

### BR-002: Account Data Display Requirements
All account information fields must be displayed for each record processed.
- **Implementation:** The `displayAllFields()` method in the Account entity formats all fields for display
- **Used by:** Sequential processing endpoint

### BR-003: Account File Access Control
Account file must be opened for input operations before any record can be read.
- **Implementation:** `openAccountFileForInput()` method validates database access before processing
- **Result codes:** 0 (success), 12 (file error)

### BR-004: End of File Detection
Processing must stop when end-of-file condition is detected.
- **Implementation:** `detectEndOfFile()` method returns result code 16 when processing completes
- **Result code:** 16 (EOF reached)

---

## Error Handling

### Standard Error Response Format
```json
{
  "timestamp": "2024-01-15T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Account ID must be an 11-digit numeric value",
  "path": "/api/accounts"
}
```

### Common Error Messages

| Error Message | HTTP Status | Cause |
|---------------|-------------|-------|
| Account ID must be an 11-digit numeric value | 400 | Invalid ACCT-ID format |
| Active status must be 'A' or 'I' | 400 | Invalid status value |
| Account with ID already exists | 400 | Duplicate account creation |
| Account not found | 404 | Account doesn't exist |
| Credit amount must be greater than zero | 400 | Invalid credit amount |
| Debit amount must be greater than zero | 400 | Invalid debit amount |
| Transaction exceeds available credit limit or account is not active | 400 | Insufficient credit or inactive account |
| Group ID cannot be null or empty | 400 | Missing group ID |

---

## Testing Examples

### Create an Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": "12345678901",
    "acctActiveStatus": "A",
    "acctCurrBal": 1500.50,
    "acctCreditLimit": 5000.00,
    "acctCashCreditLimit": 1000.00,
    "acctOpenDate": "2023-01-15",
    "acctExpirationDate": "2026-01-15",
    "acctCurrCycCredit": 250.00,
    "acctCurrCycDebit": 150.00,
    "acctGroupId": "GRP001"
  }'
```

### Get All Accounts
```bash
curl -X GET "http://localhost:8080/api/accounts?page=0&size=20"
```

### Apply Debit
```bash
curl -X POST "http://localhost:8080/api/accounts/12345678901/debit?amount=100.00"
```

### Check Transaction
```bash
curl -X GET "http://localhost:8080/api/accounts/12345678901/can-process-transaction?amount=500.00"
```

### Process Sequential
```bash
curl -X GET "http://localhost:8080/api/accounts/process-sequential"
```

---

## Database Schema

### Table: accounts

```sql
CREATE TABLE accounts (
    acct_id BIGINT NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    acct_curr_bal DECIMAL(15, 2) NOT NULL,
    acct_credit_limit DECIMAL(15, 2) NOT NULL,
    acct_cash_credit_limit DECIMAL(15, 2) NOT NULL,
    acct_open_date DATE NOT NULL,
    acct_expiration_date DATE NOT NULL,
    acct_reissue_date DATE,
    acct_curr_cyc_credit DECIMAL(15, 2) NOT NULL,
    acct_curr_cyc_debit DECIMAL(15, 2) NOT NULL,
    acct_group_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (acct_id)
);
```

### Indexes
- `idx_accounts_acct_group_id` on `acct_group_id`
- `idx_accounts_acct_active_status` on `acct_active_status`
- `idx_accounts_acct_open_date` on `acct_open_date`
- `idx_accounts_acct_expiration_date` on `acct_expiration_date`

---

## Technology Stack

- **Framework:** Spring Boot 3.5.5
- **Java Version:** 21
- **Database:** PostgreSQL (H2 for development)
- **ORM:** Spring Data JPA
- **Migration:** Flyway
- **Documentation:** OpenAPI 3.0 (Swagger)
- **Validation:** Jakarta Bean Validation
- **Logging:** SLF4J with Logback

---

## Notes

1. **Account ID Format:** All account IDs must be exactly 11 digits. The API accepts them as strings in request bodies but stores them as Long values internally.

2. **Date Formats:** All dates use ISO-8601 format (YYYY-MM-DD).

3. **Currency Precision:** All monetary values use BigDecimal with precision 15 and scale 2 (15 total digits, 2 decimal places).

4. **Pagination:** Default page size is 20. Maximum page size should be configured in application properties.

5. **Transaction Safety:** All write operations (create, update, delete, credit, debit) are wrapped in database transactions.

6. **Validation:** Request validation occurs at the controller layer using Jakarta Bean Validation annotations.

7. **Logging:** All operations are logged at INFO level with detailed context.

8. **Sequential Processing:** The sequential processing endpoint is designed to mimic batch file processing behavior from legacy systems.

---

## Future Enhancements

Potential endpoints that could be added:
- Bulk account creation
- Account statement generation
- Transaction history
- Credit limit adjustment workflow
- Account closure workflow
- Account reactivation
- Batch processing status tracking

---

**Generated:** 2024
**Version:** 1.0
**Application:** Card Demo - Account Management Module
