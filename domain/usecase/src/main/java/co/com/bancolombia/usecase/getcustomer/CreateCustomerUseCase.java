package co.com.bancolombia.usecase.getcustomer;

import co.com.bancolombia.TechnicalException;
import co.com.bancolombia.model.CustomRetryExhaustedException;
import co.com.bancolombia.model.customer.Customer;
import co.com.bancolombia.model.customer.gateways.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@RequiredArgsConstructor
public class CreateCustomerUseCase {
    private final CustomerRepository customerRepository;

     public Mono<String> createCustomer(Customer customer) {
      return customerRepository.createCustomer(customer).
         retryWhen(buildRetryConfigBackoffAndJitter(customer));
    }

    private Retry buildRetryConfigBackoffAndJitter(Customer customer) {
        return Retry.backoff(3, Duration.ofSeconds(2))
                .jitter(0.2)
                .filter(this::isRetryableException)
                .doBeforeRetry(retrySignal -> {
                    createCustomerByRetry(customer);
                    System.out.println("Retry attempt: " + retrySignal.totalRetries()); })
                .onRetryExhaustedThrow(this::handleRetryExhausted);
    }

    private Retry buildRetryConfigFixedDelay(Customer customer) {
        return Retry.fixedDelay(3, Duration.ofSeconds(2))
                .filter(this::isRetryableException)
                .doBeforeRetry(retrySignal -> {
                    createCustomerByRetry(customer);
                    System.out.println("Retry attempt: " + retrySignal.totalRetries()); })
                .onRetryExhaustedThrow(this::handleRetryExhausted);
    }

    private Retry buildRetryConfigExponentialBackoffNoJitter(Customer customer) {
        return Retry.backoff(3, Duration.ofSeconds(2))
                .filter(this::isRetryableException)
                .doBeforeRetry(retrySignal -> {
                    createCustomerByRetry(customer);
                    System.out.println("Retry attempt: " + retrySignal.totalRetries()); })
                .onRetryExhaustedThrow(this::handleRetryExhausted);
    }



    private void createCustomerByRetry(Customer customer) {
        customerRepository.createCustomer(customer)
                .subscribe();
    }

    private boolean isRetryableException(Throwable throwable) {
        return (throwable instanceof TechnicalException);
    }

    private CustomRetryExhaustedException handleRetryExhausted(RetryBackoffSpec spec, Retry.RetrySignal signal) {
        System.out.println(signal.totalRetries());
         return new CustomRetryExhaustedException("Retries exhausted");
    }
}
