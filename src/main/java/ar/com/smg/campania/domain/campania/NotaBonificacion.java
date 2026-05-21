package ar.com.smg.campania.domain.campania;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaBonificacion {
    private Integer tipoSubsidio;
    private String descripcionSubsidio;
    private String nombreCampania;
    private String porcentajeDescuento;
    private String cantidadMeses;
    private String textoBonificacion;
    private Integer rechaMotivo;
    private String rechaDescripcion;
}