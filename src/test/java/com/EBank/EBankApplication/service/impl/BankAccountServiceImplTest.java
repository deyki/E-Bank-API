package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.bankAccount.BankAccountResponse;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import com.EBank.EBankApplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankAccountServiceImplTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private BankAccountServiceImpl bankAccountService;

    private BankAccount bankAccount;
    private User user;

    @BeforeEach
    void setUp() {
        this.bankAccount = BankAccount
                .builder()
                .bankAccountID(1L)
                .iban("122333")
                .balance(100.00F)
                .banned(false)
                .build();

        this.user = User
                .builder()
                .userID(2L)
                .username("User")
                .password("Password")
                .build();
    }

    @Test
    void whenCreateBankAccount_thenReturnCorrectResult() {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setBankAccountID(3L);
        newBankAccount.setIban("3332211");
        newBankAccount.setBalance(100.00F);
        newBankAccount.setBanned(false);

        Mockito.when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        Mockito.when(bankAccountRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountRepository.findByIban(newBankAccount.getIban())).thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountRepository.save(newBankAccount)).thenReturn(newBankAccount);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        user.setBankAccount(newBankAccount);

        assertEquals(user.getBankAccount().getIban(), newBankAccount.getIban());
    }

    @Test
    void whenGetBankAccountByUserId_thenReturnCorrectResult() throws UserNotFoundException, BankAccountNotFoundException {
        Mockito.when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        Mockito.when(bankAccountRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(bankAccount));

        user.setBankAccount(bankAccount);

        BankAccountResponse bankAccountResponse = bankAccountService.getBankAccountByUserID(user.getUserID());

        assertEquals(bankAccountResponse.username(), user.getUsername());
    }
}