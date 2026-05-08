package com.quickpe.controller;

import com.quickpe.entity.Transaction;
import com.quickpe.security.CurrentUser;
import com.quickpe.security.UserPrincipal;
import com.quickpe.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * TransactionController - REST API for transactions
 * Handles money transfers and requests
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Send money
     * POST /api/transactions/send-money
     */
    @PostMapping("/send-money")
    public ResponseEntity<?> sendMoney(
            @CurrentUser UserPrincipal currentUser,
            @RequestBody SendMoneyRequest request) {
        try {
            Transaction transaction = transactionService.sendMoney(
                    currentUser.getId(),
                    request.getReceiverIdentifier(),
                    request.getAmount(),
                    request.getDescription()
            );
            return ResponseEntity.ok(new TransactionResponse(true, "Money sent successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransactionResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Request money
     * POST /api/transactions/request-money
     */
    @PostMapping("/request-money")
    public ResponseEntity<?> requestMoney(
            @CurrentUser UserPrincipal currentUser,
            @RequestBody RequestMoneyRequest request) {
        try {
            Transaction transaction = transactionService.requestMoney(
                    currentUser.getId(),
                    request.getTargetIdentifier(),
                    request.getAmount(),
                    request.getDescription()
            );
            return ResponseEntity.ok(new TransactionResponse(true, "Money requested", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransactionResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Get transaction history
     * GET /api/transactions/history
     */
    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(@CurrentUser UserPrincipal currentUser) {
        try {
            List<Transaction> transactions = transactionService.getTransactionHistory(currentUser.getId());
            return ResponseEntity.ok(new TransactionResponse(true, "Transaction history retrieved", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransactionResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Get transaction by reference ID
     * GET /api/transactions/{refId}
     */
    @GetMapping("/{refId}")
    public ResponseEntity<?> getTransaction(@PathVariable String refId) {
        try {
            Transaction transaction = transactionService.getTransactionByRefId(refId);
            return ResponseEntity.ok(new TransactionResponse(true, "Transaction retrieved", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransactionResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Send Money Request DTO
     */
    public static class SendMoneyRequest {
        private String receiverIdentifier;
        private BigDecimal amount;
        private String description;

        public String getReceiverIdentifier() { return receiverIdentifier; }
        public void setReceiverIdentifier(String receiverIdentifier) { this.receiverIdentifier = receiverIdentifier; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * Request Money Request DTO
     */
    public static class RequestMoneyRequest {
        private String targetIdentifier;
        private BigDecimal amount;
        private String description;

        public String getTargetIdentifier() { return targetIdentifier; }
        public void setTargetIdentifier(String targetIdentifier) { this.targetIdentifier = targetIdentifier; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * Transaction Response wrapper
     */
    public static class TransactionResponse {
        private Boolean success;
        private String message;
        private Object data;

        public TransactionResponse(Boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
