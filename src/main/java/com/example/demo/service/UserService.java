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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        // Convert user ID and password to uppercase
        String upperUserId = request.getUserId().toUpperCase();
        String upperPassword = request.getPassword().toUpperCase();
        
        // Check if user ID already exists
        if (userRepository.existsById(upperUserId)) {
            log.error("User with ID {} already exists", upperUserId);
            throw new IllegalArgumentException("User with ID " + upperUserId + " already exists");
        }
        
        // Validate all required fields
        validateRequiredFields(request.getFirstName(), request.getLastName(), 
                             upperPassword, request.getUserType());
        
        User user = new User();
        user.setUserId(upperUserId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(upperPassword);
        user.setUserType(request.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("User {} created successfully", savedUser.getUserId());
        
        return convertToResponseDTO(savedUser);
    }
    
    @Transactional
    public UserResponseDTO updateUser(String userId, UserUpdateDTO request) {
        log.info("Updating user with ID: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        User existingUser = userRepository.findById(upperUserId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", upperUserId);
                    return new IllegalArgumentException("User with ID " + upperUserId + " not found");
                });
        
        boolean hasChanges = false;
        
        // Only update modified fields
        if (request.getFirstName() != null && !request.getFirstName().equals(existingUser.getFirstName())) {
            existingUser.setFirstName(request.getFirstName());
            hasChanges = true;
        }
        
        if (request.getLastName() != null && !request.getLastName().equals(existingUser.getLastName())) {
            existingUser.setLastName(request.getLastName());
            hasChanges = true;
        }
        
        if (request.getPassword() != null) {
            String upperPassword = request.getPassword().toUpperCase();
            if (!upperPassword.equals(existingUser.getPassword())) {
                existingUser.setPassword(upperPassword);
                hasChanges = true;
            }
        }
        
        if (request.getUserType() != null && !request.getUserType().equals(existingUser.getUserType())) {
            existingUser.setUserType(request.getUserType());
            hasChanges = true;
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user {}", upperUserId);
            return convertToResponseDTO(existingUser);
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User {} updated successfully", updatedUser.getUserId());
        
        return convertToResponseDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        
        // Check if user exists
        User user = userRepository.findById(upperUserId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", upperUserId);
                    return new IllegalArgumentException("User with ID " + upperUserId + " not found");
                });
        
        // Prevent deletion of last admin user
        if (user.isAdmin()) {
            long adminCount = userRepository.countByUserType("A");
            if (adminCount <= 1) {
                log.error("Cannot delete the last admin user: {}", upperUserId);
                throw new IllegalStateException("Cannot delete the last admin user");
            }
        }
        
        userRepository.deleteById(upperUserId);
        log.info("User {} deleted successfully", upperUserId);
    }
    
    @Transactional(readOnly = true)
    public UserListDTO getAllUsers(Pageable pageable) {
        log.info("Retrieving users with pagination: page {}, size {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<User> userPage = userRepository.findAll(pageable);
        
        List<UserResponseDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        return new UserListDTO(
                userDTOs,
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
    public UserListDTO getUsersByType(String userType, Pageable pageable) {
        log.info("Retrieving users by type {} with pagination: page {}, size {}", 
                userType, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<User> userPage = userRepository.findByUserType(userType, pageable);
        
        List<UserResponseDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        return new UserListDTO(
                userDTOs,
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
    public UserAuthResponseDTO authenticateUser(UserAuthDTO authRequest) {
        log.info("Authenticating user with ID: {}", authRequest.getUserId());
        
        String upperUserId = authRequest.getUserId().toUpperCase();
        String upperPassword = authRequest.getPassword().toUpperCase();
        
        Optional<User> userOpt = userRepository.findById(upperUserId);
        
        if (userOpt.isEmpty()) {
            log.warn("Authentication failed: User {} not found", upperUserId);
            return createFailedAuthResponse(upperUserId);
        }
        
        User user = userOpt.get();
        
        if (!upperPassword.equals(user.getPassword())) {
            log.warn("Authentication failed: Invalid password for user {}", upperUserId);
            return createFailedAuthResponse(upperUserId);
        }
        
        log.info("User {} authenticated successfully", upperUserId);
        
        return new UserAuthResponseDTO(
                user.getUserId(),
                user.getFullName(),
                user.getUserType(),
                getUserTypeDisplayName(user.getUserType()),
                user.isAdmin(),
                true,
                LocalDateTime.now()
        );
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        
        String upperUserId = userId.toUpperCase();
        return userRepository.findById(upperUserId)
                .map(this::convertToResponseDTO);
    }
    
    @Transactional(readOnly = true)
    public UserListDTO searchUsersByName(String searchTerm, Pageable pageable) {
        log.info("Searching users by name pattern with pagination: {}", searchTerm);
        
        Page<User> userPage = userRepository.findByNameContaining(searchTerm, pageable);
        
        List<UserResponseDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        
        return new UserListDTO(
                userDTOs,
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
    
    private void validateRequiredFields(String firstName, String lastName, String password, String userType) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User type is required");
        }
        if (!userType.matches("^[AR]$")) {
            throw new IllegalArgumentException("User type must be either 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getUserType(),
                getUserTypeDisplayName(user.getUserType()),
                user.isAdmin(),
                user.isRegular(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    
    private String getUserTypeDisplayName(String userType) {
        return switch (userType) {
            case "A" -> "Administrator";
            case "R" -> "Regular User";
            default -> "Unknown";
        };
    }
    
    private UserAuthResponseDTO createFailedAuthResponse(String userId) {
        return new UserAuthResponseDTO(
                userId,
                null,
                null,
                null,
                false,
                false,
                LocalDateTime.now()
        );
    }
}