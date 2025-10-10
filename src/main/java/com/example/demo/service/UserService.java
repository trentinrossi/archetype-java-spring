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
    public Page<UserResponse> getAllUsers(String userIdPrefix, Pageable pageable) {
        log.info("Retrieving users with prefix '{}' and pagination: page={}, size={}", 
                userIdPrefix, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<User> users;
        if (userIdPrefix != null && !userIdPrefix.trim().isEmpty()) {
            users = userRepository.findByUserIdStartingWithIgnoreCase(userIdPrefix.trim(), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        log.info("Found {} users", users.getTotalElements());
        return users.map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(UserSearchDTO searchDTO, Pageable pageable) {
        log.info("Searching users with criteria: {}", searchDTO);
        
        Page<User> users = userRepository.findUsersWithFilters(
            searchDTO.getUserId(),
            searchDTO.getFirstName(),
            searchDTO.getLastName(),
            searchDTO.getUserType(),
            pageable
        );
        
        log.info("Search returned {} users", users.getTotalElements());
        return users.map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        
        Optional<User> user = userRepository.findById(userId.trim());
        
        if (user.isEmpty()) {
            log.warn("User not found with ID: {}", userId);
            throw new UserNotFoundException("User ID NOT found...");
        }
        
        return user.map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        validateCreateUserRequest(request);
        
        String normalizedUserId = request.getUserId().trim();
        
        if (userRepository.existsById(normalizedUserId)) {
            log.error("Duplicate user ID attempted: {}", normalizedUserId);
            throw new DuplicateUserIdException("User ID already exist...");
        }
        
        User user = new User();
        user.setUserId(normalizedUserId);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPassword(request.getPassword().trim());
        user.setUserType(request.getUserType().trim());
        
        User savedUser = userRepository.save(user);
        log.info("User {} has been added...", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        
        validateUpdateUserRequest(request);
        
        User existingUser = userRepository.findById(userId.trim())
                .orElseThrow(() -> new UserNotFoundException("User ID NOT found..."));
        
        // Check if any changes were made
        boolean hasChanges = !existingUser.getFirstName().equals(request.getFirstName().trim()) ||
                           !existingUser.getLastName().equals(request.getLastName().trim()) ||
                           !existingUser.getPassword().equals(request.getPassword().trim()) ||
                           !existingUser.getUserType().equals(request.getUserType().trim());
        
        if (!hasChanges) {
            throw new IllegalArgumentException("Please modify to update...");
        }
        
        // Update all fields (User ID cannot be changed)
        existingUser.setFirstName(request.getFirstName().trim());
        existingUser.setLastName(request.getLastName().trim());
        existingUser.setPassword(request.getPassword().trim());
        existingUser.setUserType(request.getUserType().trim());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User {} has been updated...", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        
        String normalizedUserId = userId.trim();
        
        if (!userRepository.existsById(normalizedUserId)) {
            log.error("Attempted to delete non-existent user: {}", normalizedUserId);
            throw new UserNotFoundException("User ID NOT found...");
        }
        
        userRepository.deleteById(normalizedUserId);
        log.info("User {} has been deleted...", normalizedUserId);
    }
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can NOT be empty...");
        }
        if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
    }
    
    private void validateUpdateUserRequest(UpdateUserRequest request) {
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can NOT be empty...");
        }
        if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setPassword(user.getPassword());
        response.setUserType(user.getUserType());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    // Custom Exception Classes
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class DuplicateUserIdException extends RuntimeException {
        public DuplicateUserIdException(String message) {
            super(message);
        }
    }
}