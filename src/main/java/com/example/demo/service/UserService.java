package com.example.demo.service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserListResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    @Transactional(readOnly = true)
    public Page<UserListResponse> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(), 
                DEFAULT_PAGE_SIZE
        );
        
        Page<User> users = userRepository.findAll(pageRequest);
        return users.map(this::convertToListResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("User ID is null or empty");
            return Optional.empty();
        }
        
        return userRepository.findById(userId).map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        validateCreateUserRequest(request);
        
        if (userRepository.existsById(request.getUserId())) {
            log.error("User with ID {} already exists", request.getUserId());
            throw new IllegalArgumentException("User with ID " + request.getUserId() + " already exists");
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID is null or empty for update operation");
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new IllegalArgumentException("User not found with ID: " + userId);
                });
        
        validateUpdateUserRequest(request);
        
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }
        
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword().trim());
        }
        
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            user.setUserType(request.getUserType().trim());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID is null or empty for delete operation");
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create user request cannot be null");
        }
        
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
        
        if (request.getUserId().length() > 8) {
            throw new IllegalArgumentException("User ID must not exceed 8 characters");
        }
        
        if (request.getFirstName().length() > 20) {
            throw new IllegalArgumentException("First name must not exceed 20 characters");
        }
        
        if (request.getLastName().length() > 20) {
            throw new IllegalArgumentException("Last name must not exceed 20 characters");
        }
        
        if (request.getPassword().length() > 8) {
            throw new IllegalArgumentException("Password must not exceed 8 characters");
        }
        
        if (request.getUserType().length() != 1) {
            throw new IllegalArgumentException("User type must be exactly 1 character");
        }
    }
    
    private void validateUpdateUserRequest(UpdateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update user request cannot be null");
        }
        
        if (request.getFirstName() != null && request.getFirstName().length() > 20) {
            throw new IllegalArgumentException("First name must not exceed 20 characters");
        }
        
        if (request.getLastName() != null && request.getLastName().length() > 20) {
            throw new IllegalArgumentException("Last name must not exceed 20 characters");
        }
        
        if (request.getPassword() != null && request.getPassword().length() > 8) {
            throw new IllegalArgumentException("Password must not exceed 8 characters");
        }
        
        if (request.getUserType() != null && request.getUserType().length() != 1) {
            throw new IllegalArgumentException("User type must be exactly 1 character");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private UserListResponse convertToListResponse(User user) {
        UserListResponse response = new UserListResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}