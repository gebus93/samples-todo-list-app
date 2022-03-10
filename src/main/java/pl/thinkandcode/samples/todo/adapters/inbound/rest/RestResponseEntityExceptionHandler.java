package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.thinkandcode.samples.todo.application.exceptions.TodoListDoesNotExistException;
import pl.thinkandcode.samples.todo.application.exceptions.TodoListsLimitExceededException;
import pl.thinkandcode.samples.todo.domain.exceptions.DomainValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { TodoListsLimitExceededException.class })
    protected ResponseEntity<Object> todoListsLimitExceptionHandler(RuntimeException ex, WebRequest request, HttpServletRequest servletRequest) {
        var status = HttpStatus.FORBIDDEN;
        return createErrorResponse(status, servletRequest, ex, request);
    }

    @ExceptionHandler(value = { TodoListDoesNotExistException.class })
    protected ResponseEntity<Object> todoListsDoesNotExistExceptionHandler(RuntimeException ex, WebRequest request, HttpServletRequest servletRequest) {
        var status = HttpStatus.NOT_FOUND;
        return createErrorResponse(status, servletRequest, ex, request);
    }

    @ExceptionHandler(value = { DomainValidationException.class })
    protected ResponseEntity<Object> domainValidationExceptionHandler(RuntimeException ex, WebRequest request, HttpServletRequest servletRequest) {
        var status = HttpStatus.BAD_REQUEST;
        return createErrorResponse(status, servletRequest, ex, request);
    }

    private ResponseEntity<Object> createErrorResponse(HttpStatus status,
                                                       HttpServletRequest servletRequest,
                                                       RuntimeException ex,
                                                       WebRequest request) {
        var errorResponse = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                servletRequest.getRequestURI(),
                ex.getMessage()
        );
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    public record ErrorResponse(Instant timestamp, int status, String error, String path, String detail) {
    }
}