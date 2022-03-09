package pl.thinkandcode.samples.todo.domain;

import org.junit.jupiter.api.Test;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidListNameException;
import pl.thinkandcode.samples.todo.domain.exceptions.TasksLimitExceededException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;

class TodoListTest {
    private static final int MAX_NUMBER_OF_TASKS = 25;

    @Test
    void givenNullListId_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        UUID listId = null;

        // when
        var throwable = catchThrowable(() -> TodoList.create(listId, listNameStringFixture()));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("List id must not be null");
    }

    @Test
    void givenNullListName_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        String listName = null;

        // when
        var throwable = catchThrowable(() -> TodoList.create(listIdFixture(), listName));

        // then
        assertThat(throwable).isInstanceOf(InvalidListNameException.class);
    }

    @Test
    void givenValidIdAndValidName_whenCreatingInstance_shouldCreateValidInstance() throws Exception {
        // given
        var listId = listIdFixture();
        var name = listNameStringFixture();

        // when
        var todoList = TodoList.create(listId, name);

        // then
        assertThat(todoList)
                .isNotNull()
                .satisfies(list -> {
                    assertThat(list.getId()).isEqualTo(listId);
                    assertThat(list.getListName()).isEqualTo(listNameFixture());
                });
    }

    @Test
    void givenValidArguments_whenAddingTask_shouldAddTaskToList() throws Exception {
        // given
        var todoList = TodoList.create(listIdFixture(), listNameStringFixture());

        // when
        todoList.addTask("task name", TaskStatus.PENDING);

        // then
        var expectedTask = Task.create("task name", TaskStatus.PENDING);
        assertThat(todoList.getTasks()).hasSize(1);
        assertThat(todoList.getTasks()).contains(expectedTask);
    }

    @Test
    void givenMaxNumberOfTasks_whenAddingTask_shouldThrowException() throws Exception {
        // given
        var todoList = TodoList.create(listIdFixture(), listNameStringFixture());
        for (int i = 0; i < MAX_NUMBER_OF_TASKS; i++) {
            todoList.addTask("task name", TaskStatus.PENDING);
        }

        // when
        var throwable = catchThrowable(() -> todoList.addTask("task name", TaskStatus.PENDING));

        // then
        assertThat(throwable)
                .isInstanceOf(TasksLimitExceededException.class)
                .hasMessage("Tasks limit has been exceeded. Each to do list can contain no more than " + MAX_NUMBER_OF_TASKS + " tasks.");
    }
}