package ar.com.smg.campania.adapter.mybatis;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import ar.com.smg.campania.application.port.out.CampaniaRepositoryPortOut;
import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.domain.campania.NotaBonificacion;
import ar.com.smg.campania.adapter.mybatis.mappers.CampaniaMapper;
import ar.com.smg.campania.adapter.mybatis.model.DescuentoModel;
import ar.com.smg.campania.adapter.mybatis.model.NotaBonificacionModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CampaniaMybatisAdapter implements CampaniaRepositoryPortOut {
    
    private final CampaniaMapper campaniaMapper;

    public CampaniaMybatisAdapter(final CampaniaMapper campaniaMapper) {
        this.campaniaMapper = campaniaMapper;
    }

    @Override
    public DescuentoZona getDescuentos(Integer zona, String condicion, Integer tipoCotizador, Date fechaCotizacion, String plan, BigDecimal descuento) {
        final DescuentoModel descuentoModel = DescuentoModel.builder()
            .zona(zona)
            .condicion(condicion)
            .tipoCotizador(tipoCotizador)
            .fechaCotizacion(fechaCotizacion)
            .plan(plan)
            .descuento(descuento)
            .build();   
        return campaniaMapper.getDescuentos(descuentoModel);
    }

    @Override
    public NotaBonificacion getNotaBonificacion(Integer cuenta, String subcuenta, Integer subsiTipo) {
        final NotaBonificacionModel notaBonificacionModel = NotaBonificacionModel.builder()
            .cuenta(cuenta)
            .subcuenta(subcuenta)
            .subsiTipo(subsiTipo)
            .build();
        return campaniaMapper.getNotaBonificacion(notaBonificacionModel);
    }
}
