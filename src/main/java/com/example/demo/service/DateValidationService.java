package com.example.demo.service;

import com.example.demo.dto.DateValidationRequest;
import com.example.demo.dto.DateValidationResponse;
import com.example.demo.entity.DateValidation;
import com.example.demo.repository.DateValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class DateValidationService {
    
    private final DateValidationRepository dateValidationRepository;
    
    // CEEDAYS equivalent feedback codes
    private static final String FC_INVALID_DATE = "FC-INVALID-DATE";
    private static final String FC_INSUFFICIENT_DATA = "FC-INSUFFICIENT-DATA";
    private static final String FC_BAD_DATE_VALUE = "FC-BAD-DATE-VALUE";
    private static final String FC_INVALID_ERA = "FC-INVALID-ERA";
    private static final String FC_UNSUPP_RANGE = "FC-UNSUPP-RANGE";
    private static final String FC_INVALID_MONTH = "FC-INVALID-MONTH";
    private static final String FC_BAD_PIC_STRING = "FC-BAD-PIC-STRING";
    private static final String FC_NON_NUMERIC_DATA = "FC-NON-NUMERIC-DATA";
    private static final String FC_YEAR_IN_ERA_ZERO = "FC-YEAR-IN-ERA-ZERO";
    
    // Severity codes
    private static final String SEVERITY_INFO = "INFO";
    private static final String SEVERITY_WARNING = "WARNING";
    private static final String SEVERITY_ERROR = "ERROR";
    private static final String SEVERITY_CRITICAL = "CRITICAL";
    
    // Message codes
    private static final int MSG_VALID_DATE = 1001;
    private static final int MSG_INSUFFICIENT_DATA = 2001;
    private static final int MSG_BAD_DATE_VALUE = 2002;
    private static final int MSG_INVALID_ERA = 2003;
    private static final int MSG_UNSUPP_RANGE = 2004;
    private static final int MSG_INVALID_MONTH = 2005;
    private static final int MSG_BAD_PIC_STRING = 2006;
    private static final int MSG_NON_NUMERIC_DATA = 2007;
    private static final int MSG_YEAR_IN_ERA_ZERO = 2008;
    
    // Lillian date base (January 1, 1900 = Lillian day 1)
    private static final LocalDate LILLIAN_BASE_DATE = LocalDate.of(1900, 1, 1);
    
    @Transactional
    public DateValidationResponse validateDate(DateValidationRequest request) {
        log.info("Validating date: {} with pattern: {}", request.getInputDate(), request.getFormatPattern());
        
        ValidationResult validationResult = performDateValidation(request.getInputDate(), request.getFormatPattern());
        
        DateValidation dateValidation = new DateValidation();
        dateValidation.setInputDate(request.getInputDate());
        dateValidation.setFormatPattern(request.getFormatPattern());
        dateValidation.setValidationResult(validationResult.isValid());
        dateValidation.setSeverityCode(validationResult.getSeverityCode());
        dateValidation.setMessageNumber(validationResult.getMessageNumber());
        dateValidation.setResultMessage(validationResult.getResultMessage());
        dateValidation.setErrorType(validationResult.getErrorType());
        dateValidation.setTestDate(request.getInputDate());
        dateValidation.setMaskUsed(request.getFormatPattern());
        dateValidation.setLillianDateOutput(validationResult.getLillianDate());
        
        DateValidation savedValidation = dateValidationRepository.save(dateValidation);
        
        return convertToResponse(savedValidation);
    }
    
    @Transactional(readOnly = true)
    public Optional<DateValidationResponse> getValidationById(Long id) {
        log.info("Retrieving date validation with ID: {}", id);
        return dateValidationRepository.findById(id).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getAllValidations(Pageable pageable) {
        log.info("Retrieving all date validations with pagination");
        return dateValidationRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsByInputDate(String inputDate, Pageable pageable) {
        log.info("Retrieving validations for input date: {}", inputDate);
        return dateValidationRepository.findByInputDate(inputDate, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsByResult(Boolean validationResult, Pageable pageable) {
        log.info("Retrieving validations by result: {}", validationResult);
        return dateValidationRepository.findByValidationResult(validationResult, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsBySeverityCode(String severityCode, Pageable pageable) {
        log.info("Retrieving validations by severity code: {}", severityCode);
        return dateValidationRepository.findBySeverityCode(severityCode, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsByErrorType(String errorType, Pageable pageable) {
        log.info("Retrieving validations by error type: {}", errorType);
        return dateValidationRepository.findByErrorType(errorType, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsByFormatPattern(String formatPattern, Pageable pageable) {
        log.info("Retrieving validations by format pattern: {}", formatPattern);
        return dateValidationRepository.findByFormatPattern(formatPattern, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DateValidationResponse> getValidationsByMessageNumber(Integer messageNumber, Pageable pageable) {
        log.info("Retrieving validations by message number: {}", messageNumber);
        return dateValidationRepository.findByMessageNumber(messageNumber, pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getValidationStatistics() {
        log.info("Retrieving validation statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        long totalValidations = dateValidationRepository.count();
        long successfulValidations = dateValidationRepository.countByValidationResult(true);
        long failedValidations = dateValidationRepository.countByValidationResult(false);
        
        statistics.put("totalValidations", totalValidations);
        statistics.put("successfulValidations", successfulValidations);
        statistics.put("failedValidations", failedValidations);
        statistics.put("successRate", totalValidations > 0 ? (double) successfulValidations / totalValidations * 100 : 0.0);
        
        // Severity code statistics
        List<Object[]> severityStats = dateValidationRepository.countBySeverityCodeGrouped();
        Map<String, Long> severityMap = new HashMap<>();
        for (Object[] stat : severityStats) {
            severityMap.put((String) stat[0], (Long) stat[1]);
        }
        statistics.put("severityCodeStats", severityMap);
        
        // Error type statistics
        List<Object[]> errorStats = dateValidationRepository.countByErrorTypeGrouped();
        Map<String, Long> errorMap = new HashMap<>();
        for (Object[] stat : errorStats) {
            errorMap.put((String) stat[0], (Long) stat[1]);
        }
        statistics.put("errorTypeStats", errorMap);
        
        return statistics;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getErrorAnalysis() {
        log.info("Retrieving error analysis");
        Map<String, Object> errorAnalysis = new HashMap<>();
        
        // Get counts for each error type
        errorAnalysis.put("invalidDateErrors", dateValidationRepository.findInvalidDateErrors().size());
        errorAnalysis.put("insufficientDataErrors", dateValidationRepository.findInsufficientDataErrors().size());
        errorAnalysis.put("badDateValueErrors", dateValidationRepository.findBadDateValueErrors().size());
        errorAnalysis.put("invalidEraErrors", dateValidationRepository.findInvalidEraErrors().size());
        errorAnalysis.put("unsupportedRangeErrors", dateValidationRepository.findUnsupportedRangeErrors().size());
        errorAnalysis.put("invalidMonthErrors", dateValidationRepository.findInvalidMonthErrors().size());
        errorAnalysis.put("badPictureStringErrors", dateValidationRepository.findBadPictureStringErrors().size());
        errorAnalysis.put("nonNumericDataErrors", dateValidationRepository.findNonNumericDataErrors().size());
        errorAnalysis.put("yearInEraZeroErrors", dateValidationRepository.findYearInEraZeroErrors().size());
        
        // Get critical failures and warnings
        errorAnalysis.put("criticalFailures", dateValidationRepository.findCriticalFailures().size());
        errorAnalysis.put("warnings", dateValidationRepository.findWarnings().size());
        
        return errorAnalysis;
    }
    
    @Transactional
    public void deleteValidation(Long id) {
        log.info("Deleting date validation with ID: {}", id);
        if (!dateValidationRepository.existsById(id)) {
            throw new IllegalArgumentException("Date validation not found with ID: " + id);
        }
        dateValidationRepository.deleteById(id);
    }
    
    private ValidationResult performDateValidation(String inputDate, String formatPattern) {
        // Check for insufficient data
        if (inputDate == null || inputDate.trim().isEmpty()) {
            return new ValidationResult(false, SEVERITY_ERROR, MSG_INSUFFICIENT_DATA, 
                    "Insufficient data provided", FC_INSUFFICIENT_DATA, null);
        }
        
        if (formatPattern == null || formatPattern.trim().isEmpty()) {
            return new ValidationResult(false, SEVERITY_ERROR, MSG_BAD_PIC_STRING, 
                    "Bad picture string", FC_BAD_PIC_STRING, null);
        }
        
        // Check for non-numeric data where expected
        if (!isValidNumericPattern(inputDate, formatPattern)) {
            return new ValidationResult(false, SEVERITY_ERROR, MSG_NON_NUMERIC_DATA, 
                    "Non-numeric data found", FC_NON_NUMERIC_DATA, null);
        }
        
        // Validate format pattern
        if (!isValidFormatPattern(formatPattern)) {
            return new ValidationResult(false, SEVERITY_ERROR, MSG_BAD_PIC_STRING, 
                    "Invalid format pattern", FC_BAD_PIC_STRING, null);
        }
        
        try {
            LocalDate parsedDate = parseDate(inputDate, formatPattern);
            
            // Check for year zero in era
            if (parsedDate.getYear() == 0) {
                return new ValidationResult(false, SEVERITY_ERROR, MSG_YEAR_IN_ERA_ZERO, 
                        "Year in era cannot be zero", FC_YEAR_IN_ERA_ZERO, null);
            }
            
            // Check for invalid month
            int month = parsedDate.getMonthValue();
            if (month < 1 || month > 12) {
                return new ValidationResult(false, SEVERITY_ERROR, MSG_INVALID_MONTH, 
                        "Invalid month value", FC_INVALID_MONTH, null);
            }
            
            // Check for unsupported range
            if (parsedDate.getYear() < 1900 || parsedDate.getYear() > 2100) {
                return new ValidationResult(false, SEVERITY_WARNING, MSG_UNSUPP_RANGE, 
                        "Date outside supported range", FC_UNSUPP_RANGE, null);
            }
            
            // Calculate Lillian date
            Long lillianDate = calculateLillianDate(parsedDate);
            
            return new ValidationResult(true, SEVERITY_INFO, MSG_VALID_DATE, 
                    "Valid date", null, lillianDate);
            
        } catch (DateTimeParseException e) {
            log.warn("Date parsing failed for input: {} with pattern: {}", inputDate, formatPattern, e);
            return new ValidationResult(false, SEVERITY_ERROR, MSG_BAD_DATE_VALUE, 
                    "Invalid date value", FC_BAD_DATE_VALUE, null);
        } catch (Exception e) {
            log.error("Unexpected error during date validation", e);
            return new ValidationResult(false, SEVERITY_ERROR, MSG_BAD_DATE_VALUE, 
                    "Date validation error", FC_INVALID_DATE, null);
        }
    }
    
    private boolean isValidNumericPattern(String inputDate, String formatPattern) {
        // Check if numeric positions in format pattern correspond to numeric characters in input
        if (inputDate.length() != formatPattern.length()) {
            return false;
        }
        
        for (int i = 0; i < formatPattern.length(); i++) {
            char patternChar = formatPattern.charAt(i);
            char inputChar = inputDate.charAt(i);
            
            if ((patternChar == 'Y' || patternChar == 'M' || patternChar == 'D') && 
                !Character.isDigit(inputChar)) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isValidFormatPattern(String formatPattern) {
        // Validate common date format patterns
        Pattern validPattern = Pattern.compile("^[YMDH/\\-\\.\\s]*$");
        return validPattern.matcher(formatPattern.toUpperCase()).matches();
    }
    
    private LocalDate parseDate(String inputDate, String formatPattern) {
        // Convert custom format pattern to Java DateTimeFormatter pattern
        String javaPattern = convertToJavaPattern(formatPattern);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaPattern);
        return LocalDate.parse(inputDate, formatter);
    }
    
    private String convertToJavaPattern(String customPattern) {
        // Convert CEEDAYS-style patterns to Java DateTimeFormatter patterns
        return customPattern
                .replaceAll("YYYY", "yyyy")
                .replaceAll("YY", "yy")
                .replaceAll("MM", "MM")
                .replaceAll("DD", "dd")
                .replaceAll("M", "M")
                .replaceAll("D", "d");
    }
    
    private Long calculateLillianDate(LocalDate date) {
        try {
            return LILLIAN_BASE_DATE.until(date).getDays() + 1;
        } catch (Exception e) {
            log.warn("Failed to calculate Lillian date for: {}", date, e);
            return null;
        }
    }
    
    private DateValidationResponse convertToResponse(DateValidation dateValidation) {
        DateValidationResponse response = new DateValidationResponse();
        response.setValidationId(dateValidation.getValidationId());
        response.setInputDate(dateValidation.getInputDate());
        response.setFormatPattern(dateValidation.getFormatPattern());
        response.setValidationResult(dateValidation.getValidationResult());
        response.setSeverityCode(dateValidation.getSeverityCode());
        response.setMessageNumber(dateValidation.getMessageNumber());
        response.setResultMessage(dateValidation.getResultMessage());
        response.setTestDate(dateValidation.getTestDate());
        response.setMaskUsed(dateValidation.getMaskUsed());
        response.setLillianDateOutput(dateValidation.getLillianDateOutput());
        response.setErrorType(dateValidation.getErrorType());
        response.setStructuredMessage(buildStructuredMessage(dateValidation));
        
        return response;
    }
    
    private String buildStructuredMessage(DateValidation dateValidation) {
        // Build CEEDAYS-style structured message
        StringBuilder message = new StringBuilder();
        message.append(dateValidation.getSeverityCode())
               .append(" Mesg Code:")
               .append(dateValidation.getMessageNumber())
               .append(dateValidation.getResultMessage());
        
        if (dateValidation.getTestDate() != null) {
            message.append(" TstDate:")
                   .append(dateValidation.getTestDate());
        }
        
        if (dateValidation.getMaskUsed() != null) {
            message.append(" Mask used:")
                   .append(dateValidation.getMaskUsed());
        }
        
        // Pad to standard CEEDAYS message length
        while (message.length() < 80) {
            message.append(" ");
        }
        
        return message.toString();
    }
    
    private static class ValidationResult {
        private final boolean valid;
        private final String severityCode;
        private final int messageNumber;
        private final String resultMessage;
        private final String errorType;
        private final Long lillianDate;
        
        public ValidationResult(boolean valid, String severityCode, int messageNumber, 
                              String resultMessage, String errorType, Long lillianDate) {
            this.valid = valid;
            this.severityCode = severityCode;
            this.messageNumber = messageNumber;
            this.resultMessage = resultMessage;
            this.errorType = errorType;
            this.lillianDate = lillianDate;
        }
        
        public boolean isValid() { return valid; }
        public String getSeverityCode() { return severityCode; }
        public int getMessageNumber() { return messageNumber; }
        public String getResultMessage() { return resultMessage; }
        public String getErrorType() { return errorType; }
        public Long getLillianDate() { return lillianDate; }
    }
}