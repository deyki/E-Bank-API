package com.EBank.EBankApplication.error;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException() {
        super();
    }

    public BankAccountNotFoundException(String message) {
        super(message);
    }

    public BankAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankAccountNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BankAccountNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
