package com.EBank.EBankApplication.error;

public class InvalidUserCredentialsException extends Exception{
    public InvalidUserCredentialsException() {
        super();
    }

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public InvalidUserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserCredentialsException(Throwable cause) {
        super(cause);
    }

    protected InvalidUserCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
