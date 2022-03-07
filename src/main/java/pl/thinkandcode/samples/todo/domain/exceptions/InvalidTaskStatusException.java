package pl.thinkandcode.samples.todo.domain.exceptions;

public class InvalidTaskStatusException extends DomainValidationException {
    public InvalidTaskStatusException() {
        super("Task status must not be null");
    }
}
