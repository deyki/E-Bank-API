package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.model.bankAccount.BankAccountResponse;
import com.EBank.EBankApplication.security.JWTFilter;
import com.EBank.EBankApplication.service.impl.BankAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankAccountServiceImpl bankAccountService;

    @MockBean
    private JWTFilter jwtFilter;

    private User user;
    private ResponseMessage bankAccountCreated;

    private BankAccountRequest bankAccountRequest;
    private BankAccountResponse bankAccountResponse;

    @BeforeEach
    void setUp() {
        this.user = User
                .builder()
                .userID(1L)
                .username("User")
                .password("Password")
                .build();

        this.bankAccountCreated = new ResponseMessage("Bank account created!");

        this.bankAccountRequest = new BankAccountRequest("122333");

        this.bankAccountResponse = new BankAccountResponse(user.getUsername(), bankAccountRequest.iban(), 100.00F, false);
    }

    @Test
    void whenCreateBankAccount_thenReturnCorrectResult() throws Exception {
        Mockito.when(bankAccountService.createBankAccount(user.getUserID(), bankAccountRequest)).thenReturn(bankAccountCreated);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bankAccount/create/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(bankAccountCreated.message())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void whenGetBankAccountByUserId_thenReturnCorrectResult() throws Exception {
        Mockito.when(bankAccountService.getBankAccountByUserID(user.getUserID())).thenReturn(bankAccountResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bankAccount/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("User")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iban", Matchers.is("122333")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}