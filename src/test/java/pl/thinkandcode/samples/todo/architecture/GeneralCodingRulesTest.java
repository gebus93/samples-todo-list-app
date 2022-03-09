package pl.thinkandcode.samples.todo.architecture;

import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.tngtech.archunit.library.GeneralCodingRules.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class GeneralCodingRulesTest extends AbstractArchitectureTest {

    public static Stream<Arguments> rules() {
        return Stream.of(
                arguments(Named.of("no classes should use JodaTime", NO_CLASSES_SHOULD_USE_JODATIME)),
                arguments(Named.of("no classes should access standard streams", NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS)),
                arguments(Named.of("no classes should throw generic exceptions", NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS)),
                arguments(Named.of("no classes should use field injection", NO_CLASSES_SHOULD_USE_FIELD_INJECTION)),
                arguments(Named.of("no classes should use java.util.logging", NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING))
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("rules")
    void generalCodingRulesShouldBeRespected(ArchRule rule) throws Exception {
        rule.check(classes);
    }
}
