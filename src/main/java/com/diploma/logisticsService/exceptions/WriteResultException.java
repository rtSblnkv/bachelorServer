package com.diploma.logisticsService.exceptions;

/**
 * Exception handler
 * Thrown when writing the result in file failed
 */
public class WriteResultException extends RuntimeException {
    public WriteResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
