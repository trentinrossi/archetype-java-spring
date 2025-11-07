package com.example.demo.service;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.enums.UserType;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for User entity operations
 * Business Rule BR001: Admin users can view all credit cards when no context is passed.
 * Non-admin users can only view cards associated with their specific account.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    
    /**
     * Create a new user
     * Business Rule BR001: Distinguish between admin and regular users
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with ID: {} and type: {}", request.getUserId(), request.getUserType());
        
        // Validate user ID length
        if (request.getUserId().length() > 20) {
            throw new IllegalArgumentException("User ID must not exceed 20 characters");
        }
        
        // Check if user already exists
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User with ID " + request.getUserId() + " already exists");
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserType(request.getUserType());
        
        // Set up account access
        if (request.getAccountIds() != null && !request.getAccountIds().isEmpty()) {
            Set<Account> accounts = new HashSet<>();
            for (String accountId : request.getAccountIds()) {
                Account account = accountRepository.findByAccountId(accountId)
                        .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
                accounts.add(account);
            }
            user.setAccounts(accounts);
        }
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {} (Admin: {})", savedUser.getUserId(), savedUser.isAdmin());
        
        return convertToResponse(savedUser);
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(String userId) {
        log.debug("Fetching user with ID: {}", userId);
        return userRepository.findByUserIdWithAccounts(userId)
                .map(this::convertToResponse);
    }
    
    /**
     * Update an existing user
     * Business Rule BR001: User type and account access can be modified
     */
    @Transactional
    public UserResponseDto updateUser(String userId, UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findByUserIdWithAccounts(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Update user type if provided
        if (request.getUserType() != null) {
            log.info("Changing user type from {} to {} for user: {}", 
                    user.getUserType(), request.getUserType(), userId);
            user.setUserType(request.getUserType());
        }
        
        // Update account access if provided
        if (request.getAccountIds() != null) {
            // Clear existing accounts
            user.getAccounts().clear();
            
            // Add new accounts
            for (String accountId : request.getAccountIds()) {
                Account account = accountRepository.findByAccountId(accountId)
                        .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
                user.addAccountAccess(account);
            }
            log.info("Updated account access for user: {} (now has access to {} accounts)", 
                    userId, request.getAccountIds().size());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }
    
    /**
     * Delete a user
     */
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    /**
     * Get all users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination");
        return userRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get users by type
     * Business Rule BR001: Distinguish between admin and regular users
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByType(UserType userType, Pageable pageable) {
        log.debug("Fetching users with type: {}", userType);
        return userRepository.findByUserType(userType, pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Check if user has access to account
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    @Transactional(readOnly = true)
    public boolean hasAccessToAccount(String userId, String accountId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return user.hasAccessToAccount(accountId);
    }
    
    /**
     * Check if user is admin
     * Business Rule BR001: Admin users have elevated permissions
     */
    @Transactional(readOnly = true)
    public boolean isAdmin(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return user.isAdmin();
    }
    
    /**
     * Get users with access to a specific account
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByAccountId(String accountId, Pageable pageable) {
        log.debug("Fetching users with access to account: {}", accountId);
        // This would need custom implementation with pagination
        return userRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Convert User entity to UserResponseDto
     */
    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setUserId(user.getUserId());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(user.getUserTypeDisplayName());
        response.setIsAdmin(user.isAdmin());
        response.setCanViewAllCards(user.canViewAllCards());
        
        if (user.getAccounts() != null) {
            Set<String> accountIds = user.getAccounts().stream()
                    .map(Account::getAccountId)
                    .collect(Collectors.toSet());
            response.setAccountIds(accountIds);
            response.setAccountCount(accountIds.size());
        } else {
            response.setAccountIds(new HashSet<>());
            response.setAccountCount(0);
        }
        
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
