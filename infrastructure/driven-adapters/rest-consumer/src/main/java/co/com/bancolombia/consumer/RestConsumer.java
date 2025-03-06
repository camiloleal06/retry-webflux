package co.com.bancolombia.consumer;

import co.com.bancolombia.TechnicalException;
import co.com.bancolombia.model.customer.Customer;
import co.com.bancolombia.model.customer.CustomerResult;
import co.com.bancolombia.model.customer.gateways.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestConsumer implements CustomerRepository {
   private final WebClient webClient;

    @Override
    public Mono<CustomerResult> createCustomer(Customer customer) {

        return webClient.post()
                .uri("https://qolpm4uzdi.execute-api.us-east-1.amazonaws.com/send/test")
                .bodyValue(Mapper.MAPPER.toCustomerRequest(customer))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .doOnSuccess(response -> log.info("Response from create customer: {}", response))
                .map(Mapper.MAPPER::toCustomerResult)
                .flatMap(this::validateCreateCustomerResult)
                .onErrorResume(error -> this.handleError(error, customer));
    }

    private Mono<CustomerResult> validateCreateCustomerResult(CustomerResult customerResult) {
        if (customerResult.getStatusCode().equals("200")) {
            log.info("Se ha creado la petición con exito");
            return Mono.just(customerResult);
        } else if (customerResult.getStatusCode().equals("500")) {
            return Mono.error(new TechnicalException("Create Customer Fail"));
        }
        else {
            return Mono.error(new RuntimeException("Error no controlado"));
        }
    }

    private Mono<CustomerResult> handleError(Throwable error, Customer customer) {
        log.error("Error creating customer", error);
        return Mono.error(new TechnicalException("Error creating customer"));
    }
}
