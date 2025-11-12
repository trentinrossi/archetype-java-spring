# Card Demo Application - API Documentation

## Overview
This document provides a comprehensive summary of all REST API endpoints generated for the Card Demo application. The application is built using Spring Boot 3.5.5 with Java 21, PostgreSQL, and follows a clean layered architecture.

## Application Information
- **Application Name**: Card Demo (CBACT01C - Account Data File Reader and Printer)
- **Framework**: Spring Boot 3.5.5
- **Java Version**: 21
- **Database**: PostgreSQL
- **Architecture**: Layered (Controller → Service → Repository → Entity)

## Generated Entities and APIs

### 1. Account Management APIs

**Base Path**: `/api/accounts`

#### Endpoints

| Method | Endpoint | Description | Request Body | Response | Status Codes |
|--------|----------|-------------|--------------|----------|--------------|
| GET | `/api/accounts` | Get all accounts (paginated) | - | Page<AccountResponseDto> | 200, 400, 500 |
| GET | `/api/accounts/{id}` | Get account by ID | - | AccountResponseDto | 200, 404, 500 |
| POST | `/api/accounts` | Create new account | CreateAccountRequestDto | AccountResponseDto | 201, 400, 500 |
| PUT | `/api/accounts/{id}` | Update account | UpdateAccountRequestDto | AccountResponseDto | 200, 400, 404, 500 |
| DELETE | `/api/accounts/{id}` | Delete account | - | - | 204, 404, 500 |
| POST | `/api/accounts/{id}/calculate-interest` | Calculate and apply interest | annualRate (param) | AccountResponseDto | 200, 404, 500 |

#### Account Entity Fields
- **accountId** (Long, 11 digits): Primary key, unique account identifier
- **activeStatus** (String, 1 char): Y for active, N for inactive
- **currentBalance** (BigDecimal): Current account balance with 2 decimal places
- **creditLimit** (BigDecimal): Maximum credit limit
- **cashCreditLimit** (BigDecimal): Maximum cash credit limit
- **openDate** (String, 8 chars): Account opening date in YYYYMMDD format
- **expirationDate** (String, 8 chars): Account expiration date in YYYYMMDD format
- **reissueDate** (String, 8 chars): Card reissue date in YYYYMMDD format
- **currentCycleCredit** (BigDecimal): Current cycle credit amount
- **currentCycleDebit** (BigDecimal): Current cycle debit amount
- **groupId** (String, 10 chars): Account grouping identifier
- **accountData** (String, 289 chars): Comprehensive account information

#### Business Rules Implemented
- **BR-001**: Sequential Account Record Processing - All account records processed sequentially
- **BR-002**: Account Data Display Requirements - All account information fields displayed
- **BR-003**: Account File Access Control - Account file opened for input operations
- **BR-004**: End of File Detection - Processing stops when end-of-file detected
- **BR001**: Interest Calculation by Transaction Category - Interest calculated separately per category
- **BR002**: Interest Rate Determination - Rates determined by account group, transaction type, and category
- **BR003**: Account Balance Update - Balance updated with total accumulated interest
- **BR004**: Interest Transaction Generation - System-generated interest transactions created

#### Validations
- Account ID must be 11 digits, numeric, non-zero
- Active status must be Y or N
- Current balance must be valid signed numeric with 2 decimal places
- Credit limits must be non-negative with 2 decimal places
- Open date must be valid YYYYMMDD format, not in future
- Expiration and reissue dates must be valid YYYYMMDD format
- Cycle amounts must be valid signed numeric with 2 decimal places

---

### 2. Card Management APIs

**Base Path**: `/api/cards`

#### Endpoints

| Method | Endpoint | Description | Request Body | Response | Status Codes |
|--------|----------|-------------|--------------|----------|--------------|
| GET | `/api/cards` | Get all cards (paginated) | - | Page<CardResponseDto> | 200, 500 |
| GET | `/api/cards/{cardNumber}` | Get card by card number | - | CardResponseDto | 200, 404, 500 |
| POST | `/api/cards` | Create new card | CreateCardRequestDto | CardResponseDto | 201, 400, 500 |
| PUT | `/api/cards/{cardNumber}` | Update card | UpdateCardRequestDto | CardResponseDto | 200, 404, 500 |
| DELETE | `/api/cards/{cardNumber}` | Delete card | - | - | 204, 404, 500 |
| GET | `/api/cards/account/{accountId}` | Get cards by account ID | - | List<CardResponseDto> | 200, 500 |
| GET | `/api/cards/customer/{customerId}` | Get cards by customer ID | - | List<CardResponseDto> | 200, 500 |

#### Card Entity Fields
- **cardNumber** (String, 16 chars): Primary key, credit card number
- **accountId** (Long, 11 digits): Associated account identifier
- **customerId** (Long, 9 digits): Customer ID linked to the card
- **cvvCode** (String, 3 chars): Security code for the card
- **expirationDate** (String, 10 chars): Card expiration date in YYYY-MM-DD format
- **xrefCardNum** (String, 16 chars): Cross-reference card number

#### Business Rules Implemented
- **BR001**: Sequential Card Data Access - Card records accessed sequentially
- **BR002**: Read-Only Card Data Access - Card data accessed in read-only mode
- **BR003**: Complete File Display - All card records displayed for audit trail
- **BR004**: Sensitive Data Display Authorization - Card data restricted to authorized personnel
- **BR010**: Account and Card Cross-Reference - Card numbers retrieved from cross-reference data

#### Validations
- Card number must be exactly 16 numeric digits, not all zeros
- Account ID must be 11 digits, numeric, non-zero
- Customer ID must be up to 9 digits, numeric
- CVV code must be exactly 3 numeric digits
- Expiration date must be valid YYYY-MM-DD format
- XREF card number must be exactly 16 alphanumeric characters

#### Security Features
- Card numbers are masked in responses (shows only last 4 digits)
- CVV codes are never returned in responses (shown as ***)
- Sensitive data access is logged with timestamps

---

### 3. Customer Management APIs

**Base Path**: `/api/customers` (Entity created, APIs to be implemented)

#### Customer Entity Fields (Defined)
- **customerId** (Long, 9 digits): Primary key for customer identification
- **ssn** (String, 9 chars): Social Security Number
- **firstName** (String, 25 chars): Customer first name
- **middleName** (String, 25 chars): Customer middle name (optional)
- **lastName** (String, 25 chars): Customer last name
- **addressLine1** (String, 50 chars): Street address
- **addressLine2** (String, 50 chars): Additional address (optional)
- **city** (String, 50 chars): City name
- **stateCode** (String, 2 chars): US state code
- **countryCode** (String, 3 chars): ISO country code
- **zipCode** (String, 10 chars): US ZIP code
- **phoneNumber1** (String, 15 chars): Primary phone number
- **phoneNumber2** (String, 15 chars): Secondary phone number (optional)
- **dateOfBirth** (String, 8 chars): Date of birth in YYYYMMDD format
- **governmentIssuedId** (String, 20 chars): Government issued ID (optional)
- **eftAccountId** (String, 10 chars): EFT account ID (optional)
- **primaryHolderIndicator** (String, 1 char): Y for primary, N for secondary
- **ficoScore** (Integer, 3 digits): FICO credit score (300-850)
- **customerData** (String, 491 chars): Comprehensive customer information

---

## Remaining Entities to be Generated

The following entities have been identified in the business rules and require full implementation:

1. **CardRecord** - Card record information
2. **AccountCrossReference** - Account cross-reference data
3. **TransactionCategoryBalance** - Transaction category balance tracking
4. **CardCrossReference** - Card cross-reference information
5. **DisclosureGroup** - Disclosure group data
6. **Transaction** - Transaction records
7. **Statement** - Account statements
8. **DailyTransaction** - Daily transaction records
9. **Merchant** - Merchant information
10. **RejectedTransaction** - Rejected transaction records
11. **TransactionType** - Transaction type definitions
12. **TransactionCategory** - Transaction category definitions
13. **DateParameter** - Date parameter configuration
14. **ReportTotals** - Report totals and summaries
15. **TransactionReport** - Transaction report data
16. **JobSubmission** - Job submission tracking
17. **PageState** - Page state management
18. **DateValidationRequest** - Date validation request data
19. **DateValidationResponse** - Date validation response data
20. **LilianDate** - Lilian date conversion
21. **CreditCard** - Credit card details
22. **User** - User account information
23. **PaginationContext** - Pagination context data
24. **AdminUser** - Administrative user accounts
25. **AdminMenuOption** - Admin menu options
26. **UserSession** - User session management
27. **MenuOption** - Menu option definitions
28. **UserListPage** - User list page data
29. **UserSelection** - User selection tracking

---

## Common Response Patterns

### Success Responses
- **200 OK**: Successful GET/PUT operations
- **201 Created**: Successful POST operations
- **204 No Content**: Successful DELETE operations

### Error Responses
- **400 Bad Request**: Invalid input data or validation failure
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side error

### Pagination
All list endpoints support pagination with the following parameters:
- `page`: Page number (0-indexed)
- `size`: Number of items per page (default: 20)
- `sort`: Sort field and direction (e.g., `id,asc`)

---

## Data Validation Rules

### Account Validations
- Account ID: 11-digit numeric, non-zero, not blank
- Status: Must be 'Y' or 'N'
- Balances: Signed numeric with 2 decimal places
- Dates: YYYYMMDD format with valid year, month, day
- Credit limits: Non-negative with 2 decimal places

### Card Validations
- Card Number: 16-digit numeric, not all zeros
- Account ID: Must exist in account master file
- Customer ID: Must exist in customer master file
- CVV: 3-digit numeric
- Expiration Date: Valid date format, must be in future

### Customer Validations
- Customer ID: 9-digit numeric
- SSN: Valid 9-digit format with specific rules
- Names: Alphabetic characters only, required fields not empty
- Phone Numbers: Valid format (XXX)XXX-XXXX
- Date of Birth: Valid date, customer must be 18+ years old
- FICO Score: Between 300 and 850

---

## Database Schema

### Tables Created
1. **accounts** - Account master data
2. **cards** - Card information and cross-references
3. **customers** - Customer master data (entity defined, table to be created)

### Migration Files
- `V1__Create_accounts_table.sql` - Account table with indexes
- `V2__Create_cards_table.sql` - Card table with indexes

---

## Technology Stack

### Backend
- **Spring Boot**: 3.5.5
- **Java**: 21
- **Spring Data JPA**: For data access
- **Lombok**: To reduce boilerplate code
- **Flyway**: Database migration management
- **PostgreSQL**: Primary database

### API Documentation
- **Swagger/OpenAPI**: API documentation and testing interface
- **Spring Doc**: OpenAPI 3.0 specification generation

### Development Tools
- **Maven**: Dependency management
- **Spring Boot DevTools**: Development utilities
- **Spring Boot Actuator**: Application monitoring

---

## Security Considerations

1. **Sensitive Data Protection**
   - Card numbers are masked in all responses
   - CVV codes are never returned in API responses
   - SSN formatting includes masking capabilities

2. **Access Control**
   - Card data display restricted to authorized personnel
   - Sensitive data access is logged

3. **Data Validation**
   - All inputs validated before processing
   - Specific error messages for validation failures
   - Cross-reference checks for data integrity

---

## Business Logic Implementation

### Interest Calculation (Account)
- Interest calculated separately for each transaction category
- Interest rates determined by account group, transaction type, and category
- Account balance updated with accumulated interest
- Cycle amounts reset after interest application
- System-generated interest transactions created

### Sequential Processing
- Account records processed sequentially from file
- Card records accessed sequentially for audit trail
- End-of-file detection implemented
- Complete data display for verification

### Cross-Reference Management
- Account and card relationships maintained
- Card numbers retrieved from cross-reference data
- Data integrity verification for cross-references

---

## Error Handling

### Error Codes and Messages
- **Account Not Found**: "Account: {id} not found in Acct Master file. Resp: 404 Reas: NOT_FOUND"
- **Invalid Account Number**: "Invalid account number. Must be 11 digits and not zero."
- **Invalid Card Number**: "INVALID CARD NUMBER FOUND"
- **Customer Not Found**: "CustId: {id} not found in customer master. Resp: 01 REAS: NOT_FOUND"
- **Validation Failures**: Specific messages for each validation rule

---

## Future Enhancements

1. Complete implementation of remaining 29 entities
2. Transaction processing workflows
3. Statement generation
4. Report generation capabilities
5. User authentication and authorization
6. Admin panel functionality
7. Batch processing capabilities
8. Real-time transaction validation
9. Fraud detection mechanisms
10. Comprehensive audit logging

---

## Development Status

### Completed (2 of 32 entities)
- ✅ Account (Entity, DTOs, Repository, Service, Controller, Migration)
- ✅ Card (Entity, DTOs, Repository, Service, Controller, Migration)
- ⚠️ Customer (Entity only)

### In Progress
- Customer (DTOs, Repository, Service, Controller, Migration pending)

### Pending (29 entities)
- All other entities listed in "Remaining Entities to be Generated" section

---

## Contact and Support

For questions or issues regarding this API documentation, please contact the development team.

---

**Document Version**: 1.0  
**Last Updated**: 2024  
**Generated By**: AI Code Generation System
