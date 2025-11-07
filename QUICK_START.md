# Quick Start Guide

Get the Account Management System up and running in minutes!

---

## Prerequisites

Ensure you have the following installed:

- ✅ **Java 21** or higher
- ✅ **Maven 3.6+**
- ✅ **PostgreSQL 12+** (or use H2 for testing)
- ✅ **Git** (optional)

---

## Step 1: Configure Database

### Option A: PostgreSQL (Recommended for Production)

1. Create a database:
```sql
CREATE DATABASE accountdb;
```

2. Copy the example configuration:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Edit `application.properties` and update:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/accountdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Option B: H2 In-Memory (Quick Testing)

Create `src/main/resources/application.properties`:
```properties
spring.application.name=account-management-system
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Flyway - Disabled for H2
spring.flyway.enabled=false

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## Step 2: Build the Application

```bash
mvn clean package
```

This will:
- Compile all Java code
- Run all tests
- Create executable JAR file

---

## Step 3: Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

You should see:
```
Started DemoApplication in X.XXX seconds
```

---

## Step 4: Verify It's Working

### Check Health
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

### Access Swagger UI
Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

You should see the interactive API documentation.

---

## Step 5: Test the API

### Create Your First Account

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "acctId": 10000000001,
    "acctActiveStatus": "A",
    "acctCurrBal": 1500.50,
    "acctCreditLimit": 5000.00,
    "acctCashCreditLimit": 1000.00,
    "acctOpenDate": "2024-01-15",
    "acctExpirationDate": "2027-01-15",
    "acctCurrCycCredit": 250.00,
    "acctCurrCycDebit": 150.00,
    "acctGroupId": "GRP001"
  }'
```

### Get the Account

```bash
curl http://localhost:8080/api/accounts/10000000001
```

### Get All Accounts

```bash
curl http://localhost:8080/api/accounts
```

---

## Step 6: Load Sample Data (Optional)

If using PostgreSQL with Flyway enabled, sample data is automatically loaded from:
```
src/main/resources/db/migration/V2__Insert_sample_accounts.sql
```

This creates 15 test accounts with various scenarios.

To verify:
```bash
curl http://localhost:8080/api/accounts/count/active
```

Should return: `8` (or more if you created additional accounts)

---

## Common Operations

### Get Active Accounts
```bash
curl http://localhost:8080/api/accounts/active
```

### Get Accounts by Group
```bash
curl http://localhost:8080/api/accounts/group/GRP001
```

### Process Accounts Sequentially (BR-001)
```bash
curl http://localhost:8080/api/accounts/process-sequential
```

### Get Total Balance
```bash
curl http://localhost:8080/api/accounts/total-balance
```

### Update an Account
```bash
curl -X PUT http://localhost:8080/api/accounts/10000000001 \
  -H "Content-Type: application/json" \
  -d '{
    "acctActiveStatus": "I",
    "acctCurrBal": 2000.00
  }'
```

### Delete an Account
```bash
curl -X DELETE http://localhost:8080/api/accounts/10000000001
```

---

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AccountServiceTest
```

### Run Integration Tests
```bash
mvn verify
```

---

## Troubleshooting

### Issue: Port 8080 already in use

**Solution**: Change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: Database connection failed

**Solution**: Verify PostgreSQL is running:
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Start PostgreSQL if needed
sudo systemctl start postgresql
```

### Issue: Flyway migration failed

**Solution**: Reset the database:
```sql
DROP DATABASE accountdb;
CREATE DATABASE accountdb;
```

Then restart the application.

### Issue: Tests failing

**Solution**: Ensure H2 dependency is in pom.xml for tests:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Development Tips

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.com.example.demo=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Hot Reload with DevTools

DevTools is already included. Just rebuild the project (Ctrl+F9 in IntelliJ) and changes will be applied automatically.

### Access H2 Console (if using H2)

Navigate to:
```
http://localhost:8080/h2-console
```

Connection settings:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

---

## IDE Setup

### IntelliJ IDEA

1. Open the project folder
2. Wait for Maven to import dependencies
3. Right-click `DemoApplication.java` → Run
4. Access Swagger UI at `http://localhost:8080/swagger-ui.html`

### Eclipse

1. File → Import → Existing Maven Projects
2. Select the project folder
3. Right-click project → Run As → Spring Boot App
4. Access Swagger UI at `http://localhost:8080/swagger-ui.html`

### VS Code

1. Open the project folder
2. Install "Spring Boot Extension Pack"
3. Press F5 to run
4. Access Swagger UI at `http://localhost:8080/swagger-ui.html`

---

## Next Steps

1. ✅ **Explore the API**: Use Swagger UI to test all endpoints
2. ✅ **Review the Code**: Check out the generated classes
3. ✅ **Read Documentation**: See `GENERATED_CODE_README.md` and `API_DOCUMENTATION.md`
4. ✅ **Run Tests**: Execute `mvn test` to see all tests pass
5. ✅ **Customize**: Add your own business logic and endpoints

---

## Useful URLs

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| Health Check | http://localhost:8080/actuator/health |
| Metrics | http://localhost:8080/actuator/metrics |
| H2 Console | http://localhost:8080/h2-console |

---

## Getting Help

1. **API Documentation**: See `API_DOCUMENTATION.md`
2. **System Overview**: See `GENERATED_CODE_README.md`
3. **Generation Details**: See `GENERATION_SUMMARY.md`
4. **Swagger UI**: Interactive API testing
5. **Code Comments**: Inline documentation in all files

---

## Production Deployment

### Build for Production
```bash
mvn clean package -DskipTests
```

### Run in Production
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/accountdb
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=secure_password
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Success Indicators

You'll know everything is working when:

✅ Application starts without errors  
✅ Swagger UI loads successfully  
✅ Health check returns `{"status":"UP"}`  
✅ You can create an account via API  
✅ You can retrieve the account  
✅ All tests pass with `mvn test`  

---

**Congratulations!** 🎉

Your Account Management System is now up and running!

Start exploring the API using Swagger UI or the cURL examples above.
