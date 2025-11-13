package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UpdateUserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private static final int PAGE_SIZE = 10;
    private static final String ADMIN_USER_TYPE = "A";
    private static final String REGULAR_USER_TYPE = "R";
    private static final String SIGN_ON_PROGRAM = "COSGN00C";

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating new user with user_id: {}", request.getUserId());

        validateUserIdNotEmpty(request.getUserId());
        validatePasswordNotEmpty(request.getPassword());
        validateFirstNameNotEmpty(request.getFirstName());
        validateLastNameNotEmpty(request.getLastName());
        validateUserTypeNotEmpty(request.getUserType());

        if (userRepository.existsByUserId(request.getUserId())) {
            log.error("User ID already exists: {}", request.getUserId());
            throw new IllegalArgumentException("User ID already exist...");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserType(request.getUserType());
        user.setAuthenticated(request.getAuthenticated() != null ? request.getAuthenticated() : false);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with user_id: {}", savedUser.getUserId());
        return convertToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUserId(String userId) {
        log.info("Fetching user by user_id: {}", userId);
        validateUserIdNotEmpty(userId);
        return userRepository.findByUserId(userId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto request) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Unable to update user - user not found: {}", id);
                    return new IllegalArgumentException("Unable to Update User...");
                });

        if (request.getPassword() != null) {
            validatePasswordNotEmpty(request.getPassword());
            user.setPassword(request.getPassword());
        }

        if (request.getFirstName() != null) {
            validateFirstNameNotEmpty(request.getFirstName());
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            validateLastNameNotEmpty(request.getLastName());
            user.setLastName(request.getLastName());
        }

        if (request.getUserType() != null) {
            validateUserTypeNotEmpty(request.getUserType());
            user.setUserType(request.getUserType());
        }

        if (request.getAuthenticated() != null) {
            user.setAuthenticated(request.getAuthenticated());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        return convertToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("Unable to delete user - user not found: {}", id);
            throw new IllegalArgumentException("Unable to Update User...");
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        log.info("Fetching all users - page: {}", pageable.getPageNumber());
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUsersByType(String userType, Pageable pageable) {
        log.info("Fetching users by type: {} - page: {}", userType, pageable.getPageNumber());
        validateUserTypeNotEmpty(userType);
        return userRepository.findByUserType(userType, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(String searchTerm, Pageable pageable) {
        log.info("Searching users with term: {} - page: {}", searchTerm, pageable.getPageNumber());
        return userRepository.findByNameContaining(searchTerm, pageable).map(this::convertToResponse);
    }

    @Transactional
    public LoginResponseDto authenticateUser(LoginRequestDto request) {
        log.info("Authenticating user: {}", request.getUserId());

        validateUserIdNotEmpty(request.getUserId());
        validatePasswordNotEmpty(request.getPassword());

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found during authentication: {}", request.getUserId());
                    return new IllegalArgumentException("User not found. Try again ...");
                });

        if (!user.getPassword().equals(request.getPassword())) {
            log.error("Wrong password for user: {}", request.getUserId());
            throw new IllegalArgumentException("Wrong Password. Try again ...");
        }

        user.setAuthenticated(true);
        User authenticatedUser = userRepository.save(user);
        log.info("User authenticated successfully: {}", request.getUserId());
        
        UserResponseDto userResponse = convertToResponse(authenticatedUser);
        return new LoginResponseDto(true, "Login successful", userResponse, "fake-session-token-123");
    }

    @Transactional(readOnly = true)
    public void checkAdminAuthentication(String userId) {
        log.info("Checking admin authentication for user: {}", userId);

        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID is empty during admin authentication check");
            throw new IllegalArgumentException("Please enter User ID ...");
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User not found during admin authentication check: {}", userId);
                    return new IllegalArgumentException("Unable to lookup User...");
                });

        if (!user.getAuthenticated()) {
            log.warn("User not authenticated: {}", userId);
            throw new IllegalArgumentException("User not authenticated - redirect to " + SIGN_ON_PROGRAM);
        }

        if (!ADMIN_USER_TYPE.equals(user.getUserType())) {
            log.warn("User is not an admin: {}", userId);
            throw new IllegalArgumentException("User is not authorized for admin access");
        }
    }

    public void validateMenuOption(Integer option, Integer maxOptions) {
        log.info("Validating menu option: {} against max: {}", option, maxOptions);

        if (option == null || option == 0) {
            log.error("Menu option is null or zero");
            throw new IllegalArgumentException("Please enter a valid option number...");
        }

        if (option > maxOptions) {
            log.error("Menu option {} exceeds maximum {}", option, maxOptions);
            throw new IllegalArgumentException("Please enter a valid option number...");
        }

        log.info("Menu option validated successfully: {}", option);
    }

    public String normalizeOptionInput(String input) {
        log.info("Normalizing option input: {}", input);

        if (input == null || input.trim().isEmpty()) {
            log.warn("Option input is empty");
            return "0";
        }

        String normalized = input.trim();

        try {
            Integer.parseInt(normalized);
            log.info("Option input normalized to: {}", normalized);
            return normalized;
        } catch (NumberFormatException e) {
            log.error("Option input is not numeric after normalization: {}", normalized);
            throw new IllegalArgumentException("Please enter a valid option number...");
        }
    }

    public void checkAccessControl(String userId, boolean isAdminOption) {
        log.info("Checking access control for user: {} on admin option: {}", userId, isAdminOption);

        validateUserIdNotEmpty(userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User not found during access control check: {}", userId);
                    return new IllegalArgumentException("Unable to lookup User...");
                });

        if (isAdminOption && !ADMIN_USER_TYPE.equals(user.getUserType())) {
            log.warn("Regular user {} attempting to access admin-only option", userId);
            throw new IllegalArgumentException("No access - Admin Only option");
        }

        log.info("Access control check passed for user: {}", userId);
    }

    public boolean isComingSoonFeature(String programName) {
        log.info("Checking if program is coming soon: {}", programName);
        return programName != null && programName.startsWith("DUMMY");
    }

    public String getComingSoonMessage(String optionName) {
        log.info("Generating coming soon message for option: {}", optionName);
        return "This option " + optionName + " is coming soon";
    }

    public String getSignOnProgram() {
        return SIGN_ON_PROGRAM;
    }

    private void validateUserIdNotEmpty(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID is empty");
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }

        if (userId.trim().replace("\u0000", "").isEmpty()) {
            log.error("User ID contains only low-values");
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }

        if (userId.length() > 8) {
            log.error("User ID exceeds maximum length of 8");
            throw new IllegalArgumentException("User ID exceeds maximum length");
        }
    }

    private void validatePasswordNotEmpty(String password) {
        if (password == null || password.trim().isEmpty()) {
            log.error("Password is empty");
            throw new IllegalArgumentException("Password can NOT be empty...");
        }

        if (password.trim().replace("\u0000", "").isEmpty()) {
            log.error("Password contains only low-values");
            throw new IllegalArgumentException("Password can NOT be empty...");
        }

        if (password.length() > 8) {
            log.error("Password exceeds maximum length of 8");
            throw new IllegalArgumentException("Password exceeds maximum length");
        }
    }

    private void validateFirstNameNotEmpty(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            log.error("First Name is empty");
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }

        if (firstName.trim().replace("\u0000", "").isEmpty()) {
            log.error("First Name contains only low-values");
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }

        if (firstName.length() > 25) {
            log.error("First Name exceeds maximum length of 25");
            throw new IllegalArgumentException("First Name exceeds maximum length");
        }
    }

    private void validateLastNameNotEmpty(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            log.error("Last Name is empty");
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }

        if (lastName.trim().replace("\u0000", "").isEmpty()) {
            log.error("Last Name contains only low-values");
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }

        if (lastName.length() > 25) {
            log.error("Last Name exceeds maximum length of 25");
            throw new IllegalArgumentException("Last Name exceeds maximum length");
        }
    }

    private void validateUserTypeNotEmpty(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            log.error("User Type is empty");
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }

        if (userType.trim().replace("\u0000", "").isEmpty()) {
            log.error("User Type contains only low-values");
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }

        if (userType.length() > 1) {
            log.error("User Type exceeds maximum length of 1");
            throw new IllegalArgumentException("User Type exceeds maximum length");
        }
    }

    private UserResponseDto convertToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUserId(user.getUserId());
        response.setUserType(user.getUserType());
        response.setAuthenticated(user.getAuthenticated());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
