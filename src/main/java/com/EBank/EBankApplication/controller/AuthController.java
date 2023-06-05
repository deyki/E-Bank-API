package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.InvalidUserCredentialsException;
import com.EBank.EBankApplication.error.RoleNotFoundException;
import com.EBank.EBankApplication.model.AuthRequest;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseMessage> signUp(@RequestBody AuthRequest authRequest) throws RoleNotFoundException, InvalidUserCredentialsException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signUp(authRequest));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseMessage> signIn(@RequestBody AuthRequest authRequest) throws InvalidUserCredentialsException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.signIn(authRequest));
    }
}
