# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Management API endpoints, following the OpenAPI 3.0 specification. The API implements the functionality from the original COBOL programs (COSGN00C, COUSR01C, COUSR02C, COUSR03C) for user authentication and management.

## Base Information
- **Base URL**: `/api/users`
- **API Version**: 1.0.0
- **Content Type**: `application/json`

## Endpoints Summary

### 1. User Authentication (Sign-On)
**Endpoint**: `POST /api/users/signin`
**Description**: Authenticate user credentials matching COSGN00C sign-on screen functionality
**Request Body**: `SignOnRequest`
```json
{
  "userId": "string (max 8 chars, required)",
  "password": "string (max 8 chars, required)"
}
```
**Response**: `SignOnResponse`
```json
{
  "success": "boolean",
  "userType": "string (A=Admin, R=Regular)",
  "message": "string"
}
```
**HTTP Status Codes**:
- `200 OK`: Authentication processed successfully
- `400 Bad Request`: Invalid request data
- `500 Internal Server Error`: Server error

### 2. Create User
**Endpoint**: `POST /api/users`
**Description**: Create a new user matching COUSR01C add user program functionality
**Request Body**: `UserCreateRequest`
```json
{
  "userId": "string (max 8 chars, required)",
  "firstName": "string (max 20 chars, required)",
  "lastName": "string (max 20 chars, required)",
  "password": "string (max 8 chars, required)",
  "userType": "string (max 1 char, optional, default: R)"
}
```
**Response**: `UserResponse`
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```
**HTTP Status Codes**:
- `201 Created`: User created successfully
- `400 Bad Request`: Invalid request data
- `409 Conflict`: User ID already exists
- `500 Internal Server Error`: Server error

### 3. Update User
**Endpoint**: `PUT /api/users/{userId}`
**Description**: Update user details matching COUSR02C update program functionality
**Path Parameters**:
- `userId`: string (required) - The user ID to update
**Request Body**: `UserUpdateRequest`
```json
{
  "firstName": "string (max 20 chars, optional)",
  "lastName": "string (max 20 chars, optional)",
  "password": "string (max 8 chars, optional)",
  "userType": "string (max 1 char, optional)"
}
```
**Response**: `UserResponse`
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```
**HTTP Status Codes**:
- `200 OK`: User updated successfully
- `400 Bad Request`: Invalid request data
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

### 4. Delete User
**Endpoint**: `DELETE /api/users/{userId}`
**Description**: Delete a user matching COUSR03C delete program functionality
**Path Parameters**:
- `userId`: string (required) - The user ID to delete
**Response**: No content
**HTTP Status Codes**:
- `204 No Content`: User deleted successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

### 5. Get User by ID
**Endpoint**: `GET /api/users/{userId}`
**Description**: Retrieve a user by their ID for display
**Path Parameters**:
- `userId`: string (required) - The user ID to retrieve
**Response**: `UserResponse`
```json
{
  "userId": "string",
  "firstName": "string",
  "lastName": "string",
  "userType": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```
**HTTP Status Codes**:
- `200 OK`: User retrieved successfully
- `404 Not Found`: User not found
- `500 Internal Server Error`: Server error

### 6. Get All Users (Paginated)
**Endpoint**: `GET /api/users`
**Description**: Retrieve a paginated list of all users
**Query Parameters**:
- `page`: integer (optional, default: 0) - Page number
- `size`: integer (optional, default: 20) - Page size
- `sort`: string (optional) - Sort criteria
**Response**: `Page<UserResponse>`
```json
{
  "content": [
    {
      "userId": "string",
      "firstName": "string",
      "lastName": "string",
      "userType": "string",
      "createdAt": "datetime",
      "updatedAt": "datetime"
    }
  ],
  "pageable": {
    "sort": {},
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 0,
  "totalPages": 0,
  "last": true,
  "first": true,
  "numberOfElements": 0
}
```
**HTTP Status Codes**:
- `200 OK`: Users retrieved successfully
- `400 Bad Request`: Invalid request parameters
- `500 Internal Server Error`: Server error

## Data Models

### SignOnRequest
- `userId`: string (max 8 characters, required)
- `password`: string (max 8 characters, required)

### SignOnResponse
- `success`: boolean (required)
- `userType`: string (optional, A=Admin, R=Regular)
- `message`: string (required)

### UserCreateRequest
- `userId`: string (max 8 characters, required)
- `firstName`: string (max 20 characters, required)
- `lastName`: string (max 20 characters, required)
- `password`: string (max 8 characters, required)
- `userType`: string (max 1 character, optional, default: R)

### UserUpdateRequest
- `firstName`: string (max 20 characters, optional)
- `lastName`: string (max 20 characters, optional)
- `password`: string (max 8 characters, optional)
- `userType`: string (max 1 character, optional)

### UserResponse
- `userId`: string (required)
- `firstName`: string (required)
- `lastName`: string (required)
- `userType`: string (optional)
- `createdAt`: datetime (required)
- `updatedAt`: datetime (required)

## Business Rules Implementation

### Authentication (COSGN00C)
- Validates user credentials against the USRSEC table
- Returns appropriate messages for "User not found" and "Wrong password"
- Provides user type information for successful authentication

### User Creation (COUSR01C)
- Validates all required fields are provided
- Checks for duplicate User IDs
- Sets default user type to "R" (Regular) if not specified
- Returns appropriate error messages for validation failures

### User Update (COUSR02C)
- Validates user exists before update
- Only updates fields that have been modified
- Maintains audit trail with updated timestamp
- Returns confirmation of successful updates

### User Deletion (COUSR03C)
- Validates user exists before deletion
- Performs hard delete from the database
- Returns appropriate error messages if user not found

## Security Considerations
- Passwords are stored as plain text (matching original COBOL implementation)
- User ID length is limited to 8 characters
- All string fields have maximum length constraints
- Input validation is performed on all endpoints

## Error Handling
- Consistent error response format
- Appropriate HTTP status codes
- Detailed error messages for validation failures
- Logging of all operations for audit purposes