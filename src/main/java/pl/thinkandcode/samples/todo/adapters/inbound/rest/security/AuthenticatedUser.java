package pl.thinkandcode.samples.todo.adapters.inbound.rest.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AuthenticatedUser {
    UUID id;
    String username;
    Instant expirationTime;
}
