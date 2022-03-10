package pl.thinkandcode.samples.todo.application;

import org.springframework.stereotype.Service;
import pl.thinkandcode.samples.todo.application.UpdateTodoListCommand.Task;
import pl.thinkandcode.samples.todo.application.exceptions.TodoListDoesNotExistException;
import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.Objects;

@Service
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

    public void updateTodoList(UpdateTodoListCommand cmd) {
        Objects.requireNonNull(cmd, "Command must not be null");
        var todoListId = Objects.requireNonNull(cmd.id(), "List id must not be null");
        if (!repository.exists(todoListId)) {
            observer.notifyUpdatedTodoListDoesNotExist(cmd);
            throw new TodoListDoesNotExistException(todoListId);
        }
        var todoList = TodoList.create(todoListId, cmd.name());
        List<Task> tasks = Objects.requireNonNullElse(cmd.tasks(), List.of());
        for (var task : tasks) {
            todoList.addTask(task.name(), task.status());
        }

        repository.save(todoList);
        observer.notifyTodoListUpdated(todoList);
    }

    public void deleteTodoList(DeleteTodoListCommand cmd) {
        Objects.requireNonNull(cmd, "Command must not be null");
        var todoListId = Objects.requireNonNull(cmd.id(), "List id must not be null");
        if (!repository.exists(todoListId)) {
            observer.notifyDeletedTodoListDoesNotExist(cmd);
            return;
        }
        repository.delete(todoListId);
        observer.notifyTodoListDeleted(todoListId);
    }

    public TodoList getTodoList(GetTodoListQuery query) {
        Objects.requireNonNull(query, "Query must not be null");
        var todoListId = Objects.requireNonNull(query.id(), "List id must not be null");
        return repository
                .findTodoList(todoListId)
                .orElseThrow(() -> new TodoListDoesNotExistException(todoListId));
    }

    public List<TodoList> getAllTodoLists() {
        return repository.findAll();
    }
}
