package pl.thinkandcode.samples.todo.component;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.*;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.RestResponseEntityExceptionHandler.ErrorResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static pl.thinkandcode.samples.todo.adapters.inbound.rest.TaskDto.TaskStatus.PENDING;

public class FailurePathTodoListComponentTest extends AbstractComponentTest {

    @Test
    void givenUserWithMaxNumberOfTodoList_whenCreatingTodoList_shouldFail() {
        // given
        var restTemplate = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);
        for (int i = 0; i < 5; i++) {
            var listName = "List-" + i;
            var createTodoListRequest = new CreateTodoListRequest(listName);
            var createTodoListResponseEntity = restTemplate.postForEntity("/todo-lists", createTodoListRequest,
                                                                          CreateTodoListResponse.class);
            assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        // when
        var createTodoListRequest = new CreateTodoListRequest("List-6");
        var httpClientErrorException = catchThrowableOfType(
                () -> restTemplate.postForEntity("/todo-lists", createTodoListRequest, CreateTodoListResponse.class),
                HttpClientErrorException.class);

        // then
        assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ErrorResponse response = convertJsonToType(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.error()).isEqualTo("Forbidden");
        assertThat(response.status()).isEqualTo(403);
        assertThat(response.path()).isEqualTo("/todo-lists");
        assertThat(response.detail()).isEqualTo("Could not create todo list. Limit has been exceeded.");
    }

    @Test
    void givenTodoListIdOfSomebodyElse_whenUpdatingTodoList_shouldFail() {
        // given
        var restTemplateForUser1 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);
        var restTemplateForUser2 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_2);
        var listName = "List-1";
        var createTodoListRequest = new CreateTodoListRequest(listName);
        var createTodoListResponseEntity = restTemplateForUser1.postForEntity("/todo-lists", createTodoListRequest,
                                                                              CreateTodoListResponse.class);
        assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var todoListId = createTodoListResponseEntity.getBody().id();

        // when
        var tasks = List.of(new TaskDto("Task to do 1", PENDING));
        var updateTodoListRequest = new UpdateTodoListRequest("List-1 (Updated)", tasks);

        var httpClientErrorException = catchThrowableOfType(
                () -> restTemplateForUser2.exchange(
                        "/todo-lists/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(updateTodoListRequest),
                        String.class,
                        todoListId),
                HttpClientErrorException.class);

        // then
        assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse response = convertJsonToType(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.error()).isEqualTo("Not Found");
        assertThat(response.status()).isEqualTo(404);
        assertThat(response.path()).isEqualTo("/todo-lists/" + todoListId);
        assertThat(response.detail()).isEqualTo("Could not find to do list with id '%s'".formatted(todoListId));
    }

    @Test
    void givenTodoListIdOfSomebodyElse_whenGettingTodoList_shouldFail() {
        // given
        var restTemplateForUser1 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);
        var restTemplateForUser2 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_2);
        var listName = "List-1";
        var createTodoListRequest = new CreateTodoListRequest(listName);
        var createTodoListResponseEntity = restTemplateForUser1.postForEntity("/todo-lists", createTodoListRequest,
                                                                              CreateTodoListResponse.class);
        assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var todoListId = createTodoListResponseEntity.getBody().id();

        // when
        var httpClientErrorException = catchThrowableOfType(
                () -> restTemplateForUser2.exchange(
                        "/todo-lists/{id}",
                        HttpMethod.GET,
                        null,
                        String.class,
                        todoListId),
                HttpClientErrorException.class);

        // then
        assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse response = convertJsonToType(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.error()).isEqualTo("Not Found");
        assertThat(response.status()).isEqualTo(404);
        assertThat(response.path()).isEqualTo("/todo-lists/" + todoListId);
        assertThat(response.detail()).isEqualTo("Could not find to do list with id '%s'".formatted(todoListId));
    }

    @Test
    void givenTodoListIdOfSomebodyElse_whenDeletingTodoList_shouldDoNothing() {
        // given
        var restTemplateForUser1 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);
        var restTemplateForUser2 = getAuthenticatedRestTemplate(AuthenticatedUser.USER_2);
        var listName = "List-1";
        var createTodoListRequest = new CreateTodoListRequest(listName);
        var createTodoListResponseEntity = restTemplateForUser1.postForEntity("/todo-lists", createTodoListRequest,
                                                                              CreateTodoListResponse.class);
        assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var todoListId = createTodoListResponseEntity.getBody().id();

        // when
        var responseEntity = restTemplateForUser2.exchange(
                "/todo-lists/{id}",
                HttpMethod.DELETE,
                null,
                String.class,
                todoListId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();

        var getTodoListResponseEntity = restTemplateForUser1.getForEntity("/todo-lists/{id}", GetTodoListResponse.class, todoListId);
        assertThat(getTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var updatedTodoList = getTodoListResponseEntity.getBody();
        assertThat(updatedTodoList).satisfies(list -> {
            assertThat(list.id()).isEqualTo(todoListId);
            assertThat(list.name()).isEqualTo("List-1");
            assertThat(list.tasks()).isEmpty();
        });
    }

    @Test
    void givenInvalidRequest_whenCreatingTodoList_shouldFail() {
        // given
        var restTemplate = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);

        // when
        var createTodoListRequest = new CreateTodoListRequest(null);
        var httpClientErrorException = catchThrowableOfType(
                () -> restTemplate.postForEntity("/todo-lists", createTodoListRequest, CreateTodoListResponse.class),
                HttpClientErrorException.class);

        // then
        assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse response = convertJsonToType(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.path()).isEqualTo("/todo-lists");
        assertThat(response.detail()).isEqualTo("List name must not be null nor blank");
    }

    @Test
    void givenInvalidRequest_whenUpdatingTodoList_shouldFail() {
        // given
        var restTemplate = getAuthenticatedRestTemplate(AuthenticatedUser.USER_1);
        var listName = "List-1";
        var createTodoListRequest = new CreateTodoListRequest(listName);
        var createTodoListResponseEntity = restTemplate.postForEntity("/todo-lists", createTodoListRequest, CreateTodoListResponse.class);
        assertThat(createTodoListResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        var todoListId = createTodoListResponseEntity.getBody().id();

        // when
        var tasks = List.of(new TaskDto("1".repeat(151), PENDING));
        var updateTodoListRequest = new UpdateTodoListRequest("List-1 (Updated)", tasks);

        var httpClientErrorException = catchThrowableOfType(
                () -> restTemplate.exchange(
                        "/todo-lists/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(updateTodoListRequest),
                        String.class,
                        todoListId),
                HttpClientErrorException.class);

        // then
        assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse response = convertJsonToType(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.path()).isEqualTo("/todo-lists/" + todoListId);
        assertThat(response.detail()).isEqualTo("Task name must have at least 3 characters and no more than 150.");
    }
}
