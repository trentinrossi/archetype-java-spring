# User Management System - OpenAPI Endpoints Summary

## Overview
This document provides a comprehensive summary of all REST API endpoints for the User Management System, implementing the business rules from COUSR00C, COUSR01C, COUSR02C, and COUSR03C programs.

## Base URL
```
/api/users
```

## Endpoints

### 1. Get All Users (COUSR00C - User List Management)
**Endpoint:** `GET /api/users`

**Description:** Retrieve a paginated list of all users with 10 users per page

**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 10, max: 10)
- `sort` (query, optional): Sort criteria (default: id,asc)

**Response:**
- **200 OK**: Paginated list of users
- **400 Bad Request**: Invalid request parameters
- **500 Internal Server Error**: Server error

**Response Schema:**
```json
{
  "content": [
    {
      "id": "USR001",
      "firstName": "John",
      "lastName": "Doe",
      "fullName": "John Doe",
      "userType": "U",
      "userTypeDisplayName": "User",
      "isAdmin": false,
      "isRegularUser": true,
      "createdAt": "2023-10-01T12:00:00",
      "updatedAt": "2023-10-01T12:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

### 2. Search Users (COUSR00C - User Filtering)
**Endpoint:** `GET /api/users/search`

**Description:** Search users with filtering by User ID

**Parameters:**
- `id` (query, optional): User ID to search for (max 8 characters)
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 10, max: 10)

**Response:**
- **200 OK**: Paginated list of filtered users
- **400 Bad Request**: Invalid search parameters
- **500 Internal Server Error**: Server error

### 3. Get User by ID (COUSR02C - User Lookup)
**Endpoint:** `GET /api/users/{id}`

**Description:** Retrieve a specific user by their ID for editing purposes

**Parameters:**
- `id` (path, required): User ID (max 8 characters)

**Response:**
- **200 OK**: User details
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

**Response Schema:**
```json
{
  "id": "USR001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "U",
  "userTypeDisplayName": "User",
  "isAdmin": false,
  "isRegularUser": true,
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 4. Create New User (COUSR01C - Add User)
**Endpoint:** `POST /api/users`

**Description:** Create a new user in the system

**Request Body:**
```json
{
  "id": "USR001",
  "firstName": "John",
  "lastName": "Doe",
  "password": "pass123",
  "userType": "U"
}
```

**Validation Rules:**
- `id`: Required, max 8 characters, must be unique
- `firstName`: Required, max 20 characters
- `lastName`: Required, max 20 characters
- `password`: Required, max 8 characters
- `userType`: Required, must be 'A' (Admin) or 'U' (User)

**Response:**
- **201 Created**: User created successfully
- **400 Bad Request**: Invalid request data or validation errors
- **409 Conflict**: User with the same ID already exists
- **500 Internal Server Error**: Server error

### 5. Update User (COUSR02C - User Update)
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
  "userType": "A"
}
```

**Validation Rules:**
- All fields are optional for updates
- `firstName`: Max 20 characters if provided
- `lastName`: Max 20 characters if provided
- `password`: Max 8 characters if provided
- `userType`: Must be 'A' (Admin) or 'U' (User) if provided
- At least one field must be provided and non-empty

**Response:**
- **200 OK**: User updated successfully
- **400 Bad Request**: Invalid request data or validation errors
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

### 6. Delete User (COUSR03C - User Deletion)
**Endpoint:** `DELETE /api/users/{id}`

**Description:** Delete a user from the system

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
  "id": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "fullName": "string (computed)",
  "userType": "string (A or U)",
  "userTypeDisplayName": "string (Admin or User)",
  "isAdmin": "boolean",
  "isRegularUser": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### CreateUserRequest
```json
{
  "id": "string (required, max 8 chars)",
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or U)"
}
```

### UpdateUserRequest
```json
{
  "firstName": "string (optional, max 20 chars)",
  "lastName": "string (optional, max 20 chars)",
  "password": "string (optional, max 8 chars)",
  "userType": "string (optional, A or U)"
}
```

### UserSearchRequest
```json
{
  "id": "string (optional, max 8 chars)"
}
```

## Business Rules Implementation

### COUSR00C - User List Management
- **Pagination**: 10 users per page maximum
- **Sorting**: Users sorted by ID in ascending order
- **Filtering**: Support for filtering by starting User ID
- **Navigation**: Forward/backward pagination support

### COUSR01C - Add New User
- **Validation**: All fields mandatory and cannot be empty
- **Uniqueness**: User ID must be unique
- **User Types**: Only 'A' (Admin) or 'U' (User) allowed
- **Error Handling**: Specific error messages for validation failures

### COUSR02C - User Update
- **Lookup**: User must exist before update
- **Validation**: At least one field must be provided for update
- **Change Detection**: Only update if changes are detected
- **Field Validation**: All fields follow same rules as creation

### COUSR03C - User Deletion
- **Lookup**: User must exist before deletion
- **Confirmation**: Direct deletion without confirmation in API
- **Cleanup**: Complete removal from system

## Error Handling

All endpoints follow consistent error response format:
- **400 Bad Request**: Validation errors, invalid parameters
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resource (User ID already exists)
- **500 Internal Server Error**: Unexpected server errors

## Security Considerations

- Input validation on all endpoints
- SQL injection prevention through JPA/Hibernate
- Parameter sanitization
- Proper HTTP status codes for different scenarios

## Performance Features

- Database indexes on user_type and names for faster queries
- Pagination to limit result sets
- Efficient queries for user lookup and filtering
- Connection pooling and transaction management