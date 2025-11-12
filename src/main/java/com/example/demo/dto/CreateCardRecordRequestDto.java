package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRecordRequestDto {

    @NotBlank(message = "Invalid card number format")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only numbers")
    @Size(min = 16, max = 16, message = "Invalid card number format")
    private String cardNumber;

    @NotBlank(message = "Card data is required")
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    private String cardData;
}
