# Card Data API - OpenAPI Summary

## Overview
This API provides endpoints for managing card data based on the COBOL CBACT02C.cbl business rules. The API implements card data file reader and printer functionality with modern REST endpoints.

## Base Information
- **Base URL**: `/api/v1/card-data`
- **API Version**: 1.0
- **Content Type**: `application/json`

## Endpoints Summary

### 1. Read All Card Data Records Sequentially
- **Endpoint**: `GET /api/v1/card-data/read-all`
- **Description**: Reads and returns all card data records in sequential order, equivalent to COBOL CBACT02C.cbl main processing loop
- **Parameters**: None
- **Response**: Array of CardDataDto objects
- **Status Codes**:
  - `200 OK`: Successfully retrieved all card data records
  - `500 Internal Server Error`: Error occurred while reading card data

### 2. Read Card Data with Pagination
- **Endpoint**: `GET /api/v1/card-data/read-paginated`
- **Description**: Reads card data records with pagination support for efficient batch processing
- **Parameters**:
  - `page` (query, optional): Page number (0-based), default: 0
  - `size` (query, optional): Page size (1-1000), default: 20
- **Response**: Page object containing CardDataDto objects
- **Status Codes**:
  - `200 OK`: Successfully retrieved paginated card data records
  - `400 Bad Request`: Invalid pagination parameters
  - `500 Internal Server Error`: Error occurred while reading card data

### 3. Find Card Data by Card Number
- **Endpoint**: `GET /api/v1/card-data/{cardNumber}`
- **Description**: Retrieves card data by card number (FDCARDNUM), equivalent to indexed file access
- **Parameters**:
  - `cardNumber` (path, required): 16-character card number
- **Response**: CardDataDto object
- **Status Codes**:
  - `200 OK`: Card data found and retrieved successfully
  - `400 Bad Request`: Invalid card number format
  - `404 Not Found`: Card data not found for the given card number
  - `500 Internal Server Error`: Error occurred while retrieving card data

### 4. Create New Card Data Record
- **Endpoint**: `POST /api/v1/card-data`
- **Description**: Creates a new card data record with FDCARDNUM and FDCARDDATA
- **Parameters**:
  - Request Body: CardDataDto object (required)
- **Response**: Created CardDataDto object
- **Status Codes**:
  - `201 Created`: Card data record created successfully
  - `400 Bad Request`: Invalid card data or card number already exists
  - `500 Internal Server Error`: Error occurred while creating card data

### 5. Update Card Data Record
- **Endpoint**: `PUT /api/v1/card-data/{cardNumber}`
- **Description**: Updates an existing card data record by card number
- **Parameters**:
  - `cardNumber` (path, required): 16-character card number
  - Request Body: CardDataDto object (required)
- **Response**: Updated CardDataDto object
- **Status Codes**:
  - `200 OK`: Card data record updated successfully
  - `400 Bad Request`: Invalid card data or card number format
  - `404 Not Found`: Card data not found for the given card number
  - `500 Internal Server Error`: Error occurred while updating card data

### 6. Delete Card Data Record
- **Endpoint**: `DELETE /api/v1/card-data/{cardNumber}`
- **Description**: Deletes a card data record by card number
- **Parameters**:
  - `cardNumber` (path, required): 16-character card number
- **Response**: No content
- **Status Codes**:
  - `204 No Content`: Card data record deleted successfully
  - `400 Bad Request`: Invalid card number format
  - `404 Not Found`: Card data not found for the given card number
  - `500 Internal Server Error`: Error occurred while deleting card data

### 7. Get Total Count of Card Data Records
- **Endpoint**: `GET /api/v1/card-data/count`
- **Description**: Returns the total number of card data records in the system
- **Parameters**: None
- **Response**: JSON object with count field
- **Status Codes**:
  - `200 OK`: Successfully retrieved card data count
  - `500 Internal Server Error`: Error occurred while counting card data

### 8. Search Card Data by Content
- **Endpoint**: `GET /api/v1/card-data/search`
- **Description**: Searches card data records by content within FDCARDDATA field
- **Parameters**:
  - `searchTerm` (query, required): Search term to look for in card data
- **Response**: Array of matching CardDataDto objects
- **Status Codes**:
  - `200 OK`: Successfully retrieved matching card data records
  - `400 Bad Request`: Invalid search term
  - `500 Internal Server Error`: Error occurred while searching card data

## Data Models

### CardDataDto
```json
{
  "cardNumber": "string (16 characters, required)",
  "cardData": "string (max 134 characters, required)",
  "createdAt": "string (ISO 8601 datetime, read-only)",
  "updatedAt": "string (ISO 8601 datetime, read-only)"
}
```

### Page Response (for paginated endpoints)
```json
{
  "content": [CardDataDto],
  "pageable": {
    "pageNumber": "integer",
    "pageSize": "integer"
  },
  "totalElements": "integer",
  "totalPages": "integer",
  "first": "boolean",
  "last": "boolean",
  "numberOfElements": "integer"
}
```

### Count Response
```json
{
  "count": "integer"
}
```

## Business Rules Implementation

### COBOL CBACT02C.cbl Mapping
- **File Access**: Sequential reading implemented via ordered queries
- **FDCARDNUM**: Mapped to `cardNumber` field (16-character primary key)
- **FDCARDDATA**: Mapped to `cardData` field (134-character data payload)
- **Error Handling**: Comprehensive error handling with appropriate HTTP status codes
- **Batch Processing**: Pagination support for efficient large dataset processing

### Validation Rules
- Card number must be exactly 16 characters
- Card data cannot exceed 134 characters
- Both fields are mandatory for create/update operations
- Unique constraint on card number (primary key)

### Security Considerations
- Input validation on all endpoints
- Proper error handling without exposing sensitive information
- Logging for audit trail and debugging

## Usage Examples

### Create a new card data record
```bash
curl -X POST /api/v1/card-data \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "1234567890123456",
    "cardData": "Sample card data content for processing"
  }'
```

### Read all records sequentially
```bash
curl -X GET /api/v1/card-data/read-all
```

### Search for specific content
```bash
curl -X GET "/api/v1/card-data/search?searchTerm=sample"
```

This API provides a complete modernization of the COBOL CBACT02C.cbl functionality while maintaining the core business logic and data structure requirements.