package com.EBank.EBankApplication.service;

import com.EBank.EBankApplication.model.ResponseMessage;
import com.EBank.EBankApplication.model.UserProfileDetailsRequest;
import com.EBank.EBankApplication.model.UserResponseModel;

public interface UserService {

    ResponseMessage createUserProfileDetails(Long userID, UserProfileDetailsRequest userProfileDetailsRequest);

    UserResponseModel getUserByID(Long userID);
}
