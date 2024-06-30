package com.tusdatos.business;

import com.tusdatos.client.WebClientTusDatos;
import com.tusdatos.ds.LaunchRequestDS;
import com.tusdatos.ds.LaunchResponseDS;
import com.tusdatos.ds.ResultResponseDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TusDatosBusiness {

    @Autowired
    private WebClientTusDatos webClientTusDatos;

    public ResponseEntity processDocument(final LaunchRequestDS launchRequest){
        this.launch(launchRequest).
                flatMap(this::jobStatus).
                flatMap(this::reportJson).
                subscribe(System.out::println);
        return ResponseEntity.status(HttpStatus.PROCESSING).build();
    }

    public Mono<LaunchResponseDS> launch(final LaunchRequestDS launchRequest) {
        return this.webClientTusDatos.launch(launchRequest).doOnSuccess(System.out::println);
    }

    public Mono<ResultResponseDS> jobStatus(final LaunchResponseDS launchResponse) {
        return Flux.interval(Duration.ofSeconds(15)).
                concatMap(aLong -> this.webClientTusDatos.jobStatus(launchResponse)).
                takeUntil(resultResponseDS -> "finalizado".equals(resultResponseDS.getState())).
                last().doOnSuccess(System.out::println);
    }

    public Mono<String> reportJson(final ResultResponseDS resultResponseDS) {
        return this.webClientTusDatos.reportJson(resultResponseDS);
    }

}