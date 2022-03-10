package pl.thinkandcode.samples.todo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.BsonDocument;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.web.client.RestTemplate;
import pl.thinkandcode.samples.todo.annotations.ComponentTest;

import java.io.UncheckedIOException;

@ComponentTest
public abstract class AbstractComponentTest {

    @BeforeEach
    void setUp() {
        cleanDatabase("mongodb://localhost:12017/todoList");
    }

    protected RestTemplate getAuthenticatedRestTemplate(AuthenticatedUser authenticatedUser) {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:18080")
                .defaultHeader("Authorization", "Bearer " + authenticatedUser.getToken())
                .build();
    }

    protected RestTemplate getUnauthenticatedRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:18080")
                .build();
    }

    private void cleanDatabase(String connectionString) {
        var mongoDbFactory = new SimpleMongoClientDatabaseFactory(connectionString);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        for (String name : mongoTemplate.getCollectionNames()) {
            var collection = mongoTemplate.getCollection(name);
            collection.deleteMany(BsonDocument.parse("{}"));
        }
    }

    protected  <T> T convertJsonToType(String json, Class<T> errorResponseClass) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(json, errorResponseClass);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
