package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.entity.Transaction;
import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.enums.TransactionType;
import com.EBank.EBankApplication.error.BanException;
import com.EBank.EBankApplication.error.BankAccountBalanceException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.transaction.*;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import com.EBank.EBankApplication.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionServiceImpl transactionService;

    private Transaction transactionDeposit;
    private Transaction transactionWithDraw;
    private Transaction transactionTransfer;
    private BankAccount bankAccount1;
    private BankAccount bankAccount2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        this.user1 = User
                .builder()
                .userID(1L)
                .username("User")
                .password("Password")
                .bankAccount(bankAccount1)
                .build();

        this.bankAccount1 = BankAccount
                .builder()
                .bankAccountID(10L)
                .iban("122333")
                .balance(100.00F)
                .user(user1)
                .banned(false)
                .build();

        this.user2 = User
                .builder()
                .userID(2L)
                .username("User2")
                .password("Password2")
                .bankAccount(bankAccount2)
                .build();

        this.bankAccount2 = BankAccount
                .builder()
                .bankAccountID(20L)
                .iban("333221")
                .balance(200.00F)
                .user(user2)
                .banned(false)
                .build();

        this.transactionDeposit = Transaction
                .builder()
                .transactionID(100L)
                .transactionType(TransactionType.DEPOSIT)
                .amount(100.00F)
                .sender(user1.getUsername())
                .receiver(user1.getUsername())
                .date(new Date())
                .build();

        this.transactionWithDraw = Transaction
                .builder()
                .transactionID(200L)
                .transactionType(TransactionType.WITHDRAW)
                .amount(20.00F)
                .sender(user1.getUsername())
                .receiver(user1.getUsername())
                .date(new Date())
                .build();

        this.transactionTransfer = Transaction
                .builder()
                .transactionID(300L)
                .transactionType(TransactionType.TRANSFER)
                .amount(20.00F)
                .sender(user1.getUsername())
                .receiver(user2.getUsername())
                .date(new Date())
                .build();
    }

    @Test
    void whenCheckBankAccountBan_thenReturnCorrectResult() throws BankAccountNotFoundException {
        Mockito.when(bankAccountRepository.findByIban(bankAccount1.getIban())).thenReturn(Optional.of(bankAccount1));

        boolean isBanned = transactionService.bankAccountBan(bankAccount1.getIban());

        assertFalse(isBanned);
    }

    @Test
    void whenDeposit_thenReturnCorrectResult() throws BankAccountNotFoundException, BanException {
        Mockito.when(bankAccountRepository.findByIban(bankAccount1.getIban())).thenReturn(Optional.of(bankAccount1));
        Mockito.when(bankAccountRepository.save(bankAccount1)).thenReturn(bankAccount1);
        Mockito.when(transactionRepository.save(transactionDeposit)).thenReturn(transactionDeposit);

        transactionService.deposit(new DepositRequest(bankAccount1.getIban(), 20.00F));

        assertEquals(bankAccount1.getBalance(), 120.00F);
    }

    @Test
    void whenWithDraw_thenReturnCorrectResult() throws BankAccountBalanceException, BankAccountNotFoundException, BanException {
        Mockito.when(bankAccountRepository.findByIban(bankAccount1.getIban())).thenReturn(Optional.of(bankAccount1));
        Mockito.when(bankAccountRepository.save(bankAccount1)).thenReturn(bankAccount1);
        Mockito.when(transactionRepository.save(transactionWithDraw)).thenReturn(transactionWithDraw);

        transactionService.withDraw(new WithDrawRequest(bankAccount1.getIban(), 20.00F));

        assertEquals(bankAccount1.getBalance(), 80.00F);
    }

    @Test
    void whenTransfer_thenReturnCorrectResult() throws BankAccountBalanceException, BankAccountNotFoundException, BanException {
        Mockito.when(bankAccountRepository.findByIban(bankAccount1.getIban())).thenReturn(Optional.of(bankAccount1));
        Mockito.when(bankAccountRepository.findByIban(bankAccount2.getIban())).thenReturn(Optional.of(bankAccount2));
        Mockito.when(bankAccountRepository.save(bankAccount1)).thenReturn(bankAccount1);
        Mockito.when(bankAccountRepository.save(bankAccount2)).thenReturn(bankAccount2);
        Mockito.when(transactionRepository.save(transactionTransfer)).thenReturn(transactionTransfer);

        transactionService.transfer(new TransferRequest(bankAccount1.getIban(), bankAccount2.getIban(), 50.00F));

        assertEquals(bankAccount1.getBalance(), 50.00F);
        assertEquals(bankAccount2.getBalance(), 250.00F);
    }

    @Test
    void whenGetDepositTransactionBySender_thenReturnCorrectResult() {
        Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transactionDeposit));

        List<TransactionResponse> transactionResponses = transactionService.getDepositTransactionBySender(new TransactionRequest(user1.getUsername()));

        assertEquals(transactionResponses.size(), 1);
    }

    @Test
    void whenGetWithDrawTransactionBySender_thenReturnCorrectResult() {
        Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transactionWithDraw));

        List<TransactionResponse> transactionResponses = transactionService.getWithDrawTransactionBySender(new TransactionRequest(user1.getUsername()));

        assertEquals(transactionResponses.size(), 1);
    }

    @Test
    void whenGetTransferTransactionBySender_thenReturnCorrectResult() {
        Mockito.when(transactionRepository.findAll()).thenReturn(List.of(transactionTransfer));

        List<TransactionResponse> transactionResponses = transactionService.getTransferTransactionBySender(new TransactionRequest(user1.getUsername()));

        assertEquals(transactionResponses.size(), 1);
    }
}