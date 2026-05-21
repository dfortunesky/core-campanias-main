package ar.com.smg.campania.adapter.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ar.com.smg.campania.application.port.in.GetDescuentosPortIn;
import ar.com.smg.campania.application.port.in.GetNotaBonificacionPortIn;
import ar.com.smg.campania.domain.campania.Descuento;
import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.domain.campania.NotaBonificacion;
import ar.com.smg.campania.domain.exception.ValidationException;

@WebMvcTest(CampaniasControllerAdapter.class)
public class CampaniasControllerAdapterTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GetNotaBonificacionPortIn getNotaBonificacionPortIn;
    
    @MockBean
    private GetDescuentosPortIn getDescuentosPortIn;
    
    private NotaBonificacion notaBonificacion;
    private DescuentoZona descuentoZona;
    
    @BeforeEach
    void setUp() {
        // Setup NotaBonificacion mock data
        notaBonificacion = NotaBonificacion.builder()
            .tipoSubsidio(1)
            .descripcionSubsidio("DESCUENTO ESP 15% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION")
            .nombreCampania("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.15% POR 12 MESES")
            .porcentajeDescuento("15")
            .cantidadMeses("12")
            .textoBonificacion("Swiss Medical le otorga un descuento del 15% durante 12 meses")
            .build();
        
        // Setup DescuentoZona mock data
        Descuento descuento1 = Descuento.builder()
            .cuenta(123456)
            .subCuenta("SUB001")
            .denominacionSubcuenta("Descuento 1")
            .plan("PLAN_001")
            .tarifa("TARIFA_001")
            .porcentajeDescuento(BigDecimal.valueOf(15.000000))
            .subsi_tipo("TIPO_001")
            .denominacionSubsidio("Subsidio 1")
            .vigenciaDesde("2025-01-01")
            .vigenciaHasta("2025-12-31")
            .cantidadMeses(12)
            .build();
            
        Descuento descuento2 = Descuento.builder()
            .cuenta(123457)
            .subCuenta("SUB002")
            .denominacionSubcuenta("Descuento 2")
            .plan("PLAN_002")
            .tarifa("TARIFA_002")
            .porcentajeDescuento(BigDecimal.valueOf(20.000000))
            .subsi_tipo("TIPO_002")
            .denominacionSubsidio("Subsidio 2")
            .vigenciaDesde("2025-01-01")
            .vigenciaHasta("2025-06-30")
            .cantidadMeses(6)
            .build();
            
        List<Descuento> descuentos = Arrays.asList(descuento1, descuento2);
        
        descuentoZona = DescuentoZona.builder()
            .zona(1)
            .condicion(10)
            .descuentos(descuentos)
            .build();
    }
    
    // ========== Tests para GET /api/v1/campanias/{cuenta}/{subcuenta}/{subsiTipo}/nota-bonificacion ==========
    
    @Test
    void testGetNotaBonificacion_ShouldReturnOkAndCorrectResponse_WhenCalledWithValidParameters() throws Exception {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "1234567890";
        Integer subsiTipo = 1;
        
        when(getNotaBonificacionPortIn.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenReturn(notaBonificacion);
        
        // When & Then
        mockMvc.perform(get("/api/v1/campanias/{cuenta}/{subcuenta}/{subsiTipo}/nota-bonificacion", 
                cuenta, subcuenta, subsiTipo)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.tipoSubsidio").value(1))
            .andExpect(jsonPath("$.data.descripcionSubsidio").value("DESCUENTO ESP 15% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION"))
            .andExpect(jsonPath("$.data.nombreCampania").value("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.15% POR 12 MESES"))
            .andExpect(jsonPath("$.data.porcentajeDescuento").value("15"))
            .andExpect(jsonPath("$.data.cantidadMeses").value("12"))
            .andExpect(jsonPath("$.data.textoBonificacion").value("Swiss Medical le otorga un descuento del 15% durante 12 meses"));
        
        verify(getNotaBonificacionPortIn).getNotaBonificacion(cuenta, subcuenta, subsiTipo);
    }
    
    @Test
    void testGetNotaBonificacion_ShouldReturnBadRequest_WhenValidationExceptionIsThrown() throws Exception {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "1234567890";
        Integer subsiTipo = 1;
        
        when(getNotaBonificacionPortIn.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenThrow(new ValidationException("Cuenta, subcuenta y subsiTipo son requeridos"));
        
        // When & Then
        mockMvc.perform(get("/api/v1/campanias/{cuenta}/{subcuenta}/{subsiTipo}/nota-bonificacion", 
                cuenta, subcuenta, subsiTipo)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
    
    // ========== Tests para GET /api/v1/campanias/{zona}/{condicion}/{tipoCotizador}/descuentos ==========
    
    @Test
    void testGetDescuentos_ShouldReturnOkAndCorrectResponse_WhenCalledWithAllParameters() throws Exception {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        when(getDescuentosPortIn.getDescuentos(zona, condicion, tipoCotizador, plan, descuento))
            .thenReturn(descuentoZona);
        
        // When & Then
        mockMvc.perform(get("/api/v1/campanias/{zona}/{condicion}/{tipoCotizador}/descuentos", 
                zona, condicion, tipoCotizador)
                .param("plan", plan)
                .param("descuento", descuento.toPlainString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.zona").value(1))
            .andExpect(jsonPath("$.data.condicion").value(10))
            .andExpect(jsonPath("$.data.descuentos").isArray())
            .andExpect(jsonPath("$.data.descuentos.length()").value(2));
        
        verify(getDescuentosPortIn).getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
    }
    
    @Test
    void testGetDescuentos_ShouldReturnOk_WhenCalledWithoutOptionalParameters() throws Exception {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        
        when(getDescuentosPortIn.getDescuentos(zona, condicion, tipoCotizador, null, null))
            .thenReturn(descuentoZona);
        
        // When & Then
        mockMvc.perform(get("/api/v1/campanias/{zona}/{condicion}/{tipoCotizador}/descuentos", 
                zona, condicion, tipoCotizador)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.zona").value(1))
            .andExpect(jsonPath("$.data.descuentos").isArray());
        
        verify(getDescuentosPortIn).getDescuentos(zona, condicion, tipoCotizador, null, null);
    }
    
    @Test
    void testGetDescuentos_ShouldReturnBadRequest_WhenValidationExceptionIsThrown() throws Exception {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        
        when(getDescuentosPortIn.getDescuentos(zona, condicion, tipoCotizador, null, null))
            .thenThrow(new ValidationException("Zona, condición son requeridos"));
        
        // When & Then
        mockMvc.perform(get("/api/v1/campanias/{zona}/{condicion}/{tipoCotizador}/descuentos", 
                zona, condicion, tipoCotizador)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
