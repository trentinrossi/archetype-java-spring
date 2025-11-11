package com.carddemo.billpayment.service;

import com.carddemo.billpayment.dto.CardCrossReferenceResponseDto;
import com.carddemo.billpayment.dto.CreateCardCrossReferenceRequestDto;
import com.carddemo.billpayment.dto.UpdateCardCrossReferenceRequestDto;
import com.carddemo.billpayment.entity.Account;
import com.carddemo.billpayment.entity.CardCrossReference;
import com.carddemo.billpayment.repository.AccountRepository;
import com.carddemo.billpayment.repository.CardCrossReferenceRepository;
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
public class CardCrossReferenceService {

    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CardCrossReferenceResponseDto createCardCrossReference(CreateCardCrossReferenceRequestDto request) {
        log.info("Creating new card cross-reference for account: {} and card: {}", 
                 request.getXrefAcctId(), maskCardNumber(request.getXrefCardNum()));

        // Validate account exists
        Account account = accountRepository.findByAcctId(request.getXrefAcctId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        // Check if cross-reference already exists
        if (cardCrossReferenceRepository.existsByXrefAcctIdAndXrefCardNum(
                request.getXrefAcctId(), request.getXrefCardNum())) {
            throw new IllegalArgumentException("Card cross-reference already exists for this account and card");
        }

        CardCrossReference crossReference = new CardCrossReference();
        crossReference.setXrefCardNum(request.getXrefCardNum());
        crossReference.setAccount(account);

        CardCrossReference savedCrossReference = cardCrossReferenceRepository.save(crossReference);
        log.info("Card cross-reference created successfully with ID: {}", savedCrossReference.getId());
        return convertToResponse(savedCrossReference);
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceById(Long id) {
        log.info("Retrieving card cross-reference by ID: {}", id);
        return cardCrossReferenceRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<CardCrossReferenceResponseDto> getCardCrossReferenceByAccountIdAndCardNum(
            String acctId, String cardNum) {
        log.info("Retrieving card cross-reference by account ID: {} and card: {}", 
                 acctId, maskCardNumber(cardNum));
        return cardCrossReferenceRepository.findByXrefAcctIdAndXrefCardNum(acctId, cardNum)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByAccountId(String acctId) {
        log.info("Retrieving all card cross-references for account: {}", acctId);
        return cardCrossReferenceRepository.findAllByXrefAcctId(acctId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardCrossReferenceResponseDto> getCardCrossReferencesByCardNum(String cardNum) {
        log.info("Retrieving all card cross-references for card: {}", maskCardNumber(cardNum));
        return cardCrossReferenceRepository.findAllByXrefCardNum(cardNum).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardCrossReferenceResponseDto updateCardCrossReference(Long id, UpdateCardCrossReferenceRequestDto request) {
        log.info("Updating card cross-reference with ID: {}", id);

        CardCrossReference crossReference = cardCrossReferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card cross-reference not found"));

        if (request.getXrefCardNum() != null) {
            // Check if new card number already exists for this account
            if (cardCrossReferenceRepository.existsByXrefAcctIdAndXrefCardNum(
                    crossReference.getXrefAcctId(), request.getXrefCardNum())) {
                throw new IllegalArgumentException("Card cross-reference already exists for this account and card");
            }
            crossReference.setXrefCardNum(request.getXrefCardNum());
        }

        CardCrossReference updatedCrossReference = cardCrossReferenceRepository.save(crossReference);
        log.info("Card cross-reference updated successfully with ID: {}", updatedCrossReference.getId());
        return convertToResponse(updatedCrossReference);
    }

    @Transactional
    public void deleteCardCrossReference(Long id) {
        log.info("Deleting card cross-reference with ID: {}", id);

        if (!cardCrossReferenceRepository.existsById(id)) {
            throw new IllegalArgumentException("Card cross-reference not found");
        }

        cardCrossReferenceRepository.deleteById(id);
        log.info("Card cross-reference deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<CardCrossReferenceResponseDto> getAllCardCrossReferences(Pageable pageable) {
        log.info("Retrieving all card cross-references with pagination");
        return cardCrossReferenceRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public boolean validateCardForAccount(String acctId, String cardNum) {
        log.info("Validating card for account: {} and card: {}", acctId, maskCardNumber(cardNum));
        return cardCrossReferenceRepository.existsByXrefAcctIdAndXrefCardNum(acctId, cardNum);
    }

    @Transactional(readOnly = true)
    public long countCardsByAccount(String acctId) {
        log.info("Counting cards for account: {}", acctId);
        return cardCrossReferenceRepository.countByXrefAcctId(acctId);
    }

    private CardCrossReferenceResponseDto convertToResponse(CardCrossReference crossReference) {
        CardCrossReferenceResponseDto response = new CardCrossReferenceResponseDto();
        response.setId(crossReference.getId());
        response.setXrefAcctId(crossReference.getXrefAcctId());
        response.setXrefCardNum(crossReference.getXrefCardNum());
        response.setMaskedCardNum(maskCardNumber(crossReference.getXrefCardNum()));
        response.setCreatedAt(crossReference.getCreatedAt());
        response.setUpdatedAt(crossReference.getUpdatedAt());
        return response;
    }

    private String maskCardNumber(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return "****************";
        }
        return "************" + cardNum.substring(12);
    }
}
