package com.tusdatos.client;

import com.tusdatos.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.Consumer;

@Slf4j
public class WebClientTemplate {

    private final WebClient webClient;

    public WebClientTemplate(WebClient webClient) {
        this.webClient = webClient.mutate().defaultHeaders(setHeaderDefault()).build();
    }

    public WebClientTemplate(WebClient webClient, String baseUrl, ExchangeFilterFunction filterFunction) {
        this.webClient = webClient
                .mutate()
                .baseUrl(baseUrl)
                .filter(filterFunction)
                .defaultHeaders(setHeaderDefault())
                .build();
    }

    protected <T> Mono<T> get(final String uri, Class<T> response) {
        return Mono.deferContextual(this::logContext).flatMap(uuid ->
                this.webClient.
                get().
                uri(uri).
                retrieve().
                bodyToMono(response).
                doOnSuccess(responseBody -> log.info("[{}] Response Body: {}", uuid, JsonUtil.toJson(responseBody)))
        );
    }

    protected <T> Mono<T> post(final String uri, Object body, Class<T> response) {
        return Mono.deferContextual(this::logContext).flatMap(uuid ->
                this.webClient.
                        post().
                        uri(uri).
                        bodyValue(JsonUtil.toJson(body)).
                        retrieve().
                        bodyToMono(response).doOnRequest(l -> log.info("[{}] Request Body: {}", uuid, JsonUtil.toJson(body))).
                        doOnSuccess(responseBody -> log.info("[{}] Response Body: {}", uuid, JsonUtil.toJson(responseBody)))
        );
    }

    protected <T> Mono<T> put(final String uri, Object body, Class<T> response) {
        return this.webClient.
                put().
                uri(uri).
                bodyValue(JsonUtil.toJson(body)).
                retrieve().
                bodyToMono(response);
    }

    protected <T> Mono<T> delete(final String uri, Class<T> response) {
        return this.webClient.
                delete().
                uri(uri).
                retrieve().
                bodyToMono(response);
    }

    protected Consumer<HttpHeaders> setHeaderDefault() {
        return httpHeaders -> new HttpHeaders();
    }

    private Mono<String> logContext(ContextView contextView) {
        String uuid = contextView.get("UUID");
        return Mono.just(uuid);
    }
}
