package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    @Autowired
    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String adminPanel() {
        return "Admin panel!";
    }

    @PostMapping("/bankAccount-ban")
    public ResponseEntity<ResponseMessage> banBankAccountByIban(@RequestBody BankAccountRequest bankAccountRequest) throws BankAccountNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.banBankAccountByIban(bankAccountRequest));
    }
}