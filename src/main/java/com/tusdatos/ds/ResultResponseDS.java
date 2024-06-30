package com.tusdatos.ds;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusdatos.ds.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class ResultResponseDS {
    @JsonProperty
    @JsonAlias("cedula")
    private String documentNumber;
    @JsonProperty
    private boolean error;
    @JsonProperty
    @JsonAlias("errores")
    private String[] errors;
    @JsonProperty
    @JsonAlias("estado")
    private String state;
    @JsonProperty
    @JsonAlias("hallazgo")
    private boolean finding;
    @JsonProperty
    @JsonAlias("hallazgos")
    private String findings;
    @JsonProperty
    private String id;
    @JsonProperty
    private Integer time;
    @JsonProperty
    @JsonAlias("typedoc")
    private DocumentType documentType;
    @JsonProperty
    @JsonAlias("validado")
    private boolean validated;

    @Override
    public String toString() {
        return "ResultResponseDS{" +
                "documentNumber='" + documentNumber + '\'' +
                ", error=" + error +
                ", errors=" + Arrays.toString(errors) +
                ", state='" + state + '\'' +
                ", finding=" + finding +
                ", findings='" + findings + '\'' +
                ", id='" + id + '\'' +
                ", time=" + time +
                ", documentType=" + documentType +
                ", validated=" + validated +
                '}';
    }
}
