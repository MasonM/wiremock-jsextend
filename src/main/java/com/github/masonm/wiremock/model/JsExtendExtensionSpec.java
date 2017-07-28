package com.github.masonm.wiremock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class JsExtendExtensionSpec {
    private final String type;
    private final String javascript;
    private UUID id = null;

    @JsonCreator
    public JsExtendExtensionSpec(
        @JsonProperty("type") String type,
        @JsonProperty("javascript") String javascript
    ) {
        this.type = type;
        this.javascript = javascript;
    }

    public String getType() {
        return type;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
