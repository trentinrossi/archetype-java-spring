# User Security Management API - OpenAPI Summary

## Overview
This document provides a comprehensive summary of the User Security Management REST API endpoints following the OpenAPI specification. The API provides functionality for user authentication, user management, and CRUD operations on user data.

## Base Information
- **Base URL**: `/api/users`
- **API Version**: 1.0.0
- **Content Type**: `application/json`

## Endpoints Summary

### 1. User Authentication

#### POST /api/users/signin
**Summary**: User sign in  
**Description**: Authenticate user with credentials  
**Request Body**: `UserSignOnRequest`
```json
{
  "userId": "USER001",
  "password": "password"
}
```
**Response**: `UserSignOnResponse`
```json
{
  "success": true,
  "message": "Sign on successful",
  "userType": "A",
  "redirectProgram": "ADMIN_MENU"
}
```
**Status Codes**:
- `200`: Sign in successful
- `400`: Invalid request data
- `401`: Invalid credentials
- `500`: Internal server error

### 2. User Management

#### GET /api/users
**Summary**: Get all users  
**Description**: Retrieve a paginated list of all users  
**Query Parameters**:
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort criteria

**Response**: `UserListResponse`
```json
{
  "users": [
    {
      "userId": "USER001",
      "firstName": "John",
      "lastName": "Doe",
      "userType": "R",
      "fullName": "John Doe",
      "createdAt": "2023-10-01T12:00:00",
      "updatedAt": "2023-10-01T12:00:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```
**Status Codes**:
- `200`: Successful retrieval of users
- `400`: Invalid request parameters
- `500`: Internal server error

#### GET /api/users/{userId}
**Summary**: Get user by ID  
**Description**: Retrieve a user by their ID  
**Path Parameters**:
- `userId`: User ID (required)

**Response**: `UserResponse`
```json
{
  "userId": "USER001",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "R",
  "fullName": "John Doe",
  "createdAt": "2023-10-01T12:00:00",
  "updatedAt": "2023-10-01T12:00:00"
}
```
**Status Codes**:
- `200`: Successful retrieval of user
- `404`: User not found
- `500`: Internal server error

#### POST /api/users
**Summary**: Create a new user  
**Description**: Create a new user  
**Request Body**: `UserCreateRequest`
```json
{
  "userId": "USER001",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password",
  "userType": "R"
}
```
**Response**: `UserResponse`
**Status Codes**:
- `201`: User created successfully
- `400`: Invalid request data
- `409`: User already exists
- `500`: Internal server error

#### PUT /api/users/{userId}
**Summary**: Update an existing user  
**Description**: Update user details by ID  
**Path Parameters**:
- `userId`: User ID (required)

**Request Body**: `UserUpdateRequest`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "password": "newpassword",
  "userType": "A"
}
```
**Response**: `UserResponse`
**Status Codes**:
- `200`: User updated successfully
- `400`: Invalid request data
- `404`: User not found
- `500`: Internal server error

#### DELETE /api/users/{userId}
**Summary**: Delete a user  
**Description**: Delete a user by ID  
**Path Parameters**:
- `userId`: User ID (required)

**Response**: No content
**Status Codes**:
- `204`: User deleted successfully
- `404`: User not found
- `500`: Internal server error

## Data Models

### UserSignOnRequest
- `userId` (string, required): User ID for sign on (max 8 characters)
- `password` (string, required): User password (max 8 characters)

### UserSignOnResponse
- `success` (boolean, required): Sign on success status
- `message` (string, required): Sign on response message
- `userType` (string, optional): User type (A=Admin, R=Regular)
- `redirectProgram` (string, optional): Redirect program

### UserCreateRequest
- `userId` (string, required): User ID (max 8 characters)
- `firstName` (string, required): First name (max 20 characters)
- `lastName` (string, required): Last name (max 20 characters)
- `password` (string, required): Password (max 8 characters)
- `userType` (string, required): User type - A=Admin, R=Regular (1 character)

### UserUpdateRequest
- `firstName` (string, optional): First name (max 20 characters)
- `lastName` (string, optional): Last name (max 20 characters)
- `password` (string, optional): Password (max 8 characters)
- `userType` (string, optional): User type - A=Admin, R=Regular (1 character)

### UserResponse
- `userId` (string): User ID
- `firstName` (string): First name
- `lastName` (string): Last name
- `userType` (string): User type (A=Admin, R=Regular)
- `fullName` (string): Full name (computed)
- `createdAt` (datetime): Creation timestamp
- `updatedAt` (datetime): Last update timestamp

### UserListResponse
- `users` (array): List of UserResponse objects
- `page` (integer): Current page number
- `size` (integer): Page size
- `totalElements` (long): Total number of elements
- `totalPages` (integer): Total number of pages
- `first` (boolean): Whether this is the first page
- `last` (boolean): Whether this is the last page

## Business Rules Implemented

1. **User Authentication**: Validates user credentials against the USRSEC database
2. **User Type Management**: Supports Admin (A) and Regular (R) user types
3. **Duplicate Prevention**: Prevents creation of users with existing User IDs
4. **Field Validation**: Enforces required fields and length constraints
5. **Case Insensitive Lookup**: User ID lookup is case insensitive
6. **Pagination Support**: All list operations support pagination
7. **Audit Trail**: Tracks creation and update timestamps
8. **Password Security**: Password field is not exposed in response objects

## Error Handling

The API implements comprehensive error handling with appropriate HTTP status codes:
- `400 Bad Request`: Invalid input data or validation errors
- `401 Unauthorized`: Authentication failures
- `404 Not Found`: Resource not found
- `409 Conflict`: Duplicate resource creation attempts
- `500 Internal Server Error`: Unexpected server errors

All error responses include descriptive messages to help clients understand and resolve issues.