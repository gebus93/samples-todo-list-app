package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.Objects;

public class TodoListCrudService {
    private final TodoListOpsObserver observer;
    private final TodoListLimitVerificationStrategy limitVerificationStrategy;
    private final TodoListIdGenerator idGenerator;
    private final TodoListRepository repository;

    public TodoListCrudService(
            TodoListOpsObserver observer,
            TodoListLimitVerificationStrategy limitVerificationStrategy,
            TodoListIdGenerator idGenerator,
            TodoListRepository repository) {
        this.observer = Objects.requireNonNull(observer, "Observer must not be null");
        this.limitVerificationStrategy = Objects.requireNonNull(limitVerificationStrategy, "Limit verification strategy must not be null");
        this.idGenerator = Objects.requireNonNull(idGenerator, "Todo list id generator must not be null");
        this.repository = Objects.requireNonNull(repository, "Repository must not be null");
    }

    public TodoList createTodoList(CreateTodoListCommand cmd) {
        Objects.requireNonNull(cmd, "Command must not be null");
        if (limitVerificationStrategy.isExceeded()) {
            observer.notifyTodoListCreationFailedDueToTheExceededLimit(cmd);
            return null;
        }
        var todoListId = idGenerator.generateId();
        var todoList = TodoList.create(todoListId, cmd.name());
        repository.save(todoList);
        observer.notifyTodoListCreated(todoList);
        return todoList;
    }
}
