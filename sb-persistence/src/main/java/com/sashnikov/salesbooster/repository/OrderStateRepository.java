package com.sashnikov.salesbooster.repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import com.sashnikov.salesbooster.app.entity.OrderState;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.UpdateOrderStatePort;
import com.sashnikov.salesbooster.model.PhoneNumberDB;
import org.apache.commons.collections4.MapUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Ilya_Sashnikau
 */
@Repository
public class OrderStateRepository extends NamedParameterJdbcDaoSupport implements UpdateOrderStatePort {

    public OrderStateRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void update(Map<PhoneNumber, OrderState> orderState) {
        if (MapUtils.isEmpty(orderState)) {
            return;
        }

        String sql = "INSERT INTO order_state (phone_number, state, updated_date) " +
                "VALUES(:phone_number, :state, :updated_date) " +
                "ON CONFLICT (phone_number) DO UPDATE " +
                "SET state = EXCLUDED.phone_number;";

        Function<Entry<PhoneNumber, OrderState>, MapSqlParameterSource> entryToParamsMap =
                phoneNumberOrderStateEntry ->
                        new MapSqlParameterSource()
                                .addValue("phone_number", new PhoneNumberDB(phoneNumberOrderStateEntry.getKey()).getNumberValue())
                                .addValue("state", phoneNumberOrderStateEntry.getValue().name())
                                .addValue("updated_date", LocalDateTime.now());
        MapSqlParameterSource[] parameters =
                orderState.entrySet().stream()
                        .map(entryToParamsMap)
                        .toArray(MapSqlParameterSource[]::new);
        getNamedParameterJdbcTemplate().batchUpdate(sql, parameters);
    }
}
