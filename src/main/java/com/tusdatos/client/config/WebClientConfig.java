package com.tusdatos.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfig {

    @Value("${webclient.config.timeout:30000}")
    private Integer timeout;

    @Bean
    public WebClient webClient() {
        return WebClient.
                builder().
                filter(this::logRequest).
                filter(this.logResponse()).
                clientConnector(new ReactorClientHttpConnector(this.httpClient())).
                build();
    }

    private Mono<ClientResponse> logRequest(ClientRequest request, ExchangeFunction next) {
        return Mono.deferContextual(contextView -> {
                    String uuid = contextView.get("UUID");
                    return Mono.just(StringUtils.isEmpty(uuid) ? UUID.randomUUID().toString() : uuid);
                }
        ).flatMap(uuid -> {
            log.info("[{}] Request: {} {}", uuid, request.method(), request.url());
            request.headers().forEach((name, values) -> values.forEach(value -> log.info("[{}] {}={}", uuid, name, value)));
            return next.exchange(request);
        });
    }


    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .registerModule(new JavaTimeModule());
    }

    private HttpClient httpClient() {
        var httpClient = HttpClient.create(this.connectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .responseTimeout(Duration.ofMillis(timeout)).doOnConnected(
                        connection -> connection
                                .addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                );
        httpClient.warmup().block();
        return httpClient;
    }

    private ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("WebClient")
                .maxConnections(500)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(8_000L))
                .maxLifeTime(Duration.ofMillis(8_000L))
                .metrics(true)
                .build();
    }
}
