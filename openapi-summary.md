# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Management API endpoints, implementing the modernized version of the COBOL CardDemo application's user management functionality.

## Base Information
- **Base URL**: `/api`
- **API Version**: 1.0.0
- **Content Type**: `application/json`

## Authentication & Authorization
The API implements user authentication through the sign-on endpoint. User types are:
- **A**: Administrator users
- **U**: Regular users

## API Endpoints

### 1. User Authentication

#### POST /api/auth/signin
**Summary**: User sign-on authentication  
**Description**: Authenticate user with credentials (equivalent to COSGN00C program)

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

**Response (200 OK)**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string (A|U)",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "authenticated": "boolean",
  "authenticationTime": "datetime"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data (empty user ID or password)
- `401 Unauthorized`: Invalid credentials (user not found or wrong password)
- `500 Internal Server Error`: System error

**Business Rules**:
- User ID cannot be empty
- Password cannot be empty
- Case-insensitive user ID lookup
- Password must match exactly
- Returns user type and admin status

---

### 2. User Management

#### POST /api/users
**Summary**: Create a new user  
**Description**: Create a new user in the system (equivalent to COUSR01C program)

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (A|U, required)"
}
```

**Response (201 Created)**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data (empty required fields)
- `409 Conflict`: User ID already exists
- `500 Internal Server Error`: System error

**Business Rules**:
- All fields are required and cannot be empty
- User ID must be unique (case-insensitive check)
- User type must be 'A' (Admin) or 'U' (User)
- User ID is converted to uppercase for storage

---

#### GET /api/users/{userId}
**Summary**: Get user by ID  
**Description**: Retrieve a user by their ID (equivalent to COUSR02C search functionality)

**Path Parameters**:
- `userId`: string (required) - The user ID to retrieve

**Response (200 OK)**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid user ID (empty)
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Business Rules**:
- User ID cannot be empty
- Case-insensitive user ID lookup
- Returns complete user profile information

---

#### PUT /api/users/{userId}
**Summary**: Update an existing user  
**Description**: Update user details by ID (equivalent to COUSR02C update functionality)

**Path Parameters**:
- `userId`: string (required) - The user ID to update

**Request Body**:
```json
{
  "firstName": "string (max 20 chars, optional)",
  "lastName": "string (max 20 chars, optional)",
  "password": "string (max 8 chars, optional)",
  "userType": "string (A|U, optional)"
}
```

**Response (200 OK)**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Business Rules**:
- User ID cannot be empty
- Only provided fields are updated (partial updates supported)
- Empty fields are ignored
- User type validation if provided
- Case-insensitive user ID lookup

---

#### DELETE /api/users/{userId}
**Summary**: Delete a user  
**Description**: Delete a user by ID (equivalent to COUSR03C program)

**Path Parameters**:
- `userId`: string (required) - The user ID to delete

**Response (204 No Content)**: User deleted successfully

**Error Responses**:
- `400 Bad Request`: Invalid user ID (empty)
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Business Rules**:
- User ID cannot be empty
- Case-insensitive user ID lookup
- Physical deletion of user record
- No response body on successful deletion

---

#### GET /api/users
**Summary**: Get all users  
**Description**: Retrieve a paginated list of all users

**Query Parameters**:
- `page`: integer (optional, default: 0) - Page number
- `size`: integer (optional, default: 20) - Page size
- `sort`: string (optional) - Sort criteria

**Response (200 OK)**:
```json
{
  "content": [
    {
      "userId": "string",
      "firstName": "string",
      "lastName": "string",
      "fullName": "string",
      "userType": "string",
      "userTypeDisplayName": "string",
      "isAdmin": "boolean",
      "createdAt": "datetime",
      "updatedAt": "datetime"
    }
  ],
  "pageable": {
    "sort": {...},
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 0,
  "totalPages": 0,
  "last": true,
  "first": true,
  "numberOfElements": 0,
  "size": 20,
  "number": 0,
  "sort": {...},
  "empty": true
}
```

**Error Responses**:
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: System error

**Business Rules**:
- Supports pagination with configurable page size
- Default page size is 20
- Returns complete user information for each user
- Supports sorting by various fields

---

## Data Models

### UserSignOnRequest
```json
{
  "userId": "string (max 8, required)",
  "password": "string (max 8, required)"
}
```

### UserCreateRequest
```json
{
  "userId": "string (max 8, required)",
  "firstName": "string (max 20, required)",
  "lastName": "string (max 20, required)",
  "password": "string (max 8, required)",
  "userType": "string (A|U, required)"
}
```

### UserUpdateRequest
```json
{
  "firstName": "string (max 20, optional)",
  "lastName": "string (max 20, optional)",
  "password": "string (max 8, optional)",
  "userType": "string (A|U, optional)"
}
```

### UserResponse
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### UserAuthenticationResponse
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "authenticated": "boolean",
  "authenticationTime": "datetime"
}
```

## Error Handling

All endpoints return standardized error responses:

### 400 Bad Request
```json
{
  "timestamp": "datetime",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error message",
  "path": "/api/endpoint"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "datetime",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed",
  "path": "/api/auth/signin"
}
```

### 404 Not Found
```json
{
  "timestamp": "datetime",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: {userId}",
  "path": "/api/users/{userId}"
}
```

### 409 Conflict
```json
{
  "timestamp": "datetime",
  "status": 409,
  "error": "Conflict",
  "message": "User ID already exists: {userId}",
  "path": "/api/users"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "datetime",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/endpoint"
}
```

## Business Rules Summary

1. **User ID Validation**: All operations require non-empty user IDs
2. **Case Sensitivity**: User ID lookups are case-insensitive
3. **Field Length Limits**: Strict adherence to original COBOL field lengths
4. **User Type Validation**: Only 'A' (Admin) and 'U' (User) are valid
5. **Required Fields**: Create operation requires all fields to be non-empty
6. **Partial Updates**: Update operation supports partial field updates
7. **Duplicate Prevention**: User IDs must be unique across the system
8. **Authentication**: Password matching is case-sensitive and exact
9. **Data Integrity**: All database operations are transactional
10. **Audit Trail**: Created and updated timestamps are automatically managed

## Migration from COBOL Programs

| COBOL Program | Modern Equivalent | HTTP Method | Endpoint |
|---------------|-------------------|-------------|----------|
| COSGN00C | User Authentication | POST | /api/auth/signin |
| COUSR01C | User Creation | POST | /api/users |
| COUSR02C | User Update/Search | GET/PUT | /api/users/{userId} |
| COUSR03C | User Deletion | DELETE | /api/users/{userId} |
| N/A | User Listing | GET | /api/users |

This API provides complete modernization of the CardDemo application's user security management functionality while maintaining business rule compatibility with the original COBOL implementation.