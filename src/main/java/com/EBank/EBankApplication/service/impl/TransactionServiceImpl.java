package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.entity.Transaction;
import com.EBank.EBankApplication.enums.TransactionType;
import com.EBank.EBankApplication.error.BanException;
import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import com.EBank.EBankApplication.repository.TransactionRepository;
import com.EBank.EBankApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;

    }

    @Override
    public Boolean bankAccountBan(String iban) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository
                .findByIban(iban)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found!"));

        return bankAccount.getBanned();
    }

    @Override
    public ResponseMessage deposit(DepositRequest depositRequest) throws BankAccountNotFoundException, BanException {
        Boolean banned = bankAccountBan(depositRequest.iban());
        if (banned) {
            throw new BanException("This bank account is banned!");
        }

        BankAccount bankAccount = bankAccountRepository
                .findByIban(depositRequest.iban())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found!"));

        Float bankAccountBalance = bankAccount.getBalance();
        bankAccount.setBalance(bankAccountBalance + depositRequest.amount());
        bankAccountRepository.save(bankAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setSender(bankAccount.getUser().getUsername());
        transaction.setReceiver(bankAccount.getUser().getUsername());
        transaction.setAmount(depositRequest.amount());
        transaction.setDate(new Date());
        transactionRepository.save(transaction);

        return new ResponseMessage("Deposit was made successfully!");
    }

    @Override
    public ResponseMessage withDraw(WithDrawRequest withDrawRequest) throws BankAccountNotFoundException, BankAccountBalanceException, BanException {
        Boolean banned = bankAccountBan(withDrawRequest.iban());
        if (banned) {
            throw new BanException("This bank account is banned!");
        }

        BankAccount bankAccount = bankAccountRepository
                .findByIban(withDrawRequest.iban())
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found!"));

        Float bankAccountBalance = bankAccount.getBalance();
        if (bankAccountBalance < withDrawRequest.amount()) {
            throw new BankAccountBalanceException("Bank account balance is lower than the withdraw amount!");
        } else {
            bankAccount.setBalance(bankAccountBalance - withDrawRequest.amount());
            bankAccountRepository.save(bankAccount);
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSender(bankAccount.getUser().getUsername());
        transaction.setReceiver(bankAccount.getUser().getUsername());
        transaction.setAmount(withDrawRequest.amount());
        transaction.setDate(new Date());
        transactionRepository.save(transaction);

        return new ResponseMessage("Withdraw was made successfully!");
    }

    @Override
    public ResponseMessage transfer(TransferRequest transferRequest) throws BankAccountNotFoundException, BankAccountBalanceException, BanException {
        Boolean senderAccountBanned = bankAccountBan(transferRequest.senderIban());
        if (senderAccountBanned) {
            throw new BanException("This bank account is banned!");
        }

        Boolean receiverAccountBanned = bankAccountBan(transferRequest.receiverIban());
        if (receiverAccountBanned) {
            throw new BanException("This bank account is banned!");
        }

        BankAccount senderBankAccount = bankAccountRepository
                .findByIban(transferRequest.senderIban())
                .orElseThrow(() -> new BankAccountNotFoundException("Invalid sender iban!"));

        BankAccount receiverBankAccount = bankAccountRepository
                .findByIban(transferRequest.receiverIban())
                .orElseThrow(() -> new BankAccountNotFoundException("Invalid receiver iban!"));

        Float senderBankAccountBalance = senderBankAccount.getBalance();
        Float receiverBankAccountBalance = receiverBankAccount.getBalance();
        if (senderBankAccountBalance < transferRequest.amount()) {
            throw new BankAccountBalanceException("Not enough money in bank account for transfer!");
        } else {
            senderBankAccount.setBalance(senderBankAccountBalance - transferRequest.amount());
            receiverBankAccount.setBalance(receiverBankAccountBalance + transferRequest.amount());
            bankAccountRepository.save(senderBankAccount);
            bankAccountRepository.save(receiverBankAccount);
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setSender(senderBankAccount.getUser().getUsername());
        transaction.setReceiver(receiverBankAccount.getUser().getUsername());
        transaction.setAmount(transferRequest.amount());
        transaction.setDate(new Date());
        transactionRepository.save(transaction);

        return new ResponseMessage("Transfer was made successfully!");
    }

    @Override
    public List<TransactionResponse> getTransactionsByTypeAndSenderUsername(TransactionRequest transactionRequest) {
        return transactionRepository
                .findAll()
                .stream()
                .filter(transaction -> transaction.getSender().equals(transactionRequest.username()))
                .filter(transaction -> transaction.getTransactionType().equals(transactionRequest.transactionType()))
                .map(transaction -> new TransactionResponse(
                        transaction.getTransactionType(),
                        transaction.getSender(),
                        transaction.getReceiver(),
                        transaction.getAmount(),
                        transaction.getDate()
                )).collect(Collectors.toList());
    }
}