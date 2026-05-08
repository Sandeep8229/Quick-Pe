package com.quickpe.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Wallet Entity - Represents user's digital wallet
 * Stores balance and transaction amounts
 */
@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "total_money_added", precision = 10, scale = 2)
    private BigDecimal totalMoneyAdded = BigDecimal.ZERO;

    @Column(name = "total_money_spent", precision = 10, scale = 2)
    private BigDecimal totalMoneySpent = BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add money to wallet
     */
    public void addMoney(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.totalMoneyAdded = this.totalMoneyAdded.add(amount);
    }

    /**
     * Deduct money from wallet
     */
    public boolean deductMoney(BigDecimal amount) {
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            this.totalMoneySpent = this.totalMoneySpent.add(amount);
            return true;
        }
        return false;
    }
}
