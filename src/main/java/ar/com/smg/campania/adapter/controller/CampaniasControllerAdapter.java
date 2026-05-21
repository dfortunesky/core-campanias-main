package ar.com.smg.campania.adapter.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.smg.campania.application.port.in.GetDescuentosPortIn;
import ar.com.smg.campania.application.port.in.GetNotaBonificacionPortIn;
import ar.com.smg.campania.domain.campania.NotaBonificacion;
import ar.com.smg.campania.domain.campania.DescuentoZona;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/campanias")
@RequiredArgsConstructor
@Slf4j
public class CampaniasControllerAdapter {
    
    private final GetNotaBonificacionPortIn getNotaBonificacionPortIn;
    private final GetDescuentosPortIn getDescuentosPortIn;
    @GetMapping("/{cuenta}/{subcuenta}/{subsiTipo}/nota-bonificacion")
    @Operation(summary = "Obtiene la nota de bonificación para una cuenta, subcuenta y tipo de subsidio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nota de bonificación obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "404", description = "Nota de bonificación no encontrada")
    })
    public ResponseEntity<NotaBonificacion> getNotaBonificacion(@PathVariable Integer cuenta, @PathVariable String subcuenta, @PathVariable Integer subsiTipo) {
        final NotaBonificacion notaBonificacion = getNotaBonificacionPortIn.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        return ResponseEntity.ok(notaBonificacion);
    }

    @GetMapping("/{zona}/{condicion}/{tipoCotizador}/descuentos")
    @Operation(summary = "Obtiene los descuentos para una zona y condición de afiliado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descuentos obtenidos correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "404", description = "Descuentos no encontrados")
    })
    public ResponseEntity<DescuentoZona> getDescuentos(@PathVariable Integer zona, @PathVariable String condicion,
     @PathVariable Integer tipoCotizador, @RequestParam(required = false) String plan, @RequestParam(required = false) BigDecimal descuento) {
        final DescuentoZona descuentos = getDescuentosPortIn.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        return ResponseEntity.ok(descuentos);
    }
}
