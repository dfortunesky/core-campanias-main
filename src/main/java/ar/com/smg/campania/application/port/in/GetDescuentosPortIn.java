package ar.com.smg.campania.application.port.in;

import java.math.BigDecimal;

import ar.com.smg.campania.domain.campania.DescuentoZona;

public interface GetDescuentosPortIn {
    DescuentoZona getDescuentos(Integer zona, String condicion, Integer tipoCotizador, String plan, BigDecimal descuento);   
}
