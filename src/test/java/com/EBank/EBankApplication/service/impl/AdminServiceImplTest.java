package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceImplTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AdminServiceImpl adminService;

    private BankAccount bankAccount;
    private User user;

    @BeforeEach
    void setUp() {
        this.user = User
                .builder()
                .userID(1L)
                .username("User")
                .password("Password")
                .bankAccount(bankAccount)
                .build();

        this.bankAccount = BankAccount
                .builder()
                .bankAccountID(100L)
                .iban("122333")
                .balance(100.00F)
                .user(user)
                .build();
    }

    @Test
    void whenBanBankAccountByIban_thenReturnCorrectResult() throws BankAccountNotFoundException {
        Mockito.when(bankAccountRepository.findByIban(bankAccount.getIban())).thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        ResponseMessage responseMessage = adminService.banBankAccountByIban(new BankAccountRequest(bankAccount.getIban()));
        ResponseMessage expectedResponseMessage = new ResponseMessage(String.format(
                "Bank account with username %s and iban %s banned!", bankAccount.getUser().getUsername(), bankAccount.getIban()
        ));

        assertEquals(responseMessage.message(), expectedResponseMessage.message());
    }
}