package pl.thinkandcode.samples.todo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.thinkandcode.samples.todo.domain.UsageSummary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatisticsServiceTest {
    @Mock
    private TodoListRepository repository;
    @Mock
    private TodoListLimitProvider limitProvider;
    private UserStatisticsService service;

    @BeforeEach
    void setUp() {
        service = new UserStatisticsService(repository, limitProvider);
    }

    @Test
    void givenNullRepository_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListRepository repository = null;

        // when
        var throwable = catchThrowable(() -> new UserStatisticsService(repository, limitProvider));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Repository must not be null");
    }

    @Test
    void givenNullLimitProvider_whenCreatingInstance_shouldThrowException() throws Exception {
        // given
        TodoListLimitProvider limitProvider = null;

        // when
        var throwable = catchThrowable(() -> new UserStatisticsService(repository, limitProvider));

        // then
        assertThat(throwable).isInstanceOf(NullPointerException.class)
                .hasMessage("Limit provider must not be null");
    }

    @Test
    void whenGettingUsageSummary_shouldGetCurrentUsageFromRepository() throws Exception {
        // given
        when(repository.countAll()).thenReturn(3);
        when(limitProvider.getLimit()).thenReturn(5);

        // when
        service.getUsageSummary();

        // then
        verify(repository, only()).countAll();
    }

    @Test
    void whenGettingUsageSummary_shouldGetLimitFromLimitsProvider() throws Exception {
        // given
        when(repository.countAll()).thenReturn(3);
        when(limitProvider.getLimit()).thenReturn(5);

        // when
        service.getUsageSummary();

        // then
        verify(limitProvider, only()).getLimit();
    }

    @Test
    void whenGettingUsageSummary_shouldReturnSummary() throws Exception {
        // given
        when(repository.countAll()).thenReturn(3);
        when(limitProvider.getLimit()).thenReturn(5);

        // when
        UsageSummary summary = service.getUsageSummary();

        // then
        assertThat(summary).isNotNull();
        assertThat(summary.getCurrent()).isEqualTo(3);
        assertThat(summary.getLimit()).isEqualTo(5);
    }
}