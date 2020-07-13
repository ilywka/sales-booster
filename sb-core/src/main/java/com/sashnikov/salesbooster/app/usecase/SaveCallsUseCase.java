package com.sashnikov.salesbooster.app.usecase;

import java.util.List;
import com.sashnikov.salesbooster.app.dto.CallDTO;
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
}
