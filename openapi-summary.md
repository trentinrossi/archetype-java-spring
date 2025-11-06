# API Documentation Summary

## Overview

This document provides a comprehensive summary of all REST API endpoints generated for the Account and Customer Management System. The application is built using Spring Boot 3.5.5 with Java 21, PostgreSQL, and follows a clean layered architecture.

## Base URL

```
http://localhost:8080/api
```

## Entities

The system manages four main entities:
1. **Account** - Account master data with balances, credit limits, and status
2. **Customer** - Customer master data with demographics and contact information
3. **Transaction** - Credit card transaction records
4. **CardCrossReference** - Relationships between cards, customers, and accounts

---

## 1. Account Management API

**Base Path:** `/api/accounts`

### Endpoints

#### GET /api/accounts
**Summary:** Get all accounts  
**Description:** Retrieve a paginated list of all accounts with balance, credit limit, dates, and status information  
**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "accountId": 12345678901,
      "accountData": "Account balance, credit limit, dates, and status information...",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-20T14:45:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

#### GET /api/accounts/{accountId}
**Summary:** Get account by ID  
**Description:** Retrieve an account by their unique 11-digit account identification number  
**Parameters:**
- `accountId` (path, required): Account ID (Long)

**Response:** `200 OK` | `404 Not Found`

#### POST /api/accounts
**Summary:** Create a new account  
**Description:** Create a new account with balance, credit limit, dates, and status information  
**Request Body:**
```json
{
  "accountId": 12345678901,
  "accountData": "Account balance, credit limit, dates, and status information..."
}
```
**Validation:**
- `accountId`: Must be 11 numeric digits
- `accountData`: Required, max 289 characters

**Response:** `201 Created` | `400 Bad Request`

#### PUT /api/accounts/{accountId}
**Summary:** Update an existing account  
**Description:** Update account details by account ID  
**Parameters:**
- `accountId` (path, required): Account ID (Long)

**Request Body:**
```json
{
  "accountData": "Updated account data..."
}
```

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### DELETE /api/accounts/{accountId}
**Summary:** Delete an account  
**Description:** Delete an account by account ID  
**Parameters:**
- `accountId` (path, required): Account ID (Long)

**Response:** `204 No Content` | `404 Not Found`

---

## 2. Customer Management API

**Base Path:** `/api/customers`

### Endpoints

#### GET /api/customers
**Summary:** Get all customers  
**Description:** Retrieve a paginated list of all customers  
**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "customerId": "123456789",
      "customerData": "Complete customer profile including demographics, addresses...",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-20T14:45:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

#### GET /api/customers/{id}
**Summary:** Get customer by ID  
**Description:** Retrieve a customer by their ID using random access pattern  
**Parameters:**
- `id` (path, required): Customer ID (Long)

**Response:** `200 OK` | `404 Not Found`

#### POST /api/customers
**Summary:** Create a new customer  
**Description:** Create a new customer with demographic information, addresses, contact information, SSN, and credit scores  
**Request Body:**
```json
{
  "customerId": "123456789",
  "customerData": "Customer profile data..."
}
```
**Validation:**
- `customerId`: Must be 9 characters in numeric format
- `customerData`: Required, max 491 characters

**Response:** `201 Created` | `400 Bad Request`

#### PUT /api/customers/{id}
**Summary:** Update an existing customer  
**Description:** Update customer details by ID  
**Parameters:**
- `id` (path, required): Customer ID (Long)

**Request Body:**
```json
{
  "customerData": "Updated customer data..."
}
```

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### DELETE /api/customers/{id}
**Summary:** Delete a customer  
**Description:** Delete a customer by ID  
**Parameters:**
- `id` (path, required): Customer ID (Long)

**Response:** `204 No Content` | `404 Not Found`

---

## 3. Transaction Management API

**Base Path:** `/api/transactions`

### Endpoints

#### GET /api/transactions
**Summary:** Get all transactions  
**Description:** Retrieve a paginated list of all transactions in sequential order by composite key  
**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "cardNumber": "1234567890123456",
      "transactionId": "TXN1234567890123",
      "transactionData": "AMT:100.00|DATE:2024-01-15|MERCHANT:ACME Store|STATUS:APPROVED",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

#### GET /api/transactions/{cardNumber}/{transactionId}
**Summary:** Get transaction by composite key  
**Description:** Retrieve a transaction by card number and transaction ID  
**Parameters:**
- `cardNumber` (path, required): Card number (16 characters)
- `transactionId` (path, required): Transaction ID (16 characters)

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### GET /api/transactions/card/{cardNumber}
**Summary:** Get transactions by card number  
**Description:** Retrieve all transactions for a specific card number in sequential order  
**Parameters:**
- `cardNumber` (path, required): Card number (16 characters)
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)

**Response:** `200 OK` | `400 Bad Request`

#### POST /api/transactions
**Summary:** Create a new transaction  
**Description:** Create a new credit card transaction record  
**Request Body:**
```json
{
  "cardNumber": "1234567890123456",
  "transactionId": "TXN1234567890123",
  "transactionData": "AMT:100.00|DATE:2024-01-15|MERCHANT:ACME Store|STATUS:APPROVED"
}
```
**Validation:**
- `cardNumber`: Must be exactly 16 characters
- `transactionId`: Must be exactly 16 characters
- `transactionData`: Required, max 318 characters

**Response:** `201 Created` | `400 Bad Request`

#### PUT /api/transactions/{cardNumber}/{transactionId}
**Summary:** Update an existing transaction  
**Description:** Update transaction details by composite key  
**Parameters:**
- `cardNumber` (path, required): Card number (16 characters)
- `transactionId` (path, required): Transaction ID (16 characters)

**Request Body:**
```json
{
  "transactionData": "Updated transaction data..."
}
```

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### DELETE /api/transactions/{cardNumber}/{transactionId}
**Summary:** Delete a transaction  
**Description:** Delete a transaction by composite key  
**Parameters:**
- `cardNumber` (path, required): Card number (16 characters)
- `transactionId` (path, required): Transaction ID (16 characters)

**Response:** `204 No Content` | `404 Not Found` | `400 Bad Request`

---

## 4. Card Cross Reference Management API

**Base Path:** `/api/card-cross-references`

### Endpoints

#### GET /api/card-cross-references
**Summary:** Get all card cross references  
**Description:** Retrieve a paginated list of all card cross references in sequential order by card number  
**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "cardNumber": "1234567890123456",
      "crossReferenceData": "CUST123456789012ACCT123456789012",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

#### GET /api/card-cross-references/{id}
**Summary:** Get card cross reference by ID  
**Description:** Retrieve a card cross reference by its ID  
**Parameters:**
- `id` (path, required): Card cross reference ID (Long)

**Response:** `200 OK` | `404 Not Found`

#### GET /api/card-cross-references/card/{cardNumber}
**Summary:** Get card cross reference by card number  
**Description:** Retrieve a card cross reference by its card number  
**Parameters:**
- `cardNumber` (path, required): Card number (16 characters)

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### POST /api/card-cross-references
**Summary:** Create a new card cross reference  
**Description:** Create a new card cross reference linking card number to customer and account  
**Request Body:**
```json
{
  "cardNumber": "1234567890123456",
  "crossReferenceData": "CUST123456789012ACCT123456789012"
}
```
**Validation:**
- `cardNumber`: Must be exactly 16 characters
- `crossReferenceData`: Required, max 34 characters

**Response:** `201 Created` | `400 Bad Request`

#### PUT /api/card-cross-references/{id}
**Summary:** Update an existing card cross reference  
**Description:** Update card cross reference details by ID  
**Parameters:**
- `id` (path, required): Card cross reference ID (Long)

**Request Body:**
```json
{
  "crossReferenceData": "Updated cross reference data..."
}
```

**Response:** `200 OK` | `404 Not Found` | `400 Bad Request`

#### DELETE /api/card-cross-references/{id}
**Summary:** Delete a card cross reference  
**Description:** Delete a card cross reference by ID  
**Parameters:**
- `id` (path, required): Card cross reference ID (Long)

**Response:** `204 No Content` | `404 Not Found`

---

## Common Response Codes

| Code | Description |
|------|-------------|
| 200  | OK - Request successful |
| 201  | Created - Resource created successfully |
| 204  | No Content - Resource deleted successfully |
| 400  | Bad Request - Invalid request data or validation error |
| 404  | Not Found - Resource not found |
| 500  | Internal Server Error - Server error occurred |

---

## Business Rules Implementation

### Account Management
- **BR005: Random Access Pattern for Account Files** - Accounts are accessed directly by account ID for immediate retrieval
- **BR008: Key Extraction for Random Access** - Account ID validation ensures 11 numeric digits

### Customer Management
- **BR004: Random Access Pattern for Customer Files** - Customers are accessed directly by customer ID for efficient retrieval
- **BR008: Key Extraction for Random Access** - Customer ID validation ensures 9 numeric characters

### Transaction Management
- **BR002: Sequential Access Pattern for Transaction Files** - Transactions are accessed sequentially in composite key order (card number + transaction ID)
- Composite primary key ensures unique transaction identification

### Card Cross Reference Management
- **BR003: Sequential Access Pattern for Cross-Reference Files** - Card cross-references are accessed sequentially by card number
- Links card numbers to customer and account relationships

---

## Database Schema

### Tables Created

1. **accounts** (V1__Create_accounts_table.sql)
   - id (BIGSERIAL PRIMARY KEY)
   - account_id (BIGINT NOT NULL UNIQUE)
   - account_data (VARCHAR(289) NOT NULL)
   - created_at (TIMESTAMP NOT NULL)
   - updated_at (TIMESTAMP NOT NULL)

2. **customers** (V2__Create_customers_table.sql)
   - id (BIGSERIAL PRIMARY KEY)
   - customer_id (VARCHAR(9) NOT NULL UNIQUE)
   - customer_data (VARCHAR(491) NOT NULL)
   - created_at (TIMESTAMP NOT NULL)
   - updated_at (TIMESTAMP NOT NULL)

3. **transactions** (V3__Create_transactions_table.sql)
   - card_number (VARCHAR(16) NOT NULL)
   - transaction_id (VARCHAR(16) NOT NULL)
   - transaction_data (VARCHAR(318) NOT NULL)
   - created_at (TIMESTAMP NOT NULL)
   - updated_at (TIMESTAMP NOT NULL)
   - PRIMARY KEY (card_number, transaction_id)

4. **card_cross_reference** (V4__Create_card_cross_reference_table.sql)
   - id (BIGSERIAL PRIMARY KEY)
   - card_number (VARCHAR(16) NOT NULL UNIQUE)
   - cross_reference_data (VARCHAR(34) NOT NULL)
   - created_at (TIMESTAMP NOT NULL)
   - updated_at (TIMESTAMP NOT NULL)

---

## Technology Stack

- **Framework:** Spring Boot 3.5.5
- **Language:** Java 21
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Migration:** Flyway
- **Documentation:** OpenAPI 3.0 (Swagger)
- **Build Tool:** Maven
- **Utilities:** Lombok

---

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL database

### Running the Application

1. Configure database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
```

2. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

3. Access Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

---

## Notes

- All endpoints support pagination using Spring Data's `Pageable` interface
- Timestamps (created_at, updated_at) are automatically managed by JPA
- All validations are enforced at both entity and service layers
- Sequential access patterns are implemented for transaction and card cross-reference files
- Random access patterns are implemented for account and customer files
- Composite keys are used for transactions to ensure unique identification

---

**Generated:** 2024
**Version:** 1.0.0
**Macro-functionality:** Account and Customers Management System
