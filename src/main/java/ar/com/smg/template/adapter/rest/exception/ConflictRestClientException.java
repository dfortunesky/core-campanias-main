package ar.com.smg.template.adapter.rest.exception;

import ar.com.smg.template.config.ErrorCode;
import ar.com.smg.template.config.GenericException;

public class ConflictRestClientException extends GenericException {

    public ConflictRestClientException(ErrorCode ec) {
        super(ec);
    }
}
