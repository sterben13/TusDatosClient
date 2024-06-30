package com.tusdatos.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.
                builder().
                clientConnector(new ReactorClientHttpConnector(this.httpClient())).
                build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .registerModule(new JavaTimeModule());
    }

    private HttpClient httpClient() {
        return HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000).
                responseTimeout(Duration.ofMillis(30_000)).doOnConnected(
                        connection -> connection.addHandlerLast(new ReadTimeoutHandler(30_000, TimeUnit.MILLISECONDS)).
                                addHandlerLast(new WriteTimeoutHandler(30_000, TimeUnit.MILLISECONDS))
                );
    }
}
