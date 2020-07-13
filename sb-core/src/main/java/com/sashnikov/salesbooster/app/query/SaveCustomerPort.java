package com.sashnikov.salesbooster.app.query;

import java.util.Map;
import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;

/**
 * @author Ilya_Sashnikau
 */
public interface SaveCustomerPort {
    Map<PhoneNumber, Customer> createCustomers(Set<PhoneNumber> newPhoneNumbers);
}
