package co.com.bancolombia.consumer.config;

import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.model.customer.gateways.CustomerRepository;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static co.com.bancolombia.consumer.config.RestConsumerUtility.getClientHttpConnector;
import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    @Value("${adapter.restconsumer.url}")
    private String url;

    @Bean
    public CustomerRepository customerRepository(@Qualifier("createCustomerWebClient") WebClient webClient) {
        return new RestConsumer(webClient);
    }

    @Bean(name = "createCustomerWebClient")
    public WebClient createCustomerWebClient(
            @Value("${adapter.restconsumer.url}") String host) {
        return WebClient.builder().baseUrl(host).clientConnector(
                        getClientHttpConnector(10000, 10000))
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE).build();
    }
}
