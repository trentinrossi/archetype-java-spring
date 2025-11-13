package com.example.demo.service;

import com.example.demo.dto.CreateAdminUserRequestDto;
import com.example.demo.dto.UpdateAdminUserRequestDto;
import com.example.demo.dto.AdminUserResponseDto;
import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;
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
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    @Transactional
    public AdminUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {
        log.info("Creating new admin user with user_id: {}", request.getUserId());

        validateUserIdNotEmpty(request.getUserId());
        validateUserIdUnique(request.getUserId());

        AdminUser adminUser = new AdminUser();
        adminUser.setUserId(request.getUserId());
        adminUser.setAuthenticationStatus(request.getAuthenticationStatus() != null ? request.getAuthenticationStatus() : false);

        AdminUser savedAdminUser = adminUserRepository.save(adminUser);
        log.info("Successfully created admin user with user_id: {}", savedAdminUser.getUserId());
        return convertToResponse(savedAdminUser);
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserResponseDto> getAdminUserById(String userId) {
        log.info("Retrieving admin user with user_id: {}", userId);

        validateUserIdNotEmpty(userId);

        Optional<AdminUser> adminUser = adminUserRepository.findByUserId(userId);
        
        if (adminUser.isEmpty()) {
            log.warn("User not found with user_id: {}", userId);
        }

        return adminUser.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public AdminUserResponseDto getAdminUserByIdOrThrow(String userId) {
        log.info("Retrieving admin user with user_id: {}", userId);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        return convertToResponse(adminUser);
    }

    @Transactional
    public AdminUserResponseDto updateAdminUser(String userId, UpdateAdminUserRequestDto request) {
        log.info("Updating admin user with user_id: {}", userId);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        if (request.getAuthenticationStatus() != null) {
            adminUser.setAuthenticationStatus(request.getAuthenticationStatus());
        }

        AdminUser updatedAdminUser = adminUserRepository.save(adminUser);
        log.info("Successfully updated admin user with user_id: {}", updatedAdminUser.getUserId());
        return convertToResponse(updatedAdminUser);
    }

    @Transactional
    public void deleteAdminUser(String userId) {
        log.info("Deleting admin user with user_id: {}", userId);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        try {
            adminUserRepository.delete(adminUser);
            log.info("Successfully deleted admin user with user_id: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete admin user with user_id: {}", userId, e);
            throw new IllegalStateException("Unable to delete user");
        }
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponseDto> getAllAdminUsers(Pageable pageable) {
        log.info("Retrieving all admin users with pagination");
        return adminUserRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public boolean authenticateAdminUser(String userId) {
        log.info("Authenticating admin user with user_id: {}", userId);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        if (!adminUser.getAuthenticationStatus()) {
            log.warn("Admin user with user_id: {} is not authenticated", userId);
            return false;
        }

        log.info("Admin user with user_id: {} is authenticated", userId);
        return true;
    }

    @Transactional
    public void setAuthenticationStatus(String userId, boolean status) {
        log.info("Setting authentication status for user_id: {} to {}", userId, status);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        adminUser.setAuthenticationStatus(status);
        adminUserRepository.save(adminUser);
        log.info("Successfully set authentication status for user_id: {}", userId);
    }

    @Transactional(readOnly = true)
    public void validateAdminAccess(String userId) {
        log.info("Validating admin access for user_id: {}", userId);

        validateUserIdNotEmpty(userId);
        validateUserIdExists(userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID NOT found..."));

        if (!adminUser.getAuthenticationStatus()) {
            log.error("User with user_id: {} is not authenticated as admin", userId);
            throw new SecurityException("User must be authenticated as admin before accessing the admin menu");
        }

        log.info("Admin access validated for user_id: {}", userId);
    }

    private void validateUserIdNotEmpty(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID validation failed: empty or null");
            throw new IllegalArgumentException("User ID cannot be empty or spaces");
        }

        if (userId.matches("^\\s+$")) {
            log.error("User ID validation failed: contains only whitespace");
            throw new IllegalArgumentException("User ID cannot be empty or spaces");
        }
    }

    private void validateUserIdUnique(String userId) {
        if (adminUserRepository.existsByUserId(userId)) {
            log.error("User ID validation failed: already exists - {}", userId);
            throw new IllegalArgumentException("User ID already exists");
        }
    }

    private void validateUserIdExists(String userId) {
        if (!adminUserRepository.existsByUserId(userId)) {
            log.error("User ID validation failed: not found - {}", userId);
            throw new IllegalArgumentException("User ID NOT found...");
        }
    }

    private AdminUserResponseDto convertToResponse(AdminUser adminUser) {
        AdminUserResponseDto response = new AdminUserResponseDto();
        response.setUserId(adminUser.getUserId());
        response.setAuthenticationStatus(adminUser.getAuthenticationStatus());
        response.setIsAuthenticated(adminUser.isAuthenticated());
        response.setMenuOptionCount(adminUser.getMenuOptionCount());
        response.setCreatedAt(adminUser.getCreatedAt());
        response.setUpdatedAt(adminUser.getUpdatedAt());
        return response;
    }
}
