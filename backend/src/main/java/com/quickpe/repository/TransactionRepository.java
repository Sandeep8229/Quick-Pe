package com.quickpe.repository;

import com.quickpe.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * TransactionRepository - Data access for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionRefId(String transactionRefId);
    
    List<Transaction> findAllBySenderIdOrderByCreatedAtDesc(Long senderId);
    
    List<Transaction> findAllByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId OR t.receiver.id = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findAllByUserId(Long userId);
}
