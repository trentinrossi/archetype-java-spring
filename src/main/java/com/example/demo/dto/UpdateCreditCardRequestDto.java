package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreditCardRequestDto {

    @Schema(description = "Name embossed on the credit card (alphabets and spaces only)", example = "JOHN DOE", required = false)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Card name must contain only alphabets and spaces")
    @Size(max = 50, message = "Embossed name must not exceed 50 characters")
    private String embossedName;

    @Schema(description = "Card expiration date in YYYY-MM-DD format", example = "2025-12-31", required = false)
    private LocalDate expirationDate;

    @Schema(description = "Active status of the card - Y for active, N for inactive", example = "Y", required = false)
    @Pattern(regexp = "^[YN]$", message = "Card status must be Y (active) or N (inactive)")
    @Size(min = 1, max = 1, message = "Card status must be exactly 1 character")
    private String cardStatus;

    @Schema(description = "Version number for optimistic locking (concurrent update prevention)", example = "1", required = false)
    private Long version;

    @Schema(description = "User ID of the person making the update", example = "USER001", required = false)
    private String updatedBy;
}
