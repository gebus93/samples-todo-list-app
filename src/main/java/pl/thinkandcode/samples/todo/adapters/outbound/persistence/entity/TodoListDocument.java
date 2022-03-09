package pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "todoLists")
public class TodoListDocument {
    @Id
    private UUID id;
    private UUID ownerId;
    private String name;
    private List<Task> tasks;
    @Version
    private Long version;
    @CreatedDate
    private Instant creationTime;
    @LastModifiedDate
    private Instant lastModificationTime;
}
