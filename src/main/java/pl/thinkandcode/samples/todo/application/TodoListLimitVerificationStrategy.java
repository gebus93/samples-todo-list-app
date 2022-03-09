package pl.thinkandcode.samples.todo.application;

/**
 * This interface simplifies replacing limits verification mechanism. It's a good example of the open-close principle in practice.
 * <p>
 * There are many options to implement it. For example:
 * <ul>
 *     <li><b>HardcodedTodoListLimitVerificationStrategy</b> - verifies user's limits comparing count of a created lists to the hardcoded value</li>
 *     <li><b>PropertiesBasedTodoListLimitVerificationStrategy</b> - verifies user's limits comparing count of a created lists to the value defined in a properties file</li>
 *     <li><b>ExternalSystemBasedTodoListLimitVerificationStrategy</b> - calls external system (e.g. subscription/limits system) to verify user's permission to perform the operation</li>
 * </ul>
 */
public interface TodoListLimitVerificationStrategy {
    boolean isExceeded();
}
