package com.EBank.EBankApplication.model.transaction;

import com.EBank.EBankApplication.enums.TransactionType;

import java.util.Date;

public record TransactionResponse(TransactionType transactionType, String sender, String receiver, Float amount, Date date) {
}
