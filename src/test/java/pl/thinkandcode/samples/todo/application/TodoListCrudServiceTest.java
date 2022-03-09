package pl.thinkandcode.samples.todo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.thinkandcode.samples.todo.application.exceptions.TodoListDoesNotExistException;
import pl.thinkandcode.samples.todo.domain.TaskStatus;
import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;

@ExtendWith(MockitoExtension.class)
class TodoListCrudServiceTest {
    @Mock
    private TodoListOpsObserver observer;
    @Mock
    private TodoListIdGenerator idGenerator;
    @Mock
    private TodoListLimitVerificationStrategy limitVerificationStrategy;
    @Mock
    private TodoListRepository repository;
    private TodoListCrudService service;

    @BeforeEach
    void setUp() {
        service = new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository);
    }

    @Test
    void givenNullObserver_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListOpsObserver observer = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Observer must not be null");
    }

    @Test
    void givenNullTodoListIdGenerator_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListIdGenerator idGenerator = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Todo list id generator must not be null");
    }

    @Test
    void givenNullLimitVerificationStrategy_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListLimitVerificationStrategy verificationStrategy = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, verificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Limit verification strategy must not be null");
    }

    @Test
    void givenNullRepository_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListRepository repository = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Repository must not be null");
    }

    @Test
    void givenNullCommand_whenCreatingTodoList_shouldThrowException() throws Exception {
        // given
        CreateTodoListCommand cmd = null;

        // when
        var throwable = catchThrowable(() -> service.createTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Command must not be null");
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_shouldVerifyLimits() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(idGenerator.generateId()).thenReturn(listIdFixture());

        // when
        service.createTodoList(cmd);

        // then
        verify(limitVerificationStrategy, only()).isExceeded();
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_andLimitIsExceeded_shouldNotifyObserver() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(limitVerificationStrategy.isExceeded()).thenReturn(true);

        // when
        service.createTodoList(cmd);

        // then
        verify(observer, only()).notifyTodoListCreationFailedDueToTheExceededLimit(cmd);
        verifyNoInteractions(idGenerator);
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_shouldGenerateListId() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(limitVerificationStrategy.isExceeded()).thenReturn(false);
        when(idGenerator.generateId()).thenReturn(listIdFixture());

        // when
        service.createTodoList(cmd);

        // then
        verify(idGenerator, only()).generateId();
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_shouldNotifyObserver() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(limitVerificationStrategy.isExceeded()).thenReturn(false);
        when(idGenerator.generateId()).thenReturn(listIdFixture());

        // when
        service.createTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        verify(observer, only()).notifyTodoListCreated(expectedTodoList);
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_shouldSaveListInTheRepository() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(limitVerificationStrategy.isExceeded()).thenReturn(false);
        when(idGenerator.generateId()).thenReturn(listIdFixture());

        // when
        service.createTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        verify(repository, only()).save(expectedTodoList);
    }

    @Test
    void givenValidCommand_whenCreatingTodoList_shouldReturnCreatedTodoList() throws Exception {
        // given
        var cmd = createTodoListCommandFixture();
        when(limitVerificationStrategy.isExceeded()).thenReturn(false);
        when(idGenerator.generateId()).thenReturn(listIdFixture());

        // when
        var createdTodoList = service.createTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        assertThat(createdTodoList).isEqualTo(expectedTodoList);
    }

    @Test
    void givenNullCommand_whenUpdatingTodoList_shouldThrowException() {
        // given
        UpdateTodoListCommand cmd = null;

        // when
        var throwable = catchThrowable(() -> service.updateTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Command must not be null");
    }

    @Test
    void givenCommandWithNullId_whenUpdatingTodoList_shouldThrowException() {
        // given
        var cmd = new UpdateTodoListCommand(null, listNameStringFixture(), List.of());

        // when
        var throwable = catchThrowable(() -> service.updateTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List id must not be null");
    }

    @Test
    void givenValidCommand_whenUpdatingTodoList_shouldVerifyListExistence() {
        // given
        var cmd = updateTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.updateTodoList(cmd);

        // then
        verify(repository).exists(listIdFixture());
    }

    @Test
    void givenValidCommand_whenUpdatingTodoList_andListDoesNotExist_shouldThrowException() {
        // given
        var cmd = updateTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(false);

        // when
        var throwable = catchThrowable(() -> service.updateTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(TodoListDoesNotExistException.class)
                .hasMessage("Could not find to do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a'");
    }

    @Test
    void givenValidCommand_whenUpdatingTodoList_andListDoesNotExist_shouldNotifyObserver() {
        // given
        var cmd = updateTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(false);

        // when
        var throwable = catchThrowable(() -> service.updateTodoList(cmd));

        // then
        verify(observer, only()).notifyUpdatedTodoListDoesNotExist(cmd);
    }

    @Test
    void givenValidCommand_whenUpdatingTodoList_shouldNotifyObserver() {
        // given
        var cmd = updateTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.updateTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        expectedTodoList.addTask(taskNameStringFixture(), TaskStatus.DONE);
        verify(observer, only()).notifyTodoListUpdated(expectedTodoList);
    }

    @Test
    void givenValidCommand_whenUpdatingTodoList_shouldSaveListInTheRepository() {
        // given
        var cmd = updateTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.updateTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        expectedTodoList.addTask(taskNameStringFixture(), TaskStatus.DONE);
        verify(repository).save(expectedTodoList);
    }

    @Test
    void givenValidCommandWithNullTasksList_whenUpdatingTodoList_shouldSaveListInTheRepositoryWithEmptyTasksList() {
        // given
        var cmd = new UpdateTodoListCommand(listIdFixture(), listNameStringFixture(), null);
        when(repository.exists(any())).thenReturn(true);

        // when
        service.updateTodoList(cmd);

        // then
        var expectedTodoList = TodoList.create(listIdFixture(), listNameStringFixture());
        verify(repository).save(expectedTodoList);
    }

    @Test
    void givenNullCommand_whenDeletingTodoList_shouldThrowException() {
        // given
        DeleteTodoListCommand cmd = null;

        // when
        var throwable = catchThrowable(() -> service.deleteTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Command must not be null");

    }

    @Test
    void givenCommandWithNullId_whenDeletingTodoList_shouldThrowException() {
        // given
        var cmd = new DeleteTodoListCommand(null);

        // when
        var throwable = catchThrowable(() -> service.deleteTodoList(cmd));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List id must not be null");
    }

    @Test
    void givenValidCommand_whenDeletingTodoList_shouldVerifyListExistence() {
        // given
        var cmd = deleteTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.deleteTodoList(cmd);

        // then
        verify(repository).exists(listIdFixture());
    }

    @Test
    void givenValidCommand_whenDeletingTodoList_andListDoesNotExist_shouldNotThrowException() {
        // given
        var cmd = deleteTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(false);

        // when
        var throwable = catchThrowable(() -> service.deleteTodoList(cmd));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void givenValidCommand_whenDeletingTodoList_andListDoesNotExist_shouldNotifyObserver() {
        // given
        var cmd = deleteTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(false);

        // when
        service.deleteTodoList(cmd);

        // then
        verify(observer, only()).notifyDeletedTodoListDoesNotExist(cmd);
    }

    @Test
    void givenValidCommand_whenDeletingTodoList_shouldNotifyObserver() {
        // given
        var cmd = deleteTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.deleteTodoList(cmd);

        // then
        verify(observer, only()).notifyTodoListDeleted(cmd.id());
    }

    @Test
    void givenValidCommand_whenDeletingTodoList_shouldSaveListInTheRepository() {
        // given
        var cmd = deleteTodoListCommandFixture();
        when(repository.exists(any())).thenReturn(true);

        // when
        service.deleteTodoList(cmd);

        // then
        verify(repository).delete(cmd.id());
    }

    @Test
    void givenNullQuery_whenGettingTodoList_shouldThrowException() {
        // given
        GetTodoListQuery query = null;

        // when
        var throwable = catchThrowable(() -> service.getTodoList(query));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Query must not be null");

    }

    @Test
    void givenQueryWithNullId_whenGettingTodoList_shouldThrowException() {
        // given
        GetTodoListQuery query = new GetTodoListQuery(null);

        // when
        var throwable = catchThrowable(() -> service.getTodoList(query));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List id must not be null");
    }

    @Test
    void givenValidQuery_whenGettingTodoList_andListDoesNotExist_shouldThrowException() {
        // given
        var query = getTodoListQueryFixture();
        when(repository.findTodoList(any())).thenReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.getTodoList(query));

        // then
        assertThat(throwable)
                .isInstanceOf(TodoListDoesNotExistException.class)
                .hasMessage("Could not find to do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a'");

    }

    @Test
    void givenValidQuery_whenGettingTodoList_shouldReturnValidTodoList() {
        // given
        var query = getTodoListQueryFixture();
        when(repository.findTodoList(any())).thenReturn(Optional.of(todoListFixture()));

        // when
        var todoList = service.getTodoList(query);

        // then
        assertThat(todoList).isEqualTo(todoListFixture());
    }

    @Test
    void whenGettingAllTodoLists_shouldReturnValidTodoLists() {
        // given
        when(repository.findAll()).thenReturn(List.of(todoListFixture()));

        // when
        var todoList = service.getAllTodoLists();

        // then
        assertThat(todoList).isEqualTo(List.of(todoListFixture()));
    }
}