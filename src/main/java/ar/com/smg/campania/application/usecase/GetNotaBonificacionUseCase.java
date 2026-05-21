package ar.com.smg.campania.application.usecase;

import org.springframework.stereotype.Component;

import ar.com.smg.campania.application.port.in.GetNotaBonificacionPortIn;
import ar.com.smg.campania.application.validator.CampaniaValidator;
import ar.com.smg.campania.application.port.out.CampaniaRepositoryPortOut;
import ar.com.smg.campania.domain.campania.NotaBonificacion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetNotaBonificacionUseCase implements GetNotaBonificacionPortIn {

    private final CampaniaRepositoryPortOut campaniaRepositoryPortOut;
    private final CampaniaValidator campaniaValidator;
    public GetNotaBonificacionUseCase(final CampaniaRepositoryPortOut campaniaRepositoryPortOut, final CampaniaValidator campaniaValidator) {
        this.campaniaRepositoryPortOut = campaniaRepositoryPortOut;
        this.campaniaValidator = campaniaValidator;
    }

    @Override
    public NotaBonificacion getNotaBonificacion(Integer cuenta, String subcuenta, Integer subsiTipo) {
        campaniaValidator.validateNotaBonificacion(cuenta, subcuenta, subsiTipo);
        log.info("Datos para obtener nota de bonificación: cuenta={}, subcuenta={}, subsiTipo={}", cuenta, subcuenta, subsiTipo);
        final NotaBonificacion notaBonificacion = campaniaRepositoryPortOut.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        return notaBonificacion;
    }
}