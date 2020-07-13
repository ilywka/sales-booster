package com.sashnikov.salesbooster.app.port;

import java.util.Map;
import com.sashnikov.salesbooster.app.entity.OrderState;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;

/**
 * @author Ilya_Sashnikau
 */
public interface UpdateOrderStatePort {
    void update(Map<PhoneNumber, OrderState> orderState);
}
