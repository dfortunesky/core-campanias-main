package ar.com.smg.campania.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.com.smg.campania.application.port.out.CampaniaRepositoryPortOut;
import ar.com.smg.campania.application.validator.CampaniaValidator;
import ar.com.smg.campania.domain.campania.NotaBonificacion;

@ExtendWith(MockitoExtension.class)
public class GetNotaBonificacionUseCaseTest {

    @Mock
    private CampaniaRepositoryPortOut campaniaRepositoryPortOut;
    
    @Mock
    private CampaniaValidator campaniaValidator;
    
    private GetNotaBonificacionUseCase getNotaBonificacionUseCase;
    
    @BeforeEach
    void setUp() {
        getNotaBonificacionUseCase = new GetNotaBonificacionUseCase(campaniaRepositoryPortOut, campaniaValidator);
    }
    
    @Test
    void testGetNotaBonificacion_ShouldReturnMockResponse_WhenCalledWithValidParameters() {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "SUB001";
        Integer subsiTipo = 1;
        
        NotaBonificacion expectedNotaBonificacion = NotaBonificacion.builder()
            .tipoSubsidio(subsiTipo)
            .descripcionSubsidio("DESCUENTO ESP 15% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION")
            .nombreCampania("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.15% POR 12 MESES")
            .porcentajeDescuento("15.00")
            .cantidadMeses("12")
            .textoBonificacion("Swiss Medical le otorga un descuento del 15% por 12 meses")
            .build();
        
        when(campaniaRepositoryPortOut.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenReturn(expectedNotaBonificacion);
        
        // When
        NotaBonificacion result = getNotaBonificacionUseCase.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Then
        assertNotNull(result);
        assertEquals(subsiTipo, result.getTipoSubsidio());
        assertEquals("DESCUENTO ESP 15% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION", result.getDescripcionSubsidio());
        assertEquals("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.15% POR 12 MESES", result.getNombreCampania());
        assertEquals("15.00", result.getPorcentajeDescuento());
        assertEquals("12", result.getCantidadMeses());
        assertEquals("Swiss Medical le otorga un descuento del 15% por 12 meses", result.getTextoBonificacion());
        
        // Verify that validator was called
        verify(campaniaValidator).validateNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Verify that repository was called
        verify(campaniaRepositoryPortOut).getNotaBonificacion(cuenta, subcuenta, subsiTipo);
    }
    
    @Test
    void testGetNotaBonificacion_ShouldCallValidator_WhenCalled() {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "1234567890";
        Integer subsiTipo = 1;
        
        NotaBonificacion expectedNotaBonificacion = NotaBonificacion.builder()
            .tipoSubsidio(subsiTipo)
            .descripcionSubsidio("DESCUENTO ESP 30% POR 6 MESES A PARTIR DEL ALTA DE AFILIACION")
            .nombreCampania("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.30% POR 6 MESES")
            .porcentajeDescuento("30.00")
            .cantidadMeses("6")
            .textoBonificacion("Swiss Medical le otorga un descuento del 30% por 6 meses")
            .build();
        
        when(campaniaRepositoryPortOut.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenReturn(expectedNotaBonificacion);
        
        // When
        getNotaBonificacionUseCase.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Then
        verify(campaniaValidator).validateNotaBonificacion(cuenta, subcuenta, subsiTipo);
        verify(campaniaRepositoryPortOut).getNotaBonificacion(cuenta, subcuenta, subsiTipo);
    }
    
    @Test
    void testGetNotaBonificacion_ShouldReturnSameResult_WhenCalledMultipleTimes() {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "1234567890";
        Integer subsiTipo = 1;
        
        NotaBonificacion expectedNotaBonificacion = NotaBonificacion.builder()
            .tipoSubsidio(subsiTipo)
            .descripcionSubsidio("DESCUENTO ESP 15% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION")
            .nombreCampania("INDIVIDUALES AMBA NUBIAL/ CAMPAÑA DTO.INDIV.15% POR 12 MESES")
            .porcentajeDescuento("15.00")
            .cantidadMeses("12")
            .textoBonificacion("Swiss Medical le otorga un descuento del 15% por 12 meses")
            .build();
        
        when(campaniaRepositoryPortOut.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenReturn(expectedNotaBonificacion);
        
        // When
        NotaBonificacion result1 = getNotaBonificacionUseCase.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        NotaBonificacion result2 = getNotaBonificacionUseCase.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getTextoBonificacion(), result2.getTextoBonificacion());
        assertTrue(result1.getTextoBonificacion().contains("Swiss Medical"));
        assertTrue(result2.getTextoBonificacion().contains("descuento"));
        assertTrue(result1.getTextoBonificacion().contains("meses"));
        
        // Verify validator was called twice
        verify(campaniaValidator, times(2)).validateNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Verify repository was called twice
        verify(campaniaRepositoryPortOut, times(2)).getNotaBonificacion(cuenta, subcuenta, subsiTipo);
    }
    
    @Test
    void testGetNotaBonificacion_ShouldHaveValidNumericValues_WhenCalled() {
        // Given
        Integer cuenta = 1234567890;
        String subcuenta = "1234567890";
        Integer subsiTipo = 1;
        
        NotaBonificacion expectedNotaBonificacion = NotaBonificacion.builder()
            .tipoSubsidio(subsiTipo)
            .descripcionSubsidio("DESCUENTO ESP 25% POR 12 MESES A PARTIR DEL ALTA DE AFILIACION")
            .nombreCampania("INDIVIDUALES NUBIAL/ CAMPAÑA MONOTRIBUTO DTO 25%")
            .porcentajeDescuento("25.00")
            .cantidadMeses("12")
            .textoBonificacion("Swiss Medical le otorga un descuento del 25% por 12 meses")
            .build();
        
        when(campaniaRepositoryPortOut.getNotaBonificacion(cuenta, subcuenta, subsiTipo))
            .thenReturn(expectedNotaBonificacion);
        
        // When 
        NotaBonificacion result = getNotaBonificacionUseCase.getNotaBonificacion(cuenta, subcuenta, subsiTipo);
        
        // Then
        assertNotNull(result);
        
        // Verify porcentajeDescuento is a valid number
        assertDoesNotThrow(() -> new BigDecimal(result.getPorcentajeDescuento()));
        BigDecimal porcentaje = new BigDecimal(result.getPorcentajeDescuento());
        assertTrue(porcentaje.compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(porcentaje.compareTo(BigDecimal.valueOf(100)) <= 0);
        
        // Verify cantidadMeses is a valid number
        assertDoesNotThrow(() -> new BigDecimal(result.getCantidadMeses()));
        BigDecimal cantidad = new BigDecimal(result.getCantidadMeses());
        assertTrue(cantidad.compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(cantidad.compareTo(BigDecimal.valueOf(12)) <= 0);
        
        // Verify repository was called
        verify(campaniaRepositoryPortOut).getNotaBonificacion(cuenta, subcuenta, subsiTipo);
    }
}