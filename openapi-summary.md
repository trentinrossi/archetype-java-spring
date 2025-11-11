# Card Account Transaction Management System - API Documentation

## Overview

This document provides a comprehensive summary of all REST API endpoints for the Card Account Transaction Management System. The system implements a complete account statement generation solution with customer management, account tracking, card management, transaction processing, and statement generation capabilities.

## Base URL

```
http://localhost:8080/api
```

## Business Rules Implementation

The API implements the following business rules:

- **BR001**: Transaction Grouping by Card - Transactions are grouped by card number for efficient processing
- **BR002**: Statement Generation Per Account - Comprehensive statements generated for each account
- **BR003**: Transaction Amount Summation - All transaction amounts are summed for statement periods
- **BR004**: Dual Format Statement Output - Statements available in both plain text and HTML formats
- **BR005**: Transaction Table Capacity Limit - Maximum 10 transactions per card
- **BR006**: Customer Name Composition - Full names composed from first, middle (if present), and last names
- **BR007**: Complete Address Display - Complete addresses with all available lines, state, country, and ZIP
- **BR008**: HTML Statement Styling Standards - HTML statements follow specific styling standards
- **BR009**: Card-Account-Customer Linkage - Proper linkage between cards, accounts, and customers

---

## Customer Management APIs

### 1. Get All Customers

**Endpoint:** `GET /api/customers`

**Description:** Retrieve a paginated list of all customers with their complete information including address and FICO credit score.

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size
- `sort` (optional) - Sort field and direction (e.g., `firstName,asc`)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "customerId": 123456789,
      "firstName": "John",
      "middleName": "Michael",
      "lastName": "Doe",
      "fullName": "John Michael Doe",
      "addressLine1": "123 Main Street",
      "addressLine2": "Apt 4B",
      "addressLine3": null,
      "stateCode": "CA",
      "countryCode": "USA",
      "zipCode": "90210-1234",
      "completeAddress": "123 Main Street, Apt 4B, CA, USA 90210-1234",
      "ficoCreditScore": 750,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
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

### 2. Get Customer by ID

**Endpoint:** `GET /api/customers/{id}`

**Description:** Retrieve a customer by their internal database ID.

**Path Parameters:**
- `id` (required) - Internal database ID

**Response:** `200 OK` - Customer object (same structure as above)

**Error Responses:**
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

---

### 3. Get Customer by Customer ID

**Endpoint:** `GET /api/customers/customer-id/{customerId}`

**Description:** Retrieve a customer by their 9-digit customer identifier used for cross-reference with accounts and cards.

**Path Parameters:**
- `customerId` (required) - 9-digit customer identifier

**Response:** `200 OK` - Customer object

**Error Responses:**
- `400 Bad Request` - Invalid customer ID format
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

---

### 4. Create Customer

**Endpoint:** `POST /api/customers`

**Description:** Create a new customer with complete information. Customer ID must be 9 digits and unique.

**Request Body:**
```json
{
  "customerId": 123456789,
  "firstName": "John",
  "middleName": "Michael",
  "lastName": "Doe",
  "addressLine1": "123 Main Street",
  "addressLine2": "Apt 4B",
  "addressLine3": null,
  "stateCode": "CA",
  "countryCode": "USA",
  "zipCode": "90210-1234",
  "ficoCreditScore": 750
}
```

**Validation Rules:**
- `customerId`: Required, must be 9 digits (100000000-999999999)
- `firstName`: Required, max 25 characters
- `middleName`: Optional, max 25 characters
- `lastName`: Required, max 25 characters
- `addressLine1`: Required, max 50 characters
- `addressLine2`: Optional, max 50 characters
- `addressLine3`: Optional, max 50 characters
- `stateCode`: Required, exactly 2 characters
- `countryCode`: Required, exactly 3 characters
- `zipCode`: Required, exactly 10 alphanumeric characters
- `ficoCreditScore`: Required, 0-999

**Response:** `201 Created` - Created customer object

**Error Responses:**
- `400 Bad Request` - Validation errors
- `409 Conflict` - Customer ID already exists
- `500 Internal Server Error` - Server error

---

### 5. Update Customer

**Endpoint:** `PUT /api/customers/{id}`

**Description:** Update customer details by internal database ID.

**Path Parameters:**
- `id` (required) - Internal database ID

**Request Body:** (All fields optional)
```json
{
  "firstName": "John",
  "middleName": "Michael",
  "lastName": "Doe",
  "addressLine1": "123 Main Street",
  "addressLine2": "Apt 4B",
  "addressLine3": null,
  "stateCode": "CA",
  "countryCode": "USA",
  "zipCode": "90210-1234",
  "ficoCreditScore": 750
}
```

**Response:** `200 OK` - Updated customer object

**Error Responses:**
- `400 Bad Request` - Validation errors
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

---

### 6. Delete Customer

**Endpoint:** `DELETE /api/customers/{id}`

**Description:** Delete a customer by their internal database ID.

**Path Parameters:**
- `id` (required) - Internal database ID

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - Customer not found
- `409 Conflict` - Customer linked to active accounts/cards
- `500 Internal Server Error` - Server error

---

## Account Management APIs

### 7. Get All Accounts

**Endpoint:** `GET /api/accounts`

**Description:** Retrieve a paginated list of all accounts.

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size

**Response:** `200 OK`
```json
{
  "content": [
    {
      "accountId": 12345678901,
      "currentBalance": 1500.50,
      "creditLimit": 5000.00,
      "availableCredit": 3499.50,
      "customerId": 123456789,
      "customerName": "John Michael Doe",
      "isOverLimit": false,
      "totalTransactionAmount": 250.75,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

---

### 8. Get Account by ID

**Endpoint:** `GET /api/accounts/{id}`

**Description:** Retrieve an account by internal database ID.

**Response:** `200 OK` - Account object

---

### 9. Get Account by Account ID

**Endpoint:** `GET /api/accounts/account-id/{accountId}`

**Description:** Retrieve an account by 11-digit account identifier.

**Path Parameters:**
- `accountId` (required) - 11-digit account identifier

**Response:** `200 OK` - Account object

---

### 10. Get Accounts by Customer ID

**Endpoint:** `GET /api/accounts/customer/{customerId}`

**Description:** Retrieve all accounts for a specific customer.

**Path Parameters:**
- `customerId` (required) - Customer ID

**Response:** `200 OK` - Paginated list of accounts

---

### 11. Create Account

**Endpoint:** `POST /api/accounts`

**Description:** Create a new account linked to a customer.

**Request Body:**
```json
{
  "accountId": 12345678901,
  "currentBalance": 0.00,
  "creditLimit": 5000.00,
  "customerId": 123456789
}
```

**Validation Rules:**
- `accountId`: Required, 11 digits (10000000000-99999999999)
- `currentBalance`: Required, max 10 integer digits, 2 decimal places
- `creditLimit`: Required, max 10 integer digits, 2 decimal places
- `customerId`: Required, must exist in customer file

**Response:** `201 Created` - Created account object

---

### 12. Update Account

**Endpoint:** `PUT /api/accounts/{id}`

**Description:** Update account balance and credit limit.

**Request Body:**
```json
{
  "currentBalance": 1500.50,
  "creditLimit": 5000.00
}
```

**Response:** `200 OK` - Updated account object

---

### 13. Delete Account

**Endpoint:** `DELETE /api/accounts/{id}`

**Response:** `204 No Content`

---

### 14. Get Total Transaction Amount for Account

**Endpoint:** `GET /api/accounts/{accountId}/total-transaction-amount`

**Description:** Calculate total transaction amount for an account (BR003).

**Response:** `200 OK`
```json
250.75
```

---

## Card Management APIs

### 15. Get All Cards

**Endpoint:** `GET /api/cards`

**Description:** Retrieve a paginated list of all cards.

**Response:** `200 OK`
```json
{
  "content": [
    {
      "cardNumber": "1234567890123456",
      "customerId": 123456789,
      "customerName": "John Michael Doe",
      "accountId": 12345678901,
      "transactionCount": 5,
      "canAddTransaction": true,
      "hasValidLinkage": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ]
}
```

---

### 16. Get Card by Card Number

**Endpoint:** `GET /api/cards/{cardNumber}`

**Description:** Retrieve a card by its 16-character card number.

**Path Parameters:**
- `cardNumber` (required) - 16-character alphanumeric card number

**Response:** `200 OK` - Card object

---

### 17. Get Cards by Customer ID

**Endpoint:** `GET /api/cards/customer/{customerId}`

**Description:** Retrieve all cards for a specific customer.

**Response:** `200 OK` - Paginated list of cards

---

### 18. Get Cards by Account ID

**Endpoint:** `GET /api/cards/account/{accountId}`

**Description:** Retrieve all cards for a specific account.

**Response:** `200 OK` - Paginated list of cards

---

### 19. Create Card

**Endpoint:** `POST /api/cards`

**Description:** Create a new card linked to customer and account (BR009).

**Request Body:**
```json
{
  "cardNumber": "1234567890123456",
  "customerId": 123456789,
  "accountId": 12345678901
}
```

**Validation Rules:**
- `cardNumber`: Required, exactly 16 alphanumeric characters
- `customerId`: Required, 9 digits, must exist
- `accountId`: Required, 11 digits, must exist

**Response:** `201 Created` - Created card object

---

### 20. Update Card

**Endpoint:** `PUT /api/cards/{cardNumber}`

**Description:** Update card linkage to customer or account.

**Request Body:**
```json
{
  "customerId": 123456789,
  "accountId": 12345678901
}
```

**Response:** `200 OK` - Updated card object

---

### 21. Delete Card

**Endpoint:** `DELETE /api/cards/{cardNumber}`

**Response:** `204 No Content`

---

### 22. Check Card Transaction Capacity

**Endpoint:** `GET /api/cards/{cardNumber}/can-add-transaction`

**Description:** Check if card can accept more transactions (BR005 - max 10 per card).

**Response:** `200 OK`
```json
true
```

---

## Transaction Management APIs

### 23. Get All Transactions

**Endpoint:** `GET /api/transactions`

**Description:** Retrieve a paginated list of all transactions.

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "cardNumber": "1234567890123456",
      "transactionId": "TXN1234567890123",
      "transactionTypeCode": "PU",
      "transactionCategoryCode": "5411",
      "transactionSource": "POS",
      "transactionDescription": "Grocery Store Purchase",
      "transactionAmount": -50.25,
      "formattedAmount": "-50.25",
      "isDebit": true,
      "isCredit": false,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ]
}
```

---

### 24. Get Transaction by ID

**Endpoint:** `GET /api/transactions/{id}`

**Response:** `200 OK` - Transaction object

---

### 25. Get Transaction by Transaction ID

**Endpoint:** `GET /api/transactions/transaction-id/{transactionId}`

**Description:** Retrieve transaction by unique 16-character transaction ID.

**Response:** `200 OK` - Transaction object

---

### 26. Get Transactions by Card Number

**Endpoint:** `GET /api/transactions/card/{cardNumber}`

**Description:** Retrieve all transactions for a specific card (BR001 - Transaction Grouping).

**Response:** `200 OK` - Paginated list of transactions

---

### 27. Get Transactions by Account ID

**Endpoint:** `GET /api/transactions/account/{accountId}`

**Description:** Retrieve all transactions for a specific account.

**Response:** `200 OK` - Paginated list of transactions

---

### 28. Create Transaction

**Endpoint:** `POST /api/transactions`

**Description:** Create a new transaction for a card. Validates BR005 (max 10 transactions per card).

**Request Body:**
```json
{
  "cardNumber": "1234567890123456",
  "transactionId": "TXN1234567890123",
  "transactionTypeCode": "PU",
  "transactionCategoryCode": "5411",
  "transactionSource": "POS",
  "transactionDescription": "Grocery Store Purchase",
  "transactionAmount": -50.25
}
```

**Validation Rules:**
- `cardNumber`: Required, 16 alphanumeric characters
- `transactionId`: Required, 16 alphanumeric characters, unique
- `transactionTypeCode`: Required, 2 characters
- `transactionCategoryCode`: Required, 4 digits
- `transactionSource`: Required, max 10 characters
- `transactionDescription`: Required, max 100 characters
- `transactionAmount`: Required, signed numeric with 2 decimal places

**Response:** `201 Created` - Created transaction object

**Error Responses:**
- `400 Bad Request` - Validation errors or card at max capacity

---

### 29. Update Transaction

**Endpoint:** `PUT /api/transactions/{id}`

**Request Body:** (All fields optional)
```json
{
  "transactionTypeCode": "PU",
  "transactionCategoryCode": "5411",
  "transactionSource": "POS",
  "transactionDescription": "Updated Description",
  "transactionAmount": -50.25
}
```

**Response:** `200 OK` - Updated transaction object

---

### 30. Delete Transaction

**Endpoint:** `DELETE /api/transactions/{id}`

**Response:** `204 No Content`

---

### 31. Get Total Transaction Amount by Card

**Endpoint:** `GET /api/transactions/card/{cardNumber}/total-amount`

**Description:** Calculate total transaction amount for a card (BR003).

**Response:** `200 OK`
```json
-250.75
```

---

### 32. Get Total Transaction Amount by Account

**Endpoint:** `GET /api/transactions/account/{accountId}/total-amount`

**Description:** Calculate total transaction amount for an account (BR003).

**Response:** `200 OK`
```json
-250.75
```

---

## Statement Management APIs

### 33. Get All Statements

**Endpoint:** `GET /api/statements`

**Description:** Retrieve a paginated list of all statements.

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "customerName": "John Michael Doe",
      "customerAddress": "123 Main Street, Apt 4B, CA, USA 90210-1234",
      "accountId": 12345678901,
      "currentBalance": 1500.50,
      "ficoScore": 750,
      "totalTransactionAmount": -250.75,
      "statementPeriodStart": "2024-01-01T00:00:00",
      "statementPeriodEnd": "2024-01-31T23:59:59",
      "plainTextContent": "...",
      "htmlContent": "...",
      "status": "GENERATED",
      "statusDisplayName": "Generated",
      "createdAt": "2024-02-01T10:30:00",
      "updatedAt": "2024-02-01T10:30:00"
    }
  ]
}
```

---

### 34. Get Statement by ID

**Endpoint:** `GET /api/statements/{id}`

**Response:** `200 OK` - Statement object

---

### 35. Get Statements by Account ID

**Endpoint:** `GET /api/statements/account/{accountId}`

**Description:** Retrieve all statements for a specific account.

**Response:** `200 OK` - Paginated list of statements

---

### 36. Get Statements by Customer ID

**Endpoint:** `GET /api/statements/customer/{customerId}`

**Description:** Retrieve all statements for a specific customer.

**Response:** `200 OK` - Paginated list of statements

---

### 37. Get Statements by Status

**Endpoint:** `GET /api/statements/status/{status}`

**Description:** Retrieve statements by status.

**Path Parameters:**
- `status` - One of: GENERATED, SENT, VIEWED, ARCHIVED

**Response:** `200 OK` - Paginated list of statements

---

### 38. Generate Statement

**Endpoint:** `POST /api/statements`

**Description:** Generate a comprehensive statement for an account (BR002). Implements:
- BR003: Transaction Amount Summation
- BR004: Dual Format Statement Output (plain text and HTML)
- BR006: Customer Name Composition
- BR007: Complete Address Display
- BR008: HTML Statement Styling Standards

**Request Body:**
```json
{
  "accountId": 12345678901,
  "statementPeriodStart": "2024-01-01T00:00:00",
  "statementPeriodEnd": "2024-01-31T23:59:59"
}
```

**Validation Rules:**
- `accountId`: Required, 11 digits, must exist

**Response:** `201 Created` - Generated statement object with both plain text and HTML content

---

### 39. Delete Statement

**Endpoint:** `DELETE /api/statements/{id}`

**Response:** `204 No Content`

---

### 40. Get Plain Text Statement

**Endpoint:** `GET /api/statements/{id}/plain-text`

**Description:** Retrieve plain text format of statement (BR004).

**Response:** `200 OK`
```
================================================================================
Financial Services Bank
123 Main Street, New York, NY 10001
================================================================================

CUSTOMER INFORMATION
--------------------------------------------------------------------------------
Name: John Michael Doe
Address: 123 Main Street, Apt 4B, CA, USA 90210-1234

ACCOUNT DETAILS
--------------------------------------------------------------------------------
Account ID: 12345678901
Current Balance: $1500.50
FICO Score: 750

TRANSACTION SUMMARY
--------------------------------------------------------------------------------
Total Transaction Amount: $-250.75
================================================================================
```

---

### 41. Get HTML Statement

**Endpoint:** `GET /api/statements/{id}/html`

**Description:** Retrieve HTML format of statement with styling (BR004, BR008).

**Response:** `200 OK` - HTML content with specific styling standards

---

### 42. Update Statement Status

**Endpoint:** `PUT /api/statements/{id}/status`

**Description:** Update statement status.

**Query Parameters:**
- `status` (required) - New status (GENERATED, SENT, VIEWED, ARCHIVED)

**Response:** `200 OK` - Updated statement object

---

### 43. Regenerate Statement Formats

**Endpoint:** `POST /api/statements/{id}/regenerate`

**Description:** Regenerate both plain text and HTML formats for an existing statement.

**Response:** `200 OK` - Updated statement object

---

## Error Handling

All endpoints return standard HTTP status codes and error responses:

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/customers"
}
```

### Common HTTP Status Codes
- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation errors or business rule violations
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource or constraint violation
- `500 Internal Server Error` - Server error

---

## Data Validation Rules Summary

### Customer
- Customer ID: 9 digits (100000000-999999999)
- First Name: Required, max 25 chars
- Last Name: Required, max 25 chars
- Address Line 1: Required, max 50 chars
- State Code: Required, exactly 2 chars
- Country Code: Required, exactly 3 chars
- ZIP Code: Required, exactly 10 alphanumeric chars
- FICO Score: Required, 0-999

### Account
- Account ID: 11 digits (10000000000-99999999999)
- Current Balance: Required, max 10 integer digits, 2 decimal places
- Credit Limit: Required, max 10 integer digits, 2 decimal places

### Card
- Card Number: Required, exactly 16 alphanumeric chars
- Max 10 transactions per card (BR005)

### Transaction
- Transaction ID: Required, exactly 16 alphanumeric chars, unique
- Transaction Type Code: Required, 2 chars
- Transaction Category Code: Required, 4 digits
- Transaction Source: Required, max 10 chars
- Transaction Description: Required, max 100 chars
- Transaction Amount: Required, signed numeric with 2 decimal places

---

## Business Rule Enforcement

The API enforces all business rules at multiple levels:

1. **Entity Level**: JPA entity validation with @PrePersist and @PreUpdate hooks
2. **DTO Level**: Jakarta validation annotations on request DTOs
3. **Service Level**: Business logic validation and calculations
4. **Database Level**: Constraints and foreign keys in migration scripts

---

## Notes

- All timestamps are in ISO 8601 format
- All monetary amounts use 2 decimal places
- Pagination uses zero-based page numbering
- Default page size is 20 items
- All endpoints support sorting via `sort` query parameter
- The system maintains referential integrity through foreign key constraints
- Statement generation is a comprehensive process that aggregates data from customers, accounts, cards, and transactions

---

## Generated Files Summary

**Total Files Generated:** 40

**Entities:** 5 (Customer, Account, Card, Transaction, Statement)
**Enums:** 1 (StatementStatus)
**DTOs:** 14 (Create/Update/Response for each entity)
**Repositories:** 5 (One per entity)
**Services:** 5 (One per entity)
**Controllers:** 5 (One per entity)
**Database Migrations:** 5 (One per entity)

---

**Document Version:** 1.0  
**Generated:** 2024  
**System:** Card Account Transaction Management System
