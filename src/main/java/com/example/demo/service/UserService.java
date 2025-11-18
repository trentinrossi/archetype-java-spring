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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User with ID already exists");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User with username already exists");
        }
        
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email already exists");
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserType(request.getUserType());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long id) {
        log.info("Retrieving user with ID: {}", id);
        return userRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUserId(String userId) {
        log.info("Retrieving user with user ID: {}", userId);
        return userRepository.findByUserId(userId)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUsername(String username) {
        log.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.info("Retrieving all users with pagination");
        return userRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByType(UserType userType, Pageable pageable) {
        log.info("Retrieving users by type: {}", userType);
        return userRepository.findByUserType(userType, pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto request) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }
        if (request.getUsername() != null) {
            if (userRepository.existsByUsername(request.getUsername()) && 
                !request.getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("User with username already exists");
            }
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail()) && 
                !request.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("User with email already exists");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(String searchTerm, Pageable pageable) {
        log.info("Searching users with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term is required");
        }
        
        return userRepository.searchUsers(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    /**
     * BR001: User Authorization for Card Viewing
     * Check if user can view a specific account's cards
     */
    @Transactional(readOnly = true)
    public boolean canUserViewAccount(Long userId, Long accountId) {
        log.debug("Checking if user {} can view account {}", userId, accountId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return user.canViewAccount(accountId);
    }

    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUserId(user.getUserId());
        response.setUserType(user.getUserType());
        response.setUserTypeDisplayName(user.getUserType().getDisplayName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setIsAdmin(user.isAdmin());
        response.setCanViewAllCards(user.canViewAllCards());
        response.setAccountCount(user.getAccountCount());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
