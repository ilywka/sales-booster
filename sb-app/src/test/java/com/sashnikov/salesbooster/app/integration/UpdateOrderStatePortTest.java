package com.sashnikov.salesbooster.app.integration;

import java.util.Map;
import com.sashnikov.salesbooster.app.entity.OrderState;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.UpdateOrderStatePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Ilya_Sashnikau
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class UpdateOrderStatePortTest extends SingletonPostgresContainerBaseTest {

    @Autowired
    private UpdateOrderStatePort updateOrderStatePort;

    @Test
    void shouldUpdateOrderState() {
        Map<PhoneNumber, OrderState> ordersState = generateMap();
        updateOrderStatePort.update(ordersState);
    }

    private Map<PhoneNumber, OrderState> generateMap() {
        return null;
    }
}
