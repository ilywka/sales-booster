package com.sashnikov.salesbooster.app.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Ilya_Sashnikau
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ActuatorTest extends SingletonPostgresContainerBaseTest {

    @Test
    void testHealthEndpoint(@Autowired TestRestTemplate testRestTemplate) {
        String response =
                testRestTemplate.getForObject("/actuator/health", String.class);

        assertThat(response, equalTo("{\"status\":\"UP\"}"));
    }
}
