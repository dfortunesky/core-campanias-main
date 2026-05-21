package ar.com.smg.template.domain.helper;

import java.util.Arrays;

import com.sybase.jdbc4.jdbc.SybSQLException;

/**
 * Helper para manejo de errores específicos de Sybase.
 * Permite identificar errores de tipo RAISERROR lanzados por stored procedures.
 * Agregar códigos de error personalizados en el array RAISERRORS según necesidad.
 */
public final class SybaseHelper {

    private static final Integer[] RAISERRORS = {99999};

    private SybaseHelper() {
    }

    public static boolean isRaiseError(SybSQLException ex) {
        return Arrays.stream(RAISERRORS).anyMatch(code -> code == ex.getErrorCode());
    }
}
