package com.sashnikov.salesbooster.app.usecase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.OrderState;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.UpdateOrderStatePort;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author Ilya_Sashnikau
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderStateCRUDService implements UpdateOrderStateFromCallsHistoryUseCase {

    private final UpdateOrderStatePort updateOrderStatePort;

    @Override
    public void updateState(UpdateOrderStateCommand updateStateCommand) {
        List<CallDTO> callDTOS = updateStateCommand.getCallDTOS();
        if (CollectionUtils.isEmpty(callDTOS)) {
            return;
        }

        Map<PhoneNumber, OrderState> ordersState = determineOrdersState(callDTOS);

        log.info("Updating order states: {}", ordersState);
        updateOrderStatePort.update(ordersState);
    }

    private Map<PhoneNumber, OrderState> determineOrdersState(List<CallDTO> callDTOS) {
        Map<PhoneNumber, List<CallDTO>> numberCalls =
                callDTOS.stream().collect(Collectors.groupingBy(CallDTO::getNumber));
        Map<PhoneNumber, OrderState> orderStates = new HashMap<>();
        for (Entry<PhoneNumber, List<CallDTO>> phoneNumberListEntry : numberCalls.entrySet()) {
            PhoneNumber phoneNumber = phoneNumberListEntry.getKey();
            phoneNumberListEntry.getValue().stream()
                    .map(this::determineOrderState)
                    .reduce((orderState, orderState2) -> {
                        if (orderState == OrderState.IN_PROGRESS || orderState2 == OrderState.IN_PROGRESS) {
                            return OrderState.IN_PROGRESS;
                        } else {
                            return OrderState.NEW;
                        }
                    })
                    .ifPresent(orderState -> orderStates.put(phoneNumber, orderState));

        }

        return orderStates;
    }

    private OrderState determineOrderState(CallDTO callDTO) {
        CallType callType = callDTO.getCallType();
        if (callType == CallType.MISSED) {
            return OrderState.NEW;
        }

        if (callType == CallType.OUTGOING || callType == CallType.INCOMING) {
            return OrderState.IN_PROGRESS;
        }

        return null;
    }
}
