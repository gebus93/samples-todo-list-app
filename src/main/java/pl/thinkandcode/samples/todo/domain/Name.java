package pl.thinkandcode.samples.todo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.thinkandcode.samples.todo.domain.exceptions.DomainValidationException;

@EqualsAndHashCode
public abstract class Name {
    private final int minLength;
    private final int maxLength;
    private final String valueMustNotBeBlank;
    private final String valueHasInvalidLength;
    @Getter
    private final String value;

    protected Name(String fieldName, int minLength, int maxLength, String value) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.valueMustNotBeBlank = fieldName + " must not be null nor blank";
        this.valueHasInvalidLength = fieldName + " must have at least " + this.minLength + " characters " +
                "and no more than " + this.maxLength + ".";
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw validationException(valueMustNotBeBlank);
        }
        value = trimWhitespaces(value);
        if (value.isBlank()) {
            throw validationException(valueMustNotBeBlank);
        }
        var length = value.length();
        if (length < minLength || length > maxLength) {
            throw validationException(valueHasInvalidLength);
        }
    }

    abstract DomainValidationException validationException(String msg);

    private String trimWhitespaces(String value) {
        return value.trim().replaceAll("(^[\\h\\v\\s\u202a-\u202e]*)|([\\h\\v\\s\u202a-\u202e]*$)", "");
    }

    @Override
    public String toString() {
        return value;
    }
}
