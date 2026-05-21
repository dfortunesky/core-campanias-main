package ar.com.smg.campania.config;

public enum ErrorCode {

    BAD_REQUEST(105, "105", "La request esta mal formateada", "CORE-CAMPANIA-BAD_REQUEST"),
    INVALID_PARAMETERS_ERROR(110, "110", "{}", "CORE-CAMPANIA-INVALID_PARAMETERS"),
    WEB_CLIENT_GENERIC(103, "103", "Unexpected rest client error", "CORE-CAMPANIA-INTERNAL_SERVER_ERROR"),
    INTERNAL_ERROR(108, "108", "Internal Error", "CORE-CAMPANIA-INTERNAL_ERROR"),
    INVALID_FILTERS_ERROR(109, "107", "Invalid filters", "CORE-CAMPANIA-INVALID_FILTERS"),
    DATA_ACCESS_ERROR(111, "111", "Unable to access Account data", "CORE-CAMPANIA-DATA_ACCESS_ERROR"),
    ESTABLISHMENT_SERVICE_ERROR(112, "112", "Establishment service error", "CORE-CAMPANIA-ESTABLISHMENT_SERVICE_ERROR"),
    AMOUNT_INVALID(113, "113", "Establishment service error", "CORE-CAMPANIA-ESTABLISHMENT_SERVICE_ERROR"),
    VALIDATION_ERROR(115, "115", "Error de validación", "CORE-CAMPANIA-VALIDATION_ERROR"),
    NOT_FOUND_ERROR(114, "114", "No encontrado", "CORE-CAMPANIA-NOT_FOUND");

    private final int value;
    private final String status;
    private final String detail;
    private final String code;

    ErrorCode(final int value, final String status, final String detail, final String code) {
        this.value = value;
        this.status = status;
        this.detail = detail;
        this.code = code;
    }

    public int value() {
        return this.value;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDetail() {
        return this.detail;
    }

    public String getCode() {
        return this.code;
    }
}