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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    @Transactional(readOnly = true)
    public UserPageResponse getAllUsers(int page, int size) {
        try {
            log.info("Fetching users - page: {}, size: {}", page, size);
            
            if (size <= 0) {
                size = DEFAULT_PAGE_SIZE;
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("secUsrId").ascending());
            Page<User> userPage = userRepository.findAll(pageable);
            
            return convertToPageResponse(userPage);
        } catch (Exception e) {
            log.error("Unable to lookup User: {}", e.getMessage());
            throw new RuntimeException("Unable to lookup User");
        }
    }
    
    @Transactional(readOnly = true)
    public UserPageResponse getUsersStartingFromId(String startUserId, int page, int size) {
        try {
            log.info("Fetching users starting from ID: {} - page: {}, size: {}", startUserId, page, size);
            
            if (size <= 0) {
                size = DEFAULT_PAGE_SIZE;
            }
            
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage;
            
            if (startUserId != null && !startUserId.trim().isEmpty()) {
                userPage = userRepository.findUsersStartingFromId(startUserId, pageable);
            } else {
                userPage = userRepository.findAll(pageable);
            }
            
            return convertToPageResponse(userPage);
        } catch (Exception e) {
            log.error("Unable to lookup User: {}", e.getMessage());
            throw new RuntimeException("Unable to lookup User");
        }
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        try {
            log.info("Creating new user with ID: {}", request.getSecUsrId());
            
            validateCreateRequest(request);
            
            if (userRepository.existsBySecUsrId(request.getSecUsrId())) {
                log.error("User ID already exists: {}", request.getSecUsrId());
                throw new IllegalArgumentException("User ID already exist");
            }
            
            User user = new User();
            user.setSecUsrId(request.getSecUsrId());
            user.setSecUsrFname(request.getSecUsrFname());
            user.setSecUsrLname(request.getSecUsrLname());
            user.setSecUsrPwd(request.getSecUsrPwd());
            user.setSecUsrType(request.getSecUsrType());
            
            User savedUser = userRepository.save(user);
            log.info("User {} has been added...", savedUser.getSecUsrId());
            
            return convertToResponse(savedUser);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to Add User: {}", e.getMessage());
            throw new RuntimeException("Unable to Add User");
        }
    }
    
    @Transactional
    public UserResponse updateUser(String secUsrId, UpdateUserRequest request) {
        try {
            log.info("Updating user with ID: {}", secUsrId);
            
            User existingUser = userRepository.findBySecUsrId(secUsrId)
                    .orElseThrow(() -> {
                        log.error("User ID NOT found: {}", secUsrId);
                        return new IllegalArgumentException("User ID NOT found");
                    });
            
            validateUpdateRequest(request);
            
            boolean hasChanges = false;
            
            if (!request.getSecUsrFname().equals(existingUser.getSecUsrFname())) {
                existingUser.setSecUsrFname(request.getSecUsrFname());
                hasChanges = true;
            }
            
            if (!request.getSecUsrLname().equals(existingUser.getSecUsrLname())) {
                existingUser.setSecUsrLname(request.getSecUsrLname());
                hasChanges = true;
            }
            
            if (!request.getSecUsrPwd().equals(existingUser.getSecUsrPwd())) {
                existingUser.setSecUsrPwd(request.getSecUsrPwd());
                hasChanges = true;
            }
            
            if (!request.getSecUsrType().equals(existingUser.getSecUsrType())) {
                existingUser.setSecUsrType(request.getSecUsrType());
                hasChanges = true;
            }
            
            if (!hasChanges) {
                log.info("Please modify to update...");
                throw new IllegalArgumentException("Please modify to update...");
            }
            
            User updatedUser = userRepository.save(existingUser);
            log.info("User {} has been updated...", updatedUser.getSecUsrId());
            
            return convertToResponse(updatedUser);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to Update User: {}", e.getMessage());
            throw new RuntimeException("Unable to Update User");
        }
    }
    
    @Transactional
    public UserDeleteResponse deleteUser(String secUsrId) {
        try {
            log.info("Deleting user with ID: {}", secUsrId);
            
            if (secUsrId == null || secUsrId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID can NOT be empty...");
            }
            
            if (!userRepository.existsBySecUsrId(secUsrId)) {
                log.error("User ID NOT found: {}", secUsrId);
                throw new IllegalArgumentException("User ID NOT found");
            }
            
            userRepository.deleteById(secUsrId);
            log.info("User {} has been deleted...", secUsrId);
            
            UserDeleteResponse response = new UserDeleteResponse();
            response.setSecUsrId(secUsrId);
            response.setMessage("User " + secUsrId + " has been deleted ...");
            response.setDeletedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return response;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to lookup User: {}", e.getMessage());
            throw new RuntimeException("Unable to lookup User");
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String secUsrId) {
        try {
            log.info("Fetching user by ID: {}", secUsrId);
            
            if (secUsrId == null || secUsrId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID can NOT be empty...");
            }
            
            Optional<User> userOptional = userRepository.findBySecUsrId(secUsrId);
            
            if (userOptional.isEmpty()) {
                log.warn("User ID NOT found: {}", secUsrId);
                return Optional.empty();
            }
            
            return userOptional.map(this::convertToResponse);
        } catch (Exception e) {
            log.error("Unable to lookup User: {}", e.getMessage());
            throw new RuntimeException("Unable to lookup User");
        }
    }
    
    @Transactional(readOnly = true)
    public UserPageResponse searchUsers(UserSearchRequest searchRequest, int page, int size) {
        try {
            log.info("Searching users with criteria: {}", searchRequest);
            
            if (size <= 0) {
                size = DEFAULT_PAGE_SIZE;
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("secUsrId").ascending());
            Page<User> userPage = userRepository.findUsersWithFilters(
                    searchRequest.getSecUsrId(),
                    searchRequest.getSecUsrFname(),
                    searchRequest.getSecUsrLname(),
                    searchRequest.getSecUsrType(),
                    pageable
            );
            
            return convertToPageResponse(userPage);
        } catch (Exception e) {
            log.error("Unable to lookup User: {}", e.getMessage());
            throw new RuntimeException("Unable to lookup User");
        }
    }
    
    private void validateCreateRequest(CreateUserRequest request) {
        if (request.getSecUsrId() == null || request.getSecUsrId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        if (request.getSecUsrFname() == null || request.getSecUsrFname().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (request.getSecUsrLname() == null || request.getSecUsrLname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (request.getSecUsrPwd() == null || request.getSecUsrPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can NOT be empty...");
        }
        if (request.getSecUsrType() == null || request.getSecUsrType().trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
    }
    
    private void validateUpdateRequest(UpdateUserRequest request) {
        if (request.getSecUsrFname() == null || request.getSecUsrFname().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (request.getSecUsrLname() == null || request.getSecUsrLname().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (request.getSecUsrPwd() == null || request.getSecUsrPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("Password can NOT be empty...");
        }
        if (request.getSecUsrType() == null || request.getSecUsrType().trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setSecUsrId(user.getSecUsrId());
        response.setSecUsrFname(user.getSecUsrFname());
        response.setSecUsrLname(user.getSecUsrLname());
        response.setSecUsrType(user.getSecUsrType());
        response.setFullName(user.getFullName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
    
    private UserPageResponse convertToPageResponse(Page<User> userPage) {
        UserPageResponse response = new UserPageResponse();
        response.setUsers(userPage.getContent().stream()
                .map(this::convertToResponse)
                .toList());
        response.setCurrentPage(userPage.getNumber());
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalElements(userPage.getTotalElements());
        response.setPageSize(userPage.getSize());
        response.setFirst(userPage.isFirst());
        response.setLast(userPage.isLast());
        response.setHasNext(userPage.hasNext());
        response.setHasPrevious(userPage.hasPrevious());
        return response;
    }
}