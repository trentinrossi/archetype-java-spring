package com.example.demo.service;

import com.example.demo.dto.CardCrossReferenceResponseDto;
import com.example.demo.dto.CreateCardCrossReferenceRequestDto;
import com.example.demo.dto.UpdateCardCrossReferenceRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.entity.Customer;
import com.example.demo.enums.CardStatus;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CardCrossReferenceRepository;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for CardCrossReference operations
 * Implements business logic for card cross reference management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CardCrossReferenceService {
    
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    
    /**
     * Create a new card cross reference
     */
    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross reference for card: {}", maskCardNumber(request.getCardNumber()));
        
        // Validate card number format (must be exactly 16 characters)
        if (request.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 characters");
        }
        
        // Check if card already exists
        if (cardCrossReferenceRepository.existsById(request.getCardNumber())) {
            throw new IllegalArgumentException("Card already exists with number: " + maskCardNumber(request.getCardNumber()));
        }
        
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId()));
        
        // Validate account exists
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.getAccountId()));
        
        // Validate that account belongs to customer
        if (!account.getCustomer().getCustomerId().equals(request.getCustomerId())) {
            throw new IllegalArgumentException("Account does not belong to the specified customer");
        }
        
        CardCrossReference cardCrossReference = new CardCrossReference();
        cardCrossReference.setCardNumber(request.getCardNumber());
        cardCrossReference.setCustomer(customer);
        cardCrossReference.setAccount(account);
        cardCrossReference.setCardType(request.getCardType());
        cardCrossReference.setCardHolderName(request.getCardHolderName());
        cardCrossReference.setExpirationDate(request.getExpirationDate());
        cardCrossReference.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
        cardCrossReference.setCardStatus(CardStatus.PENDING_ACTIVATION);
        cardCrossReference.setIsPrimaryCard(request.getIsPrimaryCard() != null ? request.getIsPrimaryCard() : false);
        cardCrossReference.setCardSequenceNumber(request.getCardSequenceNumber());
        cardCrossReference.setEmbossedName(request.getEmbossedName());
        cardCrossReference.setPinSet(false);
        cardCrossReference.setCardProductionDate(LocalDate.now());
        
        CardCrossReference savedCard = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Card cross reference created successfully for card: {}", maskCardNumber(savedCard.getCardNumber()));
        
        return convertToResponse(savedCard);
    }
    
    /**
     * Get card cross reference by card number
     */
    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceByCardNumber(String cardNumber) {
        log.debug("Fetching card cross reference for card: {}", maskCardNumber(cardNumber));
        return cardCrossReferenceRepository.findById(cardNumber).map(this::convertToResponse);
    }
    
    /**
     * Update card cross reference
     */
    @Transactional
    public CardCrossReferenceResponseDto updateCardCrossReference(String cardNumber, 
                                                                 UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross reference for card: {}", maskCardNumber(cardNumber));
        
        CardCrossReference cardCrossReference = cardCrossReferenceRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with number: " + maskCardNumber(cardNumber)));
        
        // Update only non-null fields
        if (request.getCardStatus() != null) cardCrossReference.setCardStatus(request.getCardStatus());
        if (request.getCardHolderName() != null) cardCrossReference.setCardHolderName(request.getCardHolderName());
        if (request.getExpirationDate() != null) cardCrossReference.setExpirationDate(request.getExpirationDate());
        if (request.getIsPrimaryCard() != null) cardCrossReference.setIsPrimaryCard(request.getIsPrimaryCard());
        if (request.getEmbossedName() != null) cardCrossReference.setEmbossedName(request.getEmbossedName());
        if (request.getPinSet() != null) {
            cardCrossReference.setPinSet(request.getPinSet());
            if (request.getPinSet()) {
                cardCrossReference.setPinSetDate(LocalDate.now());
            }
        }
        if (request.getPinSetDate() != null) cardCrossReference.setPinSetDate(request.getPinSetDate());
        if (request.getActivationDate() != null) {
            cardCrossReference.setActivationDate(request.getActivationDate());
            if (cardCrossReference.getCardStatus() == CardStatus.PENDING_ACTIVATION) {
                cardCrossReference.setCardStatus(CardStatus.ACTIVE);
            }
        }
        if (request.getLastUsedDate() != null) cardCrossReference.setLastUsedDate(request.getLastUsedDate());
        if (request.getReplacementCardNumber() != null) cardCrossReference.setReplacementCardNumber(request.getReplacementCardNumber());
        
        CardCrossReference updatedCard = cardCrossReferenceRepository.save(cardCrossReference);
        log.info("Card cross reference updated successfully for card: {}", maskCardNumber(updatedCard.getCardNumber()));
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Delete card cross reference
     */
    @Transactional
    public void deleteCardCrossReference(String cardNumber) {
        log.info("Deleting card cross reference for card: {}", maskCardNumber(cardNumber));
        
        if (!cardCrossReferenceRepository.existsById(cardNumber)) {
            throw new IllegalArgumentException("Card not found with number: " + maskCardNumber(cardNumber));
        }
        
        cardCrossReferenceRepository.deleteById(cardNumber);
        log.info("Card cross reference deleted successfully for card: {}", maskCardNumber(cardNumber));
    }
    
    /**
     * Get all card cross references with pagination
     */
    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.debug("Fetching all card cross references with pagination");
        return cardCrossReferenceRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get card cross references by customer ID
     */
    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByCustomerId(String customerId) {
        log.debug("Fetching card cross references for customer ID: {}", customerId);
        return cardCrossReferenceRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get card cross references by account ID
     */
    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByAccountId(String accountId) {
        log.debug("Fetching card cross references for account ID: {}", accountId);
        return cardCrossReferenceRepository.findByAccountId(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get card cross references by status
     */
    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getCardCrossReferencesByStatus(CardStatus status, Pageable pageable) {
        log.debug("Fetching card cross references with status: {}", status);
        return cardCrossReferenceRepository.findByCardStatus(status, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get active cards
     */
    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getActiveCards(Pageable pageable) {
        log.debug("Fetching active cards");
        return cardCrossReferenceRepository.findActiveCards(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get expired cards
     */
    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getExpiredCards(Pageable pageable) {
        log.debug("Fetching expired cards");
        return cardCrossReferenceRepository.findExpiredCards(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get cards expiring soon (within 3 months)
     */
    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardsExpiringSoon() {
        log.debug("Fetching cards expiring soon");
        LocalDate threeMonthsFromNow = LocalDate.now().plusMonths(3);
        return cardCrossReferenceRepository.findExpiringBetween(LocalDate.now(), threeMonthsFromNow)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Activate a card
     */
    @Transactional
    public CardCrossReferenceResponseDto activateCard(String cardNumber) {
        log.info("Activating card: {}", maskCardNumber(cardNumber));
        
        CardCrossReference card = cardCrossReferenceRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with number: " + maskCardNumber(cardNumber)));
        
        if (card.getCardStatus() != CardStatus.PENDING_ACTIVATION) {
            throw new IllegalArgumentException("Card is not in pending activation status");
        }
        
        card.setCardStatus(CardStatus.ACTIVE);
        card.setActivationDate(LocalDate.now());
        
        CardCrossReference updatedCard = cardCrossReferenceRepository.save(card);
        log.info("Card activated successfully: {}", maskCardNumber(cardNumber));
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Report card as lost or stolen
     */
    @Transactional
    public CardCrossReferenceResponseDto reportCardLostOrStolen(String cardNumber, boolean isStolen) {
        log.info("Reporting card as {}: {}", isStolen ? "stolen" : "lost", maskCardNumber(cardNumber));
        
        CardCrossReference card = cardCrossReferenceRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with number: " + maskCardNumber(cardNumber)));
        
        card.setCardStatus(isStolen ? CardStatus.STOLEN : CardStatus.LOST);
        
        CardCrossReference updatedCard = cardCrossReferenceRepository.save(card);
        log.info("Card reported as {} successfully: {}", isStolen ? "stolen" : "lost", maskCardNumber(cardNumber));
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Set PIN for card
     */
    @Transactional
    public CardCrossReferenceResponseDto setPinForCard(String cardNumber) {
        log.info("Setting PIN for card: {}", maskCardNumber(cardNumber));
        
        CardCrossReference card = cardCrossReferenceRepository.findById(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with number: " + maskCardNumber(cardNumber)));
        
        card.setPinSet(true);
        card.setPinSetDate(LocalDate.now());
        
        CardCrossReference updatedCard = cardCrossReferenceRepository.save(card);
        log.info("PIN set successfully for card: {}", maskCardNumber(cardNumber));
        
        return convertToResponse(updatedCard);
    }
    
    /**
     * Mask card number for security
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "************";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Convert CardCrossReference entity to CardCrossReferenceResponseDto
     */
    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference card) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setCardNumber(maskCardNumber(card.getCardNumber()));
        response.setCustomerId(card.getCustomer().getCustomerId());
        response.setAccountId(card.getAccount().getAccountId());
        response.setCardType(card.getCardType());
        response.setCardHolderName(card.getCardHolderName());
        response.setExpirationDate(card.getExpirationDate());
        response.setIssueDate(card.getIssueDate());
        response.setCardStatus(card.getCardStatus());
        response.setCardStatusDisplayName(card.getCardStatus().getDisplayName());
        response.setIsPrimaryCard(card.getIsPrimaryCard());
        response.setCardSequenceNumber(card.getCardSequenceNumber());
        response.setEmbossedName(card.getEmbossedName());
        response.setPinSet(card.getPinSet());
        response.setPinSetDate(card.getPinSetDate());
        response.setActivationDate(card.getActivationDate());
        response.setLastUsedDate(card.getLastUsedDate());
        response.setReplacementCardNumber(card.getReplacementCardNumber() != null ? 
                maskCardNumber(card.getReplacementCardNumber()) : null);
        response.setReplacedCardNumber(card.getReplacedCardNumber() != null ? 
                maskCardNumber(card.getReplacedCardNumber()) : null);
        response.setCardProductionDate(card.getCardProductionDate());
        response.setCardMailedDate(card.getCardMailedDate());
        response.setIsExpired(card.isExpired());
        response.setIsActive(card.isActive());
        response.setNeedsReplacement(card.needsReplacement());
        response.setCanTransact(card.canTransact());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}
