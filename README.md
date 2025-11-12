# Card Demo Application

A production-ready Spring Boot application for managing credit card accounts, transactions, and customer information.

## Overview

This application implements a comprehensive card management system with complete business rules for account processing, transaction management, interest calculation, and customer administration. It follows clean architecture principles with a layered design pattern.

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.5
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **Build Tool**: Maven 3.6+
- **API Documentation**: OpenAPI/Swagger
- **Utilities**: Lombok

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # Main application class
│   │   ├── controller/                   # REST API endpoints
│   │   │   └── AccountController.java
│   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── CreateAccountRequestDto.java
│   │   │   ├── UpdateAccountRequestDto.java
│   │   │   └── AccountResponseDto.java
│   │   ├── entity/                       # JPA Entities (32 entities)
│   │   │   ├── Account.java
│   │   │   ├── Card.java
│   │   │   ├── Customer.java
│   │   │   ├── Transaction.java
│   │   │   └── ... (28 more entities)
│   │   ├── repository/                   # Data Access Layer
│   │   │   └── AccountRepository.java
│   │   └── service/                      # Business Logic Layer
│   │       └── AccountService.java
│   └── resources/
│       ├── application.properties        # Application configuration
│       └── db/migration/                 # Flyway migrations
│           └── V1__Create_accounts_table.sql
└── test/                                 # Test classes
```

## Features

### Core Functionality

- **Account Management**: Complete CRUD operations for customer accounts
- **Card Management**: Credit card lifecycle management
- **Transaction Processing**: Transaction recording and validation
- **Customer Management**: Customer information and profile management
- **Interest Calculation**: Automated interest calculation with configurable rates
- **Cross-Reference Management**: Card-to-account and account-to-customer linking
- **Statement Generation**: Account statement creation
- **Reporting**: Transaction reports with date range filtering
- **User Management**: User authentication and authorization
- **Admin Functions**: Administrative operations and menu management

### Business Rules Implementation

The application implements 17+ business rules including:

- Sequential account record processing (BR-001)
- Account data display requirements (BR-002)
- File access control (BR-003)
- End-of-file detection (BR-004)
- Account processing sequence (BR-005)
- Zero interest rate handling (BR-006)
- Default interest rate fallback (BR-007)
- Transaction ID generation (BR-008)
- Cycle amount reset (BR-009)
- Account and card cross-reference (BR-010)
- Date parameter reading (BR-011)
- Page state maintenance (BR-012)
- Card information updates (BR-013)
- Forward/backward pagination (BR-014, BR-015)
- Input error protection (BR-017)

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL database
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```

2. **Configure the database**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run database migrations**
   
   Flyway will automatically run migrations on application startup.

5. **Start the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Quick Start Example

**Create an Account**:
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "12345678901",
    "activeStatus": "Y",
    "currentBalance": 1500.50,
    "creditLimit": 5000.00,
    "cashCreditLimit": 1000.00,
    "openDate": "2023-01-15",
    "expirationDate": "2026-01-15",
    "reissueDate": "2024-01-15",
    "currentCycleCredit": 500.00,
    "currentCycleDebit": 300.00,
    "groupId": "GROUP001"
  }'
```

**Get All Accounts**:
```bash
curl http://localhost:8080/api/accounts?page=0&size=20
```

**Get Account by ID**:
```bash
curl http://localhost:8080/api/accounts/12345678901
```

## API Documentation

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

Download the OpenAPI JSON specification:
```
http://localhost:8080/v3/api-docs
```

### API Summary

For a comprehensive list of all endpoints, see [openapi-summary.md](openapi-summary.md)

## Database

### Schema Management

Database schema is managed using Flyway migrations located in `src/main/resources/db/migration/`.

Migration files follow the naming convention: `V{version}__{description}.sql`

### Running Migrations

Migrations run automatically on application startup. To run manually:
```bash
mvn flyway:migrate
```

### Database Entities

The application manages 32 entities:

**Core Entities**:
- Account, Card, Customer, Transaction, User

**Reference Data**:
- TransactionType, TransactionCategory, DisclosureGroup, Merchant

**Cross-References**:
- CardCrossReference, AccountCrossReference

**Reporting**:
- Statement, TransactionReport, ReportTotals, DateParameter

**Administrative**:
- AdminUser, AdminMenuOption, MenuOption, UserSession

**Utility**:
- PageState, PaginationContext, JobSubmission, and more

## Development

### Code Generation

This project was generated using production-grade code generation tools that:
- Implement ALL business rule details
- Include complete validation logic
- Generate full CRUD operations
- Create comprehensive API documentation
- Follow archetype patterns exactly

### Adding New Features

1. **Create Entity**: Define JPA entity in `entity/` package
2. **Create DTOs**: Define request/response DTOs in `dto/` package
3. **Create Repository**: Define repository interface in `repository/` package
4. **Implement Service**: Add business logic in `service/` package
5. **Create Controller**: Define REST endpoints in `controller/` package
6. **Add Migration**: Create Flyway migration script
7. **Add Tests**: Write unit and integration tests

### Coding Standards

- Follow Spring Boot best practices
- Use Lombok to reduce boilerplate
- Implement proper exception handling
- Add comprehensive validation
- Document APIs with OpenAPI annotations
- Write meaningful commit messages

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AccountServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Categories

- **Unit Tests**: Service layer business logic
- **Integration Tests**: API endpoint testing
- **Repository Tests**: Database operations
- **Validation Tests**: Business rule validation

## Configuration

### Application Properties

Key configuration properties in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/carddemo
spring.datasource.username=postgres
spring.datasource.password=password

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
```

## Deployment

### Building for Production

```bash
# Create executable JAR
mvn clean package -DskipTests

# Run the JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t carddemo .
docker run -p 8080:8080 carddemo
```

## Monitoring

### Actuator Endpoints

Spring Boot Actuator provides monitoring endpoints:

- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`
- Metrics: `http://localhost:8080/actuator/metrics`

## Security

### Authentication

- User authentication required for all operations
- Admin operations restricted to admin users
- Sensitive data (SSN, CVV) masked in responses

### Best Practices

- Never commit sensitive credentials
- Use environment variables for configuration
- Implement proper access control
- Validate all input data
- Log security events

## Troubleshooting

### Common Issues

**Database Connection Error**:
- Verify PostgreSQL is running
- Check database credentials
- Ensure database exists

**Migration Failures**:
- Check migration file syntax
- Verify migration order
- Review Flyway logs

**Build Failures**:
- Ensure Java 21 is installed
- Clear Maven cache: `mvn clean`
- Update dependencies: `mvn clean install -U`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
- Review the [API Summary](openapi-summary.md)
- Check the [Archetype Guide](archetype.md)
- Review business rules documentation
- Open an issue on GitHub

## Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community
- All contributors to this project

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Application**: Card Demo (CBACT01C)  
**Framework**: Spring Boot 3.5.5  
**Java**: 21
