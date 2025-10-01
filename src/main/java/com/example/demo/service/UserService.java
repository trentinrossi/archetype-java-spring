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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Page<UserListResponse> getAllUsers(String startingUserId, Pageable pageable) {
        log.info("Retrieving users with starting ID: {} and pageable: {}", startingUserId, pageable);
        
        // Limit page size to maximum of 10
        int pageSize = Math.min(pageable.getPageSize(), 10);
        Pageable limitedPageable = PageRequest.of(
            pageable.getPageNumber(), 
            pageSize, 
            Sort.by(Sort.Direction.ASC, "secUsrId")
        );
        
        Page<User> users;
        if (startingUserId != null && !startingUserId.trim().isEmpty()) {
            users = userRepository.findUsersFromStartingId(startingUserId.trim(), limitedPageable);
        } else {
            users = userRepository.findAll(limitedPageable);
        }
        
        return users.map(this::convertToListResponse);
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getSecUsrId());
        
        // Validate all mandatory fields
        validateMandatoryFields(request);
        
        // Check for duplicate User ID
        if (userRepository.existsBySecUsrId(request.getSecUsrId())) {
            log.error("User with ID {} already exists", request.getSecUsrId());
            throw new IllegalArgumentException("User ID already exists: " + request.getSecUsrId());
        }
        
        User user = new User();
        user.setSecUsrId(request.getSecUsrId());
        user.setSecUsrFname(request.getSecUsrFname());
        user.setSecUsrLname(request.getSecUsrLname());
        user.setSecUsrPwd(request.getSecUsrPwd());
        user.setSecUsrType(request.getSecUsrType());
        
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getSecUsrId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String secUsrId) {
        log.info("Retrieving user with ID: {}", secUsrId);
        return userRepository.findBySecUsrId(secUsrId).map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(String secUsrId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", secUsrId);
        
        User user = userRepository.findBySecUsrId(secUsrId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", secUsrId);
                    return new IllegalArgumentException("User not found with ID: " + secUsrId);
                });
        
        boolean hasChanges = false;
        
        // Validate and update fields if provided and not empty
        if (request.getSecUsrFname() != null && !request.getSecUsrFname().trim().isEmpty()) {
            if (!request.getSecUsrFname().equals(user.getSecUsrFname())) {
                user.setSecUsrFname(request.getSecUsrFname());
                hasChanges = true;
            }
        }
        
        if (request.getSecUsrLname() != null && !request.getSecUsrLname().trim().isEmpty()) {
            if (!request.getSecUsrLname().equals(user.getSecUsrLname())) {
                user.setSecUsrLname(request.getSecUsrLname());
                hasChanges = true;
            }
        }
        
        if (request.getSecUsrPwd() != null && !request.getSecUsrPwd().trim().isEmpty()) {
            if (!request.getSecUsrPwd().equals(user.getSecUsrPwd())) {
                user.setSecUsrPwd(request.getSecUsrPwd());
                hasChanges = true;
            }
        }
        
        if (request.getSecUsrType() != null && !request.getSecUsrType().trim().isEmpty()) {
            if (!request.getSecUsrType().equals(user.getSecUsrType())) {
                user.setSecUsrType(request.getSecUsrType());
                hasChanges = true;
            }
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user with ID: {}", secUsrId);
            return convertToResponse(user);
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with ID: {}", updatedUser.getSecUsrId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String secUsrId) {
        log.info("Deleting user with ID: {}", secUsrId);
        
        if (!userRepository.existsBySecUsrId(secUsrId)) {
            log.error("User not found with ID: {}", secUsrId);
            throw new IllegalArgumentException("User not found with ID: " + secUsrId);
        }
        
        userRepository.deleteById(secUsrId);
        log.info("Successfully deleted user with ID: {}", secUsrId);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String secUsrId) {
        return userRepository.existsBySecUsrId(secUsrId);
    }
    
    private void validateMandatoryFields(CreateUserRequest request) {
        if (request.getSecUsrId() == null || request.getSecUsrId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is mandatory and cannot be empty");
        }
        if (request.getSecUsrFname() == null || request.getSecUsrFname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is mandatory and cannot be empty");
        }
        if (request.getSecUsrLname() == null || request.getSecUsrLname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is mandatory and cannot be empty");
        }
        if (request.getSecUsrPwd() == null || request.getSecUsrPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory and cannot be empty");
        }
        if (request.getSecUsrType() == null || request.getSecUsrType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type is mandatory and cannot be empty");
        }
        if (!request.getSecUsrType().equals("A") && !request.getSecUsrType().equals("U")) {
            throw new IllegalArgumentException("User type must be 'A' for Admin or 'U' for User");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setSecUsrId(user.getSecUsrId());
        response.setSecUsrFname(user.getSecUsrFname());
        response.setSecUsrLname(user.getSecUsrLname());
        response.setSecUsrType(user.getSecUsrType());
        response.setFullName(user.getFullName());
        response.setUserTypeDisplayName(user.getUserTypeDisplayName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private UserListResponse convertToListResponse(User user) {
        UserListResponse response = new UserListResponse();
        response.setSecUsrId(user.getSecUsrId());
        response.setSecUsrFname(user.getSecUsrFname());
        response.setSecUsrLname(user.getSecUsrLname());
        response.setFullName(user.getFullName());
        response.setSecUsrType(user.getSecUsrType());
        response.setUserTypeDisplayName(user.getUserTypeDisplayName());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}