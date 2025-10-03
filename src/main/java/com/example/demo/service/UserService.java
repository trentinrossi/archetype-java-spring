package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public UserSignOnResponse signOnUser(UserSignOnRequest request) {
        log.info("Attempting sign on for user: {}", request.getUserId());
        
        Optional<User> userOpt = userRepository.findByUserIdIgnoreCase(request.getUserId());
        
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", request.getUserId());
            return new UserSignOnResponse(false, "User not found", null, null);
        }
        
        User user = userOpt.get();
        
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Invalid password for user: {}", request.getUserId());
            return new UserSignOnResponse(false, "Invalid password", null, null);
        }
        
        String redirectProgram = user.isAdmin() ? "ADMIN_MENU" : "USER_MENU";
        
        log.info("Sign on successful for user: {} with type: {}", request.getUserId(), user.getUserType());
        return new UserSignOnResponse(true, "Sign on successful", user.getUserType(), redirectProgram);
    }
    
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        if (userRepository.existsByUserId(request.getUserId().toUpperCase())) {
            log.warn("User with ID already exists: {}", request.getUserId());
            throw new IllegalArgumentException("User with ID already exists: " + request.getUserId());
        }
        
        validateUserType(request.getUserType());
        validatePassword(request.getPassword());
        
        User user = new User();
        user.setUserId(request.getUserId().toUpperCase());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType().toUpperCase());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        Optional<User> userOpt = userRepository.findByUserIdIgnoreCase(userId);
        
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", userId);
            return Optional.empty();
        }
        
        return userOpt.map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.info("Updating user: {}", userId);
        
        User user = userRepository.findByUserIdIgnoreCase(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for update: {}", userId);
                    return new IllegalArgumentException("User not found: " + userId);
                });
        
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            validatePassword(request.getPassword());
            user.setPassword(request.getPassword());
        }
        
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            validateUserType(request.getUserType());
            user.setUserType(request.getUserType().toUpperCase());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        User user = userRepository.findByUserIdIgnoreCase(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for deletion: {}", userId);
                    return new IllegalArgumentException("User not found: " + userId);
                });
        
        userRepository.delete(user);
        log.info("User deleted successfully: {}", userId);
    }
    
    @Transactional(readOnly = true)
    public UserListResponse getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination - page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<User> userPage = userRepository.findAllByOrderByUserId(pageable);
        
        UserListResponse response = new UserListResponse();
        response.setUsers(userPage.getContent().stream()
                .map(this::convertToResponse)
                .toList());
        response.setPage(userPage.getNumber());
        response.setSize(userPage.getSize());
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setFirst(userPage.isFirst());
        response.setLast(userPage.isLast());
        
        return response;
    }
    
    private void validateUserType(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User type is required");
        }
        
        String normalizedType = userType.toUpperCase();
        if (!"A".equals(normalizedType) && !"R".equals(normalizedType)) {
            throw new IllegalArgumentException("User type must be 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }
        
        if (password.length() > 8) {
            throw new IllegalArgumentException("Password must not exceed 8 characters");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUserType(user.getUserType());
        response.setFullName(user.getFullName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}