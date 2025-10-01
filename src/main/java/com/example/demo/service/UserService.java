package com.example.demo.service;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.dto.UserDeleteDto;
import com.example.demo.dto.UserDetailDto;
import com.example.demo.dto.UserListDto;
import com.example.demo.dto.UserUpdateDto;
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
    public Page<UserListDto> getAllUsers(Pageable pageable, String userIdPrefix) {
        log.info("Retrieving users - page: {}, userIdPrefix: {}", pageable, userIdPrefix);
        
        Page<User> users;
        if (userIdPrefix != null && !userIdPrefix.trim().isEmpty()) {
            users = userRepository.findByUserIdStartingWithIgnoreCase(userIdPrefix.trim(), pageable);
        } else {
            users = userRepository.findAllOrderByUserId(pageable);
        }
        
        return users.map(this::convertToListDto);
    }
    
    @Transactional
    public UserDetailDto createUser(UserCreateDto request) {
        log.info("Creating new user with ID: {}", request.getUserId());
        
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("User ID already exists: " + request.getUserId());
        }
        
        User user = new User();
        user.setUserId(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUserType(request.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());
        
        return convertToDetailDto(savedUser);
    }
    
    @Transactional(readOnly = true)
    public UserDetailDto getUserById(String userId) {
        log.info("Retrieving user by ID: {}", userId);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return convertToDetailDto(user);
    }
    
    @Transactional
    public UserDetailDto updateUser(String userId, UserUpdateDto request) {
        log.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        boolean hasChanges = false;
        
        if (!user.getFirstName().equals(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
            hasChanges = true;
        }
        
        if (!user.getLastName().equals(request.getLastName())) {
            user.setLastName(request.getLastName());
            hasChanges = true;
        }
        
        if (!user.getPassword().equals(request.getPassword())) {
            user.setPassword(request.getPassword());
            hasChanges = true;
        }
        
        if (!user.getUserType().equals(request.getUserType())) {
            user.setUserType(request.getUserType());
            hasChanges = true;
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user ID: {}", userId);
            return convertToDetailDto(user);
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getUserId());
        
        return convertToDetailDto(updatedUser);
    }
    
    @Transactional(readOnly = true)
    public UserDeleteDto getUserForDeletion(String userId) {
        log.info("Retrieving user for deletion by ID: {}", userId);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return convertToDeleteDto(user);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", userId);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String userId) {
        return userRepository.existsByUserId(userId);
    }
    
    private UserListDto convertToListDto(User user) {
        UserListDto dto = new UserListDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserType(user.getUserType());
        return dto;
    }
    
    private UserDetailDto convertToDetailDto(User user) {
        UserDetailDto dto = new UserDetailDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setUserType(user.getUserType());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    
    private UserDeleteDto convertToDeleteDto(User user) {
        UserDeleteDto dto = new UserDeleteDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserType(user.getUserType());
        return dto;
    }
}