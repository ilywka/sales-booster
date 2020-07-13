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
    private PhoneNumber phoneNumber;
    private CallType callType;
    private LocalDateTime date;
    private long durationSeconds;

    public Call(PhoneNumber phoneNumber,
                CallType callType,
                LocalDateTime date,
                long durationSeconds) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.date = date;
        this.durationSeconds = durationSeconds;
    }
}
