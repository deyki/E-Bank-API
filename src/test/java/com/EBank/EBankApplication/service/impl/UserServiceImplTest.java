package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.entity.UserProfileDetails;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.user.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.user.UserResponseModel;
import com.EBank.EBankApplication.repository.UserProfileDetailsRepository;
import com.EBank.EBankApplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserProfileDetailsRepository userProfileDetailsRepository;

    @Autowired
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        this.user = User
                .builder()
                .userID(1L)
                .username("User")
                .password("Password")
                .build();
    }

    @Test
    void whenCreateUserProfileDetails_thenReturnCorrectResult() {
        UserProfileDetailsRequest request = new UserProfileDetailsRequest(
                "Nikolay",
                "Nikolaev",
                "0899373703",
                "nikolay@gmail.com"
        );
        UserProfileDetails userProfileDetails = new UserProfileDetails();
        userProfileDetails.setFirstName(request.firstName());
        userProfileDetails.setLastName(request.lastName());
        userProfileDetails.setPhoneNumber(request.phoneNumber());
        userProfileDetails.setEmail(request.email());

        Mockito.when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userProfileDetailsRepository.save(userProfileDetails)).thenReturn(userProfileDetails);

        user.setUserProfileDetails(userProfileDetails);

        assertEquals(user.getUserProfileDetails().getFirstName(), "Nikolay");
    }

    @Test
    void whenGetUserById_thenReturnCorrectResult() throws UserNotFoundException {
        Mockito.when(userRepository.findById(user.getUserID())).thenReturn(Optional.of(user));

        UserProfileDetails userProfileDetails = new UserProfileDetails();
        userProfileDetails.setFirstName("Nikolay");
        userProfileDetails.setLastName("Nikolaev");
        userProfileDetails.setPhoneNumber("0899373703");
        userProfileDetails.setEmail("nikolay@gmail.com");

        user.setUserProfileDetails(userProfileDetails);

        UserResponseModel userResponseModel = userService.getUserByID(user.getUserID());

        assertEquals(userResponseModel.username(), user.getUsername());
    }
}