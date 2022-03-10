package pl.thinkandcode.samples.todo.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.GetUserStatisticsResponse.TodoListStatsDto;
import pl.thinkandcode.samples.todo.application.UserStatisticsService;

@RestController
@RequestMapping("/user-stats")
@RequiredArgsConstructor
public class UserStatisticsController {
    private final UserStatisticsService service;

    @GetMapping
    GetUserStatisticsResponse getUserStatistics() {
        var usageSummary = service.getUsageSummary();
        return new GetUserStatisticsResponse(new TodoListStatsDto(usageSummary.getCurrent(), usageSummary.getLimit()));
    }

}
