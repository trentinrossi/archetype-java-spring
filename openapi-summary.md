# User Management System - API Documentation

## Overview
This document provides a comprehensive summary of the REST API endpoints for the User Management System, implemented based on the COBOL business rules from COUSR00C, COUSR01C, COUSR02C, and COUSR03C programs.

## Base URL
```
/api/users
```

## Endpoints Summary

### 1. GET /api/users
**Description:** Retrieve a paginated list of all users with optional filtering

**Business Rule Reference:** COUSR00C - User List Management

**Parameters:**
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 10, max: 10)
- `secUsrIdStartsWith` (query, optional): Filter users by User ID prefix

**Response:**
- **200 OK**: Paginated list of users
- **400 Bad Request**: Invalid request parameters
- **500 Internal Server Error**: Server error

**Response Schema:**
```json
{
  "content": [
    {
      "secUsrId": "USR00001",
      "secUsrFname": "John",
      "secUsrLname": "Doe",
      "secUsrType": "A"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### 2. GET /api/users/{id}
**Description:** Retrieve a specific user by their ID

**Business Rule Reference:** COUSR02C/COUSR03C - User Lookup functionality

**Parameters:**
- `id` (path, required): User ID (8 characters max)

**Response:**
- **200 OK**: User details found
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

**Response Schema:**
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "fullName": "John Doe",
  "secUsrType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 3. POST /api/users
**Description:** Create a new user

**Business Rule Reference:** COUSR01C - Add New User

**Request Body:**
```json
{
  "secUsrId": "USR00001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "password",
  "secUsrType": "A"
}
```

**Validation Rules:**
- All fields are mandatory (cannot be null or empty)
- `secUsrId`: Max 8 characters
- `secUsrFname`: Max 20 characters
- `secUsrLname`: Max 20 characters
- `secUsrPwd`: Max 8 characters
- `secUsrType`: 1 character ('A' for Admin, 'U' for User)

**Response:**
- **201 Created**: User created successfully
- **400 Bad Request**: Invalid data or duplicate User ID
- **500 Internal Server Error**: Server error

### 4. PUT /api/users/{id}
**Description:** Update an existing user

**Business Rule Reference:** COUSR02C - User Update Program

**Parameters:**
- `id` (path, required): User ID (cannot be modified)

**Request Body:**
```json
{
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "newpass",
  "secUsrType": "U"
}
```

**Validation Rules:**
- All fields are mandatory (cannot be null or empty)
- `secUsrFname`: Max 20 characters
- `secUsrLname`: Max 20 characters
- `secUsrPwd`: Max 8 characters
- `secUsrType`: 1 character ('A' for Admin, 'U' for User)
- User ID cannot be modified (not included in request body)

**Response:**
- **200 OK**: User updated successfully
- **400 Bad Request**: Invalid data or no changes detected
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

### 5. DELETE /api/users/{id}
**Description:** Delete a user by ID

**Business Rule Reference:** COUSR03C - User Deletion

**Parameters:**
- `id` (path, required): User ID to delete

**Response:**
- **204 No Content**: User deleted successfully
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server error

## Data Models

### UserListDTO
Used for paginated user listings
```json
{
  "secUsrId": "string (max 8 chars)",
  "secUsrFname": "string (max 20 chars)",
  "secUsrLname": "string (max 20 chars)",
  "secUsrType": "string (1 char: A or U)"
}
```

### UserCreateDTO
Used for creating new users
```json
{
  "secUsrId": "string (required, max 8 chars)",
  "secUsrFname": "string (required, max 20 chars)",
  "secUsrLname": "string (required, max 20 chars)",
  "secUsrPwd": "string (required, max 8 chars)",
  "secUsrType": "string (required, 1 char: A or U)"
}
```

### UserUpdateDTO
Used for updating existing users
```json
{
  "secUsrFname": "string (required, max 20 chars)",
  "secUsrLname": "string (required, max 20 chars)",
  "secUsrPwd": "string (required, max 8 chars)",
  "secUsrType": "string (required, 1 char: A or U)"
}
```

### UserResponseDTO
Used for API responses (excludes password for security)
```json
{
  "secUsrId": "string",
  "secUsrFname": "string",
  "secUsrLname": "string",
  "fullName": "string",
  "secUsrType": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

## Business Rules Implementation

### Validation Rules
1. **Mandatory Fields**: All user fields (except search filters) are mandatory and cannot be empty
2. **User Type Validation**: Must be 'A' (Admin) or 'U' (User)
3. **Duplicate Prevention**: User ID must be unique across the system
4. **Field Length Limits**: Enforced as per original COBOL specifications

### Pagination Rules
1. **Page Size**: Fixed at 10 users per page (matching original COBOL behavior)
2. **Sorting**: Users are sorted by User ID in ascending order
3. **Filtering**: Support for filtering by User ID prefix (starts with)

### Error Handling
1. **User Not Found**: Returns 404 for non-existent users
2. **Duplicate User ID**: Returns 400 when attempting to create duplicate users
3. **Validation Errors**: Returns 400 with detailed validation messages
4. **Server Errors**: Returns 500 for unexpected system errors

### Security Considerations
1. **Password Exclusion**: Passwords are never returned in API responses
2. **Input Validation**: All inputs are validated and sanitized
3. **Case Handling**: User IDs and User Types are converted to uppercase for consistency

## Database Schema
The API uses the `usrsec` table with the following structure:
- `sec_usr_id` VARCHAR(8) PRIMARY KEY
- `sec_usr_fname` VARCHAR(20) NOT NULL
- `sec_usr_lname` VARCHAR(20) NOT NULL
- `sec_usr_pwd` VARCHAR(8) NOT NULL
- `sec_usr_type` VARCHAR(1) NOT NULL
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

## Example Usage

### Create a new admin user
```bash
curl -X POST /api/users \
  -H "Content-Type: application/json" \
  -d '{
    "secUsrId": "ADMIN001",
    "secUsrFname": "System",
    "secUsrLname": "Administrator",
    "secUsrPwd": "admin123",
    "secUsrType": "A"
  }'
```

### Get paginated user list
```bash
curl -X GET "/api/users?page=0&size=10&secUsrIdStartsWith=USR"
```

### Update user details
```bash
curl -X PUT /api/users/USR00001 \
  -H "Content-Type: application/json" \
  -d '{
    "secUsrFname": "Updated",
    "secUsrLname": "Name",
    "secUsrPwd": "newpass",
    "secUsrType": "U"
  }'
```

### Delete a user
```bash
curl -X DELETE /api/users/USR00001
```