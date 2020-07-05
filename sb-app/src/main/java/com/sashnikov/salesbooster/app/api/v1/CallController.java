package com.sashnikov.salesbooster.app.api.v1;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ilya_Sashnikau
 */
@RestController
@RequestMapping("/api/v1/calls")
@RequiredArgsConstructor
public class CallController {

    private final SaveCallsUseCase saveCallsUseCase;

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCalls(@RequestBody List<Call> calls) {
        saveCallsUseCase.save(calls);
    }
}
