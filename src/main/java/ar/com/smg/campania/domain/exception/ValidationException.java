package ar.com.smg.campania.domain.exception;

import ar.com.smg.campania.config.GenericException;
import ar.com.smg.campania.config.ErrorCode;

public class ValidationException extends GenericException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}