# Card Demo Application - Account Management Module

## Overview

This is a production-ready Spring Boot application implementing the Account Management module for the Card Demo system. The application provides comprehensive REST APIs for managing customer accounts with financial and status information, implementing the CBACT01C - Account Data File Reader and Printer functionality.

## Features

### Core Functionality
- ✅ Complete CRUD operations for account management
- ✅ Sequential account processing (batch-style file processing)
- ✅ Credit and debit transaction processing
- ✅ Account validation and business rule enforcement
- ✅ Comprehensive query capabilities (by status, group, expiration, etc.)
- ✅ Real-time credit availability calculations
- ✅ Transaction eligibility verification

### Business Rules Implemented
- **BR-001:** Sequential Account Record Processing
- **BR-002:** Account Data Display Requirements
- **BR-003:** Account File Access Control
- **BR-004:** End of File Detection

### Technical Features
- 🔒 Input validation with Jakarta Bean Validation
- 📊 Pagination support for all list endpoints
- 🗄️ Database migrations with Flyway
- 📝 Comprehensive logging with SLF4J
- 🔄 Transaction management with Spring @Transactional
- 📖 OpenAPI/Swagger documentation
- 🎯 Clean architecture with layered design

## Technology Stack

- **Java:** 21
- **Framework:** Spring Boot 3.5.5
- **Database:** PostgreSQL (H2 for development/testing)
- **ORM:** Spring Data JPA
- **Migration:** Flyway
- **Build Tool:** Maven
- **Validation:** Jakarta Bean Validation
- **Logging:** SLF4J with Logback
- **Documentation:** OpenAPI 3.0 (Swagger)
- **Code Generation:** Lombok

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # Main application class
│   │   ├── controller/                   # REST Controllers
│   │   │   └── AccountController.java    # Account API endpoints
│   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── UpdateAccountRequestDto.java
│   │   │   └── AccountResponseDto.java
│   │   ├── entity/                       # JPA Entities
│   │   │   └── Account.java              # Account entity
│   │   ├── repository/                   # Data Access Layer
│   │   │   └── AccountRepository.java    # Account repository
│   │   └── service/                      # Business Logic Layer
│   │       └── AccountService.java       # Account service
│   └── resources/
│       ├── application.properties        # Application configuration
│       └── db/migration/                 # Flyway migrations
│           └── V1__Create_accounts_table.sql
└── test/                                 # Test classes
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher (or use H2 for development)

### Installation

1. **Clone the repository:**
```bash
git clone <repository-url>
cd card-demo-app
```

2. **Configure the database:**

Edit `src/main/resources/application.properties`:

For PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo
spring.datasource.username=your_username
spring.datasource.password=your_password
```

For H2 (development):
```properties
spring.datasource.url=jdbc:h2:mem:carddemo
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
```

3. **Build the application:**
```bash
mvn clean install
```

4. **Run the application:**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Database Migration

Flyway migrations run automatically on application startup. The initial migration creates the `accounts` table with all necessary indexes.

To manually run migrations:
```bash
mvn flyway:migrate
```

## API Documentation

### Swagger UI
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification
Access the OpenAPI JSON specification at:
```
http://localhost:8080/v3/api-docs
```

### Complete API Summary
See [openapi-summary.md](openapi-summary.md) for detailed documentation of all endpoints, including:
- Request/response formats
- Validation rules
- Error codes
- Testing examples

## Quick Start Examples

### Create an Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": "12345678901",
    "acctActiveStatus": "A",
    "acctCurrBal": 1500.50,
    "acctCreditLimit": 5000.00,
    "acctCashCreditLimit": 1000.00,
    "acctOpenDate": "2023-01-15",
    "acctExpirationDate": "2026-01-15",
    "acctCurrCycCredit": 250.00,
    "acctCurrCycDebit": 150.00,
    "acctGroupId": "GRP001"
  }'
```

### Get All Accounts
```bash
curl -X GET "http://localhost:8080/api/accounts?page=0&size=20"
```

### Apply a Debit Transaction
```bash
curl -X POST "http://localhost:8080/api/accounts/12345678901/debit?amount=100.00"
```

### Check Transaction Eligibility
```bash
curl -X GET "http://localhost:8080/api/accounts/12345678901/can-process-transaction?amount=500.00"
```

### Process Accounts Sequentially
```bash
curl -X GET "http://localhost:8080/api/accounts/process-sequential"
```

## Data Model

### Account Entity

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| acctId | Long | Yes | Unique 11-digit account identifier |
| acctActiveStatus | String(1) | Yes | 'A' (active) or 'I' (inactive) |
| acctCurrBal | BigDecimal | Yes | Current balance |
| acctCreditLimit | BigDecimal | Yes | Maximum credit limit |
| acctCashCreditLimit | BigDecimal | Yes | Maximum cash credit limit |
| acctOpenDate | LocalDate | Yes | Account opening date |
| acctExpirationDate | LocalDate | Yes | Account expiration date |
| acctReissueDate | LocalDate | No | Account reissue date |
| acctCurrCycCredit | BigDecimal | Yes | Current cycle credit amount |
| acctCurrCycDebit | BigDecimal | Yes | Current cycle debit amount |
| acctGroupId | String | No | Account group identifier |
| createdAt | LocalDateTime | Auto | Record creation timestamp |
| updatedAt | LocalDateTime | Auto | Record update timestamp |

### Computed Fields

- **availableCredit:** Credit limit - current balance
- **availableCashCredit:** Cash credit limit - current balance
- **isActive:** Whether account status is 'A'
- **isExpired:** Whether expiration date has passed
- **currentCycleNetAmount:** Current cycle credit - current cycle debit

## Validation Rules

### Account ID
- Must be exactly 11 digits
- Must be numeric
- Must be unique

### Active Status
- Must be 'A' (active) or 'I' (inactive)
- Required field

### Monetary Fields
- Must be >= 0
- Precision: 15 digits, scale: 2 decimal places
- Use BigDecimal for accuracy

### Dates
- acctOpenDate: Must be in the past or present
- acctExpirationDate: Must be in the future
- Format: ISO-8601 (YYYY-MM-DD)

## Business Logic

### Credit Application
When applying a credit:
1. Validates credit amount > 0
2. Reduces current balance by credit amount
3. Increases current cycle credit by credit amount
4. Updates timestamp

### Debit Application
When applying a debit:
1. Validates debit amount > 0
2. Checks account is active
3. Checks account is not expired
4. Verifies available credit is sufficient
5. Increases current balance by debit amount
6. Increases current cycle debit by debit amount
7. Updates timestamp

### Transaction Eligibility
A transaction can be processed if:
- Account status is 'A' (active)
- Account expiration date is in the future
- Available credit >= transaction amount

### Sequential Processing
Implements legacy batch file processing:
1. Opens account file for input (validates database access)
2. Retrieves all accounts ordered by ACCT-ID ascending
3. Displays all fields for each account
4. Detects end-of-file condition
5. Returns appropriate result code

## Error Handling

### HTTP Status Codes
- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `204 No Content` - Successful deletion
- `400 Bad Request` - Invalid input or business rule violation
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Common Error Messages
- "Account ID must be an 11-digit numeric value"
- "Active status must be 'A' or 'I'"
- "Account with ID already exists"
- "Account not found"
- "Credit amount must be greater than zero"
- "Transaction exceeds available credit limit or account is not active"

## Logging

The application uses SLF4J with Logback for comprehensive logging:

- **INFO:** Normal operations, method entry/exit
- **WARN:** Business rule violations, validation failures
- **ERROR:** System errors, exceptions

Log format includes:
- Timestamp
- Log level
- Thread name
- Logger name
- Message
- Exception stack trace (if applicable)

## Testing

### Unit Tests
Run unit tests:
```bash
mvn test
```

### Integration Tests
Run integration tests:
```bash
mvn verify
```

### Manual Testing
Use the provided Swagger UI or curl commands from the examples above.

## Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Logging Configuration
logging.level.com.example.demo=INFO
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
```

## Performance Considerations

### Database Indexes
The following indexes are created for optimal query performance:
- `idx_accounts_acct_group_id` on `acct_group_id`
- `idx_accounts_acct_active_status` on `acct_active_status`
- `idx_accounts_acct_open_date` on `acct_open_date`
- `idx_accounts_acct_expiration_date` on `acct_expiration_date`

### Pagination
All list endpoints support pagination with default page size of 20. Adjust as needed:
```
GET /api/accounts?page=0&size=50
```

### Transaction Management
- Read operations use `@Transactional(readOnly = true)` for optimization
- Write operations use `@Transactional` for data consistency

## Security Considerations

### Input Validation
- All request DTOs use Jakarta Bean Validation
- Custom validation for account ID format
- Business rule validation in service layer

### SQL Injection Prevention
- JPA/Hibernate parameterized queries
- No dynamic SQL construction

### Data Integrity
- Database constraints (primary keys, not null, unique)
- Transaction management for consistency
- Optimistic locking with @Version (if needed)

## Troubleshooting

### Common Issues

**Issue:** Application fails to start
- **Solution:** Check database connection settings in application.properties

**Issue:** Flyway migration fails
- **Solution:** Ensure database is empty or run `mvn flyway:clean` first

**Issue:** Validation errors on account creation
- **Solution:** Verify account ID is exactly 11 digits and all required fields are provided

**Issue:** Transaction rejected
- **Solution:** Check account status is 'A', not expired, and has sufficient credit

## Development Guidelines

### Code Style
- Follow Spring Boot best practices
- Use Lombok to reduce boilerplate
- Maintain separation of concerns (layered architecture)
- Write comprehensive JavaDoc comments

### Adding New Features
1. Define entity/update existing entity
2. Create/update DTOs
3. Add repository methods
4. Implement service logic
5. Create controller endpoints
6. Write tests
7. Update documentation

### Database Changes
1. Create new Flyway migration script
2. Follow naming convention: `V{number}__{description}.sql`
3. Test migration on clean database
4. Update entity if needed

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write/update tests
5. Update documentation
6. Submit a pull request

## License

[Specify your license here]

## Support

For issues, questions, or contributions, please contact:
- Email: [your-email]
- Issue Tracker: [your-issue-tracker-url]

## Changelog

### Version 1.0.0 (2024)
- Initial release
- Complete Account Management module
- CRUD operations
- Sequential processing
- Credit/debit transactions
- Comprehensive validation
- Full API documentation

---

**Built with ❤️ using Spring Boot 3.5.5 and Java 21**
