package com.tusdatos.ds.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.ds.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaunchResponseDS {

    @JsonProperty
    private String email;
    @JsonProperty
    @JsonAlias("doc")
    private String document;
    @JsonProperty
    @JsonAlias("jobid")
    private String jobId;
    @JsonProperty
    @JsonAlias("nombre")
    private String name;
    @JsonProperty
    @JsonAlias("typedoc")
    private DocumentType documentType;
    @JsonProperty
    @JsonAlias("validado")
    private boolean validated;

    @Override
    public String toString() {
        return "LaunchResponseDS{" +
                "email='" + email + '\'' +
                ", document='" + document + '\'' +
                ", jobId='" + jobId + '\'' +
                ", name='" + name + '\'' +
                ", documentType=" + documentType +
                ", validated=" + validated +
                '}';
    }
}
