package ar.com.smg.campania.application.exception; 


import ar.com.smg.campania.config.ErrorCode;
import ar.com.smg.campania.config.GenericException;

public class BusinessException extends GenericException {

    public BusinessException(ErrorCode errorCode){
        super(errorCode);
    }
}
