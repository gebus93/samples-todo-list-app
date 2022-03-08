package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoListRepository {
    void save(TodoList todoList);

    boolean exists(UUID todoListId);

    void delete(UUID todoListId);

    Optional<TodoList> findTodoList(UUID todoListId);

    List<TodoList> findAll();

    int countAll();
}
