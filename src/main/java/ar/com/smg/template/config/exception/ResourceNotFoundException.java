package ar.com.smg.template.config.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Could not find resource with id " + id);
    }
}
