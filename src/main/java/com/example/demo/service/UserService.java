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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public UserListResponse getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new UserListResponse(
                userResponses,
                userPage.getNumber(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getSize(),
                userPage.isFirst(),
                userPage.isLast(),
                userPage.hasNext(),
                userPage.hasPrevious()
        );
    }
    
    @Transactional(readOnly = true)
    public UserListResponse searchUsers(String searchTerm, Pageable pageable) {
        log.info("Searching users with term: '{}', page={}, size={}", searchTerm, pageable.getPageNumber(), pageable.getPageSize());
        
        if (!StringUtils.hasText(searchTerm)) {
            return getAllUsers(pageable);
        }
        
        Page<User> userPage = userRepository.findByFirstNameOrLastNameContainingIgnoreCase(searchTerm.trim(), pageable);
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new UserListResponse(
                userResponses,
                userPage.getNumber(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getSize(),
                userPage.isFirst(),
                userPage.isLast(),
                userPage.hasNext(),
                userPage.hasPrevious()
        );
    }
    
    @Transactional(readOnly = true)
    public UserListResponse getUsersByType(String userType, Pageable pageable) {
        log.info("Retrieving users by type: '{}', page={}, size={}", userType, pageable.getPageNumber(), pageable.getPageSize());
        
        if (!isValidUserType(userType)) {
            throw new IllegalArgumentException("Invalid user type. Must be 'A' (Admin) or 'R' (Regular)");
        }
        
        Page<User> userPage = userRepository.findByUserType(userType, pageable);
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new UserListResponse(
                userResponses,
                userPage.getNumber(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getSize(),
                userPage.isFirst(),
                userPage.isLast(),
                userPage.hasNext(),
                userPage.hasPrevious()
        );
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with userId: {}", request.getUserId());
        
        validateCreateUserRequest(request);
        
        if (userRepository.existsById(request.getUserId())) {
            log.warn("Attempt to create user with existing userId: {}", request.getUserId());
            throw new IllegalArgumentException("User with userId '" + request.getUserId() + "' already exists");
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with userId: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by userId: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        return userRepository.findById(userId).map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user with userId: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        validateUpdateUserRequest(request);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId '" + userId + "' not found"));
        
        boolean hasChanges = false;
        
        if (StringUtils.hasText(request.getFirstName()) && !request.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(request.getFirstName());
            hasChanges = true;
        }
        
        if (StringUtils.hasText(request.getLastName()) && !request.getLastName().equals(user.getLastName())) {
            user.setLastName(request.getLastName());
            hasChanges = true;
        }
        
        if (StringUtils.hasText(request.getPassword()) && !request.getPassword().equals(user.getPassword())) {
            user.setPassword(request.getPassword());
            hasChanges = true;
        }
        
        if (StringUtils.hasText(request.getUserType()) && !request.getUserType().equals(user.getUserType())) {
            user.setUserType(request.getUserType());
            hasChanges = true;
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user with userId: {}", userId);
            return convertToResponse(user);
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with userId: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with userId: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with userId '" + userId + "' not found");
        }
        
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with userId: {}", userId);
    }
    
    @Transactional(readOnly = true)
    public long countUsersByType(String userType) {
        log.info("Counting users by type: {}", userType);
        
        if (!isValidUserType(userType)) {
            throw new IllegalArgumentException("Invalid user type. Must be 'A' (Admin) or 'R' (Regular)");
        }
        
        return userRepository.countByUserType(userType);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String userId) {
        if (!StringUtils.hasText(userId)) {
            return false;
        }
        return userRepository.existsById(userId);
    }
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create user request cannot be null");
        }
        
        if (!StringUtils.hasText(request.getUserId())) {
            throw new IllegalArgumentException("User ID is required");
        }
        
        if (!StringUtils.hasText(request.getFirstName())) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (!StringUtils.hasText(request.getLastName())) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (!isValidUserType(request.getUserType())) {
            throw new IllegalArgumentException("Invalid user type. Must be 'A' (Admin) or 'R' (Regular)");
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
        
        if (StringUtils.hasText(request.getUserType()) && !isValidUserType(request.getUserType())) {
            throw new IllegalArgumentException("Invalid user type. Must be 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    private boolean isValidUserType(String userType) {
        return StringUtils.hasText(userType) && ("A".equals(userType) || "R".equals(userType));
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private String getUserTypeDisplayName(String userType) {
        return switch (userType) {
            case "A" -> "Admin";
            case "R" -> "Regular";
            default -> "Unknown";
        };
    }
}