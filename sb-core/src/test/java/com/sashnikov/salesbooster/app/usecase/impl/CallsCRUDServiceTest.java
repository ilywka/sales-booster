package com.sashnikov.salesbooster.app.usecase.impl;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.SaveCallsPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase.SaveCallsCommand;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase.UpdateOrderStateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Ilya_Sashnikau
 */
@ExtendWith(MockitoExtension.class)
class CallsCRUDServiceTest {


    @Mock private SaveCallsPort saveCallsPort;
    @Mock UpdateOrderStateFromCallsHistoryUseCase updateOrderStateFromCallsHistoryUseCase;
    @InjectMocks private CallsCRUDService callsCRUDService;

    @Test
    void shouldCreateNewCustomersAndSaveNumbers() {
        PhoneNumber number = new PhoneNumber("29", "111");
        LocalDateTime date = LocalDateTime.now();
        List<CallDTO> callDTOS = List.of(
                new CallDTO(number, CallType.INCOMING, date, 2L),
                new CallDTO(number, CallType.INCOMING, date, 1L)
        );


        SaveCallsCommand command = new SaveCallsCommand(callDTOS);
        callsCRUDService.save(command);

        Set<Call> expectedToSaveCalls = Set.of(
                new Call(number, CallType.INCOMING, date, 2L),
                new Call(number, CallType.INCOMING, date, 1L)
        );
        then(saveCallsPort).should(times(1)).save(expectedToSaveCalls);
        then(updateOrderStateFromCallsHistoryUseCase).should(times(1))
                .updateState(new UpdateOrderStateCommand(callDTOS));
    }

}