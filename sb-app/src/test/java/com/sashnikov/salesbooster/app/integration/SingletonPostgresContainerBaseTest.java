package com.sashnikov.salesbooster.app.integration;

import com.sashnikov.salesbooster.app.integration.SingletonPostgresContainerBaseTest.Initializer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Ilya_Sashnikau
 */
@ContextConfiguration(initializers = Initializer.class)
abstract class SingletonPostgresContainerBaseTest {

    private static final String DB_NAME = "sbdb";
    private static final String USER_NAME = "sbuser";
    private static final String PASSWORD = "12345678";
    private static final int MAX_POOL_SIZE = 2;
    private static final PostgreSQLContainer DB_CONTAINER;

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:11.5-alpine")
                .withDatabaseName(DB_NAME)
                .withUsername(USER_NAME)
                .withPassword(PASSWORD);
    }

    @BeforeAll
    static void beforeAll() {
        DB_CONTAINER.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            String jdbcUrl = DB_CONTAINER.getJdbcUrl();
            TestPropertyValues.of(
                    String.format("spring.datasource.url: %s", jdbcUrl),
                    String.format("spring.datasource.username: %s", USER_NAME),
                    String.format("spring.datasource.password: %s", PASSWORD),
                    String.format("spring.datasource.password: %s", PASSWORD),
                    String.format("spring.datasource.hikari.maximum-pool-size: %d", MAX_POOL_SIZE)
            ).applyTo(applicationContext);
        }
    }
}
