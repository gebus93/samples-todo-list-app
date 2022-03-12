package pl.thinkandcode.samples.todo.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.security.AuthenticatedUser;
import pl.thinkandcode.samples.todo.adapters.inbound.rest.security.JweTokenManager;
import pl.thinkandcode.samples.todo.annotations.IntegrationTest;
import pl.thinkandcode.samples.todo.testcontainers.MongoDbContainerHolder;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@IntegrationTest
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebSecurityTest {
    // 2100-01-01T00:00:00 UTC
    private static final Instant EXPIRATION_TIME = Instant.ofEpochMilli(4102444800000L);
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected JweTokenManager tokenManager;

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MongoDbContainerHolder.getInstance().getReplicaSetUrl());
    }

    @Test
    void givenUnauthenticatedRequest_whenCallingSecuredEndpoint_shouldReturn401() {
        // given
        var requestEntity = new RequestEntity<Void>(HttpMethod.GET, URI.create("/test-endpoint"));

        // when
        var responseEntity = restTemplate.exchange(requestEntity, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void givenAuthenticatedRequest_whenCallingSecuredEndpoint_shouldReturnValidResponse() {
        // given
        var token = generateUserToken();
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        var requestEntity = new HttpEntity<Void>(headers);

        // when
        var responseEntity = restTemplate.exchange("/test-endpoint", HttpMethod.GET, requestEntity, String.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(
                "Logged as: id=ecdec9cd-6a51-47a1-af89-78e7426ded96, username=jkowalski, expirationTime=2100-01-01T00:00:00Z");
    }

    private String generateUserToken() {
        var user = new AuthenticatedUser(
                UUID.fromString("ecdec9cd-6a51-47a1-af89-78e7426ded96"),
                "jkowalski",
                EXPIRATION_TIME
        );
        return tokenManager.encodeToken(user);
    }

    @TestConfiguration
    @RestController
    public static class EndpointsConf {
        @GetMapping("/test-endpoint")
        public String test() {
            var user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "Logged as: id=%s, username=%s, expirationTime=%s".formatted(user.getId(), user.getUsername(), user.getExpirationTime());
        }
    }
}
