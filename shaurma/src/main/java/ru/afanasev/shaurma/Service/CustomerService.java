package ru.afanasev.shaurma.Service;

import io.helidon.webserver.Routing.Rules;
import lombok.AllArgsConstructor;
import ru.afanasev.shaurma.common.Result;
import ru.afanasev.shaurma.common.ResultDto;
import ru.afanasev.shaurma.model.customer.CustomerDao;
import ru.afanasev.shaurma.model.customer.CustomerDto;
import io.helidon.dbclient.DbClient;
import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

@AllArgsConstructor
public class CustomerService implements Service {

        private final DbClient dbClient;
        private final CustomerDao customerDao = CustomerDao.getInstance();

        @Override
        public void update(Rules rules) {
                rules.get("/", this::getConsumers).post("/customer",
                                Handler.create(CustomerDto.class, this::insertConsumer));
        }

        private void getConsumers(ServerRequest request, ServerResponse response) {
                customerDao.findAll(dbClient);
        }

        private void insertConsumer(ServerRequest request, ServerResponse response, CustomerDto dto) {
                ResultDto result = new ResultDto();
                try{
                  customerDao.addCustomer(dbClient, dto);
                  result.setMessage("Покупатель "+dto.getName()+" успешно добавлен в базу данных");
                  result.setResult(Result.SUCCESS);
                  System.err.println("success");
                  response.send(result);
                } catch (Exception e){
                    System.err.println(e.getMessage()); 
                    result.setMessage("Покупатель "+dto.getName()+" не добавлен в базу данных");
                    result.setResult(Result.FAIL);
                    System.err.println("fail");
                    response.send(result); 
                }
                

        }
}