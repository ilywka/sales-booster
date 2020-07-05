package com.sashnikov.salesbooster.repository;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.repository.SaveCallsPort;
import org.springframework.stereotype.Repository;

/**
 * @author Ilya_Sashnikau
 */
@Repository
public class CallsRepository implements SaveCallsPort {

    @Override
    public void save(List<Call> calls) {

    }
}
