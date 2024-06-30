package com.tusdatos.client;

import com.tusdatos.utils.JsonUtil;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientTemplate {

    private final WebClient webClient;

    public WebClientTemplate(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClientTemplate(WebClient webClient, ExchangeFilterFunction filterFunction) {
        this.webClient = webClient.mutate().filter(filterFunction).build();
    }

    protected <T> Mono<T> get(final String uri, Class<T> response) {
        return this.webClient.mutate().
                baseUrl(uri).
                build().
                get().
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(response);
    }

    protected <T> Mono<T> post(final String uri, Object body, Class<T> response) {
        return this.webClient.mutate().
                baseUrl(uri).
                build().
                post().
                accept(MediaType.APPLICATION_JSON).
                bodyValue(JsonUtil.toJson(body)).
                retrieve().
                bodyToMono(response);
    }

    protected <T> Mono<T> put(final String uri, Object body, Class<T> response) {
        return this.webClient.mutate().
                baseUrl(uri).
                build().
                put().
                accept(MediaType.APPLICATION_JSON).
                bodyValue(JsonUtil.toJson(body)).
                retrieve().
                bodyToMono(response);
    }

    protected <T> Mono<T> delete(final String uri, Class<T> response) {
        return this.webClient.mutate().
                baseUrl(uri).
                build().
                delete().
                accept(MediaType.APPLICATION_JSON).
                retrieve().
                bodyToMono(response);
    }
}
