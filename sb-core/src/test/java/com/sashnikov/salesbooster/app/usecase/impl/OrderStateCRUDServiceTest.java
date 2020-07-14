package com.sashnikov.salesbooster.app.usecase.impl;

import static com.sashnikov.salesbooster.app.usecase.impl.OrderStateCRUDService.INCOMING_CALL_DURATION_THRESHOLD;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.OrderState;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.UpdateOrderStatePort;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase.UpdateOrderStateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Ilya_Sashnikau
 */
@ExtendWith(MockitoExtension.class)
class OrderStateCRUDServiceTest {

    @Mock UpdateOrderStatePort updateOrderStatePort;
    @InjectMocks private OrderStateCRUDService orderStateCRUDService;


    @Test
    void doNothingOnEmptyInput() {

        orderStateCRUDService.updateState(new UpdateOrderStateCommand(Collections.emptyList()));

        BDDMockito.then(updateOrderStatePort).shouldHaveNoInteractions();
    }

    @Test
    void shouldUpdateStateOnSingleCallPerNumber() {
        LocalDateTime now = LocalDateTime.now();
        PhoneNumber newStateRejected = new PhoneNumber("29", "121");
        PhoneNumber newStateMissed = new PhoneNumber("29", "123");
        PhoneNumber newStateIncoming = new PhoneNumber("29", "124");
        PhoneNumber inProgressStateIncoming = new PhoneNumber("29", "125");
        PhoneNumber inProgressStateOutgoing = new PhoneNumber("29", "126");
        List<CallDTO> callDTOS = List.of(
                new CallDTO(newStateRejected, CallType.REJECTED, now, 0l),
                new CallDTO(newStateMissed, CallType.MISSED, now, 0l),
                new CallDTO(newStateIncoming, CallType.INCOMING, now, INCOMING_CALL_DURATION_THRESHOLD - 1),
                new CallDTO(inProgressStateIncoming, CallType.INCOMING, now, INCOMING_CALL_DURATION_THRESHOLD + 1),
                new CallDTO(inProgressStateOutgoing, CallType.OUTGOING, now, INCOMING_CALL_DURATION_THRESHOLD - 1)
        );
        UpdateOrderStateCommand command = new UpdateOrderStateCommand(callDTOS);

        orderStateCRUDService.updateState(command);

        Map<PhoneNumber, OrderState> expectedStates = Map.of(
                newStateRejected, OrderState.NEW,
                newStateMissed, OrderState.NEW,
                newStateIncoming, OrderState.NEW,
                inProgressStateIncoming, OrderState.IN_PROGRESS,
                inProgressStateOutgoing, OrderState.IN_PROGRESS
        );
        BDDMockito.then(updateOrderStatePort)
                .should(times(1))
                .update(expectedStates);
    }

    @Test
    void shouldDetermineStateFromManyCallsPerNumber() {
        LocalDateTime now = LocalDateTime.now();
        PhoneNumber newStateNumber = new PhoneNumber("29", "123");
        PhoneNumber inProgressStateNumber = new PhoneNumber("29", "124");
        PhoneNumber noStateNUmber = new PhoneNumber("29", "124");
        List<CallDTO> dtos = List.of(
                new CallDTO(newStateNumber, CallType.MISSED, now, 0l),
                new CallDTO(newStateNumber, CallType.INCOMING, now, INCOMING_CALL_DURATION_THRESHOLD - 1),
                new CallDTO(inProgressStateNumber, CallType.MISSED, now, 0l),
                new CallDTO(inProgressStateNumber, CallType.OUTGOING, now, INCOMING_CALL_DURATION_THRESHOLD - 1)
        );

        orderStateCRUDService.updateState(new UpdateOrderStateCommand(dtos));

        Map<PhoneNumber, OrderState> expectedStates = Map.of(
                newStateNumber, OrderState.NEW,
                noStateNUmber, OrderState.IN_PROGRESS
        );
        BDDMockito.then(updateOrderStatePort)
                .should(times(1))
                .update(expectedStates);
    }
}