package pl.thinkandcode.samples.todo.adapters.outbound.limits;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("todo-lists")
public record TodoListLimitProperties(int limit) {
}
