package pl.thinkandcode.samples.todo;

import pl.thinkandcode.samples.todo.application.CreateTodoListCommand;
import pl.thinkandcode.samples.todo.application.DeleteTodoListCommand;
import pl.thinkandcode.samples.todo.application.GetTodoListQuery;
import pl.thinkandcode.samples.todo.application.UpdateTodoListCommand;
import pl.thinkandcode.samples.todo.domain.ListName;
import pl.thinkandcode.samples.todo.domain.TaskName;
import pl.thinkandcode.samples.todo.domain.TaskStatus;
import pl.thinkandcode.samples.todo.domain.TodoList;

import java.util.List;
import java.util.UUID;

public class CommonFixtures {
    private CommonFixtures() {
    }

    public static UUID listIdFixture() {
        return UUID.fromString("b2865319-d026-4ab1-b94a-7a67db79c66a");
    }

    public static ListName listNameFixture() {
        return new ListName(listNameStringFixture());
    }

    public static String listNameStringFixture() {
        return "TODO list";
    }

    public static TaskName taskNameFixture() {
        return new TaskName(taskNameStringFixture());
    }

    public static String taskNameStringFixture() {
        return "Task to do";
    }

    public static TodoList todoListFixture() {
        var todoList = TodoList.create(listIdFixture(), listNameStringFixture());
        todoList.addTask(taskNameStringFixture(), TaskStatus.DONE);
        return todoList;
    }

    public static CreateTodoListCommand createTodoListCommandFixture() {
        return new CreateTodoListCommand(listNameStringFixture());
    }

    public static UpdateTodoListCommand updateTodoListCommandFixture() {
        var task = new UpdateTodoListCommand.Task(taskNameStringFixture(), TaskStatus.DONE);
        return new UpdateTodoListCommand(listIdFixture(), listNameStringFixture(), List.of(task));
    }

    public static DeleteTodoListCommand deleteTodoListCommandFixture() {
        return new DeleteTodoListCommand(listIdFixture());
    }

    public static GetTodoListQuery getTodoListQueryFixture() {
        return new GetTodoListQuery(listIdFixture());
    }
}
