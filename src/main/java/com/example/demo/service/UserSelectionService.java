package com.example.demo.service;

import com.example.demo.dto.CreateUserSelectionRequestDto;
import com.example.demo.dto.UpdateUserSelectionRequestDto;
import com.example.demo.dto.UserSelectionResponseDto;
import com.example.demo.entity.UserSelection;
import com.example.demo.repository.UserSelectionRepository;
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
public class UserSelectionService {

    private final UserSelectionRepository userSelectionRepository;

    @Transactional
    public UserSelectionResponseDto createUserSelection(CreateUserSelectionRequestDto request) {
        log.info("Creating new user selection for user: {}", request.getSelectedUserId());

        UserSelection userSelection = new UserSelection();
        userSelection.setSelectionFlag(request.getSelectionFlag());
        userSelection.setSelectedUserId(request.getSelectedUserId());

        UserSelection savedUserSelection = userSelectionRepository.save(userSelection);
        log.info("Successfully created user selection with ID: {}", savedUserSelection.getId());
        return convertToResponse(savedUserSelection);
    }

    @Transactional(readOnly = true)
    public Optional<UserSelectionResponseDto> getUserSelectionById(Long id) {
        log.debug("Fetching user selection with ID: {}", id);
        return userSelectionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserSelectionResponseDto> getUserSelectionByUserId(String userId) {
        log.debug("Fetching user selection for user: {}", userId);
        return userSelectionRepository.findBySelectedUserId(userId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserSelectionResponseDto> getAllUserSelections(Pageable pageable) {
        log.debug("Fetching all user selections with pagination");
        return userSelectionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<UserSelectionResponseDto> getUpdateSelections() {
        log.debug("Fetching all update selections");
        return userSelectionRepository.findUpdateSelections()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSelectionResponseDto> getDeleteSelections() {
        log.debug("Fetching all delete selections");
        return userSelectionRepository.findDeleteSelections()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSelectionResponseDto updateUserSelection(Long id, UpdateUserSelectionRequestDto request) {
        log.info("Updating user selection with ID: {}", id);

        UserSelection userSelection = userSelectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User selection not found with ID: " + id));

        if (request.getSelectionFlag() != null) userSelection.setSelectionFlag(request.getSelectionFlag());
        if (request.getSelectedUserId() != null) userSelection.setSelectedUserId(request.getSelectedUserId());

        UserSelection updatedUserSelection = userSelectionRepository.save(userSelection);
        log.info("Successfully updated user selection with ID: {}", id);
        return convertToResponse(updatedUserSelection);
    }

    @Transactional
    public void deleteUserSelection(Long id) {
        log.info("Deleting user selection with ID: {}", id);

        if (!userSelectionRepository.existsById(id)) {
            throw new IllegalArgumentException("User selection not found with ID: " + id);
        }

        userSelectionRepository.deleteById(id);
        log.info("Successfully deleted user selection with ID: {}", id);
    }

    private UserSelectionResponseDto convertToResponse(UserSelection userSelection) {
        UserSelectionResponseDto response = new UserSelectionResponseDto();
        response.setId(userSelection.getId());
        response.setSelectionFlag(userSelection.getSelectionFlag());
        response.setSelectedUserId(userSelection.getSelectedUserId());
        response.setIsUpdateSelection(userSelection.isUpdateSelection());
        response.setIsDeleteSelection(userSelection.isDeleteSelection());
        response.setSelectionType(userSelection.getSelectionType());
        response.setCreatedAt(userSelection.getCreatedAt());
        response.setUpdatedAt(userSelection.getUpdatedAt());
        return response;
    }
}
