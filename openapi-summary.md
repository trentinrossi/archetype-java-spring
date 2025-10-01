# User Management System - OpenAPI Summary

## Overview
This document provides a comprehensive summary of all REST API endpoints for the User Management System, based on the CICS programs COUSR00C, COUSR01C, COUSR02C, and COUSR03C.

## Base URL
```
/api/users
```

## Endpoints

### 1. Get Paginated List of Users (COUSR00C)
**Endpoint:** `GET /api/users`

**Description:** Retrieve a paginated list of users with maximum 10 per page. Supports filtering by starting User ID for navigation.

**Parameters:**
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Page size (maximum 10), default: 10  
- `startingUserId` (query, optional): Starting User ID for filtering

**Response:** `200 OK`
```json
{
  "content": [
    {
      "secUsrId": "USR00001",
      "secUsrFname": "John",
      "secUsrLname": "Doe",
      "fullName": "John Doe",
      "secUsrType": "U",
      "userTypeDisplayName": "User",
      "createdAt": "2023-10-01T12:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3
}
```

**Error Responses:**
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: System error

---

### 2. Get User by ID (COUSR02C - Lookup)
**Endpoint:** `GET /api/users/{secUsrId}`

**Description:** Retrieve a user by their ID for update screen display or deletion confirmation.

**Parameters:**
- `secUsrId` (path, required): User ID (8 characters max)

**Response:** `200 OK`
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrType": "U",
  "fullName": "John Doe",
  "userTypeDisplayName": "User",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

**Error Responses:**
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

---

### 3. Create New User (COUSR01C)
**Endpoint:** `POST /api/users`

**Description:** Create a new user with validation of all mandatory fields.

**Request Body:**
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "pass123",
  "secUsrType": "U"
}
```

**Validation Rules:**
- `secUsrId`: Required, max 8 characters, alphanumeric, unique
- `secUsrFname`: Required, max 20 characters
- `secUsrLname`: Required, max 20 characters
- `secUsrPwd`: Required, max 8 characters
- `secUsrType`: Required, must be 'A' (Admin) or 'U' (User)

**Response:** `201 Created`
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrType": "U",
  "fullName": "John Doe",
  "userTypeDisplayName": "User",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

**Error Responses:**
- `400 Bad Request`: Invalid request data or duplicate User ID
- `500 Internal Server Error`: System error

---

### 4. Update User (COUSR02C)
**Endpoint:** `PUT /api/users/{secUsrId}`

**Description:** Update user details by ID with validation of changes.

**Parameters:**
- `secUsrId` (path, required): User ID (cannot be changed)

**Request Body:**
```json
{
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "newpass",
  "secUsrType": "A"
}
```

**Validation Rules:**
- All fields are optional for updates
- `secUsrFname`: Max 20 characters if provided
- `secUsrLname`: Max 20 characters if provided
- `secUsrPwd`: Max 8 characters if provided
- `secUsrType`: Must be 'A' or 'U' if provided
- At least one field must be different from current values

**Response:** `200 OK`
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrType": "A",
  "fullName": "John Doe",
  "userTypeDisplayName": "Admin",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T14:30:00"
}
```

**Error Responses:**
- `400 Bad Request`: Invalid request data or no changes detected
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

---

### 5. Delete User (COUSR03C)
**Endpoint:** `DELETE /api/users/{secUsrId}`

**Description:** Delete a user by ID after confirmation.

**Parameters:**
- `secUsrId` (path, required): User ID to delete

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

---

## Data Models

### UserListResponse
```json
{
  "secUsrId": "string (max 8 chars)",
  "secUsrFname": "string (max 20 chars)",
  "secUsrLname": "string (max 20 chars)",
  "fullName": "string (computed)",
  "secUsrType": "string (1 char: A|U)",
  "userTypeDisplayName": "string (computed)",
  "createdAt": "datetime"
}
```

### UserResponse
```json
{
  "secUsrId": "string (max 8 chars)",
  "secUsrFname": "string (max 20 chars)",
  "secUsrLname": "string (max 20 chars)",
  "secUsrType": "string (1 char: A|U)",
  "fullName": "string (computed)",
  "userTypeDisplayName": "string (computed)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### CreateUserRequest
```json
{
  "secUsrId": "string (required, max 8 chars, alphanumeric)",
  "secUsrFname": "string (required, max 20 chars)",
  "secUsrLname": "string (required, max 20 chars)",
  "secUsrPwd": "string (required, max 8 chars)",
  "secUsrType": "string (required, A|U)"
}
```

### UpdateUserRequest
```json
{
  "secUsrFname": "string (optional, max 20 chars)",
  "secUsrLname": "string (optional, max 20 chars)",
  "secUsrPwd": "string (optional, max 8 chars)",
  "secUsrType": "string (optional, A|U)"
}
```

## Business Rules Implementation

### COUSR00C - User List Management
- Maximum 10 users per page
- Support for filtering by starting User ID
- Pagination with forward/backward navigation
- Ordered by User ID ascending

### COUSR01C - Add New User
- All fields mandatory validation
- Duplicate User ID prevention
- User Type validation (A or U only)
- Success confirmation with user details

### COUSR02C - Update User
- User lookup by ID
- Field-level validation
- Change detection (no update if no changes)
- User ID cannot be modified (primary key)

### COUSR03C - Delete User
- User existence validation
- Confirmation before deletion
- Proper cleanup of user record

## Error Handling

All endpoints follow consistent error response format:
```json
{
  "timestamp": "2023-10-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User ID is mandatory and cannot be empty",
  "path": "/api/users"
}
```

## Authentication & Authorization

The current implementation does not include authentication/authorization mechanisms. In a production environment, consider implementing:
- JWT token-based authentication
- Role-based access control (Admin vs User permissions)
- API rate limiting
- Input sanitization and validation

## Testing

Use the following sample data for testing:
- Admin User: `ADMIN001` (System Administrator)
- Regular Users: `USER0001`, `USER0002`, `USER0003`
- Test various scenarios: creation, updates, deletions, pagination