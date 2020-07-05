package com.sashnikov.salesbooster.app.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.repository.ReadCallsPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * @author Ilya_Sashnikau
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SaveCallsIntegrationTest extends SingletonPostgresContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReadCallsPort readCallsPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldSaveCalls() throws Exception {
        List<Call> calls = testData();
        String data = objectMapper.writeValueAsString(calls);

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/calls")
                .content(data)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());

        List<Call> actualCalls = readCallsPort.getAll();

        assertThat(calls).usingElementComparatorIgnoringFields("id")
                .containsAll(actualCalls);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from calls_history");
    }

    private List<Call> testData() {
        List<Call> calls = new ArrayList<>();
        calls.add(new Call(null, "123", CallType.INCOMING, 123L));
        calls.add(new Call(null, "12345", CallType.OUTGOING, 1L));
        calls.add(new Call(null, "123", CallType.INCOMING, 2L));
        return calls;
    }
}
