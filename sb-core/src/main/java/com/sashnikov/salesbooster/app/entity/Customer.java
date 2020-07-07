package com.sashnikov.salesbooster.app.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ilya_Sashnikau
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Customer {

    private Long id;
    private String name;
    private PhoneNumber number;

    public Customer(long id) {
        this.id = id;
    }
}
