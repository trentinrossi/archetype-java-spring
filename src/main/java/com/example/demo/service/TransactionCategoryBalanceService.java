package com.example.demo.service;

import com.example.demo.dto.CreateTransactionCategoryBalanceRequestDto;
import com.example.demo.dto.UpdateTransactionCategoryBalanceRequestDto;
import com.example.demo.dto.TransactionCategoryBalanceResponseDto;
import com.example.demo.entity.TransactionCategoryBalance;
import com.example.demo.repository.TransactionCategoryBalanceRepository;
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
public class TransactionCategoryBalanceService {

    private final TransactionCategoryBalanceRepository repository;

    @Transactional
    public TransactionCategoryBalanceResponseDto create(CreateTransactionCategoryBalanceRequestDto request) {
        log.info("Creating new transaction category balance");

        TransactionCategoryBalance entity = new TransactionCategoryBalance();
        entity.setTrancatAcctId(request.getTrancatAcctId());
        entity.setTrancatTypeCd(request.getTrancatTypeCd());
        entity.setTrancatCd(request.getTrancatCd());
        entity.setTranCatBal(request.getTranCatBal());

        TransactionCategoryBalance saved = repository.save(entity);
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionCategoryBalanceResponseDto> getById(Long id) {
        return repository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionCategoryBalanceResponseDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public TransactionCategoryBalanceResponseDto update(Long id, UpdateTransactionCategoryBalanceRequestDto request) {
        TransactionCategoryBalance entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction category balance not found"));

        if (request.getTrancatTypeCd() != null) entity.setTrancatTypeCd(request.getTrancatTypeCd());
        if (request.getTrancatCd() != null) entity.setTrancatCd(request.getTrancatCd());
        if (request.getTranCatBal() != null) entity.setTranCatBal(request.getTranCatBal());

        TransactionCategoryBalance updated = repository.save(entity);
        return convertToResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Transaction category balance not found");
        }
        repository.deleteById(id);
    }

    private TransactionCategoryBalanceResponseDto convertToResponse(TransactionCategoryBalance entity) {
        TransactionCategoryBalanceResponseDto response = new TransactionCategoryBalanceResponseDto();
        response.setId(entity.getId());
        response.setTrancatAcctId(entity.getTrancatAcctId());
        response.setTrancatTypeCd(entity.getTrancatTypeCd());
        response.setTrancatCd(entity.getTrancatCd());
        response.setTranCatBal(entity.getTranCatBal());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
