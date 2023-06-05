package com.EBank.EBankApplication.model.transaction;

public record WithDrawRequest(String iban, Float amount) {
}
