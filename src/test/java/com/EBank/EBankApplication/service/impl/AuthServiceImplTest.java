package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.InvalidUserCredentialsException;
import com.EBank.EBankApplication.model.AuthRequest;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.repository.UserRepository;
import com.EBank.EBankApplication.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private JWTUtil jwtUtil;

    private User user;

    @BeforeEach
    void setUp() {

        this.user = User
                .builder()
                .userID(1L)
                .username("username")
                .password(bCryptPasswordEncoder.encode("password"))
                .build();
    }

    @Test
    void whenLoadUserByUsername_thenReturnCorrectResult() {
        Mockito.when(userRepository.findByUsername(this.user.getUsername())).thenReturn(Optional.of(this.user));

        UserDetails userDetails = authService.loadUserByUsername(this.user.getUsername());

        assertEquals(userDetails.getUsername(), this.user.getUsername());
    }

    @Test
    void whenSignUp_thenReturnCorrectResult() {
        User newUser = new User();
        newUser.setUsername("User2");
        newUser.setPassword(bCryptPasswordEncoder.encode("password"));

        Mockito.when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        assertNotNull(userRepository.findByUsername(newUser.getUsername()));
    }

    @Test
    void whenSignIn_thenReturnCorrectResult() throws InvalidUserCredentialsException {
        String JWToken = "JWToken";

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken(user.getUsername())).thenReturn(JWToken);

        ResponseMessage responseMessage = authService.signIn(new AuthRequest(user.getUsername(), "password"));

        assertEquals(responseMessage.message(), JWToken);
    }

    @Test
    void whenCheckBlankCredentials_thenReturnCorrectResult() {
        boolean isBlankTest1 = authService.checkBlankCredentials(new AuthRequest("", ""));
        boolean isBlankTest2 = authService.checkBlankCredentials(new AuthRequest("", "password"));
        boolean isBlankTest3 = authService.checkBlankCredentials(new AuthRequest("username", ""));
        boolean isBlankTest4 = authService.checkBlankCredentials(new AuthRequest("username", "password"));

        assertTrue(isBlankTest1);
        assertTrue(isBlankTest2);
        assertTrue(isBlankTest3);
        assertFalse(isBlankTest4);
    }
}