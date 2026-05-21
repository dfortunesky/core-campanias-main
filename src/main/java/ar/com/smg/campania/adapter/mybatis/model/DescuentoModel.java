package ar.com.smg.campania.adapter.mybatis.model;

import java.util.Date;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoModel {
    private Integer zona;
    private String condicion;
    private Integer tipoCotizador;
    private Date fechaCotizacion;
    private String plan;
    private BigDecimal descuento;
}
