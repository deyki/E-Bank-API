package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.enums.TransactionType;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.transaction.*;
import com.EBank.EBankApplication.security.JWTFilter;
import com.EBank.EBankApplication.service.impl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTFilter jwtFilter;

    @MockBean
    private TransactionServiceImpl transactionService;

    private User user;
    private User user2;

    private DepositRequest depositRequest;
    private ResponseMessage depositResponse;

    private WithDrawRequest withDrawRequest;
    private ResponseMessage withDrawResponse;

    private TransferRequest transferRequest;
    private ResponseMessage transferResponse;

    private TransactionRequest transactionRequest;
    private List<TransactionResponse> transactionDepositResponse;
    private List<TransactionResponse> transactionWithDrawResponse;
    private List<TransactionResponse> transactionTransferResponse;

    @BeforeEach
    void setUp() {
        this.user = User.builder().userID(1L).username("User").password("Password").build();
        this.user2 = User.builder().userID(2L).username("User2").password("Password2").build();

        this.depositRequest = new DepositRequest("122333", 20.00F);
        this.depositResponse = new ResponseMessage("Deposit was made successfully!");

        this.withDrawRequest = new WithDrawRequest("122333", 20.00F);
        this.withDrawResponse = new ResponseMessage("Withdraw was made successfully!");

        this.transferRequest = new TransferRequest("122333", "333221", 20.00F);
        this.transferResponse = new ResponseMessage("Transfer was made successfully!");

        this.transactionRequest = new TransactionRequest(user.getUsername());

        this.transactionDepositResponse = new ArrayList<>(List.of(
                new TransactionResponse(TransactionType.DEPOSIT, user.getUsername(), user.getUsername(), 20.00F, new Date())));

        this.transactionWithDrawResponse = new ArrayList<>(List.of(
                new TransactionResponse(TransactionType.WITHDRAW, user.getUsername(), user.getUsername(), 20.00F, new Date())));

        this.transactionTransferResponse = new ArrayList<>(List.of(
                new TransactionResponse(TransactionType.TRANSFER, user.getUsername(), user2.getUsername(), 20.00F, new Date())));
    }

    @Test
    void whenDeposit_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.deposit(depositRequest)).thenReturn(depositResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(depositResponse.message())));
    }

    @Test
    void whenWithDraw_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.withDraw(withDrawRequest)).thenReturn(withDrawResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction/withDraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withDrawRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(withDrawResponse.message())));
    }

    @Test
    void whenTransfer_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.transfer(transferRequest)).thenReturn(transferResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(transferResponse.message())));
    }

    @Test
    void whenGetAllDeposits_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.getDepositTransactionBySender(transactionRequest)).thenReturn(transactionDepositResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/allDeposits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sender", Matchers.is("User")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType", Matchers.is("DEPOSIT")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenGetAllWithDraws_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.getWithDrawTransactionBySender(transactionRequest)).thenReturn(transactionWithDrawResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/allWithDraws")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sender", Matchers.is("User")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType", Matchers.is("WITHDRAW")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenGetAllTransfers_thenReturnCorrectResult() throws Exception {
        Mockito.when(transactionService.getTransferTransactionBySender(transactionRequest)).thenReturn(transactionTransferResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/allTransfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sender", Matchers.is("User")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].receiver", Matchers.is("User2")))
                .andExpect(MockMvcResultMatchers.jsonPath("[0].transactionType", Matchers.is("TRANSFER")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}