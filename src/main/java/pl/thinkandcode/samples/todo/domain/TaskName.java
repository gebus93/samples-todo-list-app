package pl.thinkandcode.samples.todo.domain;

import pl.thinkandcode.samples.todo.domain.exceptions.DomainValidationException;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidTaskNameException;

public class TaskName extends Name {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 150;

    public TaskName(String value) {
        super("Task name", MIN_LENGTH, MAX_LENGTH, value);
    }

    @Override
    DomainValidationException validationException(String msg) {
        return new InvalidTaskNameException(msg);
    }
}
