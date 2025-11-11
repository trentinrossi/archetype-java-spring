package com.carddemo.billpayment.controller;

import com.carddemo.billpayment.dto.BillPaymentRequestDto;
import com.carddemo.billpayment.dto.BillPaymentResponseDto;
import com.carddemo.billpayment.service.BillPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bill Payment Controller - Main API for bill payment processing
 * Implements the complete bill payment workflow with all business rules
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Bill Payment", description = "APIs for processing bill payments")
@RequestMapping("/api/bill-payment")
public class BillPaymentController {
    
    private final BillPaymentService billPaymentService;
    
    @Operation(
        summary = "Process bill payment", 
        description = "Process a bill payment for an account. Implements all business rules:\n" +
                     "- BR001: Account Validation\n" +
                     "- BR002: Balance Check\n" +
                     "- BR003: Payment Confirmation\n" +
                     "- BR004: Full Balance Payment\n" +
                     "- BR005: Transaction ID Generation\n" +
                     "- BR006: Bill Payment Transaction Recording\n" +
                     "- BR007: Account Balance Update\n\n" +
                     "Workflow:\n" +
                     "1. First call without confirmation to get account info\n" +
                     "2. Second call with confirmation='Y' to process payment\n" +
                     "3. Or with confirmation='N' to cancel"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bill payment processed successfully or pending confirmation"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation failed:\n" +
                     "- Acct ID can NOT be empty...\n" +
                     "- Account ID NOT found...\n" +
                     "- You have nothing to pay...\n" +
                     "- Card number is not associated with this account\n" +
                     "- Invalid value. Valid values are (Y/N)..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/process")
    public ResponseEntity<BillPaymentResponseDto> processBillPayment(
            @Valid @RequestBody BillPaymentRequestDto request) {
        log.info("Processing bill payment request for account: {}", request.getAccountId());
        
        try {
            BillPaymentResponseDto response = billPaymentService.processBillPayment(request);
            
            // Return 200 for all cases (success, pending, cancelled)
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.error("Bill payment validation failed: {}", e.getMessage());
            
            // Return error response with 400 status
            BillPaymentResponseDto errorResponse = new BillPaymentResponseDto();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage(e.getMessage());
            errorResponse.setAccountId(request.getAccountId());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @Operation(
        summary = "Get account information for payment confirmation", 
        description = "Retrieve account information to display before payment confirmation.\n" +
                     "Validates account exists and has positive balance."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed:\n" +
                     "- Acct ID can NOT be empty...\n" +
                     "- Account ID NOT found...\n" +
                     "- You have nothing to pay...\n" +
                     "- Card number is not associated with this account"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}/confirmation")
    public ResponseEntity<BillPaymentResponseDto> getAccountForPaymentConfirmation(
            @PathVariable String accountId,
            @RequestParam(required = false) String cardNumber) {
        log.info("Getting account information for payment confirmation: {}", accountId);
        
        try {
            BillPaymentResponseDto response = billPaymentService.getAccountForPaymentConfirmation(
                    accountId, cardNumber);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.error("Account validation failed: {}", e.getMessage());
            
            BillPaymentResponseDto errorResponse = new BillPaymentResponseDto();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage(e.getMessage());
            errorResponse.setAccountId(accountId);
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @Operation(
        summary = "Quick bill payment (single step)", 
        description = "Process bill payment in a single step without explicit confirmation.\n" +
                     "Use this endpoint when confirmation is handled on the client side."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bill payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/quick-process")
    public ResponseEntity<BillPaymentResponseDto> quickProcessBillPayment(
            @RequestParam String accountId,
            @RequestParam String cardNumber) {
        log.info("Quick processing bill payment for account: {}", accountId);
        
        BillPaymentRequestDto request = new BillPaymentRequestDto();
        request.setAccountId(accountId);
        request.setCardNumber(cardNumber);
        request.setConfirmation("Y");
        
        try {
            BillPaymentResponseDto response = billPaymentService.processBillPayment(request);
            
            if ("SUCCESS".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (IllegalArgumentException e) {
            log.error("Quick bill payment failed: {}", e.getMessage());
            
            BillPaymentResponseDto errorResponse = new BillPaymentResponseDto();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage(e.getMessage());
            errorResponse.setAccountId(accountId);
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
