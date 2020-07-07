package com.sashnikov.salesbooster.app.port;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;

/**
 * @author Ilya_Sashnikau
 */
public interface GetCallsPort {
    List<Call> getAll();
}
