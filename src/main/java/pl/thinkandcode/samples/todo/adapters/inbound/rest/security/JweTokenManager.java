package pl.thinkandcode.samples.todo.adapters.inbound.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Component
public class JweTokenManager {
    private final EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
    private final ObjectMapper objectMapper;
    private final SecretKey key;

    public JweTokenManager(ObjectMapper objectMapper, JweTokenProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
        var secret = Objects.requireNonNull(properties.getSecret(), "JWE token secret must not be null");
        this.key = new SecretKeySpec(Base64.getDecoder().decode(secret), "AES");
    }

    public String encodeToken(AuthenticatedUser user) {
        Objects.requireNonNull(user, "user must not be null");
        try {
            JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, encryptionMethod);

            var authenticatedUserJson = objectMapper.writeValueAsString(user);
            Payload payload = new Payload(authenticatedUserJson);

            JWEObject jweObject = new JWEObject(header, payload);
            jweObject.encrypt(new DirectEncrypter(key));

            return jweObject.serialize();
        } catch (Exception e) {
            throw new TokenEncodingException(e);
        }
    }

    public AuthenticatedUser decodeToken(String jweToken) {
        Objects.requireNonNull(jweToken, "token must not be null");
        try {
            var jweObject = JWEObject.parse(jweToken);
            jweObject.decrypt(new DirectDecrypter(key));
            var payload = jweObject.getPayload().toString();
            return objectMapper.readValue(payload, AuthenticatedUser.class);
        } catch (Exception e) {
            throw new TokenDecodingException(e);
        }
    }

    public boolean validate(String token) {
        try {
            var authenticatedUser = decodeToken(token);
            return authenticatedUser.getExpirationTime().isAfter(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }

    public static class TokenEncodingException extends RuntimeException {
        public TokenEncodingException(Throwable cause) {
            super(cause);
        }
    }

    public static class TokenDecodingException extends RuntimeException {
        public TokenDecodingException(Throwable cause) {
            super(cause);
        }
    }
}
