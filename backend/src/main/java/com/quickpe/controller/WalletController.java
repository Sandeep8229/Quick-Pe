package com.quickpe.controller;

import com.quickpe.entity.Wallet;
import com.quickpe.security.CurrentUser;
import com.quickpe.security.UserPrincipal;
import com.quickpe.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * WalletController - REST API for wallet operations
 * Handles wallet balance and money management
 */
@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WalletController {

    @Autowired
    private WalletService walletService;

    /**
     * Get wallet balance
     * GET /api/wallet/balance
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@CurrentUser UserPrincipal currentUser) {
        try {
            BigDecimal balance = walletService.getBalance(currentUser.getId());
            return ResponseEntity.ok(new WalletResponse(true, "Balance retrieved", balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new WalletResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Add money to wallet
     * POST /api/wallet/add-money
     */
    @PostMapping("/add-money")
    public ResponseEntity<?> addMoney(
            @CurrentUser UserPrincipal currentUser,
            @RequestBody AddMoneyRequest request) {
        try {
            Wallet wallet = walletService.addMoney(currentUser.getId(), request.getAmount());
            return ResponseEntity.ok(new WalletResponse(true, "Money added successfully", wallet.getBalance()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new WalletResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Get wallet details
     * GET /api/wallet/details
     */
    @GetMapping("/details")
    public ResponseEntity<?> getWallet(@CurrentUser UserPrincipal currentUser) {
        try {
            Wallet wallet = walletService.getWalletByUserId(currentUser.getId());
            return ResponseEntity.ok(new WalletResponse(true, "Wallet details retrieved", wallet));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new WalletResponse(false, e.getMessage(), null));
        }
    }

    /**
     * Request class for adding money
     */
    public static class AddMoneyRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    /**
     * Wallet Response wrapper
     */
    public static class WalletResponse {
        private Boolean success;
        private String message;
        private Object data;

        public WalletResponse(Boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
