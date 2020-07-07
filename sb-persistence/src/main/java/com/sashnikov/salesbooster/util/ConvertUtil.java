package com.sashnikov.salesbooster.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Ilya_Sashnikau
 */
public class ConvertUtil {
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().withNano(0);
    }

    public static Timestamp toTimestamp(LocalDateTime date) {
        return Timestamp.valueOf(date.withNano(0));
    }
}
