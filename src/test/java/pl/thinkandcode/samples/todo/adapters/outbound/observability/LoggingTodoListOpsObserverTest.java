package pl.thinkandcode.samples.todo.adapters.outbound.observability;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;
import pl.thinkandcode.samples.todo.application.TodoListOpsObserver;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.thinkandcode.samples.todo.CommonFixtures.*;
import static pl.thinkandcode.samples.todo.adapters.outbound.observability.LoggingTodoListOpsObserverTest.TestArguments.args;

class LoggingTodoListOpsObserverTest {
    private ListAppender<ILoggingEvent> listAppender;
    private LoggingTodoListOpsObserver observer;

    public static Stream<Arguments> methodInvocations() {
        return Stream.of(
                args(
                        Named.of("notifyTodoListCreationFailedDueToTheExceededLimit",
                                 o -> o.notifyTodoListCreationFailedDueToTheExceededLimit(createTodoListCommandFixture())),
                        Level.INFO, "To do list creation failed. Limit has been exceeded."),
                args(
                        Named.of("notifyTodoListCreated", o -> o.notifyTodoListCreated(todoListFixture())),
                        Level.INFO, "To do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a' has been created."),
                args(
                        Named.of("notifyUpdatedTodoListDoesNotExist",
                                 o -> o.notifyUpdatedTodoListDoesNotExist(updateTodoListCommandFixture())),
                        Level.INFO, "To do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a' does not exist."),
                args(
                        Named.of("notifyTodoListUpdated", o -> o.notifyTodoListUpdated(todoListFixture())),
                        Level.INFO, "To do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a' has been updated."),
                args(
                        Named.of("notifyDeletedTodoListDoesNotExist",
                                 o -> o.notifyDeletedTodoListDoesNotExist(deleteTodoListCommandFixture())),
                        Level.INFO, "Could not delete to do list, because it does not exist."),
                args(
                        Named.of("notifyTodoListDeleted", o -> o.notifyTodoListDeleted(listIdFixture())),
                        Level.INFO, "To do list with id 'b2865319-d026-4ab1-b94a-7a67db79c66a' has been deleted.")
        );
    }

    @BeforeEach
    void setUp() {
        Logger observerLogger = (Logger) LoggerFactory.getLogger(LoggingTodoListOpsObserver.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        observerLogger.addAppender(listAppender);
        observer = new LoggingTodoListOpsObserver();
    }

    @ParameterizedTest
    @MethodSource("methodInvocations")
    void givenValidArguments_whenInvokingMethod_shouldLogMessage(Consumer<TodoListOpsObserver> methodInvocation,
                                                                 Level expectedLogLevel,
                                                                 String expectedMsg) {
        // when
        methodInvocation.accept(observer);

        // then
        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
                .containsExactly(Tuple.tuple(expectedMsg, expectedLogLevel));
    }

    public static class TestArguments implements Arguments {
        private final Object[] objects;

        private TestArguments(Object... objects) {
            this.objects = objects;
        }

        public static TestArguments args(Named<Consumer<TodoListOpsObserver>> methodInvocation,
                                         Level expectedLogLevel,
                                         String expectedMsg) {
            return new TestArguments(methodInvocation, expectedLogLevel, expectedMsg);
        }

        @Override
        public Object[] get() {
            return objects;
        }
    }
}