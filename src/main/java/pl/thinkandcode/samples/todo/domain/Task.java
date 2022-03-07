package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidTaskStatusException;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {
    private TaskName name;
    private TaskStatus status;

    public static Task create(String name, TaskStatus status) {
        if (status == null) {
            throw new InvalidTaskStatusException();
        }
        var taskName = new TaskName(name);
        return new Task(taskName, status);
    }
}
