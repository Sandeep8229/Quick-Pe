package com.quickpe.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * BankAccount Entity - Represents linked bank accounts
 * Stores bank account details for users
 */
@Entity
@Table(name = "bank_accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "account_number"),
        @UniqueConstraint(columnNames = "upi_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "upi_id", unique = true)
    private String upiId;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Account Type Enum
     */
    public enum AccountType {
        SAVINGS,
        CHECKING,
        BUSINESS
    }
}
