package ar.com.smg.campania.application.usecase;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import ar.com.smg.campania.application.port.in.GetDescuentosPortIn;
import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.application.port.out.CampaniaRepositoryPortOut;
import ar.com.smg.campania.application.validator.CampaniaValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetDescuentosUseCase implements GetDescuentosPortIn{

    private final CampaniaRepositoryPortOut campaniaRepositoryPortOut;
    private final CampaniaValidator campaniaValidator;
    public GetDescuentosUseCase(CampaniaRepositoryPortOut campaniaRepositoryPortOut, CampaniaValidator campaniaValidator) {
        this.campaniaRepositoryPortOut = campaniaRepositoryPortOut;
        this.campaniaValidator = campaniaValidator;
    }

    @Override
    public DescuentoZona getDescuentos(Integer zona, String condicion, Integer tipoCotizador, String plan, BigDecimal descuento) {
        campaniaValidator.validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        final Date fechaCotizacion = new Date();
        log.info("Datos para obtener descuentos: zona={}, condicion={}, tipoCotizador={}, fechaCotizacion={}, plan={}, descuento={}",
         zona, condicion, tipoCotizador, fechaCotizacion, plan, descuento);
        final DescuentoZona descuentos = campaniaRepositoryPortOut.getDescuentos(zona, condicion, tipoCotizador, fechaCotizacion, plan, descuento);
        log.info("Descuentos obtenidos: {}", descuentos);
        return descuentos;
    }
}
