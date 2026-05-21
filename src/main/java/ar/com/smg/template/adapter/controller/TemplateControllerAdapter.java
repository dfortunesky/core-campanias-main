package ar.com.smg.template.adapter.controller;

import ar.com.smg.template.application.port.in.PingPortIn;
import ar.com.smg.template.domain.template.PingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TemplateControllerAdapter {

    private final PingPortIn pingPortIn;

    @GetMapping("/ping")
    @Operation(summary = "Verifica que el servicio está activo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio activo")
    })
    public ResponseEntity<PingResponse> ping() {
        return ResponseEntity.ok(pingPortIn.ping());
    }
}
