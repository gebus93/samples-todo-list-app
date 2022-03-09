package pl.thinkandcode.samples.todo.architecture;

import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class HexagonalArchitectureRulesTest extends AbstractArchitectureTest {
    @Test
    void hexagonalArchitectureLayersShouldBeRespected() {
        onionArchitecture()
                .domainModels("pl.thinkandcode.samples.todo.domain..")
                .domainServices("pl.thinkandcode.samples.todo.domain..")
                .applicationServices("pl.thinkandcode.samples.todo.application..")
                .adapter("persistence", "pl.thinkandcode.samples.todo.adapters.outbound.persistence..")
                .adapter("identifiers", "pl.thinkandcode.samples.todo.adapters.outbound.identifiers..")
                .adapter("limit source", "pl.thinkandcode.samples.todo.adapters.outbound.limits..")
                .adapter("observability", "pl.thinkandcode.samples.todo.adapters.outbound.observability..")
                .check(classes);
    }
}
