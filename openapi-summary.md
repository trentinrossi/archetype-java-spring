# OpenAPI Summary - Account and Customer Management System

## API Overview

**Version**: 1.0.0  
**Base URL**: http://localhost:8080  
**API Documentation**: http://localhost:8080/swagger-ui.html

This document provides a comprehensive summary of all REST API endpoints available in the Account and Customer Management System.

---

## 1. Customer Management API

**Base Path**: `/api/customers`

### Endpoints

#### 1.1 Get All Customers
- **Method**: `GET`
- **Path**: `/api/customers`
- **Description**: Retrieve a paginated list of all customers with their complete profile information
- **Query Parameters**:
  - `page` (integer, optional): Page number (default: 0)
  - `size` (integer, optional): Page size (default: 20)
  - `sort` (string, optional): Sort criteria
- **Response Codes**:
  - `200 OK`: Successful retrieval of customers
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error
- **Response Body**: Page<CustomerResponseDto>

#### 1.2 Get Customer by ID
- **Method**: `GET`
- **Path**: `/api/customers/{customerId}`
- **Description**: Retrieve a customer by their unique 9-character customer identification number
- **Path Parameters**:
  - `customerId` (Long, required): Customer ID
- **Response Codes**:
  - `200 OK`: Successful retrieval of customer
  - `400 Bad Request`: Invalid customer ID format
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error
- **Response Body**: CustomerResponseDto

#### 1.3 Create Customer
- **Method**: `POST`
- **Path**: `/api/customers`
- **Description**: Create a new customer with demographic information, addresses, contact information, SSN, and credit scores
- **Request Body**: CreateCustomerRequestDto
  ```json
  {
    "customerId": "123456789",
    "customerData": "Customer profile data..."
  }
  ```
- **Validations**:
  - Customer ID: Required, exactly 9 numeric digits
  - Customer data: Required, max 491 characters
- **Response Codes**:
  - `201 Created`: Customer created successfully
  - `400 Bad Request`: Invalid request data or customer ID format
  - `500 Internal Server Error`: Server error
- **Response Body**: CustomerResponseDto

#### 1.4 Update Customer
- **Method**: `PUT`
- **Path**: `/api/customers/{customerId}`
- **Description**: Update customer details by customer ID including demographic information and contact details
- **Path Parameters**:
  - `customerId` (Long, required): Customer ID
- **Request Body**: UpdateCustomerRequestDto
  ```json
  {
    "customerData": "Updated customer profile data..."
  }
  ```
- **Response Codes**:
  - `200 OK`: Customer updated successfully
  - `400 Bad Request`: Invalid request data or customer ID format
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error
- **Response Body**: CustomerResponseDto

#### 1.5 Delete Customer
- **Method**: `DELETE`
- **Path**: `/api/customers/{customerId}`
- **Description**: Delete a customer by their unique customer ID
- **Path Parameters**:
  - `customerId` (Long, required): Customer ID
- **Response Codes**:
  - `204 No Content`: Customer deleted successfully
  - `400 Bad Request`: Invalid customer ID format
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error

---

## 2. Account Management API

**Base Path**: `/api/accounts`

### Endpoints

#### 2.1 Get All Accounts
- **Method**: `GET`
- **Path**: `/api/accounts`
- **Description**: Retrieve a paginated list of all accounts with master data
- **Query Parameters**:
  - `page` (integer, optional): Page number (default: 0)
  - `size` (integer, optional): Page size (default: 20)
  - `sort` (string, optional): Sort criteria
- **Response Codes**:
  - `200 OK`: Successful retrieval of accounts
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error
- **Response Body**: Page<AccountResponseDto>

#### 2.2 Get Account by ID
- **Method**: `GET`
- **Path**: `/api/accounts/{accountId}`
- **Description**: Retrieve account master data by account ID using random access pattern
- **Path Parameters**:
  - `accountId` (Long, required): Account ID
- **Response Codes**:
  - `200 OK`: Successful retrieval of account
  - `400 Bad Request`: Invalid account ID format - must be 11 numeric digits
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error
- **Response Body**: AccountResponseDto

#### 2.3 Create Account
- **Method**: `POST`
- **Path**: `/api/accounts`
- **Description**: Create a new account with master data including balances, credit limits, and status
- **Request Body**: CreateAccountRequestDto
  ```json
  {
    "accountId": "12345678901",
    "accountData": "Account data containing balance and credit information",
    "customerId": 123456789
  }
  ```
- **Validations**:
  - Account ID: Required, exactly 11 numeric digits
  - Account data: Required, max 289 characters
  - Customer ID: Required, must reference existing customer
- **Response Codes**:
  - `201 Created`: Account created successfully
  - `400 Bad Request`: Invalid request data or account ID format
  - `500 Internal Server Error`: Server error
- **Response Body**: AccountResponseDto

#### 2.4 Update Account
- **Method**: `PUT`
- **Path**: `/api/accounts/{accountId}`
- **Description**: Update account master data by account ID
- **Path Parameters**:
  - `accountId` (Long, required): Account ID
- **Request Body**: UpdateAccountRequestDto
  ```json
  {
    "accountData": "Updated account data"
  }
  ```
- **Response Codes**:
  - `200 OK`: Account updated successfully
  - `400 Bad Request`: Invalid request data or account ID format
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error
- **Response Body**: AccountResponseDto

#### 2.5 Delete Account
- **Method**: `DELETE`
- **Path**: `/api/accounts/{accountId}`
- **Description**: Delete an account by account ID
- **Path Parameters**:
  - `accountId` (Long, required): Account ID
- **Response Codes**:
  - `204 No Content`: Account deleted successfully
  - `400 Bad Request`: Invalid account ID format
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

---

## 3. Card Cross-Reference Management API

**Base Path**: `/api/card-cross-references`

### Endpoints

#### 3.1 Get All Card Cross-References
- **Method**: `GET`
- **Path**: `/api/card-cross-references`
- **Description**: Retrieve a paginated list of all card cross-references
- **Query Parameters**:
  - `page` (integer, optional): Page number (default: 0)
  - `size` (integer, optional): Page size (default: 20)
  - `sort` (string, optional): Sort criteria
- **Response Codes**:
  - `200 OK`: Successful retrieval of card cross-references
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error
- **Response Body**: Page<CardCrossReferenceResponseDto>

#### 3.2 Get Card Cross-Reference by Card Number
- **Method**: `GET`
- **Path**: `/api/card-cross-references/{cardNumber}`
- **Description**: Retrieve a card cross-reference by its card number
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
- **Response Codes**:
  - `200 OK`: Successful retrieval of card cross-reference
  - `404 Not Found`: Card cross-reference not found
  - `500 Internal Server Error`: Server error
- **Response Body**: CardCrossReferenceResponseDto

#### 3.3 Get Card Cross-References by Customer ID
- **Method**: `GET`
- **Path**: `/api/card-cross-references/customer/{customerId}`
- **Description**: Retrieve all card cross-references for a specific customer
- **Path Parameters**:
  - `customerId` (Long, required): Customer ID
- **Response Codes**:
  - `200 OK`: Successful retrieval of card cross-references
  - `500 Internal Server Error`: Server error
- **Response Body**: List<CardCrossReferenceResponseDto>

#### 3.4 Get Card Cross-References by Account ID
- **Method**: `GET`
- **Path**: `/api/card-cross-references/account/{accountId}`
- **Description**: Retrieve all card cross-references for a specific account
- **Path Parameters**:
  - `accountId` (Long, required): Account ID
- **Response Codes**:
  - `200 OK`: Successful retrieval of card cross-references
  - `500 Internal Server Error`: Server error
- **Response Body**: List<CardCrossReferenceResponseDto>

#### 3.5 Create Card Cross-Reference
- **Method**: `POST`
- **Path**: `/api/card-cross-references`
- **Description**: Create a new card cross-reference linking card number to customer and account
- **Request Body**: CreateCardCrossReferenceRequestDto
  ```json
  {
    "cardNumber": "1234567890123456",
    "crossReferenceData": "CUST123456789012ACCT123456789012",
    "customerId": 123456789,
    "accountId": 12345678901
  }
  ```
- **Validations**:
  - Card number: Required, exactly 16 characters
  - Cross reference data: Required, max 34 characters
  - Customer ID: Required, must reference existing customer
  - Account ID: Required, must reference existing account
- **Response Codes**:
  - `201 Created`: Card cross-reference created successfully
  - `400 Bad Request`: Invalid request data
  - `500 Internal Server Error`: Server error
- **Response Body**: CardCrossReferenceResponseDto

#### 3.6 Update Card Cross-Reference
- **Method**: `PUT`
- **Path**: `/api/card-cross-references/{cardNumber}`
- **Description**: Update card cross-reference details by card number
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
- **Request Body**: UpdateCardCrossReferenceRequestDto
  ```json
  {
    "crossReferenceData": "Updated cross reference data"
  }
  ```
- **Response Codes**:
  - `200 OK`: Card cross-reference updated successfully
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Card cross-reference not found
  - `500 Internal Server Error`: Server error
- **Response Body**: CardCrossReferenceResponseDto

#### 3.7 Delete Card Cross-Reference
- **Method**: `DELETE`
- **Path**: `/api/card-cross-references/{cardNumber}`
- **Description**: Delete a card cross-reference by card number
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
- **Response Codes**:
  - `204 No Content`: Card cross-reference deleted successfully
  - `404 Not Found`: Card cross-reference not found
  - `500 Internal Server Error`: Server error

---

## 4. Transaction Management API

**Base Path**: `/api/transactions`

### Endpoints

#### 4.1 Get All Transactions
- **Method**: `GET`
- **Path**: `/api/transactions`
- **Description**: Retrieve a paginated list of all credit card transactions in sequential order by composite key
- **Query Parameters**:
  - `page` (integer, optional): Page number (default: 0)
  - `size` (integer, optional): Page size (default: 20)
  - `sort` (string, optional): Sort criteria
- **Response Codes**:
  - `200 OK`: Successful retrieval of transactions
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error
- **Response Body**: Page<TransactionResponseDto>

#### 4.2 Get Transaction by Composite Key
- **Method**: `GET`
- **Path**: `/api/transactions/{cardNumber}/{transactionId}`
- **Description**: Retrieve a transaction by card number and transaction ID
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
  - `transactionId` (String, required): 16-character transaction ID
- **Response Codes**:
  - `200 OK`: Successful retrieval of transaction
  - `400 Bad Request`: Invalid card number or transaction ID length
  - `404 Not Found`: Transaction not found
  - `500 Internal Server Error`: Server error
- **Response Body**: TransactionResponseDto

#### 4.3 Get Transactions by Card Number
- **Method**: `GET`
- **Path**: `/api/transactions/card/{cardNumber}`
- **Description**: Retrieve all transactions for a specific card number in sequential order
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
- **Response Codes**:
  - `200 OK`: Successful retrieval of transactions
  - `400 Bad Request`: Invalid card number length
  - `500 Internal Server Error`: Server error
- **Response Body**: List<TransactionResponseDto>

#### 4.4 Create Transaction
- **Method**: `POST`
- **Path**: `/api/transactions`
- **Description**: Create a new credit card transaction record
- **Request Body**: CreateTransactionRequestDto
  ```json
  {
    "cardNumber": "1234567890123456",
    "transactionId": "TXN1234567890123",
    "transactionData": "Transaction details including amounts, dates, merchant information"
  }
  ```
- **Validations**:
  - Card number: Required, exactly 16 characters
  - Transaction ID: Required, exactly 16 characters
  - Transaction data: Required, max 318 characters
- **Response Codes**:
  - `201 Created`: Transaction created successfully
  - `400 Bad Request`: Invalid request data - card number or transaction ID must be exactly 16 characters
  - `500 Internal Server Error`: Server error
- **Response Body**: TransactionResponseDto

#### 4.5 Update Transaction
- **Method**: `PUT`
- **Path**: `/api/transactions/{cardNumber}/{transactionId}`
- **Description**: Update transaction details by composite key
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
  - `transactionId` (String, required): 16-character transaction ID
- **Request Body**: UpdateTransactionRequestDto
  ```json
  {
    "transactionData": "Updated transaction details"
  }
  ```
- **Response Codes**:
  - `200 OK`: Transaction updated successfully
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Transaction not found
  - `500 Internal Server Error`: Server error
- **Response Body**: TransactionResponseDto

#### 4.6 Delete Transaction
- **Method**: `DELETE`
- **Path**: `/api/transactions/{cardNumber}/{transactionId}`
- **Description**: Delete a transaction by composite key
- **Path Parameters**:
  - `cardNumber` (String, required): 16-character card number
  - `transactionId` (String, required): 16-character transaction ID
- **Response Codes**:
  - `204 No Content`: Transaction deleted successfully
  - `400 Bad Request`: Invalid card number or transaction ID length
  - `404 Not Found`: Transaction not found
  - `500 Internal Server Error`: Server error

---

## Common Response Models

### CustomerResponseDto
```json
{
  "customerId": "123456789",
  "customerData": "Customer profile data...",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

### AccountResponseDto
```json
{
  "accountId": "12345678901",
  "accountData": "Account data...",
  "customerId": 123456789,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

### CardCrossReferenceResponseDto
```json
{
  "cardNumber": "1234567890123456",
  "crossReferenceData": "CUST123456789012ACCT123456789012",
  "customerId": "123456789",
  "accountId": "12345678901",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

### TransactionResponseDto
```json
{
  "cardNumber": "1234567890123456",
  "transactionId": "TXN1234567890123",
  "transactionData": "Transaction details...",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

### ErrorResponse
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/customers",
  "validationErrors": {
    "customerId": "Customer ID must be exactly 9 characters"
  }
}
```

---

## Pagination

All list endpoints support pagination with the following query parameters:
- `page`: Page number (0-indexed, default: 0)
- `size`: Number of items per page (default: 20)
- `sort`: Sort criteria in format `property,direction` (e.g., `customerId,asc`)

Example:
```
GET /api/customers?page=0&size=10&sort=customerId,asc
```

---

## Error Handling

The API uses standard HTTP status codes:
- `200 OK`: Successful GET/PUT request
- `201 Created`: Successful POST request
- `204 No Content`: Successful DELETE request
- `400 Bad Request`: Invalid request data or validation error
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

All error responses include an ErrorResponse object with details about the error.

---

## Authentication & Authorization

Currently, the API does not implement authentication. For production use, consider implementing:
- Spring Security with JWT tokens
- OAuth2 authentication
- API key authentication

---

## Rate Limiting

No rate limiting is currently implemented. For production use, consider implementing rate limiting to prevent abuse.

---

## Versioning

The API is currently at version 1.0.0. Future versions will be indicated in the URL path (e.g., `/api/v2/customers`).

---

## Contact & Support

- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Support Email**: support@example.com
