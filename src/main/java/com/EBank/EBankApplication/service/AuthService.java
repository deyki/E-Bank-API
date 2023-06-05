package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.error.InvalidUserCredentialsException;
import com.EBank.EBankApplication.error.RoleNotFoundException;
import com.EBank.EBankApplication.model.AuthRequest;
import com.EBank.EBankApplication.model.ResponseMessage;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    ResponseMessage signUp(AuthRequest authRequest) throws InvalidUserCredentialsException, RoleNotFoundException;

    ResponseMessage signIn(AuthRequest authRequest) throws InvalidUserCredentialsException;

    boolean checkBlankCredentials(AuthRequest authRequest);
}
