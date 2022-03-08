package pl.thinkandcode.samples.todo.application;

/**
 * This interface simplifies replacing limits source. It's a good example of the open-close principle in practice.
 *
 * There are many options to implement it. For example:
 * <ul>
 *     <li><b>HardcodedTodoListLimitProvider</b> - always returns the same hardcoded value</li>
 *     <li><b>PropertiesBasedTodoListLimitProvider</b> - returns the value defined in a properties file</li>
 *     <li><b>ExternalSystemBasedTodoListLimitProvider</b> - calls external system (e.g. subscription/limits system) to get user's limit</li>
 * </ul>
 */
public interface TodoListLimitProvider {
    int getLimit();
}
