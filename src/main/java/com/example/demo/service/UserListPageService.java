package com.example.demo.service;

import com.example.demo.dto.CreateUserListPageRequestDto;
import com.example.demo.dto.UpdateUserListPageRequestDto;
import com.example.demo.dto.UserListPageResponseDto;
import com.example.demo.entity.UserListPage;
import com.example.demo.repository.UserListPageRepository;
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
public class UserListPageService {

    private final UserListPageRepository userListPageRepository;

    @Transactional
    public UserListPageResponseDto createUserListPage(CreateUserListPageRequestDto request) {
        log.info("Creating new user list page with page number: {}", request.getPageNumber());

        if (userListPageRepository.existsByPageNumber(request.getPageNumber())) {
            throw new IllegalArgumentException("User list page with page number already exists");
        }

        UserListPage userListPage = new UserListPage();
        userListPage.setPageNumber(request.getPageNumber());
        userListPage.setFirstUserId(request.getFirstUserId());
        userListPage.setLastUserId(request.getLastUserId());
        userListPage.setHasNextPage(request.getHasNextPage());
        userListPage.setRecordsPerPage(request.getRecordsPerPage() != null ? request.getRecordsPerPage() : 10);

        UserListPage savedUserListPage = userListPageRepository.save(userListPage);
        log.info("Successfully created user list page with ID: {}", savedUserListPage.getId());
        return convertToResponse(savedUserListPage);
    }

    @Transactional(readOnly = true)
    public Optional<UserListPageResponseDto> getUserListPageById(Long id) {
        log.debug("Fetching user list page with ID: {}", id);
        return userListPageRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserListPageResponseDto> getUserListPageByPageNumber(Long pageNumber) {
        log.debug("Fetching user list page with page number: {}", pageNumber);
        return userListPageRepository.findByPageNumber(pageNumber).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserListPageResponseDto> getAllUserListPages(Pageable pageable) {
        log.debug("Fetching all user list pages with pagination");
        return userListPageRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public UserListPageResponseDto updateUserListPage(Long id, UpdateUserListPageRequestDto request) {
        log.info("Updating user list page with ID: {}", id);

        UserListPage userListPage = userListPageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User list page not found with ID: " + id));

        if (request.getPageNumber() != null) userListPage.setPageNumber(request.getPageNumber());
        if (request.getFirstUserId() != null) userListPage.setFirstUserId(request.getFirstUserId());
        if (request.getLastUserId() != null) userListPage.setLastUserId(request.getLastUserId());
        if (request.getHasNextPage() != null) userListPage.setHasNextPage(request.getHasNextPage());
        if (request.getRecordsPerPage() != null) userListPage.setRecordsPerPage(request.getRecordsPerPage());

        UserListPage updatedUserListPage = userListPageRepository.save(userListPage);
        log.info("Successfully updated user list page with ID: {}", id);
        return convertToResponse(updatedUserListPage);
    }

    @Transactional
    public void deleteUserListPage(Long id) {
        log.info("Deleting user list page with ID: {}", id);

        if (!userListPageRepository.existsById(id)) {
            throw new IllegalArgumentException("User list page not found with ID: " + id);
        }

        userListPageRepository.deleteById(id);
        log.info("Successfully deleted user list page with ID: {}", id);
    }

    private UserListPageResponseDto convertToResponse(UserListPage userListPage) {
        UserListPageResponseDto response = new UserListPageResponseDto();
        response.setId(userListPage.getId());
        response.setPageNumber(userListPage.getPageNumber());
        response.setFirstUserId(userListPage.getFirstUserId());
        response.setLastUserId(userListPage.getLastUserId());
        response.setHasNextPage(userListPage.getHasNextPage());
        response.setRecordsPerPage(userListPage.getRecordsPerPage());
        response.setHasPreviousPage(userListPage.hasPreviousPage());
        response.setNextPageNumber(userListPage.getNextPageNumber());
        response.setPreviousPageNumber(userListPage.getPreviousPageNumber());
        response.setIsEmpty(userListPage.isEmpty());
        response.setRecordCount(userListPage.getRecordCount());
        response.setCreatedAt(userListPage.getCreatedAt());
        response.setUpdatedAt(userListPage.getUpdatedAt());
        return response;
    }
}
