package com.sashnikov.salesbooster.app.api.v1.model;

import java.time.LocalDateTime;
import com.sashnikov.salesbooster.app.entity.CallType;
import lombok.Data;

/**
 * @author Ilya_Sashnikau
 */
@Data
public class CallApiDTO {
    private final String number;
    private final CallType callType;
    private final LocalDateTime date;
    private final long durationSeconds;
}
