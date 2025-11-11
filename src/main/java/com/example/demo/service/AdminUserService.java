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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    @Transactional
    public AdminUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {
        log.info("Creating new admin user with user_id: {}", request.getUserId());

        if (adminUserRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Admin user with user_id already exists");
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setUserId(request.getUserId());
        adminUser.setAuthenticationStatus(request.getAuthenticationStatus() != null ? request.getAuthenticationStatus() : false);

        AdminUser savedAdminUser = adminUserRepository.save(adminUser);
        log.info("Successfully created admin user with user_id: {}", savedAdminUser.getUserId());
        return convertToResponse(savedAdminUser);
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserResponseDto> getAdminUserById(String userId) {
        log.debug("Fetching admin user by user_id: {}", userId);
        return adminUserRepository.findById(userId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserResponseDto> getAdminUserByUserId(String userId) {
        log.debug("Fetching admin user by user_id: {}", userId);
        return adminUserRepository.findByUserId(userId).map(this::convertToResponse);
    }

    @Transactional
    public AdminUserResponseDto updateAdminUser(String userId, UpdateAdminUserRequestDto request) {
        log.info("Updating admin user with user_id: {}", userId);

        AdminUser adminUser = adminUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found with user_id: " + userId));

        if (request.getAuthenticationStatus() != null) {
            adminUser.setAuthenticationStatus(request.getAuthenticationStatus());
        }

        AdminUser updatedAdminUser = adminUserRepository.save(adminUser);
        log.info("Successfully updated admin user with user_id: {}", userId);
        return convertToResponse(updatedAdminUser);
    }

    @Transactional
    public void deleteAdminUser(String userId) {
        log.info("Deleting admin user with user_id: {}", userId);

        if (!adminUserRepository.existsById(userId)) {
            throw new IllegalArgumentException("Admin user not found with user_id: " + userId);
        }

        adminUserRepository.deleteById(userId);
        log.info("Successfully deleted admin user with user_id: {}", userId);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponseDto> getAllAdminUsers(Pageable pageable) {
        log.debug("Fetching all admin users with pagination");
        return adminUserRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public AdminUserResponseDto authenticateAdminUser(String userId) {
        log.info("Authenticating admin user with user_id: {}", userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found with user_id: " + userId));

        if (adminUser.getAuthenticationStatus()) {
            log.warn("Admin user with user_id: {} is already authenticated", userId);
            throw new IllegalStateException("Admin user is already authenticated");
        }

        adminUser.authenticate();
        AdminUser authenticatedUser = adminUserRepository.save(adminUser);
        log.info("Successfully authenticated admin user with user_id: {}", userId);
        return convertToResponse(authenticatedUser);
    }

    @Transactional
    public AdminUserResponseDto deauthenticateAdminUser(String userId) {
        log.info("Deauthenticating admin user with user_id: {}", userId);

        AdminUser adminUser = adminUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found with user_id: " + userId));

        if (!adminUser.getAuthenticationStatus()) {
            log.warn("Admin user with user_id: {} is already deauthenticated", userId);
            throw new IllegalStateException("Admin user is already deauthenticated");
        }

        adminUser.deauthenticate();
        AdminUser deauthenticatedUser = adminUserRepository.save(adminUser);
        log.info("Successfully deauthenticated admin user with user_id: {}", userId);
        return convertToResponse(deauthenticatedUser);
    }

    @Transactional(readOnly = true)
    public boolean isAdminUserAuthenticated(String userId) {
        log.debug("Checking authentication status for admin user with user_id: {}", userId);
        return adminUserRepository.isUserAuthenticated(userId);
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllAuthenticatedAdminUsers() {
        log.debug("Fetching all authenticated admin users");
        List<AdminUser> authenticatedUsers = adminUserRepository.findAuthenticatedUsers();
        return authenticatedUsers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllUnauthenticatedAdminUsers() {
        log.debug("Fetching all unauthenticated admin users");
        List<AdminUser> unauthenticatedUsers = adminUserRepository.findUnauthenticatedUsers();
        return unauthenticatedUsers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countAuthenticatedAdminUsers() {
        log.debug("Counting authenticated admin users");
        return adminUserRepository.countAuthenticatedUsers();
    }

    @Transactional(readOnly = true)
    public long countUnauthenticatedAdminUsers() {
        log.debug("Counting unauthenticated admin users");
        return adminUserRepository.countByAuthenticationStatus(false);
    }

    @Transactional
    public void deauthenticateAllAdminUsers() {
        log.info("Deauthenticating all admin users");
        List<AdminUser> authenticatedUsers = adminUserRepository.findAuthenticatedUsers();
        
        for (AdminUser adminUser : authenticatedUsers) {
            adminUser.deauthenticate();
        }
        
        adminUserRepository.saveAll(authenticatedUsers);
        log.info("Successfully deauthenticated {} admin users", authenticatedUsers.size());
    }

    private AdminUserResponseDto convertToResponse(AdminUser adminUser) {
        AdminUserResponseDto response = new AdminUserResponseDto();
        response.setUserId(adminUser.getUserId());
        response.setAuthenticationStatus(adminUser.getAuthenticationStatus());
        response.setCreatedAt(adminUser.getCreatedAt());
        response.setUpdatedAt(adminUser.getUpdatedAt());
        return response;
    }
}
