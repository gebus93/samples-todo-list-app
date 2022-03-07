package pl.thinkandcode.samples.todo.domain;

import org.junit.jupiter.api.Test;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidListNameException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;

class TodoListTest {

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
        assertThat(todoList).isNotNull()
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
}