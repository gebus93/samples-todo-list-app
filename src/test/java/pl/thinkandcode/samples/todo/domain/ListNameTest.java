package pl.thinkandcode.samples.todo.domain;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import pl.thinkandcode.samples.todo.domain.exceptions.InvalidListNameException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ListNameTest {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public static Stream<Arguments> directionalFormatting() {
        return Stream.of(
                arguments(Named.of("\\u202a", "\u202a")),
                arguments(Named.of("\\u202b", "\u202b")),
                arguments(Named.of("\\u202c", "\u202c")),
                arguments(Named.of("\\u202d", "\u202d")),
                arguments(Named.of("\\u202e", "\u202e")));
    }

    public static Stream<Arguments> horizontalWhitespaces() {
        return Stream.of(
                arguments(Named.of("\\u0020", "\u0020")),
                arguments(Named.of("\\t", "\t")),
                arguments(Named.of("\\u00A0", "\u00A0")),
                arguments(Named.of("\\u1680", "\u1680")),
                arguments(Named.of("\\u180e", "\u180e")),
                arguments(Named.of("\\u2000", "\u2000")),
                arguments(Named.of("\\u2001", "\u2001")),
                arguments(Named.of("\\u2002", "\u2002")),
                arguments(Named.of("\\u2003", "\u2003")),
                arguments(Named.of("\\u2004", "\u2004")),
                arguments(Named.of("\\u2005", "\u2005")),
                arguments(Named.of("\\u2006", "\u2006")),
                arguments(Named.of("\\u2007", "\u2007")),
                arguments(Named.of("\\u2008", "\u2008")),
                arguments(Named.of("\\u2009", "\u2009")),
                arguments(Named.of("\\u200a", "\u200a")),
                arguments(Named.of("\\u202d", "\u202d")),
                arguments(Named.of("\\u202e", "\u202e")),
                arguments(Named.of("\\u202f", "\u202f")),
                arguments(Named.of("\\u205f", "\u205f")),
                arguments(Named.of("\\u3000", "\u3000")));
    }

    public static Stream<Arguments> verticalWhitespaces() {
        return Stream.of(
                arguments(Named.of("\\n", "\n")),
                arguments(Named.of("\\u000B", "\u000B")),
                arguments(Named.of("\\f", "\f")),
                arguments(Named.of("\\r", "\r")),
                arguments(Named.of("\\u0085", "\u0085")),
                arguments(Named.of("\\u2028", "\u2028")),
                arguments(Named.of("\\u2029", "\u2029")));
    }

    @Test
    void givenNullValue_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        String value = null;

        // when
        var throwable = catchThrowable(() -> new ListName(value));

        // then
        assertThat(throwable)
                .isInstanceOf(InvalidListNameException.class)
                .hasMessage("List name must not be null nor blank");
    }

    @ParameterizedTest(name = "[{index}] value=\"{0}\"")
    @EmptySource
    @MethodSource({"directionalFormatting", "horizontalWhitespaces", "verticalWhitespaces"})
    void givenEmptyValue_whenCreatingInstance_shouldThrowException(String whitespace) throws Exception {
        // given
        // repeat whitespace to make sure that it'll' fail because of a trimming mechanism.
        var value = whitespace.repeat(10);

        // when
        var throwable = catchThrowable(() -> new ListName(value));

        // then
        assertThat(throwable)
                .isInstanceOf(InvalidListNameException.class)
                .hasMessage("List name must not be null nor blank");
    }

    @Test
    void givenToShortValue_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        var value = "a".repeat(MIN_LENGTH - 1);

        // when
        var throwable = catchThrowable(() -> new ListName(value));

        // then
        assertThat(throwable)
                .isInstanceOf(InvalidListNameException.class)
                .hasMessage("List name must have at least " + MIN_LENGTH + " characters and no more than " + MAX_LENGTH + ".");
    }

    @Test
    void givenToLongValue_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        var value = "a".repeat(MAX_LENGTH + 1);

        // when
        var throwable = catchThrowable(() -> new ListName(value));

        // then
        assertThat(throwable)
                .isInstanceOf(InvalidListNameException.class)
                .hasMessage("List name must have at least " + MIN_LENGTH + " characters and no more than " + MAX_LENGTH + ".");
    }

    @Test
    void givenValueWithMinimumLength_whenCreatingInstance_shouldCreateInstanceCorrectly() throws Exception {
        // given
        var value = "a".repeat(MIN_LENGTH);

        // when
        var listName = new ListName(value);

        // then
        assertThat(listName.value()).isEqualTo(value);
    }

    @Test
    void givenValueWithMaximumLength_whenCreatingInstance_shouldCreateInstanceCorrectly() throws Exception {
        // given
        var value = "a".repeat(MAX_LENGTH);

        // when
        var listName = new ListName(value);

        // then
        assertThat(listName.value()).isEqualTo(value);

    }
}