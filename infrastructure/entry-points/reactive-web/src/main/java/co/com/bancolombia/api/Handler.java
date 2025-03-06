package co.com.bancolombia.api;

import co.com.bancolombia.usecase.getcustomer.CreateCustomerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final CreateCustomerUseCase createCustomerUseCase;

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CustomerRequest.class)
                .flatMap(customerRequest -> {
                    log.info("Received request: {}", customerRequest);
                    return createCustomerUseCase.createCustomer(Mapper.MAPPER.toCustomer(customerRequest));
                })
                .flatMap(customer -> ServerResponse.ok()
                        .bodyValue(Mapper.MAPPER.toFrontResponse(customer)));
    }
}
