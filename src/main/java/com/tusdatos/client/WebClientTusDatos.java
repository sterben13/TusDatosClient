package com.tusdatos.client;

import com.tusdatos.ds.LaunchRequestDS;
import com.tusdatos.ds.LaunchResponseDS;
import com.tusdatos.ds.ResultResponseDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class WebClientTusDatos extends WebClientTemplate {

    @Value("${webclient.tusdatos.base.uri.lauch:}")
    private String launchUri;

    @Value("${webclient.tusdatos.base.uri.results:}")
    private String resultsUri;

    @Value("${webclient.tusdatos.base.uri.report.json:}")
    private String reportJsonUri;

    @Autowired
    public WebClientTusDatos(final WebClient webClient, @Value("${webclient.tusdatos.user:}") final String user, @Value("${webclient.tusdatos.password:}") final String password) {
        super(webClient, ExchangeFilterFunctions.basicAuthentication(user, password));
    }

    public Mono<LaunchResponseDS> launch(final LaunchRequestDS launchRequest) {
        return super.post(launchUri, launchRequest, LaunchResponseDS.class);
    }

    public Mono<ResultResponseDS> jobStatus(final LaunchResponseDS launchResponse) {
        var uri = UriComponentsBuilder.
                fromUriString(resultsUri).
                encode().
                buildAndExpand(launchResponse.getJobId()).
                toUriString();
        return super.get(uri, ResultResponseDS.class);
    }

    public Mono<String> reportJson(final ResultResponseDS resultResponse) {
        var uri = UriComponentsBuilder.
                fromUriString(reportJsonUri).
                encode().
                buildAndExpand(resultResponse.getId()).
                toUriString();
        return super.get(uri, String.class);
    }
}
