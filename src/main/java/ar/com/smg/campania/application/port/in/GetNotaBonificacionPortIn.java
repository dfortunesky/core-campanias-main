package ar.com.smg.campania.application.port.in;

import ar.com.smg.campania.domain.campania.NotaBonificacion;

public interface GetNotaBonificacionPortIn {
    NotaBonificacion getNotaBonificacion(Integer cuenta, String subcuenta, Integer subsiTipo);
}
