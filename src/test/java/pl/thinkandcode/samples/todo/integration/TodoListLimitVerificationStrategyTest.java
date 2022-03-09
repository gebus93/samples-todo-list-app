package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.thinkandcode.samples.todo.adapters.outbound.limits.PropertiesFilesBasedTodoListLimitVerificationStrategy;
import pl.thinkandcode.samples.todo.application.TodoListLimitVerificationStrategy;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

public class TodoListLimitVerificationStrategyTest extends AbstractIntegrationTest {
    private static final int RANDOM_LIMIT = new Random().nextInt(100);
    private static final UUID USER_ID = UUID.fromString("5530201e-4cf7-42ca-9a3c-9fd20634333a");
    @Autowired
    private TodoListLimitVerificationStrategy limitVerificationStrategy;

    @DynamicPropertySource
    static void todoListLimitsConfig(DynamicPropertyRegistry registry) {
        registry.add("todo-lists.limit", () -> RANDOM_LIMIT);
    }

    @Test
    void shouldAutowireInstanceCorrectly() {
        assertThat(limitVerificationStrategy)
                .isNotNull()
                .isInstanceOf(PropertiesFilesBasedTodoListLimitVerificationStrategy.class);
    }

    @Test
    void givenLessTodoListsThanLimit_whenCheckingIfLimitIsExceeded_shouldReturnFalse() {
        // given
        doReturn(RANDOM_LIMIT - 1).when(repository).countAll();

        // when
        var exceeded = limitVerificationStrategy.isExceeded();

        // then
        assertThat(exceeded).isFalse();
    }

    @Test
    void givenNumberOfTodoListsTheSameAsLimit_whenCheckingIfLimitIsExceeded_shouldReturnTrue() {
        // given
        doReturn(RANDOM_LIMIT).when(repository).countAll();

        // when
        var exceeded = limitVerificationStrategy.isExceeded();

        // then
        assertThat(exceeded).isTrue();
    }
}
