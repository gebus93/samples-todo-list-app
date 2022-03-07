package pl.thinkandcode.samples.todo.domain.exceptions;

public class InvalidTaskNameException extends DomainValidationException {
    public InvalidTaskNameException(String message) {
        super(message);
    }
}
