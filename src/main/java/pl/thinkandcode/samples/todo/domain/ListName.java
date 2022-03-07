package pl.thinkandcode.samples.todo.domain;

import pl.thinkandcode.samples.todo.domain.exceptions.DomainValidationException;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidListNameException;

public class ListName extends Name {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public ListName(String value) {
        super("List name", MIN_LENGTH, MAX_LENGTH, value);
    }

    @Override
    DomainValidationException validationException(String msg) {
        return new InvalidListNameException(msg);
    }
}
