package com.example.demo.service;

import com.example.demo.dto.CardDataDto;
import com.example.demo.entity.CardData;
import com.example.demo.repository.CardDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Card Data Service
 * Service layer for CardData operations
 * Based on COBOL CBACT02C.cbl business rules for card data file processing
 */
@Service
@Transactional
public class CardDataService {

    private static final Logger logger = LoggerFactory.getLogger(CardDataService.class);

    private final CardDataRepository cardDataRepository;

    @Autowired
    public CardDataService(CardDataRepository cardDataRepository) {
        this.cardDataRepository = cardDataRepository;
    }

    /**
     * Read and display all card data records sequentially
     * Equivalent to the main processing loop in COBOL CBACT02C.cbl
     * Implements the 1000CARDFILEGETNEXT functionality
     */
    @Transactional(readOnly = true)
    public List<CardDataDto> readAllCardDataSequentially() {
        logger.info("Starting sequential read of all card data records");
        
        try {
            List<CardData> cardDataList = cardDataRepository.findAllOrderedByCardNumber();
            logger.info("Successfully read {} card data records", cardDataList.size());
            
            return cardDataList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error reading card data records: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read card data records", e);
        }
    }

    /**
     * Read card data with pagination support for batch processing
     * Implements paginated sequential access similar to COBOL batch processing
     */
    @Transactional(readOnly = true)
    public Page<CardDataDto> readCardDataWithPagination(int page, int size) {
        logger.info("Reading card data with pagination - page: {}, size: {}", page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CardData> cardDataPage = cardDataRepository.findAllOrderedByCardNumber(pageable);
            
            logger.info("Successfully read page {} with {} records", page, cardDataPage.getContent().size());
            
            return cardDataPage.map(this::convertToDto);
        } catch (Exception e) {
            logger.error("Error reading card data with pagination: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read card data with pagination", e);
        }
    }

    /**
     * Find card data by card number (primary key lookup)
     * Equivalent to indexed file access by FDCARDNUM
     */
    @Transactional(readOnly = true)
    public Optional<CardDataDto> findByCardNumber(String cardNumber) {
        logger.info("Finding card data by card number: {}", cardNumber);
        
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            logger.warn("Card number is null or empty");
            return Optional.empty();
        }
        
        try {
            Optional<CardData> cardData = cardDataRepository.findByCardNumber(cardNumber);
            if (cardData.isPresent()) {
                logger.info("Card data found for card number: {}", cardNumber);
                return Optional.of(convertToDto(cardData.get()));
            } else {
                logger.info("No card data found for card number: {}", cardNumber);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding card data by card number {}: {}", cardNumber, e.getMessage(), e);
            throw new RuntimeException("Failed to find card data by card number", e);
        }
    }

    /**
     * Create new card data record
     * Implements file write operation equivalent
     */
    public CardDataDto createCardData(CardDataDto cardDataDto) {
        logger.info("Creating new card data record for card number: {}", cardDataDto.getCardNumber());
        
        validateCardDataDto(cardDataDto);
        
        if (cardDataRepository.existsByCardNumber(cardDataDto.getCardNumber())) {
            logger.error("Card data already exists for card number: {}", cardDataDto.getCardNumber());
            throw new IllegalArgumentException("Card data already exists for card number: " + cardDataDto.getCardNumber());
        }
        
        try {
            CardData cardData = convertToEntity(cardDataDto);
            CardData savedCardData = cardDataRepository.save(cardData);
            
            logger.info("Successfully created card data record for card number: {}", savedCardData.getCardNumber());
            return convertToDto(savedCardData);
        } catch (Exception e) {
            logger.error("Error creating card data record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create card data record", e);
        }
    }

    /**
     * Update existing card data record
     */
    public CardDataDto updateCardData(String cardNumber, CardDataDto cardDataDto) {
        logger.info("Updating card data record for card number: {}", cardNumber);
        
        validateCardDataDto(cardDataDto);
        
        Optional<CardData> existingCardData = cardDataRepository.findByCardNumber(cardNumber);
        if (existingCardData.isEmpty()) {
            logger.error("Card data not found for card number: {}", cardNumber);
            throw new IllegalArgumentException("Card data not found for card number: " + cardNumber);
        }
        
        try {
            CardData cardData = existingCardData.get();
            cardData.setCardData(cardDataDto.getCardData());
            
            CardData updatedCardData = cardDataRepository.save(cardData);
            
            logger.info("Successfully updated card data record for card number: {}", cardNumber);
            return convertToDto(updatedCardData);
        } catch (Exception e) {
            logger.error("Error updating card data record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update card data record", e);
        }
    }

    /**
     * Delete card data record
     */
    public void deleteCardData(String cardNumber) {
        logger.info("Deleting card data record for card number: {}", cardNumber);
        
        if (!cardDataRepository.existsByCardNumber(cardNumber)) {
            logger.error("Card data not found for card number: {}", cardNumber);
            throw new IllegalArgumentException("Card data not found for card number: " + cardNumber);
        }
        
        try {
            cardDataRepository.deleteById(cardNumber);
            logger.info("Successfully deleted card data record for card number: {}", cardNumber);
        } catch (Exception e) {
            logger.error("Error deleting card data record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete card data record", e);
        }
    }

    /**
     * Get total count of card data records
     * Useful for batch processing statistics
     */
    @Transactional(readOnly = true)
    public long getCardDataCount() {
        logger.info("Getting total count of card data records");
        
        try {
            long count = cardDataRepository.countAllCardData();
            logger.info("Total card data records count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting card data count: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get card data count", e);
        }
    }

    /**
     * Search card data by content
     */
    @Transactional(readOnly = true)
    public List<CardDataDto> searchByCardData(String searchTerm) {
        logger.info("Searching card data by content: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            logger.warn("Search term is null or empty");
            return List.of();
        }
        
        try {
            List<CardData> cardDataList = cardDataRepository.findByCardDataContaining(searchTerm);
            logger.info("Found {} card data records matching search term", cardDataList.size());
            
            return cardDataList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching card data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search card data", e);
        }
    }

    /**
     * Convert CardData entity to DTO
     */
    private CardDataDto convertToDto(CardData cardData) {
        return new CardDataDto(
                cardData.getCardNumber(),
                cardData.getCardData(),
                cardData.getCreatedAt(),
                cardData.getUpdatedAt()
        );
    }

    /**
     * Convert CardDataDto to entity
     */
    private CardData convertToEntity(CardDataDto cardDataDto) {
        return new CardData(
                cardDataDto.getCardNumber(),
                cardDataDto.getCardData()
        );
    }

    /**
     * Validate CardDataDto
     */
    private void validateCardDataDto(CardDataDto cardDataDto) {
        if (cardDataDto == null) {
            throw new IllegalArgumentException("CardDataDto cannot be null");
        }
        
        if (cardDataDto.getCardNumber() == null || cardDataDto.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        
        if (cardDataDto.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 characters");
        }
        
        if (cardDataDto.getCardData() == null || cardDataDto.getCardData().trim().isEmpty()) {
            throw new IllegalArgumentException("Card data cannot be null or empty");
        }
        
        if (cardDataDto.getCardData().length() > 134) {
            throw new IllegalArgumentException("Card data cannot exceed 134 characters");
        }
    }
}