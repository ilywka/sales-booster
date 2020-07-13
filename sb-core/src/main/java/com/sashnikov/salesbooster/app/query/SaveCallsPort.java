package com.sashnikov.salesbooster.app.query;

import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Call;

/**
 * @author Ilya_Sashnikau
 */
public interface SaveCallsPort {
    void save(Set<Call> calls);
}
