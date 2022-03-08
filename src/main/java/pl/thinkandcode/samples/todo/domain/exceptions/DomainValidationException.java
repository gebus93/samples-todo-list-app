package pl.thinkandcode.samples.todo.domain.exceptions;

public abstract class DomainValidationException extends RuntimeException {
    protected DomainValidationException(String message) {
        super(message);
    }
}
