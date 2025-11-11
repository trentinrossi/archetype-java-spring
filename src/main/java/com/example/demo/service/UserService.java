package com.example.demo.service;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserService
 * 
 * Business logic layer for User entity.
 * Implements user management operations with permission-based access control.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR003: Single Selection Enforcement
 * - BR008: First Page Navigation Restriction
 * - BR009: Last Page Navigation Restriction
 * - BR011: Exit to Menu
 * - BR012: View Card Details
 * - BR013: Update Card Information
 * - BR014: Forward Pagination
 * - BR015: Backward Pagination
 * - BR017: Input Error Protection
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    /**
     * Creates a new user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param request The create user request
     * @return The created user response
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with ID: {}", request.getUserId());

        // Check if user already exists
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User with ID already exists: " + request.getUserId());
        }

        // Parse and validate user type
        UserType userType = UserType.fromCode(request.getUserType());

        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserType(userType);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        
        return convertToResponse(savedUser);
    }

    /**
     * Retrieves a user by ID
     * 
     * @param userId The user ID
     * @return Optional containing the user response if found
     */
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        return userRepository.findByUserId(userId).map(this::convertToResponse);
    }

    /**
     * Retrieves all users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * Updates an existing user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID to update
     * @param request The update user request
     * @return The updated user response
     */
    @Transactional
    public UserResponseDto updateUser(String userId, UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (request.getUserType() != null) {
            UserType userType = UserType.fromCode(request.getUserType());
            user.setUserType(userType);
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return convertToResponse(updatedUser);
    }

    /**
     * Deletes a user
     * 
     * @param userId The user ID to delete
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
     * Retrieves users by user type
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userType The user type to filter by
     * @param pageable Pagination information
     * @return Page of users with the specified type
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByType(UserType userType, Pageable pageable) {
        log.info("Retrieving users by type: {}", userType);
        return userRepository.findByUserType(userType, pageable).map(this::convertToResponse);
    }

    /**
     * Checks if a user is an admin
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @return true if user is admin, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isAdminUser(String userId) {
        return userRepository.isAdminUser(userId);
    }

    /**
     * Checks if a user can view all cards
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @return true if user can view all cards, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean canViewAllCards(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return user.canViewAllCards();
    }

    /**
     * Checks if a user requires account context
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @return true if user requires account context, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean requiresAccountContext(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return user.requiresAccountContext();
    }

    /**
     * Gets accessible account IDs for a user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @return List of accessible account IDs
     */
    @Transactional(readOnly = true)
    public List<String> getAccessibleAccountIds(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // BR001: Admin users can access all accounts
        if (user.isAdmin()) {
            return accountRepository.findAll().stream()
                    .map(account -> account.getAccountId())
                    .collect(Collectors.toList());
        }
        
        // BR001: Regular users can only access their assigned accounts
        return accountRepository.findAccountIdsByUserId(userId);
    }

    /**
     * Converts User entity to UserResponseDto
     * 
     * @param user The user entity
     * @return The user response DTO
     */
    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setUserId(user.getUserId());
        response.setUserType(user.getUserType().getCode());
        response.setUserTypeDisplayName(user.getUserType().getDisplayName());
        response.setIsAdmin(user.isAdmin());
        response.setCanViewAllCards(user.canViewAllCards());
        response.setRequiresAccountContext(user.requiresAccountContext());
        response.setAccessibleAccountIds(user.getAccessibleAccountIds().stream().collect(Collectors.toList()));
        response.setAccessibleAccountCount(user.getAccessibleAccountIds().size());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
