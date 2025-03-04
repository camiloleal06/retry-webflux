package co.com.bancolombia.consumer;

import co.com.bancolombia.model.customer.Customer;
import co.com.bancolombia.model.customer.CustomerResult;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {
    Mapper MAPPER = Mappers.getMapper(Mapper.class);
CustomerResult toCustomerResult(CustomerResponse customerResponse);
CustomerRequest toCustomerRequest(Customer customer);
}