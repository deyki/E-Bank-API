package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;

public interface AdminService {

    ResponseMessage banBankAccountByIban(BankAccountRequest bankAccountRequest) throws BankAccountNotFoundException;
}
