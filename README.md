# Card Services Account and Payment Processing System

## Overview

This is a modernized implementation of a Card Services Account and Payment Processing system, originally based on COBOL mainframe programs. The system has been completely rewritten using Spring Boot and follows modern software development best practices while preserving all original business rules and validation logic.

## Original COBOL Programs Modernized

This implementation modernizes the following COBOL programs:

1. **COACTUPC** - Account Update Processing Program
2. **COACTVWC** - Account View Business Logic Program
3. **COBIL00C** - Bill Payment Processing Program
4. **COCRDLIC** - Credit Card List Management Program
5. **COCRDSLC** - Credit Card Detail View Program
6. **COCRDUPC** - Credit Card Update Program

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate**
- **Jakarta Bean Validation**
- **Lombok**
- **Flyway** (for database migrations)
- **H2/PostgreSQL/MySQL** (database)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # REST API Controllers
│   │   │   ├── AccountController.java
│   │   │   ├── CustomerController.java
│   │   │   ├── CardController.java
│   │   │   └── TransactionController.java
│   │   ├── service/             # Business Logic Services
│   │   │   ├── AccountService.java
│   │   │   ├── CustomerService.java
│   │   │   ├── CardService.java
│   │   │   └── TransactionService.java
│   │   ├── repository/          # Data Access Layer
│   │   │   ├── AccountRepository.java
│   │   │   ├── CustomerRepository.java
│   │   │   ├── CardRepository.java
│   │   │   ├── TransactionRepository.java
│   │   │   └── CardCrossReferenceRepository.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── Account.java
│   │   │   ├── Customer.java
│   │   │   ├── Card.java
│   │   │   ├── Transaction.java
│   │   │   └── CardCrossReference.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── AccountDTO.java
│   │   │   ├── AccountCreateDTO.java
│   │   │   ├── AccountUpdateDTO.java
│   │   │   ├── CustomerDTO.java
│   │   │   ├── CustomerCreateDTO.java
│   │   │   ├── CustomerUpdateDTO.java
│   │   │   ├── CardDTO.java
│   │   │   ├── CardCreateDTO.java
│   │   │   ├── CardUpdateDTO.java
│   │   │   ├── CardListDTO.java
│   │   │   ├── TransactionDTO.java
│   │   │   └── BillPaymentDTO.java
│   │   └── exception/           # Exception Handling
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── db/migration/        # Flyway Migration Scripts
│       │   └── V1__create_card_services_tables.sql
│       └── application.properties
```

## Database Schema

The system uses the following main tables:

- **accounts** - Account master data
- **customers** - Customer information
- **cards** - Credit card information
- **transactions** - Transaction records
- **card_cross_references** - Relationships between accounts, cards, and customers

## Key Features

### Account Management
- Create, read, update, and delete accounts
- Comprehensive validation for monetary amounts, dates, and status codes
- Automatic calculation of available credit
- Support for account grouping

### Customer Management
- Full customer lifecycle management
- Validation for SSN, phone numbers, addresses
- FICO credit score tracking (300-850 range)
- Support for primary and secondary cardholders

### Card Management
- Card creation and updates
- Paginated card listing with filtering
- Card expiration tracking
- Active/inactive status management
- Embossed name validation (alphabets and spaces only)

### Payment Processing
- Full bill payment processing
- Automatic transaction ID generation
- Balance updates with transaction recording
- Transaction history tracking

## Business Rules Preserved

All original COBOL business rules have been preserved, including:

1. **Account Validation**:
   - Account ID must be 11 digits
   - Credit limits must be positive
   - Dates must be valid and logical
   - Status must be Y or N

2. **Customer Validation**:
   - SSN must follow SSA rules (no 000, 666, or 900-999 in first 3 digits)
   - Phone numbers must be in (XXX)XXX-XXXX format
   - State and ZIP code cross-validation
   - FICO score range validation (300-850)
   - Names can only contain alphabets and spaces

3. **Card Validation**:
   - Card number must be 16 digits
   - CVV must be 3 digits
   - Expiration date must be in the future
   - Embossed name alphabetic validation

4. **Payment Processing**:
   - Full balance payment only
   - Transaction ID sequencing
   - Automatic balance zeroing
   - Transaction type and category coding

## API Documentation

Complete API documentation is available in `openapi-summary.md`.

### Quick Start Examples

#### Create an Account
```bash
POST /api/accounts
Content-Type: application/json

{
  "accountId": "12345678901",
  "activeStatus": "Y",
  "creditLimit": 5000.00,
  "cashCreditLimit": 1000.00,
  "openDate": "2024-01-01",
  "expirationDate": "2029-01-01"
}
```

#### Process Bill Payment
```bash
POST /api/transactions/bill-payment
Content-Type: application/json

{
  "accountId": "12345678901",
  "confirmPayment": "Y"
}
```

#### Get Cards List (Paginated)
```bash
GET /api/cards/list?accountId=12345678901&page=0&size=7
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Database (H2 for development, PostgreSQL/MySQL for production)

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:cardservices
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway Configuration
spring.flyway.enabled=true
```

## Testing

The system includes comprehensive validation at multiple levels:

1. **Input Validation**: Jakarta Bean Validation annotations on DTOs
2. **Business Logic Validation**: Service layer validation
3. **Database Constraints**: Database-level constraints

## Migration from COBOL

This implementation maintains 100% compatibility with the original COBOL business rules while providing:

- RESTful API instead of CICS screens
- Relational database instead of VSAM files
- Modern validation framework instead of COBOL validation routines
- Transaction management instead of CICS SYNCPOINT
- Exception handling instead of CICS ABEND handling

## Future Enhancements

Potential areas for enhancement:

1. Security and authentication (OAuth2/JWT)
2. Audit logging
3. Event-driven architecture
4. Microservices decomposition
5. Real-time notifications
6. Advanced reporting and analytics

## License

[Your License Here]

## Contact

[Your Contact Information]
