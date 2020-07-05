package com.sashnikov.salesbooster.app.usecase;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;

/**
 * @author Ilya_Sashnikau
 */
public interface SaveCallsUseCase {
    void save(List<Call> calls);
}
