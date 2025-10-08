package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cards", indexes = {
    @Index(name = "idx_card_number", columnList = "card_number", unique = true),
    @Index(name = "idx_card_data_hash", columnList = "card_data_hash"),
    @Index(name = "idx_card_created_at", columnList = "created_at"),
    @Index(name = "idx_card_updated_at", columnList = "updated_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;
    
    @Column(name = "card_data", length = 134, nullable = false)
    @NotBlank(message = "Card data cannot be blank")
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    private String cardData;
    
    @Column(name = "card_data_hash", length = 64)
    private String cardDataHash;
    
    @Column(name = "data_length", nullable = false)
    private Integer dataLength;
    
    @Column(name = "record_status", length = 1, nullable = false)
    private String recordStatus = "A";
    
    @Column(name = "processing_flag", length = 1)
    private String processingFlag;
    
    @Column(name = "validation_status", length = 1)
    private String validationStatus;
    
    @Column(name = "error_code", length = 10)
    private String errorCode;
    
    @Column(name = "error_message", length = 255)
    private String errorMessage;
    
    @Column(name = "sequence_number")
    private Long sequenceNumber;
    
    @Column(name = "batch_id", length = 20)
    private String batchId;
    
    @Column(name = "source_system", length = 10)
    private String sourceSystem;
    
    @Column(name = "target_system", length = 10)
    private String targetSystem;
    
    @Column(name = "processing_date")
    private LocalDateTime processingDate;
    
    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Card(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
        this.dataLength = cardData != null ? cardData.length() : 0;
        this.recordStatus = "A";
        this.validationStatus = "P";
        this.cardDataHash = generateDataHash(cardData);
    }
    
    public Card(String cardNumber, String cardData, String batchId, String sourceSystem) {
        this(cardNumber, cardData);
        this.batchId = batchId;
        this.sourceSystem = sourceSystem;
    }
    
    @PrePersist
    @PreUpdate
    private void updateDataMetrics() {
        if (cardData != null) {
            this.dataLength = cardData.length();
            this.cardDataHash = generateDataHash(cardData);
        }
        this.lastAccessed = LocalDateTime.now();
    }
    
    private String generateDataHash(String data) {
        if (data == null) return null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
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
    
    public boolean isActive() {
        return "A".equals(recordStatus);
    }
    
    public boolean isInactive() {
        return "I".equals(recordStatus);
    }
    
    public boolean isDeleted() {
        return "D".equals(recordStatus);
    }
    
    public boolean isSuspended() {
        return "S".equals(recordStatus);
    }
    
    public boolean isPending() {
        return "P".equals(processingFlag);
    }
    
    public boolean isProcessed() {
        return "C".equals(processingFlag);
    }
    
    public boolean isProcessing() {
        return "R".equals(processingFlag);
    }
    
    public boolean isValidated() {
        return "V".equals(validationStatus);
    }
    
    public boolean isValidationPending() {
        return "P".equals(validationStatus);
    }
    
    public boolean isValidationFailed() {
        return "F".equals(validationStatus);
    }
    
    public boolean hasError() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }
    
    public boolean isReadyForProcessing() {
        return isValidCardNumber() && 
               isValidCardData() && 
               isActive() && 
               !hasError() &&
               (isValidated() || isValidationPending());
    }
    
    public boolean isExpired() {
        return processingDate != null && 
               processingDate.isBefore(LocalDateTime.now().minusDays(30));
    }
    
    public boolean requiresRevalidation() {
        return lastAccessed != null && 
               lastAccessed.isBefore(LocalDateTime.now().minusHours(24));
    }
    
    public void markAsProcessed() {
        this.processingFlag = "C";
        this.processingDate = LocalDateTime.now();
        this.validationStatus = "V";
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    public void markAsProcessing() {
        this.processingFlag = "R";
        this.processingDate = LocalDateTime.now();
    }
    
    public void markAsPending() {
        this.processingFlag = "P";
        this.validationStatus = "P";
    }
    
    public void markAsValidated() {
        this.validationStatus = "V";
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    public void markAsValidationFailed(String errorCode, String errorMessage) {
        this.validationStatus = "F";
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public void activate() {
        this.recordStatus = "A";
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    public void deactivate() {
        this.recordStatus = "I";
        this.processingFlag = null;
    }
    
    public void suspend() {
        this.recordStatus = "S";
        this.processingFlag = null;
    }
    
    public void markAsDeleted() {
        this.recordStatus = "D";
        this.processingFlag = null;
        this.validationStatus = null;
    }
    
    public void updateCardData(String newCardData) {
        if (newCardData != null && newCardData.length() <= 134) {
            this.cardData = newCardData;
            this.dataLength = newCardData.length();
            this.cardDataHash = generateDataHash(newCardData);
            this.validationStatus = "P";
            this.lastAccessed = LocalDateTime.now();
        }
    }
    
    public void setProcessingMetadata(String batchId, String sourceSystem, String targetSystem) {
        this.batchId = batchId;
        this.sourceSystem = sourceSystem;
        this.targetSystem = targetSystem;
    }
    
    public String getRecordStatusDisplayName() {
        return switch (recordStatus) {
            case "A" -> "Active";
            case "I" -> "Inactive";
            case "S" -> "Suspended";
            case "D" -> "Deleted";
            default -> "Unknown";
        };
    }
    
    public String getProcessingFlagDisplayName() {
        return switch (processingFlag) {
            case "P" -> "Pending";
            case "R" -> "Processing";
            case "C" -> "Completed";
            default -> "None";
        };
    }
    
    public String getValidationStatusDisplayName() {
        return switch (validationStatus) {
            case "V" -> "Validated";
            case "P" -> "Pending";
            case "F" -> "Failed";
            default -> "Unknown";
        };
    }
    
    public boolean isDataIntegrityValid() {
        if (cardData == null) return false;
        String currentHash = generateDataHash(cardData);
        return Objects.equals(currentHash, cardDataHash);
    }
    
    public void refreshDataIntegrity() {
        if (cardData != null) {
            this.cardDataHash = generateDataHash(cardData);
            this.dataLength = cardData.length();
        }
    }
    
    public boolean isSameCardData(String otherCardData) {
        if (cardData == null && otherCardData == null) return true;
        if (cardData == null || otherCardData == null) return false;
        return cardData.equals(otherCardData);
    }
    
    public boolean hasDataChanged(Card other) {
        if (other == null) return true;
        return !Objects.equals(this.cardData, other.cardData) ||
               !Objects.equals(this.cardDataHash, other.cardDataHash) ||
               !Objects.equals(this.dataLength, other.dataLength);
    }
    
    public void copyDataFrom(Card source) {
        if (source == null) return;
        
        this.cardData = source.cardData;
        this.dataLength = source.dataLength;
        this.cardDataHash = source.cardDataHash;
        this.recordStatus = source.recordStatus;
        this.processingFlag = source.processingFlag;
        this.validationStatus = source.validationStatus;
        this.errorCode = source.errorCode;
        this.errorMessage = source.errorMessage;
        this.batchId = source.batchId;
        this.sourceSystem = source.sourceSystem;
        this.targetSystem = source.targetSystem;
    }
    
    public boolean isProcessingStale() {
        return processingDate != null && 
               "R".equals(processingFlag) &&
               processingDate.isBefore(LocalDateTime.now().minusMinutes(30));
    }
    
    public void resetProcessingState() {
        this.processingFlag = "P";
        this.processingDate = null;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    public String getCardDataSubstring(int start, int length) {
        if (cardData == null || start < 0 || start >= cardData.length()) {
            return "";
        }
        int endIndex = Math.min(start + length, cardData.length());
        return cardData.substring(start, endIndex);
    }
    
    public boolean containsCardDataPattern(String pattern) {
        return cardData != null && cardData.contains(pattern);
    }
    
    public int getCardDataOccurrences(String pattern) {
        if (cardData == null || pattern == null) return 0;
        int count = 0;
        int index = 0;
        while ((index = cardData.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
    
    public boolean isCardDataNumeric() {
        return cardData != null && cardData.matches("^\\d+$");
    }
    
    public boolean isCardDataAlphanumeric() {
        return cardData != null && cardData.matches("^[A-Za-z0-9]+$");
    }
    
    public String getCardDataChecksum() {
        if (cardData == null) return null;
        int checksum = 0;
        for (char c : cardData.toCharArray()) {
            checksum += (int) c;
        }
        return String.valueOf(checksum % 1000);
    }
    
    public void touchLastAccessed() {
        this.lastAccessed = LocalDateTime.now();
    }
    
    public long getMinutesSinceLastAccess() {
        if (lastAccessed == null) return Long.MAX_VALUE;
        return java.time.Duration.between(lastAccessed, LocalDateTime.now()).toMinutes();
    }
    
    public long getMinutesSinceCreation() {
        if (createdAt == null) return 0;
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toMinutes();
    }
    
    public long getMinutesSinceLastUpdate() {
        if (updatedAt == null) return 0;
        return java.time.Duration.between(updatedAt, LocalDateTime.now()).toMinutes();
    }
    
    public boolean isRecentlyCreated(int minutes) {
        return getMinutesSinceCreation() <= minutes;
    }
    
    public boolean isRecentlyUpdated(int minutes) {
        return getMinutesSinceLastUpdate() <= minutes;
    }
    
    public boolean isRecentlyAccessed(int minutes) {
        return getMinutesSinceLastAccess() <= minutes;
    }
}