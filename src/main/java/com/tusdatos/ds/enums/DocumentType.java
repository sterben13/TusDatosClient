package com.tusdatos.ds.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    CC, CE, NIT;

    @JsonValue
    public String getName() {
        return this.name();
    }
}
