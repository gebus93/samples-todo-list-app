package pl.thinkandcode.samples.todo.adapters.outbound.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class AuditingConfig {
}