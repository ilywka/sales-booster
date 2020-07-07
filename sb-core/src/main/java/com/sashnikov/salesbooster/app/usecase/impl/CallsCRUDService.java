package com.sashnikov.salesbooster.app.usecase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.GetCustomerPort;
import com.sashnikov.salesbooster.app.port.SaveCallsPort;
import com.sashnikov.salesbooster.app.port.SaveCustomerPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;

/**
 * @author Ilya_Sashnikau
 */
@Service
@RequiredArgsConstructor
public class CallsCRUDService implements SaveCallsUseCase {

    private final SaveCallsPort saveCallsPort;
    private final GetCustomerPort getCustomerPort;
    private final SaveCustomerPort saveCustomerPort;

    @Override
    public void save(SaveCallsCommand command) {
        List<CallDTO> callDTOList = command.getCalls();
        Map<PhoneNumber, Customer> allPhoneNumbersCustomers = getCustomersPhoneNumbers(callDTOList);

        Set<Call> calls = callDTOList.stream()
                .map(callDTO ->
                        new Call(
                                allPhoneNumbersCustomers.get(callDTO.getNumber()),
                                callDTO.getCallType(),
                                callDTO.getDate(),
                                callDTO.getDurationSeconds()
                        )
                )
                .collect(Collectors.toSet());
        saveCallsPort.save(calls);
    }

    private Map<PhoneNumber, Customer> getCustomersPhoneNumbers(List<CallDTO> callDTOList) {
        Map<PhoneNumber, Set<CallDTO>> incomingPhoneNumberCalls =
                callDTOList.stream()
                        .collect(Collectors.groupingBy(CallDTO::getNumber, Collectors.toSet()));
        Set<PhoneNumber> incomingPhoneNumbers = incomingPhoneNumberCalls.keySet();

        Map<PhoneNumber, Customer> customerNumbers =
                getCustomerPort.getByNumbers(incomingPhoneNumbers);

        Set<PhoneNumber> existingPhoneNumbers = customerNumbers.keySet();
        Set<PhoneNumber> newPhoneNumbers =
                SetUtils.difference(incomingPhoneNumbers, existingPhoneNumbers)
                        .toSet();
        Map<PhoneNumber, Customer> newCustomersNumbers =
                saveCustomerPort.createCustomers(newPhoneNumbers);

        Map<PhoneNumber, Customer> allPhoneNumbersCustomers = new HashMap<>(customerNumbers);
        allPhoneNumbersCustomers.putAll(newCustomersNumbers);
        return allPhoneNumbersCustomers;
    }
}
