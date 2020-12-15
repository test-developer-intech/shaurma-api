package ru.afanasev.shaurma.model.customer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import io.helidon.dbclient.DbClient;

public class CustomerDao {

    private static CustomerDao instance;

    public static CustomerDao getInstance() {
        if (instance == null) {
            synchronized (CustomerDao.class) {
                instance = new CustomerDao();
            }
        }
        return instance;

    }

    public Customer findAll(DbClient dbClient) {
        dbClient.execute(exe -> exe.namedGet("get-all-customers")).await();
        return null;
    }
    public void addCustomer(DbClient dbClient, CustomerDto customerDto){
        dbClient.inTransaction(exe-> exe.namedInsert("insert-customer",          
        customerDto.getName(),
        customerDto.getNumber(),
        Timestamp.valueOf(LocalDateTime.now()).getTime())).await();

    }
    public void deleteCustomerById(DbClient dbClient, Long id){
        dbClient.inTransaction(exe-> exe.namedDelete("delete-customer-by-id", id)).await();
    }

}