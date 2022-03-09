package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.thinkandcode.samples.todo.adapters.outbound.observability.LoggingTodoListOpsObserver;
import pl.thinkandcode.samples.todo.application.TodoListOpsObserver;

import static org.assertj.core.api.Assertions.assertThat;

class TodoListOpsObserverTest extends AbstractIntegrationTest {
    @Autowired(required = false)
    private TodoListOpsObserver observer;

    @Test
    void shouldAutowireInstanceCorrectly() {
        assertThat(observer)
                .isNotNull()
                .isInstanceOf(LoggingTodoListOpsObserver.class);
    }

}