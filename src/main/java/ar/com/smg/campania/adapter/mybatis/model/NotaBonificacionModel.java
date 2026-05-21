package ar.com.smg.campania.adapter.mybatis.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaBonificacionModel {
    private Integer cuenta;
    private String subcuenta;
    private Integer subsiTipo;
}
