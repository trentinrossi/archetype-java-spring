# Bill Payment Processing API - OpenAPI Summary

## Overview

This document provides a comprehensive overview of the Bill Payment Processing REST API built with Spring Boot 3.5.5 and Java 21. The API implements a complete bill payment system with account management, card management, and transaction processing capabilities.

**Base URL:** `http://localhost:8080`

**API Version:** 1.0.0

**Technology Stack:**
- Java 21
- Spring Boot 3.5.5
- PostgreSQL Database
- Flyway Migrations
- Lombok
- OpenAPI 3.0

---

## Table of Contents

1. [Account Management API](#account-management-api)
2. [Card Management API](#card-management-api)
3. [Transaction Management API](#transaction-management-api)
4. [Data Models](#data-models)
5. [Business Rules Implementation](#business-rules-implementation)
6. [Error Handling](#error-handling)

---

## Account Management API

### Base Path: `/api/accounts`

#### 1. Get All Accounts
**Endpoint:** `GET /api/accounts`

**Description:** Retrieve a paginated list of all accounts

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)
- `sort` (string, optional): Sort criteria

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": "ACC123456789",
      "acctCurrBal": 1500.75,
      "formattedAcctCurrBal": "$1,500.75",
      "cardCount": 3,
      "transactionCount": 45,
      "hasBalanceToPay": true,
      "paymentAmount": 1500.75,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T14:45:00"
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid request parameters
- `500` - Internal server error

---

#### 2. Get Account by ID
**Endpoint:** `GET /api/accounts/{acctId}`

**Description:** Retrieve a specific account by account ID

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Response:** `200 OK`
```json
{
  "acctId": "ACC123456789",
  "acctCurrBal": 1500.75,
  "formattedAcctCurrBal": "$1,500.75",
  "cardCount": 3,
  "transactionCount": 45,
  "hasBalanceToPay": true,
  "paymentAmount": 1500.75,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:45:00"
}
```

**Status Codes:**
- `200` - Account found
- `404` - Account not found
- `500` - Internal server error

---

#### 3. Create Account
**Endpoint:** `POST /api/accounts`

**Description:** Create a new account in the system

**Request Body:**
```json
{
  "acctId": "ACC123456789",
  "acctCurrBal": 1500.75
}
```

**Response:** `201 CREATED`
```json
{
  "acctId": "ACC123456789",
  "acctCurrBal": 1500.75,
  "formattedAcctCurrBal": "$1,500.75",
  "cardCount": 0,
  "transactionCount": 0,
  "hasBalanceToPay": true,
  "paymentAmount": 1500.75,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Validations:**
- `acctId`: Required, cannot be empty or low-values
- `acctCurrBal`: Required, must be greater than zero

**Status Codes:**
- `201` - Account created successfully
- `400` - Invalid request data or validation error
- `500` - Internal server error

---

#### 4. Update Account
**Endpoint:** `PUT /api/accounts/{acctId}`

**Description:** Update account balance

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Request Body:**
```json
{
  "acctCurrBal": 2000.00
}
```

**Response:** `200 OK`
```json
{
  "acctId": "ACC123456789",
  "acctCurrBal": 2000.00,
  "formattedAcctCurrBal": "$2,000.00",
  "cardCount": 3,
  "transactionCount": 45,
  "hasBalanceToPay": true,
  "paymentAmount": 2000.00,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T16:20:00"
}
```

**Status Codes:**
- `200` - Account updated successfully
- `400` - Invalid request data
- `404` - Account not found
- `500` - Internal server error

---

#### 5. Delete Account
**Endpoint:** `DELETE /api/accounts/{acctId}`

**Description:** Delete an account by account ID

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Response:** `204 NO CONTENT`

**Status Codes:**
- `204` - Account deleted successfully
- `404` - Account not found
- `500` - Internal server error

---

#### 6. Validate Account (BR001)
**Endpoint:** `POST /api/accounts/{acctId}/validate`

**Description:** Validates that the entered account ID is valid and exists in the system

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Response:** `200 OK`
```json
{
  "acctId": "ACC123456789",
  "valid": true,
  "message": "Account validation successful"
}
```

**Business Rule:** BR001 - Account Validation

**Status Codes:**
- `200` - Validation completed
- `400` - Account ID is empty or invalid ("Acct ID can NOT be empty...")
- `404` - Account ID not found ("Account ID NOT found...")
- `500` - Internal server error

---

#### 7. Check Account Balance (BR002)
**Endpoint:** `POST /api/accounts/{acctId}/check-balance`

**Description:** Verifies that the account has a positive balance before allowing payment

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Response:** `200 OK`
```json
{
  "acctId": "ACC123456789",
  "hasBalance": true,
  "currentBalance": 1500.75,
  "message": "Account has sufficient balance for payment"
}
```

**Business Rule:** BR002 - Balance Check

**Status Codes:**
- `200` - Balance check successful
- `400` - Account has zero or negative balance ("You have nothing to pay...")
- `404` - Account not found
- `500` - Internal server error

---

#### 8. Process Full Balance Payment (BR004, BR006, BR007)
**Endpoint:** `POST /api/accounts/{acctId}/process-payment`

**Description:** Process bill payment for the full current account balance

**Path Parameters:**
- `acctId` (string, required): Account identifier

**Response:** `200 OK`
```json
{
  "acctId": "ACC123456789",
  "paymentAmount": 1500.75,
  "newBalance": 0.00,
  "status": "SUCCESS",
  "message": "Full balance payment processed successfully"
}
```

**Business Rules:**
- BR004 - Full Balance Payment
- BR006 - Transaction Record Creation
- BR007 - Account Balance Update

**Status Codes:**
- `200` - Payment processed successfully
- `400` - Invalid payment request or insufficient balance
- `404` - Account not found
- `500` - Internal server error

---

#### 9. Get Accounts with Positive Balance
**Endpoint:** `GET /api/accounts/positive-balance`

**Description:** Retrieve all accounts that have a positive balance

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": "ACC123456789",
      "acctCurrBal": 1500.75,
      "formattedAcctCurrBal": "$1,500.75",
      "hasBalanceToPay": true,
      "paymentAmount": 1500.75
    }
  ],
  "totalElements": 50
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid request parameters
- `500` - Internal server error

---

## Card Management API

### Base Path: `/api/cards`

#### 1. Get All Cards
**Endpoint:** `GET /api/cards`

**Description:** Retrieve a paginated list of all cards

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "xrefCardNum": "4111111111111111",
      "xrefAcctId": "ACC123456789",
      "isValid": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 100
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid request parameters
- `500` - Internal server error

---

#### 2. Get Card by ID
**Endpoint:** `GET /api/cards/{id}`

**Description:** Retrieve a card by its internal ID

**Path Parameters:**
- `id` (long, required): Card internal identifier

**Response:** `200 OK`
```json
{
  "id": 1,
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789",
  "isValid": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Status Codes:**
- `200` - Card found
- `404` - Card not found
- `500` - Internal server error

---

#### 3. Get Card by Card Number
**Endpoint:** `GET /api/cards/by-card-number/{cardNumber}`

**Description:** Retrieve a card by its card number

**Path Parameters:**
- `cardNumber` (string, required): Card number

**Response:** `200 OK`
```json
{
  "id": 1,
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789",
  "isValid": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Status Codes:**
- `200` - Card found
- `404` - Card not found
- `500` - Internal server error

---

#### 4. Get Cards by Account ID
**Endpoint:** `GET /api/cards/by-account/{accountId}`

**Description:** Retrieve all cards associated with an account

**Path Parameters:**
- `accountId` (string, required): Account identifier

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "xrefCardNum": "4111111111111111",
    "xrefAcctId": "ACC123456789",
    "isValid": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "xrefCardNum": "4222222222222222",
    "xrefAcctId": "ACC123456789",
    "isValid": true,
    "createdAt": "2024-01-16T11:00:00",
    "updatedAt": "2024-01-16T11:00:00"
  }
]
```

**Status Codes:**
- `200` - Cards found
- `404` - No cards found for account
- `500` - Internal server error

---

#### 5. Create Card
**Endpoint:** `POST /api/cards`

**Description:** Create a new credit card

**Request Body:**
```json
{
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 1,
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789",
  "isValid": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Validations:**
- `xrefCardNum`: Required, cannot be blank
- `xrefAcctId`: Required, cannot be blank

**Status Codes:**
- `201` - Card created successfully
- `400` - Invalid request data
- `500` - Internal server error

---

#### 6. Update Card
**Endpoint:** `PUT /api/cards/{id}`

**Description:** Update card details by ID

**Path Parameters:**
- `id` (long, required): Card internal identifier

**Request Body:**
```json
{
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789",
  "isValid": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T16:20:00"
}
```

**Status Codes:**
- `200` - Card updated successfully
- `400` - Invalid request data
- `404` - Card not found
- `500` - Internal server error

---

#### 7. Delete Card
**Endpoint:** `DELETE /api/cards/{id}`

**Description:** Delete a card by ID

**Path Parameters:**
- `id` (long, required): Card internal identifier

**Response:** `204 NO CONTENT`

**Status Codes:**
- `204` - Card deleted successfully
- `404` - Card not found
- `500` - Internal server error

---

#### 8. Validate Card and Account Relationship
**Endpoint:** `POST /api/cards/validate`

**Description:** Validate that a card number is associated with the correct account ID

**Request Body:**
```json
{
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789"
}
```

**Response:** `200 OK`
```json
{
  "xrefCardNum": "4111111111111111",
  "xrefAcctId": "ACC123456789",
  "valid": true,
  "message": "Card and account relationship is valid"
}
```

**Status Codes:**
- `200` - Validation completed
- `400` - Invalid request data or validation failed
- `404` - Card or account not found
- `500` - Internal server error

---

## Transaction Management API

### Base Path: `/api/transactions`

#### 1. Get All Transactions
**Endpoint:** `GET /api/transactions`

**Description:** Retrieve a paginated list of all transactions

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "tranId": 1234567890123456,
      "tranTypeCd": "02",
      "tranCatCd": 2,
      "tranSource": "POS TERM",
      "tranDesc": "BILL PAYMENT - ONLINE",
      "tranAmt": 1500.75,
      "tranAmtFormatted": "$1,500.75",
      "tranCardNum": "4111111111111111",
      "tranMerchantId": 999999999,
      "tranMerchantName": "BILL PAYMENT",
      "tranMerchantCity": "N/A",
      "tranMerchantZip": "N/A",
      "tranOrigTs": "2024-01-15T10:30:00",
      "tranProcTs": "2024-01-15T10:30:00",
      "tranAcctId": "ACC123456789",
      "isBillPayment": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 1000
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid request parameters
- `500` - Internal server error

---

#### 2. Get Transaction by ID
**Endpoint:** `GET /api/transactions/{id}`

**Description:** Retrieve a transaction by internal ID

**Path Parameters:**
- `id` (long, required): Transaction internal identifier

**Response:** `200 OK`
```json
{
  "id": 1,
  "tranId": 1234567890123456,
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.75,
  "tranAmtFormatted": "$1,500.75",
  "tranCardNum": "4111111111111111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranOrigTs": "2024-01-15T10:30:00",
  "tranProcTs": "2024-01-15T10:30:00",
  "tranAcctId": "ACC123456789",
  "isBillPayment": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Status Codes:**
- `200` - Transaction found
- `404` - Transaction not found
- `500` - Internal server error

---

#### 3. Get Transaction by Transaction ID
**Endpoint:** `GET /api/transactions/by-tran-id/{tranId}`

**Description:** Retrieve a transaction by unique transaction ID

**Path Parameters:**
- `tranId` (long, required): Transaction unique identifier

**Response:** `200 OK` (same as above)

**Status Codes:**
- `200` - Transaction found
- `404` - Transaction not found
- `500` - Internal server error

---

#### 4. Get Transactions by Account ID
**Endpoint:** `GET /api/transactions/by-account/{accountId}`

**Description:** Retrieve all transactions for a specific account

**Path Parameters:**
- `accountId` (string, required): Account identifier

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "tranId": 1234567890123456,
    "tranTypeCd": "02",
    "tranCatCd": 2,
    "tranDesc": "BILL PAYMENT - ONLINE",
    "tranAmt": 1500.75,
    "tranAcctId": "ACC123456789",
    "isBillPayment": true
  }
]
```

**Status Codes:**
- `200` - Transactions found
- `400` - Invalid account ID
- `500` - Internal server error

---

#### 5. Get Transactions by Card Number
**Endpoint:** `GET /api/transactions/by-card/{cardNumber}`

**Description:** Retrieve all transactions for a specific card

**Path Parameters:**
- `cardNumber` (string, required): Card number

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "tranId": 1234567890123456,
    "tranCardNum": "4111111111111111",
    "tranDesc": "BILL PAYMENT - ONLINE",
    "tranAmt": 1500.75
  }
]
```

**Status Codes:**
- `200` - Transactions found
- `400` - Invalid card number
- `500` - Internal server error

---

#### 6. Create Transaction
**Endpoint:** `POST /api/transactions`

**Description:** Create a new transaction record

**Request Body:**
```json
{
  "tranId": 1234567890123456,
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.75,
  "tranCardNum": "4111111111111111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranOrigTs": "2024-01-15T10:30:00",
  "tranProcTs": "2024-01-15T10:30:00",
  "tranAcctId": "ACC123456789"
}
```

**Response:** `201 CREATED`
```json
{
  "id": 1,
  "tranId": 1234567890123456,
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.75,
  "tranAmtFormatted": "$1,500.75",
  "tranCardNum": "4111111111111111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranOrigTs": "2024-01-15T10:30:00",
  "tranProcTs": "2024-01-15T10:30:00",
  "tranAcctId": "ACC123456789",
  "isBillPayment": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Validations:**
- `tranId`: Required, must be unique, 16 digits max
- `tranTypeCd`: Required, exactly 2 characters
- `tranCatCd`: Required, single digit
- `tranSource`: Required, max 8 characters
- `tranDesc`: Required, max 50 characters
- `tranAmt`: Required, must be greater than zero
- `tranCardNum`: Required
- `tranMerchantId`: Required, 9 digits max
- `tranMerchantName`: Required, max 50 characters
- `tranMerchantCity`: Required, max 50 characters
- `tranMerchantZip`: Required, max 10 characters
- `tranOrigTs`: Required
- `tranProcTs`: Required
- `tranAcctId`: Required

**Status Codes:**
- `201` - Transaction created successfully
- `400` - Invalid request data or "Tran ID already exist..."
- `500` - Internal server error

---

#### 7. Update Transaction
**Endpoint:** `PUT /api/transactions/{id}`

**Description:** Update transaction details by internal ID

**Path Parameters:**
- `id` (long, required): Transaction internal identifier

**Request Body:**
```json
{
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.75,
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranProcTs": "2024-01-15T10:30:00"
}
```

**Response:** `200 OK` (full transaction object)

**Status Codes:**
- `200` - Transaction updated successfully
- `400` - Invalid request data
- `404` - Transaction not found
- `500` - Internal server error

---

#### 8. Delete Transaction
**Endpoint:** `DELETE /api/transactions/{id}`

**Description:** Delete a transaction by internal ID

**Path Parameters:**
- `id` (long, required): Transaction internal identifier

**Response:** `204 NO CONTENT`

**Status Codes:**
- `204` - Transaction deleted successfully
- `404` - Transaction not found
- `500` - Internal server error

---

#### 9. Generate Next Transaction ID (BR005)
**Endpoint:** `POST /api/transactions/generate-id`

**Description:** Generate unique transaction ID by incrementing the highest existing ID

**Response:** `200 OK`
```json
{
  "nextTransactionId": 1234567890123457,
  "message": "Transaction ID generated successfully"
}
```

**Business Rule:** BR005 - Transaction ID Generation

**Algorithm:**
1. Query the highest existing transaction ID
2. If no transactions exist, return 1
3. Otherwise, return highest ID + 1
4. Validate uniqueness before returning

**Status Codes:**
- `200` - Transaction ID generated successfully
- `500` - Internal server error or "Tran ID already exist..."

---

#### 10. Create Bill Payment Transaction (BR006)
**Endpoint:** `POST /api/transactions/bill-payment`

**Description:** Create standardized transaction record for bill payment

**Request Body:**
```json
{
  "accountId": "ACC123456789",
  "cardNumber": "4111111111111111",
  "amount": 1500.75
}
```

**Response:** `201 CREATED`
```json
{
  "id": 1,
  "tranId": 1234567890123456,
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.75,
  "tranAmtFormatted": "$1,500.75",
  "tranCardNum": "4111111111111111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranOrigTs": "2024-01-15T10:30:00",
  "tranProcTs": "2024-01-15T10:30:00",
  "tranAcctId": "ACC123456789",
  "isBillPayment": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Business Rule:** BR006 - Transaction Record Creation

**Predefined Values:**
- `tranTypeCd`: "02" (Bill Payment)
- `tranCatCd`: 2 (Payment Category)
- `tranSource`: "POS TERM"
- `tranDesc`: "BILL PAYMENT - ONLINE"
- `tranMerchantId`: 999999999
- `tranMerchantName`: "BILL PAYMENT"
- `tranMerchantCity`: "N/A"
- `tranMerchantZip`: "N/A"
- `tranOrigTs`: Current timestamp (BR008)
- `tranProcTs`: Current timestamp (BR008)

**Status Codes:**
- `201` - Bill payment transaction created successfully
- `400` - Invalid request data or account balance not positive
- `404` - Account or card not found
- `500` - Internal server error

---

#### 11. Get All Bill Payment Transactions
**Endpoint:** `GET /api/transactions/bill-payments`

**Description:** Retrieve all transactions with type code '02' (bill payments)

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "tranId": 1234567890123456,
      "tranTypeCd": "02",
      "tranCatCd": 2,
      "tranDesc": "BILL PAYMENT - ONLINE",
      "tranAmt": 1500.75,
      "isBillPayment": true
    }
  ],
  "totalElements": 500
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid request parameters
- `500` - Internal server error

---

#### 12. Get Bill Payment Transactions by Account
**Endpoint:** `GET /api/transactions/bill-payments/by-account/{accountId}`

**Description:** Retrieve all bill payment transactions for a specific account

**Path Parameters:**
- `accountId` (string, required): Account identifier

**Query Parameters:**
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "tranId": 1234567890123456,
      "tranTypeCd": "02",
      "tranCatCd": 2,
      "tranDesc": "BILL PAYMENT - ONLINE",
      "tranAmt": 1500.75,
      "tranAcctId": "ACC123456789",
      "isBillPayment": true
    }
  ],
  "totalElements": 50
}
```

**Status Codes:**
- `200` - Successful retrieval
- `400` - Invalid account ID
- `500` - Internal server error

---

## Data Models

### Account Model

```json
{
  "acctId": "string (11 chars max)",
  "acctCurrBal": "decimal (13,2)",
  "formattedAcctCurrBal": "string",
  "cardCount": "integer",
  "transactionCount": "integer",
  "hasBalanceToPay": "boolean",
  "paymentAmount": "decimal",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Relationships:**
- One-to-Many with Card
- One-to-Many with Transaction

---

### Card Model

```json
{
  "id": "long",
  "xrefCardNum": "string (16 chars max)",
  "xrefAcctId": "string (11 chars max)",
  "isValid": "boolean",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Relationships:**
- Many-to-One with Account

---

### Transaction Model

```json
{
  "id": "long",
  "tranId": "long (16 digits max)",
  "tranTypeCd": "string (2 chars)",
  "tranCatCd": "integer (1 digit)",
  "tranSource": "string (8 chars max)",
  "tranDesc": "string (50 chars max)",
  "tranAmt": "decimal (11,2)",
  "tranAmtFormatted": "string",
  "tranCardNum": "string (16 chars max)",
  "tranMerchantId": "long (9 digits max)",
  "tranMerchantName": "string (50 chars max)",
  "tranMerchantCity": "string (50 chars max)",
  "tranMerchantZip": "string (10 chars max)",
  "tranOrigTs": "timestamp",
  "tranProcTs": "timestamp",
  "tranAcctId": "string (11 chars max)",
  "isBillPayment": "boolean",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Relationships:**
- Many-to-One with Account

---

## Business Rules Implementation

### BR001: Account Validation
**Endpoint:** `POST /api/accounts/{acctId}/validate`

**Description:** Validates that the entered account ID is valid and exists in the system

**Validation Logic:**
1. Check if account ID is not empty or low-values
2. Check if account exists in the database
3. Return validation result

**Error Messages:**
- "Acct ID can NOT be empty..." - When account ID is null, empty, or contains low-values
- "Account ID NOT found..." - When account does not exist in database

---

### BR002: Balance Check
**Endpoint:** `POST /api/accounts/{acctId}/check-balance`

**Description:** Verifies that the account has a positive balance before allowing payment

**Validation Logic:**
1. Validate account exists (BR001)
2. Check if account balance > 0
3. Return balance check result

**Error Messages:**
- "You have nothing to pay..." - When account balance is zero or negative

---

### BR004: Full Balance Payment
**Endpoint:** `POST /api/accounts/{acctId}/process-payment`

**Description:** Payment processes the full current account balance

**Processing Logic:**
1. Validate account (BR001)
2. Check balance (BR002)
3. Set payment amount = current account balance
4. Create transaction record (BR006)
5. Update account balance (BR007)

---

### BR005: Transaction ID Generation
**Endpoint:** `POST /api/transactions/generate-id`

**Description:** Generates unique transaction ID by incrementing the highest existing ID

**Algorithm:**
1. Query: `SELECT MAX(tran_id) FROM transactions`
2. If result is null, return 1
3. Otherwise, return max_id + 1
4. Validate uniqueness

**Error Messages:**
- "Tran ID already exist..." - When generated ID already exists (duplicate key error)

---

### BR006: Transaction Record Creation
**Endpoint:** `POST /api/transactions/bill-payment`

**Description:** Creates standardized transaction record for bill payment

**Predefined Values:**
- Transaction Type Code: "02"
- Transaction Category Code: 2
- Transaction Source: "POS TERM"
- Transaction Description: "BILL PAYMENT - ONLINE"
- Merchant ID: 999999999
- Merchant Name: "BILL PAYMENT"
- Merchant City: "N/A"
- Merchant ZIP: "N/A"

**Dynamic Values:**
- Transaction ID: Generated using BR005
- Transaction Amount: From account balance
- Card Number: From request
- Origination Timestamp: Current timestamp (BR008)
- Processing Timestamp: Current timestamp (BR008)

---

### BR007: Account Balance Update
**Endpoint:** `POST /api/accounts/{acctId}/process-payment`

**Description:** Updates account balance after successful payment

**Update Logic:**
1. Validate account exists
2. Calculate new balance: current_balance - payment_amount
3. Update account record
4. Handle update errors

**Error Messages:**
- "If update fails, display error message" - When database update fails

---

### BR008: Timestamp Generation
**Used in:** Transaction creation endpoints

**Description:** Generates current timestamp for transaction recording

**Format:** `yyyy-MM-dd HH:mm:ss.SSSSSS` (26 characters)

**Implementation:**
- Uses `LocalDateTime.now()` for origination and processing timestamps
- Automatically set on transaction creation

---

## Error Handling

### Standard Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Acct ID can NOT be empty...",
  "path": "/api/accounts/validate"
}
```

### HTTP Status Codes

- **200 OK** - Successful GET, PUT, POST (non-creation)
- **201 CREATED** - Successful POST (creation)
- **204 NO CONTENT** - Successful DELETE
- **400 BAD REQUEST** - Validation errors, business rule violations
- **404 NOT FOUND** - Resource not found
- **500 INTERNAL SERVER ERROR** - Server errors

### Common Error Messages

**Account Errors:**
- "Acct ID can NOT be empty..."
- "Account ID NOT found..."
- "You have nothing to pay..."
- "Account with ID already exists"

**Card Errors:**
- "Card number is required"
- "Account ID is required"
- "Card with this card number already exists"
- "Card not found with ID: {id}"

**Transaction Errors:**
- "Tran ID already exist..."
- "Transaction not found"
- "Transaction amount must be greater than zero"
- "Account balance must be positive"

---

## Authentication & Authorization

**Note:** This API currently does not implement authentication or authorization. In a production environment, consider implementing:
- OAuth 2.0 / JWT tokens
- API keys
- Role-based access control (RBAC)
- Rate limiting

---

## Rate Limiting

**Note:** No rate limiting is currently implemented. Consider adding rate limiting in production:
- Per IP address
- Per API key
- Per user account

---

## Pagination

All list endpoints support pagination with the following parameters:

- `page` (integer, default: 0): Zero-based page index
- `size` (integer, default: 20): Number of items per page
- `sort` (string, optional): Sort criteria (e.g., "acctId,asc")

**Example:**
```
GET /api/accounts?page=0&size=20&sort=acctId,asc
```

---

## Testing the API

### Using cURL

**Create Account:**
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"acctId":"ACC123456789","acctCurrBal":1500.75}'
```

**Validate Account:**
```bash
curl -X POST http://localhost:8080/api/accounts/ACC123456789/validate
```

**Process Payment:**
```bash
curl -X POST http://localhost:8080/api/accounts/ACC123456789/process-payment
```

**Generate Transaction ID:**
```bash
curl -X POST http://localhost:8080/api/transactions/generate-id
```

### Using Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

---

## Database Schema

### Tables

1. **accounts**
   - acct_id (VARCHAR(11), PRIMARY KEY)
   - acct_curr_bal (DECIMAL(13,2), NOT NULL)
   - created_at (TIMESTAMP, NOT NULL)
   - updated_at (TIMESTAMP, NOT NULL)

2. **cards**
   - id (BIGSERIAL, PRIMARY KEY)
   - xref_card_num (VARCHAR(16), NOT NULL)
   - xref_acct_id (VARCHAR(11), NOT NULL, FOREIGN KEY → accounts.acct_id)
   - created_at (TIMESTAMP, NOT NULL)
   - updated_at (TIMESTAMP, NOT NULL)

3. **transactions**
   - id (BIGSERIAL, PRIMARY KEY)
   - tran_id (BIGINT, NOT NULL, UNIQUE)
   - tran_type_cd (VARCHAR(2), NOT NULL)
   - tran_cat_cd (INTEGER, NOT NULL)
   - tran_source (VARCHAR(8), NOT NULL)
   - tran_desc (VARCHAR(50), NOT NULL)
   - tran_amt (DECIMAL(11,2), NOT NULL)
   - tran_card_num (VARCHAR(16), NOT NULL)
   - tran_merchant_id (BIGINT, NOT NULL)
   - tran_merchant_name (VARCHAR(50), NOT NULL)
   - tran_merchant_city (VARCHAR(50), NOT NULL)
   - tran_merchant_zip (VARCHAR(10), NOT NULL)
   - tran_orig_ts (TIMESTAMP, NOT NULL)
   - tran_proc_ts (TIMESTAMP, NOT NULL)
   - tran_acct_id (VARCHAR(11), NOT NULL, FOREIGN KEY → accounts.acct_id)
   - created_at (TIMESTAMP, NOT NULL)
   - updated_at (TIMESTAMP, NOT NULL)

### Indexes

- accounts: idx_acct_id (UNIQUE)
- cards: idx_card_num, idx_card_acct_id
- transactions: idx_tran_id (UNIQUE), idx_tran_card_num, idx_tran_type_cd, idx_tran_orig_ts, idx_tran_acct_id

---

## Deployment

### Prerequisites
- Java 21
- PostgreSQL 12+
- Maven 3.6+

### Configuration

Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/billpayment
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

### Build & Run

```bash
# Build
mvn clean package

# Run
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Support & Contact

For questions or issues, please contact the development team or create an issue in the project repository.

---

**Document Version:** 1.0.0  
**Last Updated:** 2024-01-15  
**API Version:** 1.0.0
