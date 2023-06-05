package com.EBank.EBankApplication.model;

public record UserProfileDetailsRequest(String firstName, String lastName, String phoneNumber, String email) {
}
