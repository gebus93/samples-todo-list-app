package pl.thinkandcode.samples.todo.application.exceptions;

import java.util.UUID;

public class TodoListDoesNotExistException extends RuntimeException {
    public TodoListDoesNotExistException(UUID todoListId) {
        super("Could not find to do list with id '%s'".formatted(todoListId));
    }
}
