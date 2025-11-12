# Card Demo Application - API Summary

## Overview

This document provides a comprehensive summary of the REST APIs generated for the Card Demo Application. The application is built using Spring Boot 3.5.5 with Java 21, following a clean layered architecture pattern.

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Java Version**: Java 21
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Migration Tool**: Flyway
- **API Documentation**: OpenAPI/Swagger
- **Build Tool**: Maven

## Architecture

The application follows a clean layered architecture:

1. **Controller Layer**: REST API endpoints
2. **Service Layer**: Business logic implementation
3. **Repository Layer**: Data access layer
4. **Entity Layer**: JPA entities (database models)
5. **DTO Layer**: Data Transfer Objects for API communication

## Generated Entities

The following 32 entities have been generated with complete CRUD operations:

### Core Business Entities

1. **Account** - Customer account with financial and status information
2. **Card** - Credit card information linked to accounts
3. **Customer** - Customer personal and contact information
4. **Transaction** - Transaction records including interest transactions
5. **User** - System users with authentication

### Cross-Reference Entities

6. **CardCrossReference** - Links cards to accounts and customers
7. **AccountCrossReference** - Account cross-reference data

### Transaction Management

8. **TransactionType** - Transaction type reference data
9. **TransactionCategory** - Transaction category reference data
10. **TransactionCategoryBalance** - Balance by transaction category
11. **DailyTransaction** - Daily transaction records
12. **RejectedTransaction** - Failed transactions with rejection reasons

### Financial Entities

13. **Statement** - Account statements
14. **DisclosureGroup** - Interest rate information
15. **Merchant** - Merchant information

### Reporting Entities

16. **DateParameter** - Date range parameters for reports
17. **ReportTotals** - Report totals accumulation
18. **TransactionReport** - Transaction report requests
19. **JobSubmission** - Batch job submissions

### Pagination & State Management

20. **PageState** - Transaction list pagination state
21. **PaginationContext** - Card listing pagination
22. **UserListPage** - User list pagination

### Administrative Entities

23. **AdminUser** - Administrative users
24. **AdminMenuOption** - Admin menu options
25. **MenuOption** - User menu options
26. **UserSession** - User session information
27. **UserSelection** - User selection actions

### Utility Entities

28. **DateValidationRequest** - Date validation input
29. **DateValidationResponse** - Date validation output
30. **LilianDate** - Internal date representation
31. **CreditCard** - Credit card with account association
32. **CardRecord** - Complete card record data

## API Endpoints

### Account Management API

**Base Path**: `/api/accounts`

#### Standard CRUD Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/accounts` | Get all accounts (paginated) | - | Page<AccountResponseDto> |
| GET | `/api/accounts/{id}` | Get account by ID | - | AccountResponseDto |
| POST | `/api/accounts` | Create new account | CreateAccountRequestDto | AccountResponseDto |
| PUT | `/api/accounts/{id}` | Update account | UpdateAccountRequestDto | AccountResponseDto |
| DELETE | `/api/accounts/{id}` | Delete account | - | 204 No Content |

#### Business Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/accounts/{id}/reset-cycle` | Reset cycle amounts (BR009) | - | 200 OK |
| POST | `/api/accounts/{id}/update-interest` | Update balance with interest | interestAmount (param) | 200 OK |
| GET | `/api/accounts/{id}/validate-payment` | Validate payment balance | - | 200 OK |
| GET | `/api/accounts/active` | Get active accounts | - | Page<AccountResponseDto> |
| GET | `/api/accounts/expired` | Get expired accounts | - | Page<AccountResponseDto> |
| GET | `/api/accounts/with-balance` | Get accounts with positive balance | - | Page<AccountResponseDto> |
| GET | `/api/accounts/by-group/{groupId}` | Get accounts by group | - | Page<AccountResponseDto> |

### Account Entity Fields

**CreateAccountRequestDto**:
- accountId (String, 11 digits, required) - Unique account identifier
- activeStatus (String, 1 char, required) - Y for active, N for inactive
- currentBalance (BigDecimal, required) - Current account balance
- creditLimit (BigDecimal, required) - Credit limit
- cashCreditLimit (BigDecimal, required) - Cash credit limit
- openDate (LocalDate, required) - Account opening date
- expirationDate (LocalDate, required) - Account expiration date
- reissueDate (LocalDate, required) - Card reissue date
- currentCycleCredit (BigDecimal, required) - Current cycle credit amount
- currentCycleDebit (BigDecimal, required) - Current cycle debit amount
- groupId (String, 10 chars, optional) - Account group identifier

**AccountResponseDto** (includes all above fields plus):
- accountData (String) - Comprehensive account information
- availableCredit (BigDecimal) - Calculated available credit
- availableCashCredit (BigDecimal) - Calculated available cash credit
- netCycleAmount (BigDecimal) - Net cycle amount
- activeStatusDisplay (String) - Human-readable status
- isExpired (Boolean) - Expiration status
- creditUtilizationPercentage (BigDecimal) - Credit utilization
- hasOutstandingBalance (Boolean) - Balance status
- formattedAccountId (String) - Formatted account ID
- accountSummary (String) - Account summary text
- createdAt (LocalDateTime) - Creation timestamp
- updatedAt (LocalDateTime) - Last update timestamp

## Business Rules Implementation

The following business rules have been fully implemented:

### Account Processing (BR-001 to BR-004)
- **BR-001**: Sequential Account Record Processing - Accounts processed sequentially
- **BR-002**: Account Data Display Requirements - All fields displayed
- **BR-003**: Account File Access Control - File opened for input operations
- **BR-004**: End of File Detection - Processing stops at EOF

### Interest Calculation (BR-005 to BR-009)
- **BR-005**: Account Processing Sequence - Sequential processing with category completion
- **BR-006**: Zero Interest Rate Handling - No calculation when rate is zero
- **BR-007**: Default Interest Rate Fallback - Uses default rate when specific rate not found
- **BR-008**: Transaction ID Generation - Unique IDs generated with date and suffix
- **BR-009**: Cycle Amount Reset - Cycle amounts reset after interest calculation

### Cross-Reference Management (BR-010)
- **BR-010**: Account and Card Cross-Reference - Card numbers retrieved from cross-reference

### User Interface (BR-011 to BR-017)
- **BR-011**: Date Parameter Reading - Date parameters read from parameter file
- **BR-012**: Page State Maintenance - Pagination state maintained
- **BR-013**: Update Card Information - Navigation to card update program
- **BR-014**: Forward Pagination - Next set of records read
- **BR-015**: Backward Pagination - Previous set of records read
- **BR-017**: Input Error Protection - Row selection fields protected on errors

## Validation Rules

### Account Validations
- Account ID must be 11 digits, numeric, non-zero
- Active status must be Y or N
- Current balance must be valid signed numeric with 2 decimals
- Credit limits must be positive amounts with 2 decimals
- Open date must be in the past
- Expiration date must be valid YYYYMMDD format
- Cycle amounts must be valid signed numeric with 2 decimals

### Card Validations
- Card number must be 16 digits, numeric
- Account ID must exist in account master file
- Customer ID must be 9 digits, numeric
- CVV code must be 3 digits, numeric
- Expiration date must be valid

### Customer Validations
- Customer ID must be 9 digits, numeric
- SSN must be valid 9-digit number with specific rules
- First name and last name required, alphabetic only
- FICO score must be between 300 and 850
- Primary holder indicator must be Y or N

### Transaction Validations
- Transaction ID must be 16 characters, alphanumeric, unique
- Card number must be 16 digits and exist in cross-reference
- Transaction amount must be valid signed numeric with 2 decimals
- Transaction type code must exist in reference file

## Error Handling

All endpoints implement comprehensive error handling with appropriate HTTP status codes:

- **200 OK**: Successful operation
- **201 Created**: Resource created successfully
- **204 No Content**: Successful deletion
- **400 Bad Request**: Invalid request data or validation error
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

Error responses include:
- Specific error messages from business rules
- Validation error details
- Business rule error codes where applicable

## Database Schema

### Migration Files

Database migrations are managed using Flyway with the following naming convention:
- Format: `V{version}__{description}.sql`
- Example: `V1__Create_accounts_table.sql`

### Account Table Structure

```sql
CREATE TABLE accounts (
    account_id BIGINT NOT NULL PRIMARY KEY,
    acct_id BIGINT NOT NULL,
    xref_acct_id BIGINT NOT NULL,
    acct_active_status VARCHAR(1) NOT NULL,
    active_status VARCHAR(1) NOT NULL,
    account_status VARCHAR(1) NOT NULL,
    acct_curr_bal DECIMAL(12, 2) NOT NULL,
    current_balance DECIMAL(12, 2) NOT NULL,
    acct_credit_limit DECIMAL(12, 2) NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    acct_cash_credit_limit DECIMAL(12, 2) NOT NULL,
    cash_credit_limit DECIMAL(12, 2) NOT NULL,
    acct_open_date DATE NOT NULL,
    open_date DATE NOT NULL,
    acct_expiraion_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    acct_reissue_date DATE,
    reissue_date DATE NOT NULL,
    acct_curr_cyc_credit DECIMAL(12, 2) NOT NULL,
    current_cycle_credit DECIMAL(12, 2) NOT NULL,
    acct_curr_cyc_debit DECIMAL(12, 2) NOT NULL,
    current_cycle_debit DECIMAL(12, 2) NOT NULL,
    acct_group_id VARCHAR(10),
    group_id VARCHAR(10),
    account_data VARCHAR(289) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Indexes

The following indexes are created for optimal query performance:
- `idx_accounts_acct_id` on `acct_id`
- `idx_accounts_xref_acct_id` on `xref_acct_id`
- `idx_accounts_acct_group_id` on `acct_group_id`
- `idx_accounts_group_id` on `group_id`
- `idx_accounts_account_status` on `account_status`
- `idx_accounts_active_status` on `active_status`
- `idx_accounts_expiration_date` on `expiration_date`

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Page number (0-indexed)
- `size`: Number of records per page (default: 20)
- `sort`: Sort field and direction (e.g., `accountId,asc`)

Example: `GET /api/accounts?page=0&size=20&sort=accountId,asc`

## API Documentation

The application includes OpenAPI/Swagger documentation accessible at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Security Considerations

- Sensitive data (SSN, CVV) is masked in responses
- User authentication required for all operations
- Admin-only operations restricted to admin users
- Card data display restricted to authorized personnel (BR-004)

## Development Guidelines

### Adding New Endpoints

1. Define the endpoint in the appropriate controller
2. Implement business logic in the service layer
3. Add repository methods if needed
4. Create DTOs for request/response
5. Add validation annotations
6. Document with OpenAPI annotations

### Testing

- Unit tests for service layer business logic
- Integration tests for API endpoints
- Validation tests for all business rules
- Database migration tests

## Future Enhancements

Potential areas for expansion:
- Additional reporting endpoints
- Batch processing APIs
- Real-time transaction processing
- Advanced search and filtering
- Export functionality (CSV, PDF)
- Audit logging endpoints
- Analytics and dashboard APIs

## Support and Maintenance

For issues or questions:
- Review business rules documentation
- Check archetype patterns
- Verify entity relationships
- Validate request/response formats

## Version History

- **v1.0.0**: Initial release with 32 entities and complete CRUD operations
  - Account management API
  - Full business rules implementation
  - Comprehensive validation
  - Database migrations
  - OpenAPI documentation

---

**Generated**: 2024
**Application**: Card Demo (CBACT01C)
**Framework**: Spring Boot 3.5.5
**Java Version**: 21
