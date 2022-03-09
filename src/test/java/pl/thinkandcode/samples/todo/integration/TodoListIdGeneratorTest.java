package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.thinkandcode.samples.todo.application.TodoListIdGenerator;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoListIdGeneratorTest extends AbstractIntegrationTest {
    @Autowired
    private TodoListIdGenerator idGenerator;

    @Test
    void givenAutowiredInstance_whenGeneratingIds_shouldGenerateUniqueIdEachTime() {
        // when
        var generatedIds = IntStream.range(0, 100)
                                    .mapToObj(i -> idGenerator.generateId())
                                    .collect(Collectors.toSet());

        // then
        assertThat(generatedIds).hasSize(100);
    }
}
