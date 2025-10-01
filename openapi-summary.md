# User Management System API Documentation

## Overview
This document provides a comprehensive overview of the User Management System REST API endpoints. The API implements the functionality of the legacy CICS programs COUSR00C (list users), COUSR01C (add user), COUSR02C (update user), and COUSR03C (delete user).

## Base URL
```
/api/users
```

## Endpoints Summary

### 1. Get All Users
**Endpoint:** `GET /api/users`
**Description:** Retrieve a paginated list of all users
**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20, max: 10 per legacy requirement)

**Response:**
- **200 OK**: Successful retrieval of users
- **400 Bad Request**: Invalid request parameters
- **500 Internal Server Error**: Server error

**Response Body:**
```json
{
  "content": [
    {
      "userId": "USR12345",
      "firstName": "John",
      "lastName": "Doe",
      "fullName": "John Doe",
      "userType": "A",
      "createdAt": "2023-10-01T12:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### 2. Get User by ID
**Endpoint:** `GET /api/users/{id}`
**Description:** Retrieve a specific user by their ID
**Parameters:**
- `id` (path, required): User ID (max 8 characters)

**Response:**
- **200 OK**: Successful retrieval of user
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

**Response Body:**
```json
{
  "userId": "USR12345",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 3. Create User
**Endpoint:** `POST /api/users`
**Description:** Create a new user
**Request Body:**
```json
{
  "userId": "USR12345",
  "firstName": "John",
  "lastName": "Doe",
  "password": "pass123",
  "userType": "A"
}
```

**Validation Rules:**
- `userId`: Required, max 8 characters
- `firstName`: Required, max 20 characters
- `lastName`: Required, max 20 characters
- `password`: Required, max 8 characters
- `userType`: Required, exactly 1 character

**Response:**
- **201 Created**: User created successfully
- **400 Bad Request**: Invalid request data or validation errors
- **409 Conflict**: User already exists
- **500 Internal Server Error**: Server error

**Response Body:**
```json
{
  "userId": "USR12345",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 4. Update User
**Endpoint:** `PUT /api/users/{id}`
**Description:** Update an existing user's details
**Parameters:**
- `id` (path, required): User ID (max 8 characters)

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "password": "newpass",
  "userType": "U"
}
```

**Validation Rules:**
- `firstName`: Optional, max 20 characters
- `lastName`: Optional, max 20 characters
- `password`: Optional, max 8 characters
- `userType`: Optional, exactly 1 character

**Response:**
- **200 OK**: User updated successfully
- **400 Bad Request**: Invalid request data or validation errors
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

**Response Body:**
```json
{
  "userId": "USR12345",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "U",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T13:00:00"
}
```

### 5. Delete User
**Endpoint:** `DELETE /api/users/{id}`
**Description:** Delete a user by ID
**Parameters:**
- `id` (path, required): User ID (max 8 characters)

**Response:**
- **204 No Content**: User deleted successfully
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

## Data Models

### UserResponse
```json
{
  "userId": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "fullName": "string (computed)",
  "userType": "string (1 char)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### UserListResponse
```json
{
  "userId": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "fullName": "string (computed)",
  "userType": "string (1 char)",
  "createdAt": "datetime"
}
```

### CreateUserRequest
```json
{
  "userId": "string (required, max 8 chars)",
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, 1 char)"
}
```

### UpdateUserRequest
```json
{
  "firstName": "string (optional, max 20 chars)",
  "lastName": "string (optional, max 20 chars)",
  "password": "string (optional, max 8 chars)",
  "userType": "string (optional, 1 char)"
}
```

## Error Handling

All endpoints return appropriate HTTP status codes and error messages:

### Common Error Responses
- **400 Bad Request**: Invalid input data or validation errors
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource already exists (for create operations)
- **500 Internal Server Error**: Unexpected server error

### Error Response Format
```json
{
  "timestamp": "2023-10-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users"
}
```

## Business Rules Implementation

This API implements the business rules from the legacy CICS programs:

1. **COUSR00C (List Users)**: Implemented via `GET /api/users` with pagination support (10 users per page)
2. **COUSR01C (Add User)**: Implemented via `POST /api/users` with full validation
3. **COUSR02C (Update User)**: Implemented via `PUT /api/users/{id}` with partial updates
4. **COUSR03C (Delete User)**: Implemented via `DELETE /api/users/{id}`

All field validations, length restrictions, and business logic from the original COBOL programs have been preserved and implemented in the Spring Boot application.

## Authentication & Authorization

Note: The current implementation does not include authentication or authorization mechanisms. In a production environment, consider implementing:
- JWT-based authentication
- Role-based access control (RBAC)
- API rate limiting
- Input sanitization and security headers