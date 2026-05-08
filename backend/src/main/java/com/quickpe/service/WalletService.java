package com.quickpe.service;

import com.quickpe.entity.User;
import com.quickpe.entity.Wallet;
import com.quickpe.repository.UserRepository;
import com.quickpe.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * WalletService - Business logic for wallet operations
 * Handles wallet creation, balance management, and money transfers
 */
@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create wallet for new user
     */
    public Wallet createWallet(User user) {
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .totalMoneyAdded(BigDecimal.ZERO)
                .totalMoneySpent(BigDecimal.ZERO)
                .isActive(true)
                .build();

        return walletRepository.save(wallet);
    }

    /**
     * Get wallet by user ID
     */
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));
    }

    /**
     * Get wallet balance
     */
    public BigDecimal getBalance(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getBalance();
    }

    /**
     * Add money to wallet
     */
    public Wallet addMoney(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        Wallet wallet = getWalletByUserId(userId);
        wallet.addMoney(amount);
        return walletRepository.save(wallet);
    }

    /**
     * Deduct money from wallet
     */
    public Wallet deductMoney(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        Wallet wallet = getWalletByUserId(userId);
        boolean success = wallet.deductMoney(amount);

        if (!success) {
            throw new RuntimeException("Insufficient balance");
        }

        return walletRepository.save(wallet);
    }

    /**
     * Transfer money between wallets
     */
    public boolean transferMoney(Long senderId, Long receiverId, BigDecimal amount) {
        Wallet senderWallet = getWalletByUserId(senderId);
        Wallet receiverWallet = getWalletByUserId(receiverId);

        if (senderWallet.deductMoney(amount)) {
            receiverWallet.addMoney(amount);
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);
            return true;
        }

        return false;
    }

    /**
     * Check if user has sufficient balance
     */
    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getBalance().compareTo(amount) >= 0;
    }
}
