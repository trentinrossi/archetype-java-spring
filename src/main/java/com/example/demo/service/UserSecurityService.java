package com.example.demo.service;

import com.example.demo.dto.SignonRequest;
import com.example.demo.dto.SignonResponse;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserCreateResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public SignonResponse authenticateUser(SignonRequest request) {
        log.info("Attempting authentication for user: {}", request.getUserId());
        
        try {
            // Convert User ID and Password to uppercase as per COSGN00C business rule
            String upperUserId = request.getUserId().toUpperCase();
            String upperPassword = request.getPassword().toUpperCase();
            
            // Read USRSEC file (database table)
            Optional<User> userOptional = userRepository.findById(upperUserId);
            
            if (userOptional.isEmpty()) {
                log.warn("User not found: {}", upperUserId);
                return SignonResponse.failure("User not found");
            }
            
            User user = userOptional.get();
            
            // Verify password match
            if (!user.getPassword().equals(upperPassword)) {
                log.warn("Invalid password for user: {}", upperUserId);
                return SignonResponse.failure("Invalid password");
            }
            
            // Determine user type and target module
            String userType = user.getUserType();
            String targetModule = user.getTargetModule();
            
            log.info("Authentication successful for user: {} with type: {}", upperUserId, userType);
            return SignonResponse.success(userType, targetModule);
            
        } catch (Exception e) {
            log.error("Authentication error for user: {}", request.getUserId(), e);
            return SignonResponse.failure("Authentication system error");
        }
    }
    
    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        log.info("Attempting to create new user: {}", request.getUserId());
        
        try {
            // Validate all input fields as per COUSR01C business rule
            if (isBlankOrEmpty(request.getFirstName())) {
                return UserCreateResponse.failure("First name is required");
            }
            
            if (isBlankOrEmpty(request.getLastName())) {
                return UserCreateResponse.failure("Last name is required");
            }
            
            if (isBlankOrEmpty(request.getUserId())) {
                return UserCreateResponse.failure("User ID is required");
            }
            
            if (isBlankOrEmpty(request.getPassword())) {
                return UserCreateResponse.failure("Password is required");
            }
            
            if (isBlankOrEmpty(request.getUserType())) {
                return UserCreateResponse.failure("User type is required");
            }
            
            // Validate user type
            String userType = request.getUserType().toUpperCase();
            if (!"A".equals(userType) && !"R".equals(userType)) {
                return UserCreateResponse.failure("Invalid user type. Must be A (Admin) or R (Regular)");
            }
            
            // Convert to uppercase as per business rules
            String upperUserId = request.getUserId().toUpperCase();
            String upperPassword = request.getPassword().toUpperCase();
            
            // Check for duplicate User ID
            if (userRepository.existsById(upperUserId)) {
                log.warn("Duplicate user ID attempted: {}", upperUserId);
                return UserCreateResponse.failure("User ID already exists");
            }
            
            // Create new user entity
            User newUser = new User(
                upperUserId,
                request.getFirstName(),
                request.getLastName(),
                upperPassword,
                userType
            );
            
            // Write to USRSEC file (database table)
            User savedUser = userRepository.save(newUser);
            
            log.info("User created successfully: {}", savedUser.getUserId());
            return UserCreateResponse.success(savedUser.getUserId());
            
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate key error for user: {}", request.getUserId(), e);
            return UserCreateResponse.failure("User ID already exists");
        } catch (Exception e) {
            log.error("Error creating user: {}", request.getUserId(), e);
            return UserCreateResponse.failure("System error occurred while creating user");
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        return userRepository.findById(upperUserId).map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UserCreateRequest request) {
        log.info("Updating user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        User user = userRepository.findById(upperUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Update fields with validation
        if (!isBlankOrEmpty(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        
        if (!isBlankOrEmpty(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        
        if (!isBlankOrEmpty(request.getPassword())) {
            user.setPassword(request.getPassword().toUpperCase());
        }
        
        if (!isBlankOrEmpty(request.getUserType())) {
            String userType = request.getUserType().toUpperCase();
            if ("A".equals(userType) || "R".equals(userType)) {
                user.setUserType(userType);
            } else {
                throw new IllegalArgumentException("Invalid user type. Must be A (Admin) or R (Regular)");
            }
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        if (!userRepository.existsById(upperUserId)) {
            throw new IllegalArgumentException("User not found");
        }
        
        userRepository.deleteById(upperUserId);
        log.info("User deleted successfully: {}", upperUserId);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String userId) {
        String upperUserId = userId.toUpperCase();
        return userRepository.existsById(upperUserId);
    }
    
    @Transactional(readOnly = true)
    public long countUsersByType(String userType) {
        return userRepository.countByUserType(userType.toUpperCase());
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(response.getUserTypeDisplayName());
        response.setTargetModule(user.getTargetModule());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private boolean isBlankOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}