package com.example.demo.service;

import com.example.demo.dto.CreateCustomerRequestDto;
import com.example.demo.dto.UpdateCustomerRequestDto;
import com.example.demo.dto.CustomerResponseDto;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\(\\d{3}\\)\\d{3}-\\d{4}$");
    private static final Pattern STATE_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{3}$");
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}$");
    private static final Pattern EFT_ACCOUNT_PATTERN = Pattern.compile("^\\d{10}$");

    @Transactional
    public CustomerResponseDto createCustomer(CreateCustomerRequestDto request) {
        log.info("Creating new customer with ID: {}", request.getCustomerId());

        validateCustomerRequest(request);
        validateCustomerIdNotExists(request.getCustomerId());
        validateCustomerAge(request.getDateOfBirth());
        validateFicoScore(request.getFicoScore());
        validatePrimaryCardholderIndicator(request.getPrimaryCardholderIndicator());

        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setAddressLine1(request.getAddressLine1());
        customer.setAddressLine2(request.getAddressLine2());
        customer.setAddressLine3(request.getAddressLine3());
        customer.setStateCode(request.getStateCode());
        customer.setCountryCode(request.getCountryCode());
        customer.setZipCode(request.getZipCode());
        customer.setPhoneNumber1(request.getPhoneNumber1());
        customer.setPhoneNumber2(request.getPhoneNumber2());
        customer.setSsn(request.getSsn());
        customer.setGovernmentIssuedId(request.getGovernmentIssuedId());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setEftAccountId(request.getEftAccountId());
        customer.setPrimaryCardholderIndicator(request.getPrimaryCardholderIndicator());
        customer.setFicoScore(request.getFicoScore());
        customer.setFicoCreditScore(request.getFicoCreditScore());
        customer.setCity(request.getCity());
        customer.setPrimaryPhoneNumber(request.getPrimaryPhoneNumber());
        customer.setSecondaryPhoneNumber(request.getSecondaryPhoneNumber());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        return convertToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerResponseDto> getCustomerById(Long customerId) {
        log.info("Retrieving customer with ID: {}", customerId);
        
        validateCustomerIdFormat(customerId);
        
        return customerRepository.findById(customerId).map(customer -> {
            log.info("Customer retrieved successfully with ID: {}", customerId);
            return convertToResponse(customer);
        });
    }

    @Transactional
    public CustomerResponseDto updateCustomer(Long customerId, UpdateCustomerRequestDto request) {
        log.info("Updating customer with ID: {}", customerId);

        validateCustomerIdFormat(customerId);
        
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", customerId);
                    return new IllegalArgumentException(
                        String.format("Customer not found with ID: %d", customerId)
                    );
                });

        validateUpdateRequest(request);

        if (request.getFirstName() != null) {
            existingCustomer.setFirstName(request.getFirstName());
        }
        if (request.getMiddleName() != null) {
            existingCustomer.setMiddleName(request.getMiddleName());
        }
        if (request.getLastName() != null) {
            existingCustomer.setLastName(request.getLastName());
        }
        if (request.getAddressLine1() != null) {
            existingCustomer.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            existingCustomer.setAddressLine2(request.getAddressLine2());
        }
        if (request.getAddressLine3() != null) {
            existingCustomer.setAddressLine3(request.getAddressLine3());
        }
        if (request.getStateCode() != null) {
            existingCustomer.setStateCode(request.getStateCode());
        }
        if (request.getCountryCode() != null) {
            existingCustomer.setCountryCode(request.getCountryCode());
        }
        if (request.getZipCode() != null) {
            existingCustomer.setZipCode(request.getZipCode());
        }
        if (request.getPhoneNumber1() != null) {
            existingCustomer.setPhoneNumber1(request.getPhoneNumber1());
        }
        if (request.getPhoneNumber2() != null) {
            existingCustomer.setPhoneNumber2(request.getPhoneNumber2());
        }
        if (request.getSsn() != null) {
            existingCustomer.setSsn(request.getSsn());
        }
        if (request.getGovernmentIssuedId() != null) {
            existingCustomer.setGovernmentIssuedId(request.getGovernmentIssuedId());
        }
        if (request.getDateOfBirth() != null) {
            validateCustomerAge(request.getDateOfBirth());
            existingCustomer.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getEftAccountId() != null) {
            existingCustomer.setEftAccountId(request.getEftAccountId());
        }
        if (request.getPrimaryCardholderIndicator() != null) {
            validatePrimaryCardholderIndicator(request.getPrimaryCardholderIndicator());
            existingCustomer.setPrimaryCardholderIndicator(request.getPrimaryCardholderIndicator());
        }
        if (request.getFicoScore() != null) {
            validateFicoScore(request.getFicoScore());
            existingCustomer.setFicoScore(request.getFicoScore());
        }
        if (request.getFicoCreditScore() != null) {
            existingCustomer.setFicoCreditScore(request.getFicoCreditScore());
        }
        if (request.getCity() != null) {
            existingCustomer.setCity(request.getCity());
        }
        if (request.getPrimaryPhoneNumber() != null) {
            existingCustomer.setPrimaryPhoneNumber(request.getPrimaryPhoneNumber());
        }
        if (request.getSecondaryPhoneNumber() != null) {
            existingCustomer.setSecondaryPhoneNumber(request.getSecondaryPhoneNumber());
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with ID: {}", customerId);
        return convertToResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        
        validateCustomerIdFormat(customerId);
        
        if (!customerRepository.existsById(customerId)) {
            log.error("Customer not found with ID: {}", customerId);
            throw new IllegalArgumentException(
                String.format("Customer not found with ID: %d", customerId)
            );
        }
        
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully with ID: {}", customerId);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        log.info("Retrieving all customers with pagination");
        return customerRepository.findAll(pageable).map(this::convertToResponse);
    }

    private void validateCustomerIdFormat(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() != 9) {
            throw new IllegalArgumentException("Customer ID must be 9 digits");
        }
    }

    private void validateCustomerIdNotExists(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException(
                String.format("Customer with ID %d already exists", customerId)
            );
        }
    }

    private void validateCustomerRequest(CreateCustomerRequestDto request) {
        validateFirstName(request.getFirstName());
        validateMiddleName(request.getMiddleName());
        validateLastName(request.getLastName());
        validateAddressLine1(request.getAddressLine1());
        validateAddressLine3(request.getAddressLine3());
        validateStateCode(request.getStateCode());
        validateCountryCode(request.getCountryCode());
        validateZipCode(request.getZipCode(), request.getStateCode());
        validatePhoneNumber1(request.getPhoneNumber1());
        validatePhoneNumber2(request.getPhoneNumber2());
        validateSsn(request.getSsn());
        validateDateOfBirth(request.getDateOfBirth());
        validateEftAccountId(request.getEftAccountId());
    }

    private void validateUpdateRequest(UpdateCustomerRequestDto request) {
        if (request.getFirstName() != null) {
            validateFirstName(request.getFirstName());
        }
        if (request.getMiddleName() != null) {
            validateMiddleName(request.getMiddleName());
        }
        if (request.getLastName() != null) {
            validateLastName(request.getLastName());
        }
        if (request.getAddressLine1() != null) {
            validateAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine3() != null) {
            validateAddressLine3(request.getAddressLine3());
        }
        if (request.getStateCode() != null) {
            validateStateCode(request.getStateCode());
        }
        if (request.getCountryCode() != null) {
            validateCountryCode(request.getCountryCode());
        }
        if (request.getZipCode() != null && request.getStateCode() != null) {
            validateZipCode(request.getZipCode(), request.getStateCode());
        }
        if (request.getPhoneNumber1() != null) {
            validatePhoneNumber1(request.getPhoneNumber1());
        }
        if (request.getPhoneNumber2() != null) {
            validatePhoneNumber2(request.getPhoneNumber2());
        }
        if (request.getSsn() != null) {
            validateSsn(request.getSsn());
        }
        if (request.getDateOfBirth() != null) {
            validateDateOfBirth(request.getDateOfBirth());
        }
        if (request.getEftAccountId() != null) {
            validateEftAccountId(request.getEftAccountId());
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(firstName).matches()) {
            throw new IllegalArgumentException("First name must contain only letters");
        }
    }

    private void validateMiddleName(String middleName) {
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (!ALPHABETIC_PATTERN.matcher(middleName).matches()) {
                log.warn("Middle name must contain only letters");
            }
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(lastName).matches()) {
            throw new IllegalArgumentException("Last name must contain only letters");
        }
    }

    private void validateAddressLine1(String addressLine1) {
        if (addressLine1 == null || addressLine1.trim().isEmpty()) {
            throw new IllegalArgumentException("Address line 1 is required");
        }
    }

    private void validateAddressLine3(String addressLine3) {
        if (addressLine3 == null || addressLine3.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(addressLine3).matches()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
    }

    private void validateStateCode(String stateCode) {
        if (stateCode == null || !STATE_CODE_PATTERN.matcher(stateCode).matches()) {
            throw new IllegalArgumentException("Invalid state code");
        }
    }

    private void validateCountryCode(String countryCode) {
        if (countryCode == null || !COUNTRY_CODE_PATTERN.matcher(countryCode).matches()) {
            throw new IllegalArgumentException("Invalid country code");
        }
    }

    private void validateZipCode(String zipCode, String stateCode) {
        if (zipCode == null || !ZIP_CODE_PATTERN.matcher(zipCode).matches()) {
            throw new IllegalArgumentException("Invalid zip code for state");
        }
    }

    private void validatePhoneNumber1(String phoneNumber) {
        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        
        String areaCode = phoneNumber.substring(1, 4);
        String prefix = phoneNumber.substring(5, 8);
        String lineNumber = phoneNumber.substring(9, 13);
        
        if (areaCode.equals("000") || prefix.equals("000") || lineNumber.equals("0000")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }

    private void validatePhoneNumber2(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                log.warn("Invalid phone number format");
                return;
            }
            
            String areaCode = phoneNumber.substring(1, 4);
            String prefix = phoneNumber.substring(5, 8);
            String lineNumber = phoneNumber.substring(9, 13);
            
            if (areaCode.equals("000") || prefix.equals("000") || lineNumber.equals("0000")) {
                log.warn("Invalid phone number format");
            }
        }
    }

    private void validateSsn(Long ssn) {
        if (ssn == null) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        
        String ssnStr = String.format("%09d", ssn);
        if (ssnStr.length() != 9) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        
        String areaNumber = ssnStr.substring(0, 3);
        String groupNumber = ssnStr.substring(3, 5);
        String serialNumber = ssnStr.substring(5, 9);
        
        int area = Integer.parseInt(areaNumber);
        if (area == 0 || area == 666 || (area >= 900 && area <= 999)) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        
        if (groupNumber.equals("00")) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        
        if (serialNumber.equals("0000")) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
    }

    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        LocalDate now = LocalDate.now();
        
        if (dateOfBirth.isAfter(now)) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        Period period = Period.between(dateOfBirth, now);
        int age = period.getYears();
        
        if (age < 18) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        if (age > 120) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
    }

    private void validateCustomerAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfBirth, now);
        int age = period.getYears();
        
        if (age < 18) {
            log.error("Customer does not meet minimum age requirement. Age: {}", age);
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        if (age > 120) {
            log.error("Customer age exceeds maximum allowed. Age: {}", age);
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        
        log.info("Customer age validated successfully. Age: {}", age);
    }

    private void validateEftAccountId(String eftAccountId) {
        if (eftAccountId != null && !eftAccountId.trim().isEmpty()) {
            if (!EFT_ACCOUNT_PATTERN.matcher(eftAccountId).matches()) {
                log.warn("EFT Account ID must be 10 digits");
            }
        }
    }

    private void validatePrimaryCardholderIndicator(String indicator) {
        if (indicator == null || (!indicator.equals("Y") && !indicator.equals("N"))) {
            throw new IllegalArgumentException("Primary cardholder indicator must be Y or N");
        }
    }

    private void validateFicoScore(Integer ficoScore) {
        if (ficoScore == null) {
            throw new IllegalArgumentException("FICO Score should be between 300 and 850");
        }
        
        if (ficoScore < 300 || ficoScore > 850) {
            throw new IllegalArgumentException("FICO Score should be between 300 and 850");
        }
    }

    public String formatSsn(Long ssn) {
        if (ssn == null) {
            return null;
        }
        
        String ssnStr = String.format("%09d", ssn);
        return String.format("%s-%s-%s", 
            ssnStr.substring(0, 3), 
            ssnStr.substring(3, 5), 
            ssnStr.substring(5, 9)
        );
    }

    public String formatFullAddress(Customer customer) {
        StringBuilder address = new StringBuilder();
        
        if (customer.getAddressLine1() != null && !customer.getAddressLine1().trim().isEmpty()) {
            address.append(customer.getAddressLine1());
        }
        
        if (customer.getAddressLine2() != null && !customer.getAddressLine2().trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(customer.getAddressLine2());
        }
        
        if (customer.getAddressLine3() != null && !customer.getAddressLine3().trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(customer.getAddressLine3());
        }
        
        if (customer.getStateCode() != null && !customer.getStateCode().trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(customer.getStateCode());
        }
        
        if (customer.getZipCode() != null && !customer.getZipCode().trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(" ");
            }
            address.append(customer.getZipCode());
        }
        
        if (customer.getCountryCode() != null && !customer.getCountryCode().trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(customer.getCountryCode());
        }
        
        return address.toString();
    }

    public String getFicoScoreRating(Integer ficoScore) {
        if (ficoScore == null) {
            return "Unknown";
        }
        
        if (ficoScore >= 800 && ficoScore <= 850) {
            return "Exceptional";
        } else if (ficoScore >= 740 && ficoScore < 800) {
            return "Very Good";
        } else if (ficoScore >= 670 && ficoScore < 740) {
            return "Good";
        } else if (ficoScore >= 580 && ficoScore < 670) {
            return "Fair";
        } else if (ficoScore >= 300 && ficoScore < 580) {
            return "Poor";
        } else {
            return "Invalid";
        }
    }

    public int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        
        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfBirth, now);
        return period.getYears();
    }

    private CustomerResponseDto convertToResponse(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setMiddleName(customer.getMiddleName());
        response.setLastName(customer.getLastName());
        response.setFullName(customer.getFullName());
        response.setAddressLine1(customer.getAddressLine1());
        response.setAddressLine2(customer.getAddressLine2());
        response.setAddressLine3(customer.getAddressLine3());
        response.setStateCode(customer.getStateCode());
        response.setCountryCode(customer.getCountryCode());
        response.setZipCode(customer.getZipCode());
        response.setPhoneNumber1(customer.getPhoneNumber1());
        response.setPhoneNumber2(customer.getPhoneNumber2());
        response.setSsn(formatSsn(customer.getSsn()));
        response.setSsnRaw(customer.getSsn());
        response.setGovernmentIssuedId(customer.getGovernmentIssuedId());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setAge(calculateAge(customer.getDateOfBirth()));
        response.setEftAccountId(customer.getEftAccountId());
        response.setPrimaryCardholderIndicator(customer.getPrimaryCardholderIndicator());
        response.setPrimaryCardholderStatus(customer.isPrimaryCardholder() ? "Primary Cardholder" : "Secondary Cardholder");
        response.setFicoScore(customer.getFicoScore());
        response.setFicoScoreRating(getFicoScoreRating(customer.getFicoScore()));
        response.setFicoCreditScore(customer.getFicoCreditScore());
        response.setCity(customer.getCity());
        response.setPrimaryPhoneNumber(customer.getPrimaryPhoneNumber());
        response.setSecondaryPhoneNumber(customer.getSecondaryPhoneNumber());
        response.setFullAddress(formatFullAddress(customer));
        response.setMeetsAgeRequirement(customer.meetsAgeRequirement());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}
