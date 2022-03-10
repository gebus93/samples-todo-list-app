package pl.thinkandcode.samples.todo.adapters.outbound.persistence;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.security.AuthenticatedUser;

import java.util.UUID;

@Component
public class LoggedUserDataProvider {
    public UUID getUserId() {
        var principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
}
