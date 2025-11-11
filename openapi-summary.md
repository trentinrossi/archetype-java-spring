# Bill Payment API - OpenAPI Summary

## Overview

This document provides a comprehensive summary of all API endpoints generated for the CardDemo Bill Payment Processing system. The API implements a complete bill payment workflow with full business rule validation.

---

## Base URL

```
http://localhost:8080/api
```

---

## Business Rules Implementation

The API implements the following business rules:

- **BR001**: Account Validation - Validates that the entered account ID exists in the system
- **BR002**: Balance Check - Verifies that the account has a positive balance to pay
- **BR003**: Payment Confirmation - Requires user confirmation before processing payment
- **BR004**: Full Balance Payment - Payment processes the full current account balance
- **BR005**: Transaction ID Generation - Generates unique sequential transaction ID
- **BR006**: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
- **BR007**: Account Balance Update - Updates account balance after successful payment

---

## API Endpoints

### 1. Bill Payment API

Main API for processing bill payments with complete workflow implementation.

#### POST /api/bill-payment/process

Process a bill payment for an account.

**Request Body:**
```json
{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmation": "Y"
}
```

**Workflow:**
1. First call without confirmation to get account info
2. Second call with confirmation='Y' to process payment
3. Or with confirmation='N' to cancel

**Response (Success):**
```json
{
  "status": "SUCCESS",
  "message": "Bill payment processed successfully",
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "transactionId": 1,
  "maskedCardNumber": "************1111",
  "paymentTimestamp": "2024-01-15T10:30:00",
  "formattedPaymentAmount": "$1500.00"
}
```

**Response (Pending Confirmation):**
```json
{
  "status": "PENDING_CONFIRMATION",
  "message": "Please confirm payment. Current balance: $1500.00",
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "maskedCardNumber": "************1111"
}
```

**Response (Cancelled):**
```json
{
  "status": "CANCELLED",
  "message": "Payment cancelled by user",
  "accountId": "ACC00001234",
  "paymentAmount": 0.00,
  "previousBalance": 1500.00,
  "newBalance": 1500.00,
  "maskedCardNumber": "************1111"
}
```

**Error Responses:**
- `400 Bad Request`: Validation failed
  - "Acct ID can NOT be empty..."
  - "Account ID NOT found..."
  - "You have nothing to pay..."
  - "Card number is not associated with this account"
  - "Invalid value. Valid values are (Y/N)..."

---

#### GET /api/bill-payment/account/{accountId}/confirmation

Get account information for payment confirmation.

**Parameters:**
- `accountId` (path): Account ID (11 characters)
- `cardNumber` (query, optional): Card number (16 characters)

**Response:**
```json
{
  "status": "PENDING_CONFIRMATION",
  "message": "Please confirm payment. Current balance: $1500.00",
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "maskedCardNumber": "************1111"
}
```

---

#### POST /api/bill-payment/quick-process

Quick bill payment in a single step (confirmation handled client-side).

**Parameters:**
- `accountId` (query): Account ID
- `cardNumber` (query): Card number

**Response:** Same as `/process` endpoint

---

### 2. Account Management API

APIs for managing credit card accounts and bill payments.

#### GET /api/accounts

Get all accounts (paginated).

**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)
- `sort` (query, optional): Sort field and direction

**Response:**
```json
{
  "content": [
    {
      "acctId": "ACC00001234",
      "acctCurrBal": 1500.00,
      "hasPositiveBalance": true,
      "paymentAmount": 1500.00,
      "eligibleForPayment": true,
      "paymentStatusMessage": "Account ready for payment",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {...},
  "totalElements": 5,
  "totalPages": 1
}
```

---

#### GET /api/accounts/{acctId}

Get account by ID.

**Parameters:**
- `acctId` (path): Account ID (11 characters)

**Response:**
```json
{
  "acctId": "ACC00001234",
  "acctCurrBal": 1500.00,
  "hasPositiveBalance": true,
  "paymentAmount": 1500.00,
  "eligibleForPayment": true,
  "paymentStatusMessage": "Account ready for payment",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Error Responses:**
- `404 Not Found`: Account ID NOT found...

---

#### POST /api/accounts

Create a new account.

**Request Body:**
```json
{
  "acctId": "ACC00001234",
  "acctCurrBal": 1500.00
}
```

**Validation:**
- `acctId`: Required, exactly 11 characters
- `acctCurrBal`: Required, must be >= 0

**Response:** `201 Created` with account details

---

#### PUT /api/accounts/{acctId}

Update an existing account.

**Request Body:**
```json
{
  "acctCurrBal": 2000.00
}
```

**Response:** `200 OK` with updated account details

---

#### DELETE /api/accounts/{acctId}

Delete an account.

**Response:** `204 No Content`

---

#### GET /api/accounts/{acctId}/validate-payment

Validate account for payment (BR001 + BR002).

**Response:**
```json
{
  "valid": true,
  "message": "Account is valid for payment",
  "accountId": "ACC00001234"
}
```

**Error Response:**
```json
{
  "valid": false,
  "message": "You have nothing to pay...",
  "accountId": "ACC00001234"
}
```

---

#### GET /api/accounts/{acctId}/payment-amount

Get full balance payment amount (BR004).

**Response:**
```json
{
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "formattedAmount": "$1500.00",
  "message": "Full balance payment amount"
}
```

---

#### GET /api/accounts/positive-balance

Get accounts with positive balance (eligible for payment).

**Parameters:**
- `page`, `size`, `sort` (same as GET /api/accounts)

**Response:** Paginated list of accounts with positive balance

---

### 3. Card Cross Reference API

APIs for managing card-account cross-references.

#### GET /api/card-cross-references

Get all card cross-references (paginated).

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "xrefAcctId": "ACC00001234",
      "xrefCardNum": "4111111111111111",
      "maskedCardNum": "************1111",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ],
  "pageable": {...},
  "totalElements": 6,
  "totalPages": 1
}
```

---

#### GET /api/card-cross-references/{id}

Get card cross-reference by ID.

---

#### GET /api/card-cross-references/account/{acctId}

Get all card cross-references for a specific account.

**Response:** Array of card cross-references

---

#### GET /api/card-cross-references/card/{cardNum}

Get all card cross-references for a specific card.

**Response:** Array of card cross-references

---

#### POST /api/card-cross-references

Create a new card cross-reference.

**Request Body:**
```json
{
  "xrefAcctId": "ACC00001234",
  "xrefCardNum": "4111111111111111"
}
```

**Validation:**
- `xrefAcctId`: Required, exactly 11 characters
- `xrefCardNum`: Required, exactly 16 characters

**Response:** `201 Created` with cross-reference details

---

#### PUT /api/card-cross-references/{id}

Update card cross-reference.

**Request Body:**
```json
{
  "xrefCardNum": "4222222222222222"
}
```

---

#### DELETE /api/card-cross-references/{id}

Delete card cross-reference.

**Response:** `204 No Content`

---

#### GET /api/card-cross-references/validate

Validate that a card is associated with an account.

**Parameters:**
- `acctId` (query): Account ID
- `cardNum` (query): Card number

**Response:**
```json
{
  "valid": true,
  "accountId": "ACC00001234",
  "message": "Card is associated with account"
}
```

---

### 4. Transaction Management API

APIs for managing bill payment transactions.

#### GET /api/transactions

Get all transactions (paginated).

**Response:**
```json
{
  "content": [
    {
      "tranId": 1,
      "tranTypeCd": "02",
      "tranCatCd": 2,
      "tranSource": "POS TERM",
      "tranDesc": "BILL PAYMENT - ONLINE",
      "tranAmt": 1500.00,
      "tranCardNum": "4111111111111111",
      "maskedCardNum": "************1111",
      "tranMerchantId": 999999999,
      "tranMerchantName": "BILL PAYMENT",
      "tranMerchantCity": "N/A",
      "tranMerchantZip": "N/A",
      "tranOrigTs": "2024-01-15T10:30:00",
      "tranProcTs": "2024-01-15T10:30:00",
      "accountId": "ACC00001234",
      "formattedAmount": "$1500.00",
      "isBillPayment": true,
      "transactionSummary": "Transaction ID: 1, Type: BILL PAYMENT - ONLINE, Amount: $1500.00, Date: 2024-01-15T10:30:00",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {...}
}
```

---

#### GET /api/transactions/{id}

Get transaction by ID.

---

#### POST /api/transactions

Create a new transaction.

**Request Body:**
```json
{
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.00,
  "tranCardNum": "4111111111111111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "accountId": "ACC00001234"
}
```

**Validation:**
- All fields required except `tranOrigTs` and `tranProcTs` (auto-generated)
- `tranTypeCd`: Exactly 2 characters
- `tranCatCd`: Single digit (0-9)
- `tranCardNum`: Exactly 16 characters
- `accountId`: Exactly 11 characters

**Response:** `201 Created` with transaction details

---

#### PUT /api/transactions/{id}

Update transaction (limited fields).

**Request Body:**
```json
{
  "tranDesc": "BILL PAYMENT - UPDATED",
  "tranMerchantName": "UPDATED MERCHANT",
  "tranMerchantCity": "NEW YORK",
  "tranMerchantZip": "10001"
}
```

---

#### DELETE /api/transactions/{id}

Delete transaction.

**Response:** `204 No Content`

---

#### GET /api/transactions/card/{cardNum}

Get transactions by card number (paginated).

---

#### GET /api/transactions/account/{accountId}

Get transactions by account ID (paginated).

---

#### GET /api/transactions/bill-payments

Get all bill payment transactions (type=02, category=2) (paginated).

**Response:** Filtered list of bill payment transactions only

---

#### GET /api/transactions/bill-payments/card/{cardNum}

Get bill payment transactions for a specific card (paginated).

---

#### GET /api/transactions/date-range

Get transactions within a date range.

**Parameters:**
- `startDate` (query): Start date (ISO 8601 format)
- `endDate` (query): End date (ISO 8601 format)
- `page`, `size`, `sort` (pagination)

**Example:**
```
GET /api/transactions/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
```

---

#### GET /api/transactions/recent

Get recent transactions since a specific date.

**Parameters:**
- `since` (query): Date to filter from (ISO 8601 format)
- `page`, `size`, `sort` (pagination)

**Example:**
```
GET /api/transactions/recent?since=2024-01-01T00:00:00
```

---

#### GET /api/transactions/statistics/card/{cardNum}

Get transaction statistics for a card.

**Response:**
```json
{
  "cardNumber": "************1111",
  "transactionCount": 5,
  "totalAmount": 7500.00,
  "formattedTotalAmount": "$7500.00"
}
```

---

#### GET /api/transactions/statistics/bill-payments

Get bill payment statistics.

**Response:**
```json
{
  "billPaymentCount": 3,
  "totalBillPaymentAmount": 4250.50,
  "formattedTotalAmount": "$4250.50"
}
```

---

## Error Codes and Messages

### Common Error Messages

| Error Code | Message | Description |
|------------|---------|-------------|
| 400 | Acct ID can NOT be empty... | Account ID is required |
| 404 | Account ID NOT found... | Account does not exist |
| 400 | You have nothing to pay... | Account balance is zero or negative |
| 400 | Invalid value. Valid values are (Y/N)... | Invalid confirmation value |
| 400 | Card number is not associated with this account | Card validation failed |
| 400 | Account ID already exists | Duplicate account creation |
| 404 | Card cross-reference not found | Cross-reference does not exist |
| 404 | Transaction not found | Transaction does not exist |

---

## Data Models

### Account
- `acctId`: String (11 characters) - Unique account identifier
- `acctCurrBal`: Decimal (13,2) - Current account balance
- `hasPositiveBalance`: Boolean - Computed field
- `paymentAmount`: Decimal - Full balance for payment
- `eligibleForPayment`: Boolean - Computed field
- `paymentStatusMessage`: String - Status message
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### CardCrossReference
- `id`: Long - Auto-generated ID
- `xrefAcctId`: String (11 characters) - Account ID
- `xrefCardNum`: String (16 characters) - Card number
- `maskedCardNum`: String - Masked card number for display
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### Transaction
- `tranId`: Long - Auto-generated transaction ID
- `tranTypeCd`: String (2 characters) - Transaction type (02 for bill payment)
- `tranCatCd`: Integer - Transaction category (2 for bill payment)
- `tranSource`: String (10 characters) - Transaction source
- `tranDesc`: String (50 characters) - Description
- `tranAmt`: Decimal (11,2) - Transaction amount
- `tranCardNum`: String (16 characters) - Card number
- `maskedCardNum`: String - Masked card number
- `tranMerchantId`: Integer - Merchant ID
- `tranMerchantName`: String (50 characters) - Merchant name
- `tranMerchantCity`: String (50 characters) - Merchant city
- `tranMerchantZip`: String (10 characters) - Merchant ZIP
- `tranOrigTs`: Timestamp - Origination timestamp
- `tranProcTs`: Timestamp - Processing timestamp
- `accountId`: String (11 characters) - Associated account
- `formattedAmount`: String - Formatted amount display
- `isBillPayment`: Boolean - Computed field
- `transactionSummary`: String - Summary text
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

---

## Bill Payment Workflow Example

### Step 1: Validate Account and Get Payment Info

```http
GET /api/bill-payment/account/ACC00001234/confirmation?cardNumber=4111111111111111
```

**Response:**
```json
{
  "status": "PENDING_CONFIRMATION",
  "message": "Please confirm payment. Current balance: $1500.00",
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "maskedCardNumber": "************1111"
}
```

### Step 2: User Confirms Payment

```http
POST /api/bill-payment/process
Content-Type: application/json

{
  "accountId": "ACC00001234",
  "cardNumber": "4111111111111111",
  "confirmation": "Y"
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Bill payment processed successfully",
  "accountId": "ACC00001234",
  "paymentAmount": 1500.00,
  "previousBalance": 1500.00,
  "newBalance": 0.00,
  "transactionId": 1,
  "maskedCardNumber": "************1111",
  "paymentTimestamp": "2024-01-15T10:30:00",
  "formattedPaymentAmount": "$1500.00"
}
```

### Step 3: Verify Transaction

```http
GET /api/transactions/1
```

**Response:**
```json
{
  "tranId": 1,
  "tranTypeCd": "02",
  "tranCatCd": 2,
  "tranSource": "POS TERM",
  "tranDesc": "BILL PAYMENT - ONLINE",
  "tranAmt": 1500.00,
  "tranCardNum": "4111111111111111",
  "maskedCardNum": "************1111",
  "tranMerchantId": 999999999,
  "tranMerchantName": "BILL PAYMENT",
  "tranMerchantCity": "N/A",
  "tranMerchantZip": "N/A",
  "tranOrigTs": "2024-01-15T10:30:00",
  "tranProcTs": "2024-01-15T10:30:00",
  "accountId": "ACC00001234",
  "formattedAmount": "$1500.00",
  "isBillPayment": true,
  "transactionSummary": "Transaction ID: 1, Type: BILL PAYMENT - ONLINE, Amount: $1500.00, Date: 2024-01-15T10:30:00"
}
```

---

## Testing with Sample Data

The system includes sample data for testing:

### Sample Accounts
- `ACC00001234` - Balance: $1500.00
- `ACC00005678` - Balance: $2750.50
- `ACC00009012` - Balance: $0.00 (no payment available)
- `ACC00003456` - Balance: $500.25
- `ACC00007890` - Balance: $10000.00

### Sample Cards
- `4111111111111111` - Associated with ACC00001234
- `4222222222222222` - Associated with ACC00005678
- `4333333333333333` - Associated with ACC00009012
- `4444444444444444` - Associated with ACC00003456
- `4555555555555555` - Associated with ACC00007890
- `4666666666666666` - Associated with ACC00001234 (second card)

---

## Security Considerations

1. **Card Number Masking**: All API responses mask card numbers, showing only the last 4 digits
2. **Validation**: All inputs are validated with proper error messages
3. **Transaction Integrity**: Payment processing uses database transactions to ensure consistency
4. **Audit Trail**: All operations are logged with timestamps

---

## Technical Stack

- **Framework**: Spring Boot 3.5.5
- **Java Version**: 21
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Validation**: Jakarta Bean Validation

---

## Getting Started

1. **Start the application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Access Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Test with sample data**:
   - Use the sample accounts and cards listed above
   - Try the bill payment workflow
   - Explore transaction history

---

## Support

For issues or questions, please refer to the business rules documentation or contact the development team.
