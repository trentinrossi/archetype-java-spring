package com.example.demo.service;

import com.example.demo.dto.CreateUserSessionRequestDto;
import com.example.demo.dto.UpdateUserSessionRequestDto;
import com.example.demo.dto.UserSessionResponseDto;
import com.example.demo.entity.UserSession;
import com.example.demo.repository.UserSessionRepository;
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
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Transactional
    public UserSessionResponseDto createUserSession(CreateUserSessionRequestDto request) {
        log.info("Creating new user session for user: {}", request.getUserId());

        UserSession userSession = new UserSession();
        userSession.setTransactionId(request.getTransactionId());
        userSession.setProgramName(request.getProgramName());
        userSession.setFromProgram(request.getFromProgram());
        userSession.setFromTransaction(request.getFromTransaction());
        userSession.setProgramContext(request.getProgramContext());
        userSession.setReenterFlag(request.getReenterFlag());
        userSession.setToProgram(request.getToProgram());
        userSession.setProgramReenterFlag(request.getProgramReenterFlag());
        userSession.setUserType(request.getUserType());
        userSession.setFromTransactionId(request.getFromTransactionId());
        userSession.setUserId(request.getUserId());

        UserSession savedUserSession = userSessionRepository.save(userSession);
        log.info("Successfully created user session with ID: {}", savedUserSession.getId());
        return convertToResponse(savedUserSession);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getUserSessionById(Long id) {
        log.debug("Fetching user session with ID: {}", id);
        return userSessionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getUserSessionByUserId(String userId) {
        log.debug("Fetching user session for user: {}", userId);
        return userSessionRepository.findTopByUserIdOrderByCreatedAtDesc(userId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionResponseDto> getAllUserSessions(Pageable pageable) {
        log.debug("Fetching all user sessions with pagination");
        return userSessionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public UserSessionResponseDto updateUserSession(Long id, UpdateUserSessionRequestDto request) {
        log.info("Updating user session with ID: {}", id);

        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User session not found with ID: " + id));

        if (request.getTransactionId() != null) userSession.setTransactionId(request.getTransactionId());
        if (request.getProgramName() != null) userSession.setProgramName(request.getProgramName());
        if (request.getFromProgram() != null) userSession.setFromProgram(request.getFromProgram());
        if (request.getFromTransaction() != null) userSession.setFromTransaction(request.getFromTransaction());
        if (request.getProgramContext() != null) userSession.setProgramContext(request.getProgramContext());
        if (request.getReenterFlag() != null) userSession.setReenterFlag(request.getReenterFlag());
        if (request.getToProgram() != null) userSession.setToProgram(request.getToProgram());
        if (request.getProgramReenterFlag() != null) userSession.setProgramReenterFlag(request.getProgramReenterFlag());
        if (request.getUserType() != null) userSession.setUserType(request.getUserType());
        if (request.getFromTransactionId() != null) userSession.setFromTransactionId(request.getFromTransactionId());
        if (request.getUserId() != null) userSession.setUserId(request.getUserId());

        UserSession updatedUserSession = userSessionRepository.save(userSession);
        log.info("Successfully updated user session with ID: {}", id);
        return convertToResponse(updatedUserSession);
    }

    @Transactional
    public void deleteUserSession(Long id) {
        log.info("Deleting user session with ID: {}", id);

        if (!userSessionRepository.existsById(id)) {
            throw new IllegalArgumentException("User session not found with ID: " + id);
        }

        userSessionRepository.deleteById(id);
        log.info("Successfully deleted user session with ID: {}", id);
    }

    private UserSessionResponseDto convertToResponse(UserSession userSession) {
        UserSessionResponseDto response = new UserSessionResponseDto();
        response.setId(userSession.getId());
        response.setTransactionId(userSession.getTransactionId());
        response.setProgramName(userSession.getProgramName());
        response.setFromProgram(userSession.getFromProgram());
        response.setFromTransaction(userSession.getFromTransaction());
        response.setProgramContext(userSession.getProgramContext());
        response.setReenterFlag(userSession.getReenterFlag());
        response.setToProgram(userSession.getToProgram());
        response.setProgramReenterFlag(userSession.getProgramReenterFlag());
        response.setUserType(userSession.getUserType());
        response.setFromTransactionId(userSession.getFromTransactionId());
        response.setUserId(userSession.getUserId());
        response.setSessionIdentifier(userSession.getSessionIdentifier());
        response.setIsAdminUser(userSession.isAdminUser());
        response.setIsRegularUser(userSession.isRegularUser());
        response.setCreatedAt(userSession.getCreatedAt());
        response.setUpdatedAt(userSession.getUpdatedAt());
        return response;
    }
}
