package ar.com.smg.campania.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import ar.com.smg.campania.domain.ResponseWrapper;

import java.util.Map;

@ControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    public GlobalResponseWrapper(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // Se aplica solo si el método devuelve un ResponseEntity o un objeto normal
        return !returnType.getContainingClass().getPackageName().startsWith("org.springframework");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  org.springframework.http.MediaType selectedContentType,
                                  Class selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {

        if (body instanceof ResponseWrapper) {
            return body; // Si ya está envuelto, no hacemos nada
        }

        final String resourcePath = ((ServletServerHttpRequest) request).getServletRequest().getRequestURI();
        final int status = response instanceof ResponseEntity ?
                ((ResponseEntity<?>) response).getStatusCode().value() :
                HttpStatus.OK.value();

        return new ResponseWrapper<>(
                resourcePath,
                status,
                body,
                Map.of("traceId", MDC.get("traceId"))
        );
    }
}
