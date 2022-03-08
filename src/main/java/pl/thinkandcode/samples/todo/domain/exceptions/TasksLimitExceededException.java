package pl.thinkandcode.samples.todo.domain.exceptions;

public class TasksLimitExceededException extends DomainValidationException {
    public TasksLimitExceededException(int limit) {
        super("Tasks limit has been exceeded. Each to do list can contain no more than " + limit + " tasks.");
    }
}
