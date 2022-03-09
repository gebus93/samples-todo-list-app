package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.thinkandcode.samples.todo.adapters.outbound.limits.PropertiesFilesBasedTodoListLimitVerificationStrategy;
import pl.thinkandcode.samples.todo.application.TodoListLimitVerificationStrategy;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TodoListLimitVerificationStrategyTest extends AbstractIntegrationTest {
    private static final int RANDOM_LIMIT = new Random().nextInt(100);
    @Autowired
    private TodoListLimitVerificationStrategy limitVerificationStrategy;

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("todo-lists.limit", () -> RANDOM_LIMIT);
    }

    @Test
    void shouldAutowireInstanceCorrectly() {
        assertThat(limitVerificationStrategy)
                .isNotNull()
                .isInstanceOf(PropertiesFilesBasedTodoListLimitVerificationStrategy.class);
    }

    @Test
    void givenLessTodoListsThanLimit_whenCheckingIfLimitIsExceeded_shouldReturnFalse() throws Exception {
        // given
        when(repository.countAll()).thenReturn(RANDOM_LIMIT - 1);

        // when
        var exceeded = limitVerificationStrategy.isExceeded();

        // then
        assertThat(exceeded).isFalse();
    }

    @Test
    void givenNumberOfTodoListsTheSameAsLimit_whenCheckingIfLimitIsExceeded_shouldReturnTrue() throws Exception {
        // given
        when(repository.countAll()).thenReturn(RANDOM_LIMIT);

        // when
        var exceeded = limitVerificationStrategy.isExceeded();

        // then
        assertThat(exceeded).isTrue();
    }
}
