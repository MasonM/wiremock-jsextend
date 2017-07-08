package com.github.masonm.wiremock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsExtendCreateExtensionSpec {
    private final String name;
    private final String type;
    private final String javascript;

    @JsonCreator
    public JsExtendCreateExtensionSpec(
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("javascript") String javascript
    ) {
        this.name = name;
        this.type = type;
        this.javascript = javascript;
    }

    public String getName() { return name; }

    public String getType() { return type; }

    public String getJavascript() { return javascript; }
}
