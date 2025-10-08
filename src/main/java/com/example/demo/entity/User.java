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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

@Entity
@Table(name = "usrsec", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id", unique = true),
    @Index(name = "idx_user_type", columnList = "user_type"),
    @Index(name = "idx_user_status", columnList = "user_status"),
    @Index(name = "idx_user_created_at", columnList = "created_at"),
    @Index(name = "idx_user_updated_at", columnList = "updated_at"),
    @Index(name = "idx_user_last_login", columnList = "last_login_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @Column(name = "user_id", length = 8, nullable = false)
    @NotBlank(message = "User ID cannot be blank")
    @Size(min = 8, max = 8, message = "User ID must be exactly 8 characters")
    private String userId;
    
    @Column(name = "first_name", length = 20, nullable = false)
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    private String firstName;
    
    @Column(name = "last_name", length = 20, nullable = false)
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    private String lastName;
    
    @Column(name = "password", length = 64, nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;
    
    @Column(name = "password_salt", length = 32)
    private String passwordSalt;
    
    @Column(name = "user_type", length = 1, nullable = false)
    @NotBlank(message = "User type cannot be blank")
    @Size(min = 1, max = 1, message = "User type must be exactly 1 character")
    private String userType;
    
    @Column(name = "user_status", length = 1, nullable = false)
    private String userStatus = "A";
    
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts = 0;
    
    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked = false;
    
    @Column(name = "password_expired", nullable = false)
    private Boolean passwordExpired = false;
    
    @Column(name = "force_password_change", nullable = false)
    private Boolean forcePasswordChange = false;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;
    
    @Column(name = "password_expiry_date")
    private LocalDateTime passwordExpiryDate;
    
    @Column(name = "account_expiry_date")
    private LocalDateTime accountExpiryDate;
    
    @Column(name = "session_token", length = 128)
    private String sessionToken;
    
    @Column(name = "session_expiry")
    private LocalDateTime sessionExpiry;
    
    @Column(name = "created_by", length = 8)
    private String createdBy;
    
    @Column(name = "updated_by", length = 8)
    private String updatedBy;
    
    @Column(name = "login_count", nullable = false)
    private Long loginCount = 0L;
    
    @Column(name = "last_ip_address", length = 45)
    private String lastIpAddress;
    
    @Column(name = "security_question", length = 255)
    private String securityQuestion;
    
    @Column(name = "security_answer_hash", length = 64)
    private String securityAnswerHash;
    
    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled = false;
    
    @Column(name = "two_factor_secret", length = 32)
    private String twoFactorSecret;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String userId, String firstName, String lastName, String password, String userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.userStatus = "A";
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.passwordExpired = false;
        this.forcePasswordChange = false;
        this.loginCount = 0L;
        this.twoFactorEnabled = false;
        setPassword(password);
    }
    
    public User(String userId, String firstName, String lastName, String password, String userType, String createdBy) {
        this(userId, firstName, lastName, password, userType);
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }
    
    @PrePersist
    @PreUpdate
    private void updateSecurityMetrics() {
        if (password != null && passwordSalt == null) {
            generatePasswordSalt();
        }
        
        if (passwordExpiryDate == null && lastPasswordChange != null) {
            passwordExpiryDate = lastPasswordChange.plusDays(90);
        }
        
        if (passwordExpiryDate != null && passwordExpiryDate.isBefore(LocalDateTime.now())) {
            passwordExpired = true;
        }
        
        if (failedLoginAttempts >= 5) {
            accountLocked = true;
        }
    }
    
    public void setPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        if (plainPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        generatePasswordSalt();
        this.password = hashPassword(plainPassword, passwordSalt);
        this.lastPasswordChange = LocalDateTime.now();
        this.passwordExpiryDate = LocalDateTime.now().plusDays(90);
        this.passwordExpired = false;
        this.forcePasswordChange = false;
        this.failedLoginAttempts = 0;
    }
    
    public boolean verifyPassword(String plainPassword) {
        if (plainPassword == null || password == null || passwordSalt == null) {
            return false;
        }
        
        String hashedInput = hashPassword(plainPassword, passwordSalt);
        return Objects.equals(password, hashedInput);
    }
    
    private void generatePasswordSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.passwordSalt = bytesToHex(salt);
    }
    
    private String hashPassword(String plainPassword, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(plainPassword.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isAdmin() {
        return "A".equals(userType);
    }
    
    public boolean isRegular() {
        return "R".equals(userType);
    }
    
    public boolean isActive() {
        return "A".equals(userStatus);
    }
    
    public boolean isInactive() {
        return "I".equals(userStatus);
    }
    
    public boolean isSuspended() {
        return "S".equals(userStatus);
    }
    
    public boolean isDeleted() {
        return "D".equals(userStatus);
    }
    
    public boolean isLocked() {
        return accountLocked != null && accountLocked;
    }
    
    public boolean isPasswordExpired() {
        return passwordExpired != null && passwordExpired;
    }
    
    public boolean requiresPasswordChange() {
        return forcePasswordChange != null && forcePasswordChange;
    }
    
    public boolean isAccountExpired() {
        return accountExpiryDate != null && accountExpiryDate.isBefore(LocalDateTime.now());
    }
    
    public boolean canLogin() {
        return isActive() && !isLocked() && !isAccountExpired();
    }
    
    public boolean canAuthenticate(String plainPassword) {
        return canLogin() && verifyPassword(plainPassword);
    }
    
    public boolean hasValidSession() {
        return sessionToken != null && 
               sessionExpiry != null && 
               sessionExpiry.isAfter(LocalDateTime.now());
    }
    
    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled != null && twoFactorEnabled;
    }
    
    public void recordSuccessfulLogin(String ipAddress) {
        this.lastLoginAt = LocalDateTime.now();
        this.lastIpAddress = ipAddress;
        this.failedLoginAttempts = 0;
        this.loginCount = (loginCount != null ? loginCount : 0L) + 1;
        
        if (isLocked()) {
            this.accountLocked = false;
        }
    }
    
    public void recordFailedLogin() {
        this.failedLoginAttempts = (failedLoginAttempts != null ? failedLoginAttempts : 0) + 1;
        
        if (failedLoginAttempts >= 5) {
            this.accountLocked = true;
        }
    }
    
    public void createSession() {
        this.sessionToken = generateSessionToken();
        this.sessionExpiry = LocalDateTime.now().plusHours(8);
    }
    
    public void invalidateSession() {
        this.sessionToken = null;
        this.sessionExpiry = null;
    }
    
    private String generateSessionToken() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[64];
        random.nextBytes(token);
        return bytesToHex(token);
    }
    
    public void activate() {
        this.userStatus = "A";
        this.accountLocked = false;
        this.failedLoginAttempts = 0;
    }
    
    public void deactivate() {
        this.userStatus = "I";
        invalidateSession();
    }
    
    public void suspend() {
        this.userStatus = "S";
        invalidateSession();
    }
    
    public void markAsDeleted() {
        this.userStatus = "D";
        invalidateSession();
    }
    
    public void unlock() {
        this.accountLocked = false;
        this.failedLoginAttempts = 0;
    }
    
    public void lock() {
        this.accountLocked = true;
        invalidateSession();
    }
    
    public void forcePasswordChange() {
        this.forcePasswordChange = true;
        this.passwordExpired = true;
        invalidateSession();
    }
    
    public void enableTwoFactor() {
        this.twoFactorEnabled = true;
        this.twoFactorSecret = generateTwoFactorSecret();
    }
    
    public void disableTwoFactor() {
        this.twoFactorEnabled = false;
        this.twoFactorSecret = null;
    }
    
    private String generateTwoFactorSecret() {
        SecureRandom random = new SecureRandom();
        byte[] secret = new byte[16];
        random.nextBytes(secret);
        return bytesToHex(secret);
    }
    
    public void setSecurityQuestion(String question, String answer) {
        this.securityQuestion = question;
        if (answer != null) {
            this.securityAnswerHash = hashSecurityAnswer(answer);
        }
    }
    
    public boolean verifySecurityAnswer(String answer) {
        if (answer == null || securityAnswerHash == null) {
            return false;
        }
        
        String hashedAnswer = hashSecurityAnswer(answer);
        return Objects.equals(securityAnswerHash, hashedAnswer);
    }
    
    private String hashSecurityAnswer(String answer) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedAnswer = md.digest(answer.toLowerCase().trim().getBytes());
            return bytesToHex(hashedAnswer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing security answer", e);
        }
    }
    
    public String getUserTypeDisplayName() {
        return switch (userType) {
            case "A" -> "Administrator";
            case "R" -> "Regular User";
            default -> "Unknown";
        };
    }
    
    public String getUserStatusDisplayName() {
        return switch (userStatus) {
            case "A" -> "Active";
            case "I" -> "Inactive";
            case "S" -> "Suspended";
            case "D" -> "Deleted";
            default -> "Unknown";
        };
    }
    
    public boolean isPasswordNearExpiry(int daysThreshold) {
        if (passwordExpiryDate == null) return false;
        return passwordExpiryDate.isBefore(LocalDateTime.now().plusDays(daysThreshold));
    }
    
    public long getDaysSinceLastLogin() {
        if (lastLoginAt == null) return Long.MAX_VALUE;
        return java.time.Duration.between(lastLoginAt, LocalDateTime.now()).toDays();
    }
    
    public long getDaysSincePasswordChange() {
        if (lastPasswordChange == null) return Long.MAX_VALUE;
        return java.time.Duration.between(lastPasswordChange, LocalDateTime.now()).toDays();
    }
    
    public long getDaysUntilPasswordExpiry() {
        if (passwordExpiryDate == null) return Long.MAX_VALUE;
        return java.time.Duration.between(LocalDateTime.now(), passwordExpiryDate).toDays();
    }
    
    public long getDaysUntilAccountExpiry() {
        if (accountExpiryDate == null) return Long.MAX_VALUE;
        return java.time.Duration.between(LocalDateTime.now(), accountExpiryDate).toDays();
    }
    
    public boolean isRecentlyCreated(int days) {
        if (createdAt == null) return false;
        return createdAt.isAfter(LocalDateTime.now().minusDays(days));
    }
    
    public boolean isRecentlyUpdated(int hours) {
        if (updatedAt == null) return false;
        return updatedAt.isAfter(LocalDateTime.now().minusHours(hours));
    }
    
    public boolean isRecentlyLoggedIn(int hours) {
        if (lastLoginAt == null) return false;
        return lastLoginAt.isAfter(LocalDateTime.now().minusHours(hours));
    }
    
    public boolean isFrequentUser(long minimumLogins) {
        return loginCount != null && loginCount >= minimumLogins;
    }
    
    public boolean hasSecurityQuestion() {
        return securityQuestion != null && !securityQuestion.trim().isEmpty();
    }
    
    public boolean isHighRiskAccount() {
        return failedLoginAttempts >= 3 || 
               getDaysSinceLastLogin() > 90 ||
               isPasswordNearExpiry(7) ||
               (accountExpiryDate != null && getDaysUntilAccountExpiry() <= 30);
    }
    
    public void extendAccountExpiry(int days) {
        if (accountExpiryDate == null) {
            this.accountExpiryDate = LocalDateTime.now().plusDays(days);
        } else {
            this.accountExpiryDate = accountExpiryDate.plusDays(days);
        }
    }
    
    public void extendPasswordExpiry(int days) {
        if (passwordExpiryDate == null) {
            this.passwordExpiryDate = LocalDateTime.now().plusDays(days);
        } else {
            this.passwordExpiryDate = passwordExpiryDate.plusDays(days);
        }
        this.passwordExpired = false;
    }
    
    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        if (isLocked()) {
            this.accountLocked = false;
        }
    }
    
    public void updateProfile(String firstName, String lastName, String updatedBy) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            this.firstName = firstName.trim();
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            this.lastName = lastName.trim();
        }
        this.updatedBy = updatedBy;
    }
    
    public void changeUserType(String newUserType, String updatedBy) {
        if ("A".equals(newUserType) || "R".equals(newUserType)) {
            this.userType = newUserType;
            this.updatedBy = updatedBy;
        } else {
            throw new IllegalArgumentException("Invalid user type. Must be 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    public boolean validateUserId() {
        return userId != null && 
               userId.length() == 8 && 
               userId.matches("^[A-Za-z0-9]{8}$");
    }
    
    public boolean validateUserType() {
        return "A".equals(userType) || "R".equals(userType);
    }
    
    public boolean validateUserStatus() {
        return "A".equals(userStatus) || "I".equals(userStatus) || 
               "S".equals(userStatus) || "D".equals(userStatus);
    }
    
    public void copySecuritySettingsFrom(User source) {
        if (source == null) return;
        
        this.twoFactorEnabled = source.twoFactorEnabled;
        this.securityQuestion = source.securityQuestion;
        this.securityAnswerHash = source.securityAnswerHash;
        this.passwordExpiryDate = source.passwordExpiryDate;
        this.accountExpiryDate = source.accountExpiryDate;
    }
    
    public boolean hasPermission(String permission) {
        if (isAdmin()) {
            return true;
        }
        
        return switch (permission.toLowerCase()) {
            case "read", "view" -> isActive();
            case "create", "update", "delete" -> false;
            default -> false;
        };
    }
    
    public void auditSecurityEvent(String eventType, String details) {
        // This method can be extended to log security events
        // For now, it updates the last accessed timestamp
        this.updatedAt = LocalDateTime.now();
    }
}