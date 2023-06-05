package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.model.bankAccount.BankAccountResponse;
import com.EBank.EBankApplication.model.ResponseMessage;

public interface BankAccountService {
    ResponseMessage createBankAccount(Long userID, BankAccountRequest bankAccountRequest) throws UserNotFoundException;

    BankAccountResponse getBankAccountByUserID(Long userID) throws UserNotFoundException, BankAccountNotFoundException;
}
