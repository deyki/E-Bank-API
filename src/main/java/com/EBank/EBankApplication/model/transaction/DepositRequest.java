package com.EBank.EBankApplication.model.transaction;

public record DepositRequest(String iban, Float amount) {
}
