package com.sashnikov.salesbooster.app.usecase;

import java.util.List;
import com.sashnikov.salesbooster.app.dto.CallDTO;
import lombok.Data;

/**
 * @author Ilya_Sashnikau
 */
public interface UpdateOrderStateFromCallsHistoryUseCase {
    void updateState(UpdateOrderStateCommand updateStateCommand);

    @Data
    class UpdateOrderStateCommand {
        private final List<CallDTO> callDTOS;
    }
}
