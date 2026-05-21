package ar.com.smg.template.adapter.rest.exception;

import ar.com.smg.template.config.ErrorCode;
import ar.com.smg.template.config.GenericException;

public final class TimeoutRestClientException extends GenericException {

    public TimeoutRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
