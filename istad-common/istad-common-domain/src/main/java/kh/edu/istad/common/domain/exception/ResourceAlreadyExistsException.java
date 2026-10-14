package kh.edu.istad.common.domain.exception;

public class ResourceAlreadyExistsException extends DomainException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(resource + " with " + field + ": '" + value + "' already exists");
    }
}
