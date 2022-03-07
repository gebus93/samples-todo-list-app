package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Objects;
import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoList {
    UUID id;
    ListName listName;

    public static TodoList create(UUID id, String name) {
        var listId = Objects.requireNonNull(id, "List id must not be null");
        var listName = new ListName(name);
        return new TodoList(listId, listName);
    }

}
