package pl.thinkandcode.samples.todo.contracts;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.thinkandcode.samples.todo.annotations.ContractTest;
import pl.thinkandcode.samples.todo.application.TodoListCrudService;
import pl.thinkandcode.samples.todo.application.UserStatisticsService;
import pl.thinkandcode.samples.todo.domain.TodoList;
import pl.thinkandcode.samples.todo.testcontainers.MongoDbContainerHolder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;

@ContractTest
@SpringBootTest(webEnvironment = MOCK)
public class AbstractContractTest {
    @Autowired
    private WebApplicationContext ctx;
    @MockBean
    private TodoListCrudService todoListCrudService;
    @MockBean
    private UserStatisticsService userStatisticsService;

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MongoDbContainerHolder.getInstance().getReplicaSetUrl());
    }

    @BeforeEach
    public void setup() {
        var mockMvcBuilder = MockMvcBuilders
                .webAppContextSetup(ctx)
                .apply(SecurityMockMvcConfigurers.springSecurity());
        RestAssuredMockMvc.standaloneSetup(mockMvcBuilder);

        when(todoListCrudService.createTodoList(any())).thenReturn(TodoList.create(listIdFixture(), listNameStringFixture()));
        when(todoListCrudService.getTodoList(any())).thenReturn(todoListFixture());
        when(todoListCrudService.getAllTodoLists()).thenReturn(List.of(todoListFixture()));
        when(userStatisticsService.getUsageSummary()).thenReturn(usageSummaryFixture());
    }
}