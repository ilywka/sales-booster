package com.sashnikov.salesbooster.app.api.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * @author Ilya_Sashnikau
 */
@WebMvcTest(CallController.class)
@MockBeans(value = {
        @MockBean(SaveCallsUseCase.class)
})
class CallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createCalls() throws Exception {
        MockHttpServletRequestBuilder request = post("/api/v1/calls")
                .content("[]")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isCreated());
    }
}