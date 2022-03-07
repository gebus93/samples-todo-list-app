package pl.thinkandcode.samples.todo.domain.exceptions;

public abstract class DomainValidationException extends RuntimeException {
    protected DomainValidationException() {
        super();
    }

    protected DomainValidationException(String message) {
        super(message);
    }

    protected DomainValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DomainValidationException(Throwable cause) {
        super(cause);
    }

    protected DomainValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
