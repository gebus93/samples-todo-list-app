package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.TaskStatus;

import java.util.List;
import java.util.UUID;

public record UpdateTodoListCommand(UUID id, String name, List<Task> tasks) {

    public record Task(String name, TaskStatus status) {
    }

}
