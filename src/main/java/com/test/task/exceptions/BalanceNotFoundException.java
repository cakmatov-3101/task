package com.test.task.exceptions;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(String message) {
        super(message);
    }

}
