package ar.com.smg.campania.adapter.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;

import ar.com.smg.campania.adapter.mybatis.model.DescuentoModel;
import ar.com.smg.campania.adapter.mybatis.model.NotaBonificacionModel;
import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.domain.campania.NotaBonificacion;

@Mapper
public interface CampaniaMapper {
    DescuentoZona getDescuentos(DescuentoModel descuentoModel);
    NotaBonificacion getNotaBonificacion(NotaBonificacionModel notaBonificacionModel);
}
