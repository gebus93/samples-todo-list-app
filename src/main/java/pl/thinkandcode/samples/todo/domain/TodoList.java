package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList {
    UUID id;
    ListName listName;
    List<Task> tasks;

    public static TodoList create(UUID id, String name) {
        var listId = Objects.requireNonNull(id, "List id must not be null");
        var listName = new ListName(name);
        return new TodoList(listId, listName, new ArrayList<>());
    }

    public void addTask(String taskName, TaskStatus status) {
        this.tasks.add(Task.create(taskName, status));
    }
}
