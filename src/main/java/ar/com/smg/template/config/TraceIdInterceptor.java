package ar.com.smg.template.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdInterceptor implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String traceId = httpServletRequest.getHeader("traceparent");
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceparent();
        }

        MDC.put("traceId", traceId);

        if (httpServletResponse.getHeader("traceparent") == null) {
            httpServletResponse.addHeader("traceparent", traceId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String generateTraceparent() {
        final String version = "00";
        final String traceId = generateTraceId();
        final String parentId = "0000000000000000";
        final String flags = "01";
        return version + "-" + traceId + "-" + parentId + "-" + flags;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
