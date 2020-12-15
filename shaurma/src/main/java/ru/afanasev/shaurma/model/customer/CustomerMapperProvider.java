package ru.afanasev.shaurma.model.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.helidon.dbclient.DbColumn;
import io.helidon.dbclient.DbMapper;
import io.helidon.dbclient.DbRow;
import io.helidon.dbclient.spi.DbMapperProvider;

public class CustomerMapperProvider implements DbMapperProvider {
    private static final CustomerMapper MAPPER = new CustomerMapper();

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<DbMapper<T>> mapper(Class<T> type) {
        return type.equals(Customer.class) ? Optional.of((DbMapper<T>) MAPPER) : Optional.empty();
    }

    static class CustomerMapper implements DbMapper<Customer> {

        @Override
        public Customer read(DbRow row) {
            DbColumn id = row.column("customer_id");
            DbColumn name = row.column("name");
            DbColumn number = row.column("mob_number");
            return new Customer(id.as(Long.class), name.as(String.class), number.as(Integer.class));
        }

        @Override
        public Map<String, Object> toNamedParameters(Customer value) {
            Map<String, Object> map = new HashMap<>(3);
            map.put("customer_id", value.getId());
            map.put("name", value.getName());
            map.put("mob_number", value.getNumber());
            return map;
        }

        @Override
        public List<Object> toIndexedParameters(Customer value) {
            List<Object> list = new ArrayList<>(3);
            list.add(value.getId());
            list.add(value.getName());
            list.add(value.getName());
            return list;
        }
    }
}