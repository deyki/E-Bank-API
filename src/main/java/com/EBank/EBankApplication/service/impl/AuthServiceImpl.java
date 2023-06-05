package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.error.InvalidUserCredentialsException;
import com.EBank.EBankApplication.error.RoleNotFoundException;
import com.EBank.EBankApplication.model.AuthRequest;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.repository.UserRepository;
import com.EBank.EBankApplication.security.JWTUtil;
import com.EBank.EBankApplication.service.AuthService;
import com.EBank.EBankApplication.service.RoleService;
import com.google.common.collect.Sets;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RoleService roleService;
    private final AuthManagerServiceImpl authManagerService;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtil jwtUtil, RoleService roleService, AuthManagerServiceImpl authManagerService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
        this.authManagerService = authManagerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));
    }

    @Override
    public ResponseMessage signUp(AuthRequest authRequest) throws InvalidUserCredentialsException, RoleNotFoundException {
        boolean blankCredentials = checkBlankCredentials(authRequest);
        if (blankCredentials) {
            throw new InvalidUserCredentialsException("Blank credentials not allowed!");
        }

        userRepository.findByUsername(authRequest.username()).ifPresent(user -> {
                    try {
                        throw new InvalidUserCredentialsException(String.format("Username %s already exist!", authRequest.username()));
                    } catch (InvalidUserCredentialsException exception) {
                        throw new RuntimeException(exception);
                    }
                });

        User user = new User();
        if (userRepository.count() == 0) {
            roleService.seedRolesInDB();
            user.setAuthorities(Sets.newHashSet(roleService.findByAuthority("ADMIN")));
        } else {
            user.setAuthorities(Sets.newHashSet(roleService.findByAuthority("USER")));
        }
        user.setUsername(authRequest.username());
        user.setPassword(bCryptPasswordEncoder.encode(authRequest.password()));
        userRepository.save(user);

        return new ResponseMessage("Account created!");
    }

    @Override
    public ResponseMessage signIn(AuthRequest authRequest) throws InvalidUserCredentialsException {
        boolean blankCredentials = checkBlankCredentials(authRequest);
        if (blankCredentials) {
            throw new InvalidUserCredentialsException("Blank credentials not allowed!");
        }

        User user = userRepository.findByUsername(authRequest.username())
                .orElseThrow(() -> new InvalidUserCredentialsException("Invalid username or password!"));

        boolean validPassword = bCryptPasswordEncoder.matches(authRequest.password(), user.getPassword());
        if (!validPassword) {
            throw new InvalidUserCredentialsException("Invalid username or password!");
        }

        final String JWToken = jwtUtil.generateToken(user.getUsername());

        authManagerService.authenticate(authRequest);

        return new ResponseMessage(JWToken);
    }

    @Override
    public boolean checkBlankCredentials(AuthRequest authRequest) {
        if (authRequest.username().isBlank()) {
            return true;
        } else if (authRequest.password().isBlank()) {
            return true;
        } else if (authRequest.username().isBlank() && authRequest.password().isBlank()) {
            return true;
        }
        return false;
    }
}
