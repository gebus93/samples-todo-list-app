package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.thinkandcode.samples.todo.domain.exceptions.TasksLimitExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList {
    private static final int MAX_NUMBER_OF_TASKS = 25;
    UUID id;
    ListName listName;
    List<Task> tasks;

    public static TodoList create(UUID id, String name) {
        var listId = Objects.requireNonNull(id, "List id must not be null");
        var listName = new ListName(name);
        return new TodoList(listId, listName, new ArrayList<>());
    }

    public void addTask(String taskName, TaskStatus status) {
        if (tasks.size() == MAX_NUMBER_OF_TASKS) {
            throw new TasksLimitExceededException(MAX_NUMBER_OF_TASKS);
        }
        this.tasks.add(Task.create(taskName, status));
    }
}
