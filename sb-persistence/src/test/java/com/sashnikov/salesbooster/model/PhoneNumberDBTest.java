package com.sashnikov.salesbooster.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import org.junit.jupiter.api.Test;

/**
 * @author Ilya_Sashnikau
 */
class PhoneNumberDBTest {

    @Test
    void shouldConvertValues() {
        PhoneNumber phoneNumber = new PhoneNumber("29", "123");
        PhoneNumberDB phoneNumberDB = new PhoneNumberDB(phoneNumber);
        PhoneNumber actualPhoneNumber = new PhoneNumberDB(phoneNumberDB.getNumberValue()).toPhoneNumber();

        assertThat(actualPhoneNumber, equalTo(phoneNumber));
    }
}