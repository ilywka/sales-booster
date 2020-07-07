package com.sashnikov.salesbooster.app.entity;

import java.time.LocalDateTime;
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
public class Call {

    private Long id;
    private Customer customer;
    private CallType callType;
    private LocalDateTime date;
    private long durationSeconds;

    public Call(Customer customer, CallType callType, LocalDateTime date, long durationSeconds) {
        this.customer = customer;
        this.callType = callType;
        this.date = date;
        this.durationSeconds = durationSeconds;
    }
}
