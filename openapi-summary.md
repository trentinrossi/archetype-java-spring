# Bill Payment System - API Documentation

## Overview

This document provides a comprehensive summary of all REST API endpoints generated for the **Bill Payment System**. The system implements a complete bill payment workflow with account validation, balance checking, payment confirmation, transaction recording, and balance updates.

## Business Rules Implementation

The system implements the following business rules:

- **BR001**: Account Validation - Validates that the entered account ID exists in the system
- **BR002**: Balance Check - Verifies that the account has a positive balance to pay
- **BR003**: Payment Confirmation - Requires user confirmation before processing payment
- **BR004**: Full Balance Payment - Payment processes the full current account balance
- **BR005**: Transaction ID Generation - Generates unique sequential transaction ID
- **BR006**: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
- **BR007**: Account Balance Update - Updates account balance after successful payment

---

## API Endpoints Summary

### Account Management APIs (`/api/accounts`)
- `GET /api/accounts` - Get all accounts (paginated)
- `GET /api/accounts/{acctId}` - Get account by ID
- `GET /api/accounts/positive-balance` - Get accounts with positive balance
- `GET /api/accounts/{acctId}/validate-payment` - Validate account for payment
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{acctId}` - Update account
- `DELETE /api/accounts/{acctId}` - Delete account

### Card Cross Reference APIs (`/api/card-cross-references`)
- `GET /api/card-cross-references` - Get all card cross references (paginated)
- `GET /api/card-cross-references/{id}` - Get cross reference by ID
- `GET /api/card-cross-references/by-card/{cardNum}` - Get by card number
- `GET /api/card-cross-references/by-account/{acctId}` - Get by account ID
- `POST /api/card-cross-references` - Create new cross reference
- `PUT /api/card-cross-references/{id}` - Update cross reference
- `DELETE /api/card-cross-references/{id}` - Delete cross reference

### Transaction APIs (`/api/transactions`)
- `GET /api/transactions` - Get all transactions (paginated)
- `GET /api/transactions/{tranId}` - Get transaction by ID
- `GET /api/transactions/by-card/{cardNum}` - Get by card number
- `GET /api/transactions/by-account/{acctId}` - Get by account ID
- `GET /api/transactions/bill-payments` - Get bill payment transactions
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{tranId}` - Update transaction
- `DELETE /api/transactions/{tranId}` - Delete transaction

### Bill Payment Workflow APIs (`/api/bill-payment`)
- `GET /api/bill-payment/preview` - Preview bill payment
- `POST /api/bill-payment/process` - Process bill payment

---

## Complete Bill Payment Workflow

### Step 1: Preview Payment (Optional)
**Endpoint**: `GET /api/bill-payment/preview`

**Query Parameters**:
- `acctId`: Account identifier (11 characters)
- `cardNum`: Card number (16 characters)

**Example Request**:
```bash
GET /api/bill-payment/preview?acctId=ACC00001234&cardNum=4111111111111111
```

**Example Response** (200 OK):
```json
{
  "success": false,
  "message": "Bill payment preview. Confirm to proceed with payment.",
  "transactionId": null,
  "acctId": "ACC00001234",
  "cardNum": "4111111111111111",
  "amountPaid": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "processedAt": "2024-01-15T10:30:00"
}
```

### Step 2: Process Payment
**Endpoint**: `POST /api/bill-payment/process`

**Request Body**:
```json
{
  "acctId": "ACC00001234",
  "cardNum": "4111111111111111",
  "confirmed": true
}
```

**Example Response** (200 OK):
```json
{
  "success": true,
  "message": "Bill payment processed successfully",
  "transactionId": 1234567890123456,
  "acctId": "ACC00001234",
  "cardNum": "4111111111111111",
  "amountPaid": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "processedAt": "2024-01-15T10:30:00"
}
```

**Business Rules Applied**:
1. **BR001**: Validates account exists
2. **BR002**: Checks account has positive balance
3. **BR003**: Requires confirmation (`confirmed: true`)
4. **BR004**: Pays full account balance
5. **BR005**: Generates sequential transaction ID
6. **BR006**: Records transaction with bill payment attributes
7. **BR007**: Updates account balance to zero

---

## Error Handling

### Common Error Messages

**Account Validation Errors (BR001)**:
- `"Acct ID can NOT be empty..."` - Account ID is null or empty
- `"Account ID NOT found..."` - Account does not exist

**Balance Check Errors (BR002)**:
- `"You have nothing to pay..."` - Account balance is zero or negative

**Card Validation Errors**:
- `"Card number not associated with this account"` - Card not linked to account

**Validation Errors**:
- `"Account ID must be exactly 11 characters"` - Invalid account ID length
- `"Card number must be exactly 16 characters"` - Invalid card number length
- `"Transaction amount must be greater than zero"` - Invalid amount

---

## Sample Data

### Accounts
| Account ID   | Balance    | Status              |
|--------------|------------|---------------------|
| ACC00001234  | $1,500.00  | Has positive balance|
| ACC00005678  | $2,750.50  | Has positive balance|
| ACC00009012  | $500.25    | Has positive balance|
| ACC00003456  | $0.00      | No balance to pay   |
| ACC00007890  | $10,000.00 | Has positive balance|

### Card Cross References
| Card Number      | Account ID   |
|------------------|--------------|
| 4111111111111111 | ACC00001234  |
| 4222222222222222 | ACC00005678  |
| 4333333333333333 | ACC00009012  |
| 4444444444444444 | ACC00003456  |
| 4555555555555555 | ACC00007890  |

---

## Testing Examples

### Using cURL

**1. Preview Bill Payment**:
```bash
curl -X GET "http://localhost:8080/api/bill-payment/preview?acctId=ACC00001234&cardNum=4111111111111111"
```

**2. Process Bill Payment**:
```bash
curl -X POST "http://localhost:8080/api/bill-payment/process" \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": "ACC00001234",
    "cardNum": "4111111111111111",
    "confirmed": true
  }'
```

**3. Verify Account Balance**:
```bash
curl -X GET "http://localhost:8080/api/accounts/ACC00001234"
```

**4. View Transaction History**:
```bash
curl -X GET "http://localhost:8080/api/transactions/by-account/ACC00001234"
```

**5. Get All Bill Payment Transactions**:
```bash
curl -X GET "http://localhost:8080/api/transactions/bill-payments"
```

---

## Technical Details

### Technology Stack
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **Documentation**: OpenAPI/Swagger

### Database Tables
- `accounts` - Customer account information
- `card_cross_reference` - Card-to-account mappings
- `transactions` - Bill payment transaction records

### Transaction Management
- All payment operations use database transactions
- Automatic rollback on errors
- Ensures data consistency

---

## Data Models

### Account Response
```json
{
  "acctId": "string (11 chars)",
  "acctCurrBal": "decimal (13,2)",
  "hasPositiveBalance": "boolean",
  "cardCrossReferences": "array",
  "transactions": "array",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Transaction Response
```json
{
  "tranId": "long (16 digits)",
  "tranTypeCd": "string (2 chars, default: 02)",
  "tranCatCd": "integer (1 digit, default: 2)",
  "tranSource": "string (10 chars, default: POS TERM)",
  "tranDesc": "string (50 chars)",
  "tranAmt": "decimal (11,2)",
  "tranCardNum": "string (16 chars)",
  "tranMerchantId": "integer (9 digits)",
  "tranMerchantName": "string (50 chars)",
  "tranMerchantCity": "string (50 chars)",
  "tranMerchantZip": "string (10 chars)",
  "tranOrigTs": "timestamp",
  "tranProcTs": "timestamp",
  "acctId": "string (11 chars)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Bill Payment Response
```json
{
  "success": "boolean",
  "message": "string",
  "transactionId": "long (nullable)",
  "acctId": "string (11 chars)",
  "cardNum": "string (16 chars)",
  "amountPaid": "decimal",
  "previousBalance": "decimal",
  "newBalance": "decimal",
  "processedAt": "timestamp"
}
```

---

## Notes

- All timestamps are in ISO 8601 format
- All monetary amounts use 2 decimal places
- Account IDs are exactly 11 characters
- Card numbers are exactly 16 characters
- Transaction IDs are generated sequentially
- Bill payment transactions use type code "02" and category code 2
- Full balance payments reduce account balance to zero
- All endpoints return proper HTTP status codes
