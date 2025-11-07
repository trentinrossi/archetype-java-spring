package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Account;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for CreditCard entity operations
 * Business Rule BR001: User Permission Based Card Listing
 * Business Rule BR002: Pagination Display Limit (max 7 records per page)
 * Business Rule BR004: Filter Application Logic
 * Business Rule BR008: Record Exclusion Based on Filters
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardService {
    
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    
    /**
     * Maximum records per page as per Business Rule BR002
     */
    private static final int MAX_RECORDS_PER_PAGE = 7;
    
    /**
     * Create a new credit card
     * Validates card number and account ID format
     */
    @Transactional
    public CreditCardResponseDto createCreditCard(CreateCreditCardRequestDto request) {
        log.info("Creating new credit card for account: {}", request.getAccountId());
        
        // Validate card number format (16 digits)
        if (!request.getCardNumber().matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be exactly 16 numeric digits");
        }
        
        // Validate account ID format (11 digits)
        if (!request.getAccountId().matches("\\d{11}")) {
            throw new IllegalArgumentException("Account ID must be exactly 11 numeric digits");
        }
        
        // Check if card number already exists
        if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Credit card with number " + request.getCardNumber() + " already exists");
        }
        
        // Find or create account
        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.getAccountId()));
        
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(request.getCardNumber());
        creditCard.setAccount(account);
        creditCard.setCardStatus(request.getCardStatus());
        
        // Validate using entity method
        if (!creditCard.isValidCardNumber()) {
            throw new IllegalArgumentException("Invalid card number format");
        }
        
        CreditCard savedCard = creditCardRepository.save(creditCard);
        log.info("Credit card created successfully with ID: {} for account: {}", 
                savedCard.getId(), savedCard.getAccountId());
        
        return convertToResponse(savedCard);
    }
    
    /**
     * Get credit card by ID
     */
    @Transactional(readOnly = true)
    public Optional<CreditCardResponseDto> getCreditCardById(Long id) {
        log.debug("Fetching credit card with ID: {}", id);
        return creditCardRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit card by card number
     */
    @Transactional(readOnly = true)
    public Optional<CreditCardResponseDto> getCreditCardByCardNumber(String cardNumber) {
        log.debug("Fetching credit card with number: {}", cardNumber);
        return creditCardRepository.findByCardNumber(cardNumber)
                .map(this::convertToResponse);
    }
    
    /**
     * Update an existing credit card
     * Business Rule BR003: Single Selection Enforcement (one card at a time)
     */
    @Transactional
    public CreditCardResponseDto updateCreditCard(Long id, UpdateCreditCardRequestDto request) {
        log.info("Updating credit card with ID: {}", id);
        
        CreditCard creditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Credit card not found with ID: " + id));
        
        // Update card status if provided
        if (request.getCardStatus() != null) {
            log.info("Changing card status from {} to {} for card: {}", 
                    creditCard.getCardStatus(), request.getCardStatus(), id);
            creditCard.setCardStatus(request.getCardStatus());
        }
        
        CreditCard updatedCard = creditCardRepository.save(creditCard);
        log.info("Credit card updated successfully with ID: {}", updatedCard.getId());
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Delete a credit card
     */
    @Transactional
    public void deleteCreditCard(Long id) {
        log.info("Deleting credit card with ID: {}", id);
        
        if (!creditCardRepository.existsById(id)) {
            throw new IllegalArgumentException("Credit card not found with ID: " + id);
        }
        
        creditCardRepository.deleteById(id);
        log.info("Credit card deleted successfully with ID: {}", id);
    }
    
    /**
     * Get all credit cards with pagination
     * Business Rule BR002: Maximum of 7 records per page
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getAllCreditCards(Pageable pageable) {
        log.debug("Fetching all credit cards with pagination");
        
        // Enforce max page size
        Pageable adjustedPageable = enforceMaxPageSize(pageable);
        
        return creditCardRepository.findAll(adjustedPageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit cards with filters and user permissions
     * Business Rule BR001: User Permission Based Card Listing
     * Business Rule BR004: Filter Application Logic
     * Business Rule BR008: Record Exclusion Based on Filters
     */
    @Transactional(readOnly = true)
    public CreditCardPageResponseDto getCreditCardsWithFilters(
            String userId, 
            CreditCardFilterRequestDto filter, 
            Pageable pageable) {
        
        log.info("Fetching credit cards for user: {} with filters - accountId: {}, cardNumber: {}", 
                userId, filter.getAccountId(), filter.getCardNumber());
        
        // Enforce max page size (Business Rule BR002)
        Pageable adjustedPageable = enforceMaxPageSize(pageable);
        
        // Get user to check permissions
        User user = userRepository.findByUserIdWithAccounts(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        Page<CreditCard> creditCardPage;
        
        // Business Rule BR001: Admin users can view all cards when no context is passed
        if (user.isAdmin() && !filter.hasAnyFilter()) {
            log.debug("Admin user viewing all cards without filters");
            creditCardPage = creditCardRepository.findAll(adjustedPageable);
        }
        // Business Rule BR004: Filters are applied cumulatively if both are specified
        else if (filter.hasAccountIdFilter() && filter.hasCardNumberFilter()) {
            log.debug("Applying both account ID and card number filters");
            
            // Business Rule BR001: Check user access for non-admin
            if (!user.isAdmin() && !user.hasAccessToAccount(filter.getAccountId())) {
                throw new SecurityException("User does not have access to account: " + filter.getAccountId());
            }
            
            creditCardPage = creditCardRepository.findByAccountIdAndCardNumber(
                    filter.getAccountId(), filter.getCardNumber(), adjustedPageable);
        }
        // Business Rule BR004: Filter by account ID only
        else if (filter.hasAccountIdFilter()) {
            log.debug("Applying account ID filter only");
            
            // Business Rule BR001: Check user access for non-admin
            if (!user.isAdmin() && !user.hasAccessToAccount(filter.getAccountId())) {
                throw new SecurityException("User does not have access to account: " + filter.getAccountId());
            }
            
            creditCardPage = creditCardRepository.findByAccountId(filter.getAccountId(), adjustedPageable);
        }
        // Business Rule BR004: Filter by card number only
        else if (filter.hasCardNumberFilter()) {
            log.debug("Applying card number filter only");
            creditCardPage = creditCardRepository.findByCardNumber(filter.getCardNumber(), adjustedPageable);
        }
        // Business Rule BR001: Non-admin users can only view cards associated with their specific account
        else if (!user.isAdmin()) {
            log.debug("Non-admin user viewing cards from accessible accounts");
            creditCardPage = creditCardRepository.findCreditCardsByUserId(userId, adjustedPageable);
        }
        else {
            log.debug("Admin user viewing all cards");
            creditCardPage = creditCardRepository.findAll(adjustedPageable);
        }
        
        // Convert to paginated response with state information
        return convertToPageResponse(creditCardPage);
    }
    
    /**
     * Get credit cards by account ID
     * Business Rule BR004: Filter by account ID
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByAccountId(String accountId, Pageable pageable) {
        log.debug("Fetching credit cards for account: {}", accountId);
        
        // Enforce max page size
        Pageable adjustedPageable = enforceMaxPageSize(pageable);
        
        return creditCardRepository.findByAccountId(accountId, adjustedPageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get credit cards accessible by user
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    @Transactional(readOnly = true)
    public Page<CreditCardResponseDto> getCreditCardsByUserId(String userId, Pageable pageable) {
        log.debug("Fetching credit cards for user: {}", userId);
        
        // Enforce max page size
        Pageable adjustedPageable = enforceMaxPageSize(pageable);
        
        return creditCardRepository.findCreditCardsByUserId(userId, adjustedPageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Enforce maximum page size
     * Business Rule BR002: Maximum of 7 records per page
     */
    private Pageable enforceMaxPageSize(Pageable pageable) {
        if (pageable.getPageSize() > MAX_RECORDS_PER_PAGE) {
            log.debug("Adjusting page size from {} to max allowed {}", 
                    pageable.getPageSize(), MAX_RECORDS_PER_PAGE);
            return PageRequest.of(pageable.getPageNumber(), MAX_RECORDS_PER_PAGE, pageable.getSort());
        }
        return pageable;
    }
    
    /**
     * Convert CreditCard entity to CreditCardResponseDto
     */
    private CreditCardResponseDto convertToResponse(CreditCard creditCard) {
        CreditCardResponseDto response = new CreditCardResponseDto();
        response.setId(creditCard.getId());
        response.setCardNumber(creditCard.getCardNumber());
        response.setMaskedCardNumber(creditCard.getMaskedCardNumber());
        response.setAccountId(creditCard.getAccountId());
        response.setCardStatus(creditCard.getCardStatus());
        response.setCardStatusDisplayName(creditCard.getCardStatusDisplayName());
        response.setIsActive(creditCard.isActive());
        response.setCanPerformTransactions(creditCard.canPerformTransactions());
        response.setIsBlocked(creditCard.isBlocked());
        response.setCreatedAt(creditCard.getCreatedAt());
        response.setUpdatedAt(creditCard.getUpdatedAt());
        return response;
    }
    
    /**
     * Convert Page to CreditCardPageResponseDto with state information
     * Business Rule BR005: Page Navigation State Management
     */
    private CreditCardPageResponseDto convertToPageResponse(Page<CreditCard> page) {
        List<CreditCardResponseDto> creditCards = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        CreditCardPageResponseDto response = new CreditCardPageResponseDto();
        response.setCreditCards(creditCards);
        response.setCurrentPage(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setTotalRecords(page.getTotalElements());
        response.setPageSize(page.getSize());
        
        // Business Rule BR005: First and last card keys displayed
        if (!creditCards.isEmpty()) {
            response.setFirstCardKey(creditCards.get(0).getCardNumber());
            response.setLastCardKey(creditCards.get(creditCards.size() - 1).getCardNumber());
        }
        
        // Business Rule BR005: Whether additional pages exist
        response.setHasNextPage(page.hasNext());
        response.setHasPreviousPage(page.hasPrevious());
        response.setIsFirstPage(page.isFirst());
        response.setIsLastPage(page.isLast());
        
        return response;
    }
}
