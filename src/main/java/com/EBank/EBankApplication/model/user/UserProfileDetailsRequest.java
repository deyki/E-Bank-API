package com.EBank.EBankApplication.model.user;

public record UserProfileDetailsRequest(String firstName, String lastName, String phoneNumber, String email) {
}
