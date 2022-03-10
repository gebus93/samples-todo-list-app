package pl.thinkandcode.samples.todo.integration;

import org.bson.BsonDocument;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.LoggedUserDataProvider;
import pl.thinkandcode.samples.todo.annotations.IntegrationTest;
import pl.thinkandcode.samples.todo.application.TodoListRepository;
import pl.thinkandcode.samples.todo.testcontainers.MongoDbContainerHolder;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@IntegrationTest
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected MongoTemplate mongoTemplate;
    @SpyBean
    protected TodoListRepository repository;
    @MockBean // Mock implementation is good enough for integration tests. Real one should be verified in component tests
    protected LoggedUserDataProvider userDataProvider;

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MongoDbContainerHolder.getInstance().getReplicaSetUrl());
    }

    @BeforeEach
    void cleanDatabase() {
        mongoTemplate.getCollection("todoLists").deleteMany(BsonDocument.parse("{}"));
    }
}
