package pl.thinkandcode.samples.todo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.thinkandcode.samples.todo.domain.TodoList;

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
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Observer must not be null");
    }

    @Test
    void givenNullTodoListIdGenerator_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListIdGenerator idGenerator = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Todo list id generator must not be null");
    }

    @Test
    void givenNullLimitVerificationStrategy_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListLimitVerificationStrategy verificationStrategy = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, verificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Limit verification strategy must not be null");
    }

    @Test
    void givenNullRepository_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListRepository repository = null;

        // when
        var throwable = catchThrowable(() -> new TodoListCrudService(observer, limitVerificationStrategy, idGenerator, repository));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Repository must not be null");
    }

    @Test
    void givenNullCommand_whenCreatingTodoList_shouldThrowException() throws Exception {
        // given
        CreateTodoListCommand cmd = null;

        // when
        var throwable = catchThrowable(() -> service.createTodoList(cmd));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
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
}