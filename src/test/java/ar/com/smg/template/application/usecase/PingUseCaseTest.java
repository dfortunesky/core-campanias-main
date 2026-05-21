package ar.com.smg.template.application.usecase;

import ar.com.smg.template.domain.template.PingResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PingUseCaseTest {

    @InjectMocks
    private PingUseCase pingUseCase;

    @Test
    void ping_ShouldReturnPongMessage() {
        final PingResponse response = pingUseCase.ping();

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("pong");
        assertThat(response.getTimestamp()).isNotNull();
    }
}
