package pl.thinkandcode.samples.todo.adapters.outbound.identifiers;

import org.springframework.stereotype.Component;
import pl.thinkandcode.samples.todo.application.TodoListIdGenerator;

import java.util.UUID;

@Component
public class RandomUuidTodoListIdGenerator implements TodoListIdGenerator {
    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
