# Account and Customer Management System

A Spring Boot REST API application for managing accounts, customers, transactions, and card cross-references.

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL database

### Setup

1. **Clone the repository**
   ```bash
   cd \tmp\archetypes\trentinrossi_archetype-java-spring_0b9297ee-9e4e-4667-b626-41c083721897
   ```

2. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

3. **Build the Application**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📋 Features

### Entities
- **Account** - Account master data with balances, credit limits, and status
- **Customer** - Customer master data with demographics and contact information
- **Transaction** - Credit card transaction records with composite keys
- **CardCrossReference** - Relationships between cards, customers, and accounts

### Capabilities
- ✅ Full CRUD operations for all entities
- ✅ Pagination support on all list endpoints
- ✅ Sequential access patterns for transactions and card references
- ✅ Random access patterns for accounts and customers
- ✅ Comprehensive validation at entity and service layers
- ✅ Automatic timestamp management
- ✅ Database migrations with Flyway
- ✅ OpenAPI/Swagger documentation

## 🔌 API Endpoints

### Account Management
```
GET    /api/accounts                    - List all accounts
GET    /api/accounts/{accountId}        - Get account by ID
POST   /api/accounts                    - Create new account
PUT    /api/accounts/{accountId}        - Update account
DELETE /api/accounts/{accountId}        - Delete account
```

### Customer Management
```
GET    /api/customers                   - List all customers
GET    /api/customers/{id}              - Get customer by ID
POST   /api/customers                   - Create new customer
PUT    /api/customers/{id}              - Update customer
DELETE /api/customers/{id}              - Delete customer
```

### Transaction Management
```
GET    /api/transactions                                    - List all transactions
GET    /api/transactions/{cardNumber}/{transactionId}      - Get by composite key
GET    /api/transactions/card/{cardNumber}                 - Get by card number
POST   /api/transactions                                    - Create new transaction
PUT    /api/transactions/{cardNumber}/{transactionId}      - Update transaction
DELETE /api/transactions/{cardNumber}/{transactionId}      - Delete transaction
```

### Card Cross Reference Management
```
GET    /api/card-cross-references                  - List all references
GET    /api/card-cross-references/{id}             - Get by ID
GET    /api/card-cross-references/card/{cardNumber} - Get by card number
POST   /api/card-cross-references                  - Create new reference
PUT    /api/card-cross-references/{id}             - Update reference
DELETE /api/card-cross-references/{id}             - Delete reference
```

## 📊 Database Schema

### Tables
1. **accounts** - Account master data
2. **customers** - Customer master data
3. **transactions** - Transaction records (composite key)
4. **card_cross_reference** - Card-customer-account relationships

All tables include:
- Automatic timestamp tracking (created_at, updated_at)
- Proper indexes for performance
- Unique constraints where applicable

## 🏗️ Architecture

The application follows a clean layered architecture:

```
┌─────────────────────────────────────┐
│         Controller Layer            │  REST API endpoints
├─────────────────────────────────────┤
│          Service Layer              │  Business logic
├─────────────────────────────────────┤
│        Repository Layer             │  Data access
├─────────────────────────────────────┤
│          Entity Layer               │  Database models
└─────────────────────────────────────┘
```

### Project Structure
```
src/main/java/com/example/demo/
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
├── entity/              # JPA Entities
├── repository/          # Data Access Layer
└── service/             # Business Logic Layer

src/main/resources/
├── application.properties
└── db/migration/        # Flyway migrations
```

## 🔒 Validation Rules

### Account
- Account ID: Must be 11 numeric digits
- Account Data: Required, max 289 characters

### Customer
- Customer ID: Must be 9 numeric characters
- Customer Data: Required, max 491 characters

### Transaction
- Card Number: Must be exactly 16 characters
- Transaction ID: Must be exactly 16 characters
- Transaction Data: Required, max 318 characters

### Card Cross Reference
- Card Number: Must be exactly 16 characters
- Cross Reference Data: Required, max 34 characters

## 📚 Documentation

- **[API Documentation](openapi-summary.md)** - Complete API reference
- **[Generation Summary](GENERATION_SUMMARY.md)** - Code generation details
- **[Archetype Guide](archetype.md)** - Architecture patterns and conventions

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 3.5.5 |
| Language | Java 21 |
| Database | PostgreSQL |
| ORM | Spring Data JPA |
| Migration | Flyway |
| Documentation | OpenAPI 3.0 (Swagger) |
| Build Tool | Maven |
| Utilities | Lombok |

## 📝 Example Usage

### Create an Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 12345678901,
    "accountData": "Balance: $1000.00, Credit Limit: $5000.00, Status: Active"
  }'
```

### Get All Customers (Paginated)
```bash
curl http://localhost:8080/api/customers?page=0&size=20
```

### Create a Transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "1234567890123456",
    "transactionId": "TXN1234567890123",
    "transactionData": "AMT:100.00|DATE:2024-01-15|MERCHANT:ACME Store|STATUS:APPROVED"
  }'
```

## 🧪 Testing

Access the Swagger UI to test all endpoints interactively:
```
http://localhost:8080/swagger-ui.html
```

## 📈 Business Rules Implemented

- **BR001**: File Operation Dispatcher - REST API routing
- **BR002**: Sequential Access Pattern for Transactions
- **BR003**: Sequential Access Pattern for Card Cross References
- **BR004**: Random Access Pattern for Customers
- **BR005**: Random Access Pattern for Accounts
- **BR008**: Key Extraction for Random Access

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

## 🚧 Future Enhancements

- [ ] Add Spring Security for authentication
- [ ] Implement JWT token-based authorization
- [ ] Add comprehensive unit and integration tests
- [ ] Implement caching with Redis
- [ ] Add monitoring with Spring Boot Actuator
- [ ] Implement rate limiting
- [ ] Add API versioning
- [ ] Create Docker containerization

## 📄 License

This project was generated using the Spring Boot archetype and follows enterprise-grade patterns and best practices.

## 🤝 Contributing

This is a generated project. For modifications:
1. Follow the existing architecture patterns
2. Maintain the layered structure
3. Add appropriate tests
4. Update documentation

## 📞 Support

For issues or questions:
- Review the [API Documentation](openapi-summary.md)
- Check the [Archetype Guide](archetype.md)
- Examine the business rules in `.codegen/business_rules/`

---

**Generated:** 2024  
**Version:** 1.0.0  
**Macro-functionality:** Account and Customers Management System
