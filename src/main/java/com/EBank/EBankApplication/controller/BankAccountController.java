package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.model.bankAccount.BankAccountResponse;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.service.impl.BankAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bankAccount")
public class BankAccountController {

    private final BankAccountServiceImpl bankAccountService;

    @Autowired
    public BankAccountController(BankAccountServiceImpl bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create/{userID}")
    public ResponseEntity<ResponseMessage> createBankAccount(@PathVariable Long userID, @RequestBody BankAccountRequest bankAccountRequest) throws UserNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bankAccountService.createBankAccount(userID, bankAccountRequest));
    }

    @GetMapping("/get/{userID}")
    public ResponseEntity<BankAccountResponse> getBankAccountByUserID(@PathVariable Long userID) throws UserNotFoundException, BankAccountNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bankAccountService.getBankAccountByUserID(userID));
    }
}
