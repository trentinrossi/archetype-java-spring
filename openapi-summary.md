# Date Validation API - OpenAPI Summary

## Overview
This API provides date validation functionality equivalent to the COBOL CEEDAYS API. It validates dates using format patterns and returns detailed validation results with comprehensive error handling and analysis capabilities.

## Base URL
```
/api/date-validations
```

## Endpoints

### 1. Validate Date
**POST** `/api/date-validations`

Validates a date with a specified format pattern and creates a validation record.

**Request Body:**
```json
{
  "inputDate": "20231201",
  "formatPattern": "YYYYMMDD"
}
```

**Response (201 Created):**
```json
{
  "validationId": 1,
  "inputDate": "20231201",
  "formatPattern": "YYYYMMDD",
  "validationResult": true,
  "severityCode": "INFO",
  "messageNumber": 1001,
  "resultMessage": "Valid date",
  "testDate": "20231201",
  "maskUsed": "YYYYMMDD",
  "lillianDateOutput": 738521,
  "errorType": null,
  "structuredMessage": "INFO Mesg Code:1001Valid date      TstDate:20231201 Mask used:YYYYMMDD    "
}
```

### 2. Get Validation by ID
**GET** `/api/date-validations/{id}`

Retrieves a specific date validation by its ID.

**Parameters:**
- `id` (path): Validation ID (Long)

**Response (200 OK):** Same as validation response above
**Response (404 Not Found):** When validation not found

### 3. Get All Validations
**GET** `/api/date-validations`

Retrieves a paginated list of all date validations.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort criteria

**Response (200 OK):**
```json
{
  "content": [...],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "first": true,
  "numberOfElements": 20
}
```

### 4. Get Validations by Input Date
**GET** `/api/date-validations/by-input-date`

Retrieves validations filtered by input date.

**Query Parameters:**
- `inputDate` (required): Input date to filter by
- Pagination parameters (page, size, sort)

### 5. Get Validations by Result
**GET** `/api/date-validations/by-result`

Retrieves validations filtered by validation result.

**Query Parameters:**
- `validationResult` (required): Boolean validation result
- Pagination parameters (page, size, sort)

### 6. Get Validations by Severity Code
**GET** `/api/date-validations/by-severity`

Retrieves validations filtered by severity code.

**Query Parameters:**
- `severityCode` (required): Severity code (INFO, WARNING, ERROR, CRITICAL)
- Pagination parameters (page, size, sort)

### 7. Get Validations by Error Type
**GET** `/api/date-validations/by-error-type`

Retrieves validations filtered by error type.

**Query Parameters:**
- `errorType` (required): Error type (INVALID_DATE, INSUFFICIENT_DATA, etc.)
- Pagination parameters (page, size, sort)

### 8. Get Validations by Format Pattern
**GET** `/api/date-validations/by-format-pattern`

Retrieves validations filtered by format pattern.

**Query Parameters:**
- `formatPattern` (required): Format pattern to filter by
- Pagination parameters (page, size, sort)

### 9. Get Validations by Message Number
**GET** `/api/date-validations/by-message-number`

Retrieves validations filtered by message number.

**Query Parameters:**
- `messageNumber` (required): Message number to filter by
- Pagination parameters (page, size, sort)

### 10. Get Validation Statistics
**GET** `/api/date-validations/statistics`

Retrieves comprehensive statistics about date validations.

**Response (200 OK):**
```json
{
  "totalValidations": 1000,
  "successfulValidations": 850,
  "failedValidations": 150,
  "successRate": 85.0,
  "severityCodeStats": {
    "INFO": 850,
    "ERROR": 120,
    "WARNING": 30
  },
  "errorTypeStats": {
    "INVALID_DATE": 50,
    "BAD_DATE_VALUE": 40,
    "INVALID_MONTH": 30
  }
}
```

### 11. Get Error Analysis
**GET** `/api/date-validations/error-analysis`

Retrieves detailed error analysis for failed validations.

**Response (200 OK):**
```json
{
  "invalidDateErrors": 25,
  "insufficientDataErrors": 15,
  "badDateValueErrors": 40,
  "invalidEraErrors": 5,
  "unsupportedRangeErrors": 10,
  "invalidMonthErrors": 30,
  "badPictureStringErrors": 8,
  "nonNumericDataErrors": 12,
  "yearInEraZeroErrors": 3,
  "criticalFailures": 45,
  "warnings": 30
}
```

### 12. Delete Validation
**DELETE** `/api/date-validations/{id}`

Deletes a date validation by ID.

**Parameters:**
- `id` (path): Validation ID (Long)

**Response (204 No Content):** Successful deletion
**Response (404 Not Found):** When validation not found

## Error Types

The API supports the following CEEDAYS-equivalent error types:

- `FC-INVALID-DATE`: Date is valid (paradoxical naming from CEEDAYS)
- `FC-INSUFFICIENT-DATA`: Insufficient data provided
- `FC-BAD-DATE-VALUE`: Invalid date value
- `FC-INVALID-ERA`: Invalid era in date
- `FC-UNSUPP-RANGE`: Date outside supported range
- `FC-INVALID-MONTH`: Invalid month value
- `FC-BAD-PIC-STRING`: Invalid format pattern
- `FC-NON-NUMERIC-DATA`: Non-numeric data where numbers expected
- `FC-YEAR-IN-ERA-ZERO`: Year in era cannot be zero

## Severity Codes

- `INFO`: Successful validation
- `WARNING`: Validation passed with warnings
- `ERROR`: Validation failed with errors
- `CRITICAL`: Critical validation failure

## Message Numbers

- `1001`: Valid date
- `2001`: Insufficient data
- `2002`: Bad date value
- `2003`: Invalid era
- `2004`: Unsupported range
- `2005`: Invalid month
- `2006`: Bad picture string
- `2007`: Non-numeric data
- `2008`: Year in era zero

## Format Patterns

The API supports CEEDAYS-style format patterns:
- `YYYY`: 4-digit year
- `YY`: 2-digit year
- `MM`: 2-digit month
- `DD`: 2-digit day
- `M`: 1-digit month
- `D`: 1-digit day

Common patterns:
- `YYYYMMDD`: 20231201
- `YYYY-MM-DD`: 2023-12-01
- `MM/DD/YYYY`: 12/01/2023
- `DD.MM.YYYY`: 01.12.2023

## Lillian Date

The API calculates Lillian dates (days since January 1, 1900) for valid dates, maintaining compatibility with CEEDAYS functionality.

## Authentication

This API currently does not require authentication. In production environments, appropriate authentication and authorization mechanisms should be implemented.

## Rate Limiting

No rate limiting is currently implemented. Consider implementing rate limiting for production use.

## Versioning

This is version 1 of the Date Validation API. Future versions will maintain backward compatibility where possible.