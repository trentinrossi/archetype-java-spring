# API Testing Guide - Card and Account Management System

## 🧪 Quick Start Testing Guide

This guide provides ready-to-use API calls for testing all implemented business rules and features.

---

## 📋 Prerequisites

- Application running on `http://localhost:8080`
- Sample data loaded (via V5 migration)
- REST client (Postman, curl, or similar)

---

## 🔐 Test Users

### Admin User
- **User ID**: `admin001`
- **Type**: ADMIN
- **Access**: All accounts and cards

### Regular Users
- **User ID**: `user001` - Access to accounts: 12345678901, 98765432109
- **User ID**: `user002` - Access to account: 11111111111
- **User ID**: `user003` - Access to account: 22222222222

---

## 🧪 Test Scenarios

### 1. Test BR001: User Permission Based Card Listing

#### Test 1.1: Admin Views All Cards (No Filter)
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": ""
}
```
**Expected**: Returns all 18 credit cards (first page of 7)

#### Test 1.2: Regular User Views Own Cards
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=user001
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": ""
}
```
**Expected**: Returns only cards from accounts 12345678901 and 98765432109 (13 cards total)

#### Test 1.3: Regular User Attempts Unauthorized Access
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=user002
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": ""
}
```
**Expected**: HTTP 403 Forbidden - User does not have access to this account

---

### 2. Test BR002: Pagination Display Limit (7 Records)

#### Test 2.1: First Page (7 Records)
```bash
GET http://localhost:8080/api/credit-cards?page=0&size=7
```
**Expected**: 
- Returns 7 credit cards
- `hasNextPage: true`
- `isFirstPage: true`
- `totalPages: 3` (18 cards / 7 per page)

#### Test 2.2: Request More Than 7 Records
```bash
GET http://localhost:8080/api/credit-cards?page=0&size=20
```
**Expected**: 
- System enforces max 7 records
- Returns only 7 cards despite requesting 20

#### Test 2.3: Last Page
```bash
GET http://localhost:8080/api/credit-cards?page=2&size=7
```
**Expected**:
- Returns remaining 4 cards
- `hasNextPage: false`
- `isLastPage: true`

---

### 3. Test BR003: Single Selection Enforcement

#### Test 3.1: Get Single Card by ID
```bash
GET http://localhost:8080/api/credit-cards/1
```
**Expected**: Returns single credit card with ID 1

#### Test 3.2: Update Single Card
```bash
PUT http://localhost:8080/api/credit-cards/1
Content-Type: application/json

{
  "cardStatus": "BLOCKED"
}
```
**Expected**: 
- HTTP 200 OK
- Card status changed to BLOCKED
- Only one card affected

---

### 4. Test BR004: Filter Application Logic

#### Test 4.1: Filter by Account ID Only
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": ""
}
```
**Expected**: Returns 8 cards from account 12345678901

#### Test 4.2: Filter by Card Number Only
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": "1234567890123456"
}
```
**Expected**: Returns 1 card with exact card number

#### Test 4.3: Cumulative Filters (Both Account and Card Number)
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": "1234567890123456"
}
```
**Expected**: Returns 1 card matching both criteria

#### Test 4.4: No Filter (Blank Values)
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": ""
}
```
**Expected**: Returns all cards (no filtering applied)

#### Test 4.5: No Filter (Zero Values)
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "00000000000",
  "cardNumber": "0000000000000000"
}
```
**Expected**: Returns all cards (zeros indicate no filter)

---

### 5. Test BR005: Page Navigation State Management

#### Test 5.1: Check Navigation State - First Page
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001&page=0&size=7
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": ""
}
```
**Expected Response**:
```json
{
  "creditCards": [...],
  "currentPage": 0,
  "totalPages": 2,
  "totalRecords": 8,
  "pageSize": 7,
  "firstCardKey": "1234567890123456",
  "lastCardKey": "1234567890123462",
  "hasNextPage": true,
  "hasPreviousPage": false,
  "isFirstPage": true,
  "isLastPage": false
}
```

#### Test 5.2: Check Navigation State - Last Page
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001&page=1&size=7
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": ""
}
```
**Expected Response**:
```json
{
  "creditCards": [...],
  "currentPage": 1,
  "totalPages": 2,
  "totalRecords": 8,
  "pageSize": 7,
  "firstCardKey": "1234567890123463",
  "lastCardKey": "1234567890123463",
  "hasNextPage": false,
  "hasPreviousPage": true,
  "isFirstPage": false,
  "isLastPage": true
}
```

---

### 6. Test BR006: Program Integration Flow

#### Test 6.1: View Card Detail
```bash
GET http://localhost:8080/api/credit-cards/1
```
**Expected**: Full card details for integration with detail view program

#### Test 6.2: Update Card from Integration
```bash
PUT http://localhost:8080/api/credit-cards/1
Content-Type: application/json

{
  "cardStatus": "SUSPENDED"
}
```
**Expected**: Card updated successfully, ready for return to list program

#### Test 6.3: Get Card by Card Number (Alternative Lookup)
```bash
GET http://localhost:8080/api/credit-cards/card-number/1234567890123456
```
**Expected**: Card details retrieved by card number

---

### 7. Test BR008: Record Exclusion Based on Filters

#### Test 7.1: Filter Excludes Non-Matching Records
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "98765432109",
  "cardNumber": ""
}
```
**Expected**: 
- Returns only 5 cards from account 98765432109
- Cards from other accounts excluded
- Total records: 5 (not 18)

#### Test 7.2: Strict Card Number Filter
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=admin001
Content-Type: application/json

{
  "accountId": "",
  "cardNumber": "9876543210987654"
}
```
**Expected**:
- Returns exactly 1 card
- All other 17 cards excluded
- No partial matches

---

## 🔧 CRUD Operations Testing

### Account Management

#### Create Account
```bash
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountId": "33333333333"
}
```
**Expected**: HTTP 201 Created

#### Get Account
```bash
GET http://localhost:8080/api/accounts/33333333333
```

#### Update Account
```bash
PUT http://localhost:8080/api/accounts/33333333333
Content-Type: application/json

{
  "notes": "Updated account"
}
```

#### Delete Account
```bash
DELETE http://localhost:8080/api/accounts/33333333333
```
**Expected**: HTTP 204 No Content

---

### User Management

#### Create User
```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "userId": "testuser001",
  "userType": "REGULAR",
  "accountIds": ["12345678901"]
}
```
**Expected**: HTTP 201 Created

#### Get User
```bash
GET http://localhost:8080/api/users/testuser001
```

#### Update User (Change to Admin)
```bash
PUT http://localhost:8080/api/users/testuser001
Content-Type: application/json

{
  "userType": "ADMIN",
  "accountIds": []
}
```

#### Check Admin Status
```bash
GET http://localhost:8080/api/users/testuser001/is-admin
```
**Expected**: `true`

#### Check Account Access
```bash
GET http://localhost:8080/api/users/user001/access/12345678901
```
**Expected**: `true`

#### Delete User
```bash
DELETE http://localhost:8080/api/users/testuser001
```

---

### Credit Card Management

#### Create Credit Card
```bash
POST http://localhost:8080/api/credit-cards
Content-Type: application/json

{
  "cardNumber": "5555555555555555",
  "accountId": "12345678901",
  "cardStatus": "ACTIVE"
}
```
**Expected**: HTTP 201 Created

#### Get Credit Card
```bash
GET http://localhost:8080/api/credit-cards/1
```

#### Update Credit Card Status
```bash
PUT http://localhost:8080/api/credit-cards/1
Content-Type: application/json

{
  "cardStatus": "BLOCKED"
}
```

#### Get Cards by Account
```bash
GET http://localhost:8080/api/credit-cards/account/12345678901?page=0&size=7
```

#### Get Cards by User
```bash
GET http://localhost:8080/api/credit-cards/user/user001?page=0&size=7
```

#### Delete Credit Card
```bash
DELETE http://localhost:8080/api/credit-cards/1
```

---

## ❌ Error Scenarios Testing

### Invalid Account ID Format
```bash
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountId": "123"
}
```
**Expected**: HTTP 400 Bad Request - "Account ID must be exactly 11 numeric digits"

### Invalid Card Number Format
```bash
POST http://localhost:8080/api/credit-cards
Content-Type: application/json

{
  "cardNumber": "1234",
  "accountId": "12345678901",
  "cardStatus": "ACTIVE"
}
```
**Expected**: HTTP 400 Bad Request - "Card number must be exactly 16 numeric digits"

### Duplicate Card Number
```bash
POST http://localhost:8080/api/credit-cards
Content-Type: application/json

{
  "cardNumber": "1234567890123456",
  "accountId": "12345678901",
  "cardStatus": "ACTIVE"
}
```
**Expected**: HTTP 400 Bad Request - "Credit card already exists"

### Non-Existent Account
```bash
POST http://localhost:8080/api/credit-cards
Content-Type: application/json

{
  "cardNumber": "9999999999999999",
  "accountId": "99999999999",
  "cardStatus": "ACTIVE"
}
```
**Expected**: HTTP 400 Bad Request - "Account not found"

### Unauthorized Access
```bash
POST http://localhost:8080/api/credit-cards/filter?userId=user002
Content-Type: application/json

{
  "accountId": "12345678901",
  "cardNumber": ""
}
```
**Expected**: HTTP 403 Forbidden - "User does not have access to account"

---

## 📊 Pagination Testing Scenarios

### Scenario 1: Account with 8 Cards (2 Pages)
```bash
# Page 1 (7 cards)
GET http://localhost:8080/api/credit-cards/account/12345678901?page=0&size=7

# Page 2 (1 card)
GET http://localhost:8080/api/credit-cards/account/12345678901?page=1&size=7
```

### Scenario 2: All Cards (3 Pages)
```bash
# Page 1 (7 cards)
GET http://localhost:8080/api/credit-cards?page=0&size=7

# Page 2 (7 cards)
GET http://localhost:8080/api/credit-cards?page=1&size=7

# Page 3 (4 cards)
GET http://localhost:8080/api/credit-cards?page=2&size=7
```

---

## 🎯 Business Rule Validation Checklist

Use this checklist to verify all business rules:

- [ ] **BR001**: Admin can view all cards without filter
- [ ] **BR001**: Regular user can only view accessible account cards
- [ ] **BR001**: Regular user gets 403 for unauthorized account
- [ ] **BR002**: Maximum 7 records per page enforced
- [ ] **BR002**: Requesting more than 7 returns only 7
- [ ] **BR003**: Single card operations work correctly
- [ ] **BR004**: Filter by account ID works
- [ ] **BR004**: Filter by card number works
- [ ] **BR004**: Cumulative filters work (both account and card)
- [ ] **BR004**: Blank/zero values indicate no filter
- [ ] **BR005**: Navigation state includes all required fields
- [ ] **BR005**: First/last card keys are correct
- [ ] **BR005**: hasNextPage/hasPreviousPage flags are accurate
- [ ] **BR006**: Card detail endpoint works
- [ ] **BR006**: Card update endpoint works
- [ ] **BR008**: Non-matching records are excluded
- [ ] **BR008**: Excluded records don't count toward page limit

---

## 🔍 Swagger UI Testing

Access the interactive API documentation:

```
http://localhost:8080/swagger-ui.html
```

All endpoints are documented with:
- Request/response schemas
- Example values
- Try-it-out functionality
- Business rule references

---

## 📝 Notes

1. **User Context**: Always provide `userId` parameter for filter operations
2. **Pagination**: Default page size is 7, maximum is also 7 (enforced)
3. **Filters**: Empty string or zeros indicate "no filter"
4. **Permissions**: Admin users bypass account access checks
5. **Validation**: All inputs are validated before processing
6. **Error Handling**: Meaningful error messages for all failure scenarios

---

## ✅ Success Criteria

Your implementation passes if:
- ✅ All 7 business rules are testable and working
- ✅ Pagination enforces 7 records maximum
- ✅ Permission checks prevent unauthorized access
- ✅ Filters work individually and cumulatively
- ✅ Navigation state is accurate on all pages
- ✅ CRUD operations work for all entities
- ✅ Validation catches all invalid inputs
- ✅ Error messages are clear and specific

---

*Happy Testing! 🚀*
