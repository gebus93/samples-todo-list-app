package pl.thinkandcode.samples.todo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsageSummary {
    int current;
    int limit;

    public static UsageSummary create(int createdListsCount, int listsLimit) {
        if (createdListsCount < 0) {
            throw new IllegalArgumentException("Number of created list must not be negative");
        }
        if (listsLimit < 0) {
            throw new IllegalArgumentException("Limit of created list must not be negative");
        }
        return new UsageSummary(createdListsCount, listsLimit);
    }
}
