# CardDemo Bill Payment Processing System

A production-ready Spring Boot application for processing credit card bill payments with complete business rule validation and transaction management.

## Overview

This application implements a comprehensive bill payment system for credit card accounts. It provides REST APIs for account management, card cross-referencing, transaction processing, and complete bill payment workflows.

## Features

### Core Functionality
- ✅ **Account Management**: Create, read, update, and delete credit card accounts
- ✅ **Card Cross-Reference**: Link multiple cards to accounts
- ✅ **Transaction Processing**: Record and manage bill payment transactions
- ✅ **Bill Payment Workflow**: Complete payment processing with validation and confirmation

### Business Rules Implementation
- **BR001**: Account Validation - Validates account existence
- **BR002**: Balance Check - Verifies positive balance for payments
- **BR003**: Payment Confirmation - Requires user confirmation
- **BR004**: Full Balance Payment - Processes full account balance
- **BR005**: Transaction ID Generation - Auto-generates unique IDs
- **BR006**: Bill Payment Recording - Records transactions with specific attributes
- **BR007**: Account Balance Update - Updates balance after payment

### Technical Features
- 🔒 **Data Validation**: Comprehensive input validation with specific error messages
- 🔐 **Security**: Card number masking in all responses
- 📊 **Pagination**: All list endpoints support pagination
- 🔍 **Advanced Queries**: Filter by date range, card number, account, etc.
- 📈 **Statistics**: Transaction counts and amount summaries
- 🗄️ **Database Migrations**: Flyway for version-controlled schema changes
- 📝 **API Documentation**: OpenAPI/Swagger integration
- 🧪 **Sample Data**: Pre-loaded test data for development

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.5
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **Build Tool**: Maven
- **API Documentation**: Swagger/OpenAPI 3.0
- **Validation**: Jakarta Bean Validation
- **Logging**: SLF4J with Logback

## Project Structure

```
src/
├── main/
│   ├── java/com/carddemo/billpayment/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── AccountController.java
│   │   │   ├── BillPaymentController.java
│   │   │   ├── CardCrossReferenceController.java
│   │   │   └── TransactionController.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── AccountResponseDto.java
│   │   │   ├── BillPaymentRequestDto.java
│   │   │   ├── BillPaymentResponseDto.java
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── UpdateAccountRequestDto.java
│   │   │   └── ... (other DTOs)
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
│   │       ├── BillPaymentService.java
│   │       ├── CardCrossReferenceService.java
│   │       └── TransactionService.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway Migrations
│           ├── V1__Create_accounts_table.sql
│           ├── V2__Create_card_cross_reference_table.sql
│           ├── V3__Create_transactions_table.sql
│           └── V4__Insert_sample_data.sql
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
CREATE DATABASE carddemo_billpayment;
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo_billpayment
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Accessing the API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Base API URL**: http://localhost:8080/api

## API Documentation

Comprehensive API documentation is available in the [openapi-summary.md](openapi-summary.md) file.

### Quick API Overview

#### Bill Payment
- `POST /api/bill-payment/process` - Process bill payment
- `GET /api/bill-payment/account/{accountId}/confirmation` - Get payment confirmation info
- `POST /api/bill-payment/quick-process` - Quick payment (single step)

#### Accounts
- `GET /api/accounts` - List all accounts
- `GET /api/accounts/{acctId}` - Get account by ID
- `POST /api/accounts` - Create account
- `PUT /api/accounts/{acctId}` - Update account
- `DELETE /api/accounts/{acctId}` - Delete account
- `GET /api/accounts/{acctId}/validate-payment` - Validate for payment
- `GET /api/accounts/{acctId}/payment-amount` - Get payment amount
- `GET /api/accounts/positive-balance` - Get accounts with positive balance

#### Card Cross References
- `GET /api/card-cross-references` - List all cross-references
- `GET /api/card-cross-references/{id}` - Get by ID
- `GET /api/card-cross-references/account/{acctId}` - Get by account
- `GET /api/card-cross-references/card/{cardNum}` - Get by card
- `POST /api/card-cross-references` - Create cross-reference
- `PUT /api/card-cross-references/{id}` - Update cross-reference
- `DELETE /api/card-cross-references/{id}` - Delete cross-reference
- `GET /api/card-cross-references/validate` - Validate card for account

#### Transactions
- `GET /api/transactions` - List all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction
- `GET /api/transactions/card/{cardNum}` - Get by card
- `GET /api/transactions/account/{accountId}` - Get by account
- `GET /api/transactions/bill-payments` - Get bill payments only
- `GET /api/transactions/date-range` - Get by date range
- `GET /api/transactions/recent` - Get recent transactions
- `GET /api/transactions/statistics/card/{cardNum}` - Card statistics
- `GET /api/transactions/statistics/bill-payments` - Bill payment statistics

## Sample Data

The application includes sample data for testing:

### Accounts
| Account ID | Balance |
|------------|---------|
| ACC00001234 | $1,500.00 |
| ACC00005678 | $2,750.50 |
| ACC00009012 | $0.00 |
| ACC00003456 | $500.25 |
| ACC00007890 | $10,000.00 |

### Cards
| Card Number | Account |
|-------------|---------|
| 4111111111111111 | ACC00001234 |
| 4222222222222222 | ACC00005678 |
| 4333333333333333 | ACC00009012 |
| 4444444444444444 | ACC00003456 |
| 4555555555555555 | ACC00007890 |
| 4666666666666666 | ACC00001234 |

## Bill Payment Workflow Example

### Using cURL

#### Step 1: Get Account Information
```bash
curl -X GET "http://localhost:8080/api/bill-payment/account/ACC00001234/confirmation?cardNumber=4111111111111111"
```

#### Step 2: Process Payment with Confirmation
```bash
curl -X POST "http://localhost:8080/api/bill-payment/process" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "ACC00001234",
    "cardNumber": "4111111111111111",
    "confirmation": "Y"
  }'
```

### Using Swagger UI

1. Navigate to http://localhost:8080/swagger-ui.html
2. Find the "Bill Payment" section
3. Try the `/api/bill-payment/process` endpoint
4. Use the sample data provided

## Business Rules Validation

The system enforces strict business rules:

1. **Account Validation**: Account must exist before any operation
2. **Balance Check**: Account must have positive balance for payment
3. **Confirmation Required**: User must confirm before payment processing
4. **Full Balance Payment**: Always processes the full account balance
5. **Transaction Recording**: Creates detailed transaction record
6. **Balance Update**: Updates account balance to zero after payment
7. **Card Validation**: Card must be associated with the account

## Error Handling

The API provides specific error messages for validation failures:

- `Acct ID can NOT be empty...` - Account ID is required
- `Account ID NOT found...` - Account does not exist
- `You have nothing to pay...` - Account balance is zero or negative
- `Invalid value. Valid values are (Y/N)...` - Invalid confirmation
- `Card number is not associated with this account` - Card validation failed

## Security Features

- **Card Number Masking**: All responses mask card numbers (show only last 4 digits)
- **Input Validation**: Comprehensive validation on all inputs
- **Transaction Integrity**: Database transactions ensure data consistency
- **Audit Trail**: All operations logged with timestamps

## Database Schema

### accounts
- `acct_id` VARCHAR(11) PRIMARY KEY
- `acct_curr_bal` DECIMAL(13,2) NOT NULL
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

### card_cross_reference
- `id` BIGSERIAL PRIMARY KEY
- `xref_acct_id` VARCHAR(11) NOT NULL (FK to accounts)
- `xref_card_num` VARCHAR(16) NOT NULL
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP
- UNIQUE(xref_acct_id, xref_card_num)

### transactions
- `tran_id` BIGSERIAL PRIMARY KEY
- `tran_type_cd` VARCHAR(2) NOT NULL
- `tran_cat_cd` INTEGER NOT NULL
- `tran_source` VARCHAR(10) NOT NULL
- `tran_desc` VARCHAR(50) NOT NULL
- `tran_amt` DECIMAL(11,2) NOT NULL
- `tran_card_num` VARCHAR(16) NOT NULL
- `tran_merchant_id` INTEGER NOT NULL
- `tran_merchant_name` VARCHAR(50) NOT NULL
- `tran_merchant_city` VARCHAR(50) NOT NULL
- `tran_merchant_zip` VARCHAR(10) NOT NULL
- `tran_orig_ts` TIMESTAMP NOT NULL
- `tran_proc_ts` TIMESTAMP NOT NULL
- `account_id` VARCHAR(11) NOT NULL (FK to accounts)
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

## Development

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Creating a Production Build

```bash
mvn clean package -DskipTests
```

The JAR file will be created in the `target/` directory.

### Running in Production

```bash
java -jar target/bill-payment-0.0.1-SNAPSHOT.jar
```

## Configuration

Key configuration properties in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo_billpayment
spring.datasource.username=postgres
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Logging
logging.level.com.carddemo.billpayment=INFO
```

## Monitoring and Health

The application includes Spring Boot Actuator endpoints:

- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## Contributing

1. Follow the existing code structure and patterns
2. Ensure all business rules are properly implemented
3. Add appropriate validation and error handling
4. Update documentation for any API changes
5. Write tests for new functionality

## License

This project is part of the CardDemo Bill Payment Processing System.

## Support

For issues, questions, or contributions, please refer to the project documentation or contact the development team.

---

**Version**: 1.0.0  
**Last Updated**: 2024-01-15  
**Status**: Production Ready
