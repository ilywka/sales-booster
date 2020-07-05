package com.sashnikov.salesbooster.app.repository;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;

/**
 * @author Ilya_Sashnikau
 */
public interface SaveCallsPort {
    void save(List<Call> calls);
}
