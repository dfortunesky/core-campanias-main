package ar.com.smg.campania.domain.helper;

import java.util.Arrays;

import com.sybase.jdbc4.jdbc.SybSQLException;

public final class AflimedHelper {

    private static final Integer[] RAISERRORS = {99999};

    private AflimedHelper() {
    }

    public static boolean isRaiseError(SybSQLException ex) {
        return Arrays.stream(RAISERRORS).anyMatch(code -> code == ex.getErrorCode());
    }

}
