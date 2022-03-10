package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.UUID;

public record GetTodoListResponse(UUID id, String name, List<TaskDto> tasks) {

    public static GetTodoListResponse from(TodoList todoList) {
        var tasks = todoList.getTasks()
                            .stream()
                            .map(TaskDto::from)
                            .toList();
        return new GetTodoListResponse(
                todoList.getId(),
                todoList.getListName().getValue(),
                tasks
        );
    }
}
