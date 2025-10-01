# User Management System - OpenAPI Endpoints Summary

## Overview
This document provides a comprehensive summary of all REST API endpoints for the CardDemo User Management System, implementing the business rules from COBOL programs COUSR00C, COUSR01C, COUSR02C, and COUSR03C.

## Base URL
```
/api/users
```

## Endpoints

### 1. Get All Users
**Endpoint:** `GET /api/users`

**Description:** Retrieve a paginated list of all users (equivalent to COUSR00C - User List Management)

**Parameters:**
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Page size, default: 10

**Response:** `UserPageResponse`
- Contains list of users with pagination metadata
- Default page size is 10 users per page (as per business rules)

**Status Codes:**
- `200`: Successful retrieval of users
- `400`: Invalid request parameters
- `500`: Unable to lookup User

---

### 2. Get Users Starting From ID
**Endpoint:** `GET /api/users/from/{startUserId}`

**Description:** Retrieve users starting from a specific User ID with pagination (implements search from User ID functionality from COUSR00C)

**Parameters:**
- `startUserId` (path, required): Starting User ID
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Page size, default: 10

**Response:** `UserPageResponse`

**Status Codes:**
- `200`: Successful retrieval of users
- `400`: Invalid request parameters
- `500`: Unable to lookup User

---

### 3. Get User by ID
**Endpoint:** `GET /api/users/{id}`

**Description:** Retrieve a specific user by their ID (implements user lookup functionality from COUSR02C and COUSR03C)

**Parameters:**
- `id` (path, required): User ID

**Response:** `UserResponse`

**Status Codes:**
- `200`: Successful retrieval of user
- `400`: User ID can NOT be empty
- `404`: User ID NOT found
- `500`: Unable to lookup User

---

### 4. Create User
**Endpoint:** `POST /api/users`

**Description:** Create a new user in the system (equivalent to COUSR01C - Add New User)

**Request Body:** `CreateUserRequest`
```json
{
  "secUsrId": "USR001",
  "secUsrFname": "John",
  "secUsrLname": "Doe", 
  "secUsrPwd": "pass123",
  "secUsrType": "U"
}
```

**Validation Rules:**
- All fields are mandatory (as per business rules)
- `secUsrId`: Max 8 characters
- `secUsrFname`: Max 20 characters
- `secUsrLname`: Max 20 characters
- `secUsrPwd`: Max 8 characters
- `secUsrType`: Exactly 1 character (A=Admin, U=User)

**Response:** `UserResponse`

**Status Codes:**
- `201`: User has been added
- `400`: User ID already exist or validation error
- `500`: Unable to Add User

---

### 5. Update User
**Endpoint:** `PUT /api/users/{id}`

**Description:** Update an existing user's details (equivalent to COUSR02C - User Update Program)

**Parameters:**
- `id` (path, required): User ID

**Request Body:** `UpdateUserRequest`
```json
{
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "newpass",
  "secUsrType": "A"
}
```

**Business Logic:**
- Validates that user exists
- Checks for actual changes before updating
- All fields are mandatory for update

**Response:** `UserResponse`

**Status Codes:**
- `200`: User has been updated
- `400`: User ID NOT found or Please modify to update or validation error
- `500`: Unable to Update User

---

### 6. Delete User
**Endpoint:** `DELETE /api/users/{id}`

**Description:** Delete a user from the system (equivalent to COUSR03C - User Deletion)

**Parameters:**
- `id` (path, required): User ID

**Response:** `UserDeleteResponse`
```json
{
  "secUsrId": "USR001",
  "message": "User USR001 has been deleted ...",
  "deletedAt": "2023-10-01T12:00:00"
}
```

**Status Codes:**
- `200`: User has been deleted
- `400`: User ID can NOT be empty
- `404`: User ID NOT found
- `500`: Unable to lookup User

---

### 7. Search Users
**Endpoint:** `GET /api/users/search`

**Description:** Search users with various filters and pagination (implements filtering functionality from COUSR00C)

**Parameters:**
- `secUsrId` (query, optional): User ID to search
- `secUsrFname` (query, optional): First name to search
- `secUsrLname` (query, optional): Last name to search
- `secUsrType` (query, optional): User type to search
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Page size, default: 10

**Response:** `UserPageResponse`

**Status Codes:**
- `200`: Successful search results
- `400`: Invalid search parameters
- `500`: Unable to lookup User

---

## Data Models

### UserResponse
```json
{
  "secUsrId": "USR001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrType": "U",
  "fullName": "John Doe",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### UserPageResponse
```json
{
  "users": [UserResponse],
  "currentPage": 0,
  "totalPages": 5,
  "totalElements": 50,
  "pageSize": 10,
  "first": true,
  "last": false,
  "hasNext": true,
  "hasPrevious": false
}
```

### CreateUserRequest
```json
{
  "secUsrId": "USR001",
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "pass123",
  "secUsrType": "U"
}
```

### UpdateUserRequest
```json
{
  "secUsrFname": "John",
  "secUsrLname": "Doe",
  "secUsrPwd": "newpass",
  "secUsrType": "A"
}
```

### UserDeleteResponse
```json
{
  "secUsrId": "USR001",
  "message": "User USR001 has been deleted ...",
  "deletedAt": "2023-10-01T12:00:00"
}
```

## Business Rules Implementation

### Pagination
- Default page size: 10 users per page (as specified in COUSR00C)
- Pages are 0-based indexed
- Navigation equivalent to PF7 (previous page) and PF8 (next page) functionality

### Validation Messages
The API implements the exact error messages from the COBOL business rules:
- "User ID can NOT be empty..."
- "First Name can NOT be empty..."
- "Last Name can NOT be empty..."
- "Password can NOT be empty..."
- "User Type can NOT be empty..."
- "User ID already exist"
- "User ID NOT found"
- "Unable to Add User"
- "Unable to Update User"
- "Unable to lookup User"
- "Please modify to update..."

### User Types
- `A`: Administrator
- `U`: Regular User

### Field Constraints
- User ID: 8 characters maximum
- First Name: 20 characters maximum
- Last Name: 20 characters maximum
- Password: 8 characters maximum
- User Type: 1 character exactly

## Error Handling
All endpoints implement proper error handling with appropriate HTTP status codes and business rule compliant error messages, maintaining consistency with the original COBOL program behavior.