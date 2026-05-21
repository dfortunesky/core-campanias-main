package ar.com.smg.campania.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sybase.jdbc4.jdbc.SybSQLException;

import ar.com.smg.campania.application.exception.NotFoundException;
import ar.com.smg.campania.domain.exception.ValidationException;
import ar.com.smg.campania.domain.helper.AflimedHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    private final HttpServletRequest httpServletRequest;

    public ErrorHandler(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handle(final HttpMessageNotReadableException ex) {
        String mensaje = ex.getMessage();
        if (mensaje != null && mensaje.contains("LocalDateTime")) {
            mensaje = "El formato de fecha debe ser yyyy-MM-dd'T'HH:mm:ss (por ejemplo: 2025-04-01T00:00:00)";
        }
        log.error(ErrorCode.VALIDATION_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, new ValidationException(mensaje), ErrorCode.VALIDATION_ERROR, false);
    }

    @ExceptionHandler(UncategorizedSQLException.class)
    public ResponseEntity<ErrorResponse> handle(final SybSQLException ex) {
        if (AflimedHelper.isRaiseError(ex)) {
            log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
            return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.VALIDATION_ERROR, false);
        } else {
            log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
            return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR, true);
        }
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handle(final Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR, true);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handle(final MissingServletRequestParameterException ex) {
        log.error(ErrorCode.INVALID_PARAMETERS_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.INVALID_PARAMETERS_ERROR, false);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handle(final MethodArgumentTypeMismatchException ex) {
        final String mensaje = String.format("El parámetro '%s' debe ser de tipo %s. Valor recibido: '%s'", 
            ex.getName(), 
            ex.getRequiredType().getSimpleName(), 
            ex.getValue());
        log.error(ErrorCode.INVALID_PARAMETERS_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, new ValidationException(mensaje), ErrorCode.INVALID_PARAMETERS_ERROR, false);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(final NotFoundException ex) {
        log.error(ErrorCode.NOT_FOUND_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ErrorCode.NOT_FOUND_ERROR, false);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handle(final ValidationException ex) {
        log.error(ErrorCode.VALIDATION_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.VALIDATION_ERROR, false);
    }

    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorResponse {

        private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']";

        @JsonProperty
        private String name;
        @JsonProperty
        private Integer status;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private int errorInternalCode;
        @JsonProperty
        private String errorDescription;
        @JsonProperty
        private String errorCode;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
        private LocalDateTime timestamp;
        @JsonProperty
        private String resource;
        @JsonProperty
        private String detail;
        @JsonProperty
        private Map<String, String> metadata;
    }

    private ResponseEntity<ErrorResponse> buildResponseError(
            final HttpStatus httpStatus,
            final Throwable ex,
            final ErrorCode errorCode,
            final Boolean hideException) {
        final String queryString = Optional.ofNullable(httpServletRequest.getQueryString())
                .orElse("");

        final Map<String, String> metaData = new HashMap<>();
        metaData.put("query_string", queryString);

        log.error(String.format("Error en core-padrones | %s: %s", ex.getClass().getCanonicalName(), ex.getMessage()));

        final ErrorResponse apiErrorResponse;

        if (hideException) {
            apiErrorResponse = ErrorResponse
                    .builder()
                    .timestamp(LocalDateTime.now())
                    .name(httpStatus.getReasonPhrase())
                    .detail("Error interno")
                    .status(httpStatus.value())
                    .code(errorCode.value())
                    .errorInternalCode(errorCode.value())
                    .resource(httpServletRequest.getRequestURI())
                    .metadata(metaData)
                    .errorInternalCode(errorCode.value())
                    .errorDescription(errorCode.getDetail())
                    .errorCode(errorCode.getCode())
                    .build();
        } else {
            apiErrorResponse = ErrorResponse
                    .builder()
                    .timestamp(LocalDateTime.now())
                    .name(httpStatus.getReasonPhrase())
                    .detail(String.format("%s", ex.getMessage()))
                    .status(httpStatus.value())
                    .code(errorCode.value())
                    .errorInternalCode(errorCode.value())
                    .resource(httpServletRequest.getRequestURI())
                    .metadata(metaData)
                    .errorInternalCode(errorCode.value())
                    .errorDescription(errorCode.getDetail())
                    .errorCode(errorCode.getCode())
                    .build();
        }

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }
}

