# User Security Administration API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Administration API endpoints, following the OpenAPI specification. The API provides functionality for managing users, authentication, and menu navigation based on the modernized COBOL programs from the CardDemo application.

## Base Information
- **Base URL**: `/api`
- **API Version**: 1.0.0
- **Title**: User Security Administration API
- **Description**: APIs for user security administration and authentication

## Endpoints Summary

### 1. User Management

#### POST /api/users
**Summary**: Add new user (COUSR01C)  
**Description**: Create a new user in the system  
**Request Body**: UserCreateDTO  
**Responses**:
- 201: User created successfully (UserResponseDTO)
- 400: Invalid request data or user already exists
- 500: Internal server error

**Request Schema (UserCreateDTO)**:
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (A or R, required)"
}
```

#### PUT /api/users/{userId}
**Summary**: Update user (COUSR02C)  
**Description**: Update an existing user by ID  
**Path Parameters**: userId (string, required)  
**Request Body**: UserUpdateDTO  
**Responses**:
- 200: User updated successfully (UserResponseDTO)
- 400: Invalid request data
- 404: User not found
- 500: Internal server error

**Request Schema (UserUpdateDTO)**:
```json
{
  "firstName": "string (max 20 chars, optional)",
  "lastName": "string (max 20 chars, optional)",
  "password": "string (max 8 chars, optional)",
  "userType": "string (A or R, optional)"
}
```

#### DELETE /api/users/{userId}
**Summary**: Delete user with confirmation (COUSR03C)  
**Description**: Delete a user by ID with confirmation  
**Path Parameters**: userId (string, required)  
**Responses**:
- 204: User deleted successfully
- 404: User not found
- 409: User cannot be deleted due to business rules
- 500: Internal server error

#### GET /api/users
**Summary**: List all users with pagination (COUSR00C)  
**Description**: Retrieve a paginated list of all users with optional filtering  
**Query Parameters**:
- page: Page number (default: 0)
- size: Page size (default: 20)
- userType: Filter by user type (A for Admin, R for Regular, optional)
**Responses**:
- 200: Successful retrieval of users (UserListDTO)
- 400: Invalid request parameters
- 500: Internal server error

#### GET /api/users/{userId}
**Summary**: Get user by ID  
**Description**: Retrieve a specific user by their ID  
**Path Parameters**: userId (string, required)  
**Responses**:
- 200: Successful retrieval of user (UserResponseDTO)
- 404: User not found
- 500: Internal server error

#### GET /api/users/search
**Summary**: Search users by name  
**Description**: Search users by first name or last name  
**Query Parameters**:
- searchTerm: Search term for name (required)
- page: Page number (default: 0)
- size: Page size (default: 20)
**Responses**:
- 200: Successful search results (UserListDTO)
- 400: Invalid search parameters
- 500: Internal server error

### 2. Authentication

#### POST /api/auth/signin
**Summary**: User authentication (COSGN00C)  
**Description**: Authenticate user credentials  
**Request Body**: UserAuthDTO  
**Responses**:
- 200: Authentication successful (UserAuthResponseDTO)
- 401: Authentication failed - invalid credentials
- 400: Invalid request data
- 500: Internal server error

**Request Schema (UserAuthDTO)**:
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

**Response Schema (UserAuthResponseDTO)**:
```json
{
  "userId": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "authenticated": "boolean",
  "authenticatedAt": "string (ISO 8601 datetime)"
}
```

## Response Schemas

### UserResponseDTO
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "userTypeDisplayName": "string",
  "isAdmin": "boolean",
  "isRegular": "boolean",
  "createdAt": "string (ISO 8601 datetime)",
  "updatedAt": "string (ISO 8601 datetime)"
}
```

### UserListDTO
```json
{
  "users": "array of UserResponseDTO",
  "currentPage": "integer",
  "totalPages": "integer",
  "totalElements": "integer",
  "pageSize": "integer",
  "isFirst": "boolean",
  "isLast": "boolean",
  "hasNext": "boolean",
  "hasPrevious": "boolean"
}
```

## Business Rules Implemented

1. **User Creation (COUSR01C)**:
   - User ID and password are converted to uppercase
   - All fields are required and validated
   - Duplicate user ID validation
   - User type must be 'A' (Admin) or 'R' (Regular)

2. **User Update (COUSR02C)**:
   - Only modified fields are updated
   - User existence validation
   - Password is converted to uppercase if provided

3. **User Deletion (COUSR03C)**:
   - User existence validation
   - Prevents deletion of the last admin user
   - Confirmation required for deletion

4. **User Listing (COUSR00C)**:
   - Pagination support (similar to PF7/PF8 navigation)
   - Filtering by user type
   - Ordered by user ID

5. **Authentication (COSGN00C)**:
   - Case-insensitive user ID lookup (converted to uppercase)
   - Password validation
   - Returns user details without password

## Error Handling

All endpoints include comprehensive error handling with appropriate HTTP status codes:
- 200: Success
- 201: Created
- 204: No Content (for deletions)
- 400: Bad Request (validation errors)
- 401: Unauthorized (authentication failures)
- 404: Not Found
- 409: Conflict (business rule violations)
- 500: Internal Server Error

## Security Considerations

- Passwords are never returned in response DTOs
- User authentication is required for protected operations
- Admin-only operations are restricted based on user type
- Input validation prevents injection attacks
- Proper error messages without sensitive information exposure

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Zero-based page number (default: 0)
- `size`: Number of items per page (default: 20, max: 100)
- `sort`: Sorting criteria (default: by userId)

## Data Validation

- User ID: Maximum 8 characters, alphanumeric
- First Name: Maximum 20 characters, required
- Last Name: Maximum 20 characters, required
- Password: Maximum 8 characters, required
- User Type: Single character, must be 'A' or 'R'

This API provides a complete modernization of the COBOL CardDemo user security administration functionality while maintaining all original business rules and adding modern REST API capabilities.