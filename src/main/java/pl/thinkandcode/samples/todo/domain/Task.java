package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {
    private TaskName name;
    private TaskStatus status;

    public static Task create(String name, TaskStatus status) {
        var taskName = new TaskName(name);
        var taskStatus = Objects.requireNonNull(status, "Task status must not be null");
        return new Task(taskName, taskStatus);
    }
}
