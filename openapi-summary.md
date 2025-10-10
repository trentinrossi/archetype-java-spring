# User Management System - OpenAPI Endpoints Summary

## Overview
This document provides a comprehensive summary of all REST API endpoints for the User Management System, modernized from COBOL programs COUSR00C, COUSR01C, COUSR02C, and COUSR03C.

## Base URL
```
/api/users
```

## Authentication
- Currently no authentication implemented
- Future enhancement: JWT token-based authentication

## Endpoints

### 1. List Users with Pagination and Filtering
**Endpoint:** `GET /api/users`

**Description:** Retrieve a paginated list of users with optional filtering by User ID prefix. Supports PF7/PF8 style navigation from the original COBOL program.

**Parameters:**
- `userIdPrefix` (query, optional): User ID prefix to filter by (e.g., "USR")
- `page` (query, optional): Page number (0-based), default: 0
- `size` (query, optional): Page size, default: 10
- `sortBy` (query, optional): Sort field, default: "userId"
- `sortDirection` (query, optional): Sort direction (ASC/DESC), default: "ASC"

**Response Codes:**
- `200 OK`: Users retrieved successfully
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: System error

**Example Request:**
```http
GET /api/users?userIdPrefix=USR&page=0&size=10&sortBy=userId&sortDirection=ASC
```

**Example Response:**
```json
{
  "content": [
    {
      "userId": "USR00001",
      "firstName": "John",
      "lastName": "Doe",
      "fullName": "John Doe",
      "password": "pass123",
      "userType": "A",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
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

### 2. Get User by ID
**Endpoint:** `GET /api/users/{userId}`

**Description:** Retrieve a specific user by their exact User ID match.

**Parameters:**
- `userId` (path, required): User ID to retrieve (max 8 characters)

**Response Codes:**
- `200 OK`: User found and retrieved successfully
- `400 Bad Request`: Invalid User ID format
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Example Request:**
```http
GET /api/users/USR00001
```

**Example Response:**
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "password": "pass123",
  "userType": "A",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### 3. Create New User
**Endpoint:** `POST /api/users`

**Description:** Add a new user to the system with validation. Implements the functionality from COUSR01C.

**Request Body:**
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "password": "pass123",
  "userType": "A"
}
```

**Validation Rules:**
- All fields are mandatory
- `userId`: Max 8 characters, must be unique
- `firstName`: Max 20 characters
- `lastName`: Max 20 characters
- `password`: Max 8 characters
- `userType`: Exactly 1 character (A=Admin, U=User)

**Response Codes:**
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data or validation errors
- `409 Conflict`: User ID already exists
- `500 Internal Server Error`: System error

**Example Response:**
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "password": "pass123",
  "userType": "A",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### 4. Update Existing User
**Endpoint:** `PUT /api/users/{userId}`

**Description:** Update user details by User ID. Implements the functionality from COUSR02C.

**Parameters:**
- `userId` (path, required): User ID to update (max 8 characters)

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "password": "newpass",
  "userType": "A"
}
```

**Validation Rules:**
- All fields are mandatory
- User ID cannot be changed
- Same field length constraints as create operation
- At least one field must be different from current values

**Response Codes:**
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data, validation errors, or no changes detected
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Example Response:**
```json
{
  "userId": "USR00001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "password": "newpass",
  "userType": "A",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

### 5. Delete User
**Endpoint:** `DELETE /api/users/{userId}`

**Description:** Delete a user by User ID. Implements the functionality from COUSR03C.

**Parameters:**
- `userId` (path, required): User ID to delete (max 8 characters)

**Response Codes:**
- `204 No Content`: User deleted successfully
- `400 Bad Request`: Invalid User ID format
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

**Example Request:**
```http
DELETE /api/users/USR00001
```

### 6. Advanced User Search
**Endpoint:** `POST /api/users/search`

**Description:** Search users with multiple criteria and pagination.

**Request Body:**
```json
{
  "userId": "USR",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "A",
  "page": 0,
  "size": 10,
  "sortBy": "userId",
  "sortDirection": "ASC"
}
```

**Response Codes:**
- `200 OK`: Search completed successfully
- `400 Bad Request`: Invalid search criteria
- `500 Internal Server Error`: System error

**Example Response:**
Same format as the list users endpoint with paginated results.

## Error Handling

### Standard Error Response Format
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User ID can NOT be empty...",
  "path": "/api/users"
}
```

### Common Error Messages
These messages match the original COBOL program messages:

- `"User ID can NOT be empty..."`
- `"First Name can NOT be empty..."`
- `"Last Name can NOT be empty..."`
- `"Password can NOT be empty..."`
- `"User Type can NOT be empty..."`
- `"User ID already exist..."`
- `"User ID NOT found..."`
- `"Please modify to update..."`
- `"Unable to lookup User..."`
- `"Unable to Add User..."`
- `"Unable to Update User..."`

## Data Models

### User Entity
```json
{
  "userId": "string (max 8 chars)",
  "firstName": "string (max 20 chars)",
  "lastName": "string (max 20 chars)",
  "password": "string (max 8 chars)",
  "userType": "string (1 char: A or U)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Pagination Response
```json
{
  "content": "array of user objects",
  "pageable": {
    "pageNumber": "number",
    "pageSize": "number"
  },
  "totalElements": "number",
  "totalPages": "number",
  "first": "boolean",
  "last": "boolean"
}
```

## Business Rules Implemented

1. **User Listing (COUSR00C):**
   - Paginated display (10 users per page)
   - Filtering by User ID prefix
   - Forward/backward navigation
   - Sorting capabilities

2. **User Creation (COUSR01C):**
   - All fields mandatory validation
   - Unique User ID constraint
   - Field length validations
   - Success/error messaging

3. **User Update (COUSR02C):**
   - User lookup by ID
   - Field validation
   - Change detection
   - Update confirmation

4. **User Deletion (COUSR03C):**
   - User lookup by ID
   - Deletion confirmation
   - Success messaging

## Security Considerations

- **Password Storage:** Currently stored in plain text (matches COBOL implementation)
- **Future Enhancement:** Implement password hashing (bcrypt, Argon2)
- **Authentication:** No authentication currently implemented
- **Authorization:** No role-based access control implemented

## Performance Considerations

- Database indexes on frequently queried fields
- Pagination to limit result sets
- Connection pooling for database access
- Caching can be implemented for frequently accessed data

## Testing

All endpoints should be tested with:
- Valid data scenarios
- Invalid data scenarios
- Edge cases (empty strings, null values, maximum lengths)
- Concurrent access scenarios
- Database constraint violations

## Deployment

The application can be deployed as:
- Standalone Spring Boot JAR
- Docker container
- Kubernetes deployment
- Traditional application server (Tomcat, etc.)

## Monitoring and Logging

- All operations are logged with appropriate log levels
- Request/response logging for debugging
- Performance metrics collection
- Error tracking and alerting