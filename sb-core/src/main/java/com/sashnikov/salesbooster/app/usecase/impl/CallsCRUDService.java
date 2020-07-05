package com.sashnikov.salesbooster.app.usecase.impl;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.repository.SaveCallsPort;
import com.sashnikov.salesbooster.app.usecase.SaveCallsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Ilya_Sashnikau
 */
@Service
@RequiredArgsConstructor
public class CallsCRUDService implements SaveCallsUseCase {

    private final SaveCallsPort saveCallsPort;

    @Override
    public void save(List<Call> calls) {
        saveCallsPort.save(calls);
    }
}
