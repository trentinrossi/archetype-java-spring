# Credit Card Management API - OpenAPI Summary

## Application Overview

**Application Name:** COCRDSLC - Credit Card Detail Request Processing  
**Version:** 1.0.0  
**Framework:** Spring Boot 3.5.5 with Java 21  
**Database:** PostgreSQL with Flyway migrations  
**Base URL:** `/api`

This API provides comprehensive credit card management functionality with robust authorization controls, validation rules, and business logic implementation.

---

## Table of Contents

1. [Account Management APIs](#account-management-apis)
2. [Customer Management APIs](#customer-management-apis)
3. [User Management APIs](#user-management-apis)
4. [Credit Card Management APIs](#credit-card-management-apis)
5. [Data Models](#data-models)
6. [Business Rules Implementation](#business-rules-implementation)
7. [Error Codes](#error-codes)

---

## Account Management APIs

### Base Path: `/api/accounts`

#### 1. Get All Accounts
- **Endpoint:** `GET /api/accounts`
- **Description:** Retrieve a paginated list of all accounts
- **Parameters:**
  - `page` (query, optional): Page number (default: 0)
  - `size` (query, optional): Page size (default: 20)
  - `sort` (query, optional): Sort criteria
- **Response:** `200 OK`
  ```json
  {
    "content": [
      {
        "accountId": 12345678901,
        "formattedAccountId": "123-4567-8901",
        "creditCardCount": 3,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00"
      }
    ],
    "pageable": {...},
    "totalElements": 100,
    "totalPages": 5
  }
  ```
- **Error Responses:**
  - `400 Bad Request`: Invalid request parameters
  - `500 Internal Server Error`: Server error

#### 2. Get Account by ID
- **Endpoint:** `GET /api/accounts/{id}`
- **Description:** Retrieve an account by its 11-digit account ID
- **Parameters:**
  - `id` (path, required): 11-digit account ID
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Invalid account ID format (must be 11 digits)
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

#### 3. Create Account
- **Endpoint:** `POST /api/accounts`
- **Description:** Create a new credit card account
- **Request Body:**
  ```json
  {
    "accountId": "12345678901"
  }
  ```
- **Validations:**
  - Account ID must be exactly 11 numeric digits
  - Account ID cannot be zero or all zeros
  - Account ID must not already exist
- **Response:** `201 Created`
- **Error Responses:**
  - `400 Bad Request`: Invalid account ID format or validation failure
  - `500 Internal Server Error`: Server error

#### 4. Update Account
- **Endpoint:** `PUT /api/accounts/{id}`
- **Description:** Update account details
- **Parameters:**
  - `id` (path, required): Account ID
- **Request Body:**
  ```json
  {
    "accountId": "12345678901"
  }
  ```
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

#### 5. Delete Account
- **Endpoint:** `DELETE /api/accounts/{id}`
- **Description:** Delete an account
- **Parameters:**
  - `id` (path, required): Account ID
- **Response:** `204 No Content`
- **Error Responses:**
  - `400 Bad Request`: Invalid account ID
  - `404 Not Found`: Account not found
  - `500 Internal Server Error`: Server error

#### 6. Search Accounts
- **Endpoint:** `GET /api/accounts/search`
- **Description:** Search for accounts using search term (BR003: Search Criteria Requirement)
- **Parameters:**
  - `searchTerm` (query, required): Search term for account ID
  - `page`, `size`, `sort` (query, optional): Pagination parameters
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Search term required
  - `500 Internal Server Error`: Server error

---

## Customer Management APIs

### Base Path: `/api/customers`

#### 1. Get All Customers
- **Endpoint:** `GET /api/customers`
- **Description:** Retrieve a paginated list of all customers
- **Parameters:** Standard pagination parameters
- **Response:** `200 OK`
  ```json
  {
    "content": [
      {
        "id": 1,
        "customerId": "CUST001",
        "firstName": "John",
        "lastName": "Doe",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "+1234567890",
        "accountCount": 2,
        "creditCardCount": 3,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00"
      }
    ]
  }
  ```

#### 2. Get Customer by ID
- **Endpoint:** `GET /api/customers/{id}`
- **Description:** Retrieve a customer by internal ID
- **Response:** `200 OK`
- **Error Responses:** `404 Not Found`

#### 3. Get Customer by Customer ID
- **Endpoint:** `GET /api/customers/customer-id/{customerId}`
- **Description:** Retrieve a customer by unique customer ID
- **Response:** `200 OK`
- **Error Responses:** `404 Not Found`

#### 4. Create Customer
- **Endpoint:** `POST /api/customers`
- **Description:** Create a new customer
- **Request Body:**
  ```json
  {
    "customerId": "CUST001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890"
  }
  ```
- **Validations:**
  - Customer ID is required (max 20 characters)
  - First name and last name are required (max 100 characters each)
  - Email must be valid format (max 255 characters)
  - Phone number must be valid format (10-20 digits)
- **Response:** `201 Created`

#### 5. Update Customer
- **Endpoint:** `PUT /api/customers/{id}`
- **Description:** Update customer details
- **Response:** `200 OK`

#### 6. Delete Customer
- **Endpoint:** `DELETE /api/customers/{id}`
- **Description:** Delete a customer
- **Response:** `204 No Content`

#### 7. Search Customers
- **Endpoint:** `GET /api/customers/search`
- **Description:** Search customers by name, customer ID, or email
- **Parameters:**
  - `searchTerm` (query, required): Search term
- **Response:** `200 OK`

---

## User Management APIs

### Base Path: `/api/users`

#### 1. Get All Users
- **Endpoint:** `GET /api/users`
- **Description:** Retrieve a paginated list of all users
- **Response:** `200 OK`
  ```json
  {
    "content": [
      {
        "id": 1,
        "userId": "USER001",
        "userType": "REGULAR",
        "userTypeDisplayName": "Regular",
        "username": "johndoe",
        "email": "john.doe@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "fullName": "John Doe",
        "isAdmin": false,
        "canViewAllCards": false,
        "accountCount": 2,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00"
      }
    ]
  }
  ```

#### 2. Get User by ID
- **Endpoint:** `GET /api/users/{id}`
- **Description:** Retrieve a user by internal ID
- **Response:** `200 OK`

#### 3. Get User by User ID
- **Endpoint:** `GET /api/users/user-id/{userId}`
- **Description:** Retrieve a user by unique user ID
- **Response:** `200 OK`

#### 4. Get User by Username
- **Endpoint:** `GET /api/users/username/{username}`
- **Description:** Retrieve a user by username
- **Response:** `200 OK`

#### 5. Get Users by Type
- **Endpoint:** `GET /api/users/type/{userType}`
- **Description:** Retrieve users filtered by type (ADMIN or REGULAR)
- **Parameters:**
  - `userType` (path, required): ADMIN or REGULAR
- **Response:** `200 OK`

#### 6. Create User
- **Endpoint:** `POST /api/users`
- **Description:** Create a new system user
- **Request Body:**
  ```json
  {
    "userId": "USER001",
    "userType": "REGULAR",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Validations:**
  - User ID is required (max 20 characters)
  - User type is required (ADMIN or REGULAR)
  - Username is required (max 50 characters)
  - Email must be valid format
- **Response:** `201 Created`

#### 7. Update User
- **Endpoint:** `PUT /api/users/{id}`
- **Description:** Update user details
- **Response:** `200 OK`

#### 8. Delete User
- **Endpoint:** `DELETE /api/users/{id}`
- **Description:** Delete a user
- **Response:** `204 No Content`

#### 9. Search Users
- **Endpoint:** `GET /api/users/search`
- **Description:** Search users by username, user ID, first name, or last name
- **Parameters:**
  - `searchTerm` (query, required): Search term
- **Response:** `200 OK`

#### 10. Check User Authorization
- **Endpoint:** `GET /api/users/{userId}/can-view-account/{accountId}`
- **Description:** Check if a user can view a specific account (BR001: User Authorization for Card Viewing)
- **Parameters:**
  - `userId` (path, required): User ID
  - `accountId` (path, required): Account ID
- **Response:** `200 OK`
  ```json
  true
  ```
- **Business Rule:** Admin users can view all accounts, regular users can only view their own

---

## Credit Card Management APIs

### Base Path: `/api/credit-cards`

#### 1. Get All Credit Cards
- **Endpoint:** `GET /api/credit-cards`
- **Description:** Retrieve a paginated list of all credit cards
- **Response:** `200 OK`
  ```json
  {
    "content": [
      {
        "cardNumber": "1234567890123456",
        "formattedCardNumber": "1234 5678 9012 3456",
        "maskedCardNumber": "**** **** **** 3456",
        "accountId": 12345678901,
        "embossedName": "JOHN DOE",
        "expirationDate": "2025-12-31",
        "expirationDateFormatted": "12/2025",
        "expiryMonth": "12",
        "expiryYear": "2025",
        "expirationMonth": 12,
        "expirationYear": 2025,
        "expirationDay": 31,
        "activeStatus": "Y",
        "cardStatus": "Y",
        "isActive": true,
        "isExpired": false,
        "cvvCode": "***",
        "version": 1,
        "lastModifiedBy": "USER001",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00"
      }
    ]
  }
  ```

#### 2. Get Credit Card by Card Number
- **Endpoint:** `GET /api/credit-cards/{cardNumber}`
- **Description:** Retrieve a credit card by its 16-digit card number (BR004: Card Record Retrieval)
- **Parameters:**
  - `cardNumber` (path, required): 16-digit card number
- **Validations:**
  - Card number must be exactly 16 numeric digits
  - Card number cannot be all zeros
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Invalid card number format
  - `404 Not Found`: Credit card not found
  - `500 Internal Server Error`: Server error

#### 3. Get Credit Cards by Account ID
- **Endpoint:** `GET /api/credit-cards/account/{accountId}`
- **Description:** Retrieve all credit cards for a specific account
- **Parameters:**
  - `accountId` (path, required): 11-digit account ID
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Invalid account ID
  - `404 Not Found`: Account not found

#### 4. Create Credit Card
- **Endpoint:** `POST /api/credit-cards`
- **Description:** Create a new credit card
- **Request Body:**
  ```json
  {
    "cardNumber": "1234567890123456",
    "accountId": 12345678901,
    "embossedName": "JOHN DOE",
    "expirationDate": "2025-12-31",
    "cardStatus": "Y",
    "cvvCode": "123"
  }
  ```
- **Validations:**
  - Card number: 16 digits, numeric, not all zeros
  - Account ID: 11 digits, numeric, not all zeros, must exist
  - Embossed name: Alphabets and spaces only (max 50 characters)
  - Expiration date: Required, valid date format
  - Card status: Must be 'Y' or 'N'
  - CVV code: 3 digits, numeric
- **Response:** `201 Created`
- **Error Responses:**
  - `400 Bad Request`: Validation failure
  - `500 Internal Server Error`: Server error

#### 5. Update Credit Card
- **Endpoint:** `PUT /api/credit-cards/{cardNumber}`
- **Description:** Update credit card details (BR002: Card Detail Modification)
- **Headers:**
  - `X-User-ID` (optional): User ID making the update
- **Request Body:**
  ```json
  {
    "embossedName": "JOHN DOE",
    "expirationDate": "2026-12-31",
    "cardStatus": "Y",
    "version": 1,
    "updatedBy": "USER001"
  }
  ```
- **Business Rules:**
  - **BR002:** Only embossed name, card status, and expiration date can be modified
  - **BR003:** Concurrent Update Prevention - version field used for optimistic locking
  - **BR004:** Update Confirmation Requirement - explicit confirmation required
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Invalid request data
  - `404 Not Found`: Credit card not found
  - `409 Conflict`: Concurrent update detected (card modified by another user)
  - `500 Internal Server Error`: Server error

#### 6. Delete Credit Card
- **Endpoint:** `DELETE /api/credit-cards/{cardNumber}`
- **Description:** Delete a credit card
- **Parameters:**
  - `cardNumber` (path, required): 16-digit card number
- **Response:** `204 No Content`
- **Error Responses:**
  - `400 Bad Request`: Invalid card number
  - `404 Not Found`: Credit card not found

#### 7. Search Credit Cards
- **Endpoint:** `GET /api/credit-cards/search`
- **Description:** Search for credit cards (BR001: Card Search and Retrieval, BR003: Search Criteria Requirement)
- **Headers:**
  - `X-User-ID` (required): User ID performing the search
- **Parameters:**
  - `accountId` (query, optional): 11-digit account ID
  - `cardNumber` (query, optional): 16-digit card number
  - `page`, `size`, `sort` (query, optional): Pagination parameters
- **Business Rules:**
  - **BR003:** At least one search criterion must be provided
  - **BR001:** User Authorization - Admin users see all cards, regular users see only their own
  - **BR008:** Record Filtering Logic applied
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: No search criteria provided or invalid format
  - `403 Forbidden`: User not authorized to view requested cards
  - `500 Internal Server Error`: Server error

#### 8. Get Card Details with Authorization
- **Endpoint:** `GET /api/credit-cards/details`
- **Description:** Retrieve specific card details with authorization check (BR001: Card Search and Retrieval)
- **Headers:**
  - `X-User-ID` (required): User ID
- **Parameters:**
  - `accountId` (query, required): 11-digit account ID
  - `cardNumber` (query, required): 16-digit card number
- **Business Rules:**
  - **BR001:** Both account ID and card number must be provided
  - **BR001:** User authorization enforced (admin can view all, regular users only their own)
  - **BR004:** Card record retrieval using card number as primary key
- **Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Missing required parameters
  - `403 Forbidden`: Access denied
  - `404 Not Found`: Account/card combination not found
  - `500 Internal Server Error`: Server error

#### 9. Return from List Screen
- **Endpoint:** `GET /api/credit-cards/return-from-list`
- **Description:** Handle navigation when returning from card list screen (BR006: Return from List Screen)
- **Headers:**
  - `X-User-ID` (required): User ID
- **Parameters:**
  - `accountId` (query, optional): Previous account ID search value
  - `cardNumber` (query, optional): Previous card number search value
  - Pagination parameters
- **Business Rule:** Maintains previous search criteria when returning from list
- **Response:** `200 OK`

---

## Data Models

### Account Model
```json
{
  "accountId": 12345678901,
  "formattedAccountId": "123-4567-8901",
  "creditCardCount": 3,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**Validations:**
- Account ID: Exactly 11 numeric digits, not zero, not all zeros

### Customer Model
```json
{
  "id": 1,
  "customerId": "CUST001",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "accountCount": 2,
  "creditCardCount": 3,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**Validations:**
- Customer ID: Required, max 20 characters
- First Name: Required, max 100 characters
- Last Name: Required, max 100 characters
- Email: Valid email format, max 255 characters
- Phone Number: Valid phone format, 10-20 digits

### User Model
```json
{
  "id": 1,
  "userId": "USER001",
  "userType": "REGULAR",
  "userTypeDisplayName": "Regular",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "isAdmin": false,
  "canViewAllCards": false,
  "accountCount": 2,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**User Types:**
- `ADMIN`: Can view all credit cards without restrictions
- `REGULAR`: Can only view cards associated with their account

**Validations:**
- User ID: Required, max 20 characters
- User Type: Required (ADMIN or REGULAR)
- Username: Required, max 50 characters, unique
- Email: Valid email format, unique

### Credit Card Model
```json
{
  "cardNumber": "1234567890123456",
  "formattedCardNumber": "1234 5678 9012 3456",
  "maskedCardNumber": "**** **** **** 3456",
  "accountId": 12345678901,
  "embossedName": "JOHN DOE",
  "expirationDate": "2025-12-31",
  "expirationDateFormatted": "12/2025",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "expirationMonth": 12,
  "expirationYear": 2025,
  "expirationDay": 31,
  "activeStatus": "Y",
  "cardStatus": "Y",
  "isActive": true,
  "isExpired": false,
  "cvvCode": "***",
  "version": 1,
  "lastModifiedBy": "USER001",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**Validations:**
- Card Number: Exactly 16 numeric digits, not all zeros
- Account ID: Exactly 11 numeric digits, must exist
- Embossed Name: Alphabets and spaces only, max 50 characters
- Expiration Date: Required, valid date
- Card Status: Must be 'Y' (active) or 'N' (inactive)
- Expiration Month: 1-12
- Expiration Year: 1950-2099
- CVV Code: Exactly 3 numeric digits

### PageContext Model
```json
{
  "screenNumber": 1,
  "firstCardNumber": "1234567890123456",
  "lastCardNumber": "1234567890123999",
  "nextPageIndicator": "Y",
  "lastPageDisplayed": 9
}
```

**Usage:** Maintains pagination state for card listing (BR006: Page Navigation Control)

---

## Business Rules Implementation

### BR001: Account Number Validation
- **Implementation:** Account Service, Entity validation
- **Rule:** Account number must be a valid 11-digit numeric value when provided
- **Validation:** Not zero, not all zeros, exactly 11 digits
- **Error Message:** "ACCOUNT NUMBER MUST BE 11 DIGITS" or "Please enter a valid account number"

### BR001: Card Search and Retrieval
- **Implementation:** Credit Card Service, Controller
- **Rule:** Users must provide both account ID and card number to search for and retrieve card details
- **Endpoints:** `GET /api/credit-cards/details`
- **Validation:** Both parameters required

### BR001: User Authorization for Card Viewing
- **Implementation:** User Service, Credit Card Service
- **Rule:** Controls which credit cards a user can view based on their user type
- **Logic:**
  - Admin users: Can view all credit cards without restrictions
  - Regular users: Can only view cards associated with their account
- **Enforcement:** Applied in search and retrieval operations

### BR002: Card Detail Modification
- **Implementation:** Credit Card Service, Entity
- **Rule:** Users can modify specific card fields including name, status, and expiration date
- **Modifiable Fields:**
  - `embossedName`
  - `cardStatus`
  - `expirationDate`
- **Non-modifiable Fields:** Card number, account ID, CVV code

### BR003: Concurrent Update Prevention
- **Implementation:** Credit Card Entity (optimistic locking)
- **Rule:** System prevents data loss from concurrent updates by checking if record was modified by another user
- **Mechanism:** Version field with `@Version` annotation
- **Error Response:** `409 Conflict` when concurrent modification detected

### BR003: Search Criteria Requirement
- **Implementation:** Account Service, Credit Card Service
- **Rule:** At least one search criterion must be provided to perform a search
- **Validation:** Checks that either account ID or card number is provided
- **Error Message:** "At least one search criterion must be provided"

### BR003: Single Selection Enforcement
- **Implementation:** Credit Card Service
- **Rule:** Ensures only one credit card can be selected for action at a time
- **Validation:** Validates selection list size

### BR004: Account Number Filter Validation
- **Implementation:** Account Service
- **Rule:** Validates account number format when used as a filter
- **Validation:** If provided, must be 11 digits, numeric, not zero
- **Error Message:** "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER"

### BR004: Card Record Retrieval
- **Implementation:** Credit Card Repository, Service
- **Rule:** Retrieve credit card details using card number as the primary key
- **Endpoint:** `GET /api/credit-cards/{cardNumber}`

### BR004: Update Confirmation Requirement
- **Implementation:** Controller layer (UI responsibility)
- **Rule:** All card updates require explicit user confirmation before being committed to the database
- **Note:** Confirmation handled by frontend/UI layer

### BR006: Return from List Screen
- **Implementation:** Credit Card Controller, Service
- **Rule:** Handle navigation when returning from the card list screen
- **Endpoint:** `GET /api/credit-cards/return-from-list`
- **Behavior:** Maintains previous search criteria (account ID, card number)

### BR006: Page Navigation Control
- **Implementation:** PageContext DTO
- **Rule:** Controls forward and backward navigation through paginated card lists
- **Features:**
  - Track current page number
  - Identify first and last card on page
  - Indicate if more pages exist
  - Support backward/forward navigation

### BR007: Input Error Highlighting
- **Implementation:** Validation annotations, Service layer
- **Rule:** Highlight fields with validation errors in red and position cursor appropriately
- **Mechanism:** Detailed error messages with field names
- **Note:** Visual highlighting handled by frontend

### BR008: Record Filtering Logic
- **Implementation:** Credit Card Service
- **Rule:** Applies account and card number filters to retrieved records
- **Logic:** Filters applied based on provided search criteria

### BR012: Input Error Handling
- **Implementation:** Service layer validation
- **Rule:** Handles validation errors in user input and prevents further processing
- **Mechanism:** Throws `IllegalArgumentException` with detailed error messages

---

## Error Codes

### HTTP Status Codes

#### 200 OK
- Successful GET request
- Successful PUT request

#### 201 Created
- Successful POST request (resource created)

#### 204 No Content
- Successful DELETE request

#### 400 Bad Request
- Invalid request parameters
- Validation failure
- Missing required fields
- Invalid format (card number, account ID, etc.)
- Search criteria not provided

**Common Error Messages:**
- "ACCOUNT NUMBER MUST BE 11 DIGITS"
- "CARD NUMBER MUST BE 16 DIGITS"
- "Please enter a valid account number"
- "Please enter a valid card number"
- "Card status must be Y (active) or N (inactive)"
- "Card name must contain only alphabets and spaces"
- "Expiration month must be between 1 and 12"
- "Expiration year must be between 1950 and 2099"
- "At least one search criterion must be provided"
- "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER"

#### 403 Forbidden
- User not authorized to view requested resource
- Access denied for regular users trying to view other accounts' cards

**Error Message:**
- "Access denied: You can only view cards associated with your account"

#### 404 Not Found
- Resource not found (account, customer, user, credit card)
- Account/card combination not found

**Error Messages:**
- "Account not found"
- "Customer not found"
- "User not found"
- "Credit card not found"
- "Account/card combination not found"

#### 409 Conflict
- Concurrent update detected (optimistic locking failure)
- Resource already exists

**Error Message:**
- "Concurrent update detected - card was modified by another user"
- "Account with ID already exists"
- "Credit card with this number already exists"

#### 500 Internal Server Error
- Unexpected server error
- Database error
- Technical error during processing

**Error Message:**
- "Internal server error"
- "Technical error occurred while retrieving card details"

---

## Authentication & Authorization

### Headers

#### X-User-ID
- **Type:** String
- **Required:** For credit card search and modification operations
- **Description:** User ID of the authenticated user
- **Usage:** Used for authorization checks and audit trails

### Authorization Rules

1. **Admin Users (UserType.ADMIN)**
   - Can view all credit cards
   - Can view all accounts
   - No restrictions on search operations

2. **Regular Users (UserType.REGULAR)**
   - Can only view cards associated with their own account
   - Cannot view other users' cards
   - Search results filtered by user's account

---

## Pagination

All list endpoints support pagination with the following parameters:

- `page` (default: 0): Page number (zero-based)
- `size` (default: 20): Number of items per page
- `sort` (optional): Sort criteria (e.g., "createdAt,desc")

**Response Format:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {...}
  },
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true,
  "numberOfElements": 20
}
```

---

## Database Schema

### Tables

1. **accounts**
   - Primary Key: `account_id` (BIGINT, 11 digits)
   - Foreign Keys: `customer_id`, `user_id`
   - Indexes: account_id, customer_id, user_id

2. **customers**
   - Primary Key: `id` (BIGSERIAL)
   - Unique: `customer_id`, `email`
   - Indexes: customer_id, email, name

3. **users**
   - Primary Key: `id` (BIGSERIAL)
   - Unique: `user_id`, `username`, `email`
   - Indexes: user_id, username, email, user_type

4. **credit_cards**
   - Primary Key: `card_number` (VARCHAR(16))
   - Foreign Keys: `account_id`, `customer_id`
   - Indexes: account_id, customer_id, card_status, expiration_date, embossed_name
   - Composite Index: (account_id, card_number)
   - Constraints: Check constraints for card_number length, card_status values, expiration ranges

---

## Security Considerations

1. **Card Number Masking:** Card numbers are masked in responses (e.g., "**** **** **** 3456")
2. **CVV Protection:** CVV codes are always masked in responses ("***")
3. **Optimistic Locking:** Version field prevents concurrent update conflicts
4. **User Authorization:** Enforced at service layer for all card operations
5. **Input Validation:** Comprehensive validation at DTO and entity levels
6. **Audit Trail:** `lastModifiedBy` field tracks who made changes

---

## Development Notes

### Technology Stack
- **Java:** 21
- **Spring Boot:** 3.5.5
- **Database:** PostgreSQL
- **Migration Tool:** Flyway
- **Build Tool:** Maven
- **Lombok:** For reducing boilerplate code
- **OpenAPI:** For API documentation

### Code Structure
```
src/main/java/com/example/demo/
├── controller/       # REST API endpoints
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── enums/           # Enum definitions
├── repository/      # Data access layer
└── service/         # Business logic layer

src/main/resources/
└── db/migration/    # Flyway migration scripts
```

### Best Practices Implemented
1. Separation of concerns (layered architecture)
2. DTO pattern (never expose entities directly)
3. Bean validation annotations
4. Comprehensive error handling
5. Logging at all layers
6. Transaction management
7. Optimistic locking for concurrency control
8. Database constraints and indexes

---

## Contact & Support

For API support or questions, please contact the development team.

**Version:** 1.0.0  
**Last Updated:** 2024-01-20
