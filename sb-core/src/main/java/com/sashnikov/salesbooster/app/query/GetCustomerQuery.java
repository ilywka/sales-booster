package com.sashnikov.salesbooster.app.query;

import java.util.Map;
import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;

/**
 * @author Ilya_Sashnikau
 */
public interface GetCustomerQuery {
    Map<PhoneNumber, Customer> getByNumbers(Set<PhoneNumber> keySet);
}
