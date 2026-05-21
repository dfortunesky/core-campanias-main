package ar.com.smg.template.domain.template;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PingResponse {
    private String message;
    private LocalDateTime timestamp;
}
