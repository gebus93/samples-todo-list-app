package pl.thinkandcode.samples.todo.adapters.outbound.persistence;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class will read user data from spring security, but for now it will work as a stub.
 * <p>
 * TODO replace it when spring-security will be used in a project
 */
@Component
public class LoggedUserDataProvider {
    public UUID getUserId() {
        return UUID.fromString("ecdec9cd-6a51-47a1-af89-78e7426ded96");
    }
}
