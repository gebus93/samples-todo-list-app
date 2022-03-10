package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import java.util.List;

public record UpdateTodoListRequest(String name, List<TaskDto> tasks) {

}
