# Account Management API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, no authentication is required. In production, implement appropriate security measures.

---

## Endpoints

### 1. Get All Accounts

Retrieve a paginated list of all accounts.

**Endpoint:** `GET /accounts`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20, max: 100)
- `sort` (optional): Sort field and direction (e.g., `acctId,asc`)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "acctId": 10000000001,
      "acctActiveStatus": "A",
      "acctActiveStatusDisplay": "Active",
      "isActive": true,
      "acctCurrBal": 1500.50,
      "acctCreditLimit": 5000.00,
      "acctCashCreditLimit": 1000.00,
      "availableCredit": 3500.50,
      "availableCashCredit": -500.50,
      "acctOpenDate": "2024-01-15",
      "acctExpirationDate": "2027-01-15",
      "isExpired": false,
      "acctReissueDate": null,
      "acctCurrCycCredit": 250.00,
      "acctCurrCycDebit": 150.00,
      "netCycleAmount": 100.00,
      "acctGroupId": "GRP001",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-20T14:45:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 2. Get Account by ID

Retrieve a specific account by its ID.

**Endpoint:** `GET /accounts/{acctId}`

**Path Parameters:**
- `acctId`: 11-digit account identifier

**Response:** `200 OK`
```json
{
  "acctId": 10000000001,
  "acctActiveStatus": "A",
  "acctActiveStatusDisplay": "Active",
  "isActive": true,
  "acctCurrBal": 1500.50,
  "acctCreditLimit": 5000.00,
  "acctCashCreditLimit": 1000.00,
  "availableCredit": 3500.50,
  "availableCashCredit": -500.50,
  "acctOpenDate": "2024-01-15",
  "acctExpirationDate": "2027-01-15",
  "isExpired": false,
  "acctReissueDate": null,
  "acctCurrCycCredit": 250.00,
  "acctCurrCycDebit": 150.00,
  "netCycleAmount": 100.00,
  "acctGroupId": "GRP001",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Account not found",
  "path": "/api/accounts/99999999999"
}
```

---

### 3. Create Account

Create a new account.

**Endpoint:** `POST /accounts`

**Request Body:**
```json
{
  "acctId": 10000000001,
  "acctActiveStatus": "A",
  "acctCurrBal": 1500.50,
  "acctCreditLimit": 5000.00,
  "acctCashCreditLimit": 1000.00,
  "acctOpenDate": "2024-01-15",
  "acctExpirationDate": "2027-01-15",
  "acctReissueDate": null,
  "acctCurrCycCredit": 250.00,
  "acctCurrCycDebit": 150.00,
  "acctGroupId": "GRP001"
}
```

**Validation Rules:**
- `acctId`: Required, must be 11-digit number (10000000000-99999999999)
- `acctActiveStatus`: Required, must be 'A' or 'I'
- `acctCurrBal`: Required, must be >= 0, max 13 integer digits, 2 decimal places
- `acctCreditLimit`: Required, must be >= 0, max 13 integer digits, 2 decimal places
- `acctCashCreditLimit`: Required, must be >= 0, must not exceed credit limit
- `acctOpenDate`: Required, cannot be in the future
- `acctExpirationDate`: Required, must be after open date, must be in the future
- `acctReissueDate`: Optional, if provided must be after open date
- `acctCurrCycCredit`: Required, must be >= 0
- `acctCurrCycDebit`: Required, must be >= 0
- `acctGroupId`: Optional, max 50 characters

**Response:** `201 Created`
```json
{
  "acctId": 10000000001,
  "acctActiveStatus": "A",
  "acctActiveStatusDisplay": "Active",
  "isActive": true,
  "acctCurrBal": 1500.50,
  "acctCreditLimit": 5000.00,
  "acctCashCreditLimit": 1000.00,
  "availableCredit": 3500.50,
  "availableCashCredit": -500.50,
  "acctOpenDate": "2024-01-15",
  "acctExpirationDate": "2027-01-15",
  "isExpired": false,
  "acctReissueDate": null,
  "acctCurrCycCredit": 250.00,
  "acctCurrCycDebit": 150.00,
  "netCycleAmount": 100.00,
  "acctGroupId": "GRP001",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Error Response:** `400 Bad Request`
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "acctId: Account ID must be an 11-digit number",
  "path": "/api/accounts"
}
```

---

### 4. Update Account

Update an existing account. All fields are optional for partial updates.

**Endpoint:** `PUT /accounts/{acctId}`

**Path Parameters:**
- `acctId`: 11-digit account identifier

**Request Body:**
```json
{
  "acctActiveStatus": "I",
  "acctCurrBal": 2000.00,
  "acctCreditLimit": 6000.00
}
```

**Response:** `200 OK`
```json
{
  "acctId": 10000000001,
  "acctActiveStatus": "I",
  "acctActiveStatusDisplay": "Inactive",
  "isActive": false,
  "acctCurrBal": 2000.00,
  "acctCreditLimit": 6000.00,
  "acctCashCreditLimit": 1000.00,
  "availableCredit": 4000.00,
  "availableCashCredit": -1000.00,
  "acctOpenDate": "2024-01-15",
  "acctExpirationDate": "2027-01-15",
  "isExpired": false,
  "acctReissueDate": null,
  "acctCurrCycCredit": 250.00,
  "acctCurrCycDebit": 150.00,
  "netCycleAmount": 100.00,
  "acctGroupId": "GRP001",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T15:45:00"
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Account with ID 99999999999 not found",
  "path": "/api/accounts/99999999999"
}
```

---

### 5. Delete Account

Delete an account by ID.

**Endpoint:** `DELETE /accounts/{acctId}`

**Path Parameters:**
- `acctId`: 11-digit account identifier

**Response:** `204 No Content`

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Account with ID 99999999999 not found",
  "path": "/api/accounts/99999999999"
}
```

---

### 6. Process Accounts Sequentially

Process all accounts in sequential order (implements BR-001 and BR-004).

**Endpoint:** `GET /accounts/process-sequential`

**Response:** `200 OK`
```json
[
  {
    "acctId": 10000000001,
    "acctActiveStatus": "A",
    "acctActiveStatusDisplay": "Active",
    "isActive": true,
    "acctCurrBal": 1500.50,
    "acctCreditLimit": 5000.00,
    "acctCashCreditLimit": 1000.00,
    "availableCredit": 3500.50,
    "availableCashCredit": -500.50,
    "acctOpenDate": "2024-01-15",
    "acctExpirationDate": "2027-01-15",
    "isExpired": false,
    "acctReissueDate": null,
    "acctCurrCycCredit": 250.00,
    "acctCurrCycDebit": 150.00,
    "netCycleAmount": 100.00,
    "acctGroupId": "GRP001",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:45:00"
  },
  {
    "acctId": 10000000002,
    "acctActiveStatus": "A",
    ...
  }
]
```

---

### 7. Get Active Accounts

Retrieve all active accounts (status = 'A').

**Endpoint:** `GET /accounts/active`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response:** `200 OK` (same structure as Get All Accounts)

---

### 8. Get Inactive Accounts

Retrieve all inactive accounts (status = 'I').

**Endpoint:** `GET /accounts/inactive`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response:** `200 OK` (same structure as Get All Accounts)

---

### 9. Get Accounts by Group

Retrieve accounts by group ID.

**Endpoint:** `GET /accounts/group/{groupId}`

**Path Parameters:**
- `groupId`: Account group identifier

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response:** `200 OK` (same structure as Get All Accounts)

---

### 10. Get Accounts Expiring Before Date

Retrieve accounts expiring before a specific date.

**Endpoint:** `GET /accounts/expiring-before`

**Query Parameters:**
- `date` (required): Date in ISO format (YYYY-MM-DD)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Example:** `GET /accounts/expiring-before?date=2025-12-31`

**Response:** `200 OK` (same structure as Get All Accounts)

---

### 11. Get Accounts Over Credit Limit

Retrieve accounts with balance exceeding credit limit.

**Endpoint:** `GET /accounts/over-credit-limit`

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response:** `200 OK` (same structure as Get All Accounts)

---

### 12. Get Total Balance

Calculate total balance across all accounts.

**Endpoint:** `GET /accounts/total-balance`

**Response:** `200 OK`
```json
15750.50
```

---

### 13. Get Total Active Balance

Calculate total balance for active accounts only.

**Endpoint:** `GET /accounts/total-active-balance`

**Response:** `200 OK`
```json
12500.75
```

---

### 14. Get Active Account Count

Get the count of active accounts.

**Endpoint:** `GET /accounts/count/active`

**Response:** `200 OK`
```json
8
```

---

### 15. Get Inactive Account Count

Get the count of inactive accounts.

**Endpoint:** `GET /accounts/count/inactive`

**Response:** `200 OK`
```json
3
```

---

## Error Responses

### Validation Error (400)
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "acctId: Account ID must be an 11-digit number, acctActiveStatus: Account active status must be 'A' or 'I'",
  "path": "/api/accounts"
}
```

### Business Rule Violation (400)
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Account with ID 10000000001 already exists",
  "path": "/api/accounts"
}
```

### Not Found (404)
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Account not found",
  "path": "/api/accounts/99999999999"
}
```

### Internal Server Error (500)
```json
{
  "timestamp": "2024-01-20T15:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/accounts"
}
```

---

## Business Rules

### BR-001: Account File Sequential Processing
- **Implementation**: `GET /accounts/process-sequential`
- **Description**: Processes all account records sequentially from the database
- **Behavior**: Returns accounts ordered by account ID in ascending order

### BR-002: Account Record Display
- **Implementation**: All GET endpoints
- **Description**: All account record fields are displayed for each successfully read record
- **Behavior**: Response DTOs include all entity fields plus computed values

### BR-004: End-of-File Detection
- **Implementation**: Integrated into sequential processing
- **Description**: End-of-file condition is detected and handled gracefully
- **Behavior**: Returns empty list when no accounts exist, logs completion

---

## Swagger UI

Interactive API documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

## OpenAPI Specification

JSON specification is available at:
```
http://localhost:8080/api-docs
```

---

## Testing with cURL

### Create Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": 10000000001,
    "acctActiveStatus": "A",
    "acctCurrBal": 1500.50,
    "acctCreditLimit": 5000.00,
    "acctCashCreditLimit": 1000.00,
    "acctOpenDate": "2024-01-15",
    "acctExpirationDate": "2027-01-15",
    "acctCurrCycCredit": 250.00,
    "acctCurrCycDebit": 150.00,
    "acctGroupId": "GRP001"
  }'
```

### Get Account
```bash
curl -X GET http://localhost:8080/api/accounts/10000000001
```

### Update Account
```bash
curl -X PUT http://localhost:8080/api/accounts/10000000001 \
  -H "Content-Type: application/json" \
  -d '{
    "acctActiveStatus": "I",
    "acctCurrBal": 2000.00
  }'
```

### Delete Account
```bash
curl -X DELETE http://localhost:8080/api/accounts/10000000001
```

### Get All Accounts (Paginated)
```bash
curl -X GET "http://localhost:8080/api/accounts?page=0&size=20&sort=acctId,asc"
```

### Process Sequentially
```bash
curl -X GET http://localhost:8080/api/accounts/process-sequential
```

---

## Rate Limiting

Currently, no rate limiting is implemented. Consider adding rate limiting in production.

## Versioning

Current API version: v1 (implicit in base URL)

Future versions should use explicit versioning: `/api/v2/accounts`

---

## Support

For issues or questions:
1. Check this documentation
2. Review the Swagger UI
3. Examine the code comments
4. Check the GENERATED_CODE_README.md file
