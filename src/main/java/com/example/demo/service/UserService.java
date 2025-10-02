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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public UserSignOnResponse signOnUser(UserSignOnRequest request) {
        log.info("Attempting sign on for user: {}", request.getUserId());
        
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            log.warn("Sign on failed: User ID is empty");
            return new UserSignOnResponse(false, "User ID is required", null, null);
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("Sign on failed: Password is empty for user: {}", request.getUserId());
            return new UserSignOnResponse(false, "Password is required", null, null);
        }
        
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            log.warn("Sign on failed: User not found: {}", request.getUserId());
            return new UserSignOnResponse(false, "User not found", null, null);
        }
        
        User user = userOptional.get();
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Sign on failed: Wrong password for user: {}", request.getUserId());
            return new UserSignOnResponse(false, "Invalid password", null, null);
        }
        
        String redirectProgram = determineRedirectProgram(user.getUserType());
        log.info("Sign on successful for user: {} with type: {}", request.getUserId(), user.getUserType());
        
        return new UserSignOnResponse(true, "Sign on successful", user.getUserType(), redirectProgram);
    }
    
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        validateCreateUserRequest(request);
        
        if (userRepository.existsById(request.getUserId())) {
            log.warn("User creation failed: User ID already exists: {}", request.getUserId());
            throw new IllegalArgumentException("User ID already exists");
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request) {
        log.info("Updating user with ID: {}", request.getUserId());
        
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            log.warn("Update failed: User ID is empty");
            throw new IllegalArgumentException("User ID is required");
        }
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.warn("Update failed: User not found: {}", request.getUserId());
                    return new IllegalArgumentException("User not found");
                });
        
        validateAndUpdateUserFields(user, request);
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(UserDeleteRequest request) {
        log.info("Deleting user with ID: {}", request.getUserId());
        
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            log.warn("Delete failed: User ID is empty");
            throw new IllegalArgumentException("User ID is required");
        }
        
        if (!userRepository.existsById(request.getUserId())) {
            log.warn("Delete failed: User not found: {}", request.getUserId());
            throw new IllegalArgumentException("User not found");
        }
        
        userRepository.deleteById(request.getUserId());
        log.info("User deleted successfully: {}", request.getUserId());
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        return userRepository.findById(userId).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    private void validateCreateUserRequest(UserCreateRequest request) {
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type is required");
        }
        if (!isValidUserType(request.getUserType())) {
            throw new IllegalArgumentException("User type must be 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    private void validateAndUpdateUserFields(User user, UserUpdateRequest request) {
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            if (!isValidUserType(request.getUserType())) {
                throw new IllegalArgumentException("User type must be 'A' (Admin) or 'R' (Regular)");
            }
            user.setUserType(request.getUserType());
        }
    }
    
    private boolean isValidUserType(String userType) {
        return "A".equals(userType) || "R".equals(userType);
    }
    
    private boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() <= 8;
    }
    
    private String determineRedirectProgram(String userType) {
        if ("A".equals(userType)) {
            return "ADMINMENU";
        } else if ("R".equals(userType)) {
            return "USERMENU";
        }
        return "MAINMENU";
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUserType(user.getUserType());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}