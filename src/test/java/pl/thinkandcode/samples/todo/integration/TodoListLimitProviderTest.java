package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.thinkandcode.samples.todo.adapters.outbound.limits.PropertiesFilesBasedTodoListLimitProvider;
import pl.thinkandcode.samples.todo.application.TodoListLimitProvider;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoListLimitProviderTest extends AbstractIntegrationTest {
    private static final int RANDOM_LIMIT = new Random().nextInt(100);
    @Autowired
    private TodoListLimitProvider limitProvider;

    @DynamicPropertySource
    static void todoListLimitsConfig(DynamicPropertyRegistry registry) {
        registry.add("todo-lists.limit", () -> RANDOM_LIMIT);
    }

    @Test
    void shouldAutowireInstanceCorrectly() {
        assertThat(limitProvider)
                .isNotNull()
                .isInstanceOf(PropertiesFilesBasedTodoListLimitProvider.class);
    }

    @Test
    void whenGettingLimit_shouldReturnValueFromPropertiesFile() {
        // when
        var limit = limitProvider.getLimit();

        // then
        assertThat(limit).isEqualTo(RANDOM_LIMIT);
    }
}
