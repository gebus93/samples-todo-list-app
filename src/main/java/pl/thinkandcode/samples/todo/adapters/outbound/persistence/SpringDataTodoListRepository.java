package pl.thinkandcode.samples.todo.adapters.outbound.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity.TodoListDocument;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTodoListRepository extends MongoRepository<TodoListDocument, UUID> {
    int countAllByOwnerId(UUID ownerId);

    List<TodoListDocument> findAllByOwnerId(UUID ownerId);

    Optional<TodoListDocument> findByIdAndOwnerId(UUID todoListId, UUID ownerId);

    void deleteByIdAndOwnerId(UUID todoListId, UUID ownerId);

    boolean existsByIdAndOwnerId(UUID todoListId, UUID ownerId);
}
