package ar.com.smg.campania.domain;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseWrapper<T> {
    private String resource;
    private int status;
    private T data;
    private LocalDateTime timestamp;
    private Map<String, String> metadata;

    public ResponseWrapper(String resource, int status, T data, Map<String, String> metadata) {
        this.resource = resource;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.metadata = metadata;
    }

    public String getResource() { return resource; }
    public int getStatus() { return status; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, String> getMetadata() { return metadata; }
}

