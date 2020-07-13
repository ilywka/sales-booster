package com.sashnikov.salesbooster.app.query;

import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;

/**
 * @author Ilya_Sashnikau
 */
public interface GetCallsQuery {
    List<Call> getAll();
}
