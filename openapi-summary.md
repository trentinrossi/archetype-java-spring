# OpenAPI Summary - Card Account and Customer File Processing System

This document provides a comprehensive summary of all REST API endpoints created for the Card Account and Customer File Processing System, based on the modernization of COBOL programs CBACT01C, CBACT03C, CBCUS01C, and CBSTM03B.

## Base URL
```
http://localhost:8080/api
```

## API Overview

The system provides four main API groups:
- **Account Management** - Based on CBACT01C program functionality
- **Customer Management** - Based on CBCUS01C program functionality  
- **Transaction Management** - Based on CBSTM03B program functionality
- **Card Cross Reference Management** - Based on CBACT03C program functionality

---

## Account Management APIs (`/api/accounts`)

### Core CRUD Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/accounts` | Get all accounts (paginated) | None | `Page<AccountResponse>` |
| GET | `/api/accounts/{acctId}` | Get account by ID | None | `AccountResponse` |
| POST | `/api/accounts` | Create new account | `CreateAccountRequest` | `AccountResponse` |
| PUT | `/api/accounts/{acctId}` | Update existing account | `UpdateAccountRequest` | `AccountResponse` |
| DELETE | `/api/accounts/{acctId}` | Delete account | None | `204 No Content` |

### Sequential Reading Operations (CBACT01C Functionality)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/accounts/sequential-read` | Sequential read of accounts | `AccountSequentialReadRequest` | `AccountSequentialReadResponse` |
| POST | `/api/accounts/file/open` | Open account file for processing | None | `String` |
| GET | `/api/accounts/file/read/{recordCount}` | Read accounts sequentially | None | `List<AccountResponse>` |
| POST | `/api/accounts/file/close` | Close account file | None | `String` |
| POST | `/api/accounts/{acctId}/display` | Display account record (CBACT01C style) | None | `String` |

### Filtering and Search Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/accounts/status/{status}` | Get accounts by status | None | `Page<AccountResponse>` |
| GET | `/api/accounts/group/{groupId}` | Get accounts by group ID | None | `Page<AccountResponse>` |
| GET | `/api/accounts/active` | Get all active accounts | None | `List<AccountResponse>` |
| GET | `/api/accounts/inactive` | Get all inactive accounts | None | `List<AccountResponse>` |
| GET | `/api/accounts/over-limit` | Get over-limit accounts | None | `List<AccountResponse>` |
| GET | `/api/accounts/over-cash-limit` | Get over cash limit accounts | None | `List<AccountResponse>` |
| GET | `/api/accounts/expiring` | Get expiring accounts | Query: `beforeDate` | `List<AccountResponse>` |
| GET | `/api/accounts/recently-reissued` | Get recently reissued accounts | Query: `sinceDate` | `List<AccountResponse>` |

### Utility Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/accounts/distinct/statuses` | Get distinct account statuses | None | `List<String>` |
| GET | `/api/accounts/distinct/groups` | Get distinct group IDs | None | `List<String>` |
| GET | `/api/accounts/count/status/{status}` | Count accounts by status | None | `Long` |
| GET | `/api/accounts/count/group/{groupId}` | Count accounts by group | None | `Long` |

---

## Customer Management APIs (`/api/customers`)

### Core CRUD Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/customers` | Get all customers (paginated) | None | `Page<CustomerResponse>` |
| GET | `/api/customers/{customerId}` | Get customer by ID | None | `CustomerResponse` |
| POST | `/api/customers` | Create new customer | `CreateCustomerRequest` | `CustomerResponse` |
| PUT | `/api/customers/{customerId}` | Update existing customer | `UpdateCustomerRequest` | `CustomerResponse` |
| DELETE | `/api/customers/{customerId}` | Delete customer | None | `204 No Content` |

### Sequential Reading Operations (CBCUS01C Functionality)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/customers/sequential-read` | Sequential read of customers | `CustomerSequentialReadRequest` | `CustomerSequentialReadResponse` |
| POST | `/api/customers/file/open` | Open customer file for processing | None | `String` |
| GET | `/api/customers/file/read/{recordCount}` | Read customers sequentially | None | `List<CustomerResponse>` |
| POST | `/api/customers/file/close` | Close customer file | None | `String` |
| POST | `/api/customers/{customerId}/display` | Display customer record (CBCUS01C style) | None | `String` |

### Filtering and Search Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/customers/status/{status}` | Get customers by status | None | `Page<CustomerResponse>` |
| GET | `/api/customers/type/{customerType}` | Get customers by type | None | `Page<CustomerResponse>` |
| GET | `/api/customers/active` | Get all active customers | None | `List<CustomerResponse>` |
| GET | `/api/customers/inactive` | Get all inactive customers | None | `List<CustomerResponse>` |
| GET | `/api/customers/personal` | Get personal customers | None | `List<CustomerResponse>` |
| GET | `/api/customers/business` | Get business customers | None | `List<CustomerResponse>` |
| GET | `/api/customers/good-credit` | Get customers with good credit | None | `List<CustomerResponse>` |
| GET | `/api/customers/poor-credit` | Get customers with poor credit | None | `List<CustomerResponse>` |
| GET | `/api/customers/search/name` | Search customers by name | Query: `firstName`, `lastName` | `Page<CustomerResponse>` |
| GET | `/api/customers/search/email/{email}` | Find customer by email | None | `CustomerResponse` |
| GET | `/api/customers/search/phone/{phone}` | Find customers by phone | None | `List<CustomerResponse>` |

### Utility Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/customers/distinct/statuses` | Get distinct customer statuses | None | `List<String>` |
| GET | `/api/customers/distinct/types` | Get distinct customer types | None | `List<String>` |
| GET | `/api/customers/distinct/ratings` | Get distinct credit ratings | None | `List<String>` |
| GET | `/api/customers/distinct/states` | Get distinct states | None | `List<String>` |
| GET | `/api/customers/distinct/cities` | Get distinct cities | None | `List<String>` |
| GET | `/api/customers/count/status/{status}` | Count customers by status | None | `Long` |
| GET | `/api/customers/count/type/{type}` | Count customers by type | None | `Long` |
| GET | `/api/customers/count/city/{city}` | Count customers by city | None | `Long` |
| GET | `/api/customers/count/state/{state}` | Count customers by state | None | `Long` |

---

## Transaction Management APIs (`/api/transactions`)

### Core CRUD Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/transactions` | Get all transactions (paginated) | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/{cardNumber}/{transactionId}` | Get transaction by composite key | None | `TransactionResponse` |
| GET | `/api/transactions/id/{id}` | Get transaction by internal ID | None | `TransactionResponse` |
| POST | `/api/transactions` | Create new transaction | `CreateTransactionRequest` | `TransactionResponse` |
| PUT | `/api/transactions/{cardNumber}/{transactionId}` | Update existing transaction | `UpdateTransactionRequest` | `TransactionResponse` |
| DELETE | `/api/transactions/{cardNumber}/{transactionId}` | Delete transaction | None | `204 No Content` |

### Sequential Reading Operations (CBSTM03B Functionality)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/transactions/sequential-read` | Sequential read of transactions | `TransactionSequentialReadRequest` | `TransactionSequentialReadResponse` |
| POST | `/api/transactions/validate-composite-key` | Validate composite key | `TransactionCompositeKeyRequest` | `TransactionCompositeKeyResponse` |

### Filtering and Search Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/transactions/card/{cardNumber}` | Get transactions by card number | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/status/{status}` | Get transactions by status | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/type/{transactionType}` | Get transactions by type | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/successful` | Get successful transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/failed` | Get failed transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/pending` | Get pending transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/high-value` | Get high value transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/reversals` | Get reversal transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/atm` | Get ATM transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/pos` | Get POS transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/online` | Get online transactions | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/merchant/{merchantId}` | Get transactions by merchant | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/terminal/{terminalId}` | Get transactions by terminal | None | `Page<TransactionResponse>` |
| GET | `/api/transactions/recent/{days}` | Get recent transactions | None | `Page<TransactionResponse>` |

### Utility Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/transactions/count/status/{status}` | Count transactions by status | None | `Long` |
| GET | `/api/transactions/count/card/{cardNumber}` | Count transactions by card | None | `Long` |

---

## Card Cross Reference Management APIs (`/api/card-cross-references`)

### Core CRUD Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/card-cross-references` | Get all card cross references (paginated) | None | `Page<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/{cardNumber}` | Get card cross reference by card number | None | `CardCrossReferenceResponse` |
| POST | `/api/card-cross-references` | Create new card cross reference | `CreateCardCrossReferenceRequest` | `CardCrossReferenceResponse` |
| PUT | `/api/card-cross-references/{cardNumber}` | Update existing card cross reference | `UpdateCardCrossReferenceRequest` | `CardCrossReferenceResponse` |
| DELETE | `/api/card-cross-references/{cardNumber}` | Delete card cross reference | None | `204 No Content` |

### Sequential Reading Operations (CBACT03C Functionality)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/card-cross-references/sequential-read` | Sequential read of card cross references | `CardCrossReferenceSequentialReadRequest` | `CardCrossReferenceSequentialReadResponse` |
| POST | `/api/card-cross-references/file/open` | Open card cross reference file | None | `String` |
| GET | `/api/card-cross-references/file/read/{recordCount}` | Read card cross references sequentially | None | `List<CardCrossReferenceResponse>` |
| POST | `/api/card-cross-references/file/close` | Close card cross reference file | None | `String` |

### Filtering and Search Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/card-cross-references/status/{status}` | Get cards by status | None | `Page<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/type/{cardType}` | Get cards by type | None | `Page<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/customer/{customerId}` | Get cards by customer ID | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/account/{accountNumber}` | Get cards by account number | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/branch/{branchCode}` | Get cards by branch code | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/active` | Get active cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/blocked` | Get blocked cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/expired` | Get expired cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/high-value` | Get high value cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/international` | Get international enabled cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/contactless` | Get contactless enabled cards | None | `List<CardCrossReferenceResponse>` |
| GET | `/api/card-cross-references/mobile-payment` | Get mobile payment enabled cards | None | `List<CardCrossReferenceResponse>` |

### Utility Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/card-cross-references/distinct/statuses` | Get distinct card statuses | None | `List<String>` |
| GET | `/api/card-cross-references/distinct/types` | Get distinct card types | None | `List<String>` |
| GET | `/api/card-cross-references/distinct/branches` | Get distinct branch codes | None | `List<String>` |
| GET | `/api/card-cross-references/count/status/{status}` | Count cards by status | None | `Long` |
| GET | `/api/card-cross-references/count/type/{type}` | Count cards by type | None | `Long` |

---

## Common HTTP Status Codes

| Status Code | Description | When Used |
|-------------|-------------|-----------|
| 200 OK | Successful GET, PUT operations | Data retrieved or updated successfully |
| 201 Created | Successful POST operations | New resource created successfully |
| 204 No Content | Successful DELETE operations | Resource deleted successfully |
| 400 Bad Request | Invalid request data | Validation errors, malformed requests |
| 404 Not Found | Resource not found | Requested resource doesn't exist |
| 409 Conflict | Resource already exists | Attempting to create duplicate resource |
| 500 Internal Server Error | Server-side errors | Unexpected server errors |

---

## Request/Response Models

### Key Data Transfer Objects (DTOs)

#### Account DTOs
- `CreateAccountRequest` - Account creation data
- `UpdateAccountRequest` - Account update data  
- `AccountResponse` - Account response data
- `AccountSequentialReadRequest` - Sequential read parameters
- `AccountSequentialReadResponse` - Sequential read results

#### Customer DTOs
- `CreateCustomerRequest` - Customer creation data
- `UpdateCustomerRequest` - Customer update data
- `CustomerResponse` - Customer response data
- `CustomerSequentialReadRequest` - Sequential read parameters
- `CustomerSequentialReadResponse` - Sequential read results

#### Transaction DTOs
- `CreateTransactionRequest` - Transaction creation data
- `UpdateTransactionRequest` - Transaction update data
- `TransactionResponse` - Transaction response data
- `TransactionSequentialReadRequest` - Sequential read parameters
- `TransactionSequentialReadResponse` - Sequential read results
- `TransactionCompositeKeyRequest` - Composite key validation request
- `TransactionCompositeKeyResponse` - Composite key validation response

#### Card Cross Reference DTOs
- `CreateCardCrossReferenceRequest` - Card cross reference creation data
- `UpdateCardCrossReferenceRequest` - Card cross reference update data
- `CardCrossReferenceResponse` - Card cross reference response data
- `CardCrossReferenceSequentialReadRequest` - Sequential read parameters
- `CardCrossReferenceSequentialReadResponse` - Sequential read results

---

## Authentication and Security

The API currently implements basic security measures:
- Input validation using Bean Validation annotations
- SQL injection prevention through JPA/Hibernate
- Error handling with appropriate HTTP status codes
- Request/response logging for audit trails

**Note**: For production deployment, additional security measures should be implemented:
- Authentication (JWT tokens, OAuth2)
- Authorization (role-based access control)
- Rate limiting
- HTTPS enforcement
- API key management

---

## Pagination and Sorting

All paginated endpoints support standard Spring Data pagination parameters:
- `page` - Page number (0-based, default: 0)
- `size` - Page size (default: 20, max: 100)
- `sort` - Sort criteria (e.g., `sort=acctId,asc`)

Example:
```
GET /api/accounts?page=0&size=10&sort=acctId,asc
```

---

## Error Handling

The API provides consistent error responses with:
- HTTP status codes
- Error messages
- Timestamp
- Request path
- Validation details (for 400 errors)

Example error response:
```json
{
  "timestamp": "2023-10-01T12:00:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Account ID is required",
  "path": "/api/accounts"
}
```

---

## COBOL Program Mapping

| COBOL Program | Functionality | Modern API Endpoints |
|---------------|---------------|---------------------|
| CBACT01C | Account file sequential reading and display | `/api/accounts/sequential-read`, `/api/accounts/file/*` |
| CBACT03C | Card cross-reference file reading and printing | `/api/card-cross-references/sequential-read`, `/api/card-cross-references/file/*` |
| CBCUS01C | Customer file sequential reading and display | `/api/customers/sequential-read`, `/api/customers/file/*` |
| CBSTM03B | Transaction file access module | `/api/transactions/sequential-read`, `/api/transactions/validate-composite-key` |

This API provides a complete modernization of the original COBOL batch processing programs while maintaining the core business logic and data processing patterns.