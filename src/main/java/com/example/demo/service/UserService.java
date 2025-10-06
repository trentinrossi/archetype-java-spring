package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public UserAuthenticationResponse authenticateUser(UserSignOnRequest request) {
        log.info("Attempting authentication for user ID: {}", request.getUserId());
        
        // Validate empty fields
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            log.warn("Authentication failed: User ID is empty");
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("Authentication failed: Password is empty for user ID: {}", request.getUserId());
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Case-insensitive user lookup
        Optional<User> userOptional = userRepository.findByUserIdIgnoreCase(request.getUserId().trim());
        
        if (userOptional.isEmpty()) {
            log.warn("Authentication failed: User not found with ID: {}", request.getUserId());
            throw new IllegalArgumentException("User not found with ID: " + request.getUserId());
        }
        
        User user = userOptional.get();
        
        // Password matching validation
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Authentication failed: Invalid password for user ID: {}", request.getUserId());
            throw new IllegalArgumentException("Invalid password for user: " + request.getUserId());
        }
        
        log.info("Authentication successful for user ID: {}", request.getUserId());
        return convertToAuthenticationResponse(user);
    }
    
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        // Validate empty fields
        validateCreateUserRequest(request);
        
        // Check for duplicate user ID (case-insensitive)
        if (userRepository.existsByUserIdIgnoreCase(request.getUserId().trim())) {
            log.warn("User creation failed: User ID already exists: {}", request.getUserId());
            throw new IllegalArgumentException("User ID already exists: " + request.getUserId());
        }
        
        User user = new User();
        user.setUserId(request.getUserId().trim().toUpperCase());
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType().toUpperCase());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user with ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Get user failed: User ID is empty");
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        return userRepository.findByUserIdIgnoreCase(userId.trim())
                .map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.info("Updating user with ID: {}", userId);
        
        // Validate user ID
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Update user failed: User ID is empty");
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        // Find user (case-insensitive)
        User user = userRepository.findByUserIdIgnoreCase(userId.trim())
                .orElseThrow(() -> {
                    log.warn("Update user failed: User not found with ID: {}", userId);
                    return new IllegalArgumentException("User not found with ID: " + userId);
                });
        
        // Update fields only if provided and not empty
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }
        
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            user.setUserType(request.getUserType().toUpperCase());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        // Validate user ID
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Delete user failed: User ID is empty");
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        // Check if user exists (case-insensitive)
        User user = userRepository.findByUserIdIgnoreCase(userId.trim())
                .orElseThrow(() -> {
                    log.warn("Delete user failed: User not found with ID: {}", userId);
                    return new IllegalArgumentException("User not found with ID: " + userId);
                });
        
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByType(String userType, Pageable pageable) {
        log.info("Retrieving users by type: {}", userType);
        
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User type cannot be empty");
        }
        
        return userRepository.findByUserType(userType.toUpperCase(), pageable)
                .map(this::convertToResponse);
    }
    
    private void validateCreateUserRequest(UserCreateRequest request) {
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type cannot be empty");
        }
        
        if (!request.getUserType().toUpperCase().matches("^[AU]$")) {
            throw new IllegalArgumentException("User type must be 'A' (Admin) or 'U' (User)");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        response.setAdmin(user.isAdmin());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private UserAuthenticationResponse convertToAuthenticationResponse(User user) {
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        response.setAdmin(user.isAdmin());
        response.setAuthenticated(true);
        response.setAuthenticationTime(LocalDateTime.now());
        return response;
    }
    
    private String getUserTypeDisplayName(String userType) {
        return switch (userType) {
            case "A" -> "Admin";
            case "U" -> "User";
            default -> "Unknown";
        };
    }
}