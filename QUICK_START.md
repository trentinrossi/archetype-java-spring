# Quick Start Guide

Get the Account and Customer Management System up and running in 5 minutes!

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+ (or use Docker)

## Option 1: Quick Start with Docker (Recommended)

### Step 1: Start the Application

```bash
docker-compose up -d
```

This will start:
- MySQL database on port 3306
- Application on port 8080

### Step 2: Verify It's Running

```bash
# Check health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

### Step 3: Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### Step 4: Test the API

Try creating a customer:

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123456789",
    "customerData": "John Doe, 123 Main St, New York, NY 10001, john.doe@example.com, SSN: 123-45-6789, Credit Score: 750"
  }'
```

**Success!** You should receive a 201 Created response with the customer details.

---

## Option 2: Local Development Setup

### Step 1: Setup MySQL Database

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE account_customer_db;

# Create user (optional)
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'apppassword';
GRANT ALL PRIVILEGES ON account_customer_db.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```

### Step 2: Configure Application

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/account_customer_db
    username: root  # or appuser
    password: password  # or apppassword
```

Or set environment variables:

```bash
export DB_USERNAME=root
export DB_PASSWORD=password
```

### Step 3: Build and Run

```bash
# Build the application
mvn clean install

# Run database migrations
mvn flyway:migrate

# Start the application
mvn spring-boot:run
```

### Step 4: Verify It's Running

```bash
curl http://localhost:8080/actuator/health
```

---

## Testing the Complete Workflow

### 1. Create a Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123456789",
    "customerData": "Jane Smith, 456 Oak Ave, Los Angeles, CA 90001, jane.smith@example.com, SSN: 987-65-4321, Credit Score: 800"
  }'
```

**Response:**
```json
{
  "customerId": "123456789",
  "customerData": "Jane Smith, 456 Oak Ave, Los Angeles, CA 90001, jane.smith@example.com, SSN: 987-65-4321, Credit Score: 800",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 2. Create an Account for the Customer

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "12345678901",
    "accountData": "Checking Account, Balance: $5000.00, Credit Limit: $10000.00, Status: Active, Open Date: 2024-01-01",
    "customerId": 123456789
  }'
```

**Response:**
```json
{
  "accountId": "12345678901",
  "accountData": "Checking Account, Balance: $5000.00, Credit Limit: $10000.00, Status: Active, Open Date: 2024-01-01",
  "customerId": 123456789,
  "createdAt": "2024-01-15T10:31:00",
  "updatedAt": "2024-01-15T10:31:00"
}
```

### 3. Create a Card Cross-Reference

```bash
curl -X POST http://localhost:8080/api/card-cross-references \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4532123456789012",
    "crossReferenceData": "CUST123456789ACCT12345678901",
    "customerId": 123456789,
    "accountId": 12345678901
  }'
```

**Response:**
```json
{
  "cardNumber": "4532123456789012",
  "crossReferenceData": "CUST123456789ACCT12345678901",
  "customerId": "123456789",
  "accountId": "12345678901",
  "createdAt": "2024-01-15T10:32:00",
  "updatedAt": "2024-01-15T10:32:00"
}
```

### 4. Create a Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4532123456789012",
    "transactionId": "TXN2024011500001",
    "transactionData": "Merchant: Amazon.com, Amount: $129.99, Date: 2024-01-15, Time: 10:30:00, Status: Approved, Category: Online Shopping"
  }'
```

**Response:**
```json
{
  "cardNumber": "4532123456789012",
  "transactionId": "TXN2024011500001",
  "transactionData": "Merchant: Amazon.com, Amount: $129.99, Date: 2024-01-15, Time: 10:30:00, Status: Approved, Category: Online Shopping",
  "createdAt": "2024-01-15T10:33:00",
  "updatedAt": "2024-01-15T10:33:00"
}
```

### 5. Retrieve All Transactions for a Card

```bash
curl http://localhost:8080/api/transactions/card/4532123456789012
```

### 6. Get Customer Details

```bash
curl http://localhost:8080/api/customers/123456789
```

### 7. Get All Accounts (Paginated)

```bash
curl "http://localhost:8080/api/accounts?page=0&size=10&sort=accountId,asc"
```

---

## Common Operations

### List All Customers
```bash
curl http://localhost:8080/api/customers
```

### Update a Customer
```bash
curl -X PUT http://localhost:8080/api/customers/123456789 \
  -H "Content-Type: application/json" \
  -d '{
    "customerData": "Updated customer information..."
  }'
```

### Delete a Customer
```bash
curl -X DELETE http://localhost:8080/api/customers/123456789
```

### Get Card Cross-References by Customer
```bash
curl http://localhost:8080/api/card-cross-references/customer/123456789
```

### Get Card Cross-References by Account
```bash
curl http://localhost:8080/api/card-cross-references/account/12345678901
```

---

## Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Application Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus Metrics
```bash
curl http://localhost:8080/actuator/prometheus
```

---

## Stopping the Application

### Docker
```bash
docker-compose down
```

### Local Development
Press `Ctrl+C` in the terminal where the application is running.

---

## Troubleshooting

### Port 8080 Already in Use

Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

### Database Connection Failed

1. Verify MySQL is running:
```bash
mysql -u root -p
```

2. Check database exists:
```sql
SHOW DATABASES;
```

3. Verify credentials in `application.yml`

### Docker Issues

```bash
# View logs
docker-compose logs -f app

# Restart services
docker-compose restart

# Clean restart
docker-compose down -v
docker-compose up -d
```

---

## Next Steps

1. **Explore the API**: Open Swagger UI at http://localhost:8080/swagger-ui.html
2. **Read the Documentation**: Check `README.md` for detailed information
3. **Review API Endpoints**: See `openapi-summary.md` for complete API documentation
4. **Customize**: Modify the code to fit your specific requirements

---

## Quick Reference

| Resource | URL |
|----------|-----|
| Application | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs | http://localhost:8080/api-docs |
| Health Check | http://localhost:8080/actuator/health |
| Metrics | http://localhost:8080/actuator/metrics |

| Endpoint Base | Description |
|---------------|-------------|
| /api/customers | Customer management |
| /api/accounts | Account management |
| /api/card-cross-references | Card cross-reference management |
| /api/transactions | Transaction management |

---

## Support

For issues or questions:
- Check `README.md` for detailed documentation
- Review `GENERATION_SUMMARY.md` for technical details
- Consult `openapi-summary.md` for API specifications

**Happy coding!** 🚀
