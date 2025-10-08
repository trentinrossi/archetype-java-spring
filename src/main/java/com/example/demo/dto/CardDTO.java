package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Schema(description = "Card number", example = "1234567890123456", required = true)
    private String cardNumber;

    @NotBlank(message = "Card data cannot be blank")
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    @Schema(description = "Card data", example = "CARD_DATA_CONTENT", required = true)
    private String cardData;

    @Schema(description = "Card data hash", example = "a1b2c3d4e5f6...", required = false)
    private String cardDataHash;

    @NotNull(message = "Data length is required")
    @Min(value = 1, message = "Data length must be positive")
    @Max(value = 134, message = "Data length cannot exceed 134")
    @Schema(description = "Data length", example = "134", required = true)
    private Integer dataLength;

    @NotBlank(message = "Record status is required")
    @Pattern(regexp = "^[AISD]$", message = "Record status must be A (Active), I (Inactive), S (Suspended), or D (Deleted)")
    @Schema(description = "Record status", example = "A", required = true)
    private String recordStatus;

    @Pattern(regexp = "^[PRC]$", message = "Processing flag must be P (Pending), R (Processing), or C (Completed)")
    @Schema(description = "Processing flag", example = "P", required = false)
    private String processingFlag;

    @Pattern(regexp = "^[VPF]$", message = "Validation status must be V (Validated), P (Pending), or F (Failed)")
    @Schema(description = "Validation status", example = "V", required = false)
    private String validationStatus;

    @Size(max = 10, message = "Error code must not exceed 10 characters")
    @Schema(description = "Error code", example = "ERR001", required = false)
    private String errorCode;

    @Size(max = 255, message = "Error message must not exceed 255 characters")
    @Schema(description = "Error message", example = "Validation failed", required = false)
    private String errorMessage;

    @Min(value = 1, message = "Sequence number must be positive")
    @Schema(description = "Sequence number", example = "1", required = false)
    private Long sequenceNumber;

    @Size(max = 20, message = "Batch ID must not exceed 20 characters")
    @Schema(description = "Batch ID", example = "BATCH_001", required = false)
    private String batchId;

    @Size(max = 10, message = "Source system must not exceed 10 characters")
    @Schema(description = "Source system", example = "MAINFRAME", required = false)
    private String sourceSystem;

    @Size(max = 10, message = "Target system must not exceed 10 characters")
    @Schema(description = "Target system", example = "ONLINE", required = false)
    private String targetSystem;

    @Schema(description = "Processing date", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime processingDate;

    @Schema(description = "Last accessed date", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime lastAccessed;

    @Schema(description = "Created date", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Updated date", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;

    public CardDTO(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
        this.dataLength = cardData != null ? cardData.length() : 0;
        this.recordStatus = "A";
        this.validationStatus = "P";
    }

    public boolean isActive() {
        return "A".equals(recordStatus);
    }

    public boolean isValidated() {
        return "V".equals(validationStatus);
    }

    public boolean hasError() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }

    public boolean isReadyForProcessing() {
        return isActive() && !hasError() && (isValidated() || "P".equals(validationStatus));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardCreateDTO {
    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "^[0-9A-Za-z]{16}$", message = "Card number must contain only alphanumeric characters")
    @Schema(description = "Card number", example = "1234567890123456", required = true)
    private String cardNumber;

    @NotBlank(message = "Card data cannot be blank")
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    @Schema(description = "Card data", example = "CARD_DATA_CONTENT", required = true)
    private String cardData;

    @Size(max = 20, message = "Batch ID must not exceed 20 characters")
    @Schema(description = "Batch ID", example = "BATCH_001", required = false)
    private String batchId;

    @Size(max = 10, message = "Source system must not exceed 10 characters")
    @Schema(description = "Source system", example = "MAINFRAME", required = false)
    private String sourceSystem;

    @Size(max = 10, message = "Target system must not exceed 10 characters")
    @Schema(description = "Target system", example = "ONLINE", required = false)
    private String targetSystem;

    @Min(value = 1, message = "Sequence number must be positive")
    @Schema(description = "Sequence number", example = "1", required = false)
    private Long sequenceNumber;

    @Pattern(regexp = "^[AISD]$", message = "Record status must be A (Active), I (Inactive), S (Suspended), or D (Deleted)")
    @Schema(description = "Initial record status", example = "A", required = false)
    private String recordStatus;

    public CardCreateDTO(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
        this.recordStatus = "A";
    }

    public boolean isValidCardNumber() {
        return cardNumber != null && 
               cardNumber.length() == 16 && 
               cardNumber.matches("^[0-9A-Za-z]{16}$");
    }

    public boolean isValidCardData() {
        return cardData != null && 
               !cardData.trim().isEmpty() && 
               cardData.length() <= 134;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardUpdateDTO {
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    @Schema(description = "Updated card data", example = "UPDATED_CARD_DATA_CONTENT", required = false)
    private String cardData;

    @Pattern(regexp = "^[AISD]$", message = "Record status must be A (Active), I (Inactive), S (Suspended), or D (Deleted)")
    @Schema(description = "Record status", example = "A", required = false)
    private String recordStatus;

    @Pattern(regexp = "^[PRC]$", message = "Processing flag must be P (Pending), R (Processing), or C (Completed)")
    @Schema(description = "Processing flag", example = "P", required = false)
    private String processingFlag;

    @Pattern(regexp = "^[VPF]$", message = "Validation status must be V (Validated), P (Pending), or F (Failed)")
    @Schema(description = "Validation status", example = "V", required = false)
    private String validationStatus;

    @Size(max = 10, message = "Error code must not exceed 10 characters")
    @Schema(description = "Error code", example = "ERR001", required = false)
    private String errorCode;

    @Size(max = 255, message = "Error message must not exceed 255 characters")
    @Schema(description = "Error message", example = "Validation failed", required = false)
    private String errorMessage;

    @Size(max = 20, message = "Batch ID must not exceed 20 characters")
    @Schema(description = "Batch ID", example = "BATCH_002", required = false)
    private String batchId;

    @Size(max = 10, message = "Source system must not exceed 10 characters")
    @Schema(description = "Source system", example = "MAINFRAME", required = false)
    private String sourceSystem;

    @Size(max = 10, message = "Target system must not exceed 10 characters")
    @Schema(description = "Target system", example = "ONLINE", required = false)
    private String targetSystem;

    @Min(value = 1, message = "Sequence number must be positive")
    @Schema(description = "Sequence number", example = "2", required = false)
    private Long sequenceNumber;

    public boolean hasChanges() {
        return cardData != null || recordStatus != null || processingFlag != null ||
               validationStatus != null || errorCode != null || errorMessage != null ||
               batchId != null || sourceSystem != null || targetSystem != null ||
               sequenceNumber != null;
    }

    public boolean hasStatusChanges() {
        return recordStatus != null || processingFlag != null || validationStatus != null;
    }

    public boolean hasErrorChanges() {
        return errorCode != null || errorMessage != null;
    }

    public boolean hasProcessingChanges() {
        return batchId != null || sourceSystem != null || targetSystem != null || sequenceNumber != null;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardResponseDTO {
    @Schema(description = "Card number", example = "1234567890123456", required = true)
    private String cardNumber;

    @Schema(description = "Card data", example = "CARD_DATA_CONTENT", required = true)
    private String cardData;

    @Schema(description = "Card data hash", example = "a1b2c3d4e5f6...", required = false)
    private String cardDataHash;

    @Schema(description = "Data length", example = "134", required = true)
    private Integer dataLength;

    @Schema(description = "Record status", example = "A", required = true)
    private String recordStatus;

    @Schema(description = "Record status display name", example = "Active", required = true)
    private String recordStatusDisplayName;

    @Schema(description = "Processing flag", example = "P", required = false)
    private String processingFlag;

    @Schema(description = "Processing flag display name", example = "Pending", required = false)
    private String processingFlagDisplayName;

    @Schema(description = "Validation status", example = "V", required = false)
    private String validationStatus;

    @Schema(description = "Validation status display name", example = "Validated", required = false)
    private String validationStatusDisplayName;

    @Schema(description = "Error code", example = "ERR001", required = false)
    private String errorCode;

    @Schema(description = "Error message", example = "Validation failed", required = false)
    private String errorMessage;

    @Schema(description = "Sequence number", example = "1", required = false)
    private Long sequenceNumber;

    @Schema(description = "Batch ID", example = "BATCH_001", required = false)
    private String batchId;

    @Schema(description = "Source system", example = "MAINFRAME", required = false)
    private String sourceSystem;

    @Schema(description = "Target system", example = "ONLINE", required = false)
    private String targetSystem;

    @Schema(description = "Processing date", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime processingDate;

    @Schema(description = "Last accessed date", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime lastAccessed;

    @Schema(description = "Created date", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Updated date", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;

    @Schema(description = "Whether card is active", example = "true", required = true)
    private boolean active;

    @Schema(description = "Whether card is validated", example = "true", required = true)
    private boolean validated;

    @Schema(description = "Whether card has errors", example = "false", required = true)
    private boolean hasError;

    @Schema(description = "Whether card is ready for processing", example = "true", required = true)
    private boolean readyForProcessing;

    @Schema(description = "Whether data integrity is valid", example = "true", required = true)
    private boolean dataIntegrityValid;

    @Schema(description = "Minutes since last access", example = "30", required = false)
    private Long minutesSinceLastAccess;

    @Schema(description = "Minutes since creation", example = "120", required = false)
    private Long minutesSinceCreation;

    @Schema(description = "Minutes since last update", example = "60", required = false)
    private Long minutesSinceLastUpdate;

    public boolean isActive() {
        return active;
    }

    public boolean isValidated() {
        return validated;
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean isReadyForProcessing() {
        return readyForProcessing;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardBatchProcessingDTO {
    @NotBlank(message = "Batch ID is required")
    @Size(max = 20, message = "Batch ID must not exceed 20 characters")
    @Schema(description = "Batch ID", example = "BATCH_001", required = true)
    private String batchId;

    @NotEmpty(message = "Card numbers list cannot be empty")
    @Size(max = 1000, message = "Batch cannot exceed 1000 cards")
    @Schema(description = "List of card numbers to process", required = true)
    private List<String> cardNumbers;

    @NotBlank(message = "Operation type is required")
    @Pattern(regexp = "^(CREATE|UPDATE|VALIDATE|DELETE|PROCESS)$", message = "Operation type must be CREATE, UPDATE, VALIDATE, DELETE, or PROCESS")
    @Schema(description = "Batch operation type", example = "VALIDATE", required = true)
    private String operationType;

    @Size(max = 10, message = "Source system must not exceed 10 characters")
    @Schema(description = "Source system", example = "MAINFRAME", required = false)
    private String sourceSystem;

    @Size(max = 10, message = "Target system must not exceed 10 characters")
    @Schema(description = "Target system", example = "ONLINE", required = false)
    private String targetSystem;

    @Schema(description = "Processing parameters", required = false)
    private java.util.Map<String, String> processingParameters;

    @Schema(description = "Whether to continue on errors", example = "true", required = false)
    private Boolean continueOnError = true;

    @Schema(description = "Maximum retry attempts", example = "3", required = false)
    private Integer maxRetryAttempts = 3;

    @Schema(description = "Processing timeout in minutes", example = "30", required = false)
    private Integer timeoutMinutes = 30;

    @Schema(description = "Whether to validate before processing", example = "true", required = false)
    private Boolean validateBeforeProcessing = true;

    public CardBatchProcessingDTO(String batchId, List<String> cardNumbers, String operationType) {
        this.batchId = batchId;
        this.cardNumbers = cardNumbers;
        this.operationType = operationType;
        this.continueOnError = true;
        this.maxRetryAttempts = 3;
        this.timeoutMinutes = 30;
        this.validateBeforeProcessing = true;
        this.processingParameters = new java.util.HashMap<>();
    }

    public int getBatchSize() {
        return cardNumbers != null ? cardNumbers.size() : 0;
    }

    public boolean isValidBatchSize() {
        return getBatchSize() > 0 && getBatchSize() <= 1000;
    }

    public void addProcessingParameter(String key, String value) {
        if (processingParameters == null) {
            processingParameters = new java.util.HashMap<>();
        }
        processingParameters.put(key, value);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardValidationDTO {
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Schema(description = "Card number to validate", example = "1234567890123456", required = true)
    private String cardNumber;

    @Schema(description = "Card data to validate", example = "CARD_DATA_CONTENT", required = false)
    private String cardData;

    @Schema(description = "Validation rules to apply", required = false)
    private List<String> validationRules;

    @Schema(description = "Whether to perform strict validation", example = "true", required = false)
    private Boolean strictValidation = false;

    @Schema(description = "Whether to validate data integrity", example = "true", required = false)
    private Boolean validateDataIntegrity = true;

    @Schema(description = "Whether to validate business rules", example = "true", required = false)
    private Boolean validateBusinessRules = true;

    @Schema(description = "Whether to validate format", example = "true", required = false)
    private Boolean validateFormat = true;

    @Schema(description = "Expected data hash for integrity check", example = "a1b2c3d4e5f6...", required = false)
    private String expectedDataHash;

    @Schema(description = "Validation context", required = false)
    private java.util.Map<String, String> validationContext;

    public CardValidationDTO(String cardNumber) {
        this.cardNumber = cardNumber;
        this.strictValidation = false;
        this.validateDataIntegrity = true;
        this.validateBusinessRules = true;
        this.validateFormat = true;
        this.validationRules = new ArrayList<>();
        this.validationContext = new java.util.HashMap<>();
    }

    public void addValidationRule(String rule) {
        if (validationRules == null) {
            validationRules = new ArrayList<>();
        }
        validationRules.add(rule);
    }

    public void addValidationContext(String key, String value) {
        if (validationContext == null) {
            validationContext = new java.util.HashMap<>();
        }
        validationContext.put(key, value);
    }

    public boolean hasValidationRules() {
        return validationRules != null && !validationRules.isEmpty();
    }

    public boolean shouldValidateAll() {
        return validateDataIntegrity && validateBusinessRules && validateFormat;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CardStatusDTO {
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Schema(description = "Card number", example = "1234567890123456", required = true)
    private String cardNumber;

    @Pattern(regexp = "^[AISD]$", message = "Record status must be A (Active), I (Inactive), S (Suspended), or D (Deleted)")
    @Schema(description = "New record status", example = "A", required = false)
    private String recordStatus;

    @Pattern(regexp = "^[PRC]$", message = "Processing flag must be P (Pending), R (Processing), or C (Completed)")
    @Schema(description = "New processing flag", example = "P", required = false)
    private String processingFlag;

    @Pattern(regexp = "^[VPF]$", message = "Validation status must be V (Validated), P (Pending), or F (Failed)")
    @Schema(description = "New validation status", example = "V", required = false)
    private String validationStatus;

    @Size(max = 10, message = "Error code must not exceed 10 characters")
    @Schema(description = "Error code", example = "ERR001", required = false)
    private String errorCode;

    @Size(max = 255, message = "Error message must not exceed 255 characters")
    @Schema(description = "Error message", example = "Validation failed", required = false)
    private String errorMessage;

    @Size(max = 255, message = "Status reason must not exceed 255 characters")
    @Schema(description = "Reason for status change", example = "Manual update by administrator", required = false)
    private String statusReason;

    @Schema(description = "Status change timestamp", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime statusChangeTimestamp;

    @Size(max = 50, message = "Changed by must not exceed 50 characters")
    @Schema(description = "User who changed the status", example = "admin_user", required = false)
    private String changedBy;

    @Schema(description = "Whether to force status change", example = "false", required = false)
    private Boolean forceChange = false;

    @Schema(description = "Whether to validate before status change", example = "true", required = false)
    private Boolean validateBeforeChange = true;

    @Schema(description = "Additional status metadata", required = false)
    private java.util.Map<String, String> statusMetadata;

    public CardStatusDTO(String cardNumber, String recordStatus) {
        this.cardNumber = cardNumber;
        this.recordStatus = recordStatus;
        this.statusChangeTimestamp = LocalDateTime.now();
        this.forceChange = false;
        this.validateBeforeChange = true;
        this.statusMetadata = new java.util.HashMap<>();
    }

    public boolean hasStatusChanges() {
        return recordStatus != null || processingFlag != null || validationStatus != null;
    }

    public boolean hasErrorInfo() {
        return errorCode != null || errorMessage != null;
    }

    public void addStatusMetadata(String key, String value) {
        if (statusMetadata == null) {
            statusMetadata = new java.util.HashMap<>();
        }
        statusMetadata.put(key, value);
    }

    public boolean isActivation() {
        return "A".equals(recordStatus);
    }

    public boolean isDeactivation() {
        return "I".equals(recordStatus);
    }

    public boolean isSuspension() {
        return "S".equals(recordStatus);
    }

    public boolean isDeletion() {
        return "D".equals(recordStatus);
    }

    public boolean isValidationUpdate() {
        return validationStatus != null;
    }

    public boolean isProcessingUpdate() {
        return processingFlag != null;
    }

    public boolean isErrorUpdate() {
        return hasErrorInfo();
    }
}