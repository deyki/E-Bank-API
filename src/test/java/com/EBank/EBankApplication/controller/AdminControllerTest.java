package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.security.JWTFilter;
import com.EBank.EBankApplication.service.impl.AdminServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImpl adminService;

    @MockBean
    private JWTFilter jwtFilter;

    private BankAccountRequest bankAccountRequest;
    private ResponseMessage bankAccountResponse;

    @BeforeEach
    void setUp() {
        this.bankAccountRequest = new BankAccountRequest("122333");
        this.bankAccountResponse = new ResponseMessage(
                String.format("Bank account with username %s and iban %s banned!", "User", "122333")
        );
    }

    @Test
    void whenAdminPanel_thenReturnCorrectResult() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenBanBankAccountByIban_thenReturnCorrectResult() throws Exception {
        Mockito.when(adminService.banBankAccountByIban(bankAccountRequest)).thenReturn(bankAccountResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/bankAccount-ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountRequest)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(bankAccountResponse.message())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}