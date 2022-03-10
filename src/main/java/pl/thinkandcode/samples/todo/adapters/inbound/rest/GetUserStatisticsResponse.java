package pl.thinkandcode.samples.todo.adapters.inbound.rest;

public record GetUserStatisticsResponse(TodoListStatsDto todoLists) {
    public record TodoListStatsDto(int created, int limit) {
    }
}
