package ar.com.smg.campania.domain.campania;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Descuento {
    private int cuenta;
    private String subCuenta;
    private String denominacionSubcuenta;
    private String plan;
    private String tarifa;
    private BigDecimal porcentajeDescuento;
    private String subsi_tipo;
    private String denominacionSubsidio;
    private String vigenciaDesde;
    private String vigenciaHasta;
    private int cantidadMeses;
}