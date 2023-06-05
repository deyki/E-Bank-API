package com.EBank.EBankApplication.model.bankAccount;

public record BankAccountResponse(String username, String iban, Float balance, Boolean banned) {
}
