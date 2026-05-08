package com.quickpe.service;

import com.quickpe.entity.BankAccount;
import com.quickpe.entity.User;
import com.quickpe.repository.BankAccountRepository;
import com.quickpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * BankAccountService - Business logic for bank account operations
 * Handles linking, verification, and management of bank accounts
 */
@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Link a new bank account
     */
    public BankAccount linkBankAccount(
            Long userId,
            String accountHolderName,
            String accountNumber,
            String ifscCode,
            String bankName,
            BankAccount.AccountType accountType,
            String upiId) {

        // Check if account already exists
        if (bankAccountRepository.existsByAccountNumber(accountNumber)) {
            throw new RuntimeException("Account number already registered");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .accountHolderName(accountHolderName)
                .accountNumber(accountNumber)
                .ifscCode(ifscCode.toUpperCase())
                .bankName(bankName)
                .accountType(accountType)
                .upiId(upiId)
                .isPrimary(false)
                .isVerified(false)
                .build();

        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Get all bank accounts for a user
     */
    public List<BankAccount> getUserBankAccounts(Long userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    /**
     * Get bank account by ID
     */
    public BankAccount getBankAccountById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
    }

    /**
     * Get primary bank account
     */
    public BankAccount getPrimaryBankAccount(Long userId) {
        return bankAccountRepository.findByUserIdAndIsPrimaryTrue(userId)
                .orElseThrow(() -> new RuntimeException("No primary bank account found"));
    }

    /**
     * Set as primary bank account
     */
    public BankAccount setPrimaryBankAccount(Long userId, Long accountId) {
        // Remove primary from other accounts
        bankAccountRepository.findByUserIdAndIsPrimaryTrue(userId)
                .ifPresent(account -> {
                    account.setIsPrimary(false);
                    bankAccountRepository.save(account);
                });

        // Set new primary
        BankAccount bankAccount = getBankAccountById(accountId);
        if (!bankAccount.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        bankAccount.setIsPrimary(true);
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Verify bank account
     */
    public BankAccount verifyBankAccount(Long accountId) {
        BankAccount bankAccount = getBankAccountById(accountId);
        bankAccount.setIsVerified(true);
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Delete bank account
     */
    public void deleteBankAccount(Long accountId) {
        bankAccountRepository.deleteById(accountId);
    }

    /**
     * Find account by UPI ID
     */
    public BankAccount findByUpiId(String upiId) {
        return bankAccountRepository.findByUpiId(upiId)
                .orElseThrow(() -> new RuntimeException("UPI account not found"));
    }
}
