package com.sashnikov.salesbooster.app.usecase;

import java.time.LocalDateTime;
import java.util.List;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import lombok.Data;

/**
 * @author Ilya_Sashnikau
 */
public interface SaveCallsUseCase {
    void save(SaveCallsCommand calls);

    @Data
    class SaveCallsCommand {
        private final List<CallDTO> calls;
    }

    @Data
    class CallDTO {
        private final PhoneNumber number;
        private final CallType callType;
        private final LocalDateTime date;
        private final long durationSeconds;
    }
}
