package pl.thinkandcode.samples.todo.application.exceptions;

public class TodoListsLimitExceededException extends RuntimeException {
    public TodoListsLimitExceededException() {
        super("Could not create todo list. Limit has been exceeded.");
    }
}
