# CardDemo Transaction Management API - OpenAPI Summary

## Overview

This document provides a comprehensive summary of all REST API endpoints for the CardDemo Transaction Management System. The system manages credit card transactions, accounts, cards, and merchant information with full CRUD operations and business rule implementations.

**Base URL**: `http://localhost:8080/api`

**Version**: 1.0.0

**Framework**: Spring Boot 3.5.5 with Java 21

---

## Table of Contents

1. [Transaction Management APIs](#transaction-management-apis)
2. [Account Management APIs](#account-management-apis)
3. [Card Management APIs](#card-management-apis)
4. [Merchant Management APIs](#merchant-management-apis)
5. [Data Models](#data-models)
6. [Error Codes](#error-codes)
7. [Business Rules Implementation](#business-rules-implementation)

---

## Transaction Management APIs

### Base Path: `/api/transactions`

#### 1. Get All Transactions (Paginated)

**Endpoint**: `GET /api/transactions`

**Description**: Retrieve a paginated list of all transactions

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 10)
- `sort` (string, optional): Sort field and direction (e.g., "transactionId,asc")

**Response**: `200 OK`
```json
{
  "content": [
    {
      "transactionId": "1234567890123456",
      "tranId": 1234567890123456,
      "tranTypeCd": 1,
      "tranCatCd": 1001,
      "tranSource": "ONLINE",
      "tranDesc": "Purchase at ABC Store",
      "tranAmt": 1234.56,
      "tranCardNum": "4532123456789012",
      "tranMerchantId": "123456789",
      "tranMerchantName": "ABC Grocery Store",
      "tranMerchantCity": "New York",
      "tranMerchantZip": "10001",
      "tranOrigTs": "2023-12-31",
      "tranProcTs": "2023-12-31",
      "formattedTransactionDate": "12/31/23",
      "formattedTransactionAmount": "+00001234.56",
      "createdAt": "2023-12-31T14:30:00",
      "updatedAt": "2023-12-31T14:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: Server error

---

#### 2. Get Transaction by ID

**Endpoint**: `GET /api/transactions/{id}`

**Description**: Retrieve a specific transaction by its ID

**Path Parameters**:
- `id` (string, required): Transaction ID (16 characters)

**Response**: `200 OK`
```json
{
  "transactionId": "1234567890123456",
  "tranId": 1234567890123456,
  "tranTypeCd": 1,
  "tranCatCd": 1001,
  "tranSource": "ONLINE",
  "tranDesc": "Purchase at ABC Store",
  "tranAmt": 1234.56,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "123456789",
  "tranMerchantName": "ABC Grocery Store",
  "tranMerchantCity": "New York",
  "tranMerchantZip": "10001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31",
  "formattedTransactionDate": "12/31/23",
  "formattedTransactionAmount": "+00001234.56",
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `404 Not Found`: Transaction not found
- `500 Internal Server Error`: Server error

---

#### 3. Create Transaction

**Endpoint**: `POST /api/transactions`

**Description**: Create a new transaction with auto-generated transaction ID

**Request Body**:
```json
{
  "transactionId": "1234567890123456",
  "tranTypeCd": 1,
  "tranCatCd": 1001,
  "tranSource": "ONLINE",
  "tranDesc": "Purchase at ABC Store",
  "tranAmt": 1234.56,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "123456789",
  "tranMerchantName": "ABC Grocery Store",
  "tranMerchantCity": "New York",
  "tranMerchantZip": "10001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31"
}
```

**Response**: `201 Created`
```json
{
  "transactionId": "1234567890123456",
  "tranId": 1234567890123456,
  "tranTypeCd": 1,
  "tranCatCd": 1001,
  "tranSource": "ONLINE",
  "tranDesc": "Purchase at ABC Store",
  "tranAmt": 1234.56,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "123456789",
  "tranMerchantName": "ABC Grocery Store",
  "tranMerchantCity": "New York",
  "tranMerchantZip": "10001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31",
  "formattedTransactionDate": "12/31/23",
  "formattedTransactionAmount": "+00001234.56",
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data or validation error
- `500 Internal Server Error`: Server error

**Validation Rules**:
- Transaction ID: Must be 16 characters, cannot be empty
- Transaction Type Code: Must be numeric, cannot be empty
- Transaction Category Code: Must be numeric, cannot be empty
- Transaction Source: Cannot be empty, max 10 characters
- Transaction Description: Cannot be empty, max 60 characters
- Transaction Amount: Must be in format -99999999.99 to +99999999.99
- Merchant ID: Must be numeric, cannot be empty, max 9 digits
- Merchant Name: Cannot be empty, max 30 characters
- Merchant City: Cannot be empty, max 25 characters
- Merchant Zip: Cannot be empty, max 10 characters
- Original Date: Must be in format YYYY-MM-DD
- Processing Date: Must be in format YYYY-MM-DD

---

#### 4. Update Transaction

**Endpoint**: `PUT /api/transactions/{id}`

**Description**: Update an existing transaction

**Path Parameters**:
- `id` (string, required): Transaction ID (16 characters)

**Request Body**:
```json
{
  "tranTypeCd": 2,
  "tranCatCd": 1002,
  "tranSource": "POS",
  "tranDesc": "Updated description",
  "tranAmt": 2345.67,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "987654321",
  "tranMerchantName": "XYZ Store",
  "tranMerchantCity": "Los Angeles",
  "tranMerchantZip": "90001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31"
}
```

**Response**: `200 OK`
```json
{
  "transactionId": "1234567890123456",
  "tranId": 1234567890123456,
  "tranTypeCd": 2,
  "tranCatCd": 1002,
  "tranSource": "POS",
  "tranDesc": "Updated description",
  "tranAmt": 2345.67,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "987654321",
  "tranMerchantName": "XYZ Store",
  "tranMerchantCity": "Los Angeles",
  "tranMerchantZip": "90001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31",
  "formattedTransactionDate": "12/31/23",
  "formattedTransactionAmount": "+00002345.67",
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T15:45:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data
- `404 Not Found`: Transaction not found
- `500 Internal Server Error`: Server error

---

#### 5. Delete Transaction

**Endpoint**: `DELETE /api/transactions/{id}`

**Description**: Delete a transaction by ID

**Path Parameters**:
- `id` (string, required): Transaction ID (16 characters)

**Response**: `204 No Content`

**Error Responses**:
- `404 Not Found`: Transaction not found
- `500 Internal Server Error`: Server error

---

#### 6. Get Transaction Page (BR001: Pagination)

**Endpoint**: `GET /api/transactions/page/{pageNumber}`

**Description**: Display transactions in pages with maximum 10 transactions per page

**Path Parameters**:
- `pageNumber` (integer, required): Page number (starting from 1)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "transactionId": "1234567890123456",
      "tranId": 1234567890123456,
      "tranTypeCd": 1,
      "tranCatCd": 1001,
      "tranSource": "ONLINE",
      "tranDesc": "Purchase at ABC Store",
      "tranAmt": 1234.56,
      "formattedTransactionDate": "12/31/23",
      "formattedTransactionAmount": "+00001234.56"
    }
  ],
  "pageNumber": 1,
  "totalPages": 10,
  "totalElements": 100,
  "hasNextPage": true,
  "hasPreviousPage": false
}
```

**Error Responses**:
- `400 Bad Request`: Invalid page number
- `500 Internal Server Error`: Server error

---

#### 7. Select Transaction for Detail View (BR002)

**Endpoint**: `GET /api/transactions/select/{transactionId}`

**Description**: Select a transaction for detailed view

**Path Parameters**:
- `transactionId` (string, required): Transaction ID (16 characters)

**Response**: `200 OK`
```json
{
  "transactionId": "1234567890123456",
  "tranId": 1234567890123456,
  "tranTypeCd": 1,
  "tranCatCd": 1001,
  "tranSource": "ONLINE",
  "tranDesc": "Purchase at ABC Store",
  "tranAmt": 1234.56,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "123456789",
  "tranMerchantName": "ABC Grocery Store",
  "tranMerchantCity": "New York",
  "tranMerchantZip": "10001",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31",
  "formattedTransactionDate": "12/31/23",
  "formattedTransactionAmount": "+00001234.56"
}
```

**Error Responses**:
- `404 Not Found`: Transaction not found
- `500 Internal Server Error`: Server error

---

#### 8. Search Transactions by ID (BR003)

**Endpoint**: `GET /api/transactions/search/{startTransactionId}`

**Description**: Search transactions starting from specific ID

**Path Parameters**:
- `startTransactionId` (string, required): Starting transaction ID (16 characters)

**Query Parameters**:
- `pageNumber` (integer, optional): Page number (default: 1)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "transactionId": "1234567890123456",
      "tranId": 1234567890123456,
      "tranTypeCd": 1,
      "tranCatCd": 1001,
      "tranSource": "ONLINE",
      "tranDesc": "Purchase at ABC Store",
      "tranAmt": 1234.56,
      "formattedTransactionDate": "12/31/23",
      "formattedTransactionAmount": "+00001234.56"
    }
  ],
  "pageNumber": 1,
  "totalPages": 5,
  "totalElements": 50
}
```

**Error Responses**:
- `404 Not Found`: Transaction not found
- `500 Internal Server Error`: Server error

---

#### 9. Navigate Forward (BR004)

**Endpoint**: `GET /api/transactions/navigate/forward/{lastTransactionId}`

**Description**: Navigate to next page of transactions

**Path Parameters**:
- `lastTransactionId` (string, required): Last transaction ID on current page

**Response**: `200 OK`
```json
{
  "content": [
    {
      "transactionId": "1234567890123467",
      "tranId": 1234567890123467,
      "tranTypeCd": 1,
      "tranCatCd": 1001,
      "tranSource": "ONLINE",
      "tranDesc": "Purchase at DEF Store",
      "tranAmt": 3456.78,
      "formattedTransactionDate": "12/31/23",
      "formattedTransactionAmount": "+00003456.78"
    }
  ],
  "pageNumber": 2,
  "hasNextPage": true,
  "hasPreviousPage": true
}
```

**Error Responses**:
- `404 Not Found`: End of file reached
- `500 Internal Server Error`: Server error

---

#### 10. Navigate Backward (BR005)

**Endpoint**: `GET /api/transactions/navigate/backward/{firstTransactionId}`

**Description**: Navigate to previous page of transactions

**Path Parameters**:
- `firstTransactionId` (string, required): First transaction ID on current page

**Query Parameters**:
- `currentPage` (integer, required): Current page number

**Response**: `200 OK`
```json
{
  "content": [
    {
      "transactionId": "1234567890123446",
      "tranId": 1234567890123446,
      "tranTypeCd": 1,
      "tranCatCd": 1001,
      "tranSource": "ONLINE",
      "tranDesc": "Purchase at GHI Store",
      "tranAmt": 5678.90,
      "formattedTransactionDate": "12/30/23",
      "formattedTransactionAmount": "+00005678.90"
    }
  ],
  "pageNumber": 1,
  "hasNextPage": true,
  "hasPreviousPage": false
}
```

**Error Responses**:
- `404 Not Found`: Beginning of file reached
- `500 Internal Server Error`: Server error

---

#### 11. Copy Last Transaction (BR013)

**Endpoint**: `GET /api/transactions/copy-last`

**Description**: Get last transaction for copy feature to speed up data entry

**Response**: `200 OK`
```json
{
  "transactionId": "1234567890123999",
  "tranId": 1234567890123999,
  "tranTypeCd": 1,
  "tranCatCd": 1001,
  "tranSource": "ONLINE",
  "tranDesc": "Purchase at JKL Store",
  "tranAmt": 7890.12,
  "tranCardNum": "4532123456789012",
  "tranMerchantId": "123456789",
  "tranMerchantName": "JKL Grocery Store",
  "tranMerchantCity": "Chicago",
  "tranMerchantZip": "60601",
  "tranOrigTs": "2023-12-31",
  "tranProcTs": "2023-12-31"
}
```

**Error Responses**:
- `404 Not Found`: No transactions found
- `500 Internal Server Error`: Server error

---

## Account Management APIs

### Base Path: `/api/accounts`

#### 1. Get All Accounts

**Endpoint**: `GET /api/accounts`

**Description**: Retrieve a paginated list of all accounts

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "acctId": 12345678901,
      "cardNumbers": ["4532123456789012", "4532123456789013"],
      "transactionCount": 25,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-12-31T14:30:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

---

#### 2. Get Account by ID

**Endpoint**: `GET /api/accounts/{id}`

**Description**: Retrieve an account by ID

**Path Parameters**:
- `id` (long, required): Account ID (11 digits)

**Response**: `200 OK`
```json
{
  "acctId": 12345678901,
  "cardNumbers": ["4532123456789012", "4532123456789013"],
  "transactionCount": 25,
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `404 Not Found`: Account not found
- `500 Internal Server Error`: Server error

---

#### 3. Create Account

**Endpoint**: `POST /api/accounts`

**Description**: Create a new account

**Request Body**:
```json
{
  "acctId": 12345678901
}
```

**Response**: `201 Created`
```json
{
  "acctId": 12345678901,
  "cardNumbers": [],
  "transactionCount": 0,
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data or account already exists
- `500 Internal Server Error`: Server error

---

#### 4. Update Account

**Endpoint**: `PUT /api/accounts/{id}`

**Description**: Update an existing account

**Path Parameters**:
- `id` (long, required): Account ID

**Request Body**:
```json
{
  "acctId": 12345678902
}
```

**Response**: `200 OK`

---

#### 5. Delete Account

**Endpoint**: `DELETE /api/accounts/{id}`

**Description**: Delete an account by ID

**Path Parameters**:
- `id` (long, required): Account ID

**Response**: `204 No Content`

**Error Responses**:
- `404 Not Found`: Account not found
- `500 Internal Server Error`: Server error

---

#### 6. Validate Account Exists (BR002)

**Endpoint**: `GET /api/accounts/validate/{id}`

**Description**: Validate that account ID exists in the system

**Path Parameters**:
- `id` (long, required): Account ID

**Response**: `200 OK`

**Error Responses**:
- `404 Not Found`: Account not found in CXACAIX file
- `500 Internal Server Error`: Server error

---

## Card Management APIs

### Base Path: `/api/cards`

#### 1. Get All Cards

**Endpoint**: `GET /api/cards`

**Description**: Retrieve a paginated list of all cards

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "cardNum": "4532123456789012",
      "acctId": 12345678901,
      "transactionCount": 15,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-12-31T14:30:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 5
}
```

---

#### 2. Get Card by Number

**Endpoint**: `GET /api/cards/{cardNum}`

**Description**: Retrieve a card by card number

**Path Parameters**:
- `cardNum` (string, required): Card number (16 digits)

**Response**: `200 OK`
```json
{
  "cardNum": "4532123456789012",
  "acctId": 12345678901,
  "transactionCount": 15,
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `404 Not Found`: Card not found
- `500 Internal Server Error`: Server error

---

#### 3. Create Card

**Endpoint**: `POST /api/cards`

**Description**: Create a new card

**Request Body**:
```json
{
  "cardNum": "4532123456789012",
  "acctId": 12345678901
}
```

**Response**: `201 Created`
```json
{
  "cardNum": "4532123456789012",
  "acctId": 12345678901,
  "transactionCount": 0,
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data, card already exists, or account not found
- `500 Internal Server Error`: Server error

**Validation Rules**:
- Card Number: Must be 16 digits, numeric only
- Account ID: Must exist in CXACAIX file (BR006)

---

#### 4. Update Card

**Endpoint**: `PUT /api/cards/{cardNum}`

**Description**: Update an existing card

**Path Parameters**:
- `cardNum` (string, required): Card number (16 digits)

**Request Body**:
```json
{
  "acctId": 12345678902
}
```

**Response**: `200 OK`

---

#### 5. Delete Card

**Endpoint**: `DELETE /api/cards/{cardNum}`

**Description**: Delete a card by card number

**Path Parameters**:
- `cardNum` (string, required): Card number (16 digits)

**Response**: `204 No Content`

**Error Responses**:
- `404 Not Found`: Card not found
- `500 Internal Server Error`: Server error

---

#### 6. Get Cards by Account ID (BR014)

**Endpoint**: `GET /api/cards/account/{acctId}`

**Description**: Retrieve all cards for a specific account

**Path Parameters**:
- `acctId` (long, required): Account ID

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "cardNum": "4532123456789012",
      "acctId": 12345678901,
      "transactionCount": 15,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-12-31T14:30:00"
    },
    {
      "cardNum": "4532123456789013",
      "acctId": 12345678901,
      "transactionCount": 10,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-12-31T14:30:00"
    }
  ],
  "totalElements": 2,
  "totalPages": 1
}
```

---

#### 7. Validate Card Exists (BR003)

**Endpoint**: `GET /api/cards/validate/{cardNum}`

**Description**: Validate that card number exists in the system

**Path Parameters**:
- `cardNum` (string, required): Card number (16 digits)

**Response**: `200 OK`

**Error Responses**:
- `404 Not Found`: Card not found in CCXREF file
- `500 Internal Server Error`: Server error

---

## Merchant Management APIs

### Base Path: `/api/merchants`

#### 1. Get All Merchants

**Endpoint**: `GET /api/merchants`

**Description**: Retrieve a paginated list of all merchants

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "merchantId": "123456789",
      "merchantName": "ABC Grocery Store",
      "merchantCity": "New York",
      "merchantZip": "10001",
      "transactionCount": 50,
      "createdAt": "2023-01-01T10:00:00",
      "updatedAt": "2023-12-31T14:30:00"
    }
  ],
  "totalElements": 200,
  "totalPages": 10
}
```

---

#### 2. Get Merchant by ID

**Endpoint**: `GET /api/merchants/{id}`

**Description**: Retrieve a merchant by ID

**Path Parameters**:
- `id` (string, required): Merchant ID (max 9 digits)

**Response**: `200 OK`
```json
{
  "merchantId": "123456789",
  "merchantName": "ABC Grocery Store",
  "merchantCity": "New York",
  "merchantZip": "10001",
  "transactionCount": 50,
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `404 Not Found`: Merchant not found
- `500 Internal Server Error`: Server error

---

#### 3. Create Merchant

**Endpoint**: `POST /api/merchants`

**Description**: Create a new merchant

**Request Body**:
```json
{
  "merchantId": "123456789",
  "merchantName": "ABC Grocery Store",
  "merchantCity": "New York",
  "merchantZip": "10001"
}
```

**Response**: `201 Created`
```json
{
  "merchantId": "123456789",
  "merchantName": "ABC Grocery Store",
  "merchantCity": "New York",
  "merchantZip": "10001",
  "transactionCount": 0,
  "createdAt": "2023-12-31T14:30:00",
  "updatedAt": "2023-12-31T14:30:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data or merchant already exists
- `500 Internal Server Error`: Server error

**Validation Rules** (BR008, BR009):
- Merchant ID: Must be numeric, max 9 digits, cannot be empty
- Merchant Name: Cannot be empty, max 30 characters
- Merchant City: Cannot be empty, max 25 characters
- Merchant Zip: Cannot be empty, max 10 characters

---

#### 4. Update Merchant

**Endpoint**: `PUT /api/merchants/{id}`

**Description**: Update an existing merchant

**Path Parameters**:
- `id` (string, required): Merchant ID

**Request Body**:
```json
{
  "merchantName": "ABC Grocery Store Updated",
  "merchantCity": "Brooklyn",
  "merchantZip": "11201"
}
```

**Response**: `200 OK`

---

#### 5. Delete Merchant

**Endpoint**: `DELETE /api/merchants/{id}`

**Description**: Delete a merchant by ID

**Path Parameters**:
- `id` (string, required): Merchant ID

**Response**: `204 No Content`

**Error Responses**:
- `404 Not Found`: Merchant not found
- `500 Internal Server Error`: Server error

---

#### 6. Search Merchants by Name

**Endpoint**: `GET /api/merchants/search`

**Description**: Search merchants by name containing search term

**Query Parameters**:
- `searchTerm` (string, required): Search term
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`
```json
{
  "content": [
    {
      "merchantId": "123456789",
      "merchantName": "ABC Grocery Store",
      "merchantCity": "New York",
      "merchantZip": "10001",
      "transactionCount": 50
    }
  ],
  "totalElements": 5,
  "totalPages": 1
}
```

---

#### 7. Get Merchants by City

**Endpoint**: `GET /api/merchants/city/{city}`

**Description**: Retrieve all merchants in a specific city

**Path Parameters**:
- `city` (string, required): City name

**Query Parameters**:
- `page` (integer, optional): Page number (default: 0)
- `size` (integer, optional): Page size (default: 20)

**Response**: `200 OK`

---

#### 8. Validate Merchant Exists (BR008)

**Endpoint**: `GET /api/merchants/validate/{id}`

**Description**: Validate that merchant ID exists in the system

**Path Parameters**:
- `id` (string, required): Merchant ID

**Response**: `200 OK`

**Error Responses**:
- `404 Not Found`: Merchant not found
- `500 Internal Server Error`: Server error

---

## Data Models

### Transaction Model

```json
{
  "transactionId": "string (16 chars)",
  "tranId": "long",
  "tranTypeCd": "integer",
  "tranCatCd": "integer",
  "tranSource": "string (max 10)",
  "tranDesc": "string (max 60)",
  "tranAmt": "decimal (11,2)",
  "tranCardNum": "string (16 chars)",
  "tranMerchantId": "string (max 9)",
  "tranMerchantName": "string (max 30)",
  "tranMerchantCity": "string (max 25)",
  "tranMerchantZip": "string (max 10)",
  "tranOrigTs": "date (YYYY-MM-DD)",
  "tranProcTs": "date (YYYY-MM-DD)",
  "formattedTransactionDate": "string (MM/DD/YY)",
  "formattedTransactionAmount": "string (+/-99999999.99)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Account Model

```json
{
  "acctId": "long (11 digits)",
  "cardNumbers": ["string"],
  "transactionCount": "integer",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Card Model

```json
{
  "cardNum": "string (16 digits)",
  "acctId": "long (11 digits)",
  "transactionCount": "integer",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Merchant Model

```json
{
  "merchantId": "string (max 9 digits)",
  "merchantName": "string (max 30)",
  "merchantCity": "string (max 25)",
  "merchantZip": "string (max 10)",
  "transactionCount": "integer",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

## Error Codes

### HTTP Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **204 No Content**: Resource deleted successfully
- **400 Bad Request**: Invalid request data or validation error
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

### Business Rule Error Messages

- **"Tran ID can NOT be empty..."**: Transaction ID is required
- **"Transaction ID NOT found..."**: Transaction does not exist
- **"Type CD must be entered..."**: Transaction type code is required
- **"Type CD must be Numeric..."**: Transaction type code must be numeric
- **"Category CD must be entered..."**: Transaction category code is required
- **"Category CD must be Numeric..."**: Transaction category code must be numeric
- **"Source must be entered..."**: Transaction source is required
- **"Description must be entered..."**: Transaction description is required
- **"Amount must be entered..."**: Transaction amount is required
- **"Amount should be in format -99999999.99"**: Invalid amount format
- **"Merchant ID must be entered..."**: Merchant ID is required
- **"Merchant ID must be Numeric..."**: Merchant ID must be numeric
- **"Merchant Name must be entered..."**: Merchant name is required
- **"Merchant City must be entered..."**: Merchant city is required
- **"Merchant Zip must be entered..."**: Merchant zip is required
- **"Orig Date must be entered..."**: Original date is required
- **"Orig Date should be in format YYYY-MM-DD"**: Invalid date format
- **"Proc Date must be entered..."**: Processing date is required
- **"Proc Date should be in format YYYY-MM-DD"**: Invalid date format
- **"Account ID must exist in CXACAIX file"**: Account not found
- **"Card Number must exist in CCXREF file"**: Card not found
- **"Duplicate transaction ID - transaction already exists"**: BR012 violation

---

## Business Rules Implementation

### BR001: Transaction List Pagination
- **Endpoint**: `GET /api/transactions/page/{pageNumber}`
- **Implementation**: Display transactions in pages with maximum 10 transactions per page
- **Features**: Page tracking, navigation support

### BR002: Transaction Selection
- **Endpoint**: `GET /api/transactions/select/{transactionId}`
- **Implementation**: Allow users to select a transaction for detailed view
- **Features**: Full transaction details retrieval

### BR003: Transaction Search by ID
- **Endpoint**: `GET /api/transactions/search/{startTransactionId}`
- **Implementation**: Search transactions starting from specific transaction ID
- **Features**: Paginated results, forward navigation

### BR004: Forward Page Navigation
- **Endpoint**: `GET /api/transactions/navigate/forward/{lastTransactionId}`
- **Implementation**: Navigate to next page of transactions
- **Features**: End of file detection

### BR005: Backward Page Navigation
- **Endpoint**: `GET /api/transactions/navigate/backward/{firstTransactionId}`
- **Implementation**: Navigate to previous page of transactions
- **Features**: Beginning of file detection

### BR006: Transaction Date Formatting
- **Implementation**: Format transaction timestamp to display date in MM/DD/YY format
- **Field**: `formattedTransactionDate`

### BR007: Transaction Amount Formatting
- **Implementation**: Format transaction amount with proper decimal places and sign
- **Field**: `formattedTransactionAmount`
- **Format**: +/-99999999.99

### BR008: End of File Handling
- **Implementation**: Handle scenarios when reaching end or beginning of transaction file
- **Features**: Empty page detection, navigation limits

### BR009: Page Number Tracking
- **Implementation**: Track and display current page number during navigation
- **Features**: Page metadata in responses

### BR010: Transaction List Initialization
- **Implementation**: Initialize transaction display fields before populating with new data
- **Features**: Empty state handling

### BR012: Duplicate Transaction ID Prevention
- **Implementation**: System prevents creation of transactions with duplicate IDs
- **Validation**: Check existence before insert

### BR013: Copy Last Transaction Feature
- **Endpoint**: `GET /api/transactions/copy-last`
- **Implementation**: Users can copy data from most recent transaction
- **Features**: Speed up data entry for similar transactions

### BR014: Account-Card Cross-Reference
- **Endpoints**: 
  - `GET /api/cards/account/{acctId}`
  - `GET /api/accounts/validate/{id}`
  - `GET /api/cards/validate/{cardNum}`
- **Implementation**: System maintains bidirectional lookup between Account ID and Card Number
- **Features**: Validation, relationship management

---

## Authentication & Authorization

**Note**: This API currently does not implement authentication. In production, consider adding:
- JWT token-based authentication
- Role-based access control (RBAC)
- API key authentication
- OAuth 2.0 integration

---

## Rate Limiting

**Note**: No rate limiting is currently implemented. Consider adding rate limiting in production to prevent abuse.

---

## Versioning

**Current Version**: v1

**API Version Strategy**: URL path versioning (e.g., `/api/v1/transactions`)

---

## Contact & Support

For API support and questions, please contact the development team.

---

**Last Updated**: 2024-01-01

**Generated by**: CardDemo Transaction Management System Code Generator
