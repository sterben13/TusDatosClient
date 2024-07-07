package com.tusdatos.ds.response;

import com.tusdatos.ds.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TusDatosResponseDS {

    private String processId;
    private String document;
    private DocumentType documentType;

}
