package co.com.bancolombia.model.customer.gateways;

import co.com.bancolombia.model.customer.Customer;
import co.com.bancolombia.model.customer.CustomerResult;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<CustomerResult> createCustomer(Customer customer);
}
