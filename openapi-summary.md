# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Management API endpoints following the OpenAPI 3.0 specification. The API provides functionality for user authentication, creation, updates, deletion, and retrieval based on the modernized COBOL CardDemo application.

## Base Information
- **Base URL**: `/api/users`
- **API Version**: 1.0.0
- **Content Type**: `application/json`
- **Authentication**: Not implemented (based on original COBOL specifications)

## Endpoints Summary

### 1. User Authentication
**POST** `/api/users/signin`

**Description**: Authenticate user with credentials (equivalent to COSGN00C.cbl sign-on functionality)

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

**Response Codes**:
- `200 OK`: Authentication successful
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication failed
- `500 Internal Server Error`: Server error

**Success Response**:
```json
{
  "success": true,
  "message": "Sign on successful",
  "userType": "A",
  "redirectProgram": "ADMINMENU"
}
```

**Error Response**:
```json
{
  "success": false,
  "message": "Invalid password",
  "userType": null,
  "redirectProgram": null
}
```

### 2. Create User
**POST** `/api/users`

**Description**: Create a new user in the system (equivalent to COUSR01C.cbl functionality)

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (1 char, A or R, required)"
}
```

**Response Codes**:
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data
- `409 Conflict`: User already exists
- `500 Internal Server Error`: Server error

**Success Response**:
```json
{
  "userId": "USER001",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 3. Update User
**PUT** `/api/users/{userId}`

**Description**: Update existing user information (equivalent to COUSR02C.cbl functionality)

**Path Parameters**:
- `userId`: string (required) - The user ID to update

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, optional)",
  "lastName": "string (max 20 chars, optional)",
  "password": "string (max 8 chars, optional)",
  "userType": "string (1 char, A or R, optional)"
}
```

**Response Codes**:
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

**Success Response**:
```json
{
  "userId": "USER001",
  "firstName": "John",
  "lastName": "Smith",
  "userType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T14:30:00"
}
```

### 4. Delete User
**DELETE** `/api/users/{userId}`

**Description**: Delete a user from the system (equivalent to COUSR03C.cbl functionality)

**Path Parameters**:
- `userId`: string (required) - The user ID to delete

**Response Codes**:
- `204 No Content`: User deleted successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

### 5. Get User by ID
**GET** `/api/users/{userId}`

**Description**: Retrieve a specific user by their user ID

**Path Parameters**:
- `userId`: string (required) - The user ID to retrieve

**Response Codes**:
- `200 OK`: User found and returned
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

**Success Response**:
```json
{
  "userId": "USER001",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "A",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 6. Get All Users
**GET** `/api/users`

**Description**: Retrieve a paginated list of all users

**Query Parameters**:
- `page`: integer (optional, default: 0) - Page number
- `size`: integer (optional, default: 20) - Page size
- `sort`: string (optional) - Sort criteria

**Response Codes**:
- `200 OK`: Users retrieved successfully
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: Server error

**Success Response**:
```json
{
  "content": [
    {
      "userId": "USER001",
      "firstName": "John",
      "lastName": "Doe",
      "userType": "A",
      "createdAt": "2023-10-01T12:00:00",
      "updatedAt": "2023-10-01T12:00:00"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1
}
```

## Data Models

### UserSignOnRequest
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

### UserSignOnResponse
```json
{
  "success": "boolean (required)",
  "message": "string (required)",
  "userType": "string (optional)",
  "redirectProgram": "string (optional)"
}
```

### UserCreateRequest
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (1 char, A or R, required)"
}
```

### UserUpdateRequest
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, optional)",
  "lastName": "string (max 20 chars, optional)",
  "password": "string (max 8 chars, optional)",
  "userType": "string (1 char, A or R, optional)"
}
```

### UserResponse
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string",
  "createdAt": "string (ISO 8601 datetime)",
  "updatedAt": "string (ISO 8601 datetime)"
}
```

## Error Handling

All endpoints return consistent error responses in the following format:

```json
{
  "error": "string (error type)",
  "message": "string (detailed error message)"
}
```

## Validation Rules

1. **User ID**: Required, maximum 8 characters, must be unique
2. **First Name**: Required for creation, maximum 20 characters
3. **Last Name**: Required for creation, maximum 20 characters
4. **Password**: Required for creation, maximum 8 characters
5. **User Type**: Required for creation, must be 'A' (Admin) or 'R' (Regular)

## Business Logic Notes

- User authentication follows the original COBOL logic from COSGN00C.cbl
- User creation implements the business rules from COUSR01C.cbl
- User updates follow the modification logic from COUSR02C.cbl
- User deletion implements the validation and deletion logic from COUSR03C.cbl
- All operations maintain data integrity and provide appropriate error handling
- The API preserves the original field length constraints from the COBOL specifications

## Security Considerations

- Passwords are stored in plain text (matching original COBOL implementation)
- No authentication/authorization mechanism implemented (as per original specifications)
- Input validation is performed on all endpoints
- Proper error handling prevents information disclosure

This API provides a complete modernization of the COBOL CardDemo User Security Management functionality while maintaining compatibility with the original business rules and data structures.