package pl.thinkandcode.samples.todo.adapters.inbound.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class JweTokenManagerTest {
    // 2100-01-01T00:00:00 UTC
    private static final Instant EXPIRATION_TIME = Instant.ofEpochMilli(4102444800000L);
    private JweTokenManager manager;

    @BeforeEach
    void setUp() {
        var properties = new JweTokenProperties();
        properties.setSecret("8gtpDtBl6cwsDBDLoWu/0A==");

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        manager = new JweTokenManager(objectMapper, properties);
    }

    @Test
    void givenNullObjectMapper_whenCreatingInstance_shouldThrowException() {
        // given
        ObjectMapper objectMapper = null;

        // when
        var throwable = catchThrowable(() -> new JweTokenManager(objectMapper, new JweTokenProperties()));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ObjectMapper must not be null");
    }

    @Test
    void givenNullJweTokenProperties_whenCreatingInstance_shouldThrowException() {
        // given
        JweTokenProperties properties = null;

        // when
        var throwable = catchThrowable(() -> new JweTokenManager(new ObjectMapper(), properties));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void givenNullJweTokenSecret_whenCreatingInstance_shouldThrowException() {
        // given
        JweTokenProperties properties = new JweTokenProperties();
        properties.setSecret(null);

        // when
        var throwable = catchThrowable(() -> new JweTokenManager(new ObjectMapper(), properties));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("JWE token secret must not be null");
    }

    @Test
    void givenNullAuthenticatedUser_whenEncodingToken_shouldThrowException() {
        // given
        AuthenticatedUser user = null;

        // when
        var throwable = catchThrowable(() -> manager.encodeToken(user));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("user must not be null");
    }

    @Test
    void givenAuthenticatedUser_whenEncodingToken_shouldCreateJweTokenString() {
        // given
        var user = new AuthenticatedUser(
                UUID.fromString("ecdec9cd-6a51-47a1-af89-78e7426ded96"),
                "jkowalski",
                EXPIRATION_TIME
        );

        // when
        var token = manager.encodeToken(user);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    void givenNullJweToken_whenDecodingToken_shouldThrowException() {
        // given
        String token = null;

        // when
        var throwable = catchThrowable(() -> manager.decodeToken(token));

        // then
        assertThat(throwable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("token must not be null");
    }

    @Test
    void givenJweTokenString_whenDecodingToken_shouldCreateValidAuthenticatedUser() {
        // given
        var token = "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..q8bxP6L8OtUr1_El.IaoRZTGZwIF6bO_dV9ZNe4qFygVuOU8Nno9F6tsaHCLk3HHfN3Ig_-zsLoU09fy6C6IM1k7f0AHK4fjMNobUwjPw9q8YZftyefdQIttSPRxR8grpR-xTFxfZD3YcolUQC14ZYQ5sq-DBBw.eV7VNYHTOHtrXGmW9kXw1g";

        // when
        var authenticatedUser = manager.decodeToken(token);

        // then
        var expected = new AuthenticatedUser(
                UUID.fromString("ecdec9cd-6a51-47a1-af89-78e7426ded96"),
                "jkowalski",
                EXPIRATION_TIME
        );
        assertThat(authenticatedUser).isEqualTo(expected);
    }

    @Test
    void givenAuthenticatedUser_whenEncodingAndDecodingToken_shouldReturnTheSameAuthenticatedUser() {
        // given
        var initialUser = new AuthenticatedUser(
                UUID.fromString("ecdec9cd-6a51-47a1-af89-78e7426ded96"),
                "jkowalski",
                EXPIRATION_TIME
        );

        // when
        var token = manager.encodeToken(initialUser);
        var authenticatedUser = manager.decodeToken(token);

        // then
        assertThat(authenticatedUser).isEqualTo(initialUser);
    }
}