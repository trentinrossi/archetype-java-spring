package com.example.demo.service;

import com.example.demo.dto.CreateAdminMenuOptionRequestDto;
import com.example.demo.dto.UpdateAdminMenuOptionRequestDto;
import com.example.demo.dto.AdminMenuOptionResponseDto;
import com.example.demo.entity.AdminMenuOption;
import com.example.demo.repository.AdminMenuOptionRepository;
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
public class AdminMenuOptionService {

    private final AdminMenuOptionRepository adminMenuOptionRepository;

    @Transactional
    public AdminMenuOptionResponseDto createAdminMenuOption(CreateAdminMenuOptionRequestDto request) {
        log.info("Creating new admin menu option with option number: {}", request.getOptionNumber());

        validateOptionNumber(request.getOptionNumber());
        validateOptionName(request.getOptionName());
        validateProgramName(request.getProgramName());

        if (adminMenuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
            log.error("Admin menu option with option number {} already exists", request.getOptionNumber());
            throw new IllegalArgumentException("Admin menu option with option number already exists");
        }

        AdminMenuOption adminMenuOption = new AdminMenuOption();
        adminMenuOption.setOptionNumber(request.getOptionNumber());
        adminMenuOption.setOptionName(request.getOptionName());
        adminMenuOption.setProgramName(request.getProgramName());
        adminMenuOption.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        AdminMenuOption savedAdminMenuOption = adminMenuOptionRepository.save(adminMenuOption);
        log.info("Successfully created admin menu option with ID: {}", savedAdminMenuOption.getId());

        return convertToResponse(savedAdminMenuOption);
    }

    @Transactional(readOnly = true)
    public Optional<AdminMenuOptionResponseDto> getAdminMenuOptionById(Long id) {
        log.debug("Fetching admin menu option with ID: {}", id);
        return adminMenuOptionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<AdminMenuOptionResponseDto> getAdminMenuOptionByOptionNumber(Integer optionNumber) {
        log.debug("Fetching admin menu option with option number: {}", optionNumber);
        validateOptionNumber(optionNumber);
        return adminMenuOptionRepository.findByOptionNumber(optionNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAllAdminMenuOptions() {
        log.debug("Fetching all admin menu options");
        return adminMenuOptionRepository.findAllByOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAllActiveAdminMenuOptions() {
        log.debug("Fetching all active admin menu options");
        return adminMenuOptionRepository.findByIsActiveTrueOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AdminMenuOptionResponseDto> getAllAdminMenuOptions(Pageable pageable) {
        log.debug("Fetching admin menu options with pagination");
        return adminMenuOptionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public AdminMenuOptionResponseDto updateAdminMenuOption(Long id, UpdateAdminMenuOptionRequestDto request) {
        log.info("Updating admin menu option with ID: {}", id);

        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Admin menu option with ID {} not found", id);
                    return new IllegalArgumentException("Admin menu option not found with ID: " + id);
                });

        if (request.getOptionNumber() != null) {
            validateOptionNumber(request.getOptionNumber());
            if (!request.getOptionNumber().equals(adminMenuOption.getOptionNumber()) &&
                    adminMenuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
                log.error("Admin menu option with option number {} already exists", request.getOptionNumber());
                throw new IllegalArgumentException("Admin menu option with option number already exists");
            }
            adminMenuOption.setOptionNumber(request.getOptionNumber());
        }

        if (request.getOptionName() != null) {
            validateOptionName(request.getOptionName());
            adminMenuOption.setOptionName(request.getOptionName());
        }

        if (request.getProgramName() != null) {
            validateProgramName(request.getProgramName());
            adminMenuOption.setProgramName(request.getProgramName());
        }

        if (request.getIsActive() != null) {
            adminMenuOption.setIsActive(request.getIsActive());
        }

        AdminMenuOption updatedAdminMenuOption = adminMenuOptionRepository.save(adminMenuOption);
        log.info("Successfully updated admin menu option with ID: {}", id);

        return convertToResponse(updatedAdminMenuOption);
    }

    @Transactional
    public void deleteAdminMenuOption(Long id) {
        log.info("Deleting admin menu option with ID: {}", id);

        if (!adminMenuOptionRepository.existsById(id)) {
            log.error("Admin menu option with ID {} not found", id);
            throw new IllegalArgumentException("Admin menu option not found with ID: " + id);
        }

        adminMenuOptionRepository.deleteById(id);
        log.info("Successfully deleted admin menu option with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public void validateMenuOptionForExecution(Integer optionNumber) {
        log.debug("Validating menu option {} for execution", optionNumber);

        validateOptionNumber(optionNumber);

        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findByOptionNumber(optionNumber)
                .orElseThrow(() -> {
                    log.error("Invalid menu option number: {}", optionNumber);
                    return new IllegalArgumentException("Invalid menu option number: " + optionNumber);
                });

        if (!adminMenuOption.getIsActive()) {
            log.warn("Menu option {} is not active (coming soon)", optionNumber);
            throw new IllegalStateException("This feature is coming soon. Menu option " + optionNumber + " is not yet available.");
        }

        log.debug("Menu option {} is valid and active", optionNumber);
    }

    @Transactional(readOnly = true)
    public boolean isMenuOptionActive(Integer optionNumber) {
        log.debug("Checking if menu option {} is active", optionNumber);
        validateOptionNumber(optionNumber);
        return adminMenuOptionRepository.findByOptionNumber(optionNumber)
                .map(AdminMenuOption::getIsActive)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public String getProgramNameForOption(Integer optionNumber) {
        log.debug("Getting program name for menu option {}", optionNumber);

        validateMenuOptionForExecution(optionNumber);

        return adminMenuOptionRepository.findByOptionNumber(optionNumber)
                .map(AdminMenuOption::getProgramName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu option number: " + optionNumber));
    }

    private void validateOptionNumber(Integer optionNumber) {
        if (optionNumber == null) {
            log.error("Option number cannot be null");
            throw new IllegalArgumentException("Option number is required");
        }

        if (optionNumber < 0 || optionNumber > 99) {
            log.error("Option number {} is out of valid range (0-99)", optionNumber);
            throw new IllegalArgumentException("Option number must be between 0 and 99");
        }
    }

    private void validateOptionName(String optionName) {
        if (optionName == null || optionName.trim().isEmpty()) {
            log.error("Option name cannot be null or empty");
            throw new IllegalArgumentException("Option name is required");
        }

        if (optionName.length() > 35) {
            log.error("Option name exceeds maximum length of 35 characters");
            throw new IllegalArgumentException("Option name must not exceed 35 characters");
        }
    }

    private void validateProgramName(String programName) {
        if (programName == null || programName.trim().isEmpty()) {
            log.error("Program name cannot be null or empty");
            throw new IllegalArgumentException("Program name is required");
        }

        if (programName.length() > 8) {
            log.error("Program name exceeds maximum length of 8 characters");
            throw new IllegalArgumentException("Program name must not exceed 8 characters");
        }
    }

    private AdminMenuOptionResponseDto convertToResponse(AdminMenuOption adminMenuOption) {
        AdminMenuOptionResponseDto response = new AdminMenuOptionResponseDto();
        response.setId(adminMenuOption.getId());
        response.setOptionNumber(adminMenuOption.getOptionNumber());
        response.setOptionName(adminMenuOption.getOptionName());
        response.setProgramName(adminMenuOption.getProgramName());
        response.setIsActive(adminMenuOption.getIsActive());
        response.setIsComingSoon(adminMenuOption.isComingSoon());
        response.setDisplayText(adminMenuOption.getDisplayText());
        response.setCanExecute(adminMenuOption.canExecute());
        response.setCreatedAt(adminMenuOption.getCreatedAt());
        response.setUpdatedAt(adminMenuOption.getUpdatedAt());
        return response;
    }
}
