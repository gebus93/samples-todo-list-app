package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.TaskDto.TaskStatus;
import pl.thinkandcode.samples.todo.application.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/todo-lists")
@RequiredArgsConstructor
public class TodoListController {
    private final TodoListCrudService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateTodoListResponse createTodoList(@RequestBody CreateTodoListRequest request) {
        var todoList = service.createTodoList(new CreateTodoListCommand(request.name()));
        return CreateTodoListResponse.from(todoList);
    }

    @GetMapping("/{listId}")
    GetTodoListResponse getTodoList(@PathVariable("listId") UUID todoListId) {
        var todoList = service.getTodoList(new GetTodoListQuery(todoListId));
        return GetTodoListResponse.from(todoList);
    }

    @GetMapping
    GetAllTodoListsResponse getAllTodoLists() {
        var todoLists = service.getAllTodoLists();
        return GetAllTodoListsResponse.from(todoLists);
    }

    @PutMapping("/{listId}")
    void updateTodoList(@RequestBody UpdateTodoListRequest request, @PathVariable("listId") UUID todoListId) {
        var tasks = Optional.ofNullable(request.tasks())
                            .stream()
                            .flatMap(Collection::stream)
                            .map(t -> {
                                var status = Optional.ofNullable(t.status())
                                                     .map(TaskStatus::toDomainObject)
                                                     .orElse(null);
                                return new UpdateTodoListCommand.Task(t.name(), status);
                            })
                            .toList();

        service.updateTodoList(new UpdateTodoListCommand(todoListId, request.name(), tasks));
    }

    @DeleteMapping("/{listId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTodoList(@PathVariable("listId") UUID todoListId) {
        service.deleteTodoList(new DeleteTodoListCommand(todoListId));
    }

}
