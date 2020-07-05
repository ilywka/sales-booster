package com.sashnikov.salesbooster.repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.sashnikov.salesbooster.app.entity.Call;
import com.sashnikov.salesbooster.app.entity.CallType;
import com.sashnikov.salesbooster.app.repository.ReadCallsPort;
import com.sashnikov.salesbooster.app.repository.SaveCallsPort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Ilya_Sashnikau
 */
@Repository
public class CallsRepository extends JdbcDaoSupport implements SaveCallsPort, ReadCallsPort {


    public CallsRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void save(List<Call> calls) {
        getJdbcTemplate()
                .batchUpdate("insert into calls_history (number, type, duration_seconds) values (?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                int index = 1;
                                ps.setString(index++, calls.get(i).getNumber());
                                ps.setString(index++, calls.get(i).getCallType().name());
                                ps.setLong(index, calls.get(i).getDurationSeconds());
                            }

                            @Override
                            public int getBatchSize() {
                                return calls.size();
                            }
                        });
    }


    @Override
    public List<Call> getAll() {
        RowMapper<Call> rowMapper = (rs, rowNum) -> {
            Call call = new Call();

            int index = 1;
            call.setId(rs.getLong(index++));
            call.setNumber(rs.getString(index++));
            call.setCallType(CallType.valueOf(rs.getString(index++)));
            call.setDurationSeconds(rs.getLong(index++));

            return call;

        };
        String sql = "select id, number, type, duration_seconds from calls_history";
        return getJdbcTemplate().query(sql, rowMapper);
    }
}
