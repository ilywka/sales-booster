package com.sashnikov.salesbooster.repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.port.GetCallsPort;
import com.sashnikov.salesbooster.app.port.SaveCallsPort;
import com.sashnikov.salesbooster.util.ConvertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Ilya_Sashnikau
 */
@Repository
public class CallsRepository extends JdbcDaoSupport implements SaveCallsPort, GetCallsPort {


    public CallsRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void save(Set<Call> calls) {
        if (CollectionUtils.isEmpty(calls)) {
            return;
        }

        List<Call> callsList = new ArrayList<>(calls);
        getJdbcTemplate()
                .batchUpdate(
                        "insert into calls_history ( customer_id, type, date, duration_seconds ) values (?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                int index = 1;
                                Call call = callsList.get(i);
                                ps.setLong(index++, call.getCustomer().getId());
                                ps.setString(index++, call.getCallType().name());
                                ps.setTimestamp(index++, ConvertUtil.toTimestamp(call.getDate()));
                                ps.setLong(index, call.getDurationSeconds());
                            }

                            @Override
                            public int getBatchSize() {
                                return callsList.size();
                            }
                        });
    }


    @Override
    public List<Call> getAll() {
        RowMapper<Call> rowMapper = (rs, rowNum) -> {
            Call call = new Call();

            int index = 1;
            call.setId(rs.getLong(index++));
            call.setCustomer(new Customer(rs.getLong(index++)));
            call.setCallType(CallType.valueOf(rs.getString(index++)));
            call.setDate(ConvertUtil.toLocalDateTime(rs.getTimestamp(index++)));
            call.setDurationSeconds(rs.getLong(index++));

            return call;

        };
        String sql = "select customer_id, customer_id, type, date, duration_seconds from calls_history";
        return getJdbcTemplate().query(sql, rowMapper);
    }
}
