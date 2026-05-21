package ar.com.smg.campania.adapter.rest.exception;


import ar.com.smg.campania.config.ErrorCode;
import ar.com.smg.campania.config.GenericException;

public final class NonTargetRestClientException extends GenericException {

    public NonTargetRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
