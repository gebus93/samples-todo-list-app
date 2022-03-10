package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.UUID;

public record CreateTodoListResponse(UUID id, String name, List<TaskDto> tasks) {

    public static CreateTodoListResponse from(TodoList todoList) {
        var tasks = todoList.getTasks()
                            .stream()
                            .map(t -> new TaskDto(t.getName().getValue(), t.getStatus().name()))
                            .toList();
        return new CreateTodoListResponse(
                todoList.getId(),
                todoList.getListName().getValue(),
                tasks
        );
    }
}
