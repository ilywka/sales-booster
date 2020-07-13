package com.sashnikov.salesbooster.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ilya_Sashnikau
 */
@Data
@AllArgsConstructor
public class PhoneNumber {
    private String code;
    private String number;

    public PhoneNumber(String numberString) {
        if (numberString.startsWith("+375")) {
            this.code = numberString.substring(4, 6);
            this.number = numberString.substring(7);
        }
        //TODO: implement
    }
}
