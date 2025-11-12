# Card Demo Application - API Documentation

## Overview

This document provides a comprehensive summary of all REST API endpoints generated for the Card Demo Application. The application is built using Spring Boot 3.5.5 with Java 21, following a clean layered architecture pattern.

**Base URL:** `http://localhost:8080`

**Application:** CBACT01C - Account Data File Reader and Printer (Card Demo)

---

## Table of Contents

1. [Account Management APIs](#account-management-apis)
2. [Card Record Management APIs](#card-record-management-apis)
3. [Customer Management APIs](#customer-management-apis)
4. [Card Management APIs](#card-management-apis)
5. [Transaction Management APIs](#transaction-management-apis)
6. [Account Cross-Reference APIs](#account-cross-reference-apis)
7. [Card Cross-Reference APIs](#card-cross-reference-apis)
8. [Transaction Category Balance APIs](#transaction-category-balance-apis)
9. [Disclosure Group APIs](#disclosure-group-apis)
10. [Statement APIs](#statement-apis)
11. [Daily Transaction APIs](#daily-transaction-apis)
12. [Merchant APIs](#merchant-apis)
13. [Rejected Transaction APIs](#rejected-transaction-apis)
14. [Transaction Type APIs](#transaction-type-apis)
15. [Transaction Category APIs](#transaction-category-apis)
16. [Date Parameter APIs](#date-parameter-apis)
17. [Report Totals APIs](#report-totals-apis)
18. [Transaction Report APIs](#transaction-report-apis)
19. [Job Submission APIs](#job-submission-apis)
20. [Page State APIs](#page-state-apis)
21. [Date Validation APIs](#date-validation-apis)
22. [Lilian Date APIs](#lilian-date-apis)
23. [Credit Card APIs](#credit-card-apis)
24. [User APIs](#user-apis)
25. [Pagination Context APIs](#pagination-context-apis)
26. [Admin User APIs](#admin-user-apis)
27. [Admin Menu Option APIs](#admin-menu-option-apis)
28. [User Session APIs](#user-session-apis)
29. [Menu Option APIs](#menu-option-apis)
30. [User List Page APIs](#user-list-page-apis)
31. [User Selection APIs](#user-selection-apis)
32. [Common Response Codes](#common-response-codes)

---

## 1. Account Management APIs

### Base Path: `/api/accounts`

#### 1.1 Get All Accounts
- **Endpoint:** `GET /api/accounts`
- **Description:** Retrieve a paginated list of all accounts from the VSAM KSDS file
- **Parameters:**
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response:** `200 OK`
  ```json
  {
    "content": [
      {
        "accountId": "12345678901",
        "activeStatus": "Y",
        "activeStatusDisplay": "Active",
        "currentBalance": 2500.75,
        "creditLimit": 10000.00,
        "cashCreditLimit": 2000.00,
        "openDate": "20230115",
        "expirationDate": "20261231",
        "reissueDate": "20240601",
        "currentCycleCredit": 750.00,
        "currentCycleDebit": 450.00,
        "groupId": "GROUP12345",
        "accountData": "...",
        "availableCredit": 7500.00,
        "availableCashCredit": 1500.00,
        "creditUtilizationPercentage": 25.00,
        "netCycleAmount": 300.00,
        "isExpired": false,
        "daysUntilExpiration": 365,
        "hasOutstandingBalance": true,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-15T10:30:00"
      }
    ],
    "pageable": {...},
    "totalElements": 100,
    "totalPages": 5
  }
  ```

#### 1.2 Get Account by ID
- **Endpoint:** `GET /api/accounts/{id}`
- **Description:** Retrieve a specific account by its 11-digit account ID
- **Parameters:**
  - `id` (path, required): 11-digit account ID
- **Response:** `200 OK` - Account details
- **Error Responses:**
  - `400 Bad Request` - Invalid account ID format
  - `404 Not Found` - Account not found

#### 1.3 Create Account
- **Endpoint:** `POST /api/accounts`
- **Description:** Create a new account record in the account master file
- **Request Body:**
  ```json
  {
    "accountId": "12345678901",
    "activeStatus": "Y",
    "currentBalance": 0.00,
    "creditLimit": 10000.00,
    "cashCreditLimit": 2000.00,
    "openDate": "20240115",
    "expirationDate": "20261231",
    "reissueDate": "20240601",
    "currentCycleCredit": 0.00,
    "currentCycleDebit": 0.00,
    "groupId": "GROUP001",
    "accountData": "..."
  }
  ```
- **Response:** `201 Created` - Created account details
- **Error Responses:**
  - `400 Bad Request` - Validation failure

#### 1.4 Update Account
- **Endpoint:** `PUT /api/accounts/{id}`
- **Description:** Update account details by 11-digit account ID
- **Parameters:**
  - `id` (path, required): 11-digit account ID
- **Request Body:** Partial account update (all fields optional)
- **Response:** `200 OK` - Updated account details
- **Error Responses:**
  - `400 Bad Request` - Validation failure
  - `404 Not Found` - Account not found

#### 1.5 Delete Account
- **Endpoint:** `DELETE /api/accounts/{id}`
- **Description:** Delete an account by its 11-digit account ID
- **Parameters:**
  - `id` (path, required): 11-digit account ID
- **Response:** `204 No Content`
- **Error Responses:**
  - `404 Not Found` - Account not found

#### 1.6 Process Accounts Sequentially
- **Endpoint:** `POST /api/accounts/process-sequential`
- **Description:** Process all account records sequentially from the VSAM KSDS file until end-of-file is reached (BR-001)
- **Response:** `200 OK` - List of processed accounts
- **Business Rule:** BR-001 - Sequential Account Record Processing

#### 1.7 Calculate Interest
- **Endpoint:** `POST /api/accounts/{id}/calculate-interest`
- **Description:** Calculate interest separately for each transaction category within an account (BR001)
- **Parameters:**
  - `id` (path, required): Account ID
  - `categoryCode` (query, required): Transaction category code
  - `transactionTypeCode` (query, required): Transaction type code
  - `categoryBalance` (query, required): Category balance amount
- **Response:** `200 OK` - Calculated interest amount
- **Business Rule:** BR001 - Interest Calculation by Transaction Category

#### 1.8 Update Account Balance
- **Endpoint:** `POST /api/accounts/{id}/update-balance`
- **Description:** Update account balance with total accumulated interest and reset cycle amounts (BR003)
- **Parameters:**
  - `id` (path, required): Account ID
  - `totalInterest` (query, required): Total interest amount
- **Response:** `200 OK` - Success message
- **Business Rule:** BR003 - Account Balance Update

#### 1.9 Validate Account for Payment
- **Endpoint:** `GET /api/accounts/{id}/validate-payment`
- **Description:** Validate that account has a balance greater than zero for payment processing
- **Parameters:**
  - `id` (path, required): Account ID
- **Response:** `200 OK` - Validation success message
- **Error Responses:**
  - `400 Bad Request` - "You have nothing to pay..."

---

## 2. Card Record Management APIs

### Base Path: `/api/card-records`

#### 2.1 Get All Card Records
- **Endpoint:** `GET /api/card-records`
- **Description:** Retrieve a paginated list of all card records
- **Parameters:** Standard pagination parameters
- **Response:** `200 OK` - Paginated card records

#### 2.2 Get Card Record by Number
- **Endpoint:** `GET /api/card-records/{cardNumber}`
- **Description:** Retrieve a specific card record by its 16-digit card number
- **Parameters:**
  - `cardNumber` (path, required): 16-digit card number
- **Response:** `200 OK` - Card record details
- **Validations:**
  - Card number must be exactly 16 numeric digits
  - Cannot be all zeros or blank

#### 2.3 Create Card Record
- **Endpoint:** `POST /api/card-records`
- **Description:** Create a new card record in the card data file
- **Request Body:**
  ```json
  {
    "cardNumber": "1234567890123456",
    "cardData": "..."
  }
  ```
- **Response:** `201 Created`
- **Validations:**
  - Card number must be 16 characters alphanumeric
  - Must exist in cross-reference file

#### 2.4 Update Card Record
- **Endpoint:** `PUT /api/card-records/{cardNumber}`
- **Description:** Update card record data
- **Response:** `200 OK`

#### 2.5 Delete Card Record
- **Endpoint:** `DELETE /api/card-records/{cardNumber}`
- **Description:** Delete a card record
- **Response:** `204 No Content`

---

## 3. Customer Management APIs

### Base Path: `/api/customers`

#### 3.1 Get All Customers
- **Endpoint:** `GET /api/customers`
- **Description:** Retrieve a paginated list of all customers
- **Response:** `200 OK` - Paginated customer list

#### 3.2 Get Customer by ID
- **Endpoint:** `GET /api/customers/{id}`
- **Description:** Retrieve a specific customer by their 9-digit customer ID
- **Parameters:**
  - `id` (path, required): 9-digit customer ID
- **Response:** `200 OK`
  ```json
  {
    "customerId": "123456789",
    "ssn": "123456789",
    "formattedSsn": "123-45-6789",
    "firstName": "John",
    "middleName": "Michael",
    "lastName": "Doe",
    "fullName": "John Michael Doe",
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 4B",
    "city": "New York",
    "stateCode": "NY",
    "countryCode": "USA",
    "zipCode": "10001",
    "phoneNumber1": "(212)555-1234",
    "phoneNumber2": "(212)555-5678",
    "dateOfBirth": "19900115",
    "age": 34,
    "governmentId": "DL123456",
    "eftAccountId": "1234567890",
    "primaryCardHolderIndicator": "Y",
    "isPrimaryCardHolder": true,
    "ficoCreditScore": 750,
    "customerData": "...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
  ```
- **Validations:**
  - Customer ID must be 9 digits numeric
  - Must exist in customer master file

#### 3.3 Create Customer
- **Endpoint:** `POST /api/customers`
- **Description:** Create a new customer record
- **Request Body:** Customer details with all required fields
- **Response:** `201 Created`
- **Validations:**
  - SSN must be valid 9-digit number (cannot start with 000, 666, or 900-999)
  - First name and last name required, alphabetic only
  - Date of birth must be valid, customer must be at least 18 years old
  - Phone numbers must be in format (XXX)XXX-XXXX
  - FICO score must be between 300 and 850
  - Primary holder indicator must be Y or N

#### 3.4 Update Customer
- **Endpoint:** `PUT /api/customers/{id}`
- **Description:** Update customer details
- **Response:** `200 OK`

#### 3.5 Delete Customer
- **Endpoint:** `DELETE /api/customers/{id}`
- **Description:** Delete a customer record
- **Response:** `204 No Content`

---

## 4. Card Management APIs

### Base Path: `/api/cards`

#### 4.1 Get All Cards
- **Endpoint:** `GET /api/cards`
- **Description:** Retrieve all card cross-reference information
- **Response:** `200 OK`

#### 4.2 Get Card by Number
- **Endpoint:** `GET /api/cards/{cardNumber}`
- **Description:** Retrieve card details by 16-digit card number
- **Response:** `200 OK`
  ```json
  {
    "cardNumber": "1234567890123456",
    "accountId": "12345678901",
    "customerId": "123456789",
    "cvvCode": "123",
    "expirationDate": "2026-12-31",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
  ```

#### 4.3 Create Card
- **Endpoint:** `POST /api/cards`
- **Description:** Create a new card cross-reference
- **Request Body:**
  ```json
  {
    "cardNumber": "1234567890123456",
    "accountId": "12345678901",
    "customerId": "123456789",
    "cvvCode": "123",
    "expirationDate": "2026-12-31"
  }
  ```
- **Response:** `201 Created`
- **Validations:**
  - Card number must be 16 digits
  - Account ID must exist in account master file
  - Customer ID must exist in customer master file
  - CVV must be 3 digits
  - Expiration date must be valid future date

#### 4.4 Update Card
- **Endpoint:** `PUT /api/cards/{cardNumber}`
- **Response:** `200 OK`

#### 4.5 Delete Card
- **Endpoint:** `DELETE /api/cards/{cardNumber}`
- **Response:** `204 No Content`

---

## 5. Transaction Management APIs

### Base Path: `/api/transactions`

#### 5.1 Get All Transactions
- **Endpoint:** `GET /api/transactions`
- **Description:** Retrieve all transaction records
- **Parameters:** Pagination and filtering options
- **Response:** `200 OK`

#### 5.2 Get Transaction by ID
- **Endpoint:** `GET /api/transactions/{id}`
- **Description:** Retrieve transaction details by transaction ID
- **Parameters:**
  - `id` (path, required): 16-character transaction ID
- **Response:** `200 OK`
  ```json
  {
    "transactionId": "2024011500012345",
    "cardNumber": "1234567890123456",
    "transactionTypeCode": "01",
    "transactionCategoryCode": "05",
    "transactionSource": "System",
    "transactionDescription": "Interest for Account 12345678901",
    "transactionAmount": 45.50,
    "merchantId": "000000000",
    "merchantName": "",
    "merchantCity": "",
    "merchantZip": "",
    "originalTimestamp": "2024-01-15-10.30.00.000000",
    "processingTimestamp": "2024-01-15-10.30.00.000000",
    "transactionDate": "01/15/24",
    "accountData": "...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
  ```

#### 5.3 Create Transaction
- **Endpoint:** `POST /api/transactions`
- **Description:** Create a new transaction record
- **Request Body:** Transaction details
- **Response:** `201 Created`
- **Validations:**
  - Transaction ID must be 16 characters alphanumeric and unique
  - Card number must exist in cross-reference file
  - Transaction type code must exist in reference file
  - Transaction amount must be valid signed numeric with 2 decimal places
  - Must not cause account balance to exceed credit limit

#### 5.4 Update Transaction
- **Endpoint:** `PUT /api/transactions/{id}`
- **Response:** `200 OK`

#### 5.5 Delete Transaction
- **Endpoint:** `DELETE /api/transactions/{id}`
- **Response:** `204 No Content`

#### 5.6 Get Transactions by Card Number
- **Endpoint:** `GET /api/transactions/by-card/{cardNumber}`
- **Description:** Retrieve all transactions for a specific card
- **Response:** `200 OK`

#### 5.7 Get Transactions by Date Range
- **Endpoint:** `GET /api/transactions/by-date-range`
- **Description:** Retrieve transactions within a date range
- **Parameters:**
  - `startDate` (query, required): Start date (YYYYMMDD)
  - `endDate` (query, required): End date (YYYYMMDD)
- **Response:** `200 OK`

---

## 6. Account Cross-Reference APIs

### Base Path: `/api/account-cross-references`

#### 6.1 Get All Account Cross-References
- **Endpoint:** `GET /api/account-cross-references`
- **Description:** Retrieve all account cross-reference records
- **Response:** `200 OK`
- **Business Rule:** BR001 - Sequential Record Processing

#### 6.2 Get Cross-Reference by Account ID
- **Endpoint:** `GET /api/account-cross-references/by-account/{accountId}`
- **Response:** `200 OK`

#### 6.3 Create Account Cross-Reference
- **Endpoint:** `POST /api/account-cross-references`
- **Response:** `201 Created`

#### 6.4 Update Account Cross-Reference
- **Endpoint:** `PUT /api/account-cross-references/{id}`
- **Response:** `200 OK`

#### 6.5 Delete Account Cross-Reference
- **Endpoint:** `DELETE /api/account-cross-references/{id}`
- **Response:** `204 No Content`

---

## 7. Card Cross-Reference APIs

### Base Path: `/api/card-cross-references`

Similar CRUD operations as Account Cross-Reference APIs.

---

## 8. Transaction Category Balance APIs

### Base Path: `/api/transaction-category-balances`

#### 8.1 Get All Category Balances
- **Endpoint:** `GET /api/transaction-category-balances`
- **Response:** `200 OK`

#### 8.2 Get Category Balance by Account and Category
- **Endpoint:** `GET /api/transaction-category-balances/by-account/{accountId}/category/{categoryCode}`
- **Response:** `200 OK`

#### 8.3 Create Category Balance
- **Endpoint:** `POST /api/transaction-category-balances`
- **Response:** `201 Created`

#### 8.4 Update Category Balance
- **Endpoint:** `PUT /api/transaction-category-balances/{id}`
- **Response:** `200 OK`

---

## 9. Disclosure Group APIs

### Base Path: `/api/disclosure-groups`

Standard CRUD operations for disclosure group management.

---

## 10. Statement APIs

### Base Path: `/api/statements`

#### 10.1 Get All Statements
- **Endpoint:** `GET /api/statements`
- **Response:** `200 OK`

#### 10.2 Get Statement by ID
- **Endpoint:** `GET /api/statements/{id}`
- **Response:** `200 OK`

#### 10.3 Get Statements by Account
- **Endpoint:** `GET /api/statements/by-account/{accountId}`
- **Response:** `200 OK`

#### 10.4 Create Statement
- **Endpoint:** `POST /api/statements`
- **Response:** `201 Created`

---

## 11. Daily Transaction APIs

### Base Path: `/api/daily-transactions`

Standard CRUD operations for daily transaction management.

---

## 12. Merchant APIs

### Base Path: `/api/merchants`

#### 12.1 Get All Merchants
- **Endpoint:** `GET /api/merchants`
- **Response:** `200 OK`

#### 12.2 Get Merchant by ID
- **Endpoint:** `GET /api/merchants/{id}`
- **Response:** `200 OK`

#### 12.3 Create Merchant
- **Endpoint:** `POST /api/merchants`
- **Response:** `201 Created`

#### 12.4 Update Merchant
- **Endpoint:** `PUT /api/merchants/{id}`
- **Response:** `200 OK`

#### 12.5 Delete Merchant
- **Endpoint:** `DELETE /api/merchants/{id}`
- **Response:** `204 No Content`

---

## 13. Rejected Transaction APIs

### Base Path: `/api/rejected-transactions`

Standard CRUD operations for rejected transaction management.

---

## 14. Transaction Type APIs

### Base Path: `/api/transaction-types`

Reference data APIs for transaction types.

---

## 15. Transaction Category APIs

### Base Path: `/api/transaction-categories`

Reference data APIs for transaction categories.

---

## 16. Date Parameter APIs

### Base Path: `/api/date-parameters`

APIs for managing date parameters used in batch processing.

---

## 17. Report Totals APIs

### Base Path: `/api/report-totals`

APIs for managing and retrieving report totals.

---

## 18. Transaction Report APIs

### Base Path: `/api/transaction-reports`

APIs for generating and retrieving transaction reports.

---

## 19. Job Submission APIs

### Base Path: `/api/job-submissions`

APIs for batch job submission and monitoring.

---

## 20. Page State APIs

### Base Path: `/api/page-states`

APIs for managing pagination state across sessions.

---

## 21. Date Validation APIs

### Base Path: `/api/date-validations`

#### 21.1 Validate Date
- **Endpoint:** `POST /api/date-validations/validate`
- **Request Body:**
  ```json
  {
    "dateString": "20240115",
    "format": "YYYYMMDD"
  }
  ```
- **Response:** `200 OK`
  ```json
  {
    "isValid": true,
    "parsedDate": "2024-01-15",
    "errorMessage": null
  }
  ```

---

## 22. Lilian Date APIs

### Base Path: `/api/lilian-dates`

APIs for Lilian date conversion and manipulation.

---

## 23. Credit Card APIs

### Base Path: `/api/credit-cards`

Extended card management APIs with credit-specific features.

---

## 24. User APIs

### Base Path: `/api/users`

Standard user management CRUD operations.

---

## 25. Pagination Context APIs

### Base Path: `/api/pagination-contexts`

APIs for managing pagination context.

---

## 26. Admin User APIs

### Base Path: `/api/admin-users`

Administrative user management APIs.

---

## 27. Admin Menu Option APIs

### Base Path: `/api/admin-menu-options`

APIs for managing administrative menu options.

---

## 28. User Session APIs

### Base Path: `/api/user-sessions`

APIs for user session management.

---

## 29. Menu Option APIs

### Base Path: `/api/menu-options`

APIs for managing menu options.

---

## 30. User List Page APIs

### Base Path: `/api/user-list-pages`

APIs for user list page management.

---

## 31. User Selection APIs

### Base Path: `/api/user-selections`

APIs for managing user selections.

---

## 32. Common Response Codes

### Success Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Resource deleted successfully

### Client Error Codes
- `400 Bad Request` - Invalid request data or validation failure
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists

### Server Error Codes
- `500 Internal Server Error` - Unexpected server error

---

## Error Response Format

All error responses follow a consistent format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/accounts",
  "errors": [
    {
      "field": "accountId",
      "message": "Account Filter must be a non-zero 11 digit number"
    }
  ]
}
```

---

## Authentication & Authorization

**Note:** Authentication and authorization mechanisms should be implemented based on organizational security requirements. Consider implementing:
- JWT-based authentication
- OAuth 2.0
- Role-based access control (RBAC)

---

## Rate Limiting

Consider implementing rate limiting to protect the API:
- Default: 100 requests per minute per IP
- Authenticated users: 1000 requests per minute

---

## Versioning

API versioning strategy:
- Current version: v1
- Version specified in URL: `/api/v1/accounts`
- Backward compatibility maintained for at least 2 major versions

---

## Additional Notes

### Business Rules Implementation

The APIs implement the following key business rules:

1. **BR-001**: Sequential Account Record Processing
2. **BR-002**: Account Data Display Requirements
3. **BR-003**: Account File Access Control
4. **BR-004**: End of File Detection
5. **BR001**: Interest Calculation by Transaction Category
6. **BR002**: Interest Rate Determination
7. **BR003**: Account Balance Update
8. **BR004**: Interest Transaction Generation
9. **BR005**: Account Processing Sequence
10. **BR006**: Primary Card Holder Designation

### Data Validation

All APIs implement comprehensive data validation including:
- Field length validation
- Format validation (numeric, alphanumeric, date formats)
- Business rule validation
- Cross-reference validation
- Range validation

### Transaction Management

All write operations (POST, PUT, DELETE) are wrapped in database transactions to ensure data consistency.

### Logging

All API operations are logged with appropriate log levels:
- INFO: Successful operations
- WARN: Validation failures
- ERROR: System errors

---

## Contact & Support

For API support and questions, please contact the development team.

**Generated:** 2024-01-15
**Version:** 1.0.0
**Application:** Card Demo (CBACT01C)
