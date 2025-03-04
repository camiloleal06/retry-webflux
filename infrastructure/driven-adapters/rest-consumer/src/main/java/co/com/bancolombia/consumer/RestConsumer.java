package co.com.bancolombia.consumer;

import co.com.bancolombia.TechnicalException;
import co.com.bancolombia.model.customer.Customer;
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
    public Mono<String> createCustomer(Customer customer) {

        return webClient.post().uri("http://sysredcartagena.duckdns.org:8888/api/v1/create")
                .bodyValue(
                        Mapper.MAPPER.toCustomerRequest(customer))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Response from create customer: {}", response))
                .flatMap(this::validateCreateCustomerResult)
                .onErrorResume(error -> this.handleError(error, customer));
    }

    private Mono<String> validateCreateCustomerResult(String customerResult) {
        if (customerResult.equals("SUCCESS")) {
            return Mono.just(customerResult);
        } else {
            return Mono.error(new TechnicalException("Create Customer Fail"));
        }
    }

    private Mono<String> handleError(Throwable error, Customer customer) {
        log.error("Error creating customer", error);
        return Mono.error(new TechnicalException("Error creating customer"));
    }
}
