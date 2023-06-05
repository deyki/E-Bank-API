package com.EBank.EBankApplication.error;

public class BankAccountBalanceException extends Exception{
    public BankAccountBalanceException() {
        super();
    }

    public BankAccountBalanceException(String message) {
        super(message);
    }

    public BankAccountBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankAccountBalanceException(Throwable cause) {
        super(cause);
    }

    protected BankAccountBalanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
