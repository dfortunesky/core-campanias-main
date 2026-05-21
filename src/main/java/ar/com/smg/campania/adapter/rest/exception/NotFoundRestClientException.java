package ar.com.smg.campania.adapter.rest.exception;


import ar.com.smg.campania.config.ErrorCode;
import ar.com.smg.campania.config.GenericException;

public final class NotFoundRestClientException extends GenericException {

    public NotFoundRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
