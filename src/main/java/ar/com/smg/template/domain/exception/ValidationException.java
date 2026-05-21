package ar.com.smg.template.domain.exception;

import ar.com.smg.template.config.ErrorCode;
import ar.com.smg.template.config.GenericException;

public class ValidationException extends GenericException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}
