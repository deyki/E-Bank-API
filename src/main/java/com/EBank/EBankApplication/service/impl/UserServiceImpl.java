package com.EBank.EBankApplication.service.impl;

import com.EBank.EBankApplication.entity.User;
import com.EBank.EBankApplication.entity.UserProfileDetails;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.UserResponseModel;
import com.EBank.EBankApplication.repository.UserProfileDetailsRepository;
import com.EBank.EBankApplication.repository.UserRepository;
import com.EBank.EBankApplication.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileDetailsRepository userProfileDetailsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserProfileDetailsRepository userProfileDetailsRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userProfileDetailsRepository = userProfileDetailsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseMessage createUserProfileDetails(Long userID, UserProfileDetailsRequest userProfileDetailsRequest) {
        User user = userRepository
                .findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        UserProfileDetails userProfileDetails = new UserProfileDetails();
        userProfileDetails.setUser(user);
        userProfileDetails.setFirstName(userProfileDetailsRequest.firstName());
        userProfileDetails.setLastName(userProfileDetailsRequest.lastName());
        userProfileDetails.setPhoneNumber(userProfileDetailsRequest.phoneNumber());
        userProfileDetails.setEmail(userProfileDetailsRequest.email());
        userProfileDetailsRepository.save(userProfileDetails);

        user.setUserProfileDetails(userProfileDetails);
        userRepository.save(user);

        return new ResponseMessage("Successfully created profile details!");
    }

    @Override
    public UserResponseModel getUserByID(Long userID) {
        User user = userRepository
                .findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        UserProfileDetails userProfileDetails = user.getUserProfileDetails();

        return new UserResponseModel(
                user.getUsername(),
                userProfileDetails.getFirstName(),
                userProfileDetails.getLastName(),
                userProfileDetails.getPhoneNumber(),
                userProfileDetails.getEmail()
        );
    }
}