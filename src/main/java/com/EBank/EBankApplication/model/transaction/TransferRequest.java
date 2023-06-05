package com.EBank.EBankApplication.model.transaction;

public record TransferRequest(String senderIban, String receiverIban, Float amount) {
}
