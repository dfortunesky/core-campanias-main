package ar.com.smg.campania.application.validator;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import ar.com.smg.campania.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CampaniaValidator {
    
    public void validateNotaBonificacion(Integer cuenta, String subcuenta, Integer subsiTipo) {
        if (cuenta == null || subcuenta == null || subcuenta.toLowerCase().equals("null") || subsiTipo == null) {
            throw new ValidationException("Cuenta, subcuenta y subsiTipo son requeridos");
        }
    }

    public void validateDescuentos(Integer zona, String condicion, Integer tipoCotizador, String plan, BigDecimal descuento) {
        if (zona == null || condicion == null ) {
            throw new ValidationException("Zona, condición son requeridos");
        }

        if (tipoCotizador == null) {
            throw new ValidationException("Tipo de cotizador es requerido");
        }

        if (plan != null && descuento == null) {
            throw new ValidationException("Si se proporciona un plan, se debe proporcionar un descuento");
        }

        if (plan == null && descuento != null) {
            throw new ValidationException("Si se proporciona un descuento, se debe proporcionar un plan");
        }

        if (descuento != null && (descuento.compareTo(BigDecimal.ZERO) <= 0 || descuento.compareTo(BigDecimal.valueOf(100)) > 0)) {
            throw new ValidationException("El descuento debe ser mayor a 0 y menor o igual a 100");
        }
    }
}
