package pl.thinkandcode.samples.todo.application;

import java.util.UUID;

@FunctionalInterface
public interface TodoListIdGenerator {
    UUID generateId();
}
