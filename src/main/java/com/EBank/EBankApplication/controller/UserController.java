package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.UserResponseModel;
import com.EBank.EBankApplication.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/create-profile-details/{userID}")
    public ResponseEntity<ResponseMessage> createProfileDetails(@PathVariable Long userID, @RequestBody UserProfileDetailsRequest userProfileDetailsRequest) throws UserNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUserProfileDetails(userID, userProfileDetailsRequest));
    }

    @GetMapping("/get-profile-details/{userID}")
    public ResponseEntity<UserResponseModel> getProfileDetails(@PathVariable Long userID) throws UserNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByID(userID));
    }
}