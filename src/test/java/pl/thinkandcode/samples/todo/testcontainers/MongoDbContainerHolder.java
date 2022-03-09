package pl.thinkandcode.samples.todo.testcontainers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;

public class MongoDbContainerHolder {
    private static final Logger log = LoggerFactory.getLogger(MongoDbContainerHolder.class);
    public static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.4.9";
    private static final MongoDBContainer INSTANCE;

    static {
        INSTANCE = new MongoDBContainer(DEFAULT_IMAGE_AND_TAG);
        if (!INSTANCE.isRunning()) {
            log.info("Starting mongodb container: " + INSTANCE.getDockerImageName());
            INSTANCE.start();
        }
    }

    public static MongoDBContainer getInstance() {
        return INSTANCE;
    }

}
