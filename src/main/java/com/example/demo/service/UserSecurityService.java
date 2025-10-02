package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;
import com.example.demo.entity.UserSecurity;
import com.example.demo.entity.UserSecurity.UserType;
import com.example.demo.repository.UserSecurityRepository;
import com.example.demo.dto.SignonRequestDTO;
import com.example.demo.dto.SignonResponseDTO;
import com.example.demo.dto.UserSecurityDTO;
import com.example.demo.dto.CreateUserSecurityRequest;
import com.example.demo.dto.UpdateUserSecurityRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService {
    
    private final UserSecurityRepository userSecurityRepository;
    
    @Transactional(readOnly = true)
    public SignonResponseDTO authenticateUser(SignonRequestDTO request) {
        log.info("Attempting authentication for user: {}", request.getUserId());
        
        // Validate credentials first
        String validationError = validateCredentials(request);
        if (validationError != null) {
            log.warn("Credential validation failed for user {}: {}", request.getUserId(), validationError);
            return SignonResponseDTO.failure(validationError);
        }
        
        // Convert to uppercase for consistency
        String userId = request.getUserId().toUpperCase();
        String password = request.getPassword().toUpperCase();
        
        // Find user by ID
        Optional<UserSecurity> userOptional = userSecurityRepository.findByUserIdIgnoreCase(userId);
        if (userOptional.isEmpty()) {
            log.warn("User not found: {}", userId);
            return SignonResponseDTO.userNotFound();
        }
        
        UserSecurity user = userOptional.get();
        
        // Check if user is active
        if (!user.canAuthenticate()) {
            log.warn("User {} is not active", userId);
            return SignonResponseDTO.failure("User account is inactive");
        }
        
        // Validate password
        if (!password.equals(user.getPassword())) {
            log.warn("Wrong password for user: {}", userId);
            return SignonResponseDTO.wrongPassword();
        }
        
        log.info("Authentication successful for user: {} with type: {}", userId, user.getUserType());
        return SignonResponseDTO.success(user.getUserType(), user.getRedirectProgram());
    }
    
    public String validateCredentials(SignonRequestDTO request) {
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            return "Please enter User ID";
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return "Please enter Password";
        }
        
        if (request.getUserId().length() > 8) {
            return "User ID must not exceed 8 characters";
        }
        
        if (request.getPassword().length() > 8) {
            return "Password must not exceed 8 characters";
        }
        
        return null;
    }
    
    @Transactional
    public UserSecurityDTO createUser(CreateUserSecurityRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        String userId = request.getUserId().toUpperCase();
        String password = request.getPassword().toUpperCase();
        
        if (userSecurityRepository.existsByUserIdIgnoreCase(userId)) {
            throw new IllegalArgumentException("User with ID already exists: " + userId);
        }
        
        UserSecurity user = new UserSecurity();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUserType(request.getUserType());
        user.setProgramName(request.getProgramName() != null ? request.getProgramName() : "COSGN00C");
        user.setTransactionId(request.getTransactionId() != null ? request.getTransactionId() : "CC00");
        user.setActive(request.getActive() != null ? request.getActive() : true);
        
        UserSecurity savedUser = userSecurityRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUserId());
        
        return convertToDTO(savedUser);
    }
    
    @Transactional
    public UserSecurityDTO updateUser(String userId, UpdateUserSecurityRequest request) {
        log.info("Updating user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        UserSecurity user = userSecurityRepository.findByUserIdIgnoreCase(upperUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + upperUserId));
        
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword().toUpperCase());
        }
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }
        if (request.getProgramName() != null) {
            user.setProgramName(request.getProgramName());
        }
        if (request.getTransactionId() != null) {
            user.setTransactionId(request.getTransactionId());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        
        UserSecurity updatedUser = userSecurityRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        if (!userSecurityRepository.existsByUserIdIgnoreCase(upperUserId)) {
            throw new IllegalArgumentException("User not found: " + upperUserId);
        }
        
        userSecurityRepository.deleteById(upperUserId);
        log.info("User deleted successfully: {}", upperUserId);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserSecurityDTO> getUserById(String userId) {
        String upperUserId = userId.toUpperCase();
        return userSecurityRepository.findByUserIdIgnoreCase(upperUserId)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<UserSecurityDTO> findActiveUsers() {
        log.info("Finding all active users");
        return userSecurityRepository.findActiveUsers()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<UserSecurityDTO> findByUserType(UserType userType) {
        log.info("Finding users by type: {}", userType);
        return userSecurityRepository.findByUserType(userType)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Page<UserSecurityDTO> getAllUsers(Pageable pageable) {
        log.info("Getting all users with pagination");
        return userSecurityRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UserSecurityDTO> getActiveUsers(Pageable pageable) {
        log.info("Getting active users with pagination");
        return userSecurityRepository.findActiveUsers(pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userSecurityRepository.countActiveUsers();
    }
    
    @Transactional(readOnly = true)
    public long countUsersByType(UserType userType) {
        return userSecurityRepository.countActiveUsersByType(userType);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String userId) {
        String upperUserId = userId.toUpperCase();
        return userSecurityRepository.existsByUserIdIgnoreCase(upperUserId);
    }
    
    @Transactional
    public UserSecurityDTO activateUser(String userId) {
        log.info("Activating user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        UserSecurity user = userSecurityRepository.findByUserIdIgnoreCase(upperUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + upperUserId));
        
        user.setActive(true);
        UserSecurity updatedUser = userSecurityRepository.save(user);
        
        log.info("User activated successfully: {}", updatedUser.getUserId());
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public UserSecurityDTO deactivateUser(String userId) {
        log.info("Deactivating user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        UserSecurity user = userSecurityRepository.findByUserIdIgnoreCase(upperUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + upperUserId));
        
        user.setActive(false);
        UserSecurity updatedUser = userSecurityRepository.save(user);
        
        log.info("User deactivated successfully: {}", updatedUser.getUserId());
        return convertToDTO(updatedUser);
    }
    
    @Transactional
    public UserSecurityDTO changePassword(String userId, String newPassword) {
        log.info("Changing password for user: {}", userId);
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter Password");
        }
        
        if (newPassword.length() > 8) {
            throw new IllegalArgumentException("Password must not exceed 8 characters");
        }
        
        String upperUserId = userId.toUpperCase();
        String upperPassword = newPassword.toUpperCase();
        
        UserSecurity user = userSecurityRepository.findByUserIdIgnoreCase(upperUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + upperUserId));
        
        user.setPassword(upperPassword);
        UserSecurity updatedUser = userSecurityRepository.save(user);
        
        log.info("Password changed successfully for user: {}", updatedUser.getUserId());
        return convertToDTO(updatedUser);
    }
    
    private UserSecurityDTO convertToDTO(UserSecurity user) {
        UserSecurityDTO dto = new UserSecurityDTO();
        dto.setUserId(user.getUserId());
        dto.setPassword(user.getPassword());
        dto.setUserType(user.getUserType());
        dto.setProgramName(user.getProgramName());
        dto.setTransactionId(user.getTransactionId());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setUserTypeDisplayName(user.getUserType().getDisplayName());
        dto.setRedirectProgram(user.getRedirectProgram());
        dto.setCanAuthenticate(user.canAuthenticate());
        return dto;
    }
}