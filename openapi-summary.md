# API Endpoints Summary - User and Security Administration

## Overview
This document provides a comprehensive summary of all REST API endpoints for the User and Security Administration system, following the OpenAPI specification.

---

## Authentication Endpoints

### POST /api/auth/login
**Description:** Authenticate user with credentials and return user information.

**Request Body:**
```json
{
  "userId": "string (required, max 8 chars)",
  "password": "string (required, max 8 chars)"
}
```

**Response 200 OK:**
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string (A=Admin, R=Regular)",
  "message": "string"
}
```

**Response 400 Bad Request:**
```json
{
  "userId": null,
  "firstName": null,
  "lastName": null,
  "userType": null,
  "message": "Wrong Password. Try again..."
}
```

---

## User Management Endpoints

### GET /api/users
**Description:** Retrieve paginated list of all users with optional filtering.

**Query Parameters:**
- `page` (integer, optional, default: 0) - Page number
- `size` (integer, optional, default: 10) - Page size
- `startUserId` (string, optional) - Filter users starting from this User ID

**Response 200 OK:**
```json
{
  "content": [
    {
      "userId": "string",
      "firstName": "string",
      "lastName": "string",
      "password": "string",
      "userType": "string",
      "createdAt": "string (yyyy-MM-dd HH:mm:ss)",
      "updatedAt": "string (yyyy-MM-dd HH:mm:ss)"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 100,
  "totalPages": 10,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### GET /api/users/{userId}
**Description:** Retrieve specific user by User ID.

**Path Parameters:**
- `userId` (string, required) - User identifier

**Response 200 OK:**
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "password": "string",
  "userType": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Response 404 Not Found:**
```json
{
  "message": "User ID NOT found..."
}
```

---

### POST /api/users
**Description:** Create a new user account.

**Request Body:**
```json
{
  "userId": "string (required, max 8 chars)",
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or R)"
}
```

**Response 201 Created:**
```json
{
  "user": {
    "userId": "string",
    "firstName": "string",
    "lastName": "string",
    "password": "string",
    "userType": "string",
    "createdAt": "string",
    "updatedAt": "string"
  },
  "message": "User {userId} has been added..."
}
```

**Response 400 Bad Request:**
```json
{
  "message": "User ID already exist..."
}
```

---

### PUT /api/users/{userId}
**Description:** Update existing user information.

**Path Parameters:**
- `userId` (string, required) - User identifier

**Request Body:**
```json
{
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or R)"
}
```

**Response 200 OK:**
```json
{
  "user": {
    "userId": "string",
    "firstName": "string",
    "lastName": "string",
    "password": "string",
    "userType": "string",
    "createdAt": "string",
    "updatedAt": "string"
  },
  "message": "User {userId} has been updated..."
}
```

**Response 400 Bad Request:**
```json
{
  "message": "User ID NOT found..." | "Please modify to update..."
}
```

---

### DELETE /api/users/{userId}
**Description:** Delete user account from the system.

**Path Parameters:**
- `userId` (string, required) - User identifier

**Response 200 OK:**
```json
{
  "message": "User {userId} has been deleted..."
}
```

**Response 400 Bad Request:**
```json
{
  "message": "User ID NOT found..."
}
```

---

## Transaction Management Endpoints

### GET /api/transactions
**Description:** Retrieve paginated list of all transactions with optional filtering.

**Query Parameters:**
- `page` (integer, optional, default: 0) - Page number
- `size` (integer, optional, default: 10) - Page size
- `startTransactionId` (string, optional) - Filter transactions starting from this Transaction ID

**Response 200 OK:**
```json
{
  "content": [
    {
      "transactionId": "string",
      "transactionDate": "string (MM/dd/yy)",
      "transactionDescription": "string",
      "transactionAmount": "number (decimal)",
      "userId": "string",
      "userFullName": "string",
      "createdAt": "string"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 100,
  "totalPages": 10,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### GET /api/transactions/{transactionId}
**Description:** Retrieve specific transaction by Transaction ID.

**Path Parameters:**
- `transactionId` (string, required) - Transaction identifier

**Response 200 OK:**
```json
{
  "transactionId": "string",
  "transactionDate": "string",
  "transactionDescription": "string",
  "transactionAmount": "number",
  "userId": "string",
  "userFullName": "string",
  "createdAt": "string"
}
```

**Response 400 Bad Request:**
```json
{
  "message": "Transaction not found"
}
```

---

### GET /api/transactions/date-range
**Description:** Retrieve transactions within a specific date range.

**Query Parameters:**
- `startDate` (string, required, format: yyyy-MM-dd) - Start date of range
- `endDate` (string, required, format: yyyy-MM-dd) - End date of range

**Response 200 OK:**
```json
[
  {
    "transactionId": "string",
    "transactionDate": "string",
    "transactionDescription": "string",
    "transactionAmount": "number",
    "userId": "string",
    "userFullName": "string",
    "createdAt": "string"
  }
]
```

**Response 400 Bad Request:**
```json
{
  "message": "Invalid date format. Expected: yyyy-MM-dd"
}
```

---

### GET /api/transactions/monthly
**Description:** Retrieve all transactions for the current month.

**Response 200 OK:**
```json
{
  "transactions": [
    {
      "transactionId": "string",
      "transactionDate": "string",
      "transactionDescription": "string",
      "transactionAmount": "number",
      "userId": "string",
      "userFullName": "string",
      "createdAt": "string"
    }
  ],
  "message": "Monthly report generated successfully"
}
```

---

### GET /api/transactions/yearly
**Description:** Retrieve all transactions for the current year.

**Response 200 OK:**
```json
{
  "transactions": [
    {
      "transactionId": "string",
      "transactionDate": "string",
      "transactionDescription": "string",
      "transactionAmount": "number",
      "userId": "string",
      "userFullName": "string",
      "createdAt": "string"
    }
  ],
  "message": "Yearly report generated successfully"
}
```

---

### GET /api/transactions/user/{userId}
**Description:** Retrieve paginated transactions for a specific user.

**Path Parameters:**
- `userId` (string, required) - User identifier

**Query Parameters:**
- `page` (integer, optional, default: 0) - Page number
- `size` (integer, optional, default: 10) - Page size

**Response 200 OK:**
```json
{
  "content": [
    {
      "transactionId": "string",
      "transactionDate": "string",
      "transactionDescription": "string",
      "transactionAmount": "number",
      "userId": "string",
      "userFullName": "string",
      "createdAt": "string"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 50,
  "totalPages": 5,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

---

## Data Models

### User
```json
{
  "userId": "string (max 8 chars, primary key)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (1 char, A=Admin, R=Regular, required)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Transaction
```json
{
  "transactionId": "string (max 16 chars, primary key)",
  "transactionDate": "timestamp (required)",
  "transactionDescription": "string (max 26 chars)",
  "transactionAmount": "decimal (12,2, required)",
  "userId": "string (max 8 chars, foreign key)",
  "createdAt": "timestamp"
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
Returned when request validation fails or business logic errors occur.

### 404 Not Found
Returned when requested resource does not exist.

### 500 Internal Server Error
Returned when unexpected server errors occur.

---

## Validation Rules

### User Fields
- **userId**: Required, max 8 characters, must be unique
- **firstName**: Required, max 20 characters, cannot be empty
- **lastName**: Required, max 20 characters, cannot be empty
- **password**: Required, max 8 characters, cannot be empty
- **userType**: Required, must be 'A' (Admin) or 'R' (Regular)

### Transaction Fields
- **transactionId**: Required, max 16 characters, must be unique
- **transactionDate**: Required, valid timestamp
- **transactionDescription**: Optional, max 26 characters
- **transactionAmount**: Required, decimal with 2 decimal places
- **userId**: Optional, must reference existing user

---

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Zero-based page number (default: 0)
- `size`: Number of items per page (default: 10)

Pagination responses include:
- `content`: Array of items
- `pageNumber`: Current page number
- `pageSize`: Items per page
- `totalElements`: Total number of items
- `totalPages`: Total number of pages
- `first`: Boolean indicating if this is the first page
- `last`: Boolean indicating if this is the last page
- `hasNext`: Boolean indicating if there is a next page
- `hasPrevious`: Boolean indicating if there is a previous page

---

## Security Considerations

1. **Authentication**: All endpoints except `/api/auth/login` should require authentication
2. **Authorization**: Admin-only operations should verify user type is 'A'
3. **Password Handling**: Passwords should be encrypted/hashed in production
4. **Input Validation**: All inputs are validated according to defined constraints
5. **SQL Injection Prevention**: Using JPA/Hibernate with parameterized queries

---

## Notes

- All date/time values are in ISO format unless specified otherwise
- User IDs and Transaction IDs are case-sensitive
- Passwords are converted to uppercase for consistency
- Pagination is zero-based (first page is 0)
- Default page size is 10 items
- Maximum page size should be limited to prevent performance issues
