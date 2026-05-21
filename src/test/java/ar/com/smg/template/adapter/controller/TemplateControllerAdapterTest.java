package ar.com.smg.template.adapter.controller;

import ar.com.smg.template.application.port.in.PingPortIn;
import ar.com.smg.template.domain.template.PingResponse;
// import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateControllerAdapter.class)
class TemplateControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PingPortIn pingPortIn;

    @Test
    void ping_ShouldReturnOkWithPongMessage() throws Exception {
        final PingResponse mockResponse = PingResponse.builder()
                .message("pong")
                .timestamp(LocalDateTime.now())
                .build();

        when(pingPortIn.ping()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/ping")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("pong"));
    }
}
