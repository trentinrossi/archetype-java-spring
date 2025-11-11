# Bill Payment System

A production-ready Spring Boot application for processing bill payments with complete account validation, balance checking, and transaction recording.

## Overview

This system implements a comprehensive bill payment workflow that processes full account balance payments. It includes robust validation, transaction management, and complete audit trails.

## Features

### Core Functionality
- ✅ **Account Management** - Create, read, update, and delete customer accounts
- ✅ **Card Cross Reference** - Link multiple cards to accounts
- ✅ **Transaction Recording** - Complete transaction history with timestamps
- ✅ **Bill Payment Workflow** - End-to-end payment processing with validation

### Business Rules Implementation
- **BR001**: Account Validation - Validates account existence
- **BR002**: Balance Check - Verifies positive balance
- **BR003**: Payment Confirmation - Requires user confirmation
- **BR004**: Full Balance Payment - Processes entire balance
- **BR005**: Transaction ID Generation - Sequential unique IDs
- **BR006**: Bill Payment Recording - Detailed transaction attributes
- **BR007**: Account Balance Update - Automatic balance updates

## Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.5** - Application framework
- **PostgreSQL** - Database
- **JPA/Hibernate** - ORM
- **Flyway** - Database migrations
- **Lombok** - Reduce boilerplate code
- **OpenAPI/Swagger** - API documentation

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API endpoints
│   │   │   ├── AccountController.java
│   │   │   ├── CardCrossReferenceController.java
│   │   │   ├── TransactionController.java
│   │   │   └── BillPaymentController.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── UpdateAccountRequestDto.java
│   │   │   ├── AccountResponseDto.java
│   │   │   ├── CreateCardCrossReferenceRequestDto.java
│   │   │   ├── UpdateCardCrossReferenceRequestDto.java
│   │   │   ├── CardCrossReferenceResponseDto.java
│   │   │   ├── CreateTransactionRequestDto.java
│   │   │   ├── UpdateTransactionRequestDto.java
│   │   │   ├── TransactionResponseDto.java
│   │   │   ├── ProcessBillPaymentRequestDto.java
│   │   │   └── BillPaymentResponseDto.java
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
│   │       ├── TransactionService.java
│   │       └── BillPaymentService.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway migrations
│           ├── V1__Create_accounts_table.sql
│           ├── V2__Create_card_cross_reference_table.sql
│           ├── V3__Create_transactions_table.sql
│           └── V4__Insert_sample_data.sql
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

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/billpayment
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Build and Run

1. Clone the repository
2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Database Migrations

Flyway will automatically run migrations on startup:
- V1: Creates accounts table
- V2: Creates card_cross_reference table
- V3: Creates transactions table
- V4: Inserts sample data for testing

## API Documentation

Complete API documentation is available in [openapi-summary.md](openapi-summary.md)

### Quick Start Examples

#### 1. Preview Bill Payment
```bash
curl -X GET "http://localhost:8080/api/bill-payment/preview?acctId=ACC00001234&cardNum=4111111111111111"
```

#### 2. Process Bill Payment
```bash
curl -X POST "http://localhost:8080/api/bill-payment/process" \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": "ACC00001234",
    "cardNum": "4111111111111111",
    "confirmed": true
  }'
```

#### 3. Get Account Details
```bash
curl -X GET "http://localhost:8080/api/accounts/ACC00001234"
```

#### 4. Get Transaction History
```bash
curl -X GET "http://localhost:8080/api/transactions/by-account/ACC00001234"
```

## Sample Data

The system includes sample data for testing:

### Accounts
| Account ID   | Balance    | Card Number      |
|--------------|------------|------------------|
| ACC00001234  | $1,500.00  | 4111111111111111 |
| ACC00005678  | $2,750.50  | 4222222222222222 |
| ACC00009012  | $500.25    | 4333333333333333 |
| ACC00003456  | $0.00      | 4444444444444444 |
| ACC00007890  | $10,000.00 | 4555555555555555 |

## Bill Payment Workflow

### Complete Process Flow

1. **Account Validation (BR001)**
   - System validates account ID exists
   - Error: "Acct ID can NOT be empty..." or "Account ID NOT found..."

2. **Balance Check (BR002)**
   - System verifies account has positive balance
   - Error: "You have nothing to pay..."

3. **Card Validation**
   - System validates card is associated with account
   - Error: "Card number not associated with this account"

4. **Payment Confirmation (BR003)**
   - User must confirm payment with `confirmed: true`
   - Without confirmation, returns preview information

5. **Full Balance Payment (BR004)**
   - System processes payment for entire account balance
   - Balance is set to zero after payment

6. **Transaction ID Generation (BR005)**
   - System generates unique sequential transaction ID
   - ID is incremented from last transaction

7. **Transaction Recording (BR006)**
   - System creates transaction record with:
     - Type Code: "02" (Bill Payment)
     - Category Code: 2 (Bill Payment)
     - Source: "POS TERM"
     - Merchant ID: 999999999
     - Merchant Name: "BILL PAYMENT"
     - Timestamps: Original and Processing

8. **Balance Update (BR007)**
   - System updates account balance to zero
   - Transaction is committed atomically

## Error Handling

### Validation Errors
- Account ID must be exactly 11 characters
- Card number must be exactly 16 characters
- Transaction amount must be greater than zero
- All required fields must be provided

### Business Rule Errors
- **BR001**: "Acct ID can NOT be empty...", "Account ID NOT found..."
- **BR002**: "You have nothing to pay..."
- **BR003**: Payment requires confirmation

### HTTP Status Codes
- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `204 No Content` - Resource deleted successfully
- `400 Bad Request` - Validation or business rule error
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Database Schema

### accounts
```sql
CREATE TABLE accounts (
    acct_id VARCHAR(11) PRIMARY KEY,
    acct_curr_bal DECIMAL(13, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### card_cross_reference
```sql
CREATE TABLE card_cross_reference (
    id BIGSERIAL PRIMARY KEY,
    xref_acct_id VARCHAR(11) NOT NULL,
    xref_card_num VARCHAR(16) NOT NULL,
    acct_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (acct_id) REFERENCES accounts(acct_id)
);
```

### transactions
```sql
CREATE TABLE transactions (
    tran_id BIGINT PRIMARY KEY,
    tran_type_cd VARCHAR(2) NOT NULL DEFAULT '02',
    tran_cat_cd INTEGER NOT NULL DEFAULT 2,
    tran_source VARCHAR(10) NOT NULL DEFAULT 'POS TERM',
    tran_desc VARCHAR(50) NOT NULL,
    tran_amt DECIMAL(11, 2) NOT NULL,
    tran_card_num VARCHAR(16) NOT NULL,
    tran_merchant_id INTEGER NOT NULL,
    tran_merchant_name VARCHAR(50) NOT NULL,
    tran_merchant_city VARCHAR(50) NOT NULL,
    tran_merchant_zip VARCHAR(10) NOT NULL,
    tran_orig_ts TIMESTAMP NOT NULL,
    tran_proc_ts TIMESTAMP NOT NULL,
    acct_id VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (acct_id) REFERENCES accounts(acct_id)
);
```

## Development

### Code Generation
This project was generated using production-grade code generation following the Spring Boot archetype patterns.

### Architecture
- **Controller Layer**: REST API endpoints with validation
- **Service Layer**: Business logic and transaction management
- **Repository Layer**: Data access with JPA
- **Entity Layer**: Database models with relationships
- **DTO Layer**: Request/Response objects with validation

### Best Practices
- ✅ Separation of concerns across layers
- ✅ DTO pattern for API communication
- ✅ Bean validation annotations
- ✅ Transaction management
- ✅ Proper error handling
- ✅ Comprehensive logging
- ✅ Database migrations with Flyway
- ✅ OpenAPI documentation

## Testing

### Manual Testing
Use the provided sample data to test the bill payment workflow:

1. Preview payment for account ACC00001234
2. Process payment with confirmation
3. Verify account balance is now zero
4. Check transaction was recorded

### Integration Testing
The system includes sample data that can be used for integration testing.

## Monitoring

The application includes Spring Boot Actuator for monitoring:
- Health checks
- Metrics
- Application info

## Contributing

1. Follow the existing code structure
2. Maintain separation of concerns
3. Add proper validation and error handling
4. Update documentation for new features
5. Write tests for new functionality

## License

This project is part of the CardDemo Bill Payment Processing system.

## Support

For issues or questions, please refer to the [API Documentation](openapi-summary.md) or contact the development team.

---

**Generated**: Production-ready code following Spring Boot archetype patterns
**Version**: 1.0.0
**Last Updated**: 2024
