package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Objects;
import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList {
    UUID id;
    ListName listName;

    public static TodoList create(UUID id, String name) {
        var listId = Objects.requireNonNull(id, "List id must not be null");
        var listName = new ListName(name);
        return new TodoList(listId, listName);
    }

}
