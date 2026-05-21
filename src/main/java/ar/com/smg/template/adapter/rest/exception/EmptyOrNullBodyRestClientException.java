package ar.com.smg.template.adapter.rest.exception;

import ar.com.smg.template.config.ErrorCode;
import ar.com.smg.template.config.GenericException;

public final class EmptyOrNullBodyRestClientException extends GenericException {

    public EmptyOrNullBodyRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
