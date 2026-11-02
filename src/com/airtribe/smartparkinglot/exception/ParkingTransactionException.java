package com.airtribe.smartparkinglot.exception;

public class ParkingTransactionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ParkingTransactionException(String message) {
        super(message);
    }
}
