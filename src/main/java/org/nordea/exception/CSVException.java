package org.nordea.exception;

public class CSVException extends RuntimeException {

    public CSVException(String message, Throwable cause) {
        super(message, cause);
    }
}