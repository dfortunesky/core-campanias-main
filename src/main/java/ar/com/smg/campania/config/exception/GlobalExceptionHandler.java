package ar.com.smg.campania.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.com.smg.campania.domain.ResponseWrapper;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<String>> handleEmployeeNotFound(ResourceNotFoundException ex,
                                                                          HttpServletRequest request) {
        return buildErrorResponse(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<String>> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(request, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseWrapper<String>> buildErrorResponse(HttpServletRequest request, String message,
                                                                       HttpStatus status) {
        final ResponseWrapper<String> errorResponse = new ResponseWrapper<>(
                request.getRequestURI(),
                status.value(),
                message,
                Map.of("traceId", MDC.get("traceId"))
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
