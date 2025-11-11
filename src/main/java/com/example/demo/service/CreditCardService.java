package com.example.demo.service;

import com.example.demo.dto.CreateCreditCardRequestDto;
import com.example.demo.dto.CreditCardFilterRequestDto;
import com.example.demo.dto.CreditCardResponseDto;
import com.example.demo.dto.UpdateCreditCardRequestDto;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.User;
import com.example.demo.enums.CardStatus;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * CreditCardService
 * 
 * Business logic layer for CreditCard entity.
 * Implements comprehensive credit card management with filtering, pagination, and access control.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR002: Card Number Filter Validation
 * - BR003: Single Selection Enforcement
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR006: Filter Record Matching
 * - BR008: First Page Navigation Restriction
 * - BR009: Last Page Navigation Restriction
 * - BR011: Exit to Menu
 * - BR012: View Card Details
 * - BR013: Update Card Information
 * - BR014: Forward Pagination
 * - BR015: Backward Pagination
 * - BR017: Input Error Protection
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    /**
     * Creates a new credit card
     * Implements BR002, BR004, BR005: Field Validations
     * 
     * @param request The create credit card request
     * @return The created credit card response
     */
    @Transactional
    public CreditCardResponseDto createCreditCard(CreateCreditCardRequestDto request) {
        log.info("Creating new credit card with card number: {}", request.getCardNumber());

        // BR002: Validate card number
        validateCardNumber(request.getCardNumber());
        
        // BR004: Validate account ID
        validateAccountId(request.getAccountId());
        
        // BR005: Validate card status
        validateCardStatus(request.getCardStatus());

        // Check if card already exists
        if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Credit card with this card number already exists");
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setAccountId(request.getAccountId());
        creditCard.setCardStatus(request.getCardStatus().charAt(0));

        CreditCard savedCreditCard = creditCardRepository.save(creditCard);
        log.info("Credit card created successfully with card number: {}", savedCreditCard.getCardNumber());
        
        return convertToResponse(savedCreditCard);
    }

    /**
     * Retrieves a credit card by card number
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number
     * @return Optional containing the credit card response if found
     */
    @Transactional(readOnly = true)
    public Optional<CreditCardResponseDto> getCreditCardByCardNumber(String cardNumber) {
        log.info("Retrieving credit card with card number: {}", cardNumber);
        validateCardNumber(cardNumber);
        return creditCardRepository.findByCardNumber(cardNumber).map(this::convertToResponse);
    }

    /**
     * Retrieves all credit cards with pagination
     * Implements BR014, BR015: Pagination
     * 
     * @param pageable Pagination information
     * @return Page of credit card responses
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getAllCreditCards(Pageable pageable) {
        log.info("Retrieving all credit cards with pagination");
        return creditCardRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * Updates an existing credit card
     * Implements BR005, BR013: Card Status Validation and Update Card Information
     * 
     * @param cardNumber The card number to update
     * @param request The update credit card request
     * @return The updated credit card response
     */
    @Transactional
    public CreditCardResponseDto updateCreditCard(String cardNumber, UpdateCreditCardRequestDto request) {
        log.info("Updating credit card with card number: {}", cardNumber);

        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit card not found with card number: " + cardNumber));

        // BR013: Update Card Information
        if (request.getCardStatus() != null) {
            validateCardStatus(request.getCardStatus());
            creditCard.setCardStatus(request.getCardStatus().charAt(0));
        }

        CreditCard updatedCreditCard = creditCardRepository.save(creditCard);
        log.info("Credit card updated successfully with card number: {}", updatedCreditCard.getCardNumber());
        
        return convertToResponse(updatedCreditCard);
    }

    /**
     * Deletes a credit card
     * 
     * @param cardNumber The card number to delete
     */
    @Transactional
    public void deleteCreditCard(String cardNumber) {
        log.info("Deleting credit card with card number: {}", cardNumber);

        if (!creditCardRepository.existsByCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Credit card not found with card number: " + cardNumber);
        }

        creditCardRepository.deleteById(cardNumber);
        log.info("Credit card deleted successfully with card number: {}", cardNumber);
    }

    /**
     * Filters credit cards based on user permissions and filter criteria
     * Implements BR001: User Permission Based Card Access
     * Implements BR006: Filter Record Matching
     * Implements BR014, BR015: Pagination
     * 
     * @param userId The user ID requesting the cards
     * @param filter The filter criteria
     * @param pageable Pagination information
     * @return Page of credit cards matching all criteria
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> filterCreditCards(String userId, CreditCardFilterRequestDto filter, Pageable pageable) {
        log.info("Filtering credit cards for user: {} with filter: {}", userId, filter);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Validate filters if supplied
        if (filter.getCardNumber() != null && !filter.isCardNumberBlank()) {
            validateCardNumberFilter(filter.getCardNumber());
        }
        
        if (filter.getAccountId() != null && !filter.isAccountIdBlank()) {
            validateAccountIdFilter(filter.getAccountId());
        }
        
        if (filter.getCardStatus() != null && !filter.isCardStatusBlank()) {
            validateCardStatusFilter(filter.getCardStatus());
        }

        // BR001: Apply user permission-based access
        Page<CreditCard> creditCards = applyUserPermissionBasedAccess(user, filter, pageable);

        if (creditCards.isEmpty()) {
            log.info("NO RECORDS FOUND");
        }

        return creditCards.map(this::convertToResponse);
    }

    /**
     * Validates single selection for view or update operations
     * Implements BR003: Single Selection Enforcement
     * 
     * @param selectionCount Number of cards selected
     * @throws IllegalArgumentException if more than one card is selected
     */
    public void validateSingleSelection(int selectionCount) {
        log.debug("Validating single selection: count={}", selectionCount);
        
        // BR003: Only one card can be selected at a time
        if (selectionCount > 1) {
            log.error("Multiple selections detected: {}", selectionCount);
            throw new IllegalArgumentException("Only one credit card can be selected at a time for view or update operations");
        }
        
        if (selectionCount == 0) {
            throw new IllegalArgumentException("No credit card selected");
        }
    }

    /**
     * Validates navigation to previous page
     * Implements BR008: First Page Navigation Restriction
     * 
     * @param pageable Current pagination information
     * @throws IllegalStateException if already on first page
     */
    public void validateBackwardNavigation(Pageable pageable) {
        log.debug("Validating backward navigation: currentPage={}", pageable.getPageNumber());
        
        // BR008: Cannot navigate to previous pages when on first page
        if (pageable.getPageNumber() == 0) {
            log.warn("Attempted to navigate before first page");
            throw new IllegalStateException("NO PREVIOUS PAGES TO DISPLAY");
        }
    }

    /**
     * Validates navigation to next page
     * Implements BR009: Last Page Navigation Restriction
     * 
     * @param page Current page information
     * @throws IllegalStateException if already on last page
     */
    public void validateForwardNavigation(Page<?> page) {
        log.debug("Validating forward navigation: isLast={}", page.isLast());
        
        // BR009: Cannot navigate to next pages when on last page
        if (page.isLast()) {
            log.warn("Attempted to navigate beyond last page");
            throw new IllegalStateException("NO MORE PAGES TO DISPLAY");
        }
    }

    /**
     * Checks if row selection should be protected due to validation errors
     * Implements BR017: Input Error Protection
     * 
     * @param hasValidationErrors Whether validation errors exist
     * @return true if row selection should be protected, false otherwise
     */
    public boolean shouldProtectRowSelection(boolean hasValidationErrors) {
        log.debug("Checking if row selection should be protected: hasValidationErrors={}", hasValidationErrors);
        
        // BR017: Protect row selection fields when input validation errors occur
        return hasValidationErrors;
    }

    /**
     * Gets the target program for viewing card details
     * Implements BR012: View Card Details
     * 
     * @return Target program name
     */
    public String getViewDetailsTargetProgram() {
        // BR012: Navigate to card detail view program
        return "COCRDSLC";
    }

    /**
     * Gets the target program for updating card information
     * Implements BR013: Update Card Information
     * 
     * @return Target program name
     */
    public String getUpdateCardTargetProgram() {
        // BR013: Navigate to card update program
        return "COCRDUPC";
    }

    /**
     * Gets the target program for exiting to menu
     * Implements BR011: Exit to Menu
     * 
     * @return Target program name
     */
    public String getExitMenuTargetProgram() {
        // BR011: Return to main menu program
        return "COMEN01C";
    }

    /**
     * Applies user permission-based access control
     * Implements BR001: User Permission Based Card Access
     * Implements BR006: Filter Record Matching
     * 
     * @param user The user requesting access
     * @param filter The filter criteria
     * @param pageable Pagination information
     * @return Page of credit cards accessible by the user
     */
    private Page<CreditCard> applyUserPermissionBasedAccess(User user, CreditCardFilterRequestDto filter, Pageable pageable) {
        String cardNumber = filter.isCardNumberBlank() ? null : filter.getCardNumber();
        String accountId = filter.isAccountIdBlank() ? null : filter.getAccountId();
        Character cardStatus = filter.isCardStatusBlank() ? null : filter.getCardStatus().charAt(0);

        // BR001: Admin users can view all credit cards without context
        if (user.isAdmin()) {
            log.info("Admin user - applying filters to all cards");
            return creditCardRepository.findByFilters(cardNumber, accountId, cardStatus, pageable);
        }

        // BR001: Regular users can only view cards associated with their specific account
        log.info("Regular user - filtering by accessible accounts");
        List<String> accessibleAccountIds = accountRepository.findAccountIdsByUserId(user.getUserId());
        
        if (accessibleAccountIds.isEmpty()) {
            throw new IllegalArgumentException("User has no accessible accounts");
        }

        // BR006: Records must match all supplied filter criteria
        return creditCardRepository.findByAccountIdInAndFilters(accessibleAccountIds, cardNumber, cardStatus, pageable);
    }

    /**
     * Validates card number format
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number to validate
     */
    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }

        String trimmedCardNumber = cardNumber.trim();

        // BR002: Cannot be blank, spaces, or zeros
        if (trimmedCardNumber.matches("^0+$")) {
            throw new IllegalArgumentException("CARD FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR002: Must be numeric and exactly 16 digits
        if (!trimmedCardNumber.matches("^\\d{16}$")) {
            throw new IllegalArgumentException("CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER");
        }
    }

    /**
     * Validates card number filter
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number filter to validate
     */
    private void validateCardNumberFilter(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty() || cardNumber.trim().matches("^0+$")) {
            log.info("Card filter is blank");
            throw new IllegalArgumentException("CARD FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        String trimmedCardNumber = cardNumber.trim();

        if (!trimmedCardNumber.matches("^\\d{16}$")) {
            throw new IllegalArgumentException("CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER");
        }
    }

    /**
     * Validates account ID format
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID to validate
     */
    private void validateAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID is required");
        }

        String trimmedAccountId = accountId.trim();

        // BR004: Cannot be blank, spaces, or zeros
        if (trimmedAccountId.matches("^0+$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR004: Must be numeric and exactly 11 digits
        if (!trimmedAccountId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
    }

    /**
     * Validates account ID filter
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID filter to validate
     */
    private void validateAccountIdFilter(String accountId) {
        if (accountId == null || accountId.trim().isEmpty() || accountId.trim().matches("^0+$")) {
            log.info("Account filter is blank");
            throw new IllegalArgumentException("ACCOUNT FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        String trimmedAccountId = accountId.trim();

        if (!trimmedAccountId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
    }

    /**
     * Validates card status format
     * Implements BR005: Card Status Filter Validation
     * 
     * @param cardStatus The card status to validate
     */
    private void validateCardStatus(String cardStatus) {
        if (cardStatus == null || cardStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Card status is required");
        }

        if (cardStatus.length() != 1) {
            throw new IllegalArgumentException("Card status must be exactly 1 character");
        }

        if (!CardStatus.isValidCode(cardStatus)) {
            throw new IllegalArgumentException("Invalid card status code: " + cardStatus);
        }
    }

    /**
     * Validates card status filter
     * Implements BR005: Card Status Filter Validation
     * 
     * @param cardStatus The card status filter to validate
     */
    private void validateCardStatusFilter(String cardStatus) {
        if (cardStatus == null || cardStatus.trim().isEmpty()) {
            log.info("Card status filter is blank");
            return;
        }

        if (cardStatus.length() != 1) {
            throw new IllegalArgumentException("Card status must be exactly 1 character");
        }

        if (!CardStatus.isValidCode(cardStatus)) {
            throw new IllegalArgumentException("Invalid card status code: " + cardStatus);
        }
    }

    /**
     * Converts CreditCard entity to CreditCardResponseDto
     * 
     * @param creditCard The credit card entity
     * @return The credit card response DTO
     */
    private CreditCardResponseDto convertToResponse(CreditCard creditCard) {
        CreditCardResponseDto response = new CreditCardResponseDto();
        response.setCardNumber(creditCard.getCardNumber());
        response.setMaskedCardNumber(creditCard.getMaskedCardNumber());
        response.setFormattedCardNumber(creditCard.getFormattedCardNumber());
        response.setAccountId(creditCard.getAccountId());
        response.setCardStatus(String.valueOf(creditCard.getCardStatus()));
        response.setCardStatusDisplayName(creditCard.getCardStatusDisplayName());
        response.setIsActive(creditCard.isActive());
        response.setIsBlocked(creditCard.isBlocked());
        response.setIsClosed(creditCard.isClosed());
        response.setCreatedAt(creditCard.getCreatedAt());
        response.setUpdatedAt(creditCard.getUpdatedAt());
        return response;
    }
}
