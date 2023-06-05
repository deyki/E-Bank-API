package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.error.UserNotFoundException;
import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.user.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.user.UserResponseModel;

public interface UserService {

    ResponseMessage createUserProfileDetails(Long userID, UserProfileDetailsRequest userProfileDetailsRequest) throws UserNotFoundException;

    UserResponseModel getUserByID(Long userID) throws UserNotFoundException;
}
