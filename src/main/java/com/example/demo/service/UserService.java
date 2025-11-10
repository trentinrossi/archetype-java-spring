package com.example.demo.service;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.enums.UserType;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for User entity.
 * Implements business logic for user operations and permission management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final AccountService accountService;
    
    /**
     * Create a new user
     * BR001: User type determines permissions (ADMIN can view all, REGULAR restricted to account)
     * @param request the create user request
     * @return the created user response
     * @throws IllegalArgumentException if validation fails
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with username: {}", request.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("Username already exists: {}", request.getUsername());
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        // Validate account ID for regular users
        if (request.getUserType() == UserType.REGULAR) {
            if (request.getAccountId() == null || request.getAccountId().isBlank()) {
                log.error("Account ID is required for regular users");
                throw new IllegalArgumentException("Account ID is required for regular users");
            }
            
            // Validate account exists
            if (!accountService.accountExists(request.getAccountId())) {
                log.error("Account not found: {}", request.getAccountId());
                throw new IllegalArgumentException("Account not found: " + request.getAccountId());
            }
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setUserType(request.getUserType());
        user.setAccountId(request.getAccountId());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return convertToResponse(savedUser);
    }
    
    /**
     * Get user by ID
     * @param id the user identifier
     * @return Optional containing the user response if found
     */
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    /**
     * Get user by username
     * @param username the username
     * @return Optional containing the user response if found
     */
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::convertToResponse);
    }
    
    /**
     * Get all users with pagination
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination");
        return userRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get users by type
     * @param userType the user type
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByType(UserType userType, Pageable pageable) {
        log.debug("Fetching users with type: {}", userType);
        return userRepository.findByUserType(userType, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Update an existing user
     * @param id the user identifier
     * @param request the update user request
     * @return the updated user response
     * @throws IllegalArgumentException if user not found or validation fails
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new IllegalArgumentException("User not found with ID: " + id);
                });
        
        // Update user type if provided
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
            
            // Validate account ID for regular users
            if (request.getUserType() == UserType.REGULAR) {
                if (request.getAccountId() == null && user.getAccountId() == null) {
                    log.error("Account ID is required for regular users");
                    throw new IllegalArgumentException("Account ID is required for regular users");
                }
            }
        }
        
        // Update account ID if provided
        if (request.getAccountId() != null) {
            if (!accountService.accountExists(request.getAccountId())) {
                log.error("Account not found: {}", request.getAccountId());
                throw new IllegalArgumentException("Account not found: " + request.getAccountId());
            }
            user.setAccountId(request.getAccountId());
        }
        
        // Update email if provided
        if (request.getEmail() != null) {
            // Check if email is already used by another user
            userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    log.error("Email already exists: {}", request.getEmail());
                    throw new IllegalArgumentException("Email already exists: " + request.getEmail());
                }
            });
            user.setEmail(request.getEmail());
        }
        
        // Update other fields
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getActive() != null) user.setActive(request.getActive());
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return convertToResponse(updatedUser);
    }
    
    /**
     * Delete a user
     * @param id the user identifier
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.error("User not found with ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }
    
    /**
     * Search users by name or username
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(String searchTerm, Pageable pageable) {
        log.debug("Searching users with term: {}", searchTerm);
        return userRepository.searchUsers(searchTerm, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Convert User entity to UserResponseDto
     * @param user the user entity
     * @return the user response DTO
     */
    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(user.getUserType() != null ? user.getUserType().getDisplayName() : null);
        response.setAccountId(user.getAccountId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setActive(user.getActive());
        response.setIsAdmin(user.isAdmin());
        response.setCanViewAllCards(user.canViewAllCards());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
