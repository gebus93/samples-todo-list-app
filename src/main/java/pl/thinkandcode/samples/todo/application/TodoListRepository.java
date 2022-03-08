package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.TodoList;

public interface TodoListRepository {
    void save(TodoList todoList);
}
