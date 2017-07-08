package com.github.masonm.wiremock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsExtendCreateExtensionSpec {
    private final String type;
    private final String javascript;

    @JsonCreator
    public JsExtendCreateExtensionSpec(
        @JsonProperty("type") String type,
        @JsonProperty("javascript") String javascript
    ) {
        this.type = type;
        this.javascript = javascript;
    }

    public String getType() { return type; }

    public String getJavascript() { return javascript; }
}
