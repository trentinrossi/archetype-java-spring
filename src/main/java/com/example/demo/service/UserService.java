package com.example.demo.service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserSearchRequest;
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
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Retrieving paginated list of users with page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(), 
                DEFAULT_PAGE_SIZE, 
                Sort.by("id").ascending()
        );
        
        Page<User> users = userRepository.findAll(pageRequest);
        log.info("Found {} users on page {}", users.getNumberOfElements(), pageable.getPageNumber());
        
        return users.map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersFromId(String startingId, Pageable pageable) {
        log.info("Retrieving users starting from ID: {} with page: {}, size: {}", 
                startingId, pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(), 
                DEFAULT_PAGE_SIZE, 
                Sort.by("id").ascending()
        );
        
        Page<User> users = userRepository.findUsersStartingFromId(startingId, pageRequest);
        log.info("Found {} users starting from ID: {}", users.getNumberOfElements(), startingId);
        
        return users.map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(UserSearchRequest searchRequest, Pageable pageable) {
        log.info("Searching users with criteria: {}", searchRequest);
        
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(), 
                DEFAULT_PAGE_SIZE, 
                Sort.by("id").ascending()
        );
        
        Page<User> users;
        if (searchRequest.getId() != null && !searchRequest.getId().trim().isEmpty()) {
            users = userRepository.findByNameOrIdContaining(searchRequest.getId().trim(), pageRequest);
        } else {
            users = userRepository.findAll(pageRequest);
        }
        
        log.info("Search returned {} users", users.getNumberOfElements());
        return users.map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String id) {
        log.info("Retrieving user by ID: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            log.warn("User ID is null or empty");
            return Optional.empty();
        }
        
        Optional<User> user = userRepository.findById(id.trim());
        if (user.isPresent()) {
            log.info("User found with ID: {}", id);
            return user.map(this::convertToResponse);
        } else {
            log.warn("User not found with ID: {}", id);
            return Optional.empty();
        }
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with ID: {}", request.getId());
        
        validateCreateUserRequest(request);
        
        if (userRepository.existsById(request.getId())) {
            log.error("User with ID {} already exists", request.getId());
            throw new IllegalArgumentException("User ID already exists: " + request.getId());
        }
        
        User user = new User();
        user.setId(request.getId().trim());
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPassword(request.getPassword().trim());
        user.setUserType(request.getUserType().trim());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return convertToResponse(savedUser);
    }
    
    @Transactional
    public UserResponse updateUser(String id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            log.error("User ID is null or empty for update");
            throw new IllegalArgumentException("User ID is required for update");
        }
        
        User user = userRepository.findById(id.trim())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new IllegalArgumentException("User not found with ID: " + id);
                });
        
        validateUpdateUserRequest(request);
        
        boolean hasChanges = false;
        
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            if (!request.getFirstName().trim().equals(user.getFirstName())) {
                user.setFirstName(request.getFirstName().trim());
                hasChanges = true;
            }
        }
        
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            if (!request.getLastName().trim().equals(user.getLastName())) {
                user.setLastName(request.getLastName().trim());
                hasChanges = true;
            }
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (!request.getPassword().trim().equals(user.getPassword())) {
                user.setPassword(request.getPassword().trim());
                hasChanges = true;
            }
        }
        
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            if (!request.getUserType().trim().equals(user.getUserType())) {
                user.setUserType(request.getUserType().trim());
                hasChanges = true;
            }
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user with ID: {}", id);
            return convertToResponse(user);
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String id) {
        log.info("Deleting user with ID: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            log.error("User ID is null or empty for deletion");
            throw new IllegalArgumentException("User ID is required for deletion");
        }
        
        if (!userRepository.existsById(id.trim())) {
            log.error("User not found with ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id.trim());
        log.info("User deleted successfully with ID: {}", id);
    }
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is mandatory and cannot be empty");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is mandatory and cannot be empty");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is mandatory and cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory and cannot be empty");
        }
        if (request.getUserType() == null || request.getUserType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type is mandatory and cannot be empty");
        }
    }
    
    private void validateUpdateUserRequest(UpdateUserRequest request) {
        boolean hasValidField = false;
        
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            hasValidField = true;
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            hasValidField = true;
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            hasValidField = true;
        }
        if (request.getUserType() != null && !request.getUserType().trim().isEmpty()) {
            hasValidField = true;
        }
        
        if (!hasValidField) {
            throw new IllegalArgumentException("At least one field is mandatory for update and cannot be empty");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        response.setAdmin(user.isAdmin());
        response.setRegularUser(user.isRegularUser());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
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