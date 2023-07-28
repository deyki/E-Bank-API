package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.BanException;
import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;
import com.EBank.EBankApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResponseMessage> deposit(@RequestBody DepositRequest depositRequest) throws BankAccountNotFoundException, BanException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.deposit(depositRequest));
    }

    @PostMapping("/withDraw")
    public ResponseEntity<ResponseMessage> withDraw(@RequestBody WithDrawRequest withDrawRequest) throws BankAccountBalanceException, BankAccountNotFoundException, BanException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.withDraw(withDrawRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseMessage> transfer(@RequestBody TransferRequest transferRequest) throws BankAccountBalanceException, BankAccountNotFoundException, BanException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.transfer(transferRequest));
    }

    @GetMapping("/allDeposits")
    public ResponseEntity<List<TransactionResponse>> getAllDeposits(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getDepositTransactionBySender(transactionRequest));
    }

    @GetMapping("/allWithDraws")
    public ResponseEntity<List<TransactionResponse>> getAllWithDraws(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getWithDrawTransactionBySender(transactionRequest));
    }

    @GetMapping("/allTransfers")
    public ResponseEntity<List<TransactionResponse>> getAllTransfers(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getTransferTransactionBySender(transactionRequest));
    }
}
