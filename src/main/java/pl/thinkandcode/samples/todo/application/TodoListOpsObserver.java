package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.UUID;

/**
 * This interface simplifies extending application without changing a code responsible for a business logic.
 * It's an example of the open-close principle. By using it, there are many ways to extend application behaviour.
 * <p>
 * For example:
 * <ul>
 *     <li><b>LoggingTodoListOpsObserver</b> - allows to log significant information outside the application layer of a hexagonal architecture. It's a good way to clean the business logic code.</li>
 *     <li><b>BusinessMetricsCollectingTodoListOpsObserver</b> - creates business metrics using provided information and publishes them to the metrics collector</li>
 *     <li><b>TransactionalOutboxEventPublishingTodoListOpsObserver</b> - allows to propagate information about actions performed in the application via events.
 *     It uses <a href='https://microservices.io/patterns/data/transactional-outbox.html'>transactional outbox pattern</a> to make sure that event will be published as an atomic action.</li>
 * </ul>
 */
public interface TodoListOpsObserver {
    void notifyTodoListCreationFailedDueToTheExceededLimit(CreateTodoListCommand cmd);

    void notifyTodoListCreated(TodoList todoList);

    void notifyUpdatedTodoListDoesNotExist(UpdateTodoListCommand cmd);

    void notifyTodoListUpdated(TodoList todoList);

    void notifyDeletedTodoListDoesNotExist(DeleteTodoListCommand cmd);

    void notifyTodoListDeleted(UUID todoListId);
}
