package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;
import com.EBank.EBankApplication.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResponseMessage> deposit(@RequestBody DepositRequest depositRequest) throws BankAccountNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.deposit(depositRequest));
    }

    @PostMapping("/withDraw")
    public ResponseEntity<ResponseMessage> withDraw(@RequestBody WithDrawRequest withDrawRequest) throws BankAccountBalanceException, BankAccountNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.withDraw(withDrawRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseMessage> transfer(@RequestBody TransferRequest transferRequest) throws BankAccountBalanceException, BankAccountNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.transfer(transferRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUsernameAndType(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.getTransactionsByTypeAndSenderUsername(transactionRequest));
    }
}
