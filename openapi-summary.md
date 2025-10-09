# User Security Administration API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of all REST API endpoints for the User Security Administration system, modernized from the COBOL CardDemo application. The API follows OpenAPI 3.0 specification and implements the business logic from the original COBOL programs.

## Base Information
- **Base URL**: `/api`
- **API Version**: 1.0.0
- **Content Type**: `application/json`
- **Authentication**: Basic authentication (based on user credentials)

## API Endpoints Summary

### 1. Authentication Endpoints

#### POST /api/auth/signin
**Summary**: User authentication  
**Description**: Authenticate user with credentials (equivalent to COSGN00C.cbl signon screen)  
**Request Body**: `SignonDTO`
```json
{
  "userId": "USR001",
  "password": "pass123"
}
```
**Responses**:
- `200 OK`: Authentication successful
- `401 Unauthorized`: Invalid credentials
- `400 Bad Request`: Invalid request data

### 2. User Management Endpoints

#### GET /api/users
**Summary**: Get all users with pagination  
**Description**: Retrieve a paginated list of all users (equivalent to COUSR00C.cbl user listing)  
**Parameters**:
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Number of items per page, default: 20

**Response**: `UserListDTO`
```json
{
  "users": [...],
  "currentPage": 0,
  "totalPages": 5,
  "totalElements": 100,
  "pageSize": 20,
  "isFirst": true,
  "isLast": false,
  "hasNext": true,
  "hasPrevious": false,
  "firstUserId": "USR001",
  "lastUserId": "USR020"
}
```

#### GET /api/users/{id}
**Summary**: Get user by ID  
**Description**: Retrieve a user by their ID  
**Parameters**:
- `id` (path, required): User ID

**Response**: `UserDTO`
**Status Codes**:
- `200 OK`: User found
- `404 Not Found`: User not found

#### POST /api/users
**Summary**: Create a new user  
**Description**: Add a new user to the system (equivalent to COUSR01C.cbl add user)  
**Request Body**: `CreateUserDTO`
```json
{
  "userId": "USR001",
  "firstName": "John",
  "lastName": "Doe",
  "password": "pass123",
  "userType": "A"
}
```
**Status Codes**:
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data
- `409 Conflict`: User already exists

#### PUT /api/users/{id}
**Summary**: Update an existing user  
**Description**: Update user details by ID (equivalent to COUSR02C.cbl update user)  
**Parameters**:
- `id` (path, required): User ID

**Request Body**: `UpdateUserDTO`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "password": "newpass",
  "userType": "A"
}
```
**Status Codes**:
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found

#### DELETE /api/users/{id}
**Summary**: Delete a user  
**Description**: Delete a user by ID with confirmation (equivalent to COUSR03C.cbl delete user)  
**Parameters**:
- `id` (path, required): User ID
- `confirm` (query, optional): Confirmation flag, default: false

**Status Codes**:
- `200 OK`: Confirmation details or deletion successful
- `400 Bad Request`: Confirmation required
- `404 Not Found`: User not found

### 3. User Selection Endpoints

#### GET /api/users/selection
**Summary**: Get all users for selection  
**Description**: Retrieve list of all users for selection purposes  
**Response**: Array of `UserSelectionDTO`

#### GET /api/users/selection/admin
**Summary**: Get admin users for selection  
**Description**: Retrieve list of admin users for selection purposes  
**Response**: Array of `UserSelectionDTO`

#### GET /api/users/selection/regular
**Summary**: Get regular users for selection  
**Description**: Retrieve list of regular users for selection purposes  
**Response**: Array of `UserSelectionDTO`

### 4. COBOL-Style Pagination Endpoints

#### GET /api/users/next/{lastUserId}
**Summary**: Get next page of users  
**Description**: Forward pagination - get users after the specified user ID (COBOL-style browsing)  
**Parameters**:
- `lastUserId` (path, required): Last user ID from current page
- `size` (query, optional): Number of items per page, default: 20

**Response**: `UserListDTO`

#### GET /api/users/previous/{firstUserId}
**Summary**: Get previous page of users  
**Description**: Backward pagination - get users before the specified user ID (COBOL-style browsing)  
**Parameters**:
- `firstUserId` (path, required): First user ID from current page
- `size` (query, optional): Number of items per page, default: 20

**Response**: `UserListDTO`

### 5. Statistics and Reporting Endpoints

#### GET /api/users/stats
**Summary**: Get user statistics  
**Description**: Retrieve comprehensive user statistics and metrics  
**Response**:
```json
{
  "totalUsers": 100,
  "adminUsers": 10,
  "regularUsers": 90,
  "timestamp": "2023-10-01T12:00:00"
}
```

### 6. Menu Navigation Endpoints

#### GET /api/users/menu
**Summary**: Get menu navigation data  
**Description**: Retrieve menu navigation support data for user interface (equivalent to COMEN01C.cbl and COADM01C.cbl menu systems)  
**Parameters**:
- `currentUserId` (query, optional): Current user ID for context

**Response**:
```json
{
  "mainMenuOptions": [...],
  "currentUser": {...},
  "userType": "A",
  "hasAdminAccess": true,
  "adminMenuOptions": [...],
  "timestamp": "2023-10-01T12:00:00"
}
```

### 7. Access Control Endpoints

#### GET /api/users/{id}/access
**Summary**: Check user access  
**Description**: Check if user has required access level  
**Parameters**:
- `id` (path, required): User ID
- `requiredType` (query, optional): Required access type (A=Admin, R=Regular)

**Response**:
```json
{
  "userId": "USR001",
  "userType": "A",
  "hasAdminAccess": true,
  "hasRegularAccess": false,
  "hasRequiredAccess": true,
  "requiredType": "A"
}
```

## Data Transfer Objects (DTOs)

### UserDTO
Main user data transfer object containing all user information except password.

### CreateUserDTO
Request DTO for creating new users with validation constraints.

### UpdateUserDTO
Request DTO for updating existing users with optional fields.

### SignonDTO
Authentication request DTO with user credentials.

### UserListDTO
Paginated response DTO containing list of users and pagination metadata.

### UserSelectionDTO
Simplified user DTO for selection purposes in UI components.

## Error Handling

All endpoints follow standard HTTP status codes:
- `200 OK`: Successful operation
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data or parameters
- `401 Unauthorized`: Authentication failed
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists
- `500 Internal Server Error`: Server error

Error responses include descriptive messages to help with troubleshooting.

## Business Rules Implementation

The API implements all business rules from the original COBOL programs:

1. **User Authentication** (COSGN00C): Validates user credentials and provides authentication status
2. **User Listing** (COUSR00C): Supports pagination with forward/backward navigation like COBOL file browsing
3. **Add User** (COUSR01C): Validates all required fields and prevents duplicate user IDs
4. **Update User** (COUSR02C): Detects field-level changes and updates only modified fields
5. **Delete User** (COUSR03C): Requires confirmation before deletion
6. **Menu Navigation** (COMEN01C, COADM01C): Provides menu structure for different user types

## Security Considerations

- Passwords are handled securely (though encryption should be added in production)
- User type validation ensures only valid types (A=Admin, R=Regular) are accepted
- Access control checks prevent unauthorized operations
- Input validation prevents malformed data

## Integration Notes

This API can be easily integrated with:
- Frontend applications (React, Angular, Vue.js)
- Mobile applications
- Other microservices
- Legacy systems through REST calls

The API maintains the same business logic and data structures as the original COBOL programs while providing modern REST endpoints for easy integration.