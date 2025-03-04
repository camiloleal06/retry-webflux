package co.com.bancolombia.model.customer.gateways;

import co.com.bancolombia.model.customer.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<String> createCustomer(Customer customer);
}
