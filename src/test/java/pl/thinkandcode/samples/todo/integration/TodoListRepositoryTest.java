package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.DefaultTodoListRepository;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.SpringDataTodoListRepository;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity.Task;
import pl.thinkandcode.samples.todo.adapters.outbound.persistence.entity.TodoListDocument;
import pl.thinkandcode.samples.todo.application.TodoListRepository;
import pl.thinkandcode.samples.todo.domain.ListName;
import pl.thinkandcode.samples.todo.domain.TaskName;
import pl.thinkandcode.samples.todo.domain.TaskStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;

class TodoListRepositoryTest extends AbstractIntegrationTest {
    private static final UUID USER_1 = UUID.fromString("67b21aae-bd1d-4a7f-be08-7973eae19089");
    private static final UUID USER_2 = UUID.fromString("27f52a6e-ea2c-43ce-b176-8893384a6590");
    @Autowired(required = false)
    private TodoListRepository todoListRepository;
    @Autowired
    private SpringDataTodoListRepository springDataTodoListRepository;

    @Test
    void shouldAutowireInstanceCorrectly() {
        assertThat(todoListRepository)
                .isNotNull()
                .isInstanceOf(DefaultTodoListRepository.class);
    }

    @Test
    void givenNewTodoList_whenSaving_shouldInsertNewDocument() {
        // given
        var todoList = todoListFixture();
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        repository.save(todoList);

        // then
        var allDocuments = mongoTemplate.findAll(TodoListDocument.class);
        assertThat(allDocuments).hasSize(1);
        assertThat(allDocuments.get(0)).satisfies(document -> {
            assertThat(document.getId()).isEqualTo(listIdFixture());
            assertThat(document.getOwnerId()).isEqualTo(USER_1);
            assertThat(document.getName()).isEqualTo(listNameStringFixture());
            assertThat(document.getCreationTime()).isNotNull();
            assertThat(document.getLastModificationTime()).isNotNull();
            assertThat(document.getVersion()).isEqualTo(0L);
            assertThat(document.getTasks()).hasSize(1)
                                           .containsExactly(new Task(taskNameStringFixture(), "DONE"));
        });
    }

    @Test
    void givenExistingTodoList_whenSaving_shouldUpdateExistingDocument() {
        // given
        var todoList = todoListFixture();
        springDataTodoListRepository.save(initialTodoListFixture());
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        repository.save(todoList);

        // then
        var allDocuments = mongoTemplate.findAll(TodoListDocument.class);
        assertThat(allDocuments).hasSize(1);
        assertThat(allDocuments.get(0)).satisfies(document -> {
            assertThat(document.getId()).isEqualTo(listIdFixture());
            assertThat(document.getOwnerId()).isEqualTo(USER_1);
            assertThat(document.getName()).isEqualTo(listNameStringFixture());
            assertThat(document.getCreationTime()).isNotNull();
            assertThat(document.getLastModificationTime()).isNotNull();
            assertThat(document.getVersion()).isEqualTo(1L);
            assertThat(document.getTasks()).hasSize(1)
                                           .containsExactly(new Task(taskNameStringFixture(), "DONE"));
        });
    }

    @Test
    void givenExistingTodoList_whenCheckingExistence_shouldReturnTrue() {
        // given
        springDataTodoListRepository.save(initialTodoListFixture());
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var exists = repository.exists(listIdFixture());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void givenNotExistingTodoListId_whenCheckingExistence_shouldReturnFalse() {
        // given
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var exists = repository.exists(listIdFixture());

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void givenOtherUserTodoListId_whenCheckingExistence_shouldReturnFalse() {
        // given
        springDataTodoListRepository.save(initialTodoListFixture());
        when(userDataProvider.getUserId()).thenReturn(USER_2);

        // when
        var exists = repository.exists(listIdFixture());

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void givenExistingTodoList_whenDeleting_shouldDeleteDocument() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(listIdFixture(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        repository.delete(listIdFixture());

        // then
        var allDocuments = mongoTemplate.findAll(TodoListDocument.class);
        assertThat(allDocuments)
                .hasSize(1)
                .allSatisfy(d -> assertThat(d.getId()).isEqualTo(otherListId));

    }

    @Test
    void givenNotExistingTodoListId_whenDeleting_shouldDoNothing() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        repository.delete(listIdFixture());

        // then
        var allDocuments = mongoTemplate.findAll(TodoListDocument.class);
        assertThat(allDocuments)
                .hasSize(1)
                .allSatisfy(d -> assertThat(d.getId()).isEqualTo(otherListId));

    }

    @Test
    void givenOtherUserTodoListId_whenDeleting_shouldDoNothing() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(listIdFixture(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        repository.delete(otherListId);

        // then
        var allDocuments = mongoTemplate.findAll(TodoListDocument.class);
        assertThat(allDocuments)
                .hasSize(2)
                .anySatisfy(d -> assertThat(d.getId()).isEqualTo(otherListId))
                .anySatisfy(d -> assertThat(d.getId()).isEqualTo(listIdFixture()));
    }

    @Test
    void givenNotExistingTodoListId_whenFindingTodoList_shouldReturnEmptyOptional() {
        // given
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoListOptional = repository.findTodoList(listIdFixture());

        // then
        assertThat(todoListOptional).isEmpty();
    }

    @Test
    void givenOtherUserTodoListId_whenFindingTodoList_shouldReturnEmptyOptional() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(listIdFixture(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoListOptional = repository.findTodoList(otherListId);

        // then
        assertThat(todoListOptional).isEmpty();
    }

    @Test
    void givenExistingTodoList_whenFindingTodoList_shouldReturnOptionalWithTodoList() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(listIdFixture(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoListOptional = repository.findTodoList(listIdFixture());

        // then
        assertThat(todoListOptional).hasValueSatisfying(
                todoList -> {
                    assertThat(todoList.getId()).isEqualTo(listIdFixture());
                    assertThat(todoList.getListName()).isEqualTo(new ListName("Initial list name"));
                    assertThat(todoList.getTasks())
                            .hasSize(1)
                            .allSatisfy(t -> {
                                assertThat(t.getName()).isEqualTo(new TaskName("initial task"));
                                assertThat(t.getStatus()).isEqualTo(TaskStatus.PENDING);
                            });
                }
        );
    }

    @Test
    void givenUserWithoutTodoLists_whenFindingAll_shouldReturnEmptyList() {
        // given
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoLists = repository.findAll();

        // then
        assertThat(todoLists).isEmpty();

    }

    @Test
    void givenUserWithTodoLists_whenFindingAll_shouldReturnAllTodoLists() {
        // given
        var otherListId = UUID.fromString("9c2a8984-6809-4eec-a6a2-8872b348dabc");
        springDataTodoListRepository.save(initialTodoListFixture(listIdFixture(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(otherListId, USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoLists = repository.findAll();

        // then
        assertThat(todoLists).hasSize(1);
        assertThat(todoLists.get(0)).satisfies(
                todoList -> {
                    assertThat(todoList.getId()).isEqualTo(listIdFixture());
                    assertThat(todoList.getListName()).isEqualTo(new ListName("Initial list name"));
                    assertThat(todoList.getTasks())
                            .hasSize(1)
                            .allSatisfy(t -> {
                                assertThat(t.getName()).isEqualTo(new TaskName("initial task"));
                                assertThat(t.getStatus()).isEqualTo(TaskStatus.PENDING);
                            });
                }
        );

    }

    @Test
    void givenUserWithoutTodoLists_whenCountingAll_shouldReturnZero() {
        // given
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoListsCount = repository.countAll();

        // then
        assertThat(todoListsCount).isZero();
    }

    @Test
    void givenUserWithTodoLists_whenCountingAll_shouldReturnNumberOfTodoLists() {
        // given
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_1));
        springDataTodoListRepository.save(initialTodoListFixture(UUID.randomUUID(), USER_2));
        when(userDataProvider.getUserId()).thenReturn(USER_1);

        // when
        var todoListsCount = repository.countAll();

        // then
        assertThat(todoListsCount).isEqualTo(2);
    }

    private TodoListDocument initialTodoListFixture(UUID listId, UUID userId) {
        var document = new TodoListDocument();
        document.setId(listId);
        document.setOwnerId(userId);
        document.setName("Initial list name");
        document.setTasks(List.of(new Task("initial task", "PENDING")));
        return document;
    }

    private TodoListDocument initialTodoListFixture() {
        return initialTodoListFixture(listIdFixture(), USER_1);
    }
}