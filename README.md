# Account and Customer Management System

A comprehensive Spring Boot application for managing accounts, customers, card cross-references, and transactions.

## Overview

This system provides RESTful APIs for:
- **Customer Management**: Create, read, update, and delete customer records with demographic information
- **Account Management**: Manage account balances, credit limits, dates, and status information
- **Card Cross-Reference Management**: Maintain relationships between card numbers, customers, and accounts
- **Transaction Management**: Process and track credit card transactions

## Features

- ✅ RESTful API design following best practices
- ✅ Comprehensive validation and error handling
- ✅ Database migrations with Flyway
- ✅ OpenAPI/Swagger documentation
- ✅ Actuator endpoints for monitoring
- ✅ Prometheus metrics integration
- ✅ Structured logging
- ✅ Multi-environment configuration (dev, prod)

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI 3
- **Database Migration**: Flyway
- **Monitoring**: Spring Actuator + Prometheus

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Docker (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd account-customer-management
```

### 2. Configure Database

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/account_customer_db
    username: your_username
    password: your_password
```

Or set environment variables:

```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run Database Migrations

```bash
mvn flyway:migrate
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## API Endpoints

### Customer Management

- `GET /api/customers` - Get all customers (paginated)
- `GET /api/customers/{customerId}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{customerId}` - Update customer
- `DELETE /api/customers/{customerId}` - Delete customer

### Account Management

- `GET /api/accounts` - Get all accounts (paginated)
- `GET /api/accounts/{accountId}` - Get account by ID
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{accountId}` - Update account
- `DELETE /api/accounts/{accountId}` - Delete account

### Card Cross-Reference Management

- `GET /api/card-cross-references` - Get all card cross-references (paginated)
- `GET /api/card-cross-references/{cardNumber}` - Get by card number
- `GET /api/card-cross-references/customer/{customerId}` - Get by customer ID
- `GET /api/card-cross-references/account/{accountId}` - Get by account ID
- `POST /api/card-cross-references` - Create new card cross-reference
- `PUT /api/card-cross-references/{cardNumber}` - Update card cross-reference
- `DELETE /api/card-cross-references/{cardNumber}` - Delete card cross-reference

### Transaction Management

- `GET /api/transactions` - Get all transactions (paginated)
- `GET /api/transactions/{cardNumber}/{transactionId}` - Get by composite key
- `GET /api/transactions/card/{cardNumber}` - Get by card number
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{cardNumber}/{transactionId}` - Update transaction
- `DELETE /api/transactions/{cardNumber}/{transactionId}` - Delete transaction

## Monitoring

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Metrics

```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus Metrics

```bash
curl http://localhost:8080/actuator/prometheus
```

## Docker Deployment

### Build Docker Image

```bash
docker build -t account-customer-management:1.0.0 .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

This will start:
- Application container
- MySQL database container

## Configuration Profiles

### Development Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Features:
- Detailed logging
- SQL query logging
- H2 console enabled

### Production Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Features:
- Optimized logging
- Enhanced security
- Performance tuning

## Database Schema

### Customers Table
- `customer_id` (BIGINT, PK): 9-digit numeric customer identifier
- `customer_data` (VARCHAR(491)): Complete customer profile
- `created_at`, `updated_at`: Audit timestamps

### Accounts Table
- `account_id` (BIGINT, PK): 11-digit numeric account identifier
- `account_data` (VARCHAR(289)): Account details
- `customer_id` (BIGINT, FK): Reference to customer
- `created_at`, `updated_at`: Audit timestamps

### Card Cross-References Table
- `card_number` (VARCHAR(16), PK): 16-character card number
- `cross_reference_data` (VARCHAR(34)): Relationship data
- `customer_id` (BIGINT, FK): Reference to customer
- `account_id` (BIGINT, FK): Reference to account
- `created_at`, `updated_at`: Audit timestamps

### Transactions Table
- `card_number` (VARCHAR(16), PK): Card number
- `transaction_id` (VARCHAR(16), PK): Transaction identifier
- `transaction_data` (VARCHAR(318)): Transaction details
- `created_at`, `updated_at`: Audit timestamps

## Business Rules

### Customer Validation
- Customer ID must be exactly 9 numeric digits
- Customer data is required and cannot exceed 491 characters

### Account Validation
- Account ID must be exactly 11 numeric digits
- Account data is required and cannot exceed 289 characters
- Must be associated with a valid customer

### Card Cross-Reference Validation
- Card number must be exactly 16 characters
- Must link to valid customer and account

### Transaction Validation
- Card number must be exactly 16 characters
- Transaction ID must be exactly 16 characters
- Transaction data cannot exceed 318 characters

## Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

## Troubleshooting

### Database Connection Issues

1. Verify MySQL is running:
```bash
mysql -u root -p
```

2. Check database exists:
```sql
SHOW DATABASES;
```

3. Verify credentials in `application.yml`

### Port Already in Use

Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For issues and questions:
- Create an issue in the repository
- Contact: support@example.com

## Version History

- **1.0.0** (2024-01-15): Initial release
  - Customer management
  - Account management
  - Card cross-reference management
  - Transaction management
