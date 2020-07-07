package com.sashnikov.salesbooster.app.port;

import java.util.Map;
import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;

/**
 * @author Ilya_Sashnikau
 */
public interface GetCustomerPort {
    Map<PhoneNumber, Customer> getByNumbers(Set<PhoneNumber> keySet);
}
