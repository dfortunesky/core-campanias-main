package ar.com.smg.campania.application.port.out;

import java.math.BigDecimal;
import java.util.Date;

import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.domain.campania.NotaBonificacion;

public interface CampaniaRepositoryPortOut {
    NotaBonificacion getNotaBonificacion(Integer cuenta, String subcuenta, Integer subsiTipo);
    DescuentoZona getDescuentos(Integer zona, String condicion, Integer tipoCotizador, Date fechaCotizacion, String plan, BigDecimal descuento);
}