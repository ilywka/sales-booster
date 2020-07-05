package com.sashnikov.salesbooster.app.entity;

import lombok.AllArgsConstructor;
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
public class Call {

    private Long id;
    private String number;
    private CallType callType;
    private long durationSeconds;

}
