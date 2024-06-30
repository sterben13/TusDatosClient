package com.tusdatos.controller;

import com.tusdatos.business.TusDatosBusiness;
import com.tusdatos.ds.LaunchRequestDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/public/v1/tusdatos")
public class TusDatosController {

    @Autowired
    private TusDatosBusiness tusDatosBusiness;

    @PostMapping("/launch")
    public ResponseEntity launch(@RequestBody LaunchRequestDS launchRequestDS) {
        return tusDatosBusiness.processDocument(launchRequestDS);
    }
}
