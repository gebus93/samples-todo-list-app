package pl.thinkandcode.samples.todo.adapters.outbound.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity.Task;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity.TodoListDocument;
import pl.thinkandcode.samples.todo.application.TodoListRepository;
import pl.thinkandcode.samples.todo.domain.TaskStatus;
import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DefaultTodoListRepository implements TodoListRepository {
    private final SpringDataTodoListRepository repository;
    private final LoggedUserDataProvider userDataProvider;

    @Override
    public void save(TodoList todoList) {
        var todoListDocument = repository
                .findById(todoList.getId())
                .orElseGet(() -> {
                    var document = new TodoListDocument();
                    document.setId(todoList.getId());
                    document.setOwnerId(userDataProvider.getUserId());
                    return document;
                });
        var tasks = todoList.getTasks()
                            .stream()
                            .map(t -> new Task(t.getName().getValue(), t.getStatus().name()))
                            .toList();

        todoListDocument.setName(todoList.getListName().getValue());
        todoListDocument.setTasks(tasks);
        repository.save(todoListDocument);
    }

    @Override
    public boolean exists(UUID todoListId) {
        return repository.existsByIdAndOwnerId(todoListId, userDataProvider.getUserId());
    }

    @Override
    public void delete(UUID todoListId) {
        repository.deleteByIdAndOwnerId(todoListId, userDataProvider.getUserId());
    }

    @Override
    public Optional<TodoList> findTodoList(UUID todoListId) {
        return repository.findByIdAndOwnerId(todoListId, userDataProvider.getUserId())
                         .map(this::mapToDomainObject);
    }

    @Override
    public List<TodoList> findAll() {
        return repository.findAllByOwnerId(userDataProvider.getUserId())
                         .stream()
                         .map(this::mapToDomainObject)
                         .toList();
    }

    @Override
    public int countAll() {
        return repository.countAllByOwnerId(userDataProvider.getUserId());
    }

    private TodoList mapToDomainObject(TodoListDocument document) {
        var todoList = TodoList.create(document.getId(), document.getName());
        Optional.ofNullable(document.getTasks())
                .stream()
                .flatMap(Collection::stream)
                .forEach(t -> todoList.addTask(t.getName(), TaskStatus.valueOf(t.getStatus())));
        return todoList;
    }
}
