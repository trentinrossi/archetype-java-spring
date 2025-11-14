# Account Management System - API Documentation

## Overview

This document provides a comprehensive overview of the Account Management System REST API. The system manages credit card accounts, customers, cards, and their cross-references with full CRUD operations and business rule validations.

**Base URL**: `/api`  
**Version**: 1.0.0  
**Framework**: Spring Boot 3.5.5 with Java 21  
**Database**: PostgreSQL with Flyway migrations

---

## Table of Contents

1. [Customer Management APIs](#customer-management-apis)
2. [Account Management APIs](#account-management-apis)
3. [Card Management APIs](#card-management-apis)
4. [Card Cross Reference Management APIs](#card-cross-reference-management-apis)
5. [Data Models](#data-models)
6. [Error Codes](#error-codes)
7. [Business Rules](#business-rules)

---

## Customer Management APIs

### Base Path: `/api/customers`

#### 1. Get All Customers
- **Endpoint**: `GET /api/customers`
- **Description**: Retrieve a paginated list of all customers
- **Parameters**:
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response**: `200 OK`
  ```json
  {
    "content": [CustomerResponseDto],
    "pageable": {...},
    "totalElements": 100,
    "totalPages": 5
  }
  ```
- **Error Responses**:
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error

#### 2. Get Customer by ID
- **Endpoint**: `GET /api/customers/{id}`
- **Description**: Retrieve a customer by their 9-digit customer ID
- **Parameters**:
  - `id` (path, required): Customer ID (9 digits)
- **Response**: `200 OK`
  ```json
  {
    "customerId": 123456789,
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    ...
  }
  ```
- **Error Responses**:
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error

#### 3. Create Customer
- **Endpoint**: `POST /api/customers`
- **Description**: Create a new customer with validation of all required fields and business rules
- **Request Body**: `CreateCustomerRequestDto`
  ```json
  {
    "customerId": 123456789,
    "firstName": "John",
    "middleName": "Michael",
    "lastName": "Doe",
    "addressLine1": "123 Main Street",
    "addressLine2": "Apt 4B",
    "addressLine3": "New York",
    "stateCode": "NY",
    "countryCode": "USA",
    "zipCode": "10001",
    "phoneNumber1": "(212)555-1234",
    "phoneNumber2": "(212)555-5678",
    "ssn": 123456789,
    "governmentIssuedId": "DL123456789",
    "dateOfBirth": "1985-06-15",
    "eftAccountId": "1234567890",
    "primaryCardholderIndicator": "Y",
    "ficoScore": 720,
    "ficoCreditScore": 720,
    "city": "New York",
    "primaryPhoneNumber": "(212)555-1234",
    "secondaryPhoneNumber": "(212)555-5678"
  }
  ```
- **Response**: `201 Created`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data - validation errors
  - `500 Internal Server Error`: Server error

#### 4. Update Customer
- **Endpoint**: `PUT /api/customers/{id}`
- **Description**: Update customer details by ID with atomic transaction
- **Parameters**:
  - `id` (path, required): Customer ID (9 digits)
- **Request Body**: `UpdateCustomerRequestDto` (all fields optional)
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error

#### 5. Delete Customer
- **Endpoint**: `DELETE /api/customers/{id}`
- **Description**: Delete a customer by ID
- **Parameters**:
  - `id` (path, required): Customer ID (9 digits)
- **Response**: `204 No Content`
- **Error Responses**:
  - `404 Not Found`: Customer not found
  - `500 Internal Server Error`: Server error

---

## Account Management APIs

### Base Path: `/api/accounts`

#### 1. Get All Accounts
- **Endpoint**: `GET /api/accounts`
- **Description**: Retrieve a paginated list of all accounts
- **Parameters**:
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response**: `200 OK`
  ```json
  {
    "content": [AccountResponseDto],
    "pageable": {...},
    "totalElements": 100,
    "totalPages": 5
  }
  ```
- **Error Responses**:
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error

#### 2. Get Account by ID
- **Endpoint**: `GET /api/accounts/{id}`
- **Description**: Retrieve an account by its 11-digit account ID
- **Parameters**:
  - `id` (path, required): Account ID (11 digits)
- **Response**: `200 OK`
  ```json
  {
    "accountId": 12345678901,
    "activeStatus": "Y",
    "currentBalance": 1500.50,
    "creditLimit": 5000.00,
    "availableCredit": 3499.50,
    ...
  }
  ```
- **Error Responses**:
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

#### 3. Create Account
- **Endpoint**: `POST /api/accounts`
- **Description**: Create a new account with validation of all required fields and business rules
- **Request Body**: `CreateAccountRequestDto`
  ```json
  {
    "accountId": 12345678901,
    "activeStatus": "Y",
    "currentBalance": 1500.50,
    "creditLimit": 5000.00,
    "cashCreditLimit": 1000.00,
    "openDate": "2023-01-15",
    "expirationDate": "2025-01-15",
    "reissueDate": "2024-01-15",
    "currentCycleCredit": 250.75,
    "currentCycleDebit": 300.25,
    "groupId": "GRP001",
    "customerId": 123456789,
    "accountStatus": "A"
  }
  ```
- **Response**: `201 Created`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data - validation errors
  - `500 Internal Server Error`: Server error

#### 4. Update Account
- **Endpoint**: `PUT /api/accounts/{id}`
- **Description**: Update account details by ID with atomic transaction
- **Parameters**:
  - `id` (path, required): Account ID (11 digits)
- **Request Body**: `UpdateAccountRequestDto` (all fields optional)
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

#### 5. Delete Account
- **Endpoint**: `DELETE /api/accounts/{id}`
- **Description**: Delete an account by ID
- **Parameters**:
  - `id` (path, required): Account ID (11 digits)
- **Response**: `204 No Content`
- **Error Responses**:
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

---

## Card Management APIs

### Base Path: `/api/cards`

#### 1. Get All Cards
- **Endpoint**: `GET /api/cards`
- **Description**: Retrieve a paginated list of all cards
- **Parameters**:
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error

#### 2. Get Card by Card Number
- **Endpoint**: `GET /api/cards/{cardNumber}`
- **Description**: Retrieve a card by its card number
- **Parameters**:
  - `cardNumber` (path, required): Card number (16 characters)
- **Response**: `200 OK`
  ```json
  {
    "cardNumber": "1234567890123456",
    "accountId": 12345678901,
    "customerId": 123456789,
    "createdAt": "2023-01-15T10:30:00",
    "updatedAt": "2023-01-15T10:30:00"
  }
  ```
- **Error Responses**:
  - `404 Not Found`: Card not found
  - `500 Internal Server Error`: Server error

#### 3. Create Card
- **Endpoint**: `POST /api/cards`
- **Description**: Create a new card
- **Request Body**: `CreateCardRequestDto`
  ```json
  {
    "cardNumber": "1234567890123456",
    "accountId": 12345678901,
    "customerId": 123456789
  }
  ```
- **Response**: `201 Created`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `500 Internal Server Error`: Server error

#### 4. Update Card
- **Endpoint**: `PUT /api/cards/{cardNumber}`
- **Description**: Update card details by card number
- **Parameters**:
  - `cardNumber` (path, required): Card number (16 characters)
- **Request Body**: `UpdateCardRequestDto` (all fields optional)
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Card not found
  - `500 Internal Server Error`: Server error

#### 5. Delete Card
- **Endpoint**: `DELETE /api/cards/{cardNumber}`
- **Description**: Delete a card by card number
- **Parameters**:
  - `cardNumber` (path, required): Card number (16 characters)
- **Response**: `204 No Content`
- **Error Responses**:
  - `404 Not Found`: Card not found
  - `500 Internal Server Error`: Server error

---

## Card Cross Reference Management APIs

### Base Path: `/api/card-cross-references`

#### 1. Get All Card Cross References
- **Endpoint**: `GET /api/card-cross-references`
- **Description**: Retrieve a paginated list of all card cross references
- **Parameters**:
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error

#### 2. Get Card Cross Reference by Account ID
- **Endpoint**: `GET /api/card-cross-references/{accountId}`
- **Description**: Retrieve a card cross reference by account ID
- **Parameters**:
  - `accountId` (path, required): Account ID (11 digits)
- **Response**: `200 OK`
  ```json
  {
    "accountId": 12345678901,
    "customerId": 123456789,
    "createdAt": "2023-01-15T10:30:00",
    "updatedAt": "2023-01-15T10:30:00"
  }
  ```
- **Error Responses**:
  - `404 Not Found`: Card cross reference not found
  - `500 Internal Server Error`: Server error

#### 3. Create Card Cross Reference
- **Endpoint**: `POST /api/card-cross-references`
- **Description**: Create a new card cross reference
- **Request Body**: `CreateCardCrossReferenceRequestDto`
  ```json
  {
    "accountId": 12345678901,
    "customerId": 123456789
  }
  ```
- **Response**: `201 Created`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `500 Internal Server Error`: Server error

#### 4. Update Card Cross Reference
- **Endpoint**: `PUT /api/card-cross-references/{accountId}`
- **Description**: Update card cross reference by account ID
- **Parameters**:
  - `accountId` (path, required): Account ID (11 digits)
- **Request Body**: `UpdateCardCrossReferenceRequestDto` (all fields optional)
- **Response**: `200 OK`
- **Error Responses**:
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Card cross reference not found
  - `500 Internal Server Error`: Server error

#### 5. Delete Card Cross Reference
- **Endpoint**: `DELETE /api/card-cross-references/{accountId}`
- **Description**: Delete a card cross reference by account ID
- **Parameters**:
  - `accountId` (path, required): Account ID (11 digits)
- **Response**: `204 No Content`
- **Error Responses**:
  - `404 Not Found`: Card cross reference not found
  - `500 Internal Server Error`: Server error

---

## Data Models

### CustomerResponseDto

```json
{
  "customerId": 123456789,
  "firstName": "John",
  "middleName": "Michael",
  "lastName": "Doe",
  "fullName": "John Michael Doe",
  "addressLine1": "123 Main Street",
  "addressLine2": "Apt 4B",
  "addressLine3": "New York",
  "stateCode": "NY",
  "countryCode": "USA",
  "zipCode": "10001",
  "phoneNumber1": "(212)555-1234",
  "phoneNumber2": "(212)555-5678",
  "ssn": "123-45-6789",
  "ssnRaw": 123456789,
  "governmentIssuedId": "DL123456789",
  "dateOfBirth": "1985-06-15",
  "age": 38,
  "eftAccountId": "1234567890",
  "primaryCardholderIndicator": "Y",
  "primaryCardholderStatus": "Primary Cardholder",
  "ficoScore": 720,
  "ficoScoreRating": "Good",
  "ficoCreditScore": 720,
  "city": "New York",
  "primaryPhoneNumber": "(212)555-1234",
  "secondaryPhoneNumber": "(212)555-5678",
  "fullAddress": "123 Main Street, Apt 4B, New York, NY 10001, USA",
  "meetsAgeRequirement": true,
  "createdAt": "2023-01-15T10:30:00",
  "updatedAt": "2023-01-15T10:30:00"
}
```

### AccountResponseDto

```json
{
  "accountId": 12345678901,
  "activeStatus": "Y",
  "currentBalance": 1500.50,
  "creditLimit": 5000.00,
  "cashCreditLimit": 1000.00,
  "openDate": "2023-01-15",
  "expirationDate": "2025-01-15",
  "reissueDate": "2024-01-15",
  "currentCycleCredit": 250.75,
  "currentCycleDebit": 300.25,
  "groupId": "GRP001",
  "customerId": 123456789,
  "accountStatus": "A",
  "availableCredit": 3499.50,
  "availableCashCredit": -500.50,
  "netCycleAmount": -49.50,
  "isActive": true,
  "isExpired": false,
  "hasGroupAssignment": true,
  "activeStatusDisplay": "Active",
  "accountStatusDisplay": "A",
  "createdAt": "2023-01-15T10:30:00",
  "updatedAt": "2023-01-15T10:30:00"
}
```

### CardResponseDto

```json
{
  "cardNumber": "1234567890123456",
  "accountId": 12345678901,
  "customerId": 123456789,
  "createdAt": "2023-01-15T10:30:00",
  "updatedAt": "2023-01-15T10:30:00"
}
```

### CardCrossReferenceResponseDto

```json
{
  "accountId": 12345678901,
  "customerId": 123456789,
  "createdAt": "2023-01-15T10:30:00",
  "updatedAt": "2023-01-15T10:30:00"
}
```

---

## Error Codes

### HTTP Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **204 No Content**: Resource deleted successfully
- **400 Bad Request**: Invalid request data or validation errors
- **404 Not Found**: Resource not found
- **409 Conflict**: Concurrent update detected
- **500 Internal Server Error**: Server error

### Validation Error Messages

#### Customer Validations
- "Customer ID is required"
- "Customer ID must be 9 digits"
- "First name must contain only letters"
- "Last name must contain only letters"
- "Invalid state code"
- "Invalid country code"
- "Invalid zip code for state"
- "Invalid phone number format"
- "Invalid Social Security Number"
- "Invalid date of birth or customer does not meet age requirement"
- "FICO Score should be between 300 and 850"
- "Primary cardholder indicator must be Y or N"

#### Account Validations
- "Account number is required"
- "Account number must be 11 digits"
- "Account Filter must be a non-zero 11 digit number"
- "Account status must be Y or N"
- "Current balance must be a valid positive number"
- "Credit limit must be a valid positive number"
- "Cash credit limit must be a valid positive number"
- "Invalid open date format or value"
- "Invalid expiration date format or value"
- "Invalid reissue date format or value"
- "Current cycle credit must be a valid positive number"
- "Current cycle debit must be a valid positive number"

---

## Business Rules

### Customer Business Rules

1. **BR001: Account Identification**
   - Account must be identified using an 11-digit account number
   - Account number must be non-zero

2. **BR002: Customer Identification**
   - Customer must be identified using a 9-digit customer ID

3. **BR003: Concurrent Update Prevention**
   - Prevent data corruption from concurrent updates
   - Uses optimistic locking with version field

4. **BR004: Atomic Update Requirement**
   - Account and customer updates must be atomic
   - Transaction rollback on any failure

5. **BR006: Account Status Management**
   - Account status must be properly maintained
   - Valid values: Y (Active) or N (Inactive)

6. **BR007: Balance Tracking**
   - Account balances must be accurately tracked
   - All balance fields must be non-negative

7. **BR008: Customer Age Requirement**
   - Customer must be at least 18 years old
   - Maximum age: 120 years

8. **BR009: Primary Cardholder Designation**
   - Each account must have a primary cardholder designation
   - Valid values: Y (Primary) or N (Secondary)

9. **BR010: Account Group Assignment**
   - Accounts can be assigned to groups for management purposes
   - Group ID is optional, max 10 characters

### Validation Rules

#### Customer Validations
- Customer ID: 9 digits, numeric, required
- First Name: Alphabetic characters only, max 25 chars, required
- Last Name: Alphabetic characters only, max 25 chars, required
- SSN: 9 digits, specific format validation, required
- Date of Birth: YYYY-MM-DD format, age 18-120, required
- FICO Score: 300-850 range, required
- State Code: 2-letter US state code, required
- Country Code: 3-letter country code, required
- ZIP Code: 5 digits, required
- Phone Numbers: (XXX)XXX-XXXX format

#### Account Validations
- Account ID: 11 digits, numeric, non-zero, required
- Active Status: Y or N, required
- All balance fields: Non-negative decimal with 2 decimal places, required
- All date fields: YYYY-MM-DD format, year 1900-2099, required
- Customer ID: Must exist in Customer Master file

---

## Authentication & Authorization

Currently, the API does not implement authentication or authorization. In a production environment, consider implementing:
- OAuth 2.0 / JWT tokens
- Role-based access control (RBAC)
- API key authentication
- Rate limiting

---

## Rate Limiting

No rate limiting is currently implemented. Consider implementing rate limiting in production:
- Per IP address
- Per API key
- Per user account

---

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Page number (0-indexed, default: 0)
- `size`: Number of items per page (default: 20, max: 100)
- `sort`: Sort field and direction (e.g., `firstName,asc`)

Example: `GET /api/customers?page=0&size=20&sort=lastName,asc`

---

## Versioning

Current API version: **v1**

The API uses URL path versioning. Future versions will be accessible via:
- `/api/v2/customers`
- `/api/v3/accounts`

---

## Support & Contact

For API support, please contact:
- Email: api-support@example.com
- Documentation: https://api.example.com/docs
- Swagger UI: https://api.example.com/swagger-ui.html

---

## Changelog

### Version 1.0.0 (2024-01-15)
- Initial release
- Customer Management APIs
- Account Management APIs
- Card Management APIs
- Card Cross Reference Management APIs
- Complete CRUD operations for all entities
- Business rule validations
- Database migrations with Flyway

---

**Generated**: 2024-01-15  
**Last Updated**: 2024-01-15  
**API Version**: 1.0.0
