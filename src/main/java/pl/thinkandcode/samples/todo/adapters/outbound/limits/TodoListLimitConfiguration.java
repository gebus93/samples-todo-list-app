package pl.thinkandcode.samples.todo.adapters.outbound.limits;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TodoListLimitProperties.class)
public class TodoListLimitConfiguration {
}
