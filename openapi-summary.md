# CardDemo Login and User Management - API Summary

## Overview

This document provides a comprehensive summary of all REST API endpoints for the CardDemo Login and User Management system. The application is built using Spring Boot 3.5.5 with Java 21, PostgreSQL database, and follows a clean layered architecture.

## Base URL

```
http://localhost:8080/api
```

## Entities

The system manages 7 core entities:
1. **AdminUser** - Administrative users with access to admin menu functions
2. **AdminMenuOption** - Administrative menu options available to admin users
3. **UserSession** - Current user session information
4. **MenuOption** - Menu options available to users based on access level
5. **User** - Users with authentication and authorization information
6. **UserListPage** - Paginated view of user records
7. **UserSelection** - User selection actions for update or delete operations

---

## 1. Admin User Management API

**Base Path:** `/api/admin-users`

### Endpoints

#### GET /api/admin-users
- **Summary:** Get all admin users
- **Description:** Retrieve a paginated list of all admin users
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<AdminUserResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/admin-users/{userId}
- **Summary:** Get admin user by ID
- **Description:** Retrieve an admin user by their user ID
- **Parameters:** 
  - `userId` (path, required): User ID (max 8 characters)
- **Response:** 200 OK - AdminUserResponseDto
- **Error Codes:** 400 (User ID cannot be empty), 404 (User not found), 500 (Internal error)

#### POST /api/admin-users
- **Summary:** Create a new admin user
- **Description:** Create a new admin user in the system
- **Request Body:** CreateAdminUserRequestDto
  ```json
  {
    "userId": "ADMIN001",
    "authenticationStatus": true
  }
  ```
- **Response:** 201 Created - AdminUserResponseDto
- **Error Codes:** 400 (Invalid data or User ID already exists), 500 (Internal error)

#### PUT /api/admin-users/{userId}
- **Summary:** Update an existing admin user
- **Description:** Update admin user details by user ID
- **Parameters:** 
  - `userId` (path, required): User ID
- **Request Body:** UpdateAdminUserRequestDto
  ```json
  {
    "authenticationStatus": true
  }
  ```
- **Response:** 200 OK - AdminUserResponseDto
- **Error Codes:** 400 (Invalid data), 404 (User not found), 500 (Internal error)

#### DELETE /api/admin-users/{userId}
- **Summary:** Delete an admin user
- **Description:** Delete an admin user by user ID
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 204 No Content
- **Error Codes:** 400 (User ID cannot be empty), 404 (User not found), 500 (Internal error)

#### GET /api/admin-users/{userId}/authenticate
- **Summary:** Authenticate admin user
- **Description:** Check if admin user is authenticated
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 200 OK - Boolean
- **Error Codes:** 400 (User ID cannot be empty), 404 (User not found), 500 (Internal error)

#### POST /api/admin-users/{userId}/validate-access
- **Summary:** Validate admin access
- **Description:** Validate if user has admin access privileges
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 200 OK
- **Error Codes:** 400 (User ID cannot be empty), 401 (No admin access), 404 (User not found), 500 (Internal error)

#### PUT /api/admin-users/{userId}/authentication-status
- **Summary:** Set authentication status
- **Description:** Set authentication status for an admin user
- **Parameters:** 
  - `userId` (path, required): User ID
  - `status` (query, required): Boolean authentication status
- **Response:** 200 OK - AdminUserResponseDto
- **Error Codes:** 400 (Invalid data), 404 (User not found), 500 (Internal error)

---

## 2. Admin Menu Options API

**Base Path:** `/api/admin-menu-options`

### Endpoints

#### GET /api/admin-menu-options
- **Summary:** Get all admin menu options
- **Description:** Retrieve a paginated list of all administrative menu options
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<AdminMenuOptionResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/admin-menu-options/{id}
- **Summary:** Get admin menu option by ID
- **Description:** Retrieve an administrative menu option by its ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Response:** 200 OK - AdminMenuOptionResponseDto
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### GET /api/admin-menu-options/by-number/{optionNumber}
- **Summary:** Get admin menu option by option number
- **Description:** Retrieve an administrative menu option by its option number
- **Parameters:** 
  - `optionNumber` (path, required): Option number (0-99)
- **Response:** 200 OK - AdminMenuOptionResponseDto
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### GET /api/admin-menu-options/active
- **Summary:** Get all active admin menu options
- **Description:** Retrieve a list of all active administrative menu options
- **Response:** 200 OK - List<AdminMenuOptionResponseDto>
- **Error Codes:** 500 (Internal error)

#### POST /api/admin-menu-options
- **Summary:** Create a new admin menu option
- **Description:** Create a new administrative menu option
- **Request Body:** CreateAdminMenuOptionRequestDto
  ```json
  {
    "optionNumber": 1,
    "optionName": "User Account Management",
    "programName": "COUSR00C",
    "isActive": true
  }
  ```
- **Response:** 201 Created - AdminMenuOptionResponseDto
- **Error Codes:** 400 (Invalid data), 500 (Internal error)

#### PUT /api/admin-menu-options/{id}
- **Summary:** Update an existing admin menu option
- **Description:** Update administrative menu option details by ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Request Body:** UpdateAdminMenuOptionRequestDto
- **Response:** 200 OK - AdminMenuOptionResponseDto
- **Error Codes:** 400 (Invalid data), 404 (Menu option not found), 500 (Internal error)

#### DELETE /api/admin-menu-options/{id}
- **Summary:** Delete an admin menu option
- **Description:** Delete an administrative menu option by ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Response:** 204 No Content
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### POST /api/admin-menu-options/{optionNumber}/validate
- **Summary:** Validate menu option for execution
- **Description:** Validate if a menu option can be executed based on its active status
- **Parameters:** 
  - `optionNumber` (path, required): Option number
- **Response:** 200 OK - Validation result
- **Error Codes:** 400 (Invalid option number), 404 (Menu option not found), 503 (Coming soon)

#### GET /api/admin-menu-options/{optionNumber}/program
- **Summary:** Get program name for option
- **Description:** Retrieve the program name associated with a menu option
- **Parameters:** 
  - `optionNumber` (path, required): Option number
- **Response:** 200 OK - Program name
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### GET /api/admin-menu-options/{optionNumber}/is-active
- **Summary:** Check if menu option is active
- **Description:** Check if a menu option is currently active
- **Parameters:** 
  - `optionNumber` (path, required): Option number
- **Response:** 200 OK - Boolean
- **Error Codes:** 400 (Invalid option number), 500 (Internal error)

---

## 3. User Session Management API

**Base Path:** `/api/user-sessions`

### Endpoints

#### GET /api/user-sessions
- **Summary:** Get all user sessions
- **Description:** Retrieve a paginated list of all user sessions
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<UserSessionResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/user-sessions/{id}
- **Summary:** Get user session by ID
- **Description:** Retrieve a user session by its ID
- **Parameters:** 
  - `id` (path, required): Session ID
- **Response:** 200 OK - UserSessionResponseDto
- **Error Codes:** 404 (Session not found), 500 (Internal error)

#### GET /api/user-sessions/user/{userId}
- **Summary:** Get user session by user ID
- **Description:** Retrieve the most recent user session for a user
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 200 OK - UserSessionResponseDto
- **Error Codes:** 404 (Session not found), 500 (Internal error)

#### POST /api/user-sessions
- **Summary:** Create a new user session
- **Description:** Create a new user session
- **Request Body:** CreateUserSessionRequestDto
  ```json
  {
    "transactionId": "CA00",
    "programName": "COADM01C",
    "fromProgram": "COMEN01C",
    "fromTransaction": "CO00",
    "programContext": 123456789,
    "reenterFlag": false,
    "toProgram": "COUSR00C",
    "programReenterFlag": "N",
    "userType": "A",
    "fromTransactionId": "CA00",
    "userId": "ADMIN001"
  }
  ```
- **Response:** 201 Created - UserSessionResponseDto
- **Error Codes:** 400 (Invalid data), 500 (Internal error)

#### PUT /api/user-sessions/{id}
- **Summary:** Update an existing user session
- **Description:** Update user session details by ID
- **Parameters:** 
  - `id` (path, required): Session ID
- **Request Body:** UpdateUserSessionRequestDto
- **Response:** 200 OK - UserSessionResponseDto
- **Error Codes:** 400 (Invalid data), 404 (Session not found), 500 (Internal error)

#### DELETE /api/user-sessions/{id}
- **Summary:** Delete a user session
- **Description:** Delete a user session by ID
- **Parameters:** 
  - `id` (path, required): Session ID
- **Response:** 204 No Content
- **Error Codes:** 404 (Session not found), 500 (Internal error)

---

## 4. Menu Options API

**Base Path:** `/api/menu-options`

### Endpoints

#### GET /api/menu-options
- **Summary:** Get all menu options
- **Description:** Retrieve a paginated list of all menu options
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<MenuOptionResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/menu-options/{id}
- **Summary:** Get menu option by ID
- **Description:** Retrieve a menu option by its ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Response:** 200 OK - MenuOptionResponseDto
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### GET /api/menu-options/by-number/{optionNumber}
- **Summary:** Get menu option by option number
- **Description:** Retrieve a menu option by its option number
- **Parameters:** 
  - `optionNumber` (path, required): Option number
- **Response:** 200 OK - MenuOptionResponseDto
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### GET /api/menu-options/accessible/{userType}
- **Summary:** Get accessible menu options for user type
- **Description:** Retrieve menu options accessible to a specific user type
- **Parameters:** 
  - `userType` (path, required): User type (A, U, or R)
- **Response:** 200 OK - List<MenuOptionResponseDto>
- **Error Codes:** 500 (Internal error)

#### POST /api/menu-options
- **Summary:** Create a new menu option
- **Description:** Create a new menu option
- **Request Body:** CreateMenuOptionRequestDto
  ```json
  {
    "optionNumber": 1,
    "optionName": "View Account",
    "programName": "COACTVW",
    "userTypeRequired": "U",
    "optionCount": 10
  }
  ```
- **Response:** 201 Created - MenuOptionResponseDto
- **Error Codes:** 400 (Invalid data), 500 (Internal error)

#### PUT /api/menu-options/{id}
- **Summary:** Update an existing menu option
- **Description:** Update menu option details by ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Request Body:** UpdateMenuOptionRequestDto
- **Response:** 200 OK - MenuOptionResponseDto
- **Error Codes:** 400 (Invalid data), 404 (Menu option not found), 500 (Internal error)

#### DELETE /api/menu-options/{id}
- **Summary:** Delete a menu option
- **Description:** Delete a menu option by ID
- **Parameters:** 
  - `id` (path, required): Menu option ID
- **Response:** 204 No Content
- **Error Codes:** 404 (Menu option not found), 500 (Internal error)

#### POST /api/menu-options/{optionNumber}/validate-access
- **Summary:** Validate menu option access
- **Description:** Validate if a user type can access a menu option
- **Parameters:** 
  - `optionNumber` (path, required): Option number
  - `userType` (query, required): User type
- **Response:** 200 OK
- **Error Codes:** 403 (Access denied), 404 (Menu option not found), 500 (Internal error)

---

## 5. User Management API

**Base Path:** `/api/users`

### Endpoints

#### GET /api/users
- **Summary:** Get all users
- **Description:** Retrieve a paginated list of all users
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<UserResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/users/{userId}
- **Summary:** Get user by ID
- **Description:** Retrieve a user by their user ID
- **Parameters:** 
  - `userId` (path, required): User ID (max 8 characters)
- **Response:** 200 OK - UserResponseDto
- **Error Codes:** 404 (User not found), 500 (Internal error)

#### GET /api/users/search
- **Summary:** Search users by name
- **Description:** Search users by first name or last name
- **Parameters:** 
  - `searchTerm` (query, required): Search term
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<UserResponseDto>
- **Error Codes:** 500 (Internal error)

#### POST /api/users
- **Summary:** Create a new user
- **Description:** Create a new user
- **Request Body:** CreateUserRequestDto
  ```json
  {
    "userId": "USER001",
    "userType": "U",
    "authenticated": false,
    "password": "pass123",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Response:** 201 Created - UserResponseDto
- **Error Codes:** 400 (Invalid data or User ID already exists), 500 (Internal error)

#### PUT /api/users/{userId}
- **Summary:** Update an existing user
- **Description:** Update user details by user ID
- **Parameters:** 
  - `userId` (path, required): User ID
- **Request Body:** UpdateUserRequestDto
  ```json
  {
    "userType": "U",
    "authenticated": true,
    "password": "newpass",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Response:** 200 OK - UserResponseDto
- **Error Codes:** 400 (Invalid data), 404 (User not found), 500 (Internal error)

#### DELETE /api/users/{userId}
- **Summary:** Delete a user
- **Description:** Delete a user by user ID
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 204 No Content
- **Error Codes:** 404 (User not found), 500 (Internal error)

#### POST /api/users/authenticate
- **Summary:** Authenticate user
- **Description:** Authenticate a user with user ID and password
- **Request Body:**
  ```json
  {
    "userId": "USER001",
    "password": "pass123"
  }
  ```
- **Response:** 200 OK - UserResponseDto
- **Error Codes:** 401 (Invalid credentials), 500 (Internal error)

---

## 6. User List Page Management API

**Base Path:** `/api/user-list-pages`

### Endpoints

#### GET /api/user-list-pages
- **Summary:** Get all user list pages
- **Description:** Retrieve a paginated list of all user list pages
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<UserListPageResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/user-list-pages/{id}
- **Summary:** Get user list page by ID
- **Description:** Retrieve a user list page by its ID
- **Parameters:** 
  - `id` (path, required): Page ID
- **Response:** 200 OK - UserListPageResponseDto
- **Error Codes:** 404 (Page not found), 500 (Internal error)

#### GET /api/user-list-pages/page/{pageNumber}
- **Summary:** Get user list page by page number
- **Description:** Retrieve a user list page by its page number
- **Parameters:** 
  - `pageNumber` (path, required): Page number (>= 1)
- **Response:** 200 OK - UserListPageResponseDto
- **Error Codes:** 404 (Page not found), 500 (Internal error)

#### POST /api/user-list-pages
- **Summary:** Create a new user list page
- **Description:** Create a new user list page
- **Request Body:** CreateUserListPageRequestDto
  ```json
  {
    "pageNumber": 1,
    "firstUserId": "USER001",
    "lastUserId": "USER010",
    "hasNextPage": true,
    "recordsPerPage": 10
  }
  ```
- **Response:** 201 Created - UserListPageResponseDto
- **Error Codes:** 400 (Invalid data), 500 (Internal error)

#### PUT /api/user-list-pages/{id}
- **Summary:** Update an existing user list page
- **Description:** Update user list page details by ID
- **Parameters:** 
  - `id` (path, required): Page ID
- **Request Body:** UpdateUserListPageRequestDto
- **Response:** 200 OK - UserListPageResponseDto
- **Error Codes:** 400 (Invalid data), 404 (Page not found), 500 (Internal error)

#### DELETE /api/user-list-pages/{id}
- **Summary:** Delete a user list page
- **Description:** Delete a user list page by ID
- **Parameters:** 
  - `id` (path, required): Page ID
- **Response:** 204 No Content
- **Error Codes:** 404 (Page not found), 500 (Internal error)

---

## 7. User Selection Management API

**Base Path:** `/api/user-selections`

### Endpoints

#### GET /api/user-selections
- **Summary:** Get all user selections
- **Description:** Retrieve a paginated list of all user selections
- **Parameters:** 
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
- **Response:** 200 OK - Page<UserSelectionResponseDto>
- **Error Codes:** 400 (Invalid parameters), 500 (Internal error)

#### GET /api/user-selections/{id}
- **Summary:** Get user selection by ID
- **Description:** Retrieve a user selection by its ID
- **Parameters:** 
  - `id` (path, required): Selection ID
- **Response:** 200 OK - UserSelectionResponseDto
- **Error Codes:** 404 (Selection not found), 500 (Internal error)

#### GET /api/user-selections/user/{userId}
- **Summary:** Get user selection by user ID
- **Description:** Retrieve a user selection by user ID
- **Parameters:** 
  - `userId` (path, required): User ID
- **Response:** 200 OK - UserSelectionResponseDto
- **Error Codes:** 404 (Selection not found), 500 (Internal error)

#### GET /api/user-selections/updates
- **Summary:** Get all update selections
- **Description:** Retrieve all user selections marked for update
- **Response:** 200 OK - List<UserSelectionResponseDto>
- **Error Codes:** 500 (Internal error)

#### GET /api/user-selections/deletes
- **Summary:** Get all delete selections
- **Description:** Retrieve all user selections marked for deletion
- **Response:** 200 OK - List<UserSelectionResponseDto>
- **Error Codes:** 500 (Internal error)

#### POST /api/user-selections
- **Summary:** Create a new user selection
- **Description:** Create a new user selection
- **Request Body:** CreateUserSelectionRequestDto
  ```json
  {
    "selectionFlag": "U",
    "selectedUserId": "USER001"
  }
  ```
- **Response:** 201 Created - UserSelectionResponseDto
- **Error Codes:** 400 (Invalid data), 500 (Internal error)

#### PUT /api/user-selections/{id}
- **Summary:** Update an existing user selection
- **Description:** Update user selection details by ID
- **Parameters:** 
  - `id` (path, required): Selection ID
- **Request Body:** UpdateUserSelectionRequestDto
- **Response:** 200 OK - UserSelectionResponseDto
- **Error Codes:** 400 (Invalid data), 404 (Selection not found), 500 (Internal error)

#### DELETE /api/user-selections/{id}
- **Summary:** Delete a user selection
- **Description:** Delete a user selection by ID
- **Parameters:** 
  - `id` (path, required): Selection ID
- **Response:** 204 No Content
- **Error Codes:** 404 (Selection not found), 500 (Internal error)

---

## Common HTTP Status Codes

- **200 OK** - Request succeeded
- **201 Created** - Resource created successfully
- **204 No Content** - Request succeeded with no response body
- **400 Bad Request** - Invalid request data or parameters
- **401 Unauthorized** - Authentication failed
- **403 Forbidden** - Access denied
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error
- **503 Service Unavailable** - Feature not yet available (coming soon)

---

## Business Rules Implementation

### Admin Authentication (BR001)
- User must be authenticated as admin before accessing the admin menu
- Implemented in: `AdminUserService.validateAdminAccess()`
- Endpoints: POST `/api/admin-users/{userId}/validate-access`

### Menu Option Validation (BR002)
- User input must be a valid menu option number
- Implemented in: `AdminMenuOptionService.validateMenuOptionForExecution()`
- Endpoints: POST `/api/admin-menu-options/{optionNumber}/validate`

### Access Control by User Type (BR003)
- Enforces access control by preventing regular users from accessing admin-only options
- Implemented in: `MenuOptionService.validateMenuOptionAccess()`
- Endpoints: POST `/api/menu-options/{optionNumber}/validate-access`

### Coming Soon Feature Handling (BR005)
- Displays informational message for menu options that are not yet implemented
- Implemented in: `AdminMenuOptionService.validateMenuOptionForExecution()`
- Returns 503 status when option is not active

### User Authentication Check
- Verifies that the user is authenticated before allowing access to the main menu
- Implemented in: `UserService.authenticateUser()`
- Endpoints: POST `/api/users/authenticate`

---

## Database Schema

The application uses PostgreSQL with Flyway migrations. Seven migration files create the following tables:

1. **admin_users** (V1) - Administrative user accounts
2. **admin_menu_options** (V2) - Administrative menu options
3. **user_sessions** (V3) - Active user sessions
4. **menu_options** (V4) - General menu options
5. **users** (V5) - User accounts
6. **user_list_pages** (V6) - Paginated user lists
7. **user_selections** (V7) - User selection records

---

## Technology Stack

- **Framework:** Spring Boot 3.5.5
- **Language:** Java 21
- **Database:** PostgreSQL
- **Migration Tool:** Flyway
- **API Documentation:** OpenAPI 3.0 (Swagger)
- **Build Tool:** Maven
- **Logging:** SLF4J with Logback

---

## Getting Started

1. **Prerequisites:**
   - Java 21 or higher
   - Maven 3.6 or higher
   - PostgreSQL database

2. **Configuration:**
   - Update `application.properties` with your database credentials
   - Default port: 8080

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access API Documentation:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/v3/api-docs

---

## Generated Files Summary

**Total Files Generated:** 56

### By Entity (7 entities × 8 files each):
- **Entities:** 7 Java classes
- **DTOs:** 21 Java classes (3 per entity: Create, Update, Response)
- **Repositories:** 7 Java interfaces
- **Services:** 7 Java classes
- **Controllers:** 7 Java classes
- **Migrations:** 7 SQL files

### File Structure:
```
src/main/java/com/example/demo/
├── entity/           (7 files)
├── dto/              (21 files)
├── repository/       (7 files)
├── service/          (7 files)
└── controller/       (7 files)

src/main/resources/db/migration/
└── V1-V7__*.sql      (7 files)
```

---

## Notes

- All endpoints support pagination where applicable (default: 20 items per page)
- All timestamps are in ISO 8601 format
- All string fields are validated for length and format
- Password fields are validated but not encrypted in this implementation (add encryption in production)
- User IDs are limited to 8 characters as per business rules
- Option numbers are limited to 0-99 range
- Selection flags must be 'U' (update) or 'D' (delete)
- User types: 'A' (admin), 'U' (user), 'R' (regular user)

---

**Document Version:** 1.0  
**Generated:** 2024  
**Application:** CardDemo Login and User Management System
