package com.EBank.EBankApplication.controller;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.entity.UserProfileDetails;
import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.user.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.user.UserResponseModel;
import com.EBank.EBankApplication.security.JWTFilter;
import com.EBank.EBankApplication.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JWTFilter jwtFilter;

    private User user;
    private UserProfileDetails userProfileDetails;
    private UserResponseModel userResponseModel;
    private UserProfileDetailsRequest request;
    private ResponseMessage userProfileDetailsCreated;

    @BeforeEach
    void setUp() {
        this.user = User
                .builder()
                .userID(1L)
                .username("User")
                .password("Password")
                .userProfileDetails(userProfileDetails)
                .build();

        this.request = new UserProfileDetailsRequest("Nikolay", "Nikolaev", "0899373703", "nikolay@gmail.com");

        this.userProfileDetails = UserProfileDetails
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .user(user)
                .build();

        this.userProfileDetailsCreated = new ResponseMessage("Successfully created profile details!");

        this.userResponseModel = new UserResponseModel(
                user.getUsername(),
                userProfileDetails.getFirstName(),
                userProfileDetails.getLastName(),
                userProfileDetails.getPhoneNumber(),
                userProfileDetails.getEmail()
        );
    }

    @Test
    void whenCreateUserProfileDetails_thenReturnCorrectResult() throws Exception {
        Mockito.when(userService.createUserProfileDetails(user.getUserID(), request)).thenReturn(userProfileDetailsCreated);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create-profile-details/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void whenGetProfileDetails_thenReturnCorrectResult() throws Exception {
        Mockito.when(userService.getUserByID(user.getUserID())).thenReturn(userResponseModel);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get-profile-details/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("User")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}