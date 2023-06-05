package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.model.AuthRequest;

public interface AuthManagerService {

    void authenticate(AuthRequest authRequest);
}
