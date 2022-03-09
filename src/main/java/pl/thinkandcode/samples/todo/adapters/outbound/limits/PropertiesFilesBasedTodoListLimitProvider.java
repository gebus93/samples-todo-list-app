package pl.thinkandcode.samples.todo.adapters.outbound.limits;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.thinkandcode.samples.todo.application.TodoListLimitProvider;

@Component
@RequiredArgsConstructor
public class PropertiesFilesBasedTodoListLimitProvider implements TodoListLimitProvider {
    private final TodoListLimitProperties properties;

    @Override
    public int getLimit() {
        return properties.limit();
    }
}
