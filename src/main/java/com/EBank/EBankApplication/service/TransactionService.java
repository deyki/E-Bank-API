package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.enums.TransactionType;
import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;

import java.util.List;

public interface TransactionService {
    ResponseMessage deposit(DepositRequest depositRequest) throws BankAccountNotFoundException;

    ResponseMessage withDraw(WithDrawRequest withDrawRequest) throws BankAccountNotFoundException, BankAccountBalanceException;

    ResponseMessage transfer(TransferRequest transferRequest) throws BankAccountNotFoundException, BankAccountBalanceException;

    List<TransactionResponse> getTransactionsByTypeAndSenderUsername(TransactionRequest transactionRequest);
}