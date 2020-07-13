package com.sashnikov.salesbooster.app.usecase.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.query.GetCustomerQuery;
import com.sashnikov.salesbooster.app.query.SaveCallsPort;
import com.sashnikov.salesbooster.app.query.SaveCustomerPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase.CallDTO;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase.SaveCallsCommand;
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
    @Mock private SaveCustomerPort saveCustomerPort;
    @Mock private GetCustomerQuery getCustomerQuery;
    @InjectMocks private CallsCRUDService callsCRUDService;

    @Test
    void shouldCreateNewCustomersAndSaveNumbers() {
        PhoneNumber existingNumber = new PhoneNumber("29", "123");
        PhoneNumber absentNumber = new PhoneNumber("29", "111");
        Customer existingCustomer = new Customer(1L, "qwe", existingNumber);
        Customer absentCustomer = new Customer(2L, "qwer", absentNumber);
        LocalDateTime date = LocalDateTime.now();
        List<CallDTO> callDTOS = List.of(
                new CallDTO(existingNumber, CallType.INCOMING, date, 1L),
                new CallDTO(existingNumber, CallType.INCOMING, date, 2L),
                new CallDTO(absentNumber, CallType.INCOMING, date, 1L)
        );

        given(getCustomerQuery.getByNumbers(Set.of(existingNumber, absentNumber))).willReturn(Map.of(existingNumber, existingCustomer));
        given(saveCustomerPort.createCustomers(Set.of(absentNumber))).willReturn(Map.of(absentNumber, absentCustomer));

        SaveCallsCommand command = new SaveCallsCommand(callDTOS);
        callsCRUDService.save(command);

        then(saveCustomerPort).should(times(1)).createCustomers(Set.of(absentNumber));
        Set<Call> expectedToSaveCalls = Set.of(
                new Call(existingCustomer, CallType.INCOMING, date, 1L),
                new Call(existingCustomer, CallType.INCOMING, date, 2L),
                new Call(absentCustomer, CallType.INCOMING, date, 1L)
        );
        then(saveCallsPort).should(times(1)).save(expectedToSaveCalls);
    }

    @Test
    void shouldSaveNumbersWhenAllNumbersPresent() {
        PhoneNumber existingNumber = new PhoneNumber("29", "123");
        PhoneNumber existingNumber2 = new PhoneNumber("29", "111");
        Customer existingCustomer = new Customer(1L, "qwe", existingNumber);
        Customer existingCustomer2 = new Customer(2L, "qwer", existingNumber2);
        LocalDateTime date = LocalDateTime.now();
        List<CallDTO> callDTOS = List.of(
                new CallDTO(existingNumber, CallType.INCOMING, date, 1L),
                new CallDTO(existingNumber, CallType.INCOMING, date, 2L),
                new CallDTO(existingNumber2, CallType.INCOMING, date, 1L)
        );

        given(getCustomerQuery.getByNumbers(Set.of(existingNumber, existingNumber2)))
                .willReturn(Map.of(
                        existingNumber, existingCustomer,
                        existingNumber2, existingCustomer2
                ));

        SaveCallsCommand command = new SaveCallsCommand(callDTOS);
        callsCRUDService.save(command);

        then(saveCustomerPort).should(times(1)).createCustomers(Set.of());
        Set<Call> expectedToSaveCalls = Set.of(
                new Call(existingCustomer, CallType.INCOMING, date, 1L),
                new Call(existingCustomer, CallType.INCOMING, date, 2L),
                new Call(existingCustomer2, CallType.INCOMING, date, 1L)
        );
        then(saveCallsPort).should(times(1)).save(expectedToSaveCalls);
    }

    @Test
    void shouldSaveNumbersWhenNoNumbersPresent() {
        PhoneNumber absentNumber = new PhoneNumber("29", "123");
        PhoneNumber absentNumber2 = new PhoneNumber("29", "111");
        Customer absentCustomer = new Customer(1L, "qwe", absentNumber);
        Customer absentCustomer2 = new Customer(2L, "qwer", absentNumber2);
        LocalDateTime date = LocalDateTime.now();
        List<CallDTO> callDTOS = List.of(
                new CallDTO(absentNumber, CallType.INCOMING, date, 1L),
                new CallDTO(absentNumber, CallType.INCOMING, date, 2L),
                new CallDTO(absentNumber2, CallType.INCOMING, date, 1L)
        );
        Set<PhoneNumber> absentNumbersSet = Set.of(absentNumber, absentNumber2);

        given(getCustomerQuery.getByNumbers(absentNumbersSet))
                .willReturn(Map.of());
        given(saveCustomerPort.createCustomers(absentNumbersSet))
                .willReturn(Map.of(
                        absentNumber, absentCustomer,
                        absentNumber2, absentCustomer2
                ));

        SaveCallsCommand command = new SaveCallsCommand(callDTOS);
        callsCRUDService.save(command);

        then(saveCustomerPort).should(times(1))
                .createCustomers(absentNumbersSet);
        Set<Call> expectedToSaveCalls = Set.of(
                new Call(absentCustomer, CallType.INCOMING, date, 1L),
                new Call(absentCustomer, CallType.INCOMING, date, 2L),
                new Call(absentCustomer2, CallType.INCOMING, date, 1L)
        );
        then(saveCallsPort).should(times(1)).save(expectedToSaveCalls);
    }
}