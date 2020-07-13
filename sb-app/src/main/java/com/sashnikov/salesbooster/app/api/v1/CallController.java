package com.sashnikov.salesbooster.app.api.v1;

import static com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase.SaveCallsCommand;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.sashnikov.salesbooster.app.api.v1.model.CallApiDTO;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
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

    private static final Function<CallApiDTO, CallDTO> apiCallToCommandCall =
            callApiDTO -> new CallDTO(
                    new PhoneNumber(callApiDTO.getNumber()),
                    callApiDTO.getCallType(),
                    callApiDTO.getDate(),
                    callApiDTO.getDurationSeconds()
            );

    private final SaveCallsUseCase saveCallsUseCase;

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCalls(@RequestBody List<CallApiDTO> calls) {
        List<CallDTO> callDTOS = calls.stream()
                .map(apiCallToCommandCall)
                .collect(Collectors.toList());

        saveCallsUseCase.save(new SaveCallsCommand(callDTOS));
    }
}
