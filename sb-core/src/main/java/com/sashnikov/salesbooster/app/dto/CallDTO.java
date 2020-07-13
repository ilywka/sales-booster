package com.sashnikov.salesbooster.app.dto;

import java.time.LocalDateTime;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import lombok.Data;

/**
 * @author Ilya_Sashnikau
 */
@Data
public class CallDTO {
    private final PhoneNumber number;
    private final CallType callType;
    private final LocalDateTime date;
    private final long durationSeconds;
}
