# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Management API endpoints, implementing the functionality from COSGN00C (Signon Screen) and COUSR01C (Add New RegularAdmin User) COBOL programs.

## Base Information
- **Base URL**: `/api`
- **API Version**: 1.0.0
- **Content Type**: `application/json`
- **Tag**: User Security Management

## Endpoints

### 1. User Authentication
**Endpoint**: `POST /api/auth/signin`

**Description**: Authenticate user with credentials (implements COSGN00C functionality)

**Request Body**:
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```

**Response**:
```json
{
  "result": "SUCCESS|FAILURE",
  "userType": "A|R",
  "targetModule": "COADM01C|COMEN01C",
  "errorMessage": "string"
}
```

**HTTP Status Codes**:
- `200 OK`: Authentication processed successfully
- `400 Bad Request`: Invalid request data
- `500 Internal Server Error`: System error

**Business Rules**:
- User ID and Password are converted to uppercase
- Admin users (A) are directed to COADM01C module
- Regular users (R) are directed to COMEN01C module
- Returns specific error messages for "User not found" and "Invalid password"

---

### 2. Create New User
**Endpoint**: `POST /api/users`

**Description**: Create a new user in the system (implements COUSR01C functionality)

**Request Body**:
```json
{
  "firstName": "string (max 100 chars, required)",
  "lastName": "string (max 100 chars, required)",
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (1 char, A|R, required)"
}
```

**Response**:
```json
{
  "result": "SUCCESS|FAILURE",
  "errorMessage": "string",
  "userId": "string"
}
```

**HTTP Status Codes**:
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data or validation error
- `409 Conflict`: User ID already exists
- `500 Internal Server Error`: System error

**Business Rules**:
- All fields are mandatory and validated
- User ID and Password are converted to uppercase
- User Type must be 'A' (Admin) or 'R' (Regular)
- Duplicate User ID detection and error handling

---

### 3. Get User by ID
**Endpoint**: `GET /api/users/{userId}`

**Description**: Retrieve a user by their ID

**Path Parameters**:
- `userId`: string (User ID to retrieve)

**Response**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "A|R",
  "userTypeDisplayName": "Administrator|Regular User",
  "targetModule": "COADM01C|COMEN01C",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

**HTTP Status Codes**:
- `200 OK`: User retrieved successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

---

### 4. Update User
**Endpoint**: `PUT /api/users/{userId}`

**Description**: Update user details by ID

**Path Parameters**:
- `userId`: string (User ID to update)

**Request Body**:
```json
{
  "firstName": "string (max 100 chars)",
  "lastName": "string (max 100 chars)",
  "password": "string (max 8 chars)",
  "userType": "string (1 char, A|R)"
}
```

**Response**:
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "userType": "A|R",
  "userTypeDisplayName": "Administrator|Regular User",
  "targetModule": "COADM01C|COMEN01C",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```

**HTTP Status Codes**:
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

---

### 5. Delete User
**Endpoint**: `DELETE /api/users/{userId}`

**Description**: Delete a user by ID

**Path Parameters**:
- `userId`: string (User ID to delete)

**Response**: No content

**HTTP Status Codes**:
- `204 No Content`: User deleted successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: System error

## Data Models

### User Entity (USRSEC Table)
- **user_id**: VARCHAR(8) PRIMARY KEY - Unique user identifier
- **first_name**: VARCHAR(100) NOT NULL - User's first name
- **last_name**: VARCHAR(100) NOT NULL - User's last name
- **password**: VARCHAR(8) NOT NULL - User's password (uppercase)
- **user_type**: VARCHAR(1) NOT NULL - User type (A=Admin, R=Regular)
- **created_at**: TIMESTAMP NOT NULL - Creation timestamp
- **updated_at**: TIMESTAMP NOT NULL - Last update timestamp

### Business Rules Summary
1. **Authentication (COSGN00C)**:
   - User ID and Password validation
   - Uppercase conversion
   - User type determination
   - Target module assignment
   - Error handling for invalid credentials

2. **User Creation (COUSR01C)**:
   - Complete field validation
   - Duplicate User ID prevention
   - Data normalization (uppercase)
   - Error handling and messaging

3. **General Rules**:
   - All User IDs are stored and processed in uppercase
   - Admin users (A) access COADM01C module
   - Regular users (R) access COMEN01C module
   - Comprehensive error handling and logging
   - Transaction management for data integrity

## Security Considerations
- Passwords are stored in plain text (as per original COBOL implementation)
- User ID uniqueness is enforced at database level
- Input validation prevents injection attacks
- Proper error handling prevents information disclosure

## Testing Data
The migration script includes sample test data:
- ADMIN001: System Administrator (Admin)
- USER001: John Doe (Regular)
- USER002: Jane Smith (Regular)

## Dependencies
- Spring Boot 3.x
- Spring Data JPA
- H2/PostgreSQL Database
- Flyway Migration
- OpenAPI 3 Documentation
- Bean Validation (JSR-303)