package pl.thinkandcode.samples.todo.domain;

import pl.thinkandcode.samples.todo.domain.exceptions.InvalidListNameException;

public record ListName(String value) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String VALUE_MUST_NOT_BE_BLANK = "List name must not be null nor blank";
    private static final String VALUE_HAS_INVALID_LENGTH
            = "List name must have at least " + MIN_LENGTH + " characters and no more than " + MAX_LENGTH + ".";

    public ListName {
        validateValue(value);
    }

    private void validateValue(String value) {
        if (value == null) {
            throw new InvalidListNameException(VALUE_MUST_NOT_BE_BLANK);
        }
        value = trimWhitespaces(value);
        if (value.isBlank()) {
            throw new InvalidListNameException(VALUE_MUST_NOT_BE_BLANK);
        }
        var length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new InvalidListNameException(VALUE_HAS_INVALID_LENGTH);
        }
    }

    private String trimWhitespaces(String value) {
        return value.trim().replaceAll("(^[\\h\\v\\s\u202a-\u202e]*)|([\\h\\v\\s\u202a-\u202e]*$)", "");
    }
}
