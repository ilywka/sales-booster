package com.sashnikov.salesbooster.app.usecase.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.port.SaveCallsPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase;
import com.sashnikov.salesbooster.app.usecase.UpdateOrderStateFromCallsHistoryUseCase.UpdateOrderStateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ilya_Sashnikau
 */
@Service
@RequiredArgsConstructor
public class CallsCRUDService implements SaveCallsUseCase {

    private final SaveCallsPort saveCallsPort;
    private final UpdateOrderStateFromCallsHistoryUseCase updateOrderStateFromCallsHistoryUseCase;

    @Override
    public void save(SaveCallsCommand command) {
        List<CallDTO> callDTOList = command.getCalls();

        Set<Call> calls = callDTOList.stream()
                .map(callDTO ->
                        new Call(
                                callDTO.getNumber(),
                                callDTO.getCallType(),
                                callDTO.getDate(),
                                callDTO.getDurationSeconds()
                        )
                )
                .collect(Collectors.toSet());

        saveCallsPort.save(calls);
        updateOrderStateFromCallsHistoryUseCase.updateState(new UpdateOrderStateCommand(callDTOList));
    }

//    private Map<PhoneNumber, Customer> getCustomersPhoneNumbers(List<CallDTO> callDTOList) {
//        Map<PhoneNumber, Set<CallDTO>> incomingPhoneNumberCalls =
//                callDTOList.stream()
//                        .collect(Collectors.groupingBy(CallDTO::getNumber, Collectors.toSet()));
//        Set<PhoneNumber> incomingPhoneNumbers = incomingPhoneNumberCalls.keySet();
//
//        Map<PhoneNumber, Customer> customerNumbers =
//                getCustomerQuery.getByNumbers(incomingPhoneNumbers);
//
//        Set<PhoneNumber> existingPhoneNumbers = customerNumbers.keySet();
//        Set<PhoneNumber> newPhoneNumbers =
//                SetUtils.difference(incomingPhoneNumbers, existingPhoneNumbers)
//                        .toSet();
//        Map<PhoneNumber, Customer> newCustomersNumbers =
//                saveCustomerPort.createCustomers(newPhoneNumbers);
//
//        Map<PhoneNumber, Customer> allPhoneNumbersCustomers = new HashMap<>(customerNumbers);
//        allPhoneNumbersCustomers.putAll(newCustomersNumbers);
//        return allPhoneNumbersCustomers;
//    }
}
