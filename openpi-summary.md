# OpenAPI Summary - User Security Management API

## Overview
This API provides comprehensive user security management functionality for the CardDemo application, implementing the COSGN00C.cbl business logic in a modern REST API format.

## Base Information
- **API Title**: User Security Management API
- **Version**: 1.0.0
- **Base URL**: `/api`
- **Tags**: User Security Management

## Authentication Endpoints

### POST /api/auth/signon
**Summary**: User Sign-on  
**Description**: Authenticate user with COSGN00C business logic - ENTER key for authentication, PF3 key for exit  
**Request Body**: `SignonRequestDTO`
```json
{
  "userId": "ADMIN001",
  "password": "ADMIN123"
}
```
**Responses**:
- `200`: Authentication successful
- `401`: Authentication failed - Wrong password or user not found
- `400`: Invalid request data or invalid key pressed
- `403`: User account is inactive
- `500`: Internal server error

### POST /api/auth/exit
**Summary**: Handle PF3 Exit  
**Description**: Handle PF3 key press for graceful exit with thank you message  
**Responses**:
- `200`: Exit successful with thank you message
- `500`: Internal server error

### POST /api/auth/invalid-key
**Summary**: Handle Invalid Key  
**Description**: Handle invalid key press during authentication  
**Responses**:
- `400`: Invalid key pressed
- `500`: Internal server error

### POST /api/auth/validate
**Summary**: Validate authentication  
**Description**: Validate if user credentials are correct without full authentication  
**Request Body**: `SignonRequestDTO`
**Responses**:
- `200`: Validation successful
- `401`: Invalid credentials
- `400`: Invalid request data
- `500`: Internal server error

## User Management Endpoints

### GET /api/users
**Summary**: Get all users  
**Description**: Retrieve a paginated list of all user security records  
**Parameters**:
- `page` (query, integer): Page number (default: 0)
- `size` (query, integer): Page size (default: 20)
- `sort` (query, string): Sort criteria

**Responses**:
- `200`: Successful retrieval of users
- `400`: Invalid request parameters
- `500`: Internal server error

### GET /api/users/{userId}
**Summary**: Get user by ID  
**Description**: Retrieve a user security record by user ID  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Responses**:
- `200`: Successful retrieval of user
- `404`: User not found
- `500`: Internal server error

### POST /api/users
**Summary**: Create a new user  
**Description**: Create a new user security record  
**Request Body**: `CreateUserSecurityRequest`
```json
{
  "userId": "NEWUSER1",
  "password": "PASS123",
  "userType": "GENERAL",
  "programName": "COSGN00C",
  "transactionId": "CC00",
  "active": true
}
```
**Responses**:
- `201`: User created successfully
- `400`: Invalid request data
- `409`: User already exists
- `500`: Internal server error

### PUT /api/users/{userId}
**Summary**: Update an existing user  
**Description**: Update user security details by user ID  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Request Body**: `UpdateUserSecurityRequest`
```json
{
  "password": "NEWPASS",
  "userType": "ADMIN",
  "programName": "COSGN00C",
  "transactionId": "CC00",
  "active": true
}
```
**Responses**:
- `200`: User updated successfully
- `400`: Invalid request data
- `404`: User not found
- `500`: Internal server error

### DELETE /api/users/{userId}
**Summary**: Delete a user  
**Description**: Delete a user security record by user ID  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Responses**:
- `204`: User deleted successfully
- `404`: User not found
- `500`: Internal server error

### PATCH /api/users/{userId}/activate
**Summary**: Activate user  
**Description**: Activate a user account  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Responses**:
- `200`: User activated successfully
- `404`: User not found
- `500`: Internal server error

### PATCH /api/users/{userId}/deactivate
**Summary**: Deactivate user  
**Description**: Deactivate a user account  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Responses**:
- `200`: User deactivated successfully
- `404`: User not found
- `500`: Internal server error

### PATCH /api/users/{userId}/change-password
**Summary**: Change user password  
**Description**: Change password for a user  
**Parameters**:
- `userId` (path, string, required): User ID (max 8 characters)

**Request Body**: `ChangePasswordRequest`
```json
{
  "newPassword": "NEWPASS1"
}
```
**Responses**:
- `200`: Password changed successfully
- `400`: Invalid request data
- `404`: User not found
- `500`: Internal server error

## Data Models

### SignonRequestDTO
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

### SignonResponseDTO
```json
{
  "success": "boolean",
  "message": "string",
  "userType": "ADMIN | GENERAL",
  "redirectProgram": "string"
}
```

### UserSecurityDTO
```json
{
  "userId": "string",
  "password": "string",
  "userType": "ADMIN | GENERAL",
  "programName": "string",
  "transactionId": "string",
  "active": "boolean",
  "createdAt": "timestamp",
  "updatedAt": "timestamp",
  "userTypeDisplayName": "string",
  "redirectProgram": "string",
  "canAuthenticate": "boolean"
}
```

### CreateUserSecurityRequest
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "ADMIN | GENERAL (required)",
  "programName": "string (optional, default: COSGN00C)",
  "transactionId": "string (optional, default: CC00)",
  "active": "boolean (optional, default: true)"
}
```

### UpdateUserSecurityRequest
```json
{
  "password": "string (max 8 chars, optional)",
  "userType": "ADMIN | GENERAL (optional)",
  "programName": "string (optional)",
  "transactionId": "string (optional)",
  "active": "boolean (optional)"
}
```

### ChangePasswordRequest
```json
{
  "newPassword": "string (max 8 chars, required)"
}
```

### ValidationResponseDTO
```json
{
  "valid": "boolean",
  "message": "string"
}
```

## Business Rules Implementation

### COSGN00C Business Logic
1. **Initialization and Setup**: Implemented in entity defaults and service initialization
2. **Signon Screen Display**: Handled by authentication endpoints
3. **User Input Handling**: 
   - ENTER key: `/api/auth/signon`
   - PF3 key: `/api/auth/exit`
   - Invalid key: `/api/auth/invalid-key`
4. **Credential Validation**: Implemented in service layer with proper error messages
5. **User Authentication**: Full authentication with USRSEC file equivalent (database)
6. **Error Handling**: Comprehensive error responses matching COBOL program messages

### User Types and Redirection
- **ADMIN**: Redirected to `COADM01C` program
- **GENERAL**: Redirected to `COMEN01C` program

### Security Features
- Case-insensitive user ID lookup
- Uppercase conversion for consistency
- Active/inactive user status
- Password validation
- Comprehensive error messages

## Error Messages
- "Please enter User ID"
- "Please enter Password"
- "Wrong Password"
- "User not found"
- "Invalid key pressed"
- "User account is inactive"
- Field length validation messages

## Sample Usage

### Authentication Flow
1. POST `/api/auth/signon` with credentials
2. Receive response with success/failure and redirect program
3. Handle PF3 exit with POST `/api/auth/exit`
4. Handle invalid keys with POST `/api/auth/invalid-key`

### User Management Flow
1. GET `/api/users` to list users
2. POST `/api/users` to create new users
3. PUT `/api/users/{userId}` to update users
4. PATCH `/api/users/{userId}/activate` or `/deactivate` for status changes
5. PATCH `/api/users/{userId}/change-password` for password updates
6. DELETE `/api/users/{userId}` to remove users