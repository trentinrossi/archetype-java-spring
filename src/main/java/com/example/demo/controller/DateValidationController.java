package com.example.demo.controller;

import com.example.demo.dto.DateValidationRequest;
import com.example.demo.dto.DateValidationResponse;
import com.example.demo.service.DateValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Date Validation", description = "APIs for date validation operations - CEEDAYS API equivalent functionality")
@RequestMapping("/api/date-validations")
public class DateValidationController {
    
    private final DateValidationService dateValidationService;
    
    @Operation(summary = "Validate a date", description = "Create a new date validation with format pattern and return validation results")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Date validation completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<DateValidationResponse> validateDate(@Valid @RequestBody DateValidationRequest request) {
        log.info("Validating date: {} with pattern: {}", request.getInputDate(), request.getFormatPattern());
        DateValidationResponse response = dateValidationService.validateDate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Get validation by ID", description = "Retrieve a date validation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validation"),
        @ApiResponse(responseCode = "404", description = "Validation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DateValidationResponse> getValidationById(
            @Parameter(description = "Validation ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Retrieving validation with ID: {}", id);
        return dateValidationService.getValidationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Get all validations", description = "Retrieve a paginated list of all date validations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<DateValidationResponse>> getAllValidations(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all validations with pagination: {}", pageable);
        Page<DateValidationResponse> validations = dateValidationService.getAllValidations(pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validations by input date", description = "Retrieve validations filtered by input date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-input-date")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsByInputDate(
            @Parameter(description = "Input date to filter by", example = "20231201", required = true)
            @RequestParam String inputDate,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations for input date: {}", inputDate);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsByInputDate(inputDate, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validations by result", description = "Retrieve validations filtered by validation result")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-result")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsByResult(
            @Parameter(description = "Validation result to filter by", example = "true", required = true)
            @RequestParam Boolean validationResult,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations with result: {}", validationResult);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsByResult(validationResult, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validations by severity code", description = "Retrieve validations filtered by severity code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-severity")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsBySeverityCode(
            @Parameter(description = "Severity code to filter by", example = "ERROR", required = true)
            @RequestParam String severityCode,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations with severity code: {}", severityCode);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsBySeverityCode(severityCode, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validations by error type", description = "Retrieve validations filtered by error type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-error-type")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsByErrorType(
            @Parameter(description = "Error type to filter by", example = "INVALID_DATE", required = true)
            @RequestParam String errorType,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations with error type: {}", errorType);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsByErrorType(errorType, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validations by format pattern", description = "Retrieve validations filtered by format pattern")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-format-pattern")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsByFormatPattern(
            @Parameter(description = "Format pattern to filter by", example = "YYYYMMDD", required = true)
            @RequestParam String formatPattern,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations with format pattern: {}", formatPattern);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsByFormatPattern(formatPattern, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Get validation statistics", description = "Retrieve statistics about date validations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of statistics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getValidationStatistics() {
        log.info("Retrieving validation statistics");
        Map<String, Object> statistics = dateValidationService.getValidationStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @Operation(summary = "Get error analysis", description = "Retrieve error analysis for failed validations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of error analysis"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/error-analysis")
    public ResponseEntity<Map<String, Object>> getErrorAnalysis() {
        log.info("Retrieving error analysis");
        Map<String, Object> errorAnalysis = dateValidationService.getErrorAnalysis();
        return ResponseEntity.ok(errorAnalysis);
    }
    
    @Operation(summary = "Get validations by message number", description = "Retrieve validations filtered by message number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of validations"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-message-number")
    public ResponseEntity<Page<DateValidationResponse>> getValidationsByMessageNumber(
            @Parameter(description = "Message number to filter by", example = "1001", required = true)
            @RequestParam Integer messageNumber,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving validations with message number: {}", messageNumber);
        Page<DateValidationResponse> validations = dateValidationService.getValidationsByMessageNumber(messageNumber, pageable);
        return ResponseEntity.ok(validations);
    }
    
    @Operation(summary = "Delete validation", description = "Delete a date validation by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Validation deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Validation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValidation(
            @Parameter(description = "Validation ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Deleting validation with ID: {}", id);
        dateValidationService.deleteValidation(id);
        return ResponseEntity.noContent().build();
    }
}