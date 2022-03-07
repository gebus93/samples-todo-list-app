package pl.thinkandcode.samples.todo;

import pl.thinkandcode.samples.todo.domain.ListName;
import pl.thinkandcode.samples.todo.domain.TaskName;

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
}
