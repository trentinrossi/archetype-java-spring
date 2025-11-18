package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

    @Schema(description = "Internal customer ID", example = "1")
    private Long id;

    @Schema(description = "Unique customer identifier", example = "CUST001")
    private String customerId;

    @Schema(description = "Customer's first name", example = "John")
    private String firstName;

    @Schema(description = "Customer's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Customer's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Customer's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Customer's phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Number of accounts associated with this customer", example = "2")
    private Integer accountCount;

    @Schema(description = "Number of credit cards associated with this customer", example = "3")
    private Integer creditCardCount;

    @Schema(description = "Timestamp when the customer was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the customer was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
