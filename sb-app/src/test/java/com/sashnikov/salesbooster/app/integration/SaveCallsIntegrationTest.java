package com.sashnikov.salesbooster.app.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sashnikov.salesbooster.app.api.v1.model.CallApiDTO;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.query.GetCallsQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;

/**
 * @author Ilya_Sashnikau
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class SaveCallsIntegrationTest extends SingletonPostgresContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetCallsQuery getCallsQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LocalDateTime testDate = LocalDateTime.now().withNano(0);

    @Test
    void shouldSaveCalls(@Autowired TestRestTemplate testRestTemplate) throws Exception {
        List<CallApiDTO> testCalls = testData();
        String data = objectMapper.writeValueAsString(testCalls);

        URI uri = new URI(testRestTemplate.getRootUri().concat("/api/v1/calls"));
        RequestEntity<String> requestEntity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(data);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(uri, requestEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        Set<Call> actualCalls = getCallsQuery.getAll()
                .stream()
                .peek(call -> call.setId(null)).collect(Collectors.toSet());

        Set<Call> expectedCalls = testCalls.stream()
                .map(callDTO ->
                        new Call(
                                new PhoneNumber(callDTO.getNumber()),
                                callDTO.getCallType(),
                                callDTO.getDate(),
                                callDTO.getDurationSeconds()
                        ))
                .collect(Collectors.toSet());

        assertThat(actualCalls, containsInAnyOrder(expectedCalls.toArray()));
        int orderStateRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_state");
        assertThat(orderStateRows, equalTo(2));
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "calls_history", "order_state");
    }

    private List<CallApiDTO> testData() {
        List<CallApiDTO> calls = new ArrayList<>();
        calls.add(new CallApiDTO("+375291111111", CallType.INCOMING, testDate, 123L));
        calls.add(new CallApiDTO("+375291111111", CallType.OUTGOING, testDate, 1L));
        calls.add(new CallApiDTO("+375442222222", CallType.INCOMING, testDate, 2L));
        return calls;
    }
}
