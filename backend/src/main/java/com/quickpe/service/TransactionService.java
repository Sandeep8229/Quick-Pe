package com.quickpe.service;

import com.quickpe.entity.Transaction;
import com.quickpe.entity.User;
import com.quickpe.repository.TransactionRepository;
import com.quickpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * TransactionService - Business logic for transaction operations
 * Handles money transfers, requests, and transaction history
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    /**
     * Send money from sender to receiver
     */
    public Transaction sendMoney(Long senderId, String receiverIdentifier, BigDecimal amount, String description) {
        // Get sender
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Find receiver by mobile or email
        User receiver = userRepository.findByMobileNumber(receiverIdentifier)
                .or(() -> userRepository.findByEmail(receiverIdentifier))
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check sufficient balance
        if (!walletService.hasSufficientBalance(senderId, amount)) {
            throw new RuntimeException("Insufficient balance");
        }

        // Transfer money
        walletService.transferMoney(senderId, receiver.getId(), amount);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .type(Transaction.TransactionType.SENT)
                .status(Transaction.TransactionStatus.COMPLETED)
                .description(description)
                .transactionRefId(generateTransactionRefId())
                .build();

        // Create corresponding received transaction for receiver
        Transaction receivedTransaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .type(Transaction.TransactionType.RECEIVED)
                .status(Transaction.TransactionStatus.COMPLETED)
                .description(description)
                .transactionRefId(transaction.getTransactionRefId())
                .build();

        transactionRepository.save(transaction);
        transactionRepository.save(receivedTransaction);

        return transaction;
    }

    /**
     * Request money from another user
     */
    public Transaction requestMoney(Long requesterId, String targetIdentifier, BigDecimal amount, String description) {
        // Get requester
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        // Find target user
        User target = userRepository.findByMobileNumber(targetIdentifier)
                .or(() -> userRepository.findByEmail(targetIdentifier))
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // Create money request transaction
        Transaction transaction = Transaction.builder()
                .sender(target)
                .receiver(requester)
                .amount(amount)
                .type(Transaction.TransactionType.REQUESTED)
                .status(Transaction.TransactionStatus.PENDING)
                .description(description)
                .transactionRefId(generateTransactionRefId())
                .build();

        return transactionRepository.save(transaction);
    }

    /**
     * Get transaction history for user
     */
    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    /**
     * Get sent transactions
     */
    public List<Transaction> getSentTransactions(Long userId) {
        return transactionRepository.findAllBySenderIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Get received transactions
     */
    public List<Transaction> getReceivedTransactions(Long userId) {
        return transactionRepository.findAllByReceiverIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Get transaction by reference ID
     */
    public Transaction getTransactionByRefId(String transactionRefId) {
        return transactionRepository.findByTransactionRefId(transactionRefId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    /**
     * Generate unique transaction reference ID
     */
    private String generateTransactionRefId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    /**
     * Cancel pending money request
     */
    public Transaction cancelMoneyRequest(String transactionRefId) {
        Transaction transaction = getTransactionByRefId(transactionRefId);

        if (!transaction.getStatus().equals(Transaction.TransactionStatus.PENDING)) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        transaction.setStatus(Transaction.TransactionStatus.CANCELLED);
        return transactionRepository.save(transaction);
    }
}
