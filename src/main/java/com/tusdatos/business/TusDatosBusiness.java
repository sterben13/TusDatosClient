package com.tusdatos.business;

import com.tusdatos.client.WebClientTusDatos;
import com.tusdatos.ds.request.LaunchRequestDS;
import com.tusdatos.ds.response.LaunchResponseDS;
import com.tusdatos.ds.response.ResultResponseDS;
import com.tusdatos.ds.response.TusDatosResponseDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TusDatosBusiness {

    @Autowired
    private WebClientTusDatos webClientTusDatos;

    public ResponseEntity<TusDatosResponseDS> processDocument(final LaunchRequestDS launchRequest) {
        var uuid = UUID.randomUUID().toString();
        this.launch(launchRequest).
                flatMap(this::jobStatus).
                flatMap(this::reportJson).
                contextWrite(context -> context.put("UUID", uuid)).
                subscribe(System.out::println);
        return ResponseEntity.status(HttpStatus.ACCEPTED).
                body(this.createResponse(launchRequest, uuid));
    }

    private TusDatosResponseDS createResponse(LaunchRequestDS launchRequest, String uuid) {
        var response = new TusDatosResponseDS();
        response.setDocument(launchRequest.getDocumentNumber());
        response.setDocumentType(launchRequest.getDocumentType());
        response.setProcessId(uuid);
        return response;
    }

    public Mono<LaunchResponseDS> launch(final LaunchRequestDS launchRequest) {
        return this.webClientTusDatos.launch(launchRequest);
    }

    public Mono<ResultResponseDS> jobStatus(final LaunchResponseDS launchResponse) {
        return Flux.interval(Duration.ofSeconds(15)).
                concatMap(aLong -> this.webClientTusDatos.jobStatus(launchResponse)).
                takeUntil(resultResponseDS -> "finalizado".equals(resultResponseDS.getState())).
                last();
    }

    public boolean retry(final ResultResponseDS resultResponseDS) {
        return Arrays.stream(resultResponseDS.getErrors()).filter(List.of("europol")::contains)
                .distinct().findAny().isPresent();
    }

    public Mono<String> reportJson(final ResultResponseDS resultResponseDS) {
        return this.webClientTusDatos.reportJson(resultResponseDS);
    }

}