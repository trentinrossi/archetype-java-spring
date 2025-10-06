package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "date_validation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateValidation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "validation_id")
    private Long validationId;
    
    @Column(name = "input_date", length = 50, nullable = false)
    private String inputDate;
    
    @Column(name = "format_pattern", length = 50, nullable = false)
    private String formatPattern;
    
    @Column(name = "validation_result", nullable = false)
    private Boolean validationResult;
    
    @Column(name = "severity_code", length = 10, nullable = false)
    private String severityCode;
    
    @Column(name = "message_number", nullable = false)
    private Integer messageNumber;
    
    @Column(name = "result_message", length = 500, nullable = false)
    private String resultMessage;
    
    @Column(name = "lillian_date_output")
    private Long lillianDateOutput;
    
    @Column(name = "error_type", length = 50)
    private String errorType;
    
    @Column(name = "test_date", length = 50)
    private String testDate;
    
    @Column(name = "mask_used", length = 50)
    private String maskUsed;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public DateValidation(String inputDate, String formatPattern, Boolean validationResult, 
                         String severityCode, Integer messageNumber, String resultMessage) {
        this.inputDate = inputDate;
        this.formatPattern = formatPattern;
        this.validationResult = validationResult;
        this.severityCode = severityCode;
        this.messageNumber = messageNumber;
        this.resultMessage = resultMessage;
    }
    
    public boolean isValidDate() {
        return Boolean.TRUE.equals(validationResult);
    }
    
    public boolean hasError() {
        return Boolean.FALSE.equals(validationResult);
    }
    
    public boolean isErrorType(String type) {
        return type != null && type.equals(errorType);
    }
    
    public boolean isCriticalError() {
        return "ERROR".equals(severityCode) || "CRITICAL".equals(severityCode);
    }
    
    public boolean isWarning() {
        return "WARNING".equals(severityCode) || "WARN".equals(severityCode);
    }
    
    public String getFormattedMessage() {
        return String.format("[%s-%d] %s", severityCode, messageNumber, resultMessage);
    }
}