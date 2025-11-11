package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.demo.enums.StatementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "statements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 75)
    private String customerName;

    @Column(name = "customer_address", nullable = false, length = 150)
    private String customerAddress;

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "fico_score", nullable = false, length = 3)
    private Integer ficoScore;

    @Column(name = "total_transaction_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalTransactionAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_reference_id", nullable = false)
    private Account account;

    @Column(name = "statement_period_start")
    private LocalDateTime statementPeriodStart;

    @Column(name = "statement_period_end")
    private LocalDateTime statementPeriodEnd;

    @Column(name = "plain_text_content", columnDefinition = "TEXT")
    private String plainTextContent;

    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatementStatus status = StatementStatus.GENERATED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Statement(String customerName, String customerAddress, Long accountId, 
                    BigDecimal currentBalance, Integer ficoScore, BigDecimal totalTransactionAmount) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.ficoScore = ficoScore;
        this.totalTransactionAmount = totalTransactionAmount;
        this.status = StatementStatus.GENERATED;
    }

    @PrePersist
    @PreUpdate
    public void validateAccountId() {
        if (accountId != null) {
            String accountIdStr = String.valueOf(accountId);
            if (accountIdStr.length() != 11) {
                throw new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file");
            }
        }
    }

    /**
     * BR006: Customer Name Composition
     * Compose full customer name from first, middle (if present), and last name
     */
    public void composeCustomerName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        
        if (middleName != null && !middleName.trim().isEmpty() && !middleName.trim().equals(" ")) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(middleName.trim());
        }
        
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName.trim());
        }
        
        this.customerName = fullName.toString();
    }

    /**
     * BR007: Complete Address Display
     * Compose complete customer address including all available address lines, state, country, and ZIP code
     */
    public void composeCompleteAddress(String addressLine1, String addressLine2, String addressLine3,
                                      String stateCode, String countryCode, String zipCode) {
        StringBuilder address = new StringBuilder();
        
        if (addressLine1 != null && !addressLine1.trim().isEmpty()) {
            address.append(addressLine1.trim());
        }
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(addressLine2.trim());
        }
        
        if (addressLine3 != null && !addressLine3.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(addressLine3.trim());
        }
        
        if (stateCode != null && !stateCode.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(stateCode.trim());
        }
        
        if (countryCode != null && !countryCode.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(countryCode.trim());
        }
        
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(" ");
            }
            address.append(zipCode.trim());
        }
        
        this.customerAddress = address.toString();
    }

    /**
     * BR003: Transaction Amount Summation
     * Calculate total transaction amount by summing all transaction amounts
     */
    public void calculateTotalTransactionAmount(BigDecimal... transactionAmounts) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (transactionAmounts != null) {
            for (BigDecimal amount : transactionAmounts) {
                if (amount != null) {
                    total = total.add(amount);
                }
            }
        }
        
        this.totalTransactionAmount = total;
    }

    /**
     * BR004: Dual Format Statement Output - Plain Text
     * Generate plain text format statement
     */
    public void generatePlainTextStatement(String bankName, String bankAddress) {
        StringBuilder statement = new StringBuilder();
        
        statement.append("=".repeat(80)).append("\n");
        statement.append(bankName).append("\n");
        statement.append(bankAddress).append("\n");
        statement.append("=".repeat(80)).append("\n\n");
        
        statement.append("CUSTOMER INFORMATION\n");
        statement.append("-".repeat(80)).append("\n");
        statement.append("Name: ").append(this.customerName).append("\n");
        statement.append("Address: ").append(this.customerAddress).append("\n\n");
        
        statement.append("ACCOUNT DETAILS\n");
        statement.append("-".repeat(80)).append("\n");
        statement.append("Account ID: ").append(this.accountId).append("\n");
        statement.append("Current Balance: $").append(this.currentBalance).append("\n");
        statement.append("FICO Score: ").append(this.ficoScore).append("\n\n");
        
        statement.append("TRANSACTION SUMMARY\n");
        statement.append("-".repeat(80)).append("\n");
        statement.append("Total Transaction Amount: $").append(this.totalTransactionAmount).append("\n");
        statement.append("=".repeat(80)).append("\n");
        
        this.plainTextContent = statement.toString();
    }

    /**
     * BR004: Dual Format Statement Output - HTML
     * BR008: HTML Statement Styling Standards
     * Generate HTML format statement with specific styling standards
     */
    public void generateHtmlStatement(String bankName, String bankAddress) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<style>\n");
        html.append("body { font-family: 'Segoe UI', sans-serif; font-size: 12px; }\n");
        html.append("table { width: 70%; margin: 0 auto; border-collapse: collapse; }\n");
        html.append(".bank-info { background-color: #FFAF33; padding: 15px; text-align: center; font-size: 16px; font-weight: bold; }\n");
        html.append(".customer-info { background-color: #f2f2f2; padding: 15px; }\n");
        html.append(".section-header { background-color: #33FFD1; padding: 10px; text-align: center; font-size: 16px; font-weight: bold; }\n");
        html.append(".column-header { background-color: #33FF5E; padding: 8px; font-size: 16px; font-weight: bold; }\n");
        html.append(".amount { text-align: right; }\n");
        html.append("td, th { padding: 8px; border: 1px solid #ddd; }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        
        html.append("<table>\n");
        html.append("<tr><td class='bank-info' colspan='2'>").append(bankName).append("<br>").append(bankAddress).append("</td></tr>\n");
        
        html.append("<tr><td class='section-header' colspan='2'>CUSTOMER INFORMATION</td></tr>\n");
        html.append("<tr class='customer-info'><td><strong>Name:</strong></td><td>").append(this.customerName).append("</td></tr>\n");
        html.append("<tr class='customer-info'><td><strong>Address:</strong></td><td>").append(this.customerAddress).append("</td></tr>\n");
        
        html.append("<tr><td class='section-header' colspan='2'>ACCOUNT DETAILS</td></tr>\n");
        html.append("<tr><td><strong>Account ID:</strong></td><td>").append(this.accountId).append("</td></tr>\n");
        html.append("<tr><td><strong>Current Balance:</strong></td><td class='amount'>$").append(this.currentBalance).append("</td></tr>\n");
        html.append("<tr><td><strong>FICO Score:</strong></td><td>").append(this.ficoScore).append("</td></tr>\n");
        
        html.append("<tr><td class='section-header' colspan='2'>TRANSACTION SUMMARY</td></tr>\n");
        html.append("<tr class='column-header'><td>Description</td><td>Amount</td></tr>\n");
        html.append("<tr><td><strong>Total Transaction Amount:</strong></td><td class='amount'>$").append(this.totalTransactionAmount).append("</td></tr>\n");
        
        html.append("</table>\n");
        html.append("</body>\n</html>");
        
        this.htmlContent = html.toString();
    }

    /**
     * BR004: Dual Format Statement Output
     * Generate both plain text and HTML format statements
     */
    public void generateDualFormatStatements(String bankName, String bankAddress) {
        generatePlainTextStatement(bankName, bankAddress);
        generateHtmlStatement(bankName, bankAddress);
    }

    /**
     * Validate account ID format
     */
    public boolean isAccountIdValid() {
        if (accountId == null) {
            return false;
        }
        String accountIdStr = String.valueOf(accountId);
        return accountIdStr.length() == 11 && accountIdStr.matches("\\d{11}");
    }
}
