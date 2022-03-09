package pl.thinkandcode.samples.todo.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pl.thinkandcode.samples.todo.annotations.IntegrationTest;
import pl.thinkandcode.samples.todo.application.TodoListRepository;

@SpringBootTest
@IntegrationTest
public class AbstractIntegrationTest {
    @SpyBean
    protected TodoListRepository repository;
}
