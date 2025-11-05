# Card Transaction Lifecycle Management API

## Overview
This API provides comprehensive management of card transactions, accounts, cards, and their relationships. It implements business rules from the CardDemo application including transaction validation, credit limit checking, account balance management, and transaction category tracking.

## Base URL
```
http://localhost:8080/api
```

## API Endpoints

### Transaction Management

#### 1. Create Transaction
- **Endpoint**: `POST /transactions`
- **Description**: Creates a new transaction with comprehensive validation including card verification, account validation, credit limit checking, and account expiration verification
- **Request Body**: CreateTransactionRequestDto
  - cardNumber (string, required, 16 digits): Card number for the transaction
  - typeCode (string, required, 2 digits): Transaction type code
  - categoryCode (string, required, 4 digits): Transaction category code
  - source (string, required, max 10 chars): Transaction source
  - description (string, optional, max 100 chars): Transaction description
  - amount (decimal, required): Transaction amount in format +/-99999999.99
  - merchantId (long, required, 9 digits): Merchant identifier
  - merchantName (string, required, max 50 chars): Merchant name
  - merchantCity (string, optional, max 50 chars): Merchant city
  - merchantZip (string, optional, max 10 chars): Merchant ZIP code
  - originalTimestamp (datetime, required): Original transaction timestamp
  - processingTimestamp (datetime, required): Processing timestamp
- **Response**: TransactionResponseDto (201 Created)
- **Error Codes**:
  - 100: INVALID CARD NUMBER FOUND
  - 101: ACCOUNT RECORD NOT FOUND
  - 102: OVERLIMIT TRANSACTION
  - 103: TRANSACTION RECEIVED AFTER ACCT EXPIRATION

#### 2. Get All Transactions
- **Endpoint**: `GET /transactions`
- **Description**: Retrieves a paginated list of all transactions
- **Query Parameters**:
  - page (integer, default: 0): Page number
  - size (integer, default: 20): Page size
  - sort (string, optional): Sort criteria
- **Response**: Page<TransactionResponseDto> (200 OK)

#### 3. Get Transaction by ID
- **Endpoint**: `GET /transactions/{id}`
- **Description**: Retrieves a specific transaction by database ID
- **Path Parameters**:
  - id (long, required): Transaction database ID
- **Response**: TransactionResponseDto (200 OK) or 404 Not Found

#### 4. Get Transaction by Transaction ID
- **Endpoint**: `GET /transactions/transaction-id/{transactionId}`
- **Description**: Retrieves a specific transaction by transaction ID
- **Path Parameters**:
  - transactionId (string, required): Unique transaction identifier
- **Response**: TransactionResponseDto (200 OK) or 404 Not Found

#### 5. Get Transactions by Card Number
- **Endpoint**: `GET /transactions/card/{cardNumber}`
- **Description**: Retrieves all transactions for a specific card number
- **Path Parameters**:
  - cardNumber (string, required): 16-digit card number
- **Query Parameters**:
  - page (integer, default: 0): Page number
  - size (integer, default: 20): Page size
- **Response**: Page<TransactionResponseDto> (200 OK)

#### 6. Get Transactions by Date Range
- **Endpoint**: `GET /transactions/date-range`
- **Description**: Retrieves transactions within a specific date range
- **Query Parameters**:
  - startDate (datetime, required): Start date in ISO format
  - endDate (datetime, required): End date in ISO format
  - page (integer, default: 0): Page number
  - size (integer, default: 20): Page size
- **Response**: Page<TransactionResponseDto> (200 OK)

#### 7. Delete Transaction
- **Endpoint**: `DELETE /transactions/{id}`
- **Description**: Deletes a transaction by ID
- **Path Parameters**:
  - id (long, required): Transaction database ID
- **Response**: 204 No Content or 404 Not Found

---

### Account Management

#### 1. Create Account
- **Endpoint**: `POST /accounts`
- **Description**: Creates a new account
- **Request Body**: CreateAccountRequestDto
  - accountId (long, required, max 11 digits): Account identifier
  - currentBalance (decimal, required): Current account balance
  - creditLimit (decimal, required): Credit limit
  - currentCycleCredit (decimal, required): Current cycle credit amount
  - currentCycleDebit (decimal, required): Current cycle debit amount
  - expirationDate (date, required): Account expiration date in YYYY-MM-DD format
- **Response**: AccountResponseDto (201 Created)

#### 2. Get Account by ID
- **Endpoint**: `GET /accounts/{accountId}`
- **Description**: Retrieves an account by account ID
- **Path Parameters**:
  - accountId (long, required): Account identifier
- **Response**: AccountResponseDto (200 OK) or 404 Not Found

#### 3. Get All Accounts
- **Endpoint**: `GET /accounts`
- **Description**: Retrieves a paginated list of all accounts
- **Query Parameters**:
  - page (integer, default: 0): Page number
  - size (integer, default: 20): Page size
- **Response**: Page<AccountResponseDto> (200 OK)

#### 4. Delete Account
- **Endpoint**: `DELETE /accounts/{accountId}`
- **Description**: Deletes an account by ID
- **Path Parameters**:
  - accountId (long, required): Account identifier
- **Response**: 204 No Content or 404 Not Found

---

### Card Management

#### 1. Create Card
- **Endpoint**: `POST /cards`
- **Description**: Creates a new card
- **Request Body**: CreateCardRequestDto
  - cardNumber (string, required, 16 digits): Card number
  - status (string, required): Card status
  - cardDetails (string, optional, max 150 chars): Additional card details
- **Response**: CardResponseDto (201 Created)

#### 2. Get Card by Card Number
- **Endpoint**: `GET /cards/{cardNumber}`
- **Description**: Retrieves a card by card number
- **Path Parameters**:
  - cardNumber (string, required): 16-digit card number
- **Response**: CardResponseDto (200 OK) or 404 Not Found

#### 3. Get All Cards
- **Endpoint**: `GET /cards`
- **Description**: Retrieves a paginated list of all cards
- **Query Parameters**:
  - page (integer, default: 0): Page number
  - size (integer, default: 20): Page size
- **Response**: Page<CardResponseDto> (200 OK)

#### 4. Delete Card
- **Endpoint**: `DELETE /cards/{cardNumber}`
- **Description**: Deletes a card by card number
- **Path Parameters**:
  - cardNumber (string, required): 16-digit card number
- **Response**: 204 No Content or 404 Not Found

---

### Card Cross Reference Management

#### 1. Create Card-to-Account Mapping
- **Endpoint**: `POST /card-cross-references`
- **Description**: Creates a new card-to-account mapping
- **Request Body**: CreateCardCrossReferenceRequestDto
  - cardNumber (string, required, 16 digits): Card number
  - accountId (long, required, max 11 digits): Account identifier
  - customerId (long, required, max 9 digits): Customer identifier
- **Response**: CardCrossReferenceResponseDto (201 Created)

#### 2. Get Mapping by Card Number
- **Endpoint**: `GET /card-cross-references/card/{cardNumber}`
- **Description**: Retrieves card-to-account mapping by card number
- **Path Parameters**:
  - cardNumber (string, required): 16-digit card number
- **Response**: CardCrossReferenceResponseDto (200 OK) or 404 Not Found

#### 3. Get Mappings by Account ID
- **Endpoint**: `GET /card-cross-references/account/{accountId}`
- **Description**: Retrieves all card-to-account mappings for a specific account
- **Path Parameters**:
  - accountId (long, required): Account identifier
- **Response**: List<CardCrossReferenceResponseDto> (200 OK)

#### 4. Delete Mapping
- **Endpoint**: `DELETE /card-cross-references/{cardNumber}`
- **Description**: Deletes a card-to-account mapping by card number
- **Path Parameters**:
  - cardNumber (string, required): 16-digit card number
- **Response**: 204 No Content or 404 Not Found

---

## Data Models

### TransactionResponseDto
```json
{
  "id": 1,
  "transactionId": "TXN0000000000001",
  "cardNumber": "1234567890123456",
  "typeCode": "01",
  "categoryCode": "5411",
  "source": "POS",
  "description": "Purchase at grocery store",
  "amount": 12345.67,
  "merchantId": 123456789,
  "merchantName": "ABC Grocery Store",
  "merchantCity": "New York",
  "merchantZip": "10001",
  "originalTimestamp": "2024-01-15T10:30:00",
  "processingTimestamp": "2024-01-15T10:30:05",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### AccountResponseDto
```json
{
  "accountId": 12345678901,
  "currentBalance": 1000.00,
  "creditLimit": 5000.00,
  "currentCycleCredit": 500.00,
  "currentCycleDebit": 200.00,
  "expirationDate": "2025-12-31",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### CardResponseDto
```json
{
  "cardNumber": "1234567890123456",
  "status": "ACTIVE",
  "cardDetails": "Premium card with rewards",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### CardCrossReferenceResponseDto
```json
{
  "cardNumber": "1234567890123456",
  "accountId": 12345678901,
  "customerId": 123456789,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

## Business Rules Implementation

### Transaction Validation (CBTRN01C & CBTRN02C)
1. **Card Number Validation**: Verifies card exists in cross-reference file (Error Code 100)
2. **Account Validation**: Confirms account exists and is valid (Error Code 101)
3. **Credit Limit Check**: Validates transaction doesn't exceed available credit (Error Code 102)
4. **Expiration Date Check**: Ensures transaction is before account expiration (Error Code 103)

### Transaction Processing (CBTRN02C)
1. **Sequential ID Generation**: Automatically generates unique transaction IDs
2. **Account Balance Updates**: Updates current balance and cycle credit/debit
3. **Category Balance Tracking**: Maintains transaction category balances
4. **Timestamp Management**: Records both original and processing timestamps

### Transaction Reporting (CBTRN03C)
1. **Date Range Filtering**: Supports filtering transactions by processing timestamp
2. **Card Number Filtering**: Retrieves all transactions for specific cards
3. **Pagination Support**: All list endpoints support pagination

---

## Error Handling

All endpoints return standard HTTP status codes:
- **200 OK**: Successful GET request
- **201 Created**: Successful POST request
- **204 No Content**: Successful DELETE request
- **400 Bad Request**: Invalid request data or validation failure
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

Error responses include detailed messages for validation failures and business rule violations.

---

## Authentication & Authorization
*Note: Authentication and authorization mechanisms should be implemented based on organizational security requirements.*

---

## Rate Limiting
*Note: Rate limiting policies should be configured based on operational requirements.*

---

## Versioning
API Version: 1.0
The API follows semantic versioning principles.
