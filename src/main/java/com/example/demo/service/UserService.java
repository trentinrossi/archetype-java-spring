package com.example.demo.service;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
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

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with user_id: {}", request.getUserId());

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User ID already exists");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserType(request.getUserType());
        user.setAuthenticated(request.getAuthenticated() != null ? request.getAuthenticated() : false);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);
        log.info("Successfully created user with user_id: {}", savedUser.getUserId());
        return convertToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(String userId) {
        log.debug("Fetching user with user_id: {}", userId);
        return userRepository.findByUserId(userId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination");
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public UserResponseDto updateUser(String userId, UpdateUserRequestDto request) {
        log.info("Updating user with user_id: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        if (request.getUserType() != null) user.setUserType(request.getUserType());
        if (request.getAuthenticated() != null) user.setAuthenticated(request.getAuthenticated());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with user_id: {}", updatedUser.getUserId());
        return convertToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with user_id: {}", userId);

        if (!userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User ID NOT found...");
        }

        try {
            userRepository.deleteByUserId(userId);
            log.info("Successfully deleted user with user_id: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete user with user_id: {}", userId, e);
            throw new IllegalStateException("Unable to delete user");
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDto authenticateUser(String userId, String password) {
        log.info("Authenticating user with user_id: {}", userId);

        User user = userRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

        if (!user.verifyPassword(password)) {
            throw new IllegalArgumentException("Password does not match");
        }

        user.authenticate();
        userRepository.save(user);

        log.info("User authenticated successfully: {}", userId);
        return convertToResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsersByName(String searchTerm, Pageable pageable) {
        log.debug("Searching users by name: {}", searchTerm);
        return userRepository.findByNameContaining(searchTerm, pageable).map(this::convertToResponse);
    }

    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setUserId(user.getUserId());
        response.setUserType(user.getUserType());
        response.setAuthenticated(user.getAuthenticated());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setIsAdmin(user.isAdmin());
        response.setIsRegularUser(user.isRegularUser());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
