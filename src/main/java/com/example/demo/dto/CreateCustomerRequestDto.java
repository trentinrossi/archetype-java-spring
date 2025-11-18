package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {

    @Schema(description = "Unique customer identifier", example = "CUST001", required = true)
    @NotBlank(message = "Customer ID is required")
    @Size(max = 20, message = "Customer ID must not exceed 20 characters")
    private String customerId;

    @Schema(description = "Customer's first name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Schema(description = "Customer's last name", example = "Doe", required = true)
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Schema(description = "Customer's email address", example = "john.doe@example.com", required = false)
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Schema(description = "Customer's phone number", example = "+1234567890", required = false)
    @Pattern(regexp = "^[+]?[0-9]{10,20}$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
}
