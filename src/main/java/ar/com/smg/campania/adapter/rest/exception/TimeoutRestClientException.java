package ar.com.smg.campania.adapter.rest.exception;


import ar.com.smg.campania.config.ErrorCode;
import ar.com.smg.campania.config.GenericException;

public final class TimeoutRestClientException extends GenericException {

    public TimeoutRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
