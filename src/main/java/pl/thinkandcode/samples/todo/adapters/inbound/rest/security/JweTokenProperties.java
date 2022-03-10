package pl.thinkandcode.samples.todo.adapters.inbound.rest.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security.jwe-token")
public class JweTokenProperties {
    private String secret;
}
