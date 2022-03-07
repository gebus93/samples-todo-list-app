package pl.thinkandcode.samples.todo.domain;

import org.junit.jupiter.api.Test;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidTaskNameException;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidTaskStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static pl.thinkandcode.samples.todo.CommonFixtures.taskNameFixture;
import static pl.thinkandcode.samples.todo.CommonFixtures.taskNameStringFixture;

class TaskTest {
    @Test
    void givenNullTaskName_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        String taskName = null;

        // when
        var throwable = catchThrowable(() -> Task.create(taskName, TaskStatus.DONE));

        // then
        assertThat(throwable).isInstanceOf(InvalidTaskNameException.class)
                .hasMessage("Task name must not be null nor blank");
    }

    @Test
    void givenNullStatus_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TaskStatus status = null;

        // when
        var throwable = catchThrowable(() -> Task.create(taskNameStringFixture(), status));

        // then
        assertThat(throwable).isInstanceOf(InvalidTaskStatusException.class)
                .hasMessage("Task status must not be null");
    }

    @Test
    void givenValidNameAndValidStatus_whenCreatingInstance_shouldCreateInstanceCorrectly() throws Exception {
        // given
        var taskName = taskNameStringFixture();
        var status = TaskStatus.PENDING;

        // when
        var task = Task.create(taskName, status);

        // then
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(taskNameFixture());
        assertThat(task.getStatus()).isEqualTo(status);
    }

}