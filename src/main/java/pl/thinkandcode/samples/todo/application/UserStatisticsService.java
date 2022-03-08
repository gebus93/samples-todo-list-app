package pl.thinkandcode.samples.todo.application;

import pl.thinkandcode.samples.todo.domain.UsageSummary;

import java.util.Objects;

public class UserStatisticsService {
    private final TodoListRepository repository;
    private final TodoListLimitProvider limitProvider;

    public UserStatisticsService(TodoListRepository repository, TodoListLimitProvider limitProvider) {
        this.repository = Objects.requireNonNull(repository, "Repository must not be null");
        this.limitProvider = Objects.requireNonNull(limitProvider, "Limit provider must not be null");
    }

    public UsageSummary getUsageSummary() {
        var currentUsage = repository.countAll();
        var limit = limitProvider.getLimit();
        return UsageSummary.create(currentUsage, limit);
    }
}
