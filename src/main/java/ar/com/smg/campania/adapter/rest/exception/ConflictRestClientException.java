package ar.com.smg.campania.adapter.rest.exception;


import ar.com.smg.campania.config.ErrorCode;
import ar.com.smg.campania.config.GenericException;

public class ConflictRestClientException extends GenericException {
    public ConflictRestClientException(ErrorCode ec) {
        super(ec);
    }
}
