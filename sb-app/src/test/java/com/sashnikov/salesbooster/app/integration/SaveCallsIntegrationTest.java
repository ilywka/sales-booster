package com.sashnikov.salesbooster.app.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.GetCallsPort;
import com.sashnikov.salesbooster.app.port.GetCustomerPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase.CallDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * @author Ilya_Sashnikau
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class SaveCallsIntegrationTest extends SingletonPostgresContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetCallsPort getCallsPort;

    @Autowired
    private GetCustomerPort getCustomerPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LocalDateTime testDate = LocalDateTime.now().withNano(0);

    @Test
    void shouldSaveCalls(@Autowired TestRestTemplate testRestTemplate) throws Exception {
        List<CallDTO> calls = testData();
        String data = objectMapper.writeValueAsString(calls);

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/calls")
                .content(data)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        URI uri = new URI(testRestTemplate.getRootUri().concat("/api/v1/calls"));
        RequestEntity<String> requestEntity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(data);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(uri, requestEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        Set<Call> actualCalls = getCallsPort.getAll()
                .stream()
                .peek(call -> call.setId(null)).collect(Collectors.toSet());

        Map<PhoneNumber, Customer> numberCustomerMap =
                getCustomerPort.getByNumbers(
                        calls.stream().map(CallDTO::getNumber).collect(Collectors.toSet())
                );

        Set<Call> expectedCalls = calls.stream().map(callDTO ->
                new Call(
                        new Customer(numberCustomerMap.get(callDTO.getNumber()).getId()),
                        callDTO.getCallType(),
                        callDTO.getDate(),
                        callDTO.getDurationSeconds()
                ))
                .collect(Collectors.toSet());

        assertThat(actualCalls, containsInAnyOrder(expectedCalls.toArray()));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from calls_history");
        jdbcTemplate.update("delete from customer");
    }

    private List<CallDTO> testData() {
        List<CallDTO> calls = new ArrayList<>();
        calls.add(new CallDTO(new PhoneNumber("29", "123"), CallType.INCOMING, testDate, 123L));
        calls.add(new CallDTO(new PhoneNumber("29", "123"), CallType.OUTGOING, testDate, 1L));
        calls.add(new CallDTO(new PhoneNumber("44", "111"), CallType.INCOMING, testDate, 2L));
        return calls;
    }
}
