# Card Transaction Lifecycle Management System

## Overview
This is a Spring Boot application that implements a comprehensive Card Transaction Lifecycle Management system. The application is a modernized version of the legacy COBOL CardDemo application, implementing all business rules for transaction processing, validation, and reporting.

## Business Context
The system manages the complete lifecycle of credit card transactions including:
- Transaction creation with comprehensive validation
- Card and account management
- Credit limit checking and account balance updates
- Transaction category balance tracking
- Date range filtering and reporting

## Technology Stack
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (development)
- **Flyway** (database migrations)
- **Lombok** (code generation)
- **SpringDoc OpenAPI** (API documentation)
- **Maven** (build tool)

## Project Structure
```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── TransactionController.java
│   │   │   ├── AccountController.java
│   │   │   ├── CardController.java
│   │   │   └── CardCrossReferenceController.java
│   │   ├── service/             # Business Logic Services
│   │   │   ├── TransactionService.java
│   │   │   ├── AccountService.java
│   │   │   ├── CardService.java
│   │   │   └── CardCrossReferenceService.java
│   │   ├── repository/          # Data Access Layer
│   │   │   ├── TransactionRepository.java
│   │   │   ├── AccountRepository.java
│   │   │   ├── CardRepository.java
│   │   │   ├── CardCrossReferenceRepository.java
│   │   │   ├── TransactionTypeRepository.java
│   │   │   ├── TransactionCategoryRepository.java
│   │   │   └── TransactionCategoryBalanceRepository.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── Transaction.java
│   │   │   ├── Account.java
│   │   │   ├── Card.java
│   │   │   ├── CardCrossReference.java
│   │   │   ├── TransactionType.java
│   │   │   ├── TransactionCategory.java
│   │   │   └── TransactionCategoryBalance.java
│   │   └── dto/                 # Data Transfer Objects
│   │       ├── CreateTransactionRequestDto.java
│   │       ├── TransactionResponseDto.java
│   │       ├── CreateAccountRequestDto.java
│   │       ├── AccountResponseDto.java
│   │       ├── CreateCardRequestDto.java
│   │       ├── CardResponseDto.java
│   │       ├── CreateCardCrossReferenceRequestDto.java
│   │       └── CardCrossReferenceResponseDto.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/
│           └── V1__create_card_transaction_tables.sql
└── test/                        # Unit and Integration Tests
```

## Business Rules Implementation

### Transaction Validation (CBTRN01C & CBTRN02C)
The system implements comprehensive transaction validation:

1. **Error Code 100 - Invalid Card Number**: Validates card exists in cross-reference file
2. **Error Code 101 - Account Not Found**: Confirms account exists and is accessible
3. **Error Code 102 - Overlimit Transaction**: Checks if transaction exceeds available credit
4. **Error Code 103 - Account Expired**: Validates transaction date is before account expiration

### Transaction Processing (CBTRN02C)
- Sequential transaction ID generation
- Automatic account balance updates (current balance, cycle credit/debit)
- Transaction category balance tracking
- Timestamp management (original and processing timestamps)

### Transaction Reporting (CBTRN03C)
- Date range filtering by processing timestamp
- Card number filtering
- Pagination support for large result sets

## Database Schema

### Core Tables
- **accounts**: Customer account information with balances and credit limits
- **cards**: Card details and status
- **card_cross_reference**: Mapping between cards and accounts
- **transactions**: Complete transaction records
- **transaction_types**: Transaction type definitions
- **transaction_categories**: Transaction category definitions
- **transaction_category_balances**: Running balances by category

## API Endpoints

### Transaction Management
- `POST /api/transactions` - Create new transaction
- `GET /api/transactions` - Get all transactions (paginated)
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/transaction-id/{transactionId}` - Get by transaction ID
- `GET /api/transactions/card/{cardNumber}` - Get transactions by card
- `GET /api/transactions/date-range` - Get transactions by date range
- `DELETE /api/transactions/{id}` - Delete transaction

### Account Management
- `POST /api/accounts` - Create new account
- `GET /api/accounts/{accountId}` - Get account by ID
- `GET /api/accounts` - Get all accounts (paginated)
- `DELETE /api/accounts/{accountId}` - Delete account

### Card Management
- `POST /api/cards` - Create new card
- `GET /api/cards/{cardNumber}` - Get card by number
- `GET /api/cards` - Get all cards (paginated)
- `DELETE /api/cards/{cardNumber}` - Delete card

### Card Cross Reference Management
- `POST /api/card-cross-references` - Create card-to-account mapping
- `GET /api/card-cross-references/card/{cardNumber}` - Get mapping by card
- `GET /api/card-cross-references/account/{accountId}` - Get mappings by account
- `DELETE /api/card-cross-references/{cardNumber}` - Delete mapping

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build the Application
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console (Development)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:cardtransactiondb`
- Username: `sa`
- Password: (leave empty)

### Access API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

## Sample Data
The database migration script includes sample transaction types and categories:

**Transaction Types:**
- 01: Purchase
- 02: Cash Advance
- 03: Payment
- 04: Refund
- 05: Fee

**Transaction Categories:**
- 5411: Grocery Stores
- 5812: Restaurants
- 5541: Gas Stations
- 5999: Miscellaneous Retail
- 6011: ATM Cash Withdrawal

## Configuration

### Database Configuration
The application uses H2 in-memory database by default. To use a different database:

1. Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cardtransactiondb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

2. Add the appropriate database driver dependency to `pom.xml`

### Logging Configuration
Adjust logging levels in `application.properties`:
```properties
logging.level.com.example.demo=DEBUG
logging.level.org.springframework.web=INFO
```

## Error Handling
The application implements comprehensive error handling:
- Validation errors return 400 Bad Request with detailed messages
- Business rule violations return 400 Bad Request with error codes
- Resource not found returns 404 Not Found
- Server errors return 500 Internal Server Error

## Security Considerations
**Note**: This implementation does not include authentication/authorization. For production use:
- Implement Spring Security
- Add JWT or OAuth2 authentication
- Implement role-based access control
- Encrypt sensitive data (card numbers, account information)
- Add audit logging

## Performance Considerations
- Database indexes on frequently queried fields
- Pagination for large result sets
- Transaction management for data consistency
- Connection pooling configuration

## Monitoring and Observability
The application includes Spring Boot Actuator endpoints:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## Deployment

### Build Docker Image
```bash
docker build -t card-transaction-management:latest .
```

### Run with Docker
```bash
docker run -p 8080:8080 card-transaction-management:latest
```

## Contributing
1. Follow SOLID principles
2. Write unit tests for new features
3. Update API documentation
4. Follow existing code style and conventions

## License
[Specify your license here]

## Contact
[Specify contact information]

## Acknowledgments
This application is a modernization of the legacy COBOL CardDemo application, implementing all business rules from:
- CBTRN01C: Daily Transaction Processing and Validation
- CBTRN02C: Daily Transaction Processing and Posting System
- CBTRN03C: Transaction Detail Report Generator
- COTRN01C: Transaction View Program
- COTRN02C: Transaction Addition Program
- CSUTLDTC: Date Validation Utility Program
