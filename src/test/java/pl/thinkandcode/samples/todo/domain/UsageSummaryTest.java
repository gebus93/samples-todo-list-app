package pl.thinkandcode.samples.todo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UsageSummaryTest {
    @Test
    void givenNegativeCurrentUsage_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        int currentUsage = -1;

        // when
        var throwable = catchThrowable(() -> UsageSummary.create(currentUsage, 5));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number of created list must not be negative");
    }
    @Test
    void givenNegativeLimit_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        int limit = -1;

        // when
        var throwable = catchThrowable(() -> UsageSummary.create(5, limit));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Limit of created list must not be negative");
    }

    @Test
    void givenValidArguments_whenCreatingInstance_shouldCreatedValidObject() throws Exception {
        // given
        int currentUsage = 0;
        int limit = 23;

        // when
        var usageSummary = UsageSummary.create(currentUsage, limit);

        // then
        assertThat(usageSummary).isNotNull();
        assertThat(usageSummary.getCurrent()).isEqualTo(currentUsage);
        assertThat(usageSummary.getLimit()).isEqualTo(limit);
    }
}