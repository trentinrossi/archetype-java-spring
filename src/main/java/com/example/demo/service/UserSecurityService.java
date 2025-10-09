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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService {
    
    private final UserRepository userRepository;
    
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String ADMIN_TYPE = "A";
    private static final String REGULAR_TYPE = "R";
    
    @Transactional(readOnly = true)
    public boolean authenticateUser(SignonDTO signonDTO) {
        log.info("Attempting authentication for user: {}", signonDTO.getUserId());
        
        if (!StringUtils.hasText(signonDTO.getUserId())) {
            log.warn("Authentication failed: User ID is empty");
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        if (!StringUtils.hasText(signonDTO.getPassword())) {
            log.warn("Authentication failed: Password is empty for user: {}", signonDTO.getUserId());
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        Optional<User> userOpt = userRepository.findById(signonDTO.getUserId());
        if (userOpt.isEmpty()) {
            log.warn("Authentication failed: User not found: {}", signonDTO.getUserId());
            return false;
        }
        
        User user = userOpt.get();
        boolean authenticated = user.getPassword().equals(signonDTO.getPassword());
        
        if (authenticated) {
            log.info("Authentication successful for user: {}", signonDTO.getUserId());
        } else {
            log.warn("Authentication failed: Invalid password for user: {}", signonDTO.getUserId());
        }
        
        return authenticated;
    }
    
    @Transactional(readOnly = true)
    public boolean validatePassword(String userId, String password) {
        log.debug("Validating password for user: {}", userId);
        
        if (!StringUtils.hasText(password)) {
            return false;
        }
        
        if (password.length() > 8) {
            return false;
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(user -> user.getPassword().equals(password)).orElse(false);
    }
    
    @Transactional(readOnly = true)
    public UserListDTO getUserList(int page, int size) {
        log.info("Retrieving user list - page: {}, size: {}", page, size);
        
        if (size <= 0) {
            size = DEFAULT_PAGE_SIZE;
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        Page<User> userPage = userRepository.findAll(pageable);
        
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUsers(userDTOs);
        userListDTO.setCurrentPage(userPage.getNumber());
        userListDTO.setTotalPages(userPage.getTotalPages());
        userListDTO.setTotalElements(userPage.getTotalElements());
        userListDTO.setPageSize(userPage.getSize());
        userListDTO.setFirst(userPage.isFirst());
        userListDTO.setLast(userPage.isLast());
        userListDTO.setHasNext(userPage.hasNext());
        userListDTO.setHasPrevious(userPage.hasPrevious());
        
        if (!userDTOs.isEmpty()) {
            userListDTO.setFirstUserId(userDTOs.get(0).getUserId());
            userListDTO.setLastUserId(userDTOs.get(userDTOs.size() - 1).getUserId());
        }
        
        log.info("Retrieved {} users for page {}", userDTOs.size(), page);
        return userListDTO;
    }
    
    @Transactional(readOnly = true)
    public UserListDTO getNextPage(String lastUserId, int size) {
        log.info("Retrieving next page after user: {}", lastUserId);
        
        if (size <= 0) {
            size = DEFAULT_PAGE_SIZE;
        }
        
        Pageable pageable = PageRequest.of(0, size, Sort.by("userId").ascending());
        Page<User> userPage = userRepository.findByUserIdGreaterThan(lastUserId, pageable);
        
        return buildUserListDTO(userPage);
    }
    
    @Transactional(readOnly = true)
    public UserListDTO getPreviousPage(String firstUserId, int size) {
        log.info("Retrieving previous page before user: {}", firstUserId);
        
        if (size <= 0) {
            size = DEFAULT_PAGE_SIZE;
        }
        
        Pageable pageable = PageRequest.of(0, size, Sort.by("userId").descending());
        Page<User> userPage = userRepository.findByUserIdLessThan(firstUserId, pageable);
        
        List<User> reversedUsers = new ArrayList<>(userPage.getContent());
        reversedUsers.sort((u1, u2) -> u1.getUserId().compareTo(u2.getUserId()));
        
        UserListDTO userListDTO = buildUserListDTO(userPage);
        userListDTO.setUsers(reversedUsers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList()));
        
        return userListDTO;
    }
    
    @Transactional
    public UserDTO addUser(CreateUserDTO createUserDTO) {
        log.info("Adding new user: {}", createUserDTO.getUserId());
        
        validateCreateUserRequest(createUserDTO);
        
        if (userRepository.existsById(createUserDTO.getUserId())) {
            log.warn("User creation failed: User ID already exists: {}", createUserDTO.getUserId());
            throw new IllegalArgumentException("User ID already exists: " + createUserDTO.getUserId());
        }
        
        User user = new User();
        user.setUserId(createUserDTO.getUserId());
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setPassword(createUserDTO.getPassword());
        user.setUserType(createUserDTO.getUserType());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUserId());
        
        return convertToUserDTO(savedUser);
    }
    
    @Transactional
    public UserDTO updateUser(String userId, UpdateUserDTO updateUserDTO) {
        log.info("Updating user: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        List<String> changes = detectFieldChanges(existingUser, updateUserDTO);
        
        if (changes.isEmpty()) {
            log.info("No changes detected for user: {}", userId);
            return convertToUserDTO(existingUser);
        }
        
        applyUpdates(existingUser, updateUserDTO);
        User updatedUser = userRepository.save(existingUser);
        
        log.info("User updated successfully: {} - Changes: {}", userId, String.join(", ", changes));
        return convertToUserDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        userRepository.deleteById(userId);
        log.info("User deleted successfully: {}", userId);
    }
    
    @Transactional
    public boolean confirmDeleteUser(String userId) {
        log.info("Confirming delete operation for user: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.warn("Delete confirmation failed: User not found: {}", userId);
            return false;
        }
        
        User user = userOpt.get();
        log.info("Delete confirmation for user: {} - {} ({})", userId, user.getFullName(), 
                getUserTypeDisplayName(user.getUserType()));
        
        return true;
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(String userId) {
        log.debug("Retrieving user by ID: {}", userId);
        
        if (!StringUtils.hasText(userId)) {
            return Optional.empty();
        }
        
        return userRepository.findById(userId)
                .map(this::convertToUserDTO);
    }
    
    @Transactional(readOnly = true)
    public List<UserSelectionDTO> getUsersForSelection() {
        log.info("Retrieving users for selection");
        
        List<User> users = userRepository.findAll(Sort.by("userId").ascending());
        
        return users.stream()
                .map(this::convertToUserSelectionDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UserSelectionDTO> getAdminUsersForSelection() {
        log.info("Retrieving admin users for selection");
        
        List<User> adminUsers = userRepository.findByUserType(ADMIN_TYPE, Sort.by("userId").ascending());
        
        return adminUsers.stream()
                .map(this::convertToUserSelectionDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UserSelectionDTO> getRegularUsersForSelection() {
        log.info("Retrieving regular users for selection");
        
        List<User> regularUsers = userRepository.findByUserType(REGULAR_TYPE, Sort.by("userId").ascending());
        
        return regularUsers.stream()
                .map(this::convertToUserSelectionDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public boolean hasUserAccess(String userId, String requiredType) {
        log.debug("Checking user access for: {} with required type: {}", userId, requiredType);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        
        if (ADMIN_TYPE.equals(requiredType)) {
            return user.isAdmin();
        } else if (REGULAR_TYPE.equals(requiredType)) {
            return user.isRegular();
        }
        
        return false;
    }
    
    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        return userRepository.count();
    }
    
    @Transactional(readOnly = true)
    public long getAdminUserCount() {
        return userRepository.countByUserType(ADMIN_TYPE);
    }
    
    @Transactional(readOnly = true)
    public long getRegularUserCount() {
        return userRepository.countByUserType(REGULAR_TYPE);
    }
    
    private void validateCreateUserRequest(CreateUserDTO createUserDTO) {
        if (!StringUtils.hasText(createUserDTO.getUserId())) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        
        if (!StringUtils.hasText(createUserDTO.getFirstName())) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        
        if (!StringUtils.hasText(createUserDTO.getLastName())) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        
        if (!StringUtils.hasText(createUserDTO.getPassword())) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (!StringUtils.hasText(createUserDTO.getUserType())) {
            throw new IllegalArgumentException("User type cannot be empty");
        }
        
        if (!ADMIN_TYPE.equals(createUserDTO.getUserType()) && !REGULAR_TYPE.equals(createUserDTO.getUserType())) {
            throw new IllegalArgumentException("User type must be 'A' (Admin) or 'R' (Regular)");
        }
    }
    
    private List<String> detectFieldChanges(User existingUser, UpdateUserDTO updateUserDTO) {
        List<String> changes = new ArrayList<>();
        
        if (StringUtils.hasText(updateUserDTO.getFirstName()) && 
            !updateUserDTO.getFirstName().equals(existingUser.getFirstName())) {
            changes.add("First Name");
        }
        
        if (StringUtils.hasText(updateUserDTO.getLastName()) && 
            !updateUserDTO.getLastName().equals(existingUser.getLastName())) {
            changes.add("Last Name");
        }
        
        if (StringUtils.hasText(updateUserDTO.getPassword()) && 
            !updateUserDTO.getPassword().equals(existingUser.getPassword())) {
            changes.add("Password");
        }
        
        if (StringUtils.hasText(updateUserDTO.getUserType()) && 
            !updateUserDTO.getUserType().equals(existingUser.getUserType())) {
            changes.add("User Type");
        }
        
        return changes;
    }
    
    private void applyUpdates(User existingUser, UpdateUserDTO updateUserDTO) {
        if (StringUtils.hasText(updateUserDTO.getFirstName())) {
            existingUser.setFirstName(updateUserDTO.getFirstName());
        }
        
        if (StringUtils.hasText(updateUserDTO.getLastName())) {
            existingUser.setLastName(updateUserDTO.getLastName());
        }
        
        if (StringUtils.hasText(updateUserDTO.getPassword())) {
            existingUser.setPassword(updateUserDTO.getPassword());
        }
        
        if (StringUtils.hasText(updateUserDTO.getUserType())) {
            if (!ADMIN_TYPE.equals(updateUserDTO.getUserType()) && !REGULAR_TYPE.equals(updateUserDTO.getUserType())) {
                throw new IllegalArgumentException("User type must be 'A' (Admin) or 'R' (Regular)");
            }
            existingUser.setUserType(updateUserDTO.getUserType());
        }
    }
    
    private UserListDTO buildUserListDTO(Page<User> userPage) {
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
        
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUsers(userDTOs);
        userListDTO.setCurrentPage(userPage.getNumber());
        userListDTO.setTotalPages(userPage.getTotalPages());
        userListDTO.setTotalElements(userPage.getTotalElements());
        userListDTO.setPageSize(userPage.getSize());
        userListDTO.setFirst(userPage.isFirst());
        userListDTO.setLast(userPage.isLast());
        userListDTO.setHasNext(userPage.hasNext());
        userListDTO.setHasPrevious(userPage.hasPrevious());
        
        if (!userDTOs.isEmpty()) {
            userListDTO.setFirstUserId(userDTOs.get(0).getUserId());
            userListDTO.setLastUserId(userDTOs.get(userDTOs.size() - 1).getUserId());
        }
        
        return userListDTO;
    }
    
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setFullName(user.getFullName());
        userDTO.setUserType(user.getUserType());
        userDTO.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        userDTO.setAdmin(user.isAdmin());
        userDTO.setRegular(user.isRegular());
        userDTO.setHasFullAccess(user.hasFullAccess());
        userDTO.setHasLimitedAccess(user.hasLimitedAccess());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
    
    private UserSelectionDTO convertToUserSelectionDTO(User user) {
        UserSelectionDTO selectionDTO = new UserSelectionDTO();
        selectionDTO.setUserId(user.getUserId());
        selectionDTO.setFullName(user.getFullName());
        selectionDTO.setUserType(user.getUserType());
        selectionDTO.setUserTypeDisplayName(getUserTypeDisplayName(user.getUserType()));
        selectionDTO.setSelectable(true);
        selectionDTO.setSelectionContext("Available for selection");
        return selectionDTO;
    }
    
    private String getUserTypeDisplayName(String userType) {
        return switch (userType) {
            case ADMIN_TYPE -> "Administrator";
            case REGULAR_TYPE -> "Regular User";
            default -> "Unknown";
        };
    }
}