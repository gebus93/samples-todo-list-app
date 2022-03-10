package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import pl.thinkandcode.samples.todo.domain.Task;

public record TaskDto(String name, TaskStatus status) {

    public static TaskDto from(Task task) {
        return new TaskDto(
                task.getName().getValue(),
                TaskStatus.fromDomainObject(task.getStatus())
        );
    }

    public enum TaskStatus {
        PENDING, DONE;

        public static TaskStatus fromDomainObject(pl.thinkandcode.samples.todo.domain.TaskStatus status) {
            return status == null ? null : TaskStatus.valueOf(status.name());
        }

        public pl.thinkandcode.samples.todo.domain.TaskStatus toDomainObject() {
            return pl.thinkandcode.samples.todo.domain.TaskStatus.valueOf(name());
        }
    }
}
