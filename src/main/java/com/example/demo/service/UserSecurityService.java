package com.example.demo.service;

import com.example.demo.dto.SignonRequest;
import com.example.demo.dto.SignonResponse;
import com.example.demo.dto.UserRegistrationRequest;
import com.example.demo.dto.UserRegistrationResponse;
import com.example.demo.dto.UserSecurityDTO;
import com.example.demo.entity.UserSecurity;
import com.example.demo.repository.UserSecurityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService {
    
    private final UserSecurityRepository userSecurityRepository;
    
    @Transactional(readOnly = true)
    public SignonResponse authenticateUser(SignonRequest request) {
        log.info("Attempting authentication for user: {}", request.getUserId());
        
        SignonResponse response = new SignonResponse();
        
        try {
            // Input validation
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                log.warn("Authentication failed: User ID is empty");
                response.setSuccess(false);
                response.setErrorMessage("User ID is required");
                response.setErrorCode("EMPTY_USER_ID");
                return response;
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                log.warn("Authentication failed: Password is empty for user: {}", request.getUserId());
                response.setSuccess(false);
                response.setErrorMessage("Password is required");
                response.setErrorCode("EMPTY_PASSWORD");
                return response;
            }
            
            // Case-insensitive user ID lookup
            String normalizedUserId = request.getUserId().toUpperCase();
            Optional<UserSecurity> userOptional = userSecurityRepository.findByUserIdIgnoreCase(normalizedUserId);
            
            if (userOptional.isEmpty()) {
                log.warn("Authentication failed: User not found: {}", normalizedUserId);
                response.setSuccess(false);
                response.setErrorMessage("User not found");
                response.setErrorCode("USER_NOT_FOUND");
                return response;
            }
            
            UserSecurity user = userOptional.get();
            
            // Exact password match
            if (!user.validatePassword(request.getPassword())) {
                log.warn("Authentication failed: Wrong password for user: {}", normalizedUserId);
                response.setSuccess(false);
                response.setErrorMessage("Invalid password");
                response.setErrorCode("WRONG_PASSWORD");
                return response;
            }
            
            // User type validation
            if (!user.hasValidUserType()) {
                log.warn("Authentication failed: Invalid user type for user: {}", normalizedUserId);
                response.setSuccess(false);
                response.setErrorMessage("Invalid user type");
                response.setErrorCode("INVALID_USER_TYPE");
                return response;
            }
            
            // Successful authentication
            log.info("Authentication successful for user: {}", normalizedUserId);
            response.setSuccess(true);
            response.setUserId(user.getUserId());
            response.setUserType(user.getUserType());
            response.setFullName(user.getFullName());
            response.setRedirectUrl(determineRedirectUrl(user.getUserType()));
            
            return response;
            
        } catch (Exception e) {
            log.error("Authentication error for user: {}", request.getUserId(), e);
            response.setSuccess(false);
            response.setErrorMessage("Authentication system error");
            response.setErrorCode("SYSTEM_ERROR");
            return response;
        }
    }
    
    @Transactional
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        log.info("Attempting user registration for user ID: {}", request.getUserId());
        
        UserRegistrationResponse response = new UserRegistrationResponse();
        
        try {
            // Input validation
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
                log.warn("Registration failed: First name is empty");
                response.setSuccess(false);
                response.setErrorMessage("First name is required");
                response.setErrorCode("EMPTY_FIRST_NAME");
                return response;
            }
            
            if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
                log.warn("Registration failed: Last name is empty");
                response.setSuccess(false);
                response.setErrorMessage("Last name is required");
                response.setErrorCode("EMPTY_LAST_NAME");
                return response;
            }
            
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                log.warn("Registration failed: User ID is empty");
                response.setSuccess(false);
                response.setErrorMessage("User ID is required");
                response.setErrorCode("EMPTY_USER_ID");
                return response;
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                log.warn("Registration failed: Password is empty");
                response.setSuccess(false);
                response.setErrorMessage("Password is required");
                response.setErrorCode("EMPTY_PASSWORD");
                return response;
            }
            
            if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
                log.warn("Registration failed: User type is empty");
                response.setSuccess(false);
                response.setErrorMessage("User type is required");
                response.setErrorCode("EMPTY_USER_TYPE");
                return response;
            }
            
            // User type validation
            String normalizedUserType = request.getUserType().toUpperCase();
            if (!"A".equals(normalizedUserType) && !"R".equals(normalizedUserType)) {
                log.warn("Registration failed: Invalid user type: {}", normalizedUserType);
                response.setSuccess(false);
                response.setErrorMessage("User type must be A (Admin) or R (Regular)");
                response.setErrorCode("INVALID_USER_TYPE");
                return response;
            }
            
            // Duplicate checking
            String normalizedUserId = request.getUserId().toUpperCase();
            if (userSecurityRepository.existsByUserIdIgnoreCase(normalizedUserId)) {
                log.warn("Registration failed: User ID already exists: {}", normalizedUserId);
                response.setSuccess(false);
                response.setErrorMessage("User ID already exists");
                response.setErrorCode("USER_EXISTS");
                return response;
            }
            
            // Create new user
            UserSecurity newUser = new UserSecurity(
                normalizedUserId,
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getPassword(),
                normalizedUserType
            );
            
            UserSecurity savedUser = userSecurityRepository.save(newUser);
            
            log.info("User registration successful for user ID: {}", normalizedUserId);
            response.setSuccess(true);
            response.setUserId(savedUser.getUserId());
            response.setFullName(savedUser.getFullName());
            response.setUserType(savedUser.getUserType());
            response.setSuccessMessage("User registered successfully");
            response.setRegisteredAt(savedUser.getCreatedAt());
            
            return response;
            
        } catch (Exception e) {
            log.error("Registration error for user: {}", request.getUserId(), e);
            response.setSuccess(false);
            response.setErrorMessage("Registration system error");
            response.setErrorCode("SYSTEM_ERROR");
            return response;
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<UserSecurityDTO> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Get user failed: User ID is empty");
            return Optional.empty();
        }
        
        String normalizedUserId = userId.toUpperCase();
        return userSecurityRepository.findByUserIdIgnoreCase(normalizedUserId)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UserSecurityDTO> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        return userSecurityRepository.findAll(pageable).map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UserSecurityDTO> getUsersByType(String userType, Pageable pageable) {
        log.info("Retrieving users by type: {}", userType);
        
        if (userType == null || userType.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        
        String normalizedUserType = userType.toUpperCase();
        return userSecurityRepository.findByUserType(normalizedUserType, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional
    public boolean updateUser(String userId, UserRegistrationRequest updateRequest) {
        log.info("Updating user: {}", userId);
        
        try {
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("Update failed: User ID is empty");
                return false;
            }
            
            String normalizedUserId = userId.toUpperCase();
            Optional<UserSecurity> userOptional = userSecurityRepository.findByUserIdIgnoreCase(normalizedUserId);
            
            if (userOptional.isEmpty()) {
                log.warn("Update failed: User not found: {}", normalizedUserId);
                return false;
            }
            
            UserSecurity user = userOptional.get();
            
            // Update fields if provided
            if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().trim().isEmpty()) {
                user.setFirstName(updateRequest.getFirstName().trim());
            }
            
            if (updateRequest.getLastName() != null && !updateRequest.getLastName().trim().isEmpty()) {
                user.setLastName(updateRequest.getLastName().trim());
            }
            
            if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
                user.setPassword(updateRequest.getPassword());
            }
            
            if (updateRequest.getUserType() != null && !updateRequest.getUserType().trim().isEmpty()) {
                String normalizedUserType = updateRequest.getUserType().toUpperCase();
                if ("A".equals(normalizedUserType) || "R".equals(normalizedUserType)) {
                    user.setUserType(normalizedUserType);
                } else {
                    log.warn("Update failed: Invalid user type: {}", normalizedUserType);
                    return false;
                }
            }
            
            userSecurityRepository.save(user);
            log.info("User updated successfully: {}", normalizedUserId);
            return true;
            
        } catch (Exception e) {
            log.error("Update error for user: {}", userId, e);
            return false;
        }
    }
    
    @Transactional
    public boolean deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        try {
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("Delete failed: User ID is empty");
                return false;
            }
            
            String normalizedUserId = userId.toUpperCase();
            if (!userSecurityRepository.existsByUserIdIgnoreCase(normalizedUserId)) {
                log.warn("Delete failed: User not found: {}", normalizedUserId);
                return false;
            }
            
            userSecurityRepository.deleteById(normalizedUserId);
            log.info("User deleted successfully: {}", normalizedUserId);
            return true;
            
        } catch (Exception e) {
            log.error("Delete error for user: {}", userId, e);
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        
        String normalizedUserId = userId.toUpperCase();
        return userSecurityRepository.existsByUserIdIgnoreCase(normalizedUserId);
    }
    
    @Transactional(readOnly = true)
    public long countUsersByType(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            return 0;
        }
        
        String normalizedUserType = userType.toUpperCase();
        return userSecurityRepository.countByUserType(normalizedUserType);
    }
    
    private String determineRedirectUrl(String userType) {
        if ("A".equals(userType)) {
            return "/admin/dashboard";
        } else if ("R".equals(userType)) {
            return "/user/dashboard";
        }
        return "/dashboard";
    }
    
    private UserSecurityDTO convertToDTO(UserSecurity user) {
        UserSecurityDTO dto = new UserSecurityDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFirstName(), user.getLastName());
        dto.setUserType(user.getUserType());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}