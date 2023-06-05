package com.EBank.EBankApplication.error;

public class BanException extends Exception{
    public BanException() {
        super();
    }

    public BanException(String message) {
        super(message);
    }

    public BanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BanException(Throwable cause) {
        super(cause);
    }

    protected BanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
