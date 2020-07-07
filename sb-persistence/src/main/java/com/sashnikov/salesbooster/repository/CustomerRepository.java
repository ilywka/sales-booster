package com.sashnikov.salesbooster.repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.sashnikov.salesbooster.app.entity.Customer;
import com.sashnikov.salesbooster.app.entity.PhoneNumber;
import com.sashnikov.salesbooster.app.port.GetCustomerPort;
import com.sashnikov.salesbooster.app.port.SaveCustomerPort;
import com.sashnikov.salesbooster.model.PhoneNumberDB;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Ilya_Sashnikau
 */
@Repository
public class CustomerRepository extends NamedParameterJdbcDaoSupport implements SaveCustomerPort, GetCustomerPort {

    public CustomerRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public Map<PhoneNumber, Customer> getByNumbers(Set<PhoneNumber> keySet) {
        if (CollectionUtils.isEmpty(keySet)) {
            return Collections.emptyMap();
        }
        Set<String> numbers = keySet.stream()
                .map(PhoneNumberDB::new)
                .map(PhoneNumberDB::getNumberValue)
                .collect(Collectors.toSet());

        String sql = "select customer_id, name, number from customer where number in (:numbers)";
        MapSqlParameterSource params = new MapSqlParameterSource("numbers", numbers);
        RowMapper<Customer> rowMapper = (rs, rowNum) -> {

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setNumber(new PhoneNumberDB(rs.getString("number")).toPhoneNumber());

            return customer;

        };
        List<Customer> customers = getNamedParameterJdbcTemplate().query(sql, params, rowMapper);

        return customers.stream()
                .collect(Collectors.toMap(Customer::getNumber, Function.identity()));
    }

    @Override
    public Map<PhoneNumber, Customer> createCustomers(Set<PhoneNumber> newPhoneNumbers) {
        if (CollectionUtils.isEmpty(newPhoneNumbers)) {
            return Collections.emptyMap();
        }

        PhoneNumber[] phoneNumbers = newPhoneNumbers.toArray(PhoneNumber[]::new);

        String sql = "INSERT INTO customer (number) VALUES (?)";


        BatchPreparedStatementSetter ps = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                PhoneNumber phoneNumber = phoneNumbers[i];
                int index = 1;
                ps.setString(index++, new PhoneNumberDB(phoneNumber).getNumberValue());

            }

            @Override
            public int getBatchSize() {
                return newPhoneNumbers.size();
            }
        };
        getJdbcTemplate().batchUpdate(sql, ps);
        return getByNumbers(newPhoneNumbers);
    }
}
