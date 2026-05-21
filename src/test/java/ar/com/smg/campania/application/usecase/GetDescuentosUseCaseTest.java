package ar.com.smg.campania.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.com.smg.campania.application.port.out.CampaniaRepositoryPortOut;
import ar.com.smg.campania.application.validator.CampaniaValidator;
import ar.com.smg.campania.domain.campania.Descuento;
import ar.com.smg.campania.domain.campania.DescuentoZona;
import ar.com.smg.campania.domain.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class GetDescuentosUseCaseTest {

    @Mock
    private CampaniaRepositoryPortOut campaniaRepositoryPortOut;
    
    @Mock
    private CampaniaValidator campaniaValidator;
    
    private GetDescuentosUseCase getDescuentosUseCase;
    
    @BeforeEach
    void setUp() {
        getDescuentosUseCase = new GetDescuentosUseCase(campaniaRepositoryPortOut, campaniaValidator);
    }
    
    @Test
    void testGetDescuentos_ShouldReturnDescuentoZona_WhenCalledWithValidParameters() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
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
        
        DescuentoZona descuentoZona = DescuentoZona.builder()
            .zona(zona)
            .condicion(Integer.parseInt(condicion, Character.MAX_RADIX))
            .descuentos(descuentos)
            .build();
        
        when(campaniaRepositoryPortOut.getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento)))
            .thenReturn(descuentoZona);
        
        // When
        DescuentoZona result = getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Then
        assertNotNull(result);
        assertEquals(zona, result.getZona());
        assertNotNull(result.getDescuentos());
        assertEquals(2, result.getDescuentos().size());
        
        // Verify first descuento
        assertEquals(123456, result.getDescuentos().get(0).getCuenta());
        assertEquals("SUB001", result.getDescuentos().get(0).getSubCuenta());
        assertEquals(BigDecimal.valueOf(15.000000), result.getDescuentos().get(0).getPorcentajeDescuento());
        assertEquals(12, result.getDescuentos().get(0).getCantidadMeses());
        
        // Verify second descuento
        assertEquals(123457, result.getDescuentos().get(1).getCuenta());
        assertEquals("SUB002", result.getDescuentos().get(1).getSubCuenta());
        assertEquals(BigDecimal.valueOf(20.000000), result.getDescuentos().get(1).getPorcentajeDescuento());
        assertEquals(6, result.getDescuentos().get(1).getCantidadMeses());
        
        // Verify that validator was called
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Verify that repository was called
        verify(campaniaRepositoryPortOut).getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento));
    }
    
    @Test
    void testGetDescuentos_ShouldCallValidator_WhenCalled() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        DescuentoZona descuentoZona = DescuentoZona.builder()
            .zona(zona)
            .condicion(10)
            .descuentos(Arrays.asList())
            .build();
        
        when(campaniaRepositoryPortOut.getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento)))
            .thenReturn(descuentoZona);
        
        // When
        getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Then
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
    }
    
    @Test
    void testGetDescuentos_ShouldCallRepository_WithCorrectParameters() {
        // Given
        Integer zona = 2;
        String condicion = "B";
        Integer tipoCotizador = 2;
        String plan = "PLAN_002";
        BigDecimal descuento = BigDecimal.valueOf(25.000000);
        
        DescuentoZona descuentoZona = DescuentoZona.builder()
            .zona(zona)
            .condicion(11)
            .descuentos(Arrays.asList())
            .build();
        
        when(campaniaRepositoryPortOut.getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento)))
            .thenReturn(descuentoZona);
        
        // When
        getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Then
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        verify(campaniaRepositoryPortOut).getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            dateCaptor.capture(), eq(plan), eq(descuento));
        
        // Verify that the date captured is not null and is recent
        Date capturedDate = dateCaptor.getValue();
        assertNotNull(capturedDate);
        assertTrue(capturedDate.getTime() <= System.currentTimeMillis());
    }
    
    @Test
    void testGetDescuentos_ShouldThrowValidationException_WhenZonaIsNull() {
        // Given
        Integer zona = null;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        doThrow(new ValidationException("Zona, condición son requeridos"))
            .when(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        });
        
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verifyNoInteractions(campaniaRepositoryPortOut);
    }
    
    @Test
    void testGetDescuentos_ShouldThrowValidationException_WhenCondicionIsNull() {
        // Given
        Integer zona = 1;
        String condicion = null;
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        doThrow(new ValidationException("Zona, condición son requeridos"))
            .when(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        });
        
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verifyNoInteractions(campaniaRepositoryPortOut);
    }
    
    @Test
    void testGetDescuentos_ShouldThrowValidationException_WhenTipoCotizadorIsNull() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = null;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        doThrow(new ValidationException("Tipo de cotizador es requerido"))
            .when(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        });
        
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verifyNoInteractions(campaniaRepositoryPortOut);
    }
    
    @Test
    void testGetDescuentos_ShouldThrowValidationException_WhenPlanProvidedButDescuentoIsNull() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = null;
        
        doThrow(new ValidationException("Si se proporciona un plan, se debe proporcionar un descuento"))
            .when(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        });
        
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verifyNoInteractions(campaniaRepositoryPortOut);
    }
    
    @Test
    void testGetDescuentos_ShouldThrowValidationException_WhenDescuentoProvidedButPlanIsNull() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = null;
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        doThrow(new ValidationException("Si se proporciona un descuento, se debe proporcionar un plan"))
            .when(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // When & Then
        assertThrows(ValidationException.class, () -> {
            getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        });
        
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verifyNoInteractions(campaniaRepositoryPortOut);
    }
    
    @Test
    void testGetDescuentos_ShouldWorkCorrectly_WhenPlanAndDescuentoAreNull() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = null;
        BigDecimal descuento = null;
        
        DescuentoZona descuentoZona = DescuentoZona.builder()
            .zona(zona)
            .condicion(10)
            .descuentos(Arrays.asList())
            .build();
        
        when(campaniaRepositoryPortOut.getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento)))
            .thenReturn(descuentoZona);
        
        // When
        DescuentoZona result = getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Then
        assertNotNull(result);
        assertEquals(zona, result.getZona());
        verify(campaniaValidator).validateDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        verify(campaniaRepositoryPortOut).getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento));
    }
    
    @Test
    void testGetDescuentos_ShouldReturnEmptyList_WhenNoDescuentosFound() {
        // Given
        Integer zona = 1;
        String condicion = "A";
        Integer tipoCotizador = 1;
        String plan = "PLAN_001";
        BigDecimal descuento = BigDecimal.valueOf(15.000000);
        
        DescuentoZona descuentoZona = DescuentoZona.builder()
            .zona(zona)
            .condicion(10)
            .descuentos(Arrays.asList())
            .build();
        
        when(campaniaRepositoryPortOut.getDescuentos(eq(zona), eq(condicion), eq(tipoCotizador), 
            any(Date.class), eq(plan), eq(descuento)))
            .thenReturn(descuentoZona);
        
        // When
        DescuentoZona result = getDescuentosUseCase.getDescuentos(zona, condicion, tipoCotizador, plan, descuento);
        
        // Then
        assertNotNull(result);
        assertEquals(zona, result.getZona());
        assertNotNull(result.getDescuentos());
        assertEquals(0, result.getDescuentos().size());
    }
}
