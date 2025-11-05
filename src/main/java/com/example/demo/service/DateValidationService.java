package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
public class DateValidationService {
    
    /**
     * Validates date string against specified format
     * Implements CSUTLDTC date validation logic
     */
    public DateValidationResult validateDate(String dateString, String dateFormat) {
        DateValidationResult result = new DateValidationResult();
        result.setDateString(dateString);
        result.setDateFormat(dateFormat);
        
        if (dateString == null || dateString.trim().isEmpty()) {
            result.setValid(false);
            result.setSeverityCode("0001");
            result.setMessageNumber("0001");
            result.setResultMessage("Insufficient");
            return result;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate parsedDate = LocalDate.parse(dateString, formatter);
            
            // Validate date is within reasonable range
            if (parsedDate.getYear() < 1601 || parsedDate.getYear() > 9999) {
                result.setValid(false);
                result.setSeverityCode("0002");
                result.setMessageNumber("0002");
                result.setResultMessage("Invalid Era");
                return result;
            }
            
            result.setValid(true);
            result.setSeverityCode("0000");
            result.setMessageNumber("0000");
            result.setResultMessage("Date is valid");
            result.setParsedDate(parsedDate);
            
        } catch (DateTimeParseException e) {
            result.setValid(false);
            result.setSeverityCode("0003");
            result.setMessageNumber("0003");
            result.setResultMessage("Datevalue error");
            log.warn("Date validation failed for: {} with format: {}", dateString, dateFormat, e);
        } catch (IllegalArgumentException e) {
            result.setValid(false);
            result.setSeverityCode("0004");
            result.setMessageNumber("0004");
            result.setResultMessage("Bad Pic String");
            log.warn("Invalid date format: {}", dateFormat, e);
        }
        
        return result;
    }
    
    public static class DateValidationResult {
        private boolean valid;
        private String severityCode;
        private String messageNumber;
        private String resultMessage;
        private String dateString;
        private String dateFormat;
        private LocalDate parsedDate;
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getSeverityCode() {
            return severityCode;
        }
        
        public void setSeverityCode(String severityCode) {
            this.severityCode = severityCode;
        }
        
        public String getMessageNumber() {
            return messageNumber;
        }
        
        public void setMessageNumber(String messageNumber) {
            this.messageNumber = messageNumber;
        }
        
        public String getResultMessage() {
            return resultMessage;
        }
        
        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }
        
        public String getDateString() {
            return dateString;
        }
        
        public void setDateString(String dateString) {
            this.dateString = dateString;
        }
        
        public String getDateFormat() {
            return dateFormat;
        }
        
        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }
        
        public LocalDate getParsedDate() {
            return parsedDate;
        }
        
        public void setParsedDate(LocalDate parsedDate) {
            this.parsedDate = parsedDate;
        }
    }
}
