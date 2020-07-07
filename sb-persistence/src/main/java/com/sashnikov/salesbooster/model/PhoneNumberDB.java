package com.sashnikov.salesbooster.model;

import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import lombok.Getter;

/**
 * @author Ilya_Sashnikau
 */
@Getter
public class PhoneNumberDB {

    private final String numberValue;
    private final String code;
    private final String number;

    public PhoneNumberDB(PhoneNumber phoneNumber) {
        this.code = phoneNumber.getCode();
        this.number = phoneNumber.getNumber();
        numberValue = String.join("-", code, number);
    }

    public PhoneNumberDB(String numberValue) {
        this.numberValue = numberValue;
        String[] split = numberValue.split("-");
        this.code = split[0];
        this.number = split[1];

    }

    public PhoneNumber toPhoneNumber() {
        return new PhoneNumber(code, number);
    }
}
