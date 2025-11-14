# Account Update Program - API Documentation

## Overview

This document provides comprehensive API documentation for the Account Update Program, a credit card account management system built with Spring Boot 3.5.5, Java 21, and PostgreSQL.

## Base URL

```
http://localhost:8080/api
```

## Architecture

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **Migration Tool**: Flyway
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven

## Business Rules Implemented

### BR001: Account Identification
Account must be identified using an 11-digit account number (non-zero). Validation implemented in entity and service layers.

### BR002: Customer Identification
Customer must be identified using a 9-digit customer ID. Validation implemented in entity and service layers.

### BR003: Concurrent Update Prevention
Prevents data loss from concurrent updates using optimistic locking with @Version annotation.

### BR004: Atomic Update Transaction
Account and customer updates succeed or fail together using @Transactional annotations.

### BR006: Account Status Management
Account status must be valid indicator. Validation implemented in entity layer.

### BR007: Balance Tracking
Account balances properly maintained with calculated fields: available credit, credit utilization.

### BR008: Customer Age Requirement
Customer must be 18-120 years old. Validation implemented in entity layer.

### BR009: Primary Cardholder Designation
Each account has primary cardholder designation (Y/N indicator).

### BR010: Account Group Assignment
Accounts can be assigned to groups (optional, max 10 characters).

## API Endpoints Summary

### Account Management (`/api/accounts`)
- `GET /api/accounts` - Get all accounts (paginated)
- `GET /api/accounts/{id}` - Get account by ID
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Delete account
- `GET /api/accounts/customer/{customerId}` - Get accounts by customer
- `GET /api/accounts/expired` - Get expired accounts
- `GET /api/accounts/over-limit` - Get over-limit accounts
- `GET /api/accounts/group/{groupId}` - Get accounts by group
- `GET /api/accounts/active-status/{status}` - Get accounts by status

### Customer Management (`/api/customers`)
- `GET /api/customers` - Get all customers (paginated)
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `GET /api/customers/state/{stateCode}` - Get customers by state
- `GET /api/customers/primary-cardholders` - Get primary cardholders
- `GET /api/customers/search?name={name}` - Search customers by name

### Card Management (`/api/cards`)
- `GET /api/cards` - Get all cards (paginated)
- `GET /api/cards/{cardNumber}` - Get card by number
- `POST /api/cards` - Create new card
- `PUT /api/cards/{cardNumber}` - Update card
- `DELETE /api/cards/{cardNumber}` - Delete card
- `GET /api/cards/account/{accountId}` - Get cards by account
- `GET /api/cards/customer/{customerId}` - Get cards by customer

### Card Cross Reference Management (`/api/card-cross-references`)
- `GET /api/card-cross-references` - Get all cross references (paginated)
- `GET /api/card-cross-references/account/{accountId}` - Get by account
- `POST /api/card-cross-references` - Create cross reference
- `PUT /api/card-cross-references/account/{accountId}` - Update cross reference
- `DELETE /api/card-cross-references/account/{accountId}` - Delete cross reference
- `GET /api/card-cross-references/customer/{customerId}` - Get by customer

## Data Models

### Account
- accountId (Long, 11 digits) - Primary key
- activeStatus (String, Y/N)
- currentBalance (BigDecimal, 12,2)
- creditLimit (BigDecimal, 12,2)
- cashCreditLimit (BigDecimal, 12,2)
- openDate, expirationDate, reissueDate (LocalDate)
- currentCycleCredit, currentCycleDebit (BigDecimal, 12,2)
- groupId (String, max 10)
- customerId (Long, 9 digits) - Foreign key
- accountStatus (String, 1 char)
- Calculated: availableCredit, creditUtilization, isOverLimit, daysUntilExpiration, accountAge

### Customer
- customerId (Long, 9 digits) - Primary key
- firstName, middleName, lastName (String, max 25)
- addressLine1, addressLine2, addressLine3 (String, max 50)
- stateCode (String, 2), countryCode (String, 3), zipCode (String, 10)
- phoneNumber1, phoneNumber2 (String, format: (XXX)XXX-XXXX)
- ssn (Long, 9 digits, unique)
- governmentIssuedId (String, max 20)
- dateOfBirth (LocalDate)
- eftAccountId (String, 10 digits)
- primaryCardholderIndicator (String, Y/N)
- ficoScore (Integer, 300-850)
- Calculated: fullName, maskedSsn, age, fullAddress, ficoScoreCategory

### Card
- cardNumber (String, 16 digits) - Primary key
- accountId (Long, 11 digits) - Foreign key
- customerId (Long, 9 digits) - Foreign key
- Calculated: maskedCardNumber, formattedCardNumber, cardIssuer

### CardCrossReference
- accountId (Long, 11 digits) - Primary key & Foreign key
- customerId (Long, 9 digits) - Foreign key

## HTTP Status Codes

- **200 OK**: Successful GET
- **201 Created**: Successful POST
- **204 No Content**: Successful DELETE
- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource not found
- **409 Conflict**: Concurrent update detected
- **500 Internal Server Error**: Server error

## Validation Rules

### Account
- Account ID: 11 digits, non-zero
- Active Status: Y or N
- Balances: Non-negative, 2 decimals
- Dates: YYYY-MM-DD, year 1900-2099
- Group ID: Max 10 chars (optional)

### Customer
- Customer ID: 9 digits
- Names: Alphabetic only
- SSN: 9 digits, valid format
- Phone: (XXX)XXX-XXXX
- Age: 18-120 years
- FICO: 300-850
- State: 2 letters, Country: 3 letters

### Card
- Card Number: 16 digits
- Account & Customer must exist

## Database Schema

**Tables**: accounts, customers, cards, card_cross_references

**Relationships**:
- accounts.customer_id → customers.customer_id
- cards.account_id → accounts.account_id
- cards.customer_id → customers.customer_id
- card_cross_references.account_id → accounts.account_id
- card_cross_references.customer_id → customers.customer_id

## Pagination

All list endpoints support:
- `page`: Page number (default: 0)
- `size`: Items per page (default: 20)
- `sort`: Sort criteria (e.g., `accountId,asc`)

## Testing

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

**Sample cURL**:
```bash
# Create Account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountId":12345678901,"activeStatus":"Y","currentBalance":1500.50,...}'

# Get Account
curl -X GET http://localhost:8080/api/accounts/12345678901

# Update Account
curl -X PUT http://localhost:8080/api/accounts/12345678901 \
  -H "Content-Type: application/json" \
  -d '{"currentBalance":1600.00}'

# Delete Account
curl -X DELETE http://localhost:8080/api/accounts/12345678901
```

## Files Generated

**Total**: 32 files

**Entities** (4): Account, Customer, Card, CardCrossReference

**Per Entity**:
- Entity class (JPA)
- 3 DTOs (Create, Update, Response)
- Repository interface
- Service class
- Controller class
- Database migration (Flyway)

**Structure**:
```
src/main/java/com/example/demo/
├── entity/
│   ├── Account.java
│   ├── Customer.java
│   ├── Card.java
│   └── CardCrossReference.java
├── dto/
│   ├── CreateAccountRequestDto.java
│   ├── UpdateAccountRequestDto.java
│   ├── AccountResponseDto.java
│   ├── CreateCustomerRequestDto.java
│   ├── UpdateCustomerRequestDto.java
│   ├── CustomerResponseDto.java
│   ├── CreateCardRequestDto.java
│   ├── UpdateCardRequestDto.java
│   ├── CardResponseDto.java
│   ├── CreateCardCrossReferenceRequestDto.java
│   ├── UpdateCardCrossReferenceRequestDto.java
│   └── CardCrossReferenceResponseDto.java
├── repository/
│   ├── AccountRepository.java
│   ├── CustomerRepository.java
│   ├── CardRepository.java
│   └── CardCrossReferenceRepository.java
├── service/
│   ├── AccountService.java
│   ├── CustomerService.java
│   ├── CardService.java
│   └── CardCrossReferenceService.java
└── controller/
    ├── AccountController.java
    ├── CustomerController.java
    ├── CardController.java
    └── CardCrossReferenceController.java

src/main/resources/db/migration/
├── V1__Create_accounts_table.sql
├── V2__Create_customers_table.sql
├── V3__Create_cards_table.sql
└── V4__Create_card_cross_references_table.sql
```

---

**Version**: 1.0.0  
**Generated**: 2024-01-15  
**Status**: Production-Ready
