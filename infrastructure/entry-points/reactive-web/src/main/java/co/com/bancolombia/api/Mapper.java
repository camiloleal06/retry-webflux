package co.com.bancolombia.api;

import co.com.bancolombia.model.customer.Customer;
import co.com.bancolombia.model.customer.CustomerResult;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {
    Mapper MAPPER = Mappers.getMapper(Mapper.class);

    Customer toCustomer(CustomerRequest customerRequest);
    //CustomerResult toCustomerResult (FrontResponse frontResponse);
    FrontResponse toFrontResponse (CustomerResult customerResult);

}