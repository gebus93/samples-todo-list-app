package pl.thinkandcode.samples.todo.adapters.outbound.limits;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.thinkandcode.samples.todo.application.TodoListLimitVerificationStrategy;
import pl.thinkandcode.samples.todo.application.TodoListRepository;

@Component
@RequiredArgsConstructor
public class PropertiesFilesBasedTodoListLimitVerificationStrategy implements TodoListLimitVerificationStrategy {
    private final PropertiesFilesBasedTodoListLimitProvider limitProvider;
    private final TodoListRepository repository;

    @Override
    public boolean isExceeded() {
        var limit = limitProvider.getLimit();
        var currentUsage = repository.countAll();
        return currentUsage < limit;
    }
}
