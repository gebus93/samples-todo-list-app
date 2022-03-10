package pl.thinkandcode.samples.todo.architecture;

import org.junit.jupiter.api.Test;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.security.AuthenticatedUser;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.LoggedUserDataProvider;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class HexagonalArchitectureRulesTest extends AbstractArchitectureTest {
    @Test
    void hexagonalArchitectureLayersShouldBeRespected() {
        onionArchitecture()
                .domainModels("pl.thinkandcode.samples.todo.domain..")
                .domainServices("pl.thinkandcode.samples.todo.domain..")
                .applicationServices("pl.thinkandcode.samples.todo.application..")
                .adapter("rest", "pl.thinkandcode.samples.todo.adapters.inbound.rest..")
                .adapter("persistence", "pl.thinkandcode.samples.todo.adapters.outbound.persistence..")
                .adapter("identifiers", "pl.thinkandcode.samples.todo.adapters.outbound.identifiers..")
                .adapter("limit source", "pl.thinkandcode.samples.todo.adapters.outbound.limits..")
                .adapter("observability", "pl.thinkandcode.samples.todo.adapters.outbound.observability..")
                .ignoreDependency(LoggedUserDataProvider.class, AuthenticatedUser.class)
                .check(classes);
    }
}
