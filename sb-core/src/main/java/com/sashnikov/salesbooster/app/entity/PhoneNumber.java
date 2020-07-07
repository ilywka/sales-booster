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

    public PhoneNumber(String rawNumber) {
        //TODO: implement
    }
}
