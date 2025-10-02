package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    public SignOnResponse signOn(SignOnRequest request) {
        log.info("Attempting sign on for user: {}", request.getUserId());
        
        try {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            
            if (userOpt.isEmpty()) {
                log.warn("User not found: {}", request.getUserId());
                return new SignOnResponse(false, null, "User not found");
            }
            
            User user = userOpt.get();
            
            if (!user.getPassword().equals(request.getPassword())) {
                log.warn("Wrong password for user: {}", request.getUserId());
                return new SignOnResponse(false, null, "Wrong password");
            }
            
            log.info("Successful sign on for user: {} with type: {}", request.getUserId(), user.getUserType());
            return new SignOnResponse(true, user.getUserType(), "Login successful");
            
        } catch (Exception e) {
            log.error("Error during sign on for user: {}", request.getUserId(), e);
            return new SignOnResponse(false, null, "Authentication failed");
        }
    }
    
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        try {
            if (userRepository.existsById(request.getUserId())) {
                log.warn("Duplicate user ID attempted: {}", request.getUserId());
                throw new IllegalArgumentException("User ID already exists");
            }
            
            User user = new User();
            user.setUserId(request.getUserId());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPassword(request.getPassword());
            user.setUserType(request.getUserType() != null ? request.getUserType() : "R");
            
            User savedUser = userRepository.save(user);
            log.info("Successfully created user: {}", savedUser.getUserId());
            
            return convertToResponse(savedUser);
            
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate key error creating user: {}", request.getUserId(), e);
            throw new IllegalArgumentException("User ID already exists");
        } catch (Exception e) {
            log.error("Error creating user: {}", request.getUserId(), e);
            throw new RuntimeException("Failed to create user");
        }
    }
    
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.info("Updating user: {}", userId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.warn("User not found for update: {}", userId);
                        return new IllegalArgumentException("User not found");
                    });
            
            boolean modified = false;
            
            if (request.getFirstName() != null && !request.getFirstName().equals(user.getFirstName())) {
                user.setFirstName(request.getFirstName());
                modified = true;
            }
            
            if (request.getLastName() != null && !request.getLastName().equals(user.getLastName())) {
                user.setLastName(request.getLastName());
                modified = true;
            }
            
            if (request.getPassword() != null && !request.getPassword().equals(user.getPassword())) {
                user.setPassword(request.getPassword());
                modified = true;
            }
            
            if (request.getUserType() != null && !request.getUserType().equals(user.getUserType())) {
                user.setUserType(request.getUserType());
                modified = true;
            }
            
            if (modified) {
                User updatedUser = userRepository.save(user);
                log.info("Successfully updated user: {}", userId);
                return convertToResponse(updatedUser);
            } else {
                log.info("No changes detected for user: {}", userId);
                return convertToResponse(user);
            }
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating user: {}", userId, e);
            throw new RuntimeException("Failed to update user");
        }
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        try {
            if (!userRepository.existsById(userId)) {
                log.warn("User not found for deletion: {}", userId);
                throw new IllegalArgumentException("User not found");
            }
            
            userRepository.deleteById(userId);
            log.info("Successfully deleted user: {}", userId);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting user: {}", userId, e);
            throw new RuntimeException("Failed to delete user");
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        try {
            return userRepository.findById(userId)
                    .map(this::convertToResponse);
        } catch (Exception e) {
            log.error("Error retrieving user: {}", userId, e);
            return Optional.empty();
        }
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        
        try {
            return userRepository.findAll(pageable)
                    .map(this::convertToResponse);
        } catch (Exception e) {
            log.error("Error retrieving all users", e);
            throw new RuntimeException("Failed to retrieve users");
        }
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