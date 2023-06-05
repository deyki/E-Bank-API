package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.BankAccount;
import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.AlreadyExistException;
import com.EBank.EBankApplication.error.BankAccountNotFoundException;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.bankAccount.BankAccountRequest;
import com.EBank.EBankApplication.model.bankAccount.BankAccountResponse;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.repository.BankAccountRepository;
import com.EBank.EBankApplication.repository.UserRepository;
import com.EBank.EBankApplication.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseMessage createBankAccount(Long userID, BankAccountRequest bankAccountRequest) throws UserNotFoundException {
        User user = userRepository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        bankAccountRepository
                .findByUsername(user.getUsername())
                .ifPresent(bankAccount -> {
                    try {
                        throw new AlreadyExistException(String.format("User %s already has bank account!", user.getUsername()));
                    } catch (AlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });

        bankAccountRepository
                .findByIban(bankAccountRequest.iban())
                .ifPresent(bankAccount -> {
                    try {
                        throw new AlreadyExistException(String.format("Bank account with iban -> %s already exist!", bankAccountRequest.iban()));
                    } catch (AlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });

        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setIban(bankAccountRequest.iban());
        bankAccount.setBalance(0.0f);
        bankAccount.setBanned(false);
        bankAccountRepository.save(bankAccount);

        user.setBankAccount(bankAccount);
        userRepository.save(user);

        return new ResponseMessage("Bank account created!");
    }

    @Override
    public BankAccountResponse getBankAccountByUserID(Long userID) throws UserNotFoundException, BankAccountNotFoundException {
        User user = userRepository
                .findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        BankAccount bankAccount = bankAccountRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("No bank account associated with user -> %s", user.getUsername())));

        return new BankAccountResponse(
                user.getUsername(),
                bankAccount.getIban(),
                bankAccount.getBalance(),
                bankAccount.getBanned()
        );
    }
}