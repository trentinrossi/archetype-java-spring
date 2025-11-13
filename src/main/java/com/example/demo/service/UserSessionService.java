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
    public UserSessionResponseDto createSession(CreateUserSessionRequestDto request) {
        log.info("Creating new session for user: {}", request.getUserId());

        UserSession session = new UserSession();
        session.setTransactionId(request.getTransactionId());
        session.setProgramName(request.getProgramName());
        session.setFromProgram(request.getFromProgram());
        session.setFromTransaction(request.getFromTransaction());
        session.setProgramContext(request.getProgramContext());
        session.setReenterFlag(request.getReenterFlag());
        session.setToProgram(request.getToProgram());
        session.setProgramReenterFlag(request.getProgramReenterFlag());
        session.setUserType(request.getUserType());
        session.setFromTransactionId(request.getFromTransactionId());
        session.setUserId(request.getUserId());

        UserSession savedSession = userSessionRepository.save(session);
        log.info("Session created successfully for user: {}", savedSession.getUserId());
        return convertToResponse(savedSession);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getSessionById(Long id) {
        log.info("Fetching session by id: {}", id);
        return userSessionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserSessionResponseDto> getLatestSessionByUserId(String userId) {
        log.info("Fetching latest session for user: {}", userId);
        return userSessionRepository.findTopByUserIdOrderByIdDesc(userId).map(this::convertToResponse);
    }

    @Transactional
    public UserSessionResponseDto updateSession(Long id, UpdateUserSessionRequestDto request) {
        log.info("Updating session with id: {}", id);

        UserSession session = userSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Session not found: {}", id);
                    return new IllegalArgumentException("Session not found");
                });

        if (request.getTransactionId() != null) {
            session.setTransactionId(request.getTransactionId());
        }
        if (request.getProgramName() != null) {
            session.setProgramName(request.getProgramName());
        }
        if (request.getFromProgram() != null) {
            session.setFromProgram(request.getFromProgram());
        }
        if (request.getFromTransaction() != null) {
            session.setFromTransaction(request.getFromTransaction());
        }
        if (request.getProgramContext() != null) {
            session.setProgramContext(request.getProgramContext());
        }
        if (request.getReenterFlag() != null) {
            session.setReenterFlag(request.getReenterFlag());
        }
        if (request.getToProgram() != null) {
            session.setToProgram(request.getToProgram());
        }
        if (request.getProgramReenterFlag() != null) {
            session.setProgramReenterFlag(request.getProgramReenterFlag());
        }
        if (request.getUserType() != null) {
            session.setUserType(request.getUserType());
        }
        if (request.getFromTransactionId() != null) {
            session.setFromTransactionId(request.getFromTransactionId());
        }
        if (request.getUserId() != null) {
            session.setUserId(request.getUserId());
        }

        UserSession updatedSession = userSessionRepository.save(session);
        log.info("Session updated successfully: {}", updatedSession.getId());
        return convertToResponse(updatedSession);
    }

    @Transactional
    public void deleteSession(Long id) {
        log.info("Deleting session with id: {}", id);

        if (!userSessionRepository.existsById(id)) {
            log.error("Session not found: {}", id);
            throw new IllegalArgumentException("Session not found");
        }

        userSessionRepository.deleteById(id);
        log.info("Session deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionResponseDto> getAllSessions(Pageable pageable) {
        log.info("Fetching all sessions - page: {}", pageable.getPageNumber());
        return userSessionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionResponseDto> getSessionsByUserId(String userId, Pageable pageable) {
        log.info("Fetching sessions for user: {} - page: {}", userId, pageable.getPageNumber());
        return userSessionRepository.findByUserId(userId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionResponseDto> getSessionsByUserType(String userType, Pageable pageable) {
        log.info("Fetching sessions for user type: {} - page: {}", userType, pageable.getPageNumber());
        return userSessionRepository.findByUserType(userType, pageable).map(this::convertToResponse);
    }

    @Transactional
    public UserSessionResponseDto transferToProgram(Long sessionId, String targetProgram, String targetTransaction) {
        log.info("Transferring session {} to program: {}", sessionId, targetProgram);

        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.error("Session not found: {}", sessionId);
                    return new IllegalArgumentException("Session not found");
                });

        session.transferToProgram(targetProgram, targetTransaction);
        UserSession updatedSession = userSessionRepository.save(session);
        log.info("Session transferred successfully to program: {}", targetProgram);
        return convertToResponse(updatedSession);
    }

    private UserSessionResponseDto convertToResponse(UserSession session) {
        UserSessionResponseDto response = new UserSessionResponseDto();
        response.setId(session.getId());
        response.setTransactionId(session.getTransactionId());
        response.setProgramName(session.getProgramName());
        response.setFromProgram(session.getFromProgram());
        response.setFromTransaction(session.getFromTransaction());
        response.setProgramContext(session.getProgramContext());
        response.setReenterFlag(session.getReenterFlag());
        response.setToProgram(session.getToProgram());
        response.setProgramReenterFlag(session.getProgramReenterFlag());
        response.setUserType(session.getUserType());
        response.setFromTransactionId(session.getFromTransactionId());
        response.setUserId(session.getUserId());
        response.setSessionContext(session.getSessionContext());
        response.setIsAdminUser(session.isAdminUser());
        response.setIsReentering(session.isReentering());
        response.setIsProgramReentering(session.isProgramReentering());
        response.setHasCallingProgram(session.hasCallingProgram());
        response.setHasContext(session.hasContext());
        response.setCreatedAt(session.getCreatedAt());
        response.setUpdatedAt(session.getUpdatedAt());
        return response;
    }
}
