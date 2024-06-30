package com.tusdatos.ds;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.ds.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaunchRequestDS {

    @JsonProperty("doc")
    private String documentNumber;
    @JsonProperty("typedoc")
    private DocumentType documentType;
    private String name;
    @JsonProperty("fechaE")
    private String expeditionDate;
    private boolean force;

}
