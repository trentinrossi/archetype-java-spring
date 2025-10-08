# User Security Profile Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the REST API endpoints for the User Security Profile Management system, following the OpenAPI 3.0 specification.

## Base Information
- **Base URL**: `/api/users`
- **API Version**: 1.0.0
- **Content Type**: `application/json`

## Endpoints Summary

### 1. Get All Users
- **Method**: `GET`
- **Path**: `/api/users`
- **Summary**: Retrieve a paginated list of all users with optional search
- **Parameters**:
  - `search` (query, optional): Search term for user names
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response Codes**:
  - `200`: Successful retrieval of users
  - `400`: Invalid request parameters
  - `500`: Internal server error
- **Response Schema**: `UserListResponse`

### 2. Get User by ID
- **Method**: `GET`
- **Path**: `/api/users/{userId}`
- **Summary**: Retrieve a user by their ID
- **Parameters**:
  - `userId` (path, required): ID of the user to retrieve
- **Response Codes**:
  - `200`: Successful retrieval of user
  - `404`: User not found
  - `500`: Internal server error
- **Response Schema**: `UserResponse`

### 3. Create New User
- **Method**: `POST`
- **Path**: `/api/users`
- **Summary**: Create a new user
- **Request Body**: `CreateUserRequest`
- **Response Codes**:
  - `201`: User created successfully
  - `400`: Invalid request data
  - `409`: User with ID already exists
  - `500`: Internal server error
- **Response Schema**: `UserResponse`

### 4. Update User
- **Method**: `PUT`
- **Path**: `/api/users/{userId}`
- **Summary**: Update user details by ID
- **Parameters**:
  - `userId` (path, required): ID of the user to update
- **Request Body**: `UpdateUserRequest`
- **Response Codes**:
  - `200`: User updated successfully
  - `400`: Invalid request data
  - `404`: User not found
  - `500`: Internal server error
- **Response Schema**: `UserResponse`

### 5. Delete User
- **Method**: `DELETE`
- **Path**: `/api/users/{userId}`
- **Summary**: Delete a user by ID
- **Parameters**:
  - `userId` (path, required): ID of the user to delete
- **Response Codes**:
  - `204`: User deleted successfully
  - `404`: User not found
  - `500`: Internal server error
- **Response**: No content

### 6. Search Users by Name
- **Method**: `GET`
- **Path**: `/api/users/search`
- **Summary**: Search users by first name or last name with pagination
- **Parameters**:
  - `name` (query, required): Search term for user names
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response Codes**:
  - `200`: Successful search results
  - `400`: Invalid search parameters
  - `500`: Internal server error
- **Response Schema**: `UserListResponse`

### 7. Get Users by Type
- **Method**: `GET`
- **Path**: `/api/users/type/{userType}`
- **Summary**: Retrieve users filtered by user type with pagination
- **Parameters**:
  - `userType` (path, required): Type of users to retrieve (A=Admin, R=Regular)
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response Codes**:
  - `200`: Successful retrieval of users by type
  - `400`: Invalid user type
  - `500`: Internal server error
- **Response Schema**: `UserListResponse`

## Data Models

### UserResponse
```json
{
  "userId": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "fullName": "string",
  "userType": "string (A or R)",
  "userTypeDisplayName": "string (Admin or Regular)",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### CreateUserRequest
```json
{
  "userId": "string (required, max 8 chars)",
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or R)"
}
```

### UpdateUserRequest
```json
{
  "firstName": "string (optional, max 20 chars)",
  "lastName": "string (optional, max 20 chars)",
  "password": "string (optional, max 8 chars)",
  "userType": "string (optional, A or R)"
}
```

### UserListResponse
```json
{
  "users": ["UserResponse"],
  "currentPage": "integer",
  "totalPages": "integer",
  "totalElements": "long",
  "pageSize": "integer",
  "first": "boolean",
  "last": "boolean",
  "hasNext": "boolean",
  "hasPrevious": "boolean"
}
```

## Validation Rules

### User ID
- Required for creation
- Maximum 8 characters
- Must be unique
- Cannot be null or empty

### First Name
- Required for creation
- Maximum 20 characters
- Cannot be null or empty for creation

### Last Name
- Required for creation
- Maximum 20 characters
- Cannot be null or empty for creation

### Password
- Required for creation
- Maximum 8 characters
- Cannot be null or empty for creation

### User Type
- Required for creation
- Must be either 'A' (Admin) or 'R' (Regular)
- Cannot be null or empty for creation

## Error Handling

All endpoints return appropriate HTTP status codes and error messages:

- `400 Bad Request`: Invalid input data or validation errors
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists (for creation)
- `500 Internal Server Error`: Unexpected server errors

Error responses follow a consistent format with descriptive messages to help clients understand and resolve issues.

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Page number (0-based, default: 0)
- `size`: Number of items per page (default: 20)
- `sort`: Sort criteria (e.g., "firstName,asc" or "createdAt,desc")

## Security Considerations

- Passwords are included in creation and update requests but never returned in responses
- All endpoints include proper validation to prevent injection attacks
- Input sanitization is performed on all user-provided data
- Proper error handling prevents information leakage

## Usage Examples

### Create a new admin user
```bash
POST /api/users
{
  "userId": "ADMIN003",
  "firstName": "Super",
  "lastName": "Admin",
  "password": "secret123",
  "userType": "A"
}
```

### Search for users by name
```bash
GET /api/users/search?name=john&page=0&size=10
```

### Get all admin users
```bash
GET /api/users/type/A?page=0&size=20&sort=firstName,asc
```

### Update user information
```bash
PUT /api/users/USER001
{
  "firstName": "Johnny",
  "lastName": "Doe"
}
```