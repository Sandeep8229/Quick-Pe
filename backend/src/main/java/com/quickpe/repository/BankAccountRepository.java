package com.quickpe.repository;

import com.quickpe.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * BankAccountRepository - Data access for BankAccount entity
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUserId(Long userId);
    Optional<BankAccount> findByUserIdAndIsPrimaryTrue(Long userId);
    Optional<BankAccount> findByUpiId(String upiId);
    Boolean existsByAccountNumber(String accountNumber);
}
