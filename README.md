# Bill Payment System - Spring Boot Application

## Overview

This is a production-ready Spring Boot application that implements a complete **Bill Payment Processing System** for credit card accounts. The system provides REST APIs for account management, card cross-references, and transaction processing with full business rule validation.

## Features

### Core Functionality

- **Account Management**: Create, read, update, and delete credit card accounts
- **Balance Management**: Track and update account balances
- **Bill Payment Processing**: Process full balance payments with validation and confirmation
- **Transaction Recording**: Complete transaction history with detailed attributes
- **Card Cross-References**: Manage relationships between accounts and card numbers

### Business Rules Implementation

The system implements the following business rules:

- **BR001: Account Validation** - Validates that the entered account ID exists in the system
- **BR002: Balance Check** - Verifies that the account has a positive balance to pay
- **BR003: Payment Confirmation** - Requires user confirmation before processing payment
- **BR004: Full Balance Payment** - Payment processes the full current account balance
- **BR005: Transaction ID Generation** - Generates unique sequential transaction ID
- **BR006: Bill Payment Transaction Recording** - Records bill payment with specific transaction attributes
- **BR007: Account Balance Update** - Updates account balance after successful payment

## Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.5** - Application framework
- **Spring Data JPA** - Data access layer
- **PostgreSQL** - Database
- **Flyway** - Database migration management
- **Lombok** - Reduce boilerplate code
- **OpenAPI/Swagger** - API documentation
- **Maven** - Build and dependency management

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── AccountController.java
│   │   │   ├── CardCrossReferenceController.java
│   │   │   └── TransactionController.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── UpdateAccountRequestDto.java
│   │   │   ├── AccountResponseDto.java
│   │   │   ├── AccountBalanceDto.java
│   │   │   ├── ProcessPaymentRequestDto.java
│   │   │   ├── ProcessPaymentResponseDto.java
│   │   │   ├── CreateCardCrossReferenceRequestDto.java
│   │   │   ├── CardCrossReferenceResponseDto.java
│   │   │   ├── CreateTransactionRequestDto.java
│   │   │   └── TransactionResponseDto.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── Account.java
│   │   │   ├── CardCrossReference.java
│   │   │   └── Transaction.java
│   │   ├── repository/          # Data Access Layer
│   │   │   ├── AccountRepository.java
│   │   │   ├── CardCrossReferenceRepository.java
│   │   │   └── TransactionRepository.java
│   │   └── service/             # Business Logic Layer
│   │       ├── AccountService.java
│   │       ├── CardCrossReferenceService.java
│   │       └── TransactionService.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway Migration Scripts
│           ├── V1__Create_accounts_table.sql
│           ├── V2__Create_card_cross_reference_table.sql
│           └── V3__Create_transactions_table.sql
└── test/                        # Test classes
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE billpayment;
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/billpayment
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

### Database Migration

Flyway will automatically run database migrations on application startup. The migrations create:
- `accounts` table
- `card_cross_reference` table
- `transactions` table

## API Documentation

### OpenAPI/Swagger UI

Once the application is running, access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### API Summary

For a complete list of all API endpoints, request/response formats, and examples, see:
- [openapi-summary.md](openapi-summary.md)

### Quick API Reference

#### Account Management
- `GET /api/accounts` - Get all accounts (paginated)
- `GET /api/accounts/{accountId}` - Get account by ID
- `GET /api/accounts/{accountId}/balance` - Get account balance
- `POST /api/accounts` - Create new account
- `POST /api/accounts/process-payment` - Process bill payment
- `PUT /api/accounts/{accountId}` - Update account
- `DELETE /api/accounts/{accountId}` - Delete account

#### Card Cross Reference Management
- `GET /api/card-cross-references` - Get all cross-references (paginated)
- `GET /api/card-cross-references/{accountId}/{cardNumber}` - Get specific cross-reference
- `GET /api/card-cross-references/account/{accountId}` - Get by account
- `GET /api/card-cross-references/card/{cardNumber}` - Get by card
- `POST /api/card-cross-references` - Create cross-reference
- `DELETE /api/card-cross-references/{accountId}/{cardNumber}` - Delete cross-reference

#### Transaction Management
- `GET /api/transactions` - Get all transactions (paginated)
- `GET /api/transactions/{transactionId}` - Get transaction by ID
- `GET /api/transactions/account/{accountId}` - Get by account
- `GET /api/transactions/bill-payments` - Get all bill payments
- `GET /api/transactions/bill-payments/account/{accountId}` - Get bill payments by account
- `POST /api/transactions` - Create transaction
- `DELETE /api/transactions/{transactionId}` - Delete transaction

## Usage Examples

### 1. Create an Account

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "ACC00001234",
    "currentBalance": 1500.00
  }'
```

### 2. Check Account Balance

```bash
curl -X GET http://localhost:8080/api/accounts/ACC00001234/balance
```

### 3. Process Bill Payment

```bash
curl -X POST http://localhost:8080/api/accounts/process-payment \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "ACC00001234",
    "cardNumber": "4111111111111111",
    "confirmPayment": "Y"
  }'
```

### 4. View Transaction History

```bash
curl -X GET http://localhost:8080/api/transactions/account/ACC00001234
```

## Bill Payment Workflow

The complete bill payment workflow:

1. **Account Validation (BR001)**
   - System validates that the account ID exists
   - Error: "Account ID NOT found..." if account doesn't exist

2. **Balance Check (BR002)**
   - System verifies account has positive balance
   - Error: "You have nothing to pay..." if balance is zero or negative

3. **Payment Confirmation (BR003)**
   - User must confirm payment with "Y" or cancel with "N"
   - Error: "Invalid value. Valid values are (Y/N)..." for invalid input

4. **Full Balance Payment (BR004)**
   - System processes payment for the full current balance
   - No partial payments allowed

5. **Transaction ID Generation (BR005)**
   - System generates unique sequential transaction ID
   - Auto-incremented by database

6. **Transaction Recording (BR006)**
   - System records transaction with specific attributes:
     - Type Code: "02" (Bill Payment)
     - Category Code: 2 (Bill Payment)
     - Source: "POS TERM"
     - Description: "BILL PAYMENT - ONLINE"
     - Merchant ID: 999999999
     - Merchant Name: "BILL PAYMENT"

7. **Balance Update (BR007)**
   - System updates account balance to zero
   - Transaction is committed atomically

## Data Models

### Account
- `accountId` (String, 11 chars) - Unique account identifier
- `currentBalance` (Decimal, 13,2) - Current account balance
- `createdAt` (Timestamp) - Creation timestamp
- `updatedAt` (Timestamp) - Last update timestamp

### CardCrossReference
- `accountId` (String, 11 chars) - Account identifier
- `cardNumber` (String, 16 chars) - Card number
- `createdAt` (Timestamp) - Creation timestamp
- `updatedAt` (Timestamp) - Last update timestamp

### Transaction
- `transactionId` (Long) - Unique transaction identifier (auto-generated)
- `transactionTypeCode` (String, 2 chars) - Transaction type ("02" for bill payment)
- `transactionCategoryCode` (Integer) - Transaction category (2 for bill payment)
- `transactionSource` (String, 10 chars) - Transaction source ("POS TERM")
- `description` (String, 50 chars) - Transaction description
- `amount` (Decimal, 11,2) - Transaction amount
- `cardNumber` (String, 16 chars) - Card number used
- `merchantId` (Long) - Merchant identifier
- `merchantName` (String, 50 chars) - Merchant name
- `merchantCity` (String, 50 chars) - Merchant city
- `merchantZip` (String, 10 chars) - Merchant ZIP code
- `originationTimestamp` (Timestamp) - Transaction origination time
- `processingTimestamp` (Timestamp) - Transaction processing time
- `accountId` (String, 11 chars) - Associated account
- `createdAt` (Timestamp) - Creation timestamp
- `updatedAt` (Timestamp) - Last update timestamp

## Validation Rules

### Account Validation
- Account ID: Required, max 11 characters, cannot be empty
- Current Balance: Required, must be greater than 0.01 for new accounts

### Payment Validation
- Account ID: Required, max 11 characters, cannot be empty
- Card Number: Required, max 16 characters
- Confirm Payment: Optional, must be "Y" or "N" if provided
- Account must exist (BR001)
- Account must have positive balance (BR002)
- User must confirm with "Y" to proceed (BR003)

### Transaction Validation
- All fields are required
- Type Code: Max 2 characters
- Category Code: Integer
- Source: Max 10 characters
- Description: Max 50 characters
- Amount: Decimal with 2 decimal places
- Card Number: Max 16 characters
- Merchant Name: Max 50 characters
- Merchant City: Max 50 characters
- Merchant ZIP: Max 10 characters

## Error Handling

The application uses standard HTTP status codes:

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `204 No Content` - Successful deletion
- `400 Bad Request` - Invalid request data or validation error
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `422 Unprocessable Entity` - Business rule validation failed
- `500 Internal Server Error` - Server error

Error messages follow business rule specifications:
- "Acct ID can NOT be empty..."
- "Account ID NOT found..."
- "You have nothing to pay..."
- "Invalid value. Valid values are (Y/N)..."

## Testing

### Manual Testing

Use the provided Swagger UI or curl commands to test the API endpoints.

### Automated Testing

Run unit and integration tests:
```bash
mvn test
```

## Configuration

### Application Properties

Key configuration properties in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/billpayment
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Logging Configuration
logging.level.com.example.demo=INFO
```

## Security Considerations

**Note:** This application does not implement authentication or authorization. For production deployment, consider adding:

- Spring Security with JWT or OAuth2
- Role-based access control (RBAC)
- API key management
- Rate limiting
- CORS configuration
- HTTPS/TLS encryption
- Input sanitization
- SQL injection prevention (already handled by JPA)

## Performance Considerations

- Database indexes are created on frequently queried columns
- Pagination is implemented for all list endpoints
- Lazy loading is used for entity relationships
- Connection pooling is configured for database connections
- Transactions are properly scoped to minimize lock duration

## Monitoring and Observability

The application includes Spring Boot Actuator for monitoring:

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
```

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify PostgreSQL is running
   - Check database credentials in application.properties
   - Ensure database exists

2. **Flyway Migration Error**
   - Check if migrations have already been applied
   - Verify database schema permissions
   - Review Flyway migration history table

3. **Port Already in Use**
   - Change server.port in application.properties
   - Kill process using port 8080

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For questions or issues, please contact the development team or create an issue in the repository.

## Changelog

### Version 1.0.0 (2024-01-15)
- Initial release
- Complete bill payment processing system
- Account management APIs
- Card cross-reference management
- Transaction management and history
- Full business rule implementation (BR001-BR007)
- Database migrations with Flyway
- OpenAPI/Swagger documentation

---

**Built with ❤️ using Spring Boot**
