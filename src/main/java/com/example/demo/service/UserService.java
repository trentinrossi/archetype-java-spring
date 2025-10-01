package com.example.demo.service;

import com.example.demo.dto.*;
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
    private static final int PAGE_SIZE = 10;
    
    @Transactional(readOnly = true)
    public Page<UserListDTO> getAllUsers(int page, UserSearchDTO searchDTO) {
        log.info("Retrieving users for page: {} with search criteria: {}", page, searchDTO);
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("secUsrId"));
        Page<User> users;
        
        if (searchDTO != null && searchDTO.getSecUsrIdStartsWith() != null && !searchDTO.getSecUsrIdStartsWith().trim().isEmpty()) {
            users = userRepository.findBySecUsrIdStartingWith(searchDTO.getSecUsrIdStartsWith().trim(), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        
        return users.map(this::convertToUserListDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getUserById(String secUsrId) {
        log.info("Retrieving user by ID: {}", secUsrId);
        
        if (secUsrId == null || secUsrId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        return userRepository.findById(secUsrId.trim().toUpperCase())
                .map(this::convertToUserResponseDTO);
    }
    
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO createDTO) {
        log.info("Creating new user with ID: {}", createDTO.getSecUsrId());
        
        validateCreateUserDTO(createDTO);
        
        String userId = createDTO.getSecUsrId().trim().toUpperCase();
        
        if (userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with ID '" + userId + "' already exists");
        }
        
        User user = new User();
        user.setSecUsrId(userId);
        user.setSecUsrFname(createDTO.getSecUsrFname().trim());
        user.setSecUsrLname(createDTO.getSecUsrLname().trim());
        user.setSecUsrPwd(createDTO.getSecUsrPwd().trim());
        user.setSecUsrType(createDTO.getSecUsrType().trim().toUpperCase());
        
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getSecUsrId());
        
        return convertToUserResponseDTO(savedUser);
    }
    
    @Transactional
    public UserResponseDTO updateUser(String secUsrId, UserUpdateDTO updateDTO) {
        log.info("Updating user with ID: {}", secUsrId);
        
        if (secUsrId == null || secUsrId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        validateUpdateUserDTO(updateDTO);
        
        String userId = secUsrId.trim().toUpperCase();
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID '" + userId + "' not found"));
        
        boolean hasChanges = false;
        
        String newFirstName = updateDTO.getSecUsrFname().trim();
        if (!existingUser.getSecUsrFname().equals(newFirstName)) {
            existingUser.setSecUsrFname(newFirstName);
            hasChanges = true;
        }
        
        String newLastName = updateDTO.getSecUsrLname().trim();
        if (!existingUser.getSecUsrLname().equals(newLastName)) {
            existingUser.setSecUsrLname(newLastName);
            hasChanges = true;
        }
        
        String newPassword = updateDTO.getSecUsrPwd().trim();
        if (!existingUser.getSecUsrPwd().equals(newPassword)) {
            existingUser.setSecUsrPwd(newPassword);
            hasChanges = true;
        }
        
        String newUserType = updateDTO.getSecUsrType().trim().toUpperCase();
        if (!existingUser.getSecUsrType().equals(newUserType)) {
            existingUser.setSecUsrType(newUserType);
            hasChanges = true;
        }
        
        if (!hasChanges) {
            log.info("No changes detected for user with ID: {}", userId);
            return convertToUserResponseDTO(existingUser);
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", updatedUser.getSecUsrId());
        
        return convertToUserResponseDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String secUsrId) {
        log.info("Deleting user with ID: {}", secUsrId);
        
        if (secUsrId == null || secUsrId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        String userId = secUsrId.trim().toUpperCase();
        
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with ID '" + userId + "' not found");
        }
        
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with ID: {}", userId);
    }
    
    @Transactional(readOnly = true)
    public boolean userExists(String secUsrId) {
        if (secUsrId == null || secUsrId.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsById(secUsrId.trim().toUpperCase());
    }
    
    private void validateCreateUserDTO(UserCreateDTO createDTO) {
        if (createDTO == null) {
            throw new IllegalArgumentException("User creation data cannot be null");
        }
        
        if (createDTO.getSecUsrId() == null || createDTO.getSecUsrId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is mandatory");
        }
        
        if (createDTO.getSecUsrFname() == null || createDTO.getSecUsrFname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is mandatory");
        }
        
        if (createDTO.getSecUsrLname() == null || createDTO.getSecUsrLname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is mandatory");
        }
        
        if (createDTO.getSecUsrPwd() == null || createDTO.getSecUsrPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory");
        }
        
        if (createDTO.getSecUsrType() == null || createDTO.getSecUsrType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type is mandatory");
        }
        
        validateUserType(createDTO.getSecUsrType().trim().toUpperCase());
    }
    
    private void validateUpdateUserDTO(UserUpdateDTO updateDTO) {
        if (updateDTO == null) {
            throw new IllegalArgumentException("User update data cannot be null");
        }
        
        if (updateDTO.getSecUsrFname() == null || updateDTO.getSecUsrFname().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is mandatory");
        }
        
        if (updateDTO.getSecUsrLname() == null || updateDTO.getSecUsrLname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is mandatory");
        }
        
        if (updateDTO.getSecUsrPwd() == null || updateDTO.getSecUsrPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is mandatory");
        }
        
        if (updateDTO.getSecUsrType() == null || updateDTO.getSecUsrType().trim().isEmpty()) {
            throw new IllegalArgumentException("User type is mandatory");
        }
        
        validateUserType(updateDTO.getSecUsrType().trim().toUpperCase());
    }
    
    private void validateUserType(String userType) {
        if (!"A".equals(userType) && !"U".equals(userType)) {
            throw new IllegalArgumentException("User type must be 'A' (Admin) or 'U' (User)");
        }
    }
    
    private UserListDTO convertToUserListDTO(User user) {
        UserListDTO dto = new UserListDTO();
        dto.setSecUsrId(user.getSecUsrId());
        dto.setSecUsrFname(user.getSecUsrFname());
        dto.setSecUsrLname(user.getSecUsrLname());
        dto.setSecUsrType(user.getSecUsrType());
        return dto;
    }
    
    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setSecUsrId(user.getSecUsrId());
        dto.setSecUsrFname(user.getSecUsrFname());
        dto.setSecUsrLname(user.getSecUsrLname());
        dto.setFullName(user.getFullName());
        dto.setSecUsrType(user.getSecUsrType());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}