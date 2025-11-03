# API Endpoints Summary

## Account Management API

### Account Endpoints

#### GET /api/accounts
- **Description**: Retrieve all accounts
- **Response**: List of AccountDTO objects
- **Status Code**: 200 OK

#### GET /api/accounts/{accountId}
- **Description**: Retrieve a specific account by ID
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Response**: AccountDTO object
- **Status Code**: 200 OK

#### GET /api/accounts/status/{activeStatus}
- **Description**: Retrieve accounts by active status
- **Path Parameters**: 
  - activeStatus (String): Account active status
- **Response**: List of AccountDTO objects
- **Status Code**: 200 OK

#### GET /api/accounts/group/{groupId}
- **Description**: Retrieve accounts by group ID
- **Path Parameters**: 
  - groupId (String): Account group identifier
- **Response**: List of AccountDTO objects
- **Status Code**: 200 OK

#### POST /api/accounts
- **Description**: Create a new account
- **Request Body**: AccountDTO object
- **Response**: Created AccountDTO object
- **Status Code**: 201 CREATED

#### PUT /api/accounts/{accountId}
- **Description**: Update an existing account
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Request Body**: AccountDTO object
- **Response**: Updated AccountDTO object
- **Status Code**: 200 OK

#### DELETE /api/accounts/{accountId}
- **Description**: Delete an account
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Response**: No content
- **Status Code**: 204 NO CONTENT

---

## Card Management API

### Card Endpoints

#### GET /api/cards
- **Description**: Retrieve all cards
- **Response**: List of CardDTO objects
- **Status Code**: 200 OK

#### GET /api/cards/{cardNumber}
- **Description**: Retrieve a specific card by card number
- **Path Parameters**: 
  - cardNumber (String): Card number
- **Response**: CardDTO object
- **Status Code**: 200 OK

#### GET /api/cards/account/{accountId}
- **Description**: Retrieve cards by account ID
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Response**: List of CardDTO objects
- **Status Code**: 200 OK

#### GET /api/cards/status/{activeStatus}
- **Description**: Retrieve cards by active status
- **Path Parameters**: 
  - activeStatus (String): Card active status
- **Response**: List of CardDTO objects
- **Status Code**: 200 OK

#### POST /api/cards
- **Description**: Create a new card
- **Request Body**: CardDTO object
- **Response**: Created CardDTO object
- **Status Code**: 201 CREATED

#### PUT /api/cards/{cardNumber}
- **Description**: Update an existing card
- **Path Parameters**: 
  - cardNumber (String): Card number
- **Request Body**: CardDTO object
- **Response**: Updated CardDTO object
- **Status Code**: 200 OK

#### DELETE /api/cards/{cardNumber}
- **Description**: Delete a card
- **Path Parameters**: 
  - cardNumber (String): Card number
- **Response**: No content
- **Status Code**: 204 NO CONTENT

---

## Customer Management API

### Customer Endpoints

#### GET /api/customers
- **Description**: Retrieve all customers
- **Response**: List of CustomerDTO objects
- **Status Code**: 200 OK

#### GET /api/customers/{customerId}
- **Description**: Retrieve a specific customer by ID
- **Path Parameters**: 
  - customerId (String): Customer identifier
- **Response**: CustomerDTO object
- **Status Code**: 200 OK

#### GET /api/customers/lastname/{lastName}
- **Description**: Retrieve customers by last name
- **Path Parameters**: 
  - lastName (String): Customer last name
- **Response**: List of CustomerDTO objects
- **Status Code**: 200 OK

#### POST /api/customers
- **Description**: Create a new customer
- **Request Body**: CustomerDTO object
- **Response**: Created CustomerDTO object
- **Status Code**: 201 CREATED

#### PUT /api/customers/{customerId}
- **Description**: Update an existing customer
- **Path Parameters**: 
  - customerId (String): Customer identifier
- **Request Body**: CustomerDTO object
- **Response**: Updated CustomerDTO object
- **Status Code**: 200 OK

#### DELETE /api/customers/{customerId}
- **Description**: Delete a customer
- **Path Parameters**: 
  - customerId (String): Customer identifier
- **Response**: No content
- **Status Code**: 204 NO CONTENT

---

## Transaction Management API

### Transaction Endpoints

#### GET /api/transactions
- **Description**: Retrieve all transactions
- **Response**: List of TransactionDTO objects
- **Status Code**: 200 OK

#### GET /api/transactions/{transactionId}
- **Description**: Retrieve a specific transaction by ID
- **Path Parameters**: 
  - transactionId (String): Transaction identifier
- **Response**: TransactionDTO object
- **Status Code**: 200 OK

#### GET /api/transactions/card/{cardNumber}
- **Description**: Retrieve transactions by card number (ordered by timestamp descending)
- **Path Parameters**: 
  - cardNumber (String): Card number
- **Response**: List of TransactionDTO objects
- **Status Code**: 200 OK

#### GET /api/transactions/account/{accountId}
- **Description**: Retrieve transactions by account ID (ordered by timestamp descending)
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Response**: List of TransactionDTO objects
- **Status Code**: 200 OK

#### POST /api/transactions
- **Description**: Create a new transaction
- **Request Body**: TransactionDTO object
- **Response**: Created TransactionDTO object
- **Status Code**: 201 CREATED

---

## Interest Calculation API

### Interest Endpoints

#### POST /api/interest/calculate
- **Description**: Calculate monthly interest for all accounts
- **Query Parameters**: 
  - processingDate (String): Processing date for interest calculation
- **Response**: List of InterestCalculationDTO objects
- **Status Code**: 200 OK

---

## Statement Generation API

### Statement Endpoints

#### GET /api/statements
- **Description**: Generate statements for all accounts
- **Response**: List of AccountStatementDTO objects
- **Status Code**: 200 OK

#### GET /api/statements/card/{cardNumber}
- **Description**: Generate statement for a specific card
- **Path Parameters**: 
  - cardNumber (String): Card number
- **Response**: AccountStatementDTO object
- **Status Code**: 200 OK

#### GET /api/statements/account/{accountId}
- **Description**: Generate statements for a specific account
- **Path Parameters**: 
  - accountId (String): Account identifier
- **Response**: List of AccountStatementDTO objects
- **Status Code**: 200 OK

---

## Data Models

### AccountDTO
- accountId: String (11 characters)
- activeStatus: String (1 character)
- currentBalance: BigDecimal
- creditLimit: BigDecimal
- cashCreditLimit: BigDecimal
- openDate: String (10 characters)
- expirationDate: String (10 characters)
- reissueDate: String (10 characters)
- currentCycleCredit: BigDecimal
- currentCycleDebit: BigDecimal
- addressZipCode: String (10 characters)
- groupId: String (10 characters)

### CardDTO
- cardNumber: String (16 characters)
- accountId: String (11 characters)
- cvvCode: String (3 characters)
- embossedName: String (50 characters)
- expirationDate: String (10 characters)
- activeStatus: String (1 character)

### CustomerDTO
- customerId: String (9 characters)
- firstName: String (25 characters)
- middleName: String (25 characters)
- lastName: String (25 characters)
- addressLine1: String (50 characters)
- addressLine2: String (50 characters)
- addressLine3: String (50 characters)
- stateCode: String (2 characters)
- countryCode: String (3 characters)
- zipCode: String (10 characters)
- phoneNumber1: String (15 characters)
- phoneNumber2: String (15 characters)
- ssn: String (9 characters)
- governmentIssuedId: String (20 characters)
- dateOfBirth: String (10 characters)
- eftAccountId: String (10 characters)
- primaryCardholderIndicator: String (1 character)
- ficoCreditScore: Integer

### TransactionDTO
- transactionId: String (16 characters)
- cardNumber: String (16 characters)
- accountId: String (11 characters)
- transactionTypeCode: String (2 characters)
- transactionCategoryCode: String (4 characters)
- transactionSource: String (50 characters)
- transactionDescription: String (100 characters)
- transactionAmount: BigDecimal
- merchantId: Long
- merchantName: String (50 characters)
- merchantCity: String (50 characters)
- merchantZip: String (10 characters)
- originalTimestamp: LocalDateTime
- processingTimestamp: LocalDateTime

### InterestCalculationDTO
- accountId: String
- balance: BigDecimal
- interestRate: BigDecimal
- monthlyInterest: BigDecimal
- transactionTypeCode: String
- transactionCategoryCode: String

### AccountStatementDTO
- customer: CustomerDTO
- account: AccountDTO
- transactions: List<TransactionDTO>
- totalAmount: BigDecimal

---

## Error Responses

All endpoints may return the following error responses:

- **400 BAD REQUEST**: Invalid request parameters or body
- **404 NOT FOUND**: Resource not found
- **500 INTERNAL SERVER ERROR**: Server error

---

## Notes

1. All monetary amounts are represented as BigDecimal with 2 decimal places
2. Date fields are stored as strings in YYYY-MM-DD format
3. All endpoints support JSON request/response format
4. Transaction lists are ordered by timestamp in descending order
5. Interest calculation uses the formula: (Balance × Interest Rate) ÷ 1200
6. Statement generation includes customer information, account details, and all associated transactions
