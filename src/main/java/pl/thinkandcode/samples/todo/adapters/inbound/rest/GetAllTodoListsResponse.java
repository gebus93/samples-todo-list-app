package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record GetAllTodoListsResponse(List<SingleTodoList> todoLists) {

    public static GetAllTodoListsResponse from(Collection<TodoList> todoLists) {
        var lists = todoLists.stream()
                             .map(SingleTodoList::from)
                             .toList();
        return new GetAllTodoListsResponse(lists);
    }

    public record SingleTodoList(UUID id, String name, List<TaskDto> tasks) {
        public static SingleTodoList from(TodoList todoList) {
            var tasks = todoList.getTasks()
                                .stream()
                                .map(t -> new TaskDto(t.getName().getValue(), t.getStatus().name()))
                                .toList();
            return new SingleTodoList(
                    todoList.getId(),
                    todoList.getListName().getValue(),
                    tasks
            );
        }
    }

}
