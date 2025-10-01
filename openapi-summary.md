# User Management System API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Management System REST API endpoints, following OpenAPI 3.0 specification. The API implements the business logic from the CICS programs COUSR00C, COUSR01C, COUSR02C, and COUSR03C.

## Base Information
- **Base URL**: `/api/users`
- **API Version**: 1.0.0
- **Content Type**: `application/json`

## Endpoints Summary

### 1. List Users (COUSR00C Implementation)
**GET** `/api/users`

**Description**: Retrieve a paginated list of all users with optional filtering by user ID prefix

**Parameters**:
- `page` (query, optional): Page number (default: 0)
- `size` (query, optional): Page size (default: 20, max: 10 for business logic compliance)
- `userIdPrefix` (query, optional): Filter users by user ID prefix

**Response Codes**:
- `200 OK`: Successful retrieval of users
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: Server error

**Response Schema**:
```json
{
  "content": [
    {
      "userId": "USR00001",
      "firstName": "John",
      "lastName": "Doe",
      "userType": "U"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10
}
```

### 2. Create User (COUSR01C Implementation)
**POST** `/api/users`

**Description**: Create a new user with validation

**Request Body**:
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "password": "pass123",
  "userType": "U"
}
```

**Validation Rules**:
- `userId`: Required, max 8 characters
- `firstName`: Required, max 20 characters
- `lastName`: Required, max 20 characters
- `password`: Required, max 8 characters
- `userType`: Required, must be 'A' (Admin) or 'U' (User)

**Response Codes**:
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data
- `409 Conflict`: User with ID already exists
- `500 Internal Server Error`: Server error

**Response Schema**:
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "U",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 3. Get User Details (COUSR02C Implementation)
**GET** `/api/users/{userId}`

**Description**: Retrieve a user by their ID

**Parameters**:
- `userId` (path, required): User ID (e.g., "USR00001")

**Response Codes**:
- `200 OK`: Successful retrieval of user
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

**Response Schema**:
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "userType": "U",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

### 4. Update User (COUSR02C Implementation)
**PUT** `/api/users/{userId}`

**Description**: Update user details by ID

**Parameters**:
- `userId` (path, required): User ID (e.g., "USR00001")

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "password": "newpass",
  "userType": "A"
}
```

**Validation Rules**:
- `firstName`: Required, max 20 characters
- `lastName`: Required, max 20 characters
- `password`: Required, max 8 characters
- `userType`: Required, must be 'A' (Admin) or 'U' (User)

**Response Codes**:
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

### 5. Get User for Deletion (COUSR03C Implementation)
**GET** `/api/users/{userId}/delete`

**Description**: Retrieve user details for deletion confirmation

**Parameters**:
- `userId` (path, required): User ID (e.g., "USR00001")

**Response Codes**:
- `200 OK`: User details retrieved for deletion confirmation
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

**Response Schema**:
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "U"
}
```

### 6. Delete User (COUSR03C Implementation)
**DELETE** `/api/users/{userId}`

**Description**: Delete a user by ID

**Parameters**:
- `userId` (path, required): User ID (e.g., "USR00001")

**Response Codes**:
- `204 No Content`: User deleted successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

## Data Models

### UserListDto
```json
{
  "userId": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "userType": "string (1 char: A or U)"
}
```

### UserCreateDto
```json
{
  "userId": "string (required, max 8 chars)",
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or U)"
}
```

### UserUpdateDto
```json
{
  "firstName": "string (required, max 20 chars)",
  "lastName": "string (required, max 20 chars)",
  "password": "string (required, max 8 chars)",
  "userType": "string (required, A or U)"
}
```

### UserDetailDto
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### UserDeleteDto
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string"
}
```

## Business Rules Implementation

### COUSR00C - User List Management
- Supports pagination with 10 users per page (configurable)
- Filtering by User ID prefix
- Ordered by User ID
- Returns user basic information for listing

### COUSR01C - Add New User
- All fields are mandatory
- User ID uniqueness validation
- Field length validation according to USRSEC file structure
- Returns created user details

### COUSR02C - User Update
- User lookup by ID required
- All fields mandatory for update
- Change detection to avoid unnecessary updates
- User ID cannot be modified (primary key)

### COUSR03C - User Deletion
- User lookup by ID for confirmation
- Soft confirmation through separate endpoint
- Actual deletion through DELETE method
- Returns appropriate status codes

## Error Handling

All endpoints return consistent error responses:

```json
{
  "timestamp": "2023-10-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users"
}
```

## Security Considerations

- Input validation on all endpoints
- SQL injection prevention through JPA
- Proper HTTP status codes
- Logging for audit trails
- Transaction management for data consistency

## Testing

The API can be tested using:
- Swagger UI (available at `/swagger-ui.html`)
- Postman collections
- Unit and integration tests included in the project

## Database Schema

The API uses the `usrsec` table with the following structure:
- `sec_usr_id` VARCHAR(8) PRIMARY KEY
- `sec_usr_fname` VARCHAR(20) NOT NULL
- `sec_usr_lname` VARCHAR(20) NOT NULL
- `sec_usr_pwd` VARCHAR(8) NOT NULL
- `sec_usr_type` VARCHAR(1) NOT NULL
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL