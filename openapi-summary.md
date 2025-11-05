# Card Transaction Lifecycle Management API

## Overview
This API provides comprehensive functionality for managing card transactions, including validation, posting, reporting, and account management. The system implements business rules from legacy COBOL programs (CBTRN01C, CBTRN02C, CBTRN03C, COTRN01C, COTRN02C, CSUTLDTC) modernized into a RESTful Spring Boot application.

## Base URL
```
http://localhost:8080/api
```

## API Endpoints

### Transaction Management

#### 1. Validate Transaction
**Endpoint:** `POST /api/transactions/validate`

**Description:** Validates a transaction without posting it to the database. Performs card number verification, account validation, credit limit checks, and expiration date validation.

**Request Body:**
```json
{
  "cardNumber": "string (16 chars, numeric)",
  "accountId": "string (11 chars)",
  "transactionTypeCode": "string (2 chars, numeric)",
  "transactionCategoryCode": "integer (0-9999)",
  "transactionSource": "string (max 10 chars)",
  "transactionDescription": "string (max 100 chars)",
  "transactionAmount": "decimal (max 9 digits, 2 decimals)",
  "merchantId": "long (max 9 digits)",
  "merchantName": "string (max 50 chars)",
  "merchantCity": "string (max 50 chars)",
  "merchantZip": "string (max 10 chars)",
  "originalTimestamp": "datetime (ISO 8601)",
  "processingTimestamp": "datetime (ISO 8601)"
}
```

**Response:** `200 OK`
```json
{
  "valid": "boolean",
  "transactionId": "string",
  "validationFailureCode": "integer",
  "validationFailureReason": "string",
  "cardNumber": "string",
  "accountId": "string",
  "customerId": "string"
}
```

**Validation Failure Codes:**
- 100: Invalid card number
- 101: Account record not found
- 102: Overlimit transaction
- 103: Transaction received after account expiration

---

#### 2. Post Transaction
**Endpoint:** `POST /api/transactions`

**Description:** Validates and posts a new transaction. Updates account balances and transaction category balances.

**Request Body:** Same as Validate Transaction

**Response:** `201 Created`
```json
{
  "transactionId": "string (16 chars, auto-generated)",
  "cardNumber": "string",
  "accountId": "string",
  "transactionTypeCode": "string",
  "transactionCategoryCode": "integer",
  "transactionSource": "string",
  "transactionDescription": "string",
  "transactionAmount": "decimal",
  "merchantId": "long",
  "merchantName": "string",
  "merchantCity": "string",
  "merchantZip": "string",
  "originalTimestamp": "datetime",
  "processingTimestamp": "datetime",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

#### 3. Get Transaction by ID
**Endpoint:** `GET /api/transactions/{transactionId}`

**Description:** Retrieves detailed information about a specific transaction.

**Path Parameters:**
- `transactionId` (string, required): Unique transaction identifier

**Response:** `200 OK`
```json
{
  "transactionId": "string",
  "cardNumber": "string",
  "accountId": "string",
  "transactionTypeCode": "string",
  "transactionCategoryCode": "integer",
  "transactionSource": "string",
  "transactionDescription": "string",
  "transactionAmount": "decimal",
  "merchantId": "long",
  "merchantName": "string",
  "merchantCity": "string",
  "merchantZip": "string",
  "originalTimestamp": "datetime",
  "processingTimestamp": "datetime",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

#### 4. Get Transactions by Card Number
**Endpoint:** `GET /api/transactions/card/{cardNumber}`

**Description:** Retrieves all transactions associated with a specific card number.

**Path Parameters:**
- `cardNumber` (string, required): 16-character card number

**Response:** `200 OK`
```json
[
  {
    "transactionId": "string",
    "cardNumber": "string",
    "accountId": "string",
    "transactionTypeCode": "string",
    "transactionCategoryCode": "integer",
    "transactionSource": "string",
    "transactionDescription": "string",
    "transactionAmount": "decimal",
    "merchantId": "long",
    "merchantName": "string",
    "merchantCity": "string",
    "merchantZip": "string",
    "originalTimestamp": "datetime",
    "processingTimestamp": "datetime",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
]
```

---

#### 5. Get Transactions by Account ID
**Endpoint:** `GET /api/transactions/account/{accountId}`

**Description:** Retrieves all transactions associated with a specific account.

**Path Parameters:**
- `accountId` (string, required): 11-character account identifier

**Response:** `200 OK` - Array of transaction objects (same structure as Get Transaction by ID)

---

#### 6. Generate Transaction Report
**Endpoint:** `POST /api/transactions/report`

**Description:** Generates a comprehensive transaction report for a specified date range. Returns all transactions with type and category descriptions.

**Request Body:**
```json
{
  "startDate": "date (yyyy-MM-dd)",
  "endDate": "date (yyyy-MM-dd)"
}
```

**Response:** `200 OK`
```json
[
  {
    "transactionId": "string",
    "accountId": "string",
    "transactionTypeCode": "string",
    "transactionTypeDescription": "string",
    "transactionCategoryCode": "integer",
    "transactionCategoryDescription": "string",
    "transactionSource": "string",
    "transactionAmount": "decimal",
    "originalTimestamp": "datetime",
    "processingTimestamp": "datetime"
  }
]
```

---

#### 7. Get Paginated Transaction Report
**Endpoint:** `POST /api/transactions/report/paginated`

**Description:** Retrieves paginated transactions for a date range, ordered by account ID.

**Request Body:**
```json
{
  "startDate": "date (yyyy-MM-dd)",
  "endDate": "date (yyyy-MM-dd)"
}
```

**Query Parameters:**
- `page` (integer, optional, default: 0): Page number
- `size` (integer, optional, default: 20): Page size
- `sort` (string, optional): Sort criteria

**Response:** `200 OK`
```json
{
  "content": [
    {
      "transactionId": "string",
      "accountId": "string",
      "transactionTypeCode": "string",
      "transactionTypeDescription": "string",
      "transactionCategoryCode": "integer",
      "transactionCategoryDescription": "string",
      "transactionSource": "string",
      "transactionAmount": "decimal",
      "originalTimestamp": "datetime",
      "processingTimestamp": "datetime"
    }
  ],
  "pageable": {
    "pageNumber": "integer",
    "pageSize": "integer",
    "sort": "object"
  },
  "totalPages": "integer",
  "totalElements": "long",
  "last": "boolean",
  "first": "boolean",
  "numberOfElements": "integer",
  "size": "integer",
  "number": "integer",
  "empty": "boolean"
}
```

---

### Account Management

#### 8. Get Account by ID
**Endpoint:** `GET /api/accounts/{accountId}`

**Description:** Retrieves detailed account information including balances and credit limits.

**Path Parameters:**
- `accountId` (string, required): 11-character account identifier

**Response:** `200 OK`
```json
{
  "accountId": "string",
  "customerId": "string",
  "currentBalance": "decimal",
  "creditLimit": "decimal",
  "currentCycleCredit": "decimal",
  "currentCycleDebit": "decimal",
  "expirationDate": "date",
  "accountStatus": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

#### 9. Get Accounts by Customer ID
**Endpoint:** `GET /api/accounts/customer/{customerId}`

**Description:** Retrieves all accounts associated with a specific customer.

**Path Parameters:**
- `customerId` (string, required): 9-character customer identifier

**Response:** `200 OK` - Array of account objects (same structure as Get Account by ID)

---

#### 10. Create Account
**Endpoint:** `POST /api/accounts`

**Description:** Creates a new customer account.

**Request Body:**
```json
{
  "accountId": "string (11 chars, required)",
  "customerId": "string (9 chars, required)",
  "currentBalance": "decimal (required)",
  "creditLimit": "decimal (required, min: 0)",
  "currentCycleCredit": "decimal (required)",
  "currentCycleDebit": "decimal (required)",
  "expirationDate": "date (required)",
  "accountStatus": "string (max 10 chars, required)"
}
```

**Response:** `201 Created` - Account object with generated timestamps

---

#### 11. Update Account
**Endpoint:** `PUT /api/accounts/{accountId}`

**Description:** Updates an existing account's information.

**Path Parameters:**
- `accountId` (string, required): Account identifier to update

**Request Body:** Same as Create Account (accountId in path takes precedence)

**Response:** `200 OK` - Updated account object

---

### Date Validation

#### 12. Validate Date
**Endpoint:** `GET /api/validation/date`

**Description:** Validates a date string against a specified format pattern.

**Query Parameters:**
- `dateString` (string, required): Date string to validate
- `dateFormat` (string, optional, default: "yyyy-MM-dd"): Expected date format pattern

**Response:** `200 OK`
```json
{
  "valid": "boolean",
  "severityCode": "string",
  "messageNumber": "string",
  "resultMessage": "string",
  "dateString": "string",
  "dateFormat": "string",
  "parsedDate": "date (if valid)"
}
```

**Validation Messages:**
- "Date is valid" (severityCode: 0000)
- "Insufficient" (severityCode: 0001) - Empty or null date
- "Invalid Era" (severityCode: 0002) - Date outside valid range (1601-9999)
- "Datevalue error" (severityCode: 0003) - Invalid date format
- "Bad Pic String" (severityCode: 0004) - Invalid format pattern

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "timestamp": "datetime",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "string"
}
```

### 404 Not Found
```json
{
  "timestamp": "datetime",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found",
  "path": "string"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "datetime",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "string"
}
```

---

## Data Validation Rules

### Transaction Validation
- Card number: 16 numeric characters
- Account ID: 11 characters
- Transaction type code: 2 numeric characters
- Transaction category code: 0-9999
- Transaction amount: -99999999.99 to 99999999.99
- Merchant ID: 0-999999999
- All timestamp fields must be in ISO 8601 format

### Account Validation
- Account ID: 11 characters
- Customer ID: 9 characters
- Credit limit: Must be positive
- Expiration date: Must be in yyyy-MM-dd format
- Account status: Maximum 10 characters

### Date Validation
- Supported formats: yyyy-MM-dd, MM/dd/yyyy, dd-MM-yyyy, and custom patterns
- Valid year range: 1601-9999
- Validates leap years and month-specific day limits

---

## Business Logic Implementation

### Transaction Processing Flow (CBTRN02C)
1. Validate card number via cross-reference lookup
2. Validate account existence
3. Check credit limit (current cycle credit - debit + transaction amount ≤ credit limit)
4. Verify account expiration date
5. Generate sequential transaction ID
6. Post transaction to database
7. Update account balances (current balance, cycle credit/debit)
8. Update transaction category balances

### Transaction Validation Flow (CBTRN01C)
1. Read transaction record
2. Validate card number in cross-reference file
3. Retrieve account ID and customer ID
4. Validate account record exists
5. Return validation result with detailed error codes

### Report Generation Flow (CBTRN03C)
1. Accept date range parameters
2. Filter transactions by processing timestamp
3. Retrieve transaction type and category descriptions
4. Calculate page totals, account totals, and grand totals
5. Return formatted report data

---

## Authentication & Authorization
*Note: Authentication and authorization mechanisms should be implemented based on organizational security requirements. Consider implementing OAuth 2.0, JWT tokens, or API keys.*

---

## Rate Limiting
*Note: Consider implementing rate limiting to prevent abuse. Recommended limits should be defined based on expected usage patterns.*

---

## Versioning
API Version: 1.0.0

The API follows semantic versioning. Breaking changes will result in a new major version.

---

## Support & Contact
For API support, integration questions, or to report issues, please contact the development team.
