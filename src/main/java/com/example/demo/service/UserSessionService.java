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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Transactional
    public UserSessionResponseDto createUserSession(CreateUserSessionRequestDto request) {
        log.info("Creating new user session with transaction ID: {}", request.getTransactionId());

        UserSession userSession = new UserSession();
        userSession.setTransactionId(request.getTransactionId());
        userSession.setProgramName(request.getProgramName());
        userSession.setFromProgram(request.getFromProgram());
        userSession.setFromTransaction(request.getFromTransaction());
        userSession.setProgramContext(request.getProgramContext());
        userSession.setReenterFlag(request.getReenterFlag() != null ? request.getReenterFlag() : false);

        UserSession savedUserSession = userSessionRepository.save(userSession);
        log.info("User session created successfully with ID: {}", savedUserSession.getId());
        return convertToResponse(savedUserSession);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getUserSessionById(Long id) {
        log.debug("Retrieving user session with ID: {}", id);
        return userSessionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getUserSessionByTransactionId(String transactionId) {
        log.debug("Retrieving user session by transaction ID: {}", transactionId);
        return userSessionRepository.findByTransactionId(transactionId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionResponseDto> getAllUserSessions(Pageable pageable) {
        log.debug("Retrieving all user sessions with pagination");
        return userSessionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public UserSessionResponseDto updateUserSession(Long id, UpdateUserSessionRequestDto request) {
        log.info("Updating user session with ID: {}", id);

        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User session not found with ID: {}", id);
                    return new IllegalArgumentException("User session not found with ID: " + id);
                });

        if (request.getTransactionId() != null) {
            userSession.setTransactionId(request.getTransactionId());
        }
        if (request.getProgramName() != null) {
            userSession.setProgramName(request.getProgramName());
        }
        if (request.getFromProgram() != null) {
            userSession.setFromProgram(request.getFromProgram());
        }
        if (request.getFromTransaction() != null) {
            userSession.setFromTransaction(request.getFromTransaction());
        }
        if (request.getProgramContext() != null) {
            userSession.setProgramContext(request.getProgramContext());
        }
        if (request.getReenterFlag() != null) {
            userSession.setReenterFlag(request.getReenterFlag());
        }

        UserSession updatedUserSession = userSessionRepository.save(userSession);
        log.info("User session updated successfully with ID: {}", updatedUserSession.getId());
        return convertToResponse(updatedUserSession);
    }

    @Transactional
    public void deleteUserSession(Long id) {
        log.info("Deleting user session with ID: {}", id);

        if (!userSessionRepository.existsById(id)) {
            log.error("User session not found with ID: {}", id);
            throw new IllegalArgumentException("User session not found with ID: " + id);
        }

        userSessionRepository.deleteById(id);
        log.info("User session deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsByProgramName(String programName) {
        log.debug("Retrieving user sessions by program name: {}", programName);

        if (programName == null || programName.trim().isEmpty()) {
            log.error("Program name cannot be null or empty");
            throw new IllegalArgumentException("Program name cannot be null or empty");
        }

        List<UserSession> sessions = userSessionRepository.findByProgramName(programName);
        log.debug("Found {} user sessions with program name: {}", sessions.size(), programName);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsByReenterFlag(Boolean reenterFlag) {
        log.debug("Retrieving user sessions by reenter flag: {}", reenterFlag);

        if (reenterFlag == null) {
            log.error("Reenter flag cannot be null");
            throw new IllegalArgumentException("Reenter flag cannot be null");
        }

        List<UserSession> sessions = userSessionRepository.findByReenterFlag(reenterFlag);
        log.debug("Found {} user sessions with reenter flag: {}", sessions.size(), reenterFlag);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsWithCallingContext() {
        log.debug("Retrieving user sessions with calling context");

        List<UserSession> sessions = userSessionRepository.findSessionsWithCallingContext();
        log.debug("Found {} user sessions with calling context", sessions.size());
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsByFromProgram(String fromProgram) {
        log.debug("Retrieving user sessions by from program: {}", fromProgram);

        List<UserSession> sessions = userSessionRepository.findByFromProgram(fromProgram);
        log.debug("Found {} user sessions with from program: {}", sessions.size(), fromProgram);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsByFromTransaction(String fromTransaction) {
        log.debug("Retrieving user sessions by from transaction: {}", fromTransaction);

        List<UserSession> sessions = userSessionRepository.findByFromTransaction(fromTransaction);
        log.debug("Found {} user sessions with from transaction: {}", sessions.size(), fromTransaction);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSessionResponseDto clearCallingContext(Long id) {
        log.info("Clearing calling context for user session with ID: {}", id);

        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User session not found with ID: {}", id);
                    return new IllegalArgumentException("User session not found with ID: " + id);
                });

        userSession.clearCallingContext();

        UserSession updatedUserSession = userSessionRepository.save(userSession);
        log.info("Calling context cleared successfully for user session with ID: {}", id);
        return convertToResponse(updatedUserSession);
    }

    @Transactional
    public UserSessionResponseDto setReenterFlag(Long id, Boolean reenterFlag) {
        log.info("Setting reenter flag to {} for user session with ID: {}", reenterFlag, id);

        if (reenterFlag == null) {
            log.error("Reenter flag cannot be null");
            throw new IllegalArgumentException("Reenter flag cannot be null");
        }

        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User session not found with ID: {}", id);
                    return new IllegalArgumentException("User session not found with ID: " + id);
                });

        userSession.setReenterFlag(reenterFlag);

        UserSession updatedUserSession = userSessionRepository.save(userSession);
        log.info("Reenter flag set successfully for user session with ID: {}", id);
        return convertToResponse(updatedUserSession);
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> getSessionsWithProgramContext() {
        log.debug("Retrieving user sessions with program context");

        List<UserSession> sessions = userSessionRepository.findSessionsWithProgramContext();
        log.debug("Found {} user sessions with program context", sessions.size());
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countSessionsByReenterFlag(Boolean reenterFlag) {
        log.debug("Counting user sessions by reenter flag: {}", reenterFlag);
        return userSessionRepository.countByReenterFlag(reenterFlag);
    }

    @Transactional(readOnly = true)
    public long countSessionsByProgramName(String programName) {
        log.debug("Counting user sessions by program name: {}", programName);
        return userSessionRepository.countByProgramName(programName);
    }

    @Transactional(readOnly = true)
    public long countSessionsWithFromProgram() {
        log.debug("Counting user sessions with from program");
        return userSessionRepository.countSessionsWithFromProgram();
    }

    @Transactional(readOnly = true)
    public long countSessionsWithProgramContext() {
        log.debug("Counting user sessions with program context");
        return userSessionRepository.countSessionsWithProgramContext();
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
        response.setCreatedAt(userSession.getCreatedAt());
        response.setUpdatedAt(userSession.getUpdatedAt());
        return response;
    }
}
