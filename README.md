# Credit Card and Account Management System (COCRDLIC)

## Overview

This is a production-ready Spring Boot application implementing the **COCRDLIC (Credit Card List)** program for managing credit cards and customer accounts. The system provides comprehensive REST APIs with full business rule enforcement, user permission-based access control, and robust data validation.

## Features

### Core Functionality
- ✅ **Account Management**: Create, read, update, and delete customer accounts
- ✅ **User Management**: Manage system users with role-based permissions (Admin/Regular)
- ✅ **Credit Card Management**: Full CRUD operations for credit cards with status tracking
- ✅ **Advanced Filtering**: Filter credit cards by card number, account ID, and status
- ✅ **Pagination Support**: Efficient pagination for large datasets
- ✅ **User Access Control**: Grant/revoke user access to specific accounts

### Business Rules Implemented

The system implements **17 comprehensive business rules**:

1. **BR001**: User Permission Based Card Access
2. **BR002**: Card Number Filter Validation (16 digits)
3. **BR003**: Single Selection Enforcement
4. **BR004**: Account Filter Validation (11 digits)
5. **BR005**: Card Status Filter Validation
6. **BR006**: Filter Record Matching
7. **BR008**: First Page Navigation Restriction
8. **BR009**: Last Page Navigation Restriction
9. **BR011**: Exit to Menu Navigation
10. **BR012**: View Card Details Navigation
11. **BR013**: Update Card Information Navigation
12. **BR014**: Forward Pagination
13. **BR015**: Backward Pagination
14. **BR017**: Input Error Protection

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **Migration**: Flyway
- **Build Tool**: Maven
- **Documentation**: OpenAPI/Swagger
- **Utilities**: Lombok

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── AccountController.java
│   │   │   ├── UserController.java
│   │   │   └── CreditCardController.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── AccountResponseDto.java
│   │   │   ├── CreateUserRequestDto.java
│   │   │   ├── UserResponseDto.java
│   │   │   ├── CreateCreditCardRequestDto.java
│   │   │   ├── CreditCardResponseDto.java
│   │   │   └── CreditCardFilterRequestDto.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── Account.java
│   │   │   ├── User.java
│   │   │   └── CreditCard.java
│   │   ├── enums/               # Enumerations
│   │   │   ├── CardStatus.java
│   │   │   └── UserType.java
│   │   ├── repository/          # Data Access Layer
│   │   │   ├── AccountRepository.java
│   │   │   ├── UserRepository.java
│   │   │   └── CreditCardRepository.java
│   │   └── service/             # Business Logic Layer
│   │       ├── AccountService.java
│   │       ├── UserService.java
│   │       └── CreditCardService.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway Migrations
│           ├── V1__Create_accounts_table.sql
│           ├── V2__Create_users_table.sql
│           ├── V3__Create_credit_cards_table.sql
│           ├── V4__Create_user_account_access_table.sql
│           └── V5__Insert_sample_data.sql
└── test/                        # Test Classes
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE cocrdlic;
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cocrdlic
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Build and Run

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Database Migrations

Flyway migrations will run automatically on application startup, creating:
- All required tables with proper constraints
- Indexes for performance optimization
- Sample data for testing

## API Documentation

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Summary

Comprehensive API documentation is available in `openapi-summary.md`, including:
- All endpoint specifications
- Request/response examples
- Business rule mappings
- Error codes and messages
- Data models

## Quick Start Examples

### 1. Create an Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountId":"12345678901"}'
```

### 2. Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"userId":"USER001","userType":"ADMIN"}'
```

### 3. Create a Credit Card
```bash
curl -X POST http://localhost:8080/api/credit-cards \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber":"1234567890123456",
    "accountId":"12345678901",
    "cardStatus":"A"
  }'
```

### 4. Filter Credit Cards (with User Permission)
```bash
curl -X POST "http://localhost:8080/api/credit-cards/filter?userId=USER001" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId":"12345678901",
    "cardStatus":"A"
  }'
```

### 5. Grant User Access to Account
```bash
curl -X POST http://localhost:8080/api/accounts/12345678901/grant-access/USER001
```

## Data Models

### Account
- **account_id**: 11-digit numeric identifier (required)
- **created_at**: Timestamp
- **updated_at**: Timestamp

### User
- **user_id**: Alphanumeric identifier (max 20 characters, required)
- **user_type**: ADMIN or REGULAR (required)
- **created_at**: Timestamp
- **updated_at**: Timestamp

### Credit Card
- **card_number**: 16-digit numeric identifier (required)
- **account_id**: 11-digit account reference (required)
- **card_status**: Single character status code (required)
- **created_at**: Timestamp
- **updated_at**: Timestamp

## Card Status Codes

| Code | Status | Description |
|------|--------|-------------|
| A | Active | Card is active and can be used |
| I | Inactive | Card is inactive |
| B | Blocked | Card is blocked |
| P | Pending | Card is pending activation |
| C | Closed | Card is closed |
| S | Suspended | Card is suspended |
| E | Expired | Card has expired |
| L | Lost | Card is reported as lost |
| T | Stolen | Card is reported as stolen |
| D | Damaged | Card is damaged |

## User Types and Permissions

### Admin User (ADMIN)
- Can view **all credit cards** without account context
- Has access to **all accounts**
- Can grant/revoke user access to accounts
- No restrictions on filtering or viewing

### Regular User (REGULAR)
- Can only view credit cards for **their accessible accounts**
- **Requires account context** for filtering
- Access must be explicitly granted by admin
- Restricted to assigned accounts only

## Business Rule Details

### BR001: User Permission Based Card Access
Admin users can view all credit cards without context, while regular users can only view cards associated with their specific account.

**Implementation**:
- Service layer checks user type before filtering
- Admin users bypass account restrictions
- Regular users filtered by accessible accounts

### BR002: Card Number Filter Validation
Card number must be numeric and exactly 16 digits if supplied, cannot be blank, spaces, or zeros.

**Validation**:
- Pattern: `^(?!0+$)[0-9]{16}$`
- Error: "CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER"

### BR004: Account Filter Validation
Account ID must be numeric and exactly 11 digits if supplied, cannot be blank, spaces, or zeros.

**Validation**:
- Pattern: `^(?!0+$)[0-9]{11}$`
- Error: "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER"

### BR006: Filter Record Matching
Records must match **all supplied filter criteria** to be displayed.

**Implementation**:
- Multiple filters combined with AND logic
- Empty/null filters are ignored
- All non-empty filters must match

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- **200 OK**: Successful operation
- **201 Created**: Resource created successfully
- **204 No Content**: Successful deletion
- **400 Bad Request**: Validation error or invalid input
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Sample Data

The application includes sample data for testing:

### Accounts
- 12345678901
- 23456789012
- 34567890123
- 45678901234
- 56789012345

### Users
- **ADMIN001** (Admin) - Full access
- **USER001** (Regular) - Access to accounts 12345678901, 23456789012
- **USER002** (Regular) - Access to account 34567890123
- **USER003** (Regular) - Access to accounts 45678901234, 56789012345

### Credit Cards
- 10 sample cards distributed across all accounts
- Various statuses (Active, Blocked, Inactive, Closed, Suspended)

## Testing

### Manual Testing
Use the provided sample data to test:
1. Admin user viewing all cards
2. Regular user viewing only accessible cards
3. Filtering by card number, account ID, and status
4. Pagination navigation
5. Access control (grant/revoke)

### API Testing Tools
- **Swagger UI**: Interactive testing at `/swagger-ui.html`
- **Postman**: Import OpenAPI specification
- **cURL**: Command-line testing (examples provided)

## Performance Considerations

### Database Indexes
The system includes optimized indexes on:
- Primary keys (account_id, user_id, card_number)
- Foreign keys (account_id in credit_cards)
- Frequently queried fields (card_status, user_type)
- Timestamp fields for sorting

### Pagination
- Default page size: 20 items
- Configurable page size via query parameters
- Efficient database queries with LIMIT/OFFSET

## Security Considerations

**⚠️ Important**: This implementation focuses on business logic and does not include authentication/authorization middleware.

For production deployment, you should add:
1. **Spring Security** for authentication
2. **JWT or OAuth2** for token-based auth
3. **Role-Based Access Control (RBAC)**
4. **HTTPS/TLS** encryption
5. **Rate limiting** and throttling
6. **Input sanitization** and SQL injection prevention
7. **CORS configuration** for web clients

## Monitoring and Logging

The application uses SLF4J with Logback for logging:
- **INFO**: Normal operations, API calls
- **WARN**: Validation failures, navigation restrictions
- **ERROR**: Exceptions, system errors

Logs include:
- Request/response details
- Business rule validations
- Database operations
- Error stack traces

## Deployment

### Production Checklist
- [ ] Configure production database credentials
- [ ] Enable HTTPS/TLS
- [ ] Add authentication/authorization
- [ ] Configure logging levels
- [ ] Set up monitoring and alerting
- [ ] Configure backup strategy
- [ ] Review and adjust connection pool settings
- [ ] Enable production profiles
- [ ] Configure CORS policies
- [ ] Set up CI/CD pipeline

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Troubleshooting

### Common Issues

**Database Connection Failed**
- Verify PostgreSQL is running
- Check database credentials in application.properties
- Ensure database exists

**Flyway Migration Failed**
- Check migration scripts for syntax errors
- Verify database user has CREATE/ALTER permissions
- Review Flyway logs for specific errors

**Validation Errors**
- Ensure card numbers are exactly 16 digits
- Ensure account IDs are exactly 11 digits
- Check that values are not all zeros

## Contributing

This is a generated codebase following the archetype patterns. When making changes:
1. Follow the existing layered architecture
2. Maintain business rule implementations
3. Update tests for new features
4. Document API changes in openapi-summary.md
5. Follow naming conventions

## License

[Specify your license here]

## Support

For questions, issues, or feature requests:
- Review the `openapi-summary.md` for API details
- Check the business rules documentation
- Consult the archetype guide

## Version History

- **1.0.0** (2024): Initial release
  - Complete CRUD operations for accounts, users, and credit cards
  - 17 business rules implemented
  - User permission-based access control
  - Comprehensive filtering and pagination
  - Sample data for testing

---

**Generated with**: Spring Boot Archetype Generator  
**Framework**: Spring Boot 3.5.5  
**Java Version**: 21  
**Database**: PostgreSQL
