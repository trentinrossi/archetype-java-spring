# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of all REST endpoints created for the User Security Management system, based on the COSGN00C.cbl and COUSR01C.cbl business rules. The API follows OpenAPI 3.0 specification and provides complete user authentication and management functionality.

## Base Information
- **API Title**: User Security Management API
- **Version**: 1.0.0
- **Base URL**: `/api`
- **Content Type**: `application/json`

## Authentication Endpoints

### POST /api/auth/signin
**Summary**: User authentication  
**Description**: Authenticate user with credentials based on COSGN00C.cbl business rules  
**Operation ID**: signin  
**Tags**: User Security Management  

**Request Body**:
```json
{
  "userId": "USER001",
  "password": "password"
}
```

**Responses**:
- **200 OK**: Authentication successful
  ```json
  {
    "success": true,
    "userId": "USER001",
    "userType": "A",
    "userTypeDisplayName": "Admin",
    "fullName": "John Doe",
    "redirectUrl": "/admin/dashboard",
    "errorMessage": null,
    "errorCode": null
  }
  ```
- **401 Unauthorized**: Authentication failed
- **400 Bad Request**: Invalid request data
- **500 Internal Server Error**: System error

## User Management Endpoints

### POST /api/users/register
**Summary**: User registration  
**Description**: Register a new user based on COUSR01C.cbl business rules  
**Operation ID**: registerUser  
**Tags**: User Security Management  

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "userId": "USER001",
  "password": "password",
  "userType": "A"
}
```

**Responses**:
- **201 Created**: User registered successfully
  ```json
  {
    "success": true,
    "userId": "USER001",
    "fullName": "John Doe",
    "userType": "A",
    "userTypeDisplayName": "Admin",
    "successMessage": "User registered successfully",
    "errorMessage": null,
    "errorCode": null,
    "registeredAt": "2023-10-01T12:00:00"
  }
  ```
- **409 Conflict**: User already exists
- **400 Bad Request**: Invalid request data
- **500 Internal Server Error**: System error

### GET /api/users/{userId}
**Summary**: Get user by ID  
**Description**: Retrieve a user by their ID  
**Operation ID**: getUserById  
**Tags**: User Security Management  

**Parameters**:
- `userId` (path, required): User ID to retrieve

**Responses**:
- **200 OK**: User found
  ```json
  {
    "userId": "USER001",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "userType": "A",
    "userTypeDisplayName": "Admin",
    "isAdmin": true,
    "isRegular": false,
    "createdAt": "2023-10-01T12:00:00",
    "updatedAt": "2023-10-01T12:00:00"
  }
  ```
- **404 Not Found**: User not found
- **500 Internal Server Error**: System error

### GET /api/users
**Summary**: Get all users  
**Description**: Retrieve a paginated list of all users  
**Operation ID**: getAllUsers  
**Tags**: User Security Management  

**Parameters**:
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)
- `sort` (query, optional): Sort criteria

**Responses**:
- **200 OK**: Users retrieved successfully
  ```json
  {
    "content": [
      {
        "userId": "USER001",
        "firstName": "John",
        "lastName": "Doe",
        "fullName": "John Doe",
        "userType": "A",
        "userTypeDisplayName": "Admin",
        "isAdmin": true,
        "isRegular": false,
        "createdAt": "2023-10-01T12:00:00",
        "updatedAt": "2023-10-01T12:00:00"
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
- **400 Bad Request**: Invalid request parameters
- **500 Internal Server Error**: System error

### GET /api/users/type/{userType}
**Summary**: Get users by type  
**Description**: Retrieve users by user type with pagination  
**Operation ID**: getUsersByType  
**Tags**: User Security Management  

**Parameters**:
- `userType` (path, required): User type (A=Admin, R=Regular)
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20)
- `sort` (query, optional): Sort criteria

**Responses**:
- **200 OK**: Users retrieved successfully
- **400 Bad Request**: Invalid user type or request parameters
- **500 Internal Server Error**: System error

### PUT /api/users/{userId}
**Summary**: Update user  
**Description**: Update user information  
**Operation ID**: updateUser  
**Tags**: User Security Management  

**Parameters**:
- `userId` (path, required): User ID to update

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "userId": "USER001",
  "password": "newpassword",
  "userType": "A"
}
```

**Responses**:
- **200 OK**: User updated successfully
- **404 Not Found**: User not found
- **400 Bad Request**: Invalid request data
- **500 Internal Server Error**: System error

### DELETE /api/users/{userId}
**Summary**: Delete user  
**Description**: Delete a user by ID  
**Operation ID**: deleteUser  
**Tags**: User Security Management  

**Parameters**:
- `userId` (path, required): User ID to delete

**Responses**:
- **204 No Content**: User deleted successfully
- **404 Not Found**: User not found
- **500 Internal Server Error**: System error

### GET /api/users/exists/{userId}
**Summary**: Check if user exists  
**Description**: Check if a user exists by ID  
**Operation ID**: userExists  
**Tags**: User Security Management  

**Parameters**:
- `userId` (path, required): User ID to check

**Responses**:
- **200 OK**: User existence check completed
  ```json
  true
  ```
- **500 Internal Server Error**: System error

### GET /api/users/count/{userType}
**Summary**: Count users by type  
**Description**: Get count of users by user type  
**Operation ID**: countUsersByType  
**Tags**: User Security Management  

**Parameters**:
- `userType` (path, required): User type (A=Admin, R=Regular)

**Responses**:
- **200 OK**: User count retrieved successfully
  ```json
  5
  ```
- **400 Bad Request**: Invalid user type
- **500 Internal Server Error**: System error

## Data Models

### SignonRequest
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

### SignonResponse
```json
{
  "success": "boolean (required)",
  "userId": "string (optional)",
  "userType": "string (optional)",
  "userTypeDisplayName": "string (optional)",
  "fullName": "string (optional)",
  "redirectUrl": "string (optional)",
  "errorMessage": "string (optional)",
  "errorCode": "string (optional)"
}
```

### UserRegistrationRequest
```json
{
  "firstName": "string (max 100 chars, required)",
  "lastName": "string (max 100 chars, required)",
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (A or R, required)"
}
```

### UserRegistrationResponse
```json
{
  "success": "boolean (required)",
  "userId": "string (optional)",
  "fullName": "string (optional)",
  "userType": "string (optional)",
  "userTypeDisplayName": "string (optional)",
  "successMessage": "string (optional)",
  "errorMessage": "string (optional)",
  "errorCode": "string (optional)",
  "registeredAt": "datetime (optional)"
}
```

### UserSecurityDTO
```json
{
  "userId": "string (required)",
  "firstName": "string (required)",
  "lastName": "string (required)",
  "fullName": "string (required)",
  "userType": "string (required)",
  "userTypeDisplayName": "string (required)",
  "isAdmin": "boolean (required)",
  "isRegular": "boolean (required)",
  "createdAt": "datetime (required)",
  "updatedAt": "datetime (required)"
}
```

## Business Rules Implementation

### Authentication Flow (COSGN00C.cbl)
1. **Input Validation**: User ID and Password are required and validated for empty values
2. **Case-Insensitive Lookup**: User ID is converted to uppercase for consistent lookups
3. **Password Validation**: Exact password match is required
4. **User Type Validation**: Only 'A' (Admin) and 'R' (Regular) user types are valid
5. **Redirect Logic**: Admin users are redirected to `/admin/dashboard`, Regular users to `/user/dashboard`
6. **Error Handling**: Specific error codes and messages for different failure scenarios

### User Registration Flow (COUSR01C.cbl)
1. **Input Validation**: All fields (First Name, Last Name, User ID, Password, User Type) are required
2. **Duplicate Checking**: User ID uniqueness is enforced
3. **User Type Validation**: Only 'A' (Admin) and 'R' (Regular) are allowed
4. **Case Normalization**: User ID and User Type are converted to uppercase
5. **Error Handling**: Specific error messages for validation failures and duplicate users

## Security Considerations
- User IDs are case-insensitive but stored in uppercase
- Passwords are stored as plain text (as per original COBOL implementation)
- User types are restricted to 'A' (Admin) and 'R' (Regular)
- All endpoints include proper error handling and logging
- Input validation is enforced at both DTO and service levels

## Database Schema
The API uses a `user_security` table with the following structure:
- `user_id` (VARCHAR(8), PRIMARY KEY)
- `first_name` (VARCHAR(100), NOT NULL)
- `last_name` (VARCHAR(100), NOT NULL)
- `password` (VARCHAR(8), NOT NULL)
- `user_type` (VARCHAR(1), NOT NULL, CHECK IN ('A', 'R'))
- `created_at` (TIMESTAMP, NOT NULL)
- `updated_at` (TIMESTAMP, NOT NULL)

## Error Codes
- `EMPTY_USER_ID`: User ID is required
- `EMPTY_PASSWORD`: Password is required
- `USER_NOT_FOUND`: User does not exist
- `WRONG_PASSWORD`: Invalid password
- `INVALID_USER_TYPE`: Invalid user type
- `USER_EXISTS`: User ID already exists
- `EMPTY_FIRST_NAME`: First name is required
- `EMPTY_LAST_NAME`: Last name is required
- `EMPTY_USER_TYPE`: User type is required
- `SYSTEM_ERROR`: Internal system error