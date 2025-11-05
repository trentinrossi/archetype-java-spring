# Card Transaction Lifecycle Management System

## Overview

This is a modern Spring Boot application that implements the Card Transaction Lifecycle Management system, migrated from legacy COBOL programs. The system provides comprehensive functionality for managing credit card transactions, including validation, posting, reporting, and account management.

## Legacy System Migration

This application modernizes the following COBOL programs:

- **CBTRN01C**: Daily Transaction Processing and Validation Program
- **CBTRN02C**: Daily Transaction Processing and Posting System
- **CBTRN03C**: Transaction Detail Report Generator
- **COTRN01C**: Transaction View Program
- **COTRN02C**: Transaction Addition Program
- **CSUTLDTC**: Date Validation Utility Program

## Features

### Transaction Management
- Transaction validation with comprehensive business rules
- Transaction posting with automatic balance updates
- Transaction retrieval by ID, card number, or account
- Sequential transaction ID generation

### Account Management
- Account creation and updates
- Balance tracking (current balance, cycle credits/debits)
- Credit limit validation
- Account expiration validation

### Reporting
- Date range transaction reports
- Paginated transaction listings
- Transaction categorization and balances

### Validation Services
- Card number cross-reference validation
- Account existence validation
- Credit limit checking
- Date format validation
- Account expiration validation

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL/MySQL** (configurable)
- **Lombok** for boilerplate reduction
- **Jakarta Validation** for input validation
- **Flyway** for database migrations

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API endpoints
│   │   │   ├── TransactionController.java
│   │   │   ├── AccountController.java
│   │   │   └── DateValidationController.java
│   │   ├── service/             # Business logic layer
│   │   │   ├── TransactionService.java
│   │   │   ├── AccountService.java
│   │   │   └── DateValidationService.java
│   │   ├── repository/          # Data access layer
│   │   │   ├── TransactionRepository.java
│   │   │   ├── AccountRepository.java
│   │   │   ├── CardRepository.java
│   │   │   ├── CardCrossReferenceRepository.java
│   │   │   ├── TransactionCategoryBalanceRepository.java
│   │   │   └── CustomerRepository.java
│   │   ├── entity/              # JPA entities
│   │   │   ├── Transaction.java
│   │   │   ├── Account.java
│   │   │   ├── Card.java
│   │   │   ├── CardCrossReference.java
│   │   │   ├── TransactionCategoryBalance.java
│   │   │   └── Customer.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── TransactionDTO.java
│   │   │   ├── AccountDTO.java
│   │   │   ├── TransactionValidationResultDTO.java
│   │   │   ├── TransactionReportDTO.java
│   │   │   └── DateRangeRequestDTO.java
│   │   └── exception/           # Exception handling
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway migrations
│           └── V1__create_card_transaction_tables.sql
└── test/                        # Unit and integration tests
```

## Database Schema

The application uses the following main tables:

- **customers**: Customer master data
- **accounts**: Account information with balances and limits
- **cards**: Credit card information
- **card_cross_reference**: Card-to-account mapping
- **transactions**: Transaction records
- **transaction_category_balance**: Category-level balance tracking

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ or MySQL 8+

### Configuration

Update `application.properties` with your database configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cardtransactions
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true
```

### Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Documentation

Comprehensive API documentation is available in `openapi-summary.md`. Key endpoints include:

### Transaction Endpoints
- `POST /api/transactions/validate` - Validate transaction
- `POST /api/transactions` - Post new transaction
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/card/{cardNumber}` - Get transactions by card
- `GET /api/transactions/account/{accountId}` - Get transactions by account
- `POST /api/transactions/report` - Generate transaction report

### Account Endpoints
- `GET /api/accounts/{accountId}` - Get account by ID
- `GET /api/accounts/customer/{customerId}` - Get accounts by customer
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{accountId}` - Update account

### Validation Endpoints
- `GET /api/validation/date` - Validate date format

## Business Rules

### Transaction Validation (CBTRN01C)
1. Card number must exist in cross-reference file
2. Account must exist and be active
3. Transaction date must be before account expiration
4. All required fields must be present

### Transaction Posting (CBTRN02C)
1. All validation rules must pass
2. Credit limit check: (cycle_credit - cycle_debit + amount) ≤ credit_limit
3. Sequential transaction ID generation
4. Automatic balance updates:
   - Current balance += transaction amount
   - Cycle credit/debit updated based on amount sign
5. Category balance tracking updated

### Report Generation (CBTRN03C)
1. Filter transactions by processing timestamp date range
2. Order by account ID and timestamp
3. Support pagination for large result sets
4. Include transaction type and category descriptions

## Error Handling

The application provides detailed error responses:

- **400 Bad Request**: Validation failures, invalid input
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected errors

All errors include timestamp, status code, error type, and descriptive message.

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TransactionServiceTest

# Run integration tests
mvn verify
```

## Deployment

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# Build Docker image
docker build -t card-transaction-api .

# Run container
docker run -p 8080:8080 card-transaction-api
```

### Production Considerations

1. **Security**: Implement authentication and authorization (OAuth 2.0, JWT)
2. **Monitoring**: Add application monitoring (Prometheus, Grafana)
3. **Logging**: Configure centralized logging (ELK stack)
4. **Performance**: Enable caching for frequently accessed data
5. **Scalability**: Deploy multiple instances behind load balancer
6. **Database**: Use connection pooling and optimize queries

## Migration Notes

### Key Differences from Legacy System

1. **Data Access**: VSAM files replaced with relational database
2. **Transaction Management**: CICS transactions replaced with Spring @Transactional
3. **Screen Handling**: BMS maps replaced with REST API
4. **Date Handling**: COBOL date routines replaced with Java LocalDate/LocalDateTime
5. **Error Handling**: File status codes replaced with exceptions and HTTP status codes

### Preserved Business Logic

- Transaction validation rules (card verification, credit limits, expiration)
- Sequential transaction ID generation
- Balance update calculations
- Category balance tracking
- Date range filtering for reports

## Contributing

1. Follow Spring Boot best practices
2. Maintain SOLID principles
3. Write unit tests for all business logic
4. Update API documentation for new endpoints
5. Follow existing code style and patterns

## License

[Specify your license here]

## Support

For questions or issues, please contact the development team or create an issue in the project repository.
