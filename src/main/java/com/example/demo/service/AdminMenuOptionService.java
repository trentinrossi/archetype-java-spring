package com.example.demo.service;

import com.example.demo.dto.CreateAdminMenuOptionRequestDto;
import com.example.demo.dto.UpdateAdminMenuOptionRequestDto;
import com.example.demo.dto.AdminMenuOptionResponseDto;
import com.example.demo.entity.AdminMenuOption;
import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminMenuOptionRepository;
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
public class AdminMenuOptionService {

    private final AdminMenuOptionRepository adminMenuOptionRepository;
    private final AdminUserRepository adminUserRepository;

    @Transactional
    public AdminMenuOptionResponseDto createAdminMenuOption(CreateAdminMenuOptionRequestDto request) {
        log.info("Creating new admin menu option with option number: {}", request.getOptionNumber());

        if (adminMenuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
            throw new IllegalArgumentException("Admin menu option with option number " + request.getOptionNumber() + " already exists");
        }

        AdminUser adminUser = adminUserRepository.findById(request.getAdminUserId())
                .orElseThrow(() -> new IllegalArgumentException("Admin user with ID " + request.getAdminUserId() + " not found"));

        AdminMenuOption adminMenuOption = new AdminMenuOption();
        adminMenuOption.setOptionNumber(request.getOptionNumber());
        adminMenuOption.setOptionName(request.getOptionName());
        adminMenuOption.setProgramName(request.getProgramName());
        adminMenuOption.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        adminMenuOption.setAdminUser(adminUser);

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
        return adminMenuOptionRepository.findByOptionNumber(optionNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAdminMenuOptionsByProgramName(String programName) {
        log.debug("Fetching admin menu options with program name: {}", programName);
        return adminMenuOptionRepository.findByProgramName(programName)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getActiveAdminMenuOptions() {
        log.debug("Fetching all active admin menu options");
        return adminMenuOptionRepository.findByIsActiveTrueOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getInactiveAdminMenuOptions() {
        log.debug("Fetching all inactive admin menu options");
        return adminMenuOptionRepository.findByIsActiveFalseOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAdminMenuOptionsByActiveStatus(Boolean isActive) {
        log.debug("Fetching admin menu options with active status: {}", isActive);
        return adminMenuOptionRepository.findByIsActive(isActive)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAdminMenuOptionsByAdminUser(String adminUserId) {
        log.debug("Fetching admin menu options for admin user: {}", adminUserId);
        AdminUser adminUser = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user with ID " + adminUserId + " not found"));
        
        return adminMenuOptionRepository.findByAdminUser(adminUser)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getActiveAdminMenuOptionsByAdminUser(String adminUserId) {
        log.debug("Fetching active admin menu options for admin user: {}", adminUserId);
        AdminUser adminUser = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user with ID " + adminUserId + " not found"));
        
        return adminMenuOptionRepository.findByAdminUserAndIsActive(adminUser, true)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> searchAdminMenuOptionsByName(String optionName) {
        log.debug("Searching admin menu options with name containing: {}", optionName);
        return adminMenuOptionRepository.findByOptionNameContaining(optionName)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAdminMenuOptionsInRange(Integer startOptionNumber, Integer endOptionNumber) {
        log.debug("Fetching admin menu options in range: {} to {}", startOptionNumber, endOptionNumber);
        
        if (startOptionNumber > endOptionNumber) {
            throw new IllegalArgumentException("Start option number must be less than or equal to end option number");
        }
        
        return adminMenuOptionRepository.findByOptionNumberRange(startOptionNumber, endOptionNumber)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AdminMenuOptionResponseDto> getAllAdminMenuOptions(Pageable pageable) {
        log.debug("Fetching all admin menu options with pagination");
        return adminMenuOptionRepository.findAllByOrderByOptionNumberAsc(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<AdminMenuOptionResponseDto> getAllAdminMenuOptionsSorted() {
        log.debug("Fetching all admin menu options sorted by option number");
        return adminMenuOptionRepository.findAllByOrderByOptionNumberAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminMenuOptionResponseDto updateAdminMenuOption(Long id, UpdateAdminMenuOptionRequestDto request) {
        log.info("Updating admin menu option with ID: {}", id);
        
        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin menu option with ID " + id + " not found"));

        if (request.getOptionNumber() != null && !request.getOptionNumber().equals(adminMenuOption.getOptionNumber())) {
            if (adminMenuOptionRepository.existsByOptionNumber(request.getOptionNumber())) {
                throw new IllegalArgumentException("Admin menu option with option number " + request.getOptionNumber() + " already exists");
            }
            adminMenuOption.setOptionNumber(request.getOptionNumber());
        }

        if (request.getOptionName() != null) {
            adminMenuOption.setOptionName(request.getOptionName());
        }

        if (request.getProgramName() != null) {
            adminMenuOption.setProgramName(request.getProgramName());
        }

        if (request.getIsActive() != null) {
            adminMenuOption.setIsActive(request.getIsActive());
        }

        if (request.getAdminUserId() != null) {
            AdminUser adminUser = adminUserRepository.findById(request.getAdminUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin user with ID " + request.getAdminUserId() + " not found"));
            adminMenuOption.setAdminUser(adminUser);
        }

        AdminMenuOption updatedAdminMenuOption = adminMenuOptionRepository.save(adminMenuOption);
        log.info("Successfully updated admin menu option with ID: {}", id);
        return convertToResponse(updatedAdminMenuOption);
    }

    @Transactional
    public AdminMenuOptionResponseDto activateAdminMenuOption(Long id) {
        log.info("Activating admin menu option with ID: {}", id);
        
        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin menu option with ID " + id + " not found"));

        if (adminMenuOption.getIsActive()) {
            log.warn("Admin menu option with ID {} is already active", id);
        }

        adminMenuOption.setIsActive(true);
        AdminMenuOption updatedAdminMenuOption = adminMenuOptionRepository.save(adminMenuOption);
        log.info("Successfully activated admin menu option with ID: {}", id);
        return convertToResponse(updatedAdminMenuOption);
    }

    @Transactional
    public AdminMenuOptionResponseDto deactivateAdminMenuOption(Long id) {
        log.info("Deactivating admin menu option with ID: {}", id);
        
        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin menu option with ID " + id + " not found"));

        if (!adminMenuOption.getIsActive()) {
            log.warn("Admin menu option with ID {} is already inactive", id);
        }

        adminMenuOption.setIsActive(false);
        AdminMenuOption updatedAdminMenuOption = adminMenuOptionRepository.save(adminMenuOption);
        log.info("Successfully deactivated admin menu option with ID: {}", id);
        return convertToResponse(updatedAdminMenuOption);
    }

    @Transactional
    public void deleteAdminMenuOption(Long id) {
        log.info("Deleting admin menu option with ID: {}", id);
        
        if (!adminMenuOptionRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin menu option with ID " + id + " not found");
        }
        
        adminMenuOptionRepository.deleteById(id);
        log.info("Successfully deleted admin menu option with ID: {}", id);
    }

    @Transactional
    public void deleteAdminMenuOptionByOptionNumber(Integer optionNumber) {
        log.info("Deleting admin menu option with option number: {}", optionNumber);
        
        AdminMenuOption adminMenuOption = adminMenuOptionRepository.findByOptionNumber(optionNumber)
                .orElseThrow(() -> new IllegalArgumentException("Admin menu option with option number " + optionNumber + " not found"));
        
        adminMenuOptionRepository.delete(adminMenuOption);
        log.info("Successfully deleted admin menu option with option number: {}", optionNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByOptionNumber(Integer optionNumber) {
        return adminMenuOptionRepository.existsByOptionNumber(optionNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByProgramName(String programName) {
        return adminMenuOptionRepository.existsByProgramName(programName);
    }

    @Transactional(readOnly = true)
    public long countActiveAdminMenuOptions() {
        log.debug("Counting active admin menu options");
        return adminMenuOptionRepository.countActiveOptions();
    }

    @Transactional(readOnly = true)
    public long countInactiveAdminMenuOptions() {
        log.debug("Counting inactive admin menu options");
        return adminMenuOptionRepository.countInactiveOptions();
    }

    @Transactional(readOnly = true)
    public long countAllAdminMenuOptions() {
        log.debug("Counting all admin menu options");
        return adminMenuOptionRepository.count();
    }

    private AdminMenuOptionResponseDto convertToResponse(AdminMenuOption adminMenuOption) {
        AdminMenuOptionResponseDto response = new AdminMenuOptionResponseDto();
        response.setId(adminMenuOption.getId());
        response.setOptionNumber(adminMenuOption.getOptionNumber());
        response.setOptionName(adminMenuOption.getOptionName());
        response.setProgramName(adminMenuOption.getProgramName());
        response.setIsActive(adminMenuOption.getIsActive());
        response.setStatusDisplay(adminMenuOption.getStatusLabel());
        response.setOptionDisplay(adminMenuOption.getDisplayText());
        if (adminMenuOption.getAdminUser() != null) {
            response.setAdminUserId(adminMenuOption.getAdminUser().getUserId());
        }
        response.setCreatedAt(adminMenuOption.getCreatedAt());
        response.setUpdatedAt(adminMenuOption.getUpdatedAt());
        return response;
    }
}
