package pl.thinkandcode.samples.todo.domain.exceptions;

public class InvalidListNameException extends DomainValidationException {
    public InvalidListNameException(String message) {
        super(message);
    }
}
