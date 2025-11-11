# Card Account Transaction Management System

A comprehensive Spring Boot application for managing card accounts, transactions, and generating account statements with dual format output (plain text and HTML).

## Overview

This system implements a complete card account transaction management solution with the following capabilities:

- **Customer Management**: Complete customer information including address and FICO credit scores
- **Account Management**: Financial account tracking with balance and credit limit management
- **Card Management**: Card issuance and linkage to customers and accounts
- **Transaction Processing**: Transaction recording with grouping by card and amount summation
- **Statement Generation**: Automated statement generation in both plain text and HTML formats

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database
- **Flyway** - Database migration management
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

## Architecture

The application follows a clean layered architecture:

```
src/main/java/com/example/demo/
├── controller/      # REST API endpoints
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── enums/          # Enumeration types
├── repository/     # Data access layer
└── service/        # Business logic layer
```

## Business Rules Implementation

The system implements 10 comprehensive business rules:

1. **BR001**: Transaction Grouping by Card - Transactions are grouped by card number for efficient processing
2. **BR002**: Statement Generation Per Account - Comprehensive statements generated for each account
3. **BR003**: Transaction Amount Summation - All transaction amounts are summed for statement periods
4. **BR004**: Dual Format Statement Output - Statements available in both plain text and HTML formats
5. **BR005**: Transaction Table Capacity Limit - Maximum 10 transactions per card
6. **BR006**: Customer Name Composition - Full names composed from first, middle (if present), and last names
7. **BR007**: Complete Address Display - Complete addresses with all available lines, state, country, and ZIP
8. **BR008**: HTML Statement Styling Standards - HTML statements follow specific styling standards
9. **BR009**: Card-Account-Customer Linkage - Proper linkage between cards, accounts, and customers
10. **BR010**: Data Validation - Comprehensive validation at entity, DTO, and service levels

## Database Schema

### Entities

1. **Customer** - Customer information with address and FICO score
2. **Account** - Financial accounts with balance and credit limit
3. **Card** - Cards linked to customers and accounts
4. **Transaction** - Financial transactions associated with cards
5. **Statement** - Generated account statements with dual format output

### Relationships

- Customer → Account (One-to-Many)
- Customer → Card (One-to-Many)
- Account → Card (One-to-Many)
- Card → Transaction (One-to-Many)
- Customer → Statement (One-to-Many)
- Account → Statement (One-to-Many)

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL database

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE card_management;
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/card_management
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

### Database Migrations

Flyway migrations will run automatically on application startup. The following tables will be created:

- V1: customers
- V2: accounts
- V3: cards
- V4: transactions
- V5: statements

## API Documentation

Complete API documentation is available in [openapi-summary.md](openapi-summary.md).

### Quick API Reference

#### Customer APIs
- `GET /api/customers` - Get all customers
- `POST /api/customers` - Create customer
- `GET /api/customers/{id}` - Get customer by ID
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

#### Account APIs
- `GET /api/accounts` - Get all accounts
- `POST /api/accounts` - Create account
- `GET /api/accounts/{id}` - Get account by ID
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Delete account

#### Card APIs
- `GET /api/cards` - Get all cards
- `POST /api/cards` - Create card
- `GET /api/cards/{cardNumber}` - Get card by number
- `PUT /api/cards/{cardNumber}` - Update card
- `DELETE /api/cards/{cardNumber}` - Delete card

#### Transaction APIs
- `GET /api/transactions` - Get all transactions
- `POST /api/transactions` - Create transaction
- `GET /api/transactions/{id}` - Get transaction by ID
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

#### Statement APIs
- `GET /api/statements` - Get all statements
- `POST /api/statements` - Generate statement
- `GET /api/statements/{id}` - Get statement by ID
- `GET /api/statements/{id}/plain-text` - Get plain text format
- `GET /api/statements/{id}/html` - Get HTML format

## Data Validation

### Customer Validation
- Customer ID: 9 digits (100000000-999999999)
- First Name: Required, max 25 characters
- Last Name: Required, max 25 characters
- Address Line 1: Required, max 50 characters
- State Code: Required, exactly 2 characters
- Country Code: Required, exactly 3 characters
- ZIP Code: Required, exactly 10 alphanumeric characters
- FICO Score: Required, 0-999

### Account Validation
- Account ID: 11 digits (10000000000-99999999999)
- Current Balance: Required, max 10 integer digits, 2 decimal places
- Credit Limit: Required, max 10 integer digits, 2 decimal places

### Card Validation
- Card Number: Required, exactly 16 alphanumeric characters
- Maximum 10 transactions per card

### Transaction Validation
- Transaction ID: Required, exactly 16 alphanumeric characters, unique
- Transaction Type Code: Required, 2 characters
- Transaction Category Code: Required, 4 digits
- Transaction Source: Required, max 10 characters
- Transaction Description: Required, max 100 characters
- Transaction Amount: Required, signed numeric with 2 decimal places

## Example Usage

### 1. Create a Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 123456789,
    "firstName": "John",
    "middleName": "Michael",
    "lastName": "Doe",
    "addressLine1": "123 Main Street",
    "addressLine2": "Apt 4B",
    "stateCode": "CA",
    "countryCode": "USA",
    "zipCode": "90210-1234",
    "ficoCreditScore": 750
  }'
```

### 2. Create an Account

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 12345678901,
    "currentBalance": 0.00,
    "creditLimit": 5000.00,
    "customerId": 123456789
  }'
```

### 3. Create a Card

```bash
curl -X POST http://localhost:8080/api/cards \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "1234567890123456",
    "customerId": 123456789,
    "accountId": 12345678901
  }'
```

### 4. Create a Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "1234567890123456",
    "transactionId": "TXN1234567890123",
    "transactionTypeCode": "PU",
    "transactionCategoryCode": "5411",
    "transactionSource": "POS",
    "transactionDescription": "Grocery Store Purchase",
    "transactionAmount": -50.25
  }'
```

### 5. Generate a Statement

```bash
curl -X POST http://localhost:8080/api/statements \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 12345678901,
    "statementPeriodStart": "2024-01-01T00:00:00",
    "statementPeriodEnd": "2024-01-31T23:59:59"
  }'
```

### 6. Get HTML Statement

```bash
curl http://localhost:8080/api/statements/1/html
```

## Statement Generation

The statement generation process implements multiple business rules:

1. **Customer Name Composition (BR006)**: Automatically composes full name from first, middle (if present), and last name
2. **Complete Address Display (BR007)**: Formats complete address with all available lines
3. **Transaction Amount Summation (BR003)**: Calculates total transaction amount for the period
4. **Dual Format Output (BR004)**: Generates both plain text and HTML formats
5. **HTML Styling Standards (BR008)**: Applies specific styling to HTML statements

### Plain Text Format Example

```
================================================================================
Financial Services Bank
123 Main Street, New York, NY 10001
================================================================================

CUSTOMER INFORMATION
--------------------------------------------------------------------------------
Name: John Michael Doe
Address: 123 Main Street, Apt 4B, CA, USA 90210-1234

ACCOUNT DETAILS
--------------------------------------------------------------------------------
Account ID: 12345678901
Current Balance: $1500.50
FICO Score: 750

TRANSACTION SUMMARY
--------------------------------------------------------------------------------
Total Transaction Amount: $-250.75
================================================================================
```

### HTML Format Features

- Responsive table layout
- Color-coded sections:
  - Bank info: Orange background (#FFAF33)
  - Customer info: Light gray background (#f2f2f2)
  - Section headers: Cyan background (#33FFD1)
  - Column headers: Green background (#33FF5E)
- Professional typography (Segoe UI font)
- Right-aligned monetary amounts

## Error Handling

The application provides comprehensive error handling:

- **400 Bad Request**: Validation errors or business rule violations
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resource or constraint violation
- **500 Internal Server Error**: Server error

Error responses include:
- Timestamp
- HTTP status code
- Error message
- Request path

## Testing

Run tests with:

```bash
mvn test
```

## Project Structure

```
card-account-transaction-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── AccountController.java
│   │   │   │   ├── CardController.java
│   │   │   │   ├── TransactionController.java
│   │   │   │   └── StatementController.java
│   │   │   ├── dto/
│   │   │   │   ├── CreateCustomerRequestDto.java
│   │   │   │   ├── UpdateCustomerRequestDto.java
│   │   │   │   ├── CustomerResponseDto.java
│   │   │   │   └── ... (other DTOs)
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Account.java
│   │   │   │   ├── Card.java
│   │   │   │   ├── Transaction.java
│   │   │   │   └── Statement.java
│   │   │   ├── enums/
│   │   │   │   └── StatementStatus.java
│   │   │   ├── repository/
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── AccountRepository.java
│   │   │   │   ├── CardRepository.java
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   └── StatementRepository.java
│   │   │   └── service/
│   │   │       ├── CustomerService.java
│   │   │       ├── AccountService.java
│   │   │       ├── CardService.java
│   │   │       ├── TransactionService.java
│   │   │       └── StatementService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__Create_customers_table.sql
│   │           ├── V2__Create_accounts_table.sql
│   │           ├── V3__Create_cards_table.sql
│   │           ├── V4__Create_transactions_table.sql
│   │           └── V5__Create_statements_table.sql
│   └── test/
├── openapi-summary.md
├── README.md
└── pom.xml
```

## Generated Files

**Total Files Generated:** 40

- **Entities:** 5
- **Enums:** 1
- **DTOs:** 14
- **Repositories:** 5
- **Services:** 5
- **Controllers:** 5
- **Database Migrations:** 5

## Contributing

This is a generated codebase following the Spring Boot archetype patterns. All code is production-ready and implements complete business rules.

## License

This project is generated as part of a code generation system.

## Support

For API documentation, see [openapi-summary.md](openapi-summary.md)

---

**Generated:** 2024  
**System:** Card Account Transaction Management System  
**Version:** 1.0
