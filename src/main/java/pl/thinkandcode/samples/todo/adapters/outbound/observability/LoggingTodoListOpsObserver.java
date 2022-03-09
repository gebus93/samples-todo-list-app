package pl.thinkandcode.samples.todo.adapters.outbound.observability;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.thinkandcode.samples.todo.application.CreateTodoListCommand;
import pl.thinkandcode.samples.todo.application.DeleteTodoListCommand;
import pl.thinkandcode.samples.todo.application.TodoListOpsObserver;
import pl.thinkandcode.samples.todo.application.UpdateTodoListCommand;
import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.UUID;

@Slf4j
@Component
public class LoggingTodoListOpsObserver implements TodoListOpsObserver {
    @Override
    public void notifyTodoListCreationFailedDueToTheExceededLimit(CreateTodoListCommand cmd) {
        log.info("To do list creation failed. Limit has been exceeded.");
    }

    @Override
    public void notifyTodoListCreated(TodoList todoList) {
        log.info("To do list with id '{}' has been created.", todoList.getId());
    }

    @Override
    public void notifyUpdatedTodoListDoesNotExist(UpdateTodoListCommand cmd) {
        log.info("To do list with id '{}' does not exist.", cmd.id());
    }

    @Override
    public void notifyTodoListUpdated(TodoList todoList) {
        log.info("To do list with id '{}' has been updated.", todoList.getId());
    }

    @Override
    public void notifyDeletedTodoListDoesNotExist(DeleteTodoListCommand cmd) {
        log.info("Could not delete to do list, because it does not exist.");
    }

    @Override
    public void notifyTodoListDeleted(UUID todoListId) {
        log.info("To do list with id '{}' has been deleted.", todoListId);
    }
}
