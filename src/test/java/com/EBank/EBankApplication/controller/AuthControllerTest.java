package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.InvalidUserCredentialsException;
import com.EBank.EBankApplication.error.RoleNotFoundException;
import com.EBank.EBankApplication.model.AuthRequest;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.security.JWTFilter;
import com.EBank.EBankApplication.security.JWTUtil;
import com.EBank.EBankApplication.service.impl.AuthServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private JWTUtil jwtUtil;

    private ResponseMessage accountCreated;
    private ResponseMessage JWToken;
    private AuthRequest authRequest;
    @BeforeEach
    void setUp() {
        this.authRequest = new AuthRequest("User", "Password");
        this.accountCreated = new ResponseMessage("Account created!");
        this.JWToken = new ResponseMessage("JWToken");
    }

    @Test
    void whenSignUp_thenReturnCorrectResult() throws Exception {
        Mockito.when(authService.signUp(authRequest)).thenReturn(accountCreated);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void whenSignIn_thenReturnCorrectResult() throws Exception {
        Mockito.when(authService.signIn(authRequest)).thenReturn(JWToken);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}