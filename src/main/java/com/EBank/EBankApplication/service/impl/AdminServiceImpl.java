package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import com.EBank.EBankApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public AdminServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public ResponseMessage banBankAccountByIban(BankAccountRequest bankAccountRequest) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository
                .findByIban(bankAccountRequest.iban())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found!"));

        bankAccount.setBanned(true);
        bankAccountRepository.save(bankAccount);

        return new ResponseMessage(
                String.format("Bank account with username %s and iban %s banned!",
                        bankAccount.getUser().getUsername(), bankAccount.getIban()));
    }
}
