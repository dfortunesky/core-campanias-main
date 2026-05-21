package ar.com.smg.template.application.usecase;

import ar.com.smg.template.application.port.in.PingPortIn;
import ar.com.smg.template.domain.template.PingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class PingUseCase implements PingPortIn {

    @Override
    public PingResponse ping() {
        log.info("Ping request received");
        return PingResponse.builder()
                .message("pong")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
