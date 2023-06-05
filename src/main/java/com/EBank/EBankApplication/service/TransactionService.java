package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.enums.TransactionType;
import com.EBank.EBankApplication.error.BanException;
import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;

import java.util.List;

public interface TransactionService {
    Boolean bankAccountBan(String iban) throws BankAccountNotFoundException;
    ResponseMessage deposit(DepositRequest depositRequest) throws BankAccountNotFoundException, BanException;

    ResponseMessage withDraw(WithDrawRequest withDrawRequest) throws BankAccountNotFoundException, BankAccountBalanceException, BanException;

    ResponseMessage transfer(TransferRequest transferRequest) throws BankAccountNotFoundException, BankAccountBalanceException, BanException;

    List<TransactionResponse> getTransactionsByTypeAndSenderUsername(TransactionRequest transactionRequest);
}