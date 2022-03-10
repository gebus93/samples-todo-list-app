package pl.thinkandcode.samples.todo.component;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.*;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.GetAllTodoListsResponse.SingleTodoList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.thinkandcode.samples.todo.adapters.inbound.rest.TaskDto.TaskStatus.PENDING;

public class HappyPathTodoListComponentTest extends AbstractComponentTest {
    @Test
    void givenValidRequests_whenFollowingHappyPath_shouldNotFail() {
        var restTemplate = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);

        // The number of created lists should be 0
        var statisticsResponse = restTemplate.getForEntity("/user-stats", GetUserStatisticsResponse.class);
        assertThat(statisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statisticsResponse.getBody()).satisfies(s -> {
            var todoListStatsDto = s.todoLists();
            assertThat(todoListStatsDto.created()).isZero();
            assertThat(todoListStatsDto.limit()).isEqualTo(5);
        });

        // Creating new list
        var createTodoListRequest = new CreateTodoListRequest("List-1");
        var createTodoListResponseEntity = restTemplate.postForEntity("/todo-lists", createTodoListRequest, CreateTodoListResponse.class);
        assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var createdTodoList = createTodoListResponseEntity.getBody();
        assertThat(createdTodoList).satisfies(list -> {
            assertThat(list.id()).isNotNull();
            assertThat(list.name()).isEqualTo("List-1");
            assertThat(list.tasks()).isEmpty();
        });
        var todoListId = createdTodoList.id();

        // Updating created list
        var tasks = List.of(new TaskDto("Task to do 1", PENDING));
        var updateTodoListRequest = new UpdateTodoListRequest("List-1 (Updated)", tasks);
        var updateTodoListResponseEntity = restTemplate.exchange(
                "/todo-lists/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateTodoListRequest),
                String.class,
                todoListId);
        assertThat(updateTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateTodoListResponseEntity.getBody()).isBlank();

        // Getting updated list
        var getTodoListResponseEntity = restTemplate.getForEntity("/todo-lists/{id}", GetTodoListResponse.class, todoListId);
        assertThat(getTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var updatedTodoList = getTodoListResponseEntity.getBody();
        assertThat(updatedTodoList).satisfies(list -> {
            assertThat(list.id()).isEqualTo(todoListId);
            assertThat(list.name()).isEqualTo("List-1 (Updated)");
            assertThat(list.tasks()).hasSize(1);
            assertThat(list.tasks()).contains(new TaskDto("Task to do 1", PENDING));
        });

        // The number of created lists should be 1
        statisticsResponse = restTemplate.getForEntity("/user-stats", GetUserStatisticsResponse.class);
        assertThat(statisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statisticsResponse.getBody()).satisfies(s -> {
            var todoListStatsDto = s.todoLists();
            assertThat(todoListStatsDto.created()).isOne();
            assertThat(todoListStatsDto.limit()).isEqualTo(5);
        });

        // Creating another 4 lists
        for (int i = 2; i <= 5; i++) {
            var listName = "List-" + i;
            createTodoListRequest = new CreateTodoListRequest(listName);
            createTodoListResponseEntity = restTemplate.postForEntity("/todo-lists", createTodoListRequest, CreateTodoListResponse.class);
            assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            createdTodoList = createTodoListResponseEntity.getBody();
            assertThat(createdTodoList).satisfies(list -> {
                assertThat(list.id()).isNotNull();
                assertThat(list.name()).isEqualTo(listName);
                assertThat(list.tasks()).isEmpty();
            });
        }

        // Getting all created lists
        var getAllTodoListResponseEntity = restTemplate.getForEntity("/todo-lists", GetAllTodoListsResponse.class);
        assertThat(getAllTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var allTodoList = getAllTodoListResponseEntity.getBody();
        assertThat(allTodoList.todoLists())
                .hasSize(5)
                .anySatisfy(list -> {
                    assertThat(list.id()).isEqualTo(todoListId);
                    assertThat(list.name()).isEqualTo("List-1 (Updated)");
                    assertThat(list.tasks()).hasSize(1);
                    assertThat(list.tasks()).contains(new TaskDto("Task to do 1", PENDING));
                })
                .anySatisfy(list -> {
                    assertThat(list.id()).isNotNull();
                    assertThat(list.name()).isEqualTo("List-2");
                    assertThat(list.tasks()).isEmpty();
                })
                .anySatisfy(list -> {
                    assertThat(list.id()).isNotNull();
                    assertThat(list.name()).isEqualTo("List-3");
                    assertThat(list.tasks()).isEmpty();
                })
                .anySatisfy(list -> {
                    assertThat(list.id()).isNotNull();
                    assertThat(list.name()).isEqualTo("List-4");
                    assertThat(list.tasks()).isEmpty();
                })
                .anySatisfy(list -> {
                    assertThat(list.id()).isNotNull();
                    assertThat(list.name()).isEqualTo("List-5");
                    assertThat(list.tasks()).isEmpty();
                });
        var listIds = allTodoList.todoLists()
                                 .stream()
                                 .map(SingleTodoList::id)
                                 .toList();

        // The number of created lists should be 5
        statisticsResponse = restTemplate.getForEntity("/user-stats", GetUserStatisticsResponse.class);
        assertThat(statisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statisticsResponse.getBody()).satisfies(s -> {
            var todoListStatsDto = s.todoLists();
            assertThat(todoListStatsDto.created()).isEqualTo(5);
            assertThat(todoListStatsDto.limit()).isEqualTo(5);
        });

        // Deleting all created lists
        for (var listId : listIds) {
            var deleteListResponseEntity = restTemplate.exchange("/todo-lists/{id}", HttpMethod.DELETE, null, String.class, listId);
            assertThat(deleteListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(deleteListResponseEntity.getBody()).isNull();
        }

        // The number of created lists should be 0
        statisticsResponse = restTemplate.getForEntity("/user-stats", GetUserStatisticsResponse.class);
        assertThat(statisticsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statisticsResponse.getBody()).satisfies(s -> {
            var todoListStatsDto = s.todoLists();
            assertThat(todoListStatsDto.created()).isEqualTo(0);
            assertThat(todoListStatsDto.limit()).isEqualTo(5);
        });

    }
}
